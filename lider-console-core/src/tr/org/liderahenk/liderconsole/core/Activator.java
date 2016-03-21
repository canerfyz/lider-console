package tr.org.liderahenk.liderconsole.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.logger.ILogger;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 * 
 */
public class Activator extends AbstractUIPlugin {

	private static final Logger logger = LoggerFactory.getLogger(Activator.class);

	// The shared instance
	private static Activator plugin;

	/** The JNDI loggers. */
	private List<ILogger> loggers;

	public List<ILogger> getLoggers() {
		if (loggers == null) {
			loggers = new ArrayList<ILogger>();

			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint(LiderConstants.EXTENSION_POINTS.LOGGER);
			IConfigurationElement[] members = extensionPoint.getConfigurationElements();
			for (IConfigurationElement member : members) {
				try {
					ILogger logger = (ILogger) member.createExecutableExtension("class"); //$NON-NLS-1$
					logger.setId(member.getAttribute("id")); //$NON-NLS-1$
					logger.setName(member.getAttribute("name")); //$NON-NLS-1$
					logger.setDescription(member.getAttribute("description"));
					// $NON-NLS-1$
					loggers.add(logger);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		return loggers;
	}

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
