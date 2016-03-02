package tr.org.liderahenk.liderconsole.core.views;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class PolicyMenuDialog extends TitleAreaDialog {
	
	private String ahenkDn;

	public PolicyMenuDialog(Shell parentShell, String ahenkDn) {
		super(parentShell);
		this.ahenkDn = ahenkDn;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite composite = new Composite(area, GridData.FILL);

		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

	
	}

	@Override
	public void create() {
		super.create();
		setMessage(ahenkDn);
	}
}

