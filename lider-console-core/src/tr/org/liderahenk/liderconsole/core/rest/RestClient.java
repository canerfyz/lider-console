package tr.org.liderahenk.liderconsole.core.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.current.RestSettings;
import tr.org.liderahenk.liderconsole.core.current.UserSettings;
import tr.org.liderahenk.liderconsole.core.rest.requests.IRequest;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.rest.responses.RestResponse;
import tr.org.liderahenk.liderconsole.core.rest.utils.PolicyUtils;
import tr.org.liderahenk.liderconsole.core.rest.utils.ProfileUtils;

/**
 * RestClient provides utility methods for sending requests to Lider Server and
 * handling their responses. Instead of this class, it is recommended that
 * plugin developers should use {@link ProfileUtils}, {@link PolicyUtils} or
 * {@link TaskUtils} according to their needs.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
@SuppressWarnings("restriction")
public class RestClient {

	private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

	/**
	 * Content type header
	 */
	private static final String CONTENT_TYPE_HEADER = "Content-Type";

	/**
	 * Accept header
	 */
	private static final String ACCEPT_HEADER = "Accept";

	/**
	 * Use only JSON for requests
	 */
	private static final String MIME_TYPE = "application/json";

	/**
	 * Username header
	 */
	private static final String USERNAME_HEADER = "username";

	/**
	 * Password header
	 */
	private static final String PASSWORD_HEADER = "password";

	/**
	 * Define this as a global variable to overcome re-generating JSessionId for
	 * each request.
	 */
	private static HttpClient httpClient = HttpClientBuilder.create().build();

	private RestClient() {
	}

	/**
	 * Main method that can be used to send POST requests to Lider Server.
	 * 
	 * @param request
	 * @return an instance of RestResponse if successful, null otherwise.
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public static IResponse post(IRequest request, String url) throws Exception {

		IResponse response = null;

		HttpPost httpPost = new HttpPost(buildUrl(url));
		httpPost.setHeader(CONTENT_TYPE_HEADER, MIME_TYPE);
		httpPost.setHeader(ACCEPT_HEADER, MIME_TYPE);

		// Convert IRequest instance to JSON and pass as HttpEntity
		StringEntity entity = new StringEntity(URLEncoder.encode(request.toJson(), "UTF-8"), StandardCharsets.UTF_8);
		httpPost.setEntity(entity);

		httpPost.setHeader(USERNAME_HEADER, UserSettings.USER_ID);
		httpPost.setHeader(PASSWORD_HEADER, UserSettings.USER_PASSWORD);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			logger.warn("REST failure. Status code: {} Reason: {} ", new Object[] {
					httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase() });
		} else { // Status OK

			BufferedReader bufferredReader = new BufferedReader(
					new InputStreamReader(httpResponse.getEntity().getContent()));
			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = bufferredReader.readLine()) != null) {
				buffer.append(line);
			}

			response = new ObjectMapper().readValue(buffer.toString(), RestResponse.class);
		}

		if (response != null) {
			logger.debug("Response received: {}", response);
		}
		return response;
	}

	/**
	 * Main method that can be used to send GET requests to Lider server.
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static IResponse get(String url) throws Exception {

		IResponse response = null;

		HttpGet httpGet = new HttpGet(buildUrl(url));
		httpGet.setHeader(CONTENT_TYPE_HEADER, MIME_TYPE);
		httpGet.setHeader(ACCEPT_HEADER, MIME_TYPE);

		HttpResponse httpResponse = httpClient.execute(httpGet);
		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			logger.warn("REST failure. Status code: {} Reason: {} ", new Object[] {
					httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase() });
		} else {

			BufferedReader bufferredReader = new BufferedReader(
					new InputStreamReader(httpResponse.getEntity().getContent()));
			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = bufferredReader.readLine()) != null) {
				buffer.append(line);
			}

			response = new ObjectMapper().readValue(buffer.toString(), RestResponse.class);
		}

		if (response != null) {
			logger.debug("Response received: {}", response);
		}
		return response;
	}

	/**
	 * Builds REST URL based on provided parameters. Resulting URL made of this
	 * format:<br/>
	 * {SERVER_IP}/{BASE_URL}/{ACTION_URL}<br/>
	 * 
	 * @param resource
	 * @param action
	 * @return
	 */
	private static String buildUrl(String base) {
		String tmp = RestSettings.getServerUrl();
		// Handle trailing/leading slash characters.
		if (!tmp.endsWith("/") && !base.startsWith("/")) {
			tmp = tmp + "/" + base;
		} else if (tmp.endsWith("/") && base.startsWith("/")) {
			tmp = tmp + base.substring(1);
		} else {
			tmp = tmp + base;
		}
		return tmp;
	}

}
