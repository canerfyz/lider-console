package tr.org.liderahenk.liderconsole.core.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import tr.org.liderahenk.liderconsole.core.model.ParameterType;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplateParameter;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

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
		setTitle(Messages.getString("TEMPLATE_PARAMETER"));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));

		// Key
		Label lblKey = new Label(composite, SWT.NONE);
		lblKey.setText(Messages.getString("PARAM_KEY"));

		txtKey = new Text(composite, SWT.BORDER);
		txtKey.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		if (parameter != null && parameter.getKey() != null) {
			txtKey.setText(parameter.getKey());
		}

		// Label
		Label lblLabel = new Label(composite, SWT.NONE);
		lblLabel.setText(Messages.getString("PARAM_LABEL"));

		txtLabel = new Text(composite, SWT.BORDER);
		txtLabel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		if (parameter != null && parameter.getLabel() != null) {
			txtLabel.setText(parameter.getLabel());
		}

		// Type
		Label lblType = new Label(composite, SWT.NONE);
		lblType.setText(Messages.getString("PARAM_TYPE"));

		cmbType = new Combo(composite, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		ParameterType[] values = ParameterType.values();
		for (int i = 0; i < values.length; i++) {
			String i18n = Messages.getString(values[i].toString().toUpperCase(Locale.ENGLISH));
			cmbType.add(i18n);
			cmbType.setData(i18n, values[i]);
			if (parameter != null && parameter.getType() != null && parameter.getType() == values[i]) {
				cmbType.select(i);
			}
		}

		return composite;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {

		setReturnCode(OK);

		if (txtKey.getText().isEmpty() || txtLabel.getText().isEmpty() || cmbType.getSelectionIndex() < 0) {
			Notifier.error(null, Messages.getString("FILL_AT_LEAST_ONE_FIELD"));
			return;
		}

		boolean editMode = true;
		if (parameter == null) {
			parameter = new ReportTemplateParameter();
			editMode = false;
		}
		// Set values
		parameter.setKey(txtKey.getText());
		parameter.setLabel(txtLabel.getText());
		parameter.setType(getSelectedType());

		// Get previous parameters...
		List<ReportTemplateParameter> params = (List<ReportTemplateParameter>) tableViewer.getInput();
		if (params == null) {
			params = new ArrayList<ReportTemplateParameter>();
		}

		if (editMode) {
			int index = tableViewer.getTable().getSelectionIndex();
			if (index > -1) {
				// Override previous param!
				params.set(index, parameter);
			}
		} else {
			// New parameter!
			params.add(parameter);
		}

		tableViewer.setInput(params);
		tableViewer.refresh();

		close();
	}

	private ParameterType getSelectedType() {
		int selectionIndex = cmbType.getSelectionIndex();
		if (selectionIndex > -1 && cmbType.getItem(selectionIndex) != null
				&& cmbType.getData(cmbType.getItem(selectionIndex)) != null) {
			return (ParameterType) cmbType.getData(cmbType.getItem(selectionIndex));
		}
		return null;
	}

}
