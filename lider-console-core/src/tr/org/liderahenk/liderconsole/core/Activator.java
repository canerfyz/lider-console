package tr.org.liderahenk.liderconsole.core;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.logger.ILogger;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	
	private static final Logger logger = LoggerFactory.getLogger(Activator.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "tr.org.liderahenk.liderconsole.core"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/** The JNDI loggers. */
	private List<ILogger> loggers;

	/**
	 * Add frequently used images with their names here.
	 * Any plugin can reach them using ImageRegistry.
	 */
	public static final String[] IMAGES = { 
		"success", 
		"warning", 
		"information", 
		"error" 
		};
	
	public List<ILogger> getLoggers() {
//		if (loggers == null) {
//			loggers = new ArrayList<ILogger>();
//
//			IExtensionRegistry registry = Platform.getExtensionRegistry();
//			IExtensionPoint extensionPoint = registry
//					.getExtensionPoint(LiderConstants.ExtensionPoints.LOGGER);
//			IConfigurationElement[] members = extensionPoint
//					.getConfigurationElements();
//			for (IConfigurationElement member : members) {
//				try {
//					ILogger logger = (ILogger) member
//							.createExecutableExtension("class"); //$NON-NLS-1$
//					logger.setId(member.getAttribute("id")); //$NON-NLS-1$
//					logger.setName(member.getAttribute("name")); //$NON-NLS-1$
//					logger.setDescription(member.getAttribute("description")); //$NON-NLS-1$
//					loggers.add(logger);
//					
//					
//					getLog().log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Test asdfsf"));
//					
//					
//					
//				} catch (Exception e) {
//					getLog().log(
//							new Status(
//									IStatus.ERROR,
//									Activator.PLUGIN_ID,
//									1,
//									org.apache.directory.studio.connection.core.Messages.error__unable_to_create_jndi_logger
//											+ member.getAttribute("class"), e)); //$NON-NLS-1$
//				}
//			}
//		}
//
//		return loggers;
		return null;
	}

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		InputStream s = getClass().getClassLoader().getResourceAsStream("logback.xml");
		
//		ILoggerFactory factory = LoggerFactory.getILoggerFactory();
//		System.out.println(factory);
		
		// FIXME İlgili view'ların listenerlarının vs. init olabilmesi için bir
		// kere çağrılması gerekmekte. Muhtemelen başka bir best practice var.
		// bulana kadar idareten.
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getWorkbenchWindows()[0];
				IWorkbenchPage page = window.getActivePage();
				try {
					page.showView("org.apache.directory.studio.ldapbrowser.ui.views.connection.ConnectionView");
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
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
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		
		for (String s : IMAGES) {
			IPath path = new Path("icons/" + s);
			
			URL url = FileLocator.find(bundle, path, null);
			
			ImageDescriptor desc = ImageDescriptor.createFromURL(url);
			
			reg.put(s, desc);
		}
	}
}
