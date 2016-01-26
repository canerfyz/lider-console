package tr.org.liderahenk.liderconsole.core.xmpp;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
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
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import tr.org.liderahenk.liderconsole.core.TaskStatusUpdate;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.ldap.LdapUtils;
import tr.org.liderahenk.liderconsole.core.listeners.LdapConnectionListener;

public class XMPPClient {
	
	private static final Logger logger = LoggerFactory.getLogger(XMPPClient.class);
	
	/**
	 * 
	 */
	private static XMPPClient instance = null;

	/**
	 * 
	 */
	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	/**
	 * 
	 */
	AbstractXMPPConnection connection;
	
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
		
		logger.debug("Trying to connect to XMPP server: {0} with user: {1}", new Object[]{ host, userName });

		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				.setUsernameAndPassword(userName, password).setServiceName(serviceName).setHost(host).setPort(port)
				.setSecurityMode(SecurityMode.disabled).build();

		connection = new XMPPTCPConnection(config);
		connection.setPacketReplyTimeout(10000); // TODO read from properties file.

		try {
			connection.connect().login();
			if (connection.isConnected() && connection.isAuthenticated()) {
				
				logger.debug("Connected to XMPP server.");

				// Register connection listener
				addConnectionListener();

				// Register presence listener
				findInitialPresences();
				addPresenceListener();

				// Register message listener
				addMessageListener();
			}
		} catch (XMPPException e) {
			logger.error(e.toString(), e);
		} catch (SmackException e) {
			logger.error(e.toString(), e);
		} catch (IOException e) {
			logger.error(e.toString(), e);
		}
	}

	private void addMessageListener() {
		ChatManager chatManager = ChatManager.getInstanceFor(connection);
		chatManager.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				chat.addMessageListener(new ChatMessageListener() {
					@Override
					public void processMessage(Chat chat, Message message) {
						if (message.getType() == Message.Type.error) {
							return;
						}

						String body = message.getBody();
						if (body != null && !body.isEmpty()) {
							postTaskStatus(body);
						}
					}
				});
			}
		});
	}

	protected void postTaskStatus(String body) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls().create();
		final TaskStatusUpdate taskStatus = (TaskStatusUpdate) gson.fromJson(body, new TypeToken<TaskStatusUpdate>() {
		}.getType());

		if (taskStatus != null && taskStatus.getPlugin() != null) {

			// Show task notification
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					// TODO notification elden gecirilecek
					// Notification.TaskNotification(taskStatus.getPlugin());
//					Notifier.notify("Test", "Test");
				}
			});

			// Notify related plug-in
			eventBroker.post(taskStatus.getPlugin(), body);
			eventBroker.post(LiderConstants.EventTopics.TASK, body);
		}

	}

	/**
	 * 
	 */
	public void findInitialPresences() {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
//				Roster roster = Roster.getInstanceFor(connection);
//				Collection<RosterEntry> entries = roster.getEntries();
//				Map<String, String> uidMap = LdapUtils.getInstance().getUidMap(LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
//
//				for (RosterEntry r : entries) {
//					String user = r.getUser();
//					Presence presence = roster.getPresence(user);
//					XMPPError xmppError = presence.getError();
//					if (xmppError != null) {
//						// TODO system-wide error msg
//					} else {
//						String uid = user.substring(0, user.indexOf('@'));
//						String dn = uidMap.containsKey(uid) ? uidMap.get(uid) : LdapUtils.getInstance().findDnByUid(uid, LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
//						if (!dn.equals("")) {
//							if (presence.getType() == Type.available) {
//								// TODO system-wide log msg
//								eventBroker.send(LiderConstants.EventTopics.ROSTER_ONLINE, dn);
//							} else if (presence.getType() == Type.unavailable) {
//								// TODO system-wide log msg
//								eventBroker.send(LiderConstants.EventTopics.ROSTER_OFFLINE, dn);
//							}
//						}
//					}
//				}
			}
		});

		thread.start();
	}

	/**
	 * 
	 */
	private void addPresenceListener() {
		final Roster roster = Roster.getInstanceFor(connection);
		roster.addRosterListener(new RosterListener() {

			@Override
			public void entriesAdded(Collection<String> entries) {
				entriesAddedOrUpdated(entries);
			}

			@Override
			public void entriesUpdated(Collection<String> entries) {
				entriesAddedOrUpdated(entries);
			}

			private void entriesAddedOrUpdated(Collection<String> entries) {
				Map<String, String> uidMap = LdapUtils.getInstance().getUidMap(LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
				for (String entry : entries) {
					Presence presence = roster.getPresence(entry);
					String jid = entry.substring(0, entry.indexOf('@'));
					String dn = uidMap.containsKey(jid) ? uidMap.get(jid) : LdapUtils.getInstance().findDnByUid(jid, LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
					if (dn != null && !dn.isEmpty()) {
						if (presence.getType() == Type.available) {
							eventBroker.send(LiderConstants.EventTopics.ROSTER_ONLINE, dn);
						} else if (presence.getType() == Type.unavailable) {
							eventBroker.send(LiderConstants.EventTopics.ROSTER_OFFLINE, dn);
						}
					}
				}
			}

			@Override
			public void entriesDeleted(Collection<String> entries) {
				Map<String, String> uidMap = LdapUtils.getInstance().getUidMap(LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
				for (String entry : entries) {
					String jid = entry.substring(0, entry.indexOf('@'));
					String dn = uidMap.containsKey(jid) ? uidMap.get(jid) : LdapUtils.getInstance().findDnByUid(jid, LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
					if (dn != null && !dn.isEmpty()) {
						eventBroker.send("roster_offline", dn);
					}
				}
			}

			@Override
			public void presenceChanged(Presence presence) {
				String jid = presence.getFrom().substring(0, presence.getFrom().indexOf('@'));
				Map<String, String> uidMap = LdapUtils.getInstance().getUidMap(LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
				String dn = uidMap.containsKey(jid) ? uidMap.get(jid) : LdapUtils.getInstance().findDnByUid(jid, LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
				if (dn != null && !dn.isEmpty()) {
					if (presence.getType() == Type.available) {
						eventBroker.send(LiderConstants.EventTopics.ROSTER_ONLINE, dn);
					} else if (presence.getType() == Type.unavailable) {
						eventBroker.send(LiderConstants.EventTopics.ROSTER_OFFLINE, dn);
					}
				}
			}

		});
	}

	/**
	 * 
	 */
	private void addConnectionListener() {
		connection.addConnectionListener(new ConnectionListener() {
			@Override
			public void connectionClosed() {
				eventBroker.send(LiderConstants.EventTopics.XMPP_OFFLINE, "");
			}

			@Override
			public void connectionClosedOnError(Exception arg0) {
				eventBroker.send(LiderConstants.EventTopics.XMPP_OFFLINE, "");
			}

			@Override
			public void reconnectingIn(int seconds) {
				eventBroker.send(LiderConstants.EventTopics.XMPP_OFFLINE, "");
			}

			@Override
			public void reconnectionFailed(Exception e) {
				eventBroker.send(LiderConstants.EventTopics.XMPP_OFFLINE, "");
			}

			@Override
			public void reconnectionSuccessful() {
				eventBroker.send(LiderConstants.EventTopics.XMPP_ONLINE, "");
			}

			@Override
			public void authenticated(XMPPConnection conn, boolean arg1) {
				eventBroker.send(LiderConstants.EventTopics.XMPP_ONLINE, "");
			}

			@Override
			public void connected(XMPPConnection conn) {
				eventBroker.send(LiderConstants.EventTopics.XMPP_ONLINE, "");
			}
		});
	}

	/**
	 * 
	 */
	public void disconnect() {
		logger.debug("Trying to disconnect from XMPP server.");
		if (connection != null && connection.isConnected()) {
			connection.disconnect();
			eventBroker.send(LiderConstants.EventTopics.XMPP_OFFLINE, "");
		}
		logger.debug("Disconnected from XMPP server.");
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}

}