package tr.org.liderahenk.liderconsole.core.rest.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.OperationLog;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Utility class for sending operation log related requests to Lider server.
 * 
 * @author <a href="mailto:cemre.alpsoy@agem.com.tr">Cemre ALPSOY</a>
 *
 */
public class OperationLogRestUtils {

	private static final Logger logger = LoggerFactory.getLogger(OperationLogRestUtils.class);

	/**
	 * Send GET request to server in order to retrieve desired operation logs.
	 * 
	 * @param logMessage
	 * @param requestIp
	 * @return
	 * @throws Exception
	 */
	public static List<OperationLog> list(String logMessage, String requestIp) throws Exception {
		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/list?");

		// Append optional parameters
		List<String> params = new ArrayList<String>();
		if (logMessage != null) {
			params.add("logMessage=" + logMessage);
		}
		if (requestIp != null) {
			params.add("requestIp=" + requestIp);
		}
		if (!params.isEmpty()) {
			url.append(StringUtils.join(params, "&"));
		}
		logger.debug("Sending request to URL: {}", url.toString());

		// Send GET request to server
		IResponse response = RestClient.get(url.toString());
		List<OperationLog> operationLogs = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("operationLogs") != null) {
			ObjectMapper mapper = new ObjectMapper();
			operationLogs = mapper.readValue(mapper.writeValueAsString(response.getResultMap().get("operationLogs")),
					new TypeReference<List<OperationLog>>() {
					});
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return operationLogs;
	}

	/**
	 * Send GET request to server in order to retrieve desired operation log.
	 * 
	 * @param operationLogId
	 * @return
	 * @throws Exception
	 */
	public static OperationLog get(Long operationLogId) throws Exception {
		if (operationLogId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(operationLogId).append("/get");
		logger.debug("Sending request to URL: {}", url.toString());

		IResponse response = RestClient.get(url.toString());
		OperationLog operationLog = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("operationLog") != null) {
			ObjectMapper mapper = new ObjectMapper();
			operationLog = mapper.readValue(mapper.writeValueAsString(response.getResultMap().get("operationLog")), OperationLog.class);
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return operationLog;
	}

	/**
	 * 
	 * @return base URL for operation log actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_OPERATION_LOG_BASE_URL));
		return url;
	}

}
