package tr.org.liderahenk.liderconsole.core.constants;

import org.eclipse.e4.core.services.events.IEventBroker;

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
		public static final String LDAP_SEARCH_EDITOR = "tr.org.liderahenk.liderconsole.core.editors.LdapSearchEditor";
		public static final String EXECUTED_TASK_EDITOR = "tr.org.liderahenk.liderconsole.core.editors.ExecutedTaskEditor";
		public static final String EXECUTED_POLICY_EDITOR = "tr.org.liderahenk.liderconsole.core.editors.ExecutedPolicyEditor";
		public static final String PROFILE_EDITOR = "tr.org.liderahenk.liderconsole.core.editors.DefaultProfileEditor";
		public static final String INSTALLED_PLUGINS_EDITOR = "tr.org.liderahenk.liderconsole.core.editors.InstalledPluginsEditor";
		public static final String POLICY_DEFINITION_EDITOR = "tr.org.liderahenk.liderconsole.core.editors.PolicyDefinitionEditor";
		public static final String AGENT_INFO_EDITOR = "tr.org.liderahenk.liderconsole.core.editors.AgentInfoEditor";
		public static final String REPORT_TEMPLATE_EDITOR = "tr.org.liderahenk.liderconsole.core.editors.ReportTemplateEditor";
	}

	public static final class EXTENSION_POINTS {
		public static final String LOGGER = "tr.org.liderahenk.liderconsole.core.logger";
		public static final String PROFILE_MENU = "tr.org.liderahenk.liderconsole.core.profilemenu";
		public static final String ACTION_MENU = "tr.org.liderahenk.liderconsole.core.actionmenu";
		public static final String POLICY_MENU = "tr.org.liderahenk.liderconsole.core.policymenu";
	}

	/**
	 * Event topics used by {@link IEventBroker}
	 */
	public static final class EVENT_TOPICS {
		/**
		 * Thrown when XMPP connection establishes
		 */
		public static final String XMPP_ONLINE = "xmpp_online";
		/**
		 * Thrown when XMPP connection fails
		 */
		public static final String XMPP_OFFLINE = "xmpp_offline";
		/**
		 * Thrown when a roster becomes online
		 */
		public static final String ROSTER_ONLINE = "roster_online";
		/**
		 * Thrown when a roster becomes offline
		 */
		public static final String ROSTER_OFFLINE = "roster_offline";
		/**
		 * Thrown when 'task status' notification received
		 */
		public static final String TASK_STATUS_NOTIFICATION_RECEIVED = "task_status_notification_received";
		/**
		 * Thrown when 'task' notification received
		 */
		public static final String TASK_NOTIFICATION_RECEIVED = "task_notification_received";
	}

	/**
	 * Configuration properties used in config.properties file
	 * 
	 * If you modify these inner class, do not forget to modify
	 * <b>config.properties</b> as well!
	 */
	public static final class CONFIG {
		public static final String REST_SOCKET_TIMEOUT = "rest.socket.timeout";
		public static final String REST_CONNECT_TIMEOUT = "rest.connect.timeout";
		public static final String REST_CONN_REQUEST_TIMEOUT = "rest.connection.request.timeout";
		public static final String REST_PROFILE_BASE_URL = "rest.profile.base.url";
		public static final String REST_PLUGIN_BASE_URL = "rest.plugin.base.url";
		public static final String REST_AGENT_BASE_URL = "rest.agent.base.url";
		public static final String REST_POLICY_BASE_URL = "rest.policy.base.url";
		public static final String REST_REPORT_BASE_URL = "rest.report.base.url";
		public static final String REST_TASK_BASE_URL = "rest.task.base.url";
		public static final String REST_COMMAND_BASE_URL = "rest.command.base.url";
		public static final String GUI_INITIAL_PERSPECTIVE_ID = "gui.initial.perspective.id";
		public static final String XMPP_MAX_RETRY_CONN = "xmpp.max.retry.connection.count";
		public static final String XMPP_REPLAY_TIMEOUT = "xmpp.packet.replay.timeout";
		public static final String XMPP_PING_TIMEOUT = "xmpp.ping.timeout";
		public static final String XMPP_USE_SSL = "xmpp.use.ssl";
		public static final String USER_LDAP_OBJ_CLS = "user.ldap.object.classes";
		public static final String USER_LDAP_UID_ATTR = "user.ldap.uid.attribute";
		public static final String USER_LDAP_PRIVILEGE_ATTR = "user.ldap.privilege.attribute";
		public static final String AGENT_LDAP_OBJ_CLS = "agent.ldap.object.classes";
		public static final String GROUP_LDAP_OBJ_CLS = "group.ldap.object.classes";
		public static final String LDAP_REST_ADDRESS_ATTR = "ldap.rest.address.attribute";
		public static final String CONFIG_LDAP_DN_PREFIX = "config.ldap.dn.prefix";
	}

	public static class FILES {
		public static final String PROPERTIES_FILE = "config.properties";
	}

}
