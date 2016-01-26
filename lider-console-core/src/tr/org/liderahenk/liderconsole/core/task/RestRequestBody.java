package tr.org.liderahenk.liderconsole.core.task;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class RestRequestBody {
	
	private String pluginId;
	private String pluginVersion;
	private String clientId;
	private String clientVersion;
	private Map<String, Object> customParameterMap = new HashMap<String, Object>(0);
	private String requestId;
	private boolean scheduled;
	private ScheduleRequest scheduleRequest;
	private Integer priority;	

	public String getPluginId() {
		return pluginId;
	}
	
	public String getPluginVersion() {
		return pluginVersion;
	}
	
	public String getClientId() {
		return clientId;
	}
	
	public String getClientVersion() {
		return clientVersion;
	}
	
	public Map<String, Object> getCustomParameterMap() {
		return customParameterMap;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public ScheduleRequest getScheduleRequest() {
		return scheduleRequest;
	}
	
	public boolean isScheduled() {
		return scheduled;
	}
		
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public void setCustomParameterMap(Map<String, Object> customParameterMap) {
		this.customParameterMap = customParameterMap;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public void setScheduleRequest(ScheduleRequest scheduleRequest) {
		this.scheduleRequest =  scheduleRequest;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
