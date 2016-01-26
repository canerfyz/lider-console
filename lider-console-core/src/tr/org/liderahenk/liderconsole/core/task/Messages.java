package tr.org.liderahenk.liderconsole.core.task;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	/*
	 * Attribute_Command_Action convention for internalization of tasks
	 * */
	
//	public static String SCRIPT_RUN_DB; 
//			//"Run Script Task";	
	
	private static final String BUNDLE_NAME = "tr.org.liderahenk.liderconsole.core.task.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
