package tr.org.liderahenk.liderconsole.core.rest.utils;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Policy;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.requests.PolicyExecutionRequest;
import tr.org.liderahenk.liderconsole.core.rest.requests.PolicyRequest;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Utility class for sending policy related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class PolicyRestUtils {

	private static final Logger logger = LoggerFactory.getLogger(PolicyRestUtils.class);

	/**
	 * Send POST request to server in order to execute policy (Policy does not
	 * really executed at this step, it is just saved. The policies are actually
	 * executed on related user login).
	 * 
	 * @param policy
	 * @return
	 * @throws Exception
	 */
	public static boolean execute(PolicyExecutionRequest policy) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/execute");
		logger.debug("Sending request: {} to URL: {}", new Object[] { policy, url.toString() });

		// Send POST request to server
		IResponse response = RestClient.post(policy, url.toString());

		if (response != null && response.getStatus() == RestResponseStatus.OK) {
			Notifier.success(null, Messages.getString("POLICY_APPLIED"));
			return true;
		}

		Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
		return false;
	}

	/**
	 * Send POST request to server in order to save specified policy.
	 * 
	 * @param policy
	 * @return
	 * @throws Exception
	 */
	public static Policy add(PolicyRequest policy) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/add");
		logger.debug("Sending request: {} to URL: {}", new Object[] { policy, url.toString() });

		// Send POST request to server
		IResponse response = RestClient.post(policy, url.toString());
		Policy result = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("policy") != null) {
			result = new ObjectMapper().readValue(response.getResultMap().get("policy").toString(), Policy.class);
			Notifier.success(null, Messages.getString("RECORD_SAVED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_SAVE"));
		}

		return result;
	}

	/**
	 * Send POST request to server in order to update specified policy.
	 * 
	 * @param policy
	 * @return
	 * @throws Exception
	 */
	public static Policy update(PolicyRequest policy) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/update");
		logger.debug("Sending request: {} to URL: {}", new Object[] { policy, url.toString() });

		IResponse response = RestClient.post(policy, url.toString());
		Policy result = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("policy") != null) {
			result = new ObjectMapper().readValue(response.getResultMap().get("policy").toString(), Policy.class);
			Notifier.success(null, Messages.getString("RECORD_SAVED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_SAVE"));
		}

		return result;
	}

	/**
	 * Send GET request to server in order to retrieve desired policies.
	 * 
	 * @param pluginName
	 * @param pluginVersion
	 * @param label
	 * @param active
	 * @return
	 * @throws Exception
	 */
	public static List<Policy> list(String label, Boolean active) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/list?");

		// Append optional parameters
		boolean useAmbersand = false;
		if (label != null) {
			useAmbersand = true;
			url.append("label=").append(label);
		}
		if (active != null) {
			url.append(useAmbersand ? "&" : "").append("active=").append(active.booleanValue());
		}
		logger.debug("Sending request to URL: {}", url.toString());

		// Send GET request to server
		IResponse response = RestClient.get(url.toString());
		List<Policy> policies = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("policies") != null) {
			policies = new ObjectMapper().readValue(response.getResultMap().get("policies").toString(),
					new TypeReference<List<Policy>>() {
					});
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return policies;
	}

	/**
	 * Send GET request to server in order to retrieve desired policy.
	 * 
	 * @param policyId
	 * @return
	 * @throws Exception
	 */
	public static Policy get(Long policyId) throws Exception {

		if (policyId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(policyId).append("/get");
		logger.debug("Sending request to URL: {}", url.toString());

		IResponse response = RestClient.get(url.toString());
		Policy policy = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("policy") != null) {
			policy = new ObjectMapper().readValue(response.getResultMap().get("policy").toString(), Policy.class);
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return policy;
	}

	/**
	 * Send GET request to server in order to delete desired policy.
	 * 
	 * @param policyId
	 * @return
	 * @throws Exception
	 */
	public static boolean delete(Long policyId) throws Exception {

		if (policyId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(policyId).append("/delete");
		logger.debug("Sending request to URL: {}", url.toString());

		IResponse response = RestClient.get(url.toString());

		if (response != null && response.getStatus() == RestResponseStatus.OK) {
			Notifier.error(null, Messages.getString("RECORD_DELETED"));
			return true;
		}

		Notifier.error(null, Messages.getString("ERROR_ON_DELETE"));
		return false;
	}

	/**
	 * 
	 * @return base URL for policy actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_POLICY_BASE_URL));
		return url;
	}

}
