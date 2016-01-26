package tr.org.liderahenk.liderconsole.core.labelproviders;

import java.net.URL;

import javax.naming.directory.SearchResult;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import tr.org.liderahenk.liderconsole.core.ldap.LdapUtils;
import tr.org.liderahenk.liderconsole.core.listeners.LdapConnectionListener;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class LdapSearchLabelProvider extends ColumnLabelProvider {

	private static final Image FOLDER = getImage("folder-open.png");
	private static final Image USER = getImage("user.png");
	private static final Image AHENK = getImage("chart-flow.png");

	@Override
	public String getText(Object element) {
		if (element instanceof SearchResult) {
			return ((SearchResult) element).getName();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		Image image = FOLDER;
		if (element instanceof SearchResult) {
			if (LdapUtils.getInstance().isAgent(((SearchResult) element).getName(),
					LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor())) {
				image = AHENK;
			} else if (LdapUtils.getInstance().isUser(((SearchResult) element).getName(),
					LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor())) {
				image = USER;
			}
		}
		return image;
	}

	// Helper Method to load the images
	private static Image getImage(String file) {
		Bundle bundle = FrameworkUtil.getBundle(SubBrowserLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();
	}

}
