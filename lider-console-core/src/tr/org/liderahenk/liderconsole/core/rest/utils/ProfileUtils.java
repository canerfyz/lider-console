package tr.org.liderahenk.liderconsole.core.rest.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.requests.ProfileRequest;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;

/**
 * Utility class for sending profile related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ProfileUtils {

	private static final Logger logger = LoggerFactory.getLogger(ProfileUtils.class);

	// TODO execute method!

	/**
	 * Send POST request to server in order to save specified profile.
	 * 
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public static IResponse add(ProfileRequest profile) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/add");
		logger.debug("Sending request: {} to URL: {}", new Object[] { profile, url.toString() });

		// Send POST request to server
		return RestClient.post(profile, url.toString());
	}

	/**
	 * Send POST request to server in order to update specified profile.
	 * 
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public static IResponse update(ProfileRequest profile) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/update");
		logger.debug("Sending request: {} to URL: {}", new Object[] { profile, url.toString() });

		return RestClient.post(profile, url.toString());
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

		if (pluginName == null || pluginName.isEmpty()) {
			throw new IllegalArgumentException("Plugin name was null.");
		}
		if (pluginVersion == null || pluginVersion.isEmpty()) {
			throw new IllegalArgumentException("Plugin version was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/list?pluginName=").append(pluginName).append("&pluginVersion=").append(pluginVersion);
		logger.debug("Sending request to URL: {}", url.toString());

		// Append optional parameters
		if (label != null) {
			url.append("&label=").append(label);
		}
		if (active != null) {
			url.append("&active=").append(active.booleanValue());
		}

		// Send GET request to server
		return RestClient.get(url.toString());
	}

	/**
	 * Send GET request to server in order to retrieve desired profile.
	 * 
	 * @param profileId
	 * @return
	 * @throws Exception
	 */
	public static IResponse get(Long profileId) throws Exception {

		if (profileId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(profileId).append("/get");
		logger.debug("Sending request to URL: {}", url.toString());

		return RestClient.get(url.toString());
	}

	/**
	 * Send GET request to server in order to delete desired profile.
	 * 
	 * @param profileId
	 * @return
	 * @throws Exception
	 */
	public static IResponse delete(Long profileId) throws Exception {

		if (profileId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(profileId).append("/delete");
		logger.debug("Sending request to URL: {}", url.toString());

		return RestClient.get(url.toString());
	}

	/**
	 * 
	 * @return base URL for profile actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_PROFILE_BASE_URL));
		return url;
	}

}
