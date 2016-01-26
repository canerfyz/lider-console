package tr.org.liderahenk.liderconsole.core.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import tr.org.liderahenk.liderconsole.core.current.RestSettings;
import tr.org.liderahenk.liderconsole.core.current.UserSettings;
import tr.org.liderahenk.liderconsole.core.dialogs.LiderMessageDialog;
import tr.org.liderahenk.liderconsole.core.schedule.ScheduleRequest;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 * 
 * Sample URL format for a REST request: 
 * http://[IP_ADDRESS]/[OBJECT_CLASS]/[OBJECT_DN]/[PLUGIN_NAME]/[ACTION]/[RESOURCE]
 * You have to first make a URL and then attach the parameters as a Map<String, String> object.
 * RestClient sends the request to the server.
 *
 */
public class RestClient {

	private static final String HEADER_VALUE = "application/json; charset=UTF-8";
	private static final String HEADER_NAME = "Content-Type";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String CONNECTION_TIMED_OUT = "Baglanti zaman asimina ugradi";
	private static final String REST_URI = "/rest.web/rest";
	private static String CLIENT_ID = "LYA";
	private static final String CLIENT_VERSION = "1.0.34";
	
	private final StringBuilder urlBuilder;
	private String url;
	private String ipAddress;
	private String objectClass;
	private String objectDn;
	private String pluginName;
	private String action;
	private String resource;
	private Map<String, Object> parameters;
	private boolean restWithoutProgress;
	private boolean showAuthError = true;
	private String stringResult;
	
	public boolean isShowAuthError() {
		return showAuthError;
	}

	public void setShowAuthError(boolean showAuthError) {
		this.showAuthError = showAuthError;
	}

	public RestClient() {
		urlBuilder = new StringBuilder();
		if(this.parameters == null || this.parameters.size() <= 0) {
			parameters = new HashMap<String, Object>();
		}
	}
	
	public RestClient(Map<String, Object> params) {					
		urlBuilder = new StringBuilder();		
		parameters = params;
	}
	
	public RestClient url(final String url) {
		this.url = url;
		return this;
	}
	public RestClient ipAddress(final String ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}
	
	public RestClient objectClass(final String objectClass) {
		this.objectClass = objectClass;
		return this;
	}
	public RestClient objectDn(final String objectDn) {
		this.objectDn = objectDn;
		return this;
	}
	public RestClient pluginName(String pluginName){
		this.pluginName = pluginName;
		return this;
	}
	public RestClient action(String action){
		this.action = action;
		return this;
	}
	public RestClient resource(String resource){
		this.resource = resource;
		return this;
	}
	
	public RestClient param(String key, String value) {
		this.parameters.put(key, value);
		return this;
	}
	
	public RestClient param(String key, Object object) {
		this.parameters.put(key, object);
		return this;
	}
	
	public RestClient make() {		
		this.url = urlBuilder.append(this.ipAddress)
						.toString();
		
		if ( null != this.objectClass )
		{
			this.url = urlBuilder.append("/").append(this.objectClass)
			.append("/")
			.append(this.objectDn)
			.append("/")
			.append(this.pluginName)
			.append("/")
			.append(this.action)
			.append("/")
			.append(this.resource)
			.toString();
		}
						
		return this;
	}
	
	/**
	 * 
	 * @return map of xmpp information.
	 * 
	 *         First item of map is 'xmppIp' which returns lider server's xmpp
	 *         ip address Second item of map is 'xmppDomain' which returns lider
	 *         server's xmpp domain address
	 */
	// TODO move to rest client
	public Map<String, Object> getXmppAddress() {
		Map<String, Object> retVal = null;
		RestClient rest_xmpp = new RestClient();
		rest_xmpp.setWithoutProgress(true);

		Map<String, Object> map_xmpp = new HashMap<String, Object>();

		map_xmpp.put("maxResults", 100);
		ServerResult serverResult_xmpp = rest_xmpp.getServerResult(map_xmpp, "GET", "NA", "ADDRESS", "XMPP", "NA",
				"Log", "", false, null);

		if (null != serverResult_xmpp && serverResult_xmpp.getStatus().equals("OK")) {
			ResponseBody responseBody = serverResult_xmpp.getResponseBody();
			retVal = responseBody.getResultMap();
		} else if (null != serverResult_xmpp && null != serverResult_xmpp.getMessages()
				&& serverResult_xmpp.getMessages().size() > 0) {
			System.out.println(serverResult_xmpp.getMessages().get(0));
		}
		return retVal;
	}
	
