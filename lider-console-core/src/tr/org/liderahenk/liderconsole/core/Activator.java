package tr.org.liderahenk.liderconsole.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
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

	/**
	 * Add frequently used images with their names here. Any plugin can reach
	 * them using ImageRegistry.
	 */
	public static final String[] IMAGES = { "add", "cancel", "delete", "done", "edit", "save", "send", "warning",
			"refresh" };

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
		// Display.getDefault().asyncExec(new Runnable() {
		// @Override
		// public void run() {
		// IWorkbench workbench = PlatformUI.getWorkbench();
		// IWorkbenchWindow window = workbench.getWorkbenchWindows()[0];
		// IWorkbenchPage page = window.getActivePage();
		// try {
		// page.showView("org.apache.directory.studio.ldapbrowser.ui.views.connection.ConnectionView");
		// } catch (PartInitException e) {
		// e.printStackTrace();
		// }
		// }
		// });
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

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path, String pluginId) {
		return imageDescriptorFromPlugin(pluginId == null ? LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE : pluginId,
				path);
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		Bundle bundle = Platform.getBundle(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE);
		for (String s : IMAGES) {
			IPath path = new Path("icons/32/" + s);
			URL url = FileLocator.find(bundle, path, null);
			ImageDescriptor desc = ImageDescriptor.createFromURL(url);
			reg.put(s, desc);
		}
	}

}
