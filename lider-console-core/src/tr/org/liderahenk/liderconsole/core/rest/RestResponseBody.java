package tr.org.liderahenk.liderconsole.core.rest;

import java.io.Serializable;
import java.util.Map;

public class RestResponseBody implements Serializable{

	private static final long serialVersionUID = 5654004394449770661L;

	private String pluginName;
	private String pluginVersion;
	private Map<String, Object> resultMap;
	private String requestId;
	private String[] tasks;
	
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public String getPluginVersion() {
		return pluginVersion;
	}
	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}
	public Map<String, Object> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String[] getTasks() {
		return tasks;
	}
	public void setTasks(String[] tasks) {
		this.tasks = tasks;
	}
	public RestResponseBody(String pluginName, String pluginVersion, Map<String, Object> resultMap, String requestId,
			String[] tasks) {
		super();
		this.pluginName = pluginName;
		this.pluginVersion = pluginVersion;
		this.resultMap = resultMap;
		this.requestId = requestId;
		this.tasks = tasks;
	}
}
