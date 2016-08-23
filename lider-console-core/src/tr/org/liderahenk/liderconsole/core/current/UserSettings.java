package tr.org.liderahenk.liderconsole.core.current;

/**
 * Application wide user settings class.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class UserSettings {

	private UserSettings() {
	}

	public static String USER_DN = null;
	public static String USER_PASSWORD = null;
	public static String USER_ID = null;

	public static void setCurrentUserDn(String userDn) {
		USER_DN = userDn;
	}

	public static void setCurrentUserId(String userId) {
		USER_ID = userId;
	}

	public static void setCurrentUserPassword(String password) {
		USER_PASSWORD = password;
	}

}
