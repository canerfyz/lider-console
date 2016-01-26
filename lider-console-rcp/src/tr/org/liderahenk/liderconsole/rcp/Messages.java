package tr.org.liderahenk.liderconsole.rcp;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "tr.org.liderahenk.liderconsole.rcp.messages"; //$NON-NLS-1$
	public static String ApplicationActionBarAdvisor_EDIT;
	public static String ApplicationActionBarAdvisor_FILE;
	public static String ApplicationActionBarAdvisor_HELP;
	public static String ApplicationActionBarAdvisor_HIDDEN;
	public static String ApplicationActionBarAdvisor_NAVIGATE;
	public static String ApplicationActionBarAdvisor_NEW;
	public static String ApplicationActionBarAdvisor_OPEN_PERSPECTIVE;
	public static String ApplicationActionBarAdvisor_SHOW_VIEW;
	public static String ApplicationActionBarAdvisor_WINDOW;
	static {
		// Initialise resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
