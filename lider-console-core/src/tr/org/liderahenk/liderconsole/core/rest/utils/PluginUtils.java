package tr.org.liderahenk.liderconsole.core.rest.utils;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Plugin;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Utility class for sending plugin related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class PluginUtils {

	private static final Logger logger = LoggerFactory.getLogger(PluginUtils.class);

	/**
	 * Send GET request to server in order to retrieve desired plugins.
	 * 
	 * @param pluginName
	 * @param pluginVersion
	 * @param label
	 * @param active
	 * @return
	 * @throws Exception
	 */
	public static List<Plugin> list(String name, String version) throws Exception {
		if ((name == null || name.isEmpty()) && (version == null || version.isEmpty())) {
			throw new IllegalArgumentException("Plugin name was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/list?name=").append(name).append("&version=").append(version);
		logger.debug("Sending request to URL: {}", url.toString());

		// Send GET request to server
		IResponse response = RestClient.get(url.toString());
		List<Plugin> plugins = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("plugins") != null) {
			plugins = new ObjectMapper().readValue(response.getResultMap().get("plugins").toString(),
					new TypeReference<List<Plugin>>() {
					});
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return plugins;
	}

	/**
	 * Send GET request to server in order to retrieve desired plugin.
	 * 
	 * @param pluginId
	 * @return
	 * @throws Exception
	 */
	public static Plugin get(Long pluginId) throws Exception {
		if (pluginId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(pluginId).append("/get");
		logger.debug("Sending request to URL: {}", url.toString());

		IResponse response = RestClient.get(url.toString());
		Plugin plugin = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("plugin") != null) {
			plugin = new ObjectMapper().readValue(response.getResultMap().get("plugin").toString(), Plugin.class);
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return plugin;
	}

	/**
	 * 
	 * @return base URL for plugin actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_PLUGIN_BASE_URL));
		return url;
	}

}
