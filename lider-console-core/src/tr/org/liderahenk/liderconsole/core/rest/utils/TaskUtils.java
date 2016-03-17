package tr.org.liderahenk.liderconsole.core.rest.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.requests.TaskRequest;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;

/**
 * Utility class for sending task related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class TaskUtils {

	private static final Logger logger = LoggerFactory.getLogger(TaskUtils.class);

	/**
	 * Send POST request to server in order to execute specified task.
	 * 
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public static IResponse execute(TaskRequest task) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/execute");
		logger.debug("Sending request: {} to URL: {}", new Object[] { task, url.toString() });

		// Send POST request to server
		return RestClient.post(task, url.toString());
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
	 * 
	 * @return base URL for task actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG_PROPS.TASK_BASE_URL));
		return url;
	}

}
