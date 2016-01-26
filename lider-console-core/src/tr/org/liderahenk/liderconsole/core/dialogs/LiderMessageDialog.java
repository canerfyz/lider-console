package tr.org.liderahenk.liderconsole.core.dialogs;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 * 
 * MysMessageDialog class with custom size and dialog icons.
 *
 */
public class LiderMessageDialog extends MessageDialog {

	public LiderMessageDialog(Shell parentShell, String dialogTitle,
			Image dialogTitleImage, String dialogMessage, int dialogImageType,
			String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
				dialogImageType, dialogButtonLabels, defaultIndex);
	}
	
    @Override
    protected Point getInitialSize() {
    	return new Point(400, 180);
    }

	public static boolean open(int kind, Shell parent, String title,
			String message, int style) {
		LiderMessageDialog dialog = new LiderMessageDialog(parent, title, null, message,
				kind, getButtonLabels(kind), 0);
		
		return dialog.open() == 0;
	}
	
    public static void openError(Shell parent, String title, String message) {
        open(ERROR, parent, title, message, SWT.NONE);
    }
    
    @Override
	public Image getInfoImage() {
		return getImageFromResource("done_64.png");
	}
    
	public Image getErrorImage() {
		return getImageFromResource("error_64.png");
	}
	
	public Image getWarningImage() {
		return getImageFromResource("warning_64.png");
	}
    
	private static Image getImageFromResource(String file) {
		Bundle bundle = FrameworkUtil.getBundle(LiderMessageDialog.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null); //$NON-NLS-1$
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();
	}	
    
    public static void openInformation(Shell parent, String title,
            String message) {
        open(INFORMATION, parent, title, message, SWT.NONE);
    }
    
    public static boolean openQuestion(Shell parent, String title,
            String message) {
        return open(QUESTION, parent, title, message, SWT.NONE);
    }
    
    public static void openWarning(Shell parent, String title, String message) {
        open(WARNING, parent, title, message, SWT.NONE);
    }
    
    public static boolean openConfirm(Shell parent, String title, String message) {
        return open(CONFIRM, parent, title, message, SWT.NONE);
    }
    
	static String[] getButtonLabels(int kind) {
		String[] dialogButtonLabels;
		switch (kind) {
		case ERROR:
		case INFORMATION:
		case WARNING: {
			dialogButtonLabels = new String[] { IDialogConstants.OK_LABEL };
			break;
		}
		case CONFIRM: {
			dialogButtonLabels = new String[] { IDialogConstants.OK_LABEL,
					IDialogConstants.CANCEL_LABEL };
			break;
		}
		case QUESTION: {
			dialogButtonLabels = new String[] { IDialogConstants.YES_LABEL,
					IDialogConstants.NO_LABEL };
			break;
		}
		case QUESTION_WITH_CANCEL: {
			dialogButtonLabels = new String[] { IDialogConstants.YES_LABEL,
                    IDialogConstants.NO_LABEL,
                    IDialogConstants.CANCEL_LABEL };
			break;
		}
		default: {
			throw new IllegalArgumentException(
					"Illegal value for kind in MessageDialog.open()"); //$NON-NLS-1$
		}
		}
		return dialogButtonLabels;
	}
}