	/**
	 * 
	 * @return RestResponse object as result of http post.
	 * @throws Exception
	 */
	public RestResponse post() throws Exception {
		final HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params , 10000);
//		HttpConnectionParams.setSoTimeout(params , 10000);
		final DefaultHttpClient httpClient = new DefaultHttpClient(params);
		
		final HttpPost postRequest = new HttpPost(this.url.trim());
		
		postRequest.setHeader(HEADER_NAME, HEADER_VALUE);		
		
		final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
		StringEntity input = new StringEntity(URLEncoder.encode( gson.toJson(this.parameters), "UTF-8"), HTTP.UTF_8);
		input.setContentEncoding("UTF-8");		
		input.setContentType(HEADER_VALUE);		
		
		postRequest.setEntity(input);
		
		postRequest.setHeader(USERNAME, UserSettings.USER_ID);
		postRequest.setHeader(PASSWORD, UserSettings.USER_PASSWORD);
		HttpResponse response = null;
		try {
			response = httpClient.execute(postRequest);	
		} catch (Exception e) {
//			System.out.println("Baglanti zaman asimina ugradi: " + e.getMessage());
			throw new Exception(CONNECTION_TIMED_OUT);
		}
		
		
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(response.getStatusLine().getStatusCode());
		
		BufferedReader bufferredReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder buffer = new StringBuilder();
		String line;
		while( (line = bufferredReader.readLine()) != null ) {
			buffer.append(line);
		}

		String _object = buffer.toString();
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		Gson gson2 = gsonBuilder.serializeNulls().create();
		
		JsonElement resultMap = null;
		
        JsonParser parser = new JsonParser();
        JsonObject obj = null;
		try {
			obj = (JsonObject)parser.parse(_object);
	        if (null != obj) {
	            JsonElement responseBody = obj.get("responseBody");
	            JsonObject obj2 = null;
	            
	            if (JsonNull.INSTANCE != responseBody && null != responseBody) {
	            	obj2 = responseBody.getAsJsonObject();	
				}
	            if (null != obj2) {
	            	resultMap = obj2.get("resultMap");	
				}	            			
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		
		ServerResult fromJson =  gson2.fromJson(_object, new TypeToken<ServerResult>() {}.getType());
		if (null != resultMap) {
			fromJson.getResponseBody().setResultMapString(resultMap.toString());	
		}	
				
		restResponse.setResult(fromJson);
		
		return restResponse;
	}

	public RestResponse postDirect() throws Exception {
		HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params , 10000);
//		HttpConnectionParams.setSoTimeout(params , 10000);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		
		HttpPost postRequest = new HttpPost(this.url);
		
		postRequest.setHeader(HEADER_NAME, HEADER_VALUE);		
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();		
		StringEntity input = new StringEntity(gson.toJson(this.parameters), HTTP.UTF_8);
		
		input.setContentEncoding("UTF-8");		
		input.setContentType(HEADER_VALUE);
		System.out.println("[REST][PARAMETERS]: " + gson.toJson(this.parameters));
		postRequest.setEntity(input);		
		postRequest.setHeader(USERNAME, UserSettings.USER_ID);
		postRequest.setHeader(PASSWORD, UserSettings.USER_PASSWORD);
		

		HttpResponse response = null;
		try {
			response = httpClient.execute(postRequest);	
		} catch (Exception e) {
			throw new Exception(CONNECTION_TIMED_OUT);
		}
		
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(response.getStatusLine().getStatusCode());
		
		BufferedReader bufferredReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder buffer = new StringBuilder();
		String line;
		while( (line = bufferredReader.readLine()) != null ) {
			buffer.append(line);
		}
		
		String _object = buffer.toString();
		
		restResponse.setStringResult(_object);

		return restResponse;
	}
	
	public RestResponse postUrlDirect() throws Exception {
		HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params , 10000);
//		HttpConnectionParams.setSoTimeout(params , 10000);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		
		HttpPost postRequest = new HttpPost(this.url);
		
