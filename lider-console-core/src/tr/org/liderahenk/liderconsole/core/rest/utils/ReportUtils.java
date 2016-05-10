package tr.org.liderahenk.liderconsole.core.rest.utils;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplate;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.requests.ReportTemplateRequest;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Utility class for sending report related requests to Lider server.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ReportUtils {

	private static final Logger logger = LoggerFactory.getLogger(ReportUtils.class);

	/**
	 * Send POST request to server in order to validate template.
	 * 
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public static boolean validate(ReportTemplateRequest template) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/validate");
		logger.debug("Sending request: {} to URL: {}", new Object[] { template, url.toString() });

		// Send POST request to server
		IResponse response = RestClient.post(template, url.toString());

		if (response != null && response.getStatus() == RestResponseStatus.OK) {
			Notifier.error(null, Messages.getString("RECORD_VALIDATED"));
			return true;
		}

		Notifier.error(null, Messages.getString("ERROR_ON_VALIDATION"));
		return false;
	}

	/**
	 * Send POST request to server in order to save specified template.
	 * 
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public static ReportTemplate add(ReportTemplateRequest template) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/add");
		logger.debug("Sending request: {} to URL: {}", new Object[] { template, url.toString() });

		// Send POST request to server
		IResponse response = RestClient.post(template, url.toString());
		ReportTemplate result = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("template") != null) {
			result = new ObjectMapper().readValue(response.getResultMap().get("template").toString(),
					ReportTemplate.class);
			Notifier.success(null, Messages.getString("RECORD_SAVED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_SAVE"));
		}

		return result;
	}

	/**
	 * Send POST request to server in order to update specified template.
	 * 
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public static ReportTemplate update(ReportTemplateRequest template) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/update");
		logger.debug("Sending request: {} to URL: {}", new Object[] { template, url.toString() });

		IResponse response = RestClient.post(template, url.toString());
		ReportTemplate result = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("template") != null) {
			result = new ObjectMapper().readValue(response.getResultMap().get("template").toString(),
					ReportTemplate.class);
			Notifier.success(null, Messages.getString("RECORD_SAVED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_SAVE"));
		}

		return result;
	}

	/**
	 * Send GET request to server in order to retrieve desired templates.
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static List<ReportTemplate> list(String name) throws Exception {

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/list?");

		// Append optional parameters
		if (name != null) {
			url.append("name=").append(name);
		}
		logger.debug("Sending request to URL: {}", url.toString());

		// Send GET request to server
		IResponse response = RestClient.get(url.toString());
		List<ReportTemplate> templates = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("templates") != null) {
			templates = new ObjectMapper().readValue(response.getResultMap().get("templates").toString(),
					new TypeReference<List<ReportTemplate>>() {
					});
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return templates;
	}

	/**
	 * Send GET request to server in order to retrieve desired template.
	 * 
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	public static ReportTemplate get(Long templateId) throws Exception {

		if (templateId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(templateId).append("/get");
		logger.debug("Sending request to URL: {}", url.toString());

		IResponse response = RestClient.get(url.toString());
		ReportTemplate template = null;

		if (response != null && response.getStatus() == RestResponseStatus.OK
				&& response.getResultMap().get("template") != null) {
			template = new ObjectMapper().readValue(response.getResultMap().get("template").toString(),
					ReportTemplate.class);
			Notifier.success(null, Messages.getString("RECORD_LISTED"));
		} else {
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}

		return template;
	}

	/**
	 * Send GET request to server in order to delete desired template.
	 * 
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	public static boolean delete(Long templateId) throws Exception {

		if (templateId == null) {
			throw new IllegalArgumentException("ID was null.");
		}

		// Build URL
		StringBuilder url = getBaseUrl();
		url.append("/").append(templateId).append("/delete");
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
	 * @return base URL for report actions
	 */
	private static StringBuilder getBaseUrl() {
		StringBuilder url = new StringBuilder(
				ConfigProvider.getInstance().get(LiderConstants.CONFIG.REST_REPORT_BASE_URL));
		return url;
	}

}
