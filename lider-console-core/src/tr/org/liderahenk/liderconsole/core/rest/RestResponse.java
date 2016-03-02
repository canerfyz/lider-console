package tr.org.liderahenk.liderconsole.core.rest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Rest response object representing a response to {@link RestRequest}
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class RestResponse implements Serializable {

	private static final long serialVersionUID = -6175684844309944880L;

	/**
	 * Contains result status. This is the only status code that can be used for
	 * handling responses.
	 */
	private RestResponseStatus status;

	/**
	 * Name of the plugin which REST request/response belongs to
	 */
	private String pluginName;

	/**
	 * Plugin version number is used to distinguish plugins with multiple
	 * bundles running on Lider Server
	 */
	private String pluginVersion;

	/**
	 * ID of the executed command of the plugin.
	 */
	private String commandId;

	/**
	 * Response messages can be used along with status to notify result.
	 */
	private List<String> messages;

	/**
	 * Contains result parameters which can be used by the plugin (e.g.
	 * displaying results)
	 */
	private Map<String, Object> resultMap;

	/**
	 * array of task ID
	 */
	private String[] tasks;

	public RestResponse() {
	}

	public RestResponse(RestResponseStatus status, String pluginName, String pluginVersion, String commandId,
			List<String> messages, Map<String, Object> resultMap, String[] tasks) {
		super();
		this.status = status;
		this.pluginName = pluginName;
		this.pluginVersion = pluginVersion;
		this.commandId = commandId;
		this.messages = messages;
		this.resultMap = resultMap;
		this.tasks = tasks;
	}

	public RestResponseStatus getStatus() {
		return status;
	}

	public void setStatus(RestResponseStatus status) {
		this.status = status;
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

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public String[] getTasks() {
		return tasks;
	}

	public void setTasks(String[] tasks) {
		this.tasks = tasks;
	}

}
