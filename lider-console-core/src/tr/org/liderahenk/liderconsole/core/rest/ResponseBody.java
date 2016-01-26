package tr.org.liderahenk.liderconsole.core.rest;

import java.util.Map;

public class ResponseBody {

	private String pluginId;
	private String pluginVersion;
	private Map<String, Object> resultMap;
	private String resultMapString;	
	private String requestId;
	
	public String getPluginId() {
		return pluginId;
	}
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
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
	public String getResultMapString() {
		return resultMapString;
	}
	public void setResultMapString(String resultMapString) {
		this.resultMapString = resultMapString;
	}	
}
