package tr.org.liderahenk.liderconsole.core.dialogs;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 * MysProgressMonitorDialog class with custom info image.
 */
public class LiderProgressMonitorDialog extends ProgressMonitorDialog {

	public LiderProgressMonitorDialog(Shell parent) {
		super(parent);		
	}
	
	@Override
	protected Image getImage() {
		return getImageFromResource("services_64.png");
	}
	
	private static Image getImageFromResource(String file) {
		Bundle bundle = FrameworkUtil.getBundle(LiderMessageDialog.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null); //$NON-NLS-1$
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();
	}
}
