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
import tr.org.liderahenk.liderconsole.core.model.Agent;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Utility class for sending agent related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class AgentUtils {

	private static final Logger logger = LoggerFactory.getLogger(AgentUtils.class);

	/**
	 * Send GET request to server in order to retrieve desired agents.
	 * 
	 * @param hostname
	 * @param dn
	 * @return
	 * @throws Exception
	 */
	public static List<Agent> list(String hostname, String dn) throws Exception {
		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/list?");

		// Append optional parameters
		List<String> params = new ArrayList<String>();
		if (hostname != null) {
			params.add("hostname=" + hostname);
		}
		if (dn != null) {
			params.add("dn=" + dn);
		}
		if (!params.isEmpty()) {
			url.append(StringUtils.join(params, "&"));
		}
		logger.debug("Sending request to URL: {}", url.toString());

		// Send GET request to server
		IResponse response = RestClient.get(url.toString());
		List<Agent> agents = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("agents") != null) {
			agents = new ObjectMapper().readValue(response.getResultMap().get("agents").toString(),
					new TypeReference<List<Agent>>() {
					});
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return agents;
	}

	/**
	 * Send GET request to server in order to retrieve desired agent.
	 * 
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	public static Agent get(Long agentId) throws Exception {
		if (agentId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(agentId).append("/get");
		logger.debug("Sending request to URL: {}", url.toString());

		IResponse response = RestClient.get(url.toString());
		Agent agent = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("agent") != null) {
			agent = new ObjectMapper().readValue(response.getResultMap().get("agent").toString(), Agent.class);
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return agent;
	}

	/**
	 * 
	 * @return base URL for agent actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_AGENT_BASE_URL));
		return url;
	}

}
