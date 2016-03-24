package tr.org.liderahenk.liderconsole.core.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;

/**
 * Provides i18n button names for extending dialogs.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class DefaultLiderDialog extends Dialog {

	protected DefaultLiderDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Override parent method in order to use i18n button names.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, Messages.getString("OK"), true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("CANCEL"), false);
	}

}
