package tr.org.liderahenk.liderconsole.core.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.NLS;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;

/**
 * Provides i18n messages for Lider Console Core strings.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "tr.org.liderahenk.liderconsole.core.i18n.messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private static Map<String, String> contributions = null;

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			// If we cannot find the message in properties file,
			// it may be contributed by a plugin.xml.
			// See tr.org.liderahenk.liderconsole.core.i18n.exsd for more info!
			try {
				if (contributions == null) {
					populateContributions();
				}
				return findContribution(key);
			} catch (MissingResourceException e1) {
				return '!' + key + '!';
			}
		}
	}

	private static void populateContributions() {
		contributions = new HashMap<String, String>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(LiderConstants.EXTENSION_POINTS.I18N);
		IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
		if (elements != null) {
			for (IConfigurationElement element : elements) {
				if (element.getName().equalsIgnoreCase("task")) {
					String key = element.getAttribute("code");
					String value = element.getAttribute("label");
					contributions.put(key.toUpperCase(Locale.ENGLISH), value);
				} else if (element.getName().equalsIgnoreCase("plugin")) {
					String key = element.getAttribute("name");
					String value = element.getAttribute("label");
					contributions.put(key.toUpperCase(Locale.ENGLISH), value);
				}
			}
		}
	}

	private static String findContribution(String key) {
		String message = null;
		String k = key.toUpperCase(Locale.ENGLISH);
		if (contributions.containsKey(k)) {
			message = contributions.get(k);
		}
		if (message == null || message.isEmpty()) {
			throw new MissingResourceException("Cannot find contribution for key", Messages.class.getName(), key);
		}
		return message;
	}

	/**
	 * Returns a formatted string using the specified message string and
	 * arguments.<br/>
	 * <br/>
	 * 
	 * <b>Example:</b><br/>
	 * messages_tr.properties:<br/>
	 * ROSTER_ONLINE=%s çevrimiçi oldu<br/>
	 * 
	 * usage:<br/>
	 * Messages.getString("ROSTER_ONLINE", dn)
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	public static String getString(String key, Object... args) {
		return String.format(getString(key), args);
	}

}
