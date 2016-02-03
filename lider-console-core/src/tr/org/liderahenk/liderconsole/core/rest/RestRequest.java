package tr.org.liderahenk.liderconsole.core.rest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Request object which is used to carry plugin parameters and designates which
 * LDAP entries to process.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class RestRequest implements Serializable {

	private static final long serialVersionUID = -6738563432268263799L;

	/**
	 * Contains DN entries which are subject to task execution.
	 */
	private List<String> dnList;

	/**
	 * This type indicates what kind of DN entries to consider when executing
	 * tasks. (For example DN list may consists of some OU groups and user may
	 * only want to execute a task on user DN's inside these groups.)
	 */
	private RestDNType dnType;

	/**
	 * Name of the plugin which executes the task.
	 */
	private String pluginName;

	/**
	 * Version number of the plugin which executes the task.
	 */
	private String pluginVersion;

	/**
	 * Command ID is a unique value in the target plugin that is used to
	 * distinguish an ICommand class from others.
	 */
	private String commandId;

	/**
	 * Custom parameter map that can be used by the plugin.
	 */
	private Map<String, Object> parameterMap;

	/**
	 * If cron expression is not null or empty, then task will be scheduled on
	 * the agent.
	 */
	private String cronExpression;

	/**
	 * Priority indicates how important a task compared to others.
	 */
	private Priority priority;

	public List<String> getDnList() {
		return dnList;
	}

	public void setDnList(List<String> dnList) {
		this.dnList = dnList;
	}

	public RestDNType getDnType() {
		return dnType;
	}

	public void setDnType(RestDNType dnType) {
		this.dnType = dnType;
	}

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

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

}
