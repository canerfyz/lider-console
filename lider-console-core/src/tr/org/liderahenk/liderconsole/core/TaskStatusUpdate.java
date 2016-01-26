package tr.org.liderahenk.liderconsole.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TaskStatusUpdate {
	private String task;
	private List<TaskMessage> messages = new ArrayList<TaskMessage>(0);
	private TaskState type;
	private Long timestamp;
	private String plugin;
	private HashMap<String, Object> pluginData;
	private String fromJid;
	private String fromDn;
	
	public String getTask() {
		return task;
	}
	
	public void setTask(String task) {
		this.task = task;
	}

	public List<TaskMessage> getMessages() {
		return messages;
	}

	public Date getTimestamp() {
		return new Date(timestamp);
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public HashMap<String, Object> getPluginData() {
		return pluginData;
	}
	
	public void setPluginData(HashMap<String, Object> data) {
		this.pluginData = data;
	}	
	
	public TaskState getType() {
		return type;
	}
	
	public void setType(TaskState type) {
		this.type = type;
	}
	
	
	public String getPlugin() {
		return plugin;
	}
	
	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public String getFromJid() {
		return fromJid;
	}

	public void setFromJid(String fromJid) {
		this.fromJid = fromJid;
	}

	public String getFromDn() {
		return fromDn;
	}

	public void setFromDn(String fromDn) {
		this.fromDn = fromDn;
	}

	
}
