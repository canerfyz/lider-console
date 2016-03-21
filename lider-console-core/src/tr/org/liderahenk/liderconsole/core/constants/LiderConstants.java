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
	
	public static final class EDITORS {
		public static final String PROFILE_EDITOR = "";
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

	/**
	 * Configuration properties used in config.properties file
	 */
	public static final class CONFIG {
		public static final String REST_PROFILE_BASE_URL = "rest.profile.base.url";
		public static final String REST_POLICY_BASE_URL = "rest.policy.base.url";
		public static final String REST_TASK_BASE_URL = "rest.task.base.url";
		public static final String GUI_INITIAL_PERSPECTIVE_ID = "gui.initial.perspective.id";
		public static final String XMPP_MAX_RETRY_CONN = "xmpp.max.retry.connection.count";
		public static final String XMPP_REPLAY_TIMEOUT = "xmpp.packet.replay.timeout";
		public static final String XMPP_PING_TIMEOUT = "xmpp.ping.timeout";
		public static final String XMPP_USE_SSL = "xmpp.use.ssl";
		public static final String USER_LDAP_OBJ_CLS = "user.ldap.object.classes";
		public static final String USER_LDAP_UID_ATTR = "user.ldap.uid.attribute";
		public static final String USER_LDAP_PRIVILEGE_ATTR = "user.ldap.privilege.attribute";
		public static final String AGENT_LDAP_OBJ_CLS = "agent.ldap.object.classes";
		public static final String LDAP_REST_ADDRESS_ATTR = "ldap.rest.address.attribute";
	}

	public static class FILES {
		public static final String PROPERTIES_FILE = "config.properties";
	}

}
