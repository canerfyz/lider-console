package tr.org.liderahenk.liderconsole.core;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 * 
 */
public class Activator extends AbstractUIPlugin {

	// The shared instance
	private static Activator plugin;

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
		configureLogger();
	}

	private void configureLogger() {
		try {
			Properties prop = new Properties();
			try {
				// Config file is in the same folder as the start script
				prop.load(new FileInputStream(LiderConstants.FILES.LOG_FILE));
			} catch (Exception ex) {
				// Config file is in the .jar file
				prop.load(Activator.class.getClassLoader().getResourceAsStream(LiderConstants.FILES.LOG_FILE));
			}
			PropertyConfigurator.configure(prop);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
