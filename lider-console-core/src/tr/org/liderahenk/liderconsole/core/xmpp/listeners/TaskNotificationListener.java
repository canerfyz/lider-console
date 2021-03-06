package tr.org.liderahenk.liderconsole.core.xmpp.listeners;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.ui.PlatformUI;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.xmpp.notifications.TaskNotification;

/**
 * Listens to task notifications. When a notification is received, it shows a
 * notification about task and also throws an event to notify plugins.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class TaskNotificationListener implements StanzaListener, StanzaFilter {

	private static Logger logger = LoggerFactory.getLogger(TaskNotificationListener.class);

	/**
	 * Pattern used to filter messages
	 */
	private static final Pattern messagePattern = Pattern.compile(".*\\\"type\\\"\\s*:\\s*\\\"TASK\\\".*",
			Pattern.CASE_INSENSITIVE);

	/**
	 * System-wide event broker
	 */
	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	@Override
	public boolean accept(Stanza stanza) {
		if (stanza instanceof Message) {
			Message msg = (Message) stanza;
			// All messages from agents are type normal
			if (Message.Type.normal.equals(msg.getType()) && messagePattern.matcher(msg.getBody()).matches()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void processPacket(Stanza packet) throws NotConnectedException {
		try {
			if (packet instanceof Message) {

				Message msg = (Message) packet;
				logger.info("Task message received from => {}, body => {}", msg.getFrom(), msg.getBody());

				ObjectMapper mapper = new ObjectMapper();
				mapper.getDeserializationConfig().setDateFormat(new SimpleDateFormat("dd-MM-yyyy HH:mm"));

				final TaskNotification task = mapper.readValue(msg.getBody(), TaskNotification.class);

				// Notify related plug-in
				eventBroker.post(LiderConstants.EVENT_TOPICS.TASK_NOTIFICATION_RECEIVED, task);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
