package tr.org.liderahenk.liderconsole.core.labelproviders;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import tr.org.liderahenk.liderconsole.core.model.SubBrowserItem;


public class SubBrowserLabelProvider extends LabelProvider {

	private static final Image FOLDER = getImage("folder-open.png");
	private static final Image USER = getImage("user.gif");
	private static final Image AHENK = getImage("ahenk.gif");

	@Override
	public String getText(Object element) {
		String retVal = "Untitled";
		if (element instanceof SubBrowserItem) {
			SubBrowserItem ss = (SubBrowserItem) element;
			retVal = ss.getTitle();
		}
		return retVal;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		Image image = FOLDER;
		if (element instanceof SubBrowserItem) {
			SubBrowserItem subBrowserItem = (SubBrowserItem) element;
			String type = subBrowserItem.getType();
			if ("USER".equals(type)) {
				image = USER;
			}
			
			if ("AHENK".equals(type)) {
				image = AHENK;
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
