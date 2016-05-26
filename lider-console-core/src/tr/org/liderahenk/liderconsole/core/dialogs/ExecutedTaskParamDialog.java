package tr.org.liderahenk.liderconsole.core.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.model.Command;
import tr.org.liderahenk.liderconsole.core.utils.PrettyPrintingMap;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ExecutedTaskParamDialog extends DefaultLiderDialog {

	private Command command;

	protected ExecutedTaskParamDialog(Shell parentShell, Command command) {
		super(parentShell);
		this.command = command;
	}

	/**
	 * Create executed task params widget
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		parent.setLayout(new GridLayout(1, false));

		Text txtParams = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		txtParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtParams.setText(new PrettyPrintingMap<String, Object>(command.getTask().getParameterMap()).toString());

		applyDialogFont(parent);
		return parent;
	}

}
