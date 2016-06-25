package tr.org.liderahenk.liderconsole.core.widgets;

import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;

/**
 * Provides confirm box that can be used by plugins.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class LiderConfirmBox extends MessageDialog {

	public LiderConfirmBox(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels,
				defaultIndex);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 180);
	}

	public static boolean open(Shell parent, String title, String message) {
		LiderConfirmBox confirm = new LiderConfirmBox(parent, title,
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/32/warning.png"),
				message, MessageDialog.QUESTION,
				new String[] { JFaceResources.getString(IDialogLabelKeys.YES_LABEL_KEY),
						JFaceResources.getString(IDialogLabelKeys.NO_LABEL_KEY) },
				SWT.NONE);
		return confirm.open() == 0;
	}

}
