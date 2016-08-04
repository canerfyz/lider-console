package tr.org.liderahenk.liderconsole.core.dialogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import tr.org.liderahenk.liderconsole.core.model.ReportViewParameter;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

public class ReportViewParamDialog extends DefaultLiderTitleAreaDialog {

	// Model
	private ReportViewParameter parameter;
	private ArrayList<ReportTemplateParameter> params;
	// Table
	private TableViewer tableViewer;
	// Widgets
	private Combo cmbReferencedParam;
	private Text txtLabel;
	private Text txtValue;

	public ReportViewParamDialog(Shell parentShell, TableViewer tableViewer,
			ArrayList<ReportTemplateParameter> params) {
		super(parentShell);
		this.tableViewer = tableViewer;
		this.params = params;
	}

	public ReportViewParamDialog(Shell parentShell, ReportViewParameter parameter, TableViewer tableViewer,
			ArrayList<ReportTemplateParameter> params) {
		super(parentShell);
		this.parameter = parameter;
		this.tableViewer = tableViewer;
		this.params = params;
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.getString("VIEW_PARAMETER"));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));

		// Referenced parameter
		Label lblReferencedParam = new Label(composite, SWT.NONE);
		lblReferencedParam.setText(Messages.getString("REFERENCED_PARAMETER"));

		cmbReferencedParam = new Combo(composite, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		if (params != null) {
			boolean selected = false;
			for (int i = 0; i < params.size(); i++) {
				ReportTemplateParameter param = params.get(i);
				cmbReferencedParam.add(param.getKey() + " - " + param.getLabel());
				cmbReferencedParam.setData(i + "", param);
				if (!selected && parameter != null && parameter.getReferencedParameterId().equals(param.getId())) {
					cmbReferencedParam.select(i);
					selected = true;
				}
			}
			if (!selected) {
				cmbReferencedParam.select(0);
			}
		}
		cmbReferencedParam.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// Label
		Label lblLabel = new Label(composite, SWT.NONE);
		lblLabel.setText(Messages.getString("PARAM_LABEL"));

		txtLabel = new Text(composite, SWT.BORDER);
		txtLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		if (parameter != null && parameter.getLabel() != null) {
			txtLabel.setText(parameter.getLabel());
		}

		// Value
		Label lblValue = new Label(composite, SWT.NONE);
		lblValue.setText(Messages.getString("PARAM_VALUE"));

		txtValue = new Text(composite, SWT.BORDER);
		txtValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		if (parameter != null && parameter.getValue() != null) {
			txtValue.setText(parameter.getValue());
		}

		handleSelection();
		return composite;
	}

	protected void handleSelection() {
		ReportTemplateParameter param = (ReportTemplateParameter) getSelectedValue(cmbReferencedParam);
		if (param != null) {
			txtLabel.setText(param.getLabel());
			if (param.getDefaultValue() != null) {
				txtValue.setText(param.getDefaultValue());
			}
			if (param.getType() == ParameterType.DATE) {
				txtValue.setToolTipText("yyyy-MM-dd HH:mm:ss");
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void okPressed() {

		setReturnCode(OK);

		if (txtLabel.getText().isEmpty() || txtValue.getText().isEmpty()
				|| cmbReferencedParam.getSelectionIndex() < 0) {
			Notifier.error(null, Messages.getString("FILL_ALL_FIELDS"));
			return;
		}

		boolean editMode = true;
		if (parameter == null) {
			parameter = new ReportViewParameter();
			editMode = false;
		}
		// Set values
		parameter.setLabel(txtLabel.getText());
		parameter.setTimestamp(new Date());
		parameter.setValue(txtValue.getText());
		parameter.setReferencedParameterId(((ReportTemplateParameter) getSelectedValue(cmbReferencedParam)).getId());

		// Get previous parameters...
		List<ReportViewParameter> params = (List<ReportViewParameter>) tableViewer.getInput();
		if (params == null) {
			params = new ArrayList<ReportViewParameter>();
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

	private Object getSelectedValue(Combo combo) {
		int selectionIndex = combo.getSelectionIndex();
		if (selectionIndex > -1 && combo.getItem(selectionIndex) != null
				&& combo.getData(selectionIndex + "") != null) {
			return combo.getData(selectionIndex + "");
		}
		return null;
	}

}
