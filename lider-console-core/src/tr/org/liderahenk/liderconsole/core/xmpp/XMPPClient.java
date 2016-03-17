package tr.org.liderahenk.liderconsole.core.xmpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.ui.PlatformUI;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.ldap.LdapUtils;
import tr.org.liderahenk.liderconsole.core.listeners.LdapConnectionListener;

/**
 * XMPP client that is used to read presence info and get task results.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class XMPPClient {

	private static final Logger logger = LoggerFactory.getLogger(XMPPClient.class);

	/**
	 * XMPPClient instance
	 */
	private static XMPPClient instance = null;

	/**
	 * System-wide event broker
	 */
	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	/**
	 * Connection and settings parameters are got from tr.org.liderahenk.cfg
	 */
	private String username;
	private String password;
	private String serviceName; // Service name / XMPP domain
	private String host; // Host name / Server name
	private Integer port; // Default 5222
	private int maxRetryConnectionCount;
	private int maxPingTimeoutCount;
	private int retryCount = 0;
	private int pingTimeoutCount = 0;
	private int packetReplyTimeout; // milliseconds
	private int pingTimeout; // milliseconds

	/**
	 * Connection & packet listeners/filters
	 */
	private XMPPConnectionListener connectionListener;
	private XMPPPingFailedListener pingFailedListener;
	private ChatManagerListenerImpl chatManagerListener;
	private RosterListenerImpl rosterListener;

	private XMPPTCPConnection connection;
	private XMPPTCPConnectionConfiguration config;

	public static synchronized XMPPClient getInstance() {
		if (instance == null) {
			instance = new XMPPClient();
		}
		return instance;
	}

	private XMPPClient() {
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @param serviceName
	 *            (or XMPP domain)
	 * @param host
	 *            (or server name)
	 * @param port
	 */
	public void connect(String userName, String password, String serviceName, String host, int port) {
		logger.info("XMPP service initialization is started");
		setParameters(userName, password, serviceName, host, port);
		createXmppTcpConfiguration(serviceName, host, port);
		connect();
		login();
		setServerSettings();
		addListeners();
		getInitialOnlineUsers();
		logger.info("XMPP service initialized");
	}

	/**
	 * Set XMPP client parameters.
	 * 
	 * @param userName
	 * @param password
	 * @param serviceName
	 * @param host
	 * @param port
	 */
	private void setParameters(String userName, String password, String serviceName, String host, int port) {
		this.username = userName;
		this.password = password;
		this.serviceName = serviceName;
		this.host = host;
		this.port = port;
		this.maxRetryConnectionCount = ConfigProvider.getInstance()
				.getInt(LiderConstants.CONFIG.XMPP_MAX_RETRY_CONN);
		this.maxPingTimeoutCount = ConfigProvider.getInstance().getInt(LiderConstants.CONFIG.XMPP_PING_TIMEOUT);
		this.packetReplyTimeout = ConfigProvider.getInstance().getInt(LiderConstants.CONFIG.XMPP_REPLAY_TIMEOUT);
		this.pingTimeout = ConfigProvider.getInstance().getInt(LiderConstants.CONFIG.XMPP_PING_TIMEOUT);
		logger.debug(this.toString());
	}

	/**
	 * Configure XMPP connection parameters.
	 * 
	 * @param port
	 * @param host
	 * @param serviceName
	 */
	private void createXmppTcpConfiguration(String serviceName, String host, int port) {
		config = XMPPTCPConnectionConfiguration.builder().setServiceName(serviceName).setHost(host).setPort(port)
				.setSecurityMode(SecurityMode.disabled) // TODO SSL Conf.
				.setDebuggerEnabled(logger.isDebugEnabled()).build();
		logger.debug("XMPP configuration finished: {}", config.toString());
	}

	/**
	 * Connect to XMPP server
	 */
	private void connect() {
		connection = new XMPPTCPConnection(config);
		// Retry connection if it fails.
		while (!connection.isConnected() && retryCount < maxRetryConnectionCount) {
			retryCount++;
			try {
				try {
					connection.connect();
				} catch (SmackException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (XMPPException e) {
				logger.error("Cannot connect to XMPP server.");
			}
		}
		retryCount = 0;
		logger.debug("Successfully connected to XMPP server.");
	}

	/**
	 * Login to connected XMPP server via provided username-password.
	 * 
	 * @param username
	 * @param password
	 */
	private void login() {
		if (connection != null && connection.isConnected()) {
			try {
				connection.login(username, password);
				logger.debug("Successfully logged in to XMPP server: {}", username);
			} catch (XMPPException e) {
				logger.error(e.getMessage(), e);
			} catch (SmackException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Configure XMPP connection to use provided ping timeout and reply timeout.
	 */
	private void setServerSettings() {
		PingManager.getInstanceFor(connection).setPingInterval(pingTimeout);
		// Specifies when incoming message delivery receipt requests
		// should be automatically acknowledged with a receipt.
		DeliveryReceiptManager.getInstanceFor(connection).setAutoReceiptMode(AutoReceiptMode.always);
		SmackConfiguration.setDefaultPacketReplyTimeout(packetReplyTimeout);
		logger.debug("Successfully set server settings: {} - {}", new Object[] { pingTimeout, packetReplyTimeout });
	}

	/**
	 * Hook packet and connection listeners
	 */
	private void addListeners() {
		connectionListener = new XMPPConnectionListener();
		pingFailedListener = new XMPPPingFailedListener();
		chatManagerListener = new ChatManagerListenerImpl();
		rosterListener = new RosterListenerImpl();
		connection.addConnectionListener(connectionListener);
		PingManager.getInstanceFor(connection).registerPingFailedListener(pingFailedListener);
		ChatManager.getInstanceFor(connection).addChatListener(chatManagerListener);
		Roster.getInstanceFor(connection).addRosterListener(rosterListener);
		logger.debug("Successfully added listeners for connection: {}", connection.toString());
	}

	/**
	 * Get online users from roster and store in onlineUsers
	 */
	private void getInitialOnlineUsers() {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				List<String> onlineUsers = new ArrayList<String>();
				Roster roster = Roster.getInstanceFor(connection);
				Collection<RosterEntry> entries = roster.getEntries();
				Map<String, String> uidMap = LdapUtils.getInstance().getUidMap(LdapConnectionListener.getConnection(),
						LdapConnectionListener.getMonitor());

				if (entries != null && !entries.isEmpty()) {
					for (RosterEntry entry : entries) {
						String jid = entry.getUser();
						Presence presence = roster.getPresence(jid);
						if (presence != null) {
							XMPPError xmppError = presence.getError();
							if (xmppError != null) {
								logger.error(xmppError.getDescriptiveText());
							} else {
								try {
									String uid = jid.substring(0, jid.indexOf('@'));
									String dn = uidMap.containsKey(uid) ? uidMap.get(uid)
											: LdapUtils.getInstance().findDnByUid(uid,
													LdapConnectionListener.getConnection(),
													LdapConnectionListener.getMonitor());
									if (!dn.equals("")) {
										if (presence.getType() == Type.available) {
											onlineUsers.add(jid.substring(0, jid.indexOf('@')));
											eventBroker.send(LiderConstants.EVENT_TOPICS.ROSTER_ONLINE, dn);
										} else if (presence.getType() == Type.unavailable) {
											onlineUsers.remove(jid.substring(0, jid.indexOf('@')));
											eventBroker.send(LiderConstants.EVENT_TOPICS.ROSTER_OFFLINE, dn);
										}
									}
								} catch (Exception e) {
									logger.error(e.getMessage(), e);
								}
							}
						}
					}
				}
				logger.debug("Online users: {}", onlineUsers.toString());
			}
		});

		thread.start();
	}

	/**
	 * Listen to connection status changes.
	 *
	 */
	class XMPPConnectionListener implements ConnectionListener {

		@Override
		public void connectionClosed() {
			logger.info("XMPP connection was closed.");
			eventBroker.send(LiderConstants.EVENT_TOPICS.XMPP_OFFLINE, "");
		}

		@Override
		public void connectionClosedOnError(Exception e) {
			logger.error("XMPP connection closed with an error", e.getMessage());
			eventBroker.send(LiderConstants.EVENT_TOPICS.XMPP_OFFLINE, "");
		}

		@Override
		public void reconnectingIn(int seconds) {
			logger.info("Reconnecting in {} seconds.", seconds);
			eventBroker.send(LiderConstants.EVENT_TOPICS.XMPP_OFFLINE, "");
		}

		@Override
		public void reconnectionFailed(Exception e) {
			logger.error("Failed to reconnect to the XMPP server.", e.getMessage());
			eventBroker.send(LiderConstants.EVENT_TOPICS.XMPP_OFFLINE, "");
		}

		@Override
		public void reconnectionSuccessful() {
			pingTimeoutCount = 0;
			logger.info("Successfully reconnected to the XMPP server.");
			eventBroker.send(LiderConstants.EVENT_TOPICS.XMPP_ONLINE, "");
		}

		@Override
		public void connected(XMPPConnection connection) {
			logger.info("User: {} connected to XMPP Server {} via port {}",
					new Object[] { connection.getUser(), connection.getHost(), connection.getPort() });
			eventBroker.send(LiderConstants.EVENT_TOPICS.XMPP_ONLINE, "");
		}

		@Override
		public void authenticated(XMPPConnection connection, boolean resumed) {
			logger.info("Connection successfully authenticated.");
			if (resumed) {
				logger.info("A previous XMPP session's stream was resumed");
			}
			eventBroker.send(LiderConstants.EVENT_TOPICS.XMPP_ONLINE, "");
		}
	}

	class XMPPPingFailedListener implements PingFailedListener {
		@Override
		public void pingFailed() {
			pingTimeoutCount++;
			logger.warn("XMPP ping failed: {}", pingTimeoutCount);
			if (pingTimeoutCount > maxPingTimeoutCount) {
				logger.error(
						"Too many consecutive pings failed! This doesn't necessarily mean that the connection is lost.");
				pingTimeoutCount = 0;
			}
		}
	}

	class ChatManagerListenerImpl implements ChatManagerListener {
		@Override
		public void chatCreated(Chat chat, boolean createdLocally) {
			if (createdLocally) {
				logger.info("The chat was created by the local user.");
			}
			chat.addMessageListener(new ChatMessageListener() {
				@Override
				public void processMessage(Chat chat, Message message) {
					// All messages from agents are type normal
					if (!Message.Type.normal.equals(message.getType())) {
						logger.debug("Not a chat message type, will not notify subscribers:  {}", message.getBody());
						return;
					}

					String from = message.getFrom();
					String body = message.getBody();
					logger.debug("Received message from: {}", from);
					logger.debug("Received message body : {}", message.getBody());

					if (null != body && !body.isEmpty()) {
						postTaskStatus(body);
					}
				}
			});
		}
	}

	protected void postTaskStatus(String body) {

		// TODO
		// TODO
		// TODO
		// TODO

		// TaskStatusUpdate taskStatus = null;
		// try {
		// taskStatus = new ObjectMapper().readValue(body,
		// TaskStatusUpdate.class);
		// } catch (IOException e) {
		// logger.error(e.getMessage(), e);
		// }
		//
		// if (taskStatus != null && taskStatus.getPlugin() != null) {
		//
		// // Show task notification
		// Display.getDefault().asyncExec(new Runnable() {
		// @Override
		// public void run() {
		// // TODO notification elden gecirilecek
		// // Notification.TaskNotification(taskStatus.getPlugin());
		// // Notifier.notify("Test", "Test");
		// }
		// });
		//
		// // Notify related plug-in
		// eventBroker.post(taskStatus.getPlugin(), body);
		// eventBroker.post(LiderConstants.EventTopics.TASK, body);
		// }

	}

	/**
	 * Listens to roster presence changes.
	 *
	 */
	class RosterListenerImpl implements RosterListener {

		final Roster roster = Roster.getInstanceFor(connection);

		@Override
		public void entriesAdded(Collection<String> entries) {
			entriesAddedOrUpdated(entries);
		}

		@Override
		public void entriesUpdated(Collection<String> entries) {
			entriesAddedOrUpdated(entries);
		}

		private void entriesAddedOrUpdated(Collection<String> entries) {
			Map<String, String> uidMap = LdapUtils.getInstance().getUidMap(LdapConnectionListener.getConnection(),
					LdapConnectionListener.getMonitor());
			for (String entry : entries) {
				Presence presence = roster.getPresence(entry);
				String jid = entry.substring(0, entry.indexOf('@'));
				String dn = uidMap.containsKey(jid) ? uidMap.get(jid)
						: LdapUtils.getInstance().findDnByUid(jid, LdapConnectionListener.getConnection(),
								LdapConnectionListener.getMonitor());
				if (dn != null && !dn.isEmpty()) {
					if (presence.getType() == Type.available) {
						eventBroker.post(LiderConstants.EVENT_TOPICS.ROSTER_ONLINE, dn);
					} else if (presence.getType() == Type.unavailable) {
						eventBroker.post(LiderConstants.EVENT_TOPICS.ROSTER_OFFLINE, dn);
					}
				}
			}
		}

		@Override
		public void entriesDeleted(Collection<String> entries) {
			Map<String, String> uidMap = LdapUtils.getInstance().getUidMap(LdapConnectionListener.getConnection(),
					LdapConnectionListener.getMonitor());
			for (String entry : entries) {
				String jid = entry.substring(0, entry.indexOf('@'));
				String dn = uidMap.containsKey(jid) ? uidMap.get(jid)
						: LdapUtils.getInstance().findDnByUid(jid, LdapConnectionListener.getConnection(),
								LdapConnectionListener.getMonitor());
				if (dn != null && !dn.isEmpty()) {
					eventBroker.post(LiderConstants.EVENT_TOPICS.ROSTER_OFFLINE, dn);
				}
			}
		}

		@Override
		public void presenceChanged(Presence presence) {

			String jid = presence.getFrom().substring(0, presence.getFrom().indexOf('@'));
			Map<String, String> uidMap = LdapUtils.getInstance().getUidMap(LdapConnectionListener.getConnection(),
					LdapConnectionListener.getMonitor());
			String dn = uidMap.containsKey(jid) ? uidMap.get(jid)
					: LdapUtils.getInstance().findDnByUid(jid, LdapConnectionListener.getConnection(),
							LdapConnectionListener.getMonitor());
			if (dn != null && !dn.isEmpty()) {
				if (presence.getType() == Type.available) {
					eventBroker.post(LiderConstants.EVENT_TOPICS.ROSTER_ONLINE, dn);
				} else if (presence.getType() == Type.unavailable) {
					eventBroker.post(LiderConstants.EVENT_TOPICS.ROSTER_OFFLINE, dn);
				}
			}

			logger.warn("Actual roster presence for {} => {}", roster.getPresence(jid).getFrom(),
					roster.getPresence(jid).toString());
		}
	}

	/**
	 * Disconnect from XMPP server.
	 */
	public void disconnect() {
		logger.debug("Trying to disconnect from XMPP server.");
		if (connection != null && connection.isConnected()) {
			// Remove listeners
			ChatManager.getInstanceFor(connection).removeChatListener(chatManagerListener);
			Roster.getInstanceFor(connection).removeRosterListener(rosterListener);
			connection.removeConnectionListener(connectionListener);
			PingManager.getInstanceFor(connection).setPingInterval(-1);
			connection.disconnect();
			eventBroker.send(LiderConstants.EVENT_TOPICS.XMPP_OFFLINE, "");
			logger.info("Successfully closed XMPP connection.");
		}
	}

	/**
	 * 
	 * @return true if connected to XMPP server, false otherwise
	 */
	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	@Override
	public String toString() {
		return "XMPPClient [username=" + username + ", password=" + password + ", serviceName=" + serviceName
				+ ", host=" + host + ", port=" + port + ", maxRetryConnectionCount=" + maxRetryConnectionCount
				+ ", maxPingTimeoutCount=" + maxPingTimeoutCount + ", retryCount=" + retryCount + ", pingTimeoutCount="
				+ pingTimeoutCount + ", packetReplyTimeout=" + packetReplyTimeout + ", pingTimeout=" + pingTimeout
				+ "]";
	}

}