		postRequest.setHeader(HEADER_NAME, HEADER_VALUE);
		
		postRequest.setHeader(USERNAME, UserSettings.USER_ID);
		postRequest.setHeader(PASSWORD, UserSettings.USER_PASSWORD);
		

		HttpResponse response = null;
		try {
			response = httpClient.execute(postRequest);	
		} catch (Exception e) {
			throw new Exception(CONNECTION_TIMED_OUT);
		}
		
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(response.getStatusLine().getStatusCode());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder buffer = new StringBuilder();
		String line;
		while( (line = br.readLine()) != null )
			buffer.append(line);			
		
		String _object = buffer.toString();
		
		restResponse.setStringResult(_object);

		return restResponse;
	}
	
	/**
	 * 
	 * @return RestResponse object as result of http get.
	 * @throws Exception
	 */
	public RestResponse get() throws Exception {
		HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params , 10000);
//		HttpConnectionParams.setSoTimeout(params , 10000);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		HttpGet getRequest = new HttpGet(this.url);
		getRequest.setHeader(USERNAME, UserSettings.USER_ID);
		getRequest.setHeader(PASSWORD, UserSettings.USER_PASSWORD);
		HttpResponse response = null;
		try {
			response = httpClient.execute(getRequest);	
		} catch (Exception e) {
			throw new Exception(CONNECTION_TIMED_OUT);
		}
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(response.getStatusLine().getStatusCode());
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder buffer = new StringBuilder();
		String line;
		while( (line = br.readLine()) != null )
			buffer.append(line);
		restResponse.setStringResult(buffer.toString());
		return restResponse;
	}
	
	public byte[] getImage() throws Exception {
		HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params , 10000);
//		HttpConnectionParams.setSoTimeout(params , 10000);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		HttpGet getRequest = new HttpGet(this.url);
		getRequest.setHeader(USERNAME, UserSettings.USER_ID);
		getRequest.setHeader(PASSWORD, UserSettings.USER_PASSWORD);
		HttpResponse response = null;
		try {
			response = httpClient.execute(getRequest);	
		} catch (Exception e) {
			throw new Exception(CONNECTION_TIMED_OUT);
		}
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(response.getStatusLine().getStatusCode());

		byte[] arr = EntityUtils.toByteArray(response.getEntity());
		
		return arr;
	}
	
	public String getPriorityResult() throws Exception {
		HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params , 10000);
//		HttpConnectionParams.setSoTimeout(params , 10000);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		HttpGet getRequest = new HttpGet(this.url);
		getRequest.setHeader(USERNAME, UserSettings.USER_ID);
		getRequest.setHeader(PASSWORD, UserSettings.USER_PASSWORD);
		HttpResponse response = null;
		try {
			response = httpClient.execute(getRequest);	
		} catch (Exception e) {
			throw new Exception(CONNECTION_TIMED_OUT);
		}
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(response.getStatusLine().getStatusCode());

		Integer result = response.getStatusLine().getStatusCode();
		
		return result.toString();
	}
	
	public byte[] getPostImage() throws Exception {
		HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params , 10000);
