package tr.org.liderahenk.liderconsole.core.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import tr.org.liderahenk.liderconsole.core.current.RestSettings;
import tr.org.liderahenk.liderconsole.core.current.UserSettings;
import tr.org.liderahenk.liderconsole.core.utils.PropertyLoader;

/**
 * RestClient provides utility methods for posting requests to Lider Server and
 * handling their responses.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
@SuppressWarnings("restriction")
public class RestClient {

	private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

	/**
	 * RestClient instance
	 */
	private static RestClient instance = null;

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
	private HttpClient httpClient;

	public static synchronized RestClient getInstance() {
		if (instance == null) {
			instance = new RestClient();
			instance.httpClient = HttpClientBuilder.create().build();
		}
		return instance;
	}

	private RestClient() {
	}

	/**
	 * Main method that can be used to post requests to Lider Server.
	 * 
	 * @param request
	 * @return an instance of RestResponse if successful, null otherwise.
	 */
	public RestResponse post(RestRequest request) {

		HttpPost httpPost = new HttpPost(buildUrl());
		httpPost.setHeader(CONTENT_TYPE_HEADER, MIME_TYPE);
		httpPost.setHeader(ACCEPT_HEADER, MIME_TYPE);

		StringEntity entity;
		RestResponse response = null;

		try {

			// Convert RestRequest instance to JSON and pass as HttpEntity
			Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
			entity = new StringEntity(URLEncoder.encode(gson.toJson(request), "UTF-8"), StandardCharsets.UTF_8);
			httpPost.setEntity(entity);

			httpPost.setHeader(USERNAME_HEADER, "lider"/*UserSettings.USER_ID*/);
			httpPost.setHeader(PASSWORD_HEADER, "12345"/*UserSettings.USER_PASSWORD*/);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				logger.info("REST failure. Status code: {} Reason: {} ", new Object[] {
						httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase() });
			} else { // Status OK

				BufferedReader bufferredReader = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity().getContent()));
				StringBuilder buffer = new StringBuilder();
				String line;
				while ((line = bufferredReader.readLine()) != null) {
					buffer.append(line);
				}

				gson = new GsonBuilder().serializeNulls().create();
				response = gson.fromJson(buffer.toString(), new TypeToken<RestResponse>() {
				}.getType());
			}

		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return response;
	}

	/**
	 * Builds REST URL based on provided parameters. Resulting URL made of this
	 * format:<br/>
	 * {SERVER_IP}/{BASE_URL}<br/>
	 * 
	 * @param resource
	 * @param action
	 * @return
	 */
	private String buildUrl() {
		String url = RestSettings.getServerUrl();
		String baseUrl = PropertyLoader.getInstance().get("lider.base.url");
		// Handle trailing/leading slash characters.
		if (!url.endsWith("/") && !baseUrl.startsWith("/")) {
			url = url + "/" + baseUrl;
		} else if (url.endsWith("/") && baseUrl.startsWith("/")) {
			url = url + baseUrl.substring(1);
		} else {
			url = url + baseUrl;
		}
		return url;
	}

}
