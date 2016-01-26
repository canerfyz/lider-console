package tr.org.liderahenk.liderconsole.core.rest;

import java.io.Serializable;
import java.util.Map;

public class RequestBody implements Serializable {

	private static final long serialVersionUID = -4228128699587488708L;

	private String dnList;

	private DNType type;

	private String commandId;

	private Map<String, Object> parameterMap;

	private String cronExpression;

	public String getDnList() {
		return dnList;
	}

	public void setDnList(String dnList) {
		this.dnList = dnList;
	}

	public DNType getType() {
		return type;
	}

	public void setType(DNType type) {
		this.type = type;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

}
