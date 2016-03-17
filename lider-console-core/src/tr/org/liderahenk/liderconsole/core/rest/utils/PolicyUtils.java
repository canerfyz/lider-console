package tr.org.liderahenk.liderconsole.core.rest.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.requests.PolicyRequest;
import tr.org.liderahenk.liderconsole.core.rest.requests.ProfileRequest;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;

/**
 * Utility class for sending policy related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class PolicyUtils {

	private static final Logger logger = LoggerFactory.getLogger(PolicyUtils.class);

	// TODO execute method!

	/**
	 * Send POST request to server in order to save specified policy.
	 * 
	 * @param policy
	 * @return
	 * @throws Exception
	 */
	public static IResponse add(PolicyRequest policy) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/add");
		logger.debug("Sending request: {} to URL: {}", new Object[] { policy, url.toString() });

		// Send POST request to server
		return RestClient.post(policy, url.toString());
	}

	/**
	 * Send POST request to server in order to update specified policy.
	 * 
	 * @param policy
	 * @return
	 * @throws Exception
	 */
	public static IResponse update(ProfileRequest policy) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/update");
		logger.debug("Sending request: {} to URL: {}", new Object[] { policy, url.toString() });

		return RestClient.post(policy, url.toString());
	}

	/**
	 * Send GET request to server in order to retrieve desired profiles.
	 * 
	 * @param pluginName
	 * @param pluginVersion
	 * @param label
	 * @param active
	 * @return
	 * @throws Exception
	 */
	public static IResponse list(String pluginName, String pluginVersion, String label, Boolean active)
			throws Exception {

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
		return RestClient.get(url.toString());
	}

	/**
	 * Send GET request to server in order to retrieve desired policy.
	 * 
	 * @param policyId
	 * @return
	 * @throws Exception
	 */
	public static IResponse get(Long policyId) throws Exception {

		if (policyId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(policyId).append("/get");
		logger.debug("Sending request to URL: {}", url.toString());

		return RestClient.get(url.toString());
	}

	/**
	 * Send GET request to server in order to delete desired policy.
	 * 
	 * @param policyId
	 * @return
	 * @throws Exception
	 */
	public static IResponse delete(Long policyId) throws Exception {

		if (policyId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(policyId).append("/delete");
		logger.debug("Sending request to URL: {}", url.toString());

		return RestClient.get(url.toString());
	}

	/**
	 * 
	 * @return base URL for policy actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG_PROPS.POLICY_BASE_URL));
		return url;
	}

}
