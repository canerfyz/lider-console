package tr.org.liderahenk.liderconsole.core.i18n;

import org.eclipse.osgi.util.NLS;

public class Messages_NLS extends NLS {
	private static final String BUNDLE_NAME = "tr.org.liderahenk.liderconsole.core.i18n.messages"; //$NON-NLS-1$
	public static String CrontabDialog_CUSTOM;
	public static String CrontabDialog_DAILY;
	public static String CrontabDialog_DAY_OF_MONTH;
	public static String CrontabDialog_DAY_OF_WEEK;
	public static String CrontabDialog_HELP;
	public static String CrontabDialog_HOUR;
	public static String CrontabDialog_HOURLY;
	public static String CrontabDialog_MINUTE;
	public static String CrontabDialog_MONTH;
	public static String CrontabDialog_MONTHLY;
	public static String CrontabDialog_PROVIDE_CRONTAB_EXECUTION_PARAMETERS;
	public static String CrontabDialog_REBOOT;
	public static String CrontabDialog_SCHEDULING;
	public static String CrontabDialog_SCHEDULING_ARGUMENTS;
	public static String CrontabDialog_WEEKLY;
	public static String CrontabDialog_YEARLY;
	public static String CrontabDialog_ONCE;
	public static String CrontabDialog_DATE;
	public static String LiderConsolePreferences;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages_NLS.class);
	}

	private Messages_NLS() {
	}
}
