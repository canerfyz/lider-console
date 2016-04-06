package tr.org.liderahenk.liderconsole.core.rest.utils;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Profile;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.requests.ProfileRequest;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Utility class for sending profile related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ProfileUtils {

	private static final Logger logger = LoggerFactory.getLogger(ProfileUtils.class);

	/**
	 * Send POST request to server in order to save specified profile.
	 * 
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public static Profile add(ProfileRequest profile) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/add");
		logger.debug("Sending request: {} to URL: {}", new Object[] { profile, url.toString() });

		// Send POST request to server
		IResponse response = RestClient.post(profile, url.toString());
		Profile result = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("profile") != null) {
			result = new ObjectMapper().readValue(response.getResultMap().get("profile").toString(), Profile.class);
			Notifier.success(null, Messages.getString("RECORD_SAVED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_SAVE"));
		}

		return result;
	}

	/**
	 * Send POST request to server in order to update specified profile.
	 * 
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public static Profile update(ProfileRequest profile) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/update");
		logger.debug("Sending request: {} to URL: {}", new Object[] { profile, url.toString() });

		// Send POST request to server
		IResponse response = RestClient.post(profile, url.toString());
		Profile result = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("profile") != null) {
			result = new ObjectMapper().readValue(response.getResultMap().get("profile").toString(), Profile.class);
			Notifier.success(null, Messages.getString("RECORD_SAVED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_SAVE"));
		}

		return result;
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
	public static List<Profile> list(String pluginName, String pluginVersion, String label, Boolean active)
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
		IResponse response = RestClient.get(url.toString());
		List<Profile> profiles = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("profiles") != null) {
			profiles = new ObjectMapper().readValue(response.getResultMap().get("profiles").toString(),
					new TypeReference<List<Profile>>() {
					});
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return profiles;
	}

	/**
	 * Send GET request to server in order to retrieve desired profile.
	 * 
	 * @param profileId
	 * @return
	 * @throws Exception
	 */
	public static Profile get(Long profileId) throws Exception {
		if (profileId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(profileId).append("/get");
		logger.debug("Sending request to URL: {}", url.toString());

		IResponse response = RestClient.get(url.toString());
		Profile profile = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("profile") != null) {
			profile = new ObjectMapper().readValue(response.getResultMap().get("profile").toString(), Profile.class);
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return profile;
	}

	/**
	 * Send GET request to server in order to delete desired profile.
	 * 
	 * @param profileId
	 * @return
	 * @throws Exception
	 */
	public static boolean delete(Long profileId) throws Exception {
		if (profileId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(profileId).append("/delete");
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
	 * @return base URL for profile actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_PROFILE_BASE_URL));
		return url;
	}

}
