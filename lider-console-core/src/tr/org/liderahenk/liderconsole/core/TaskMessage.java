package tr.org.liderahenk.liderconsole.core;

import java.util.Date;

/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class TaskMessage {
	private Long timestamp;
	private MessageLevel messageLevel = MessageLevel.INFO;
	private String message;
	
	public TaskMessage() {

	}
	
	public TaskMessage(Long timestamp, MessageLevel messageLevel,
			String message) {
		super();
		this.timestamp = timestamp;
		this.messageLevel = messageLevel;
		this.message = message;
	}
	public TaskMessage(MessageLevel messageLevel,
			String message) {
		this(new Date().getTime(), messageLevel, message);
	}
	
	public TaskMessage(
			String message) {
		this(new Date().getTime(), MessageLevel.INFO, message);
	}
	
	public Date getTimestamp() {
		return new Date(timestamp);
	}

	public MessageLevel getMessageLevel() {
		return messageLevel;
	}

	public String getMessage() {
		return message;
	}
}
