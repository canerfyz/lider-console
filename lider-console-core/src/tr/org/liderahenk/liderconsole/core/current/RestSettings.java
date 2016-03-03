package tr.org.liderahenk.liderconsole.core.current;

/**
 * 
 * Application wide configuration for rest services.
 * 
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 * 
 */
public class RestSettings {

	private RestSettings() {
	}

	private static String SERVER_URL = null;

	public static String getServerUrl() {
		return SERVER_URL;
	}

	public static void setServerUrl(String url) {
		SERVER_URL = url;
	}

	public static boolean isAvailable() {
		return SERVER_URL != null;
	}

}
