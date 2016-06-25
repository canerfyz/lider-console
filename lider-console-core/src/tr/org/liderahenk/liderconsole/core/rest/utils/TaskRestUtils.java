package tr.org.liderahenk.liderconsole.core.rest.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Command;
import tr.org.liderahenk.liderconsole.core.model.ExecutedTask;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.requests.TaskRequest;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Utility class for sending task related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class TaskRestUtils {

	private static final Logger logger = LoggerFactory.getLogger(TaskRestUtils.class);

	/**
	 * Send POST request to server in order to execute specified task.
	 * 
	 * @param task
	 * @param showNotification
	 * @return
	 * @throws Exception
	 */
	public static IResponse execute(TaskRequest task, boolean showNotification) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/execute");
		logger.debug("Sending request: {} to URL: {}", new Object[] { task, url.toString() });

		if (showNotification) {
			Notifier.success(null, Messages.getString("TASK_SENT"));
		}

		// Send POST request to server
		IResponse response = RestClient.post(task, url.toString());
		if (showNotification) {
			if (response != null && response.getStatus() == RestResponseStatus.OK) {
				Notifier.success(null, Messages.getString("TASK_EXECUTED"));
			} else {
				Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
			}
		}

		return response;
	}

	/**
	 * Convenience method for execute()
	 * 
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public static IResponse execute(TaskRequest task) throws Exception {
		return execute(task, true);
	}

	/**
	 * Convenience method for execute()
	 * 
	 * @param pluginName
	 * @param pluginVersion
	 * @param commandId
	 * @return
	 * @throws Exception
	 */
	public static IResponse execute(String pluginName, String pluginVersion, String commandId) throws Exception {
		TaskRequest task = new TaskRequest(null, null, pluginName, pluginVersion, commandId, null, null, new Date());
		return execute(task);
	}

	/**
	 * Convenience method for execute()
	 * 
	 * @param pluginName
	 * @param pluginVersion
	 * @param commandId
	 * @param showNotification
	 * @return
	 * @throws Exception
	 */
	public static IResponse execute(String pluginName, String pluginVersion, String commandId, boolean showNotification)
			throws Exception {
		TaskRequest task = new TaskRequest(null, null, pluginName, pluginVersion, commandId, null, null, new Date());
		return execute(task, showNotification);
	}

	/**
	 * Send GET request to server in order to retrieve executed tasks.
	 * 
	 * @param pluginName
	 * @param pluginVersion
	 * @param createDateRangeStart
	 * @param createDateRangeEnd
	 * @param status
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static List<ExecutedTask> listExecutedTasks(String pluginName, String pluginVersion,
			Date createDateRangeStart, Date createDateRangeEnd, Integer status, Integer maxResults) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/list/executed?");

		// Append optional parameters
		List<String> params = new ArrayList<String>();
		if (pluginName != null) {
			params.add("pluginName=" + pluginName);
		}
		if (pluginVersion != null) {
			params.add("pluginVersion=" + pluginVersion);
		}
		if (createDateRangeStart != null) {
			params.add("createDateRangeStart=" + createDateRangeStart.getTime());
		}
		if (createDateRangeEnd != null) {
			params.add("createDateRangeEnd=" + createDateRangeEnd.getTime());
		}
		if (status != null) {
			params.add("status=" + status);
		}
		if (maxResults != null) {
			params.add("maxResults=" + maxResults);
		}
		if (!params.isEmpty()) {
			url.append(StringUtils.join(params, "&"));
		}
		logger.debug("Sending request to URL: {}", url.toString());

		// Send GET request to server
		IResponse response = RestClient.get(url.toString());
		List<ExecutedTask> tasks = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("tasks") != null) {
			tasks = new ObjectMapper().readValue(response.getResultMap().get("tasks").toString(),
					new TypeReference<List<ExecutedTask>>() {
					});
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return tasks;
	}

	/**
	 * Send GET request to server in order to retrieve desired command with its
	 * details (command executions and results).
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public static Command getCommand(Long taskId) throws Exception {
		if (taskId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/command/").append(taskId).append("/get");
		logger.debug("Sending request to URL: {}", url.toString());

		IResponse response = RestClient.get(url.toString());
		Command command = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("command") != null) {
			command = new ObjectMapper().readValue(response.getResultMap().get("command").toString(), Command.class);
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return command;
	}

	/**
	 * 
	 * @return base URL for task actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_TASK_BASE_URL));
		return url;
	}

}
