package tr.org.liderahenk.liderconsole.core.current;

/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 *         Application wide configuration for rest services.
 */
public class RestSettings {

	private RestSettings() {
	}

	private static String SERVER_URL = "http://localhost:8181"; // TODO

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
