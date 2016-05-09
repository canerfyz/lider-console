package tr.org.liderahenk.liderconsole.core.dialogs;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplateParameter;

public class ReportTemplateParamDialog extends DefaultLiderTitleAreaDialog {

	// Model
	private ReportTemplateParameter parameter;
	// Table
	private TableViewer tableViewer;
	// Widgets
	private Text txtKey;
	private Text txtLabel;
	private Combo cmbType;

	public ReportTemplateParamDialog(Shell parentShell, TableViewer tableViewer) {
		super(parentShell);
		this.tableViewer = tableViewer;
	}

	public ReportTemplateParamDialog(Shell parentShell, ReportTemplateParameter parameter, TableViewer tableViewer) {
		super(parentShell);
		this.parameter = parameter;
		this.tableViewer = tableViewer;
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.getString(""));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));

		Label lblKey = new Label(composite, SWT.NONE);
		lblKey.setText(Messages.getString("PARAM_KEY"));

		txtKey = new Text(composite, SWT.BORDER);
		txtKey.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		if (parameter != null && parameter.getKey() != null) {
			txtKey.setText(parameter.getKey());
		}

		Label lblLabel = new Label(composite, SWT.NONE);
		lblLabel.setText(Messages.getString("PARAM_LABEL"));

		txtLabel = new Text(composite, SWT.BORDER);
		txtLabel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		if (parameter != null && parameter.getLabel() != null) {
			txtLabel.setText(parameter.getLabel());
		}

		Label lblType = new Label(composite, SWT.NONE);
		lblType.setText(Messages.getString("PARAM_TYPE"));

		// COMBO!!!

		return composite;
	}

}
