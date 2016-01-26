package tr.org.liderahenk.liderconsole.core.task;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class TaskMessage implements Serializable {

	private static final long serialVersionUID = 743042052638041611L;

	private Long timestamp;
	private MessageLevel messageLevel;
	private String message;

	public Date getTimestamp() {
		return new Date(timestamp);
	}

	public MessageLevel getMessageLevel() {
		return messageLevel;
	}

	public String getMessage() {
		return message;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public void setMessageLevel(MessageLevel messageLevel) {
		this.messageLevel = messageLevel;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
