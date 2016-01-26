package tr.org.liderahenk.liderconsole.core.rest;


/**
 * @author Sezgin BOZU <sezgin8810@gmail.com>
 *
 */
public class RestUrl {

	/*public static final String SERVER_REST_URL = "193.140.98.197:8181/rest.web/rest";*/	 

	public static String SERVER_REST_URL = "127.0.0.1:8181/rest.web/rest";
	
	/*public static String SERVER_REST_URL = "127.0.0.1:8181/rest.web/rest";*/
	
	private static String restUrl;
	private static String restPort;
	
	public static void buildRestUrl() {
		SERVER_REST_URL = restUrl + ":" + restPort + "/rest.web/rest";
	}
	
//	IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode("tr.org.liderahenk.ui");
//	Preferences connectionProfiles = preferences.node("ldap.profiles");
//	String host = "", port = "", dn = "";		
//	boolean useSSL=false;
//	host = connectionProfiles.node(selectedItem).get("host", host);

	public String getRestPort() {
		return restPort;
	}

	public void setRestPort(String restPort) {
		RestUrl.restPort = restPort;
	}

	public String getRestUrl() {
		return restUrl;
	}

	public static void setRestUrl(String restUrl) {
		RestUrl.restUrl = restUrl;
	}
}

