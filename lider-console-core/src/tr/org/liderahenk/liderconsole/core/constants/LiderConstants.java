package tr.org.liderahenk.liderconsole.core.constants;

/**
 * Provides common constants used throughout the system.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class LiderConstants {

	public static final class PLUGIN_IDS {
		public static final String LIDER_CONSOLE_CORE = "tr.org.liderahenk.liderconsole.core";
	}

	public static final class PERSPECTIVES {
		public static final String MAIN_PERSPECTIVE_ID = "tr.org.liderahenk.liderconsole.core.perspectives.MainPerspective";
		public static final String LDAP_BROWSER_PERSPECTIVE_ID = "org.apache.directory.studio.ldapbrowser.ui.perspective.BrowserPerspective";
	}

	public static final class VIEWS {
		public static final String BROWSER_VIEW = "org.apache.directory.studio.ldapbrowser.ui.views.browser.BrowserView";
	}

	public static final class EXTENSION_POINTS {
		public static final String LOGGER = "tr.org.liderahenk.liderconsole.core.logger";
		public static final String PROFILE_MENU = "tr.org.liderahenk.liderconsole.core.profilemenu";
		public static final String ACTION_MENU = "tr.org.liderahenk.liderconsole.core.actionmenu";
		public static final String POLICY_MENU = "tr.org.liderahenk.liderconsole.core.policymenu";
	}

	public static final class EVENT_TOPICS {
		public static final String XMPP_ONLINE = "xmpp_online";
		public static final String XMPP_OFFLINE = "xmpp_offline";
		public static final String ROSTER_ONLINE = "roster_online";
		public static final String ROSTER_OFFLINE = "roster_offline";
		public static final String TASK = "task";
	}

	public static final class CONFIG_PROPS {
		public static final String PROFILE_BASE_URL = "rest.profile.base.url";
		public static final String POLICY_BASE_URL = "rest.policy.base.url";
		public static final String TASK_BASE_URL = "rest.task.base.url";
		public static final String INITIAL_PERSPECTIVE_ID = "gui.initial.perspective.id";
	}

	// TODO read these attributes from a properties file!
	public static final class LdapAttributes {
		public static final String UserIdentityAttribute = "uid";
		public static final String configRestFulAddressAttribute = "liderServiceAddress";
		public static final String configOwnerAttribute = "liderAhenkOwnerAttributeName";
		public static final String configDeviceObjectClassAttribute = "liderDeviceObjectClassName";
		public static final String configUserIdentityAttribute = "liderUserIdentityAttributeName";
		public static final String configUserObjectClassAttribute = "liderUserObjectClassName";
		public static final String liderPrivilegeAttribute = "liderPrivilege";
		public static final String AhenkUserAttribute = "owner";
		public static final String PardusAhenkObjectClass = "pardusDevice";
		public static final String PardusUserObjectClass = "pardusAccount";
	}

	public static class FILES {
		public static final String PROPERTIES_FILE = "config.properties";
	}

}
