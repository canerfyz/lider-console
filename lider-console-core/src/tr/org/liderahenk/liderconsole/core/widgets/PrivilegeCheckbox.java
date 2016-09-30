package tr.org.liderahenk.liderconsole.core.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class PrivilegeCheckbox extends Button {

	private String code;

	public PrivilegeCheckbox(Composite parent, String code) {
		super(parent, SWT.CHECK);
		this.code = code;
	}

	@Override
	protected void checkSubclass() {
		// By default, subclassing is not allowed for many of the SWT Controls
		// This empty method disables the check that prevents subclassing of
		// this class
	}

	public String getCode() {
		return code;
	}

}
