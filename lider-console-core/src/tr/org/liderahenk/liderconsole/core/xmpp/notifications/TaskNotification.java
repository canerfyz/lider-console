package tr.org.liderahenk.liderconsole.core.xmpp.notifications;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import tr.org.liderahenk.liderconsole.core.model.Command;
import tr.org.liderahenk.liderconsole.core.xmpp.enums.NotificationType;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskNotification implements Serializable {

	private static final long serialVersionUID = 6231833300301955418L;

	private NotificationType type = NotificationType.TASK;

	private Command command;

	private Date timestamp;

	public TaskNotification() {
	}

	public TaskNotification(Command command, Date timestamp) {
		this.command = command;
		this.timestamp = timestamp;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