//		HttpConnectionParams.setSoTimeout(params , 10000);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		
		HttpPost postRequest = new HttpPost(this.url);
		postRequest.setHeader(USERNAME, UserSettings.USER_ID);
		postRequest.setHeader(PASSWORD, UserSettings.USER_PASSWORD);
		postRequest.setHeader(HEADER_NAME, HEADER_VALUE);		
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();		
		StringEntity input = new StringEntity(gson.toJson(this.parameters), HTTP.UTF_8);
		
		input.setContentEncoding("UTF-8");		
		input.setContentType(HEADER_VALUE);

		postRequest.setEntity(input);		
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(postRequest);	
		} catch (Exception e) {
			throw new Exception(CONNECTION_TIMED_OUT);
		}
		
		byte[] arr = EntityUtils.toByteArray(response.getEntity());		
		
		return arr;
	}
	
	public String getLogGap() throws Exception{
		
		HttpParams params = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(params , 10000);
//		HttpConnectionParams.setSoTimeout(params , 10000);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		
		HttpPost postRequest = new HttpPost(this.url);
		postRequest.setHeader("username", UserSettings.USER_ID);
		postRequest.setHeader("password", UserSettings.USER_PASSWORD);
		postRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(postRequest);	
		} catch (Exception e) {
			throw new Exception(CONNECTION_TIMED_OUT);
		}
		
		return EntityUtils.toString(response.getEntity());
		
	}
	
	public boolean isWithoutProgress() {
		return restWithoutProgress;
	}

	public void setWithoutProgress(boolean restWithoutProgress) {
		this.restWithoutProgress = restWithoutProgress;
	}
	
	ServerResult result = null;
	
	/**
	 * 
	 * @param map - Parameters map.
	 * @param pluginName - Return value of attribute method from "Lider" class which implements ICommand interface
	 * @param objectClass - pardus-ahenk for agent, inetOrgParson for user.
	 * @param resource - Return value of action method from "Lider" class which implements ICommand interface
	 * @param action - Return value of command method from "Lider" class which implements ICommand interface
	 * @param objectDn - Dn of selected ldap object.
	 * @param pluginId - This parameter is used in Ahenk. In Ahenk "eklentier" folder plugins must be placed as "plugin_[pluginId]"
	 * @param pluginVersion - Version of plugin. This paremeter is optional.
	 * @param scheduled - if task will be scheduled. The value this parameter must be true and the last paremeter scheduleRequest paremeter must be passed with cron arguments.
	 * @param scheduleResuest - Supply tr.org.liderahenk.liderconsole.core.schedule.ScheduleRequest parameter if the task is scheduled. 
	 * @return
	 */
	public ServerResult getServerResult( Map<String, Object> map, String pluginName, String objectClass,
			String resource, String action, String objectDn, String pluginId, String pluginVersion,
			Boolean scheduled, ScheduleRequest scheduleResuest)
	{
		if (restWithoutProgress) {
			return getServerResult2(map, pluginName, objectClass, resource, action, objectDn, pluginId, pluginVersion, scheduled, scheduleResuest);
		}
		
		if (null != UserSettings.USER_ID) {
			CLIENT_ID = UserSettings.USER_ID;	
		}
	
		Integer priority = null != map.get("priority") ? Integer.parseInt(map.get("priority").toString()) : 0;
		
		final RestClient rest = new RestClient();
	
		rest.ipAddress(RestSettings.SERVER_REST_URL + REST_URI)
		.objectClass(objectClass)
		.objectDn(objectDn)
		.pluginName(pluginName)
		.action(action)
		.resource(resource)
		.param("pluginId", pluginId)
		.param("pluginVersion", pluginVersion.isEmpty() ? "1.0.0" : pluginVersion)
		.param("clientId", CLIENT_ID)
		.param("clientVersion", CLIENT_VERSION)
		.param("customParameterMap", map)
		.param("requestId", null)
		.param("scheduled", scheduled)
		.param("scheduleRequest", scheduleResuest)
		.param("priority", priority)
		.make();		
		
		try {			
			System.out.println("[REST]: waiting response from server...");
			IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
			   progressService.runInUI(
		      PlatformUI.getWorkbench().getProgressService(),
		      new IRunnableWithProgress() {
			         public void run(IProgressMonitor monitor) {
			 			System.out.println("[REST][URL]: " + rest.url);
						
						monitor.beginTask("İşlem devam ediyor...", 100);
						
						RestResponse response = null;
						monitor.worked(20);
						try {
							response = rest.post();
						} catch (Exception e) {						
							e.printStackTrace();
						}
						
						result = response.getResult();
						if (null != result && null != result.getMessages() && result.getMessages().size() > 0 && result.getMessages().get(0).equals("Could not add task for request: java.lang.IllegalStateException: Not connected to server.")) {
							Display.getDefault().asyncExec(new Runnable() {					
								@Override
								public void run() {						
									LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Mesajlaşma servisi cevap vermiyor!", SWT.NONE);
								}
							});
						}
						System.out.println( "[REST]: " + "Result was successfully returned from server. Status: " + result != null ? result.getStatus() : "");
						if (null != result.getMessages() && result.getMessages().size() > 0) {
							System.out.println( "[REST][INFO]: " + result.getMessages().get(0));
//							if (result.getMessages().get(0).equals("NOT_AUTHORIZED") && isShowAuthError()) {
//								Display.getDefault().asyncExec(new Runnable() {					
//									@Override
//									public void run() {						
//										MysMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Yetkisiz İşlem!\n" + rest.url, SWT.NONE);
//									}
//								});
//							}
						}
						monitor.worked(100);
						monitor.done();						
			         }
			      }, null);			
		} catch (Exception e) {
			if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {						
						LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
					}
				});
			}
			System.out.println("[REST][ERROR]: " + e.getMessage());
		}		
		
		return result;		
	}
	
	public ServerResult getServerResult2( Map<String, Object> map, String pluginName, String objectClass,
			String resource, String action, String objectDn, String pluginId, String pluginVersion,
			Boolean scheduled, ScheduleRequest scheduleResuest)
	{	
		if (null != UserSettings.USER_ID) {
			CLIENT_ID = UserSettings.USER_ID;	
		}
	
		Integer priority = null != map.get("priority") ? Integer.parseInt(map.get("priority").toString()) : 0;
		
		final RestClient rest = new RestClient();
	
		rest.ipAddress(RestSettings.SERVER_REST_URL + REST_URI)
		.objectClass(objectClass)
		.objectDn(objectDn)
		.pluginName(pluginName)
		.action(action)
		.resource(resource)
		.param("pluginId", pluginId)
		.param("pluginVersion", pluginVersion.isEmpty() ? "1.0.0" : pluginVersion)
		.param("clientId", CLIENT_ID)
		.param("clientVersion", CLIENT_VERSION)
		.param("customParameterMap", map)
		.param("requestId", null)
		.param("scheduled", scheduled)
		.param("scheduleRequest", scheduleResuest)
		.param("priority", priority)
		.make();
		
		ServerResult result = null;
		
		System.out.println("[REST][URL]: " + rest.url);
		
		try {
			System.out.println("[REST]: " + "waiting response from server...");
			RestResponse response = rest.post();
			result = response.getResult();
			if (null != result && null != result.getMessages() && result.getMessages().size() > 0 && result.getMessages().get(0).equals("Could not add task for request: java.lang.IllegalStateException: Not connected to server.")) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {						
						LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Mesajlaşma servisi cevap vermiyor!", SWT.NONE);
					}
				});
			}
			System.out.println( "[REST]: " + "Result was successfully returned from server. Status: " + result != null ? result.getStatus() : "");
			if (null != result.getMessages() && result.getMessages().size() > 0) {
				System.out.println( "[REST][INFO]: " + result.getMessages().get(0));				
			}
		} catch (Exception e) {
			if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {						
						LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
					}
				});
			}
			System.out.println("[REST][ERROR]: " + e.getMessage());
		}
		return result;		
	}
	
	class RestJob extends Job
	{
		private ServerResult result;
		private final RestClient rest;
		public RestJob(String name, ServerResult result, RestClient rest) {
			super(name);
			this.result = result;
			this.rest = rest;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			System.out.println("[REST][URL]: " + rest.url);
			
			monitor.beginTask("gönderiliyor...", 100);
			
			RestResponse response = null;
			monitor.worked(20);
			try {
				response = rest.post();
			} catch (Exception e) {						
				e.printStackTrace();
			}
			
			result = response.getResult();
			if (null != result && null != result.getMessages() && result.getMessages().size() > 0 && result.getMessages().get(0).equals("Could not add task for request: java.lang.IllegalStateException: Not connected to server.")) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {						
						LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Mesajlaşma servisi cevap vermiyor!", SWT.NONE);
					}
				});
			}
			System.out.println( "[REST]: " + "Result was successfully returned from server. Status: " + result != null ? result.getStatus() : "");
			if (null != result.getMessages() && result.getMessages().size() > 0) {
				System.out.println( "[REST][INFO]: " + result.getMessages().get(0));				
			}
			monitor.worked(100);
			monitor.done();

			return Status.OK_STATUS;
		}
		
	}
	
	/**
	 * 
	 * @param restUri - URI for rest services. Example: /rest/task. 
	 * @param params - parameter map for passing to lider server
	 * @return directly json from lider server for provided restUri and parameters.
	 */
	public String getServerResult(String restUri, Map<String, Object> params)
	{
		final RestClient rest = new RestClient(params);
		rest.ipAddress(RestSettings.SERVER_REST_URL + restUri).make();
		System.out.println("[REST][URL]: " + rest.url);
		try {
				System.out.println("[REST]: " + "waiting response from server...");
				RestResponse response = rest.postDirect	();
				
				if (null != response && null != response.getResult() &&  null != response.getResult().getMessages() && null != response.getResult().getMessages().get(0) && response.getResult().getMessages().get(0).equals("Could not add task for request: java.lang.IllegalStateException: Not connected to server.")) {
					Display.getDefault().asyncExec(new Runnable() {					
						@Override
						public void run() {						
							LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Mesajlaşma servisi cevap vermiyor!", SWT.NONE);
						}
					});
				}
				System.out.println("[REST]: " + "Result was successfully returned from server. Status: " + response.getStatus());
				if (null != response.getResult() && null != response.getResult().getMessages() && response.getResult().getMessages().size() > 0) {
					System.out.println( "[REST][INFO]: " + response.getResult().getMessages().get(0));				
				}
				stringResult = response.getStringResult();
			}
		 catch (Exception e) {
				if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
					Display.getDefault().asyncExec(new Runnable() {					
						@Override
						public void run() {						
							LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
						}
					});
				}
			 System.out.println("[REST][ERROR]: " + e.getMessage());
		 }
	
		return stringResult;		
	}
	
	/**
	 * 
	 * @param restUri - Example: /rest/task
	 * @param params
	 * @return directly json from lider server for provided URI and parameters. Only difference from getServerResult( ... ) method is that this method requests via http GET. 
	 */
	public String getServerResultGET(String restUri, Map<String, Object> params)
	{
		final RestClient rest = new RestClient(params);
		rest.ipAddress(RestSettings.SERVER_REST_URL + restUri).make();

		try {
				System.out.println("[REST]: " + "waiting response from server...");
				RestResponse response = rest.get();
				
				if (null != response && null != response.getResult() &&  null != response.getResult().getMessages() && null != response.getResult().getMessages().get(0) && response.getResult().getMessages().get(0).equals("Could not add task for request: java.lang.IllegalStateException: Not connected to server.")) {
					Display.getDefault().asyncExec(new Runnable() {					
						@Override
						public void run() {						
							LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Mesajlaşma servisi cevap vermiyor!", SWT.NONE);
						}
					});
				}
				System.out.println("[REST]: " + "Result was successfully returned from server. Status: " + response.getStatus());
				if (null != response.getResult() && null != response.getResult().getMessages() && response.getResult().getMessages().size() > 0) {
					System.out.println( "[REST][INFO]: " + response.getResult().getMessages().get(0));				
				}
				stringResult = response.getStringResult();
			}
		 catch (Exception e) {
				if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
					Display.getDefault().asyncExec(new Runnable() {					
						@Override
						public void run() {						
							LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
						}
					});
				}
			 System.out.println("[REST][ERROR]: " + e.getMessage());
		 }
	
		return stringResult;		
	}
	
	public String getDirectServerResult(String url, Map<String, Object> params, ScheduleRequest scheduleRequest)
	{
		RestClient rest = new RestClient();
		
		rest.ipAddress(RestSettings.SERVER_REST_URL + REST_URI + url)
		.param("pluginId", "")
		.param("pluginVersion", "")
		.param("clientId", CLIENT_ID)
		.param("clientVersion", CLIENT_VERSION)
		.param("customParameterMap", params)
		.param("requestId", null)
		.param("scheduled", true)
		.param("scheduleRequest", scheduleRequest)		
		.make();

		try {
				System.out.println("[REST]: " + "waiting response from server...");
				RestResponse response = rest.postDirect	();	
				if (null != response && null != response.getResult() && null != response.getResult().getMessages() && null != response.getResult().getMessages().get(0) && response.getResult().getMessages().get(0).equals("Could not add task for request: java.lang.IllegalStateException: Not connected to server.")) {
					Display.getDefault().asyncExec(new Runnable() {					
						@Override
						public void run() {						
							LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Mesajlaşma servisi cevap vermiyor!", SWT.NONE);
						}
					});
				}
				System.out.println("[REST]: " + "Result was successfully returned from server. Status: " + response.getStatus());
				stringResult = response.getStringResult();
			}
		 catch (Exception e) {
				if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
					Display.getDefault().asyncExec(new Runnable() {					
						@Override
						public void run() {						
							LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
						}
					});
				}
			 System.out.println("[REST][ERROR]: " + e.getMessage());
		 }
	
		return stringResult;		
	}
	
	/**
	 * 
	 * @param restUri - Example: /rest/task
	 * @param params
	 * @param method
	 * @return byte array from lider server for given URI and parameters.
	 */
	public byte[] getServerResult(String restUri, Map<String, Object> params, String method)
	{
		RestClient rest = new RestClient(params);
		rest.ipAddress(RestSettings.SERVER_REST_URL + restUri).make();
		byte[] image = null;
		try {
			image = rest.getImage();
		} catch (Exception e) {
			if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {						
						LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
					}
				});
			}
			e.printStackTrace();
		}
		
		return image;		
	}
	
	/**
	 * 
	 * @param restUri - Example: /rest/task
	 * @return directly json string from lider server for given URI.
	 */
	public String getServerResult(String restUri)
	{
		RestClient rest = new RestClient(new HashMap<String, Object>());
		rest.ipAddress(RestSettings.SERVER_REST_URL + restUri).make();
		String returnValue = null;
		try {
			returnValue = rest.getPriorityResult();
			
		} catch (Exception e) {
			if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {						
						LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
					}
				});
			}
			e.printStackTrace();
		}
		
		return returnValue;		
	}
	
	
	public byte[] getPdfFromServer(Map<String, Object> map, String pluginName, String objectClass,
			String resource, String action, String objectDn, String pluginId, String pluginVersion,
			Boolean scheduled, ScheduleRequest scheduleResuest )
	{	
		RestClient rest = new RestClient(map);
		rest.ipAddress(RestSettings.SERVER_REST_URL + "/rest/report/pdf").make();
		byte[] image = null;
		try {
			image = rest.getPostImage();
			
		} catch (Exception e) {
			if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {						
						LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
					}
				});
			}
			e.printStackTrace();
		}
		
		return image;
	}
	
	public byte[] getLogFromServer(Map<String, Object> map )
	{	
		RestClient rest = new RestClient(map);
		rest.ipAddress(RestSettings.SERVER_REST_URL + "/rest/syslog/log").make();
		
		try {
			return rest.getPostImage();
			
		} catch (Exception e) {
			if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {						
						LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
					}
				});
			}
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public String getLogGapFromServer(Map<String, Object> map )
	{	
		RestClient rest = new RestClient(map);
		rest.ipAddress(RestSettings.SERVER_REST_URL + "/rest/syslog/logGap").make();
		
		try {
			return rest.getLogGap();
			
		} catch (Exception e) {
			if (null != e && null !=  e.getMessage() && e.getMessage().equals(CONNECTION_TIMED_OUT)) {
				Display.getDefault().asyncExec(new Runnable() {					
					@Override
					public void run() {						
						LiderMessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), "Hata", "Rest servisi cevap vermiyor!", SWT.NONE);
					}
				});
			}
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @return true if rest is connected and verified.
	 */
	public static boolean isConnectionVerified()
	{		
		if (null != RestSettings.SERVER_REST_URL) {
			RestClient rest = new RestClient();
			rest.ipAddress(RestSettings.SERVER_REST_URL + "/rest.web/rest")
					.objectClass("NA") //$NON-NLS-1$
					.objectDn("NA") //$NON-NLS-1$
					.pluginName("GET") //$NON-NLS-1$
					.action("PLUGIN") //$NON-NLS-1$
					.resource("LIST") //$NON-NLS-1$
					.param("pluginId", "") //$NON-NLS-1$ //$NON-NLS-2$
					.param("pluginVersion", "1.0.0") //$NON-NLS-1$ //$NON-NLS-2$
					.param("clientId", "") //$NON-NLS-1$ //$NON-NLS-2$
					.param("clientVersion", "") //$NON-NLS-1$ //$NON-NLS-2$
					.param("customParameterMap", new HashMap<String, Object>()) //$NON-NLS-1$
					.param("requestId", null) //$NON-NLS-1$
					.param("scheduled", false) //$NON-NLS-1$
					.param("scheduleRequest", null) //$NON-NLS-1$
					.make();
			try {
				RestResponse response = rest.post();
				if (response.getStatus() == 200) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
			return false;
		}
		
		return false;
	}
}