package tr.org.liderahenk.liderconsole.core.current;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 * Application wide configuration for rest services.
 */
public class RestSettings {
	
	private RestSettings() {
		// Auto-generated constructor stub
	}
	
	public static String SERVER_REST_URL = null;
	
	public static void setServerRestUrl(String restUrl)
	{
		SERVER_REST_URL = restUrl;
	}
	
	public static Boolean isAVAILABLE(){
		return SERVER_REST_URL!=null;
	}
}
