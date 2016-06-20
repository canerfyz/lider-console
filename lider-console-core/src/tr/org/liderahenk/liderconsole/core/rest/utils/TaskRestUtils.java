package tr.org.liderahenk.liderconsole.core.rest.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
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
	 * 
	 * @return base URL for task actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_TASK_BASE_URL));
		return url;
	}

}
