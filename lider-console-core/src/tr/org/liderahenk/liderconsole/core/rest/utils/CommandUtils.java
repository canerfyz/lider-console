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
import tr.org.liderahenk.liderconsole.core.model.ExecutedPolicy;
import tr.org.liderahenk.liderconsole.core.model.ExecutedTask;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Utility class for sending command related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class CommandUtils {

	private static final Logger logger = LoggerFactory.getLogger(CommandUtils.class);

	/**
	 * Send GET request to server in order to retrieve desired executed tasks.
	 * 
	 * @throws Exception
	 * 
	 */
	public static List<ExecutedTask> listExecutedTasks(String pluginName, String pluginVersion,
			Date createDateRangeStart, Date createDateRangeEnd, Integer status) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/task/list?");

		// Append optional parameters
		List<String> params = new ArrayList<String>();
		if (pluginName != null) {
			params.add("pluginName=" + pluginName);
		}
		if (pluginVersion != null) {
			params.add("pluginVersion=" + pluginVersion);
		}
		if (createDateRangeStart != null) {
			params.add("createDateRangeStart=" + createDateRangeStart);
		}
		if (createDateRangeEnd != null) {
			params.add("createDateRangeEnd=" + createDateRangeEnd);
		}
		if (status != null) {
			params.add("status=" + status);
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
	 * Send GET request to server in order to retrieve desired task command with
	 * its details (command executions and results).
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public static Command getTaskCommand(Long taskId) throws Exception {
		if (taskId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/task/").append(taskId).append("/get");
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
	 * Send GET request to server in order to retrieve desired executed
	 * policies.
	 * 
	 * @throws Exception
	 * 
	 */
	public static List<ExecutedPolicy> listExecutedPolicies(String label, Date createDateRangeStart,
			Date createDateRangeEnd, Integer status) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/policy/list?");

		// Append optional parameters
		List<String> params = new ArrayList<String>();
		if (label != null) {
			params.add("label=" + label);
		}
		if (createDateRangeStart != null) {
			params.add("createDateRangeStart=" + createDateRangeStart);
		}
		if (createDateRangeEnd != null) {
			params.add("createDateRangeEnd=" + createDateRangeEnd);
		}
		if (status != null) {
			params.add("status=" + status);
		}
		if (!params.isEmpty()) {
			url.append(StringUtils.join(params, "&"));
		}
		logger.debug("Sending request to URL: {}", url.toString());

		// Send GET request to server
		IResponse response = RestClient.get(url.toString());
		List<ExecutedPolicy> policies = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("policies") != null) {
			policies = new ObjectMapper().readValue(response.getResultMap().get("policies").toString(),
					new TypeReference<List<ExecutedPolicy>>() {
					});
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return policies;
	}

	/**
	 * Send GET request to server in order to retrieve desired task command with
	 * its details (command executions and results).
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public static Command getPolicyCommand(Long policyId) throws Exception {
		if (policyId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/policy/").append(policyId).append("/get");
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
	 * @return base URL for command actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_COMMAND_BASE_URL));
		return url;
	}

}
