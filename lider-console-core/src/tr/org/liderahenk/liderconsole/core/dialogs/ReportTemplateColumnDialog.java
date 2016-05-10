package tr.org.liderahenk.liderconsole.core.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplateColumn;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

public class ReportTemplateColumnDialog extends DefaultLiderTitleAreaDialog {

	// Model
	private ReportTemplateColumn column;
	// Table
	private TableViewer tableViewer;
	// Widgets
	private Spinner spnOrder;
	private Text txtName;
	private Button btnVisible;
	private Spinner spnWidth;

	public ReportTemplateColumnDialog(Shell parentShell, TableViewer tableViewer) {
		super(parentShell);
		this.tableViewer = tableViewer;
	}

	public ReportTemplateColumnDialog(Shell parentShell, ReportTemplateColumn column, TableViewer tableViewer) {
		super(parentShell);
		this.column = column;
		this.tableViewer = tableViewer;
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.getString("TEMPLATE_COLUMN"));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));

		// Column order
		Label lblOrder = new Label(composite, SWT.NONE);
		lblOrder.setText(Messages.getString("COLUMN_ORDER"));

		spnOrder = new Spinner(composite, SWT.BORDER);
		spnOrder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		spnOrder.setMinimum(1);
		if (column != null && column.getColumnOrder() != null) {
			spnOrder.setSelection(column.getColumnOrder());
		}

		// Name
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setText(Messages.getString("COLUMN_NAME"));

		txtName = new Text(composite, SWT.BORDER);
		txtName.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		if (column != null && column.getName() != null) {
			txtName.setText(column.getName());
		}

		// Visible
		Label lblVisible = new Label(composite, SWT.NONE);
		lblVisible.setText(Messages.getString("VISIBLE"));
		
		btnVisible = new Button(composite, SWT.CHECK);
		btnVisible.setSelection((column != null && column.isVisible()) || column == null);

		// Width
		Label lblWidth = new Label(composite, SWT.NONE);
		lblWidth.setText(Messages.getString("WIDTH"));

		spnWidth = new Spinner(composite, SWT.BORDER);
		spnWidth.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		spnWidth.setMinimum(20);
		if (column != null && column.getWidth() != null) {
			spnWidth.setSelection(column.getWidth());
		}

		return composite;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {

		setReturnCode(OK);

		if (txtName.getText().isEmpty()) {
			Notifier.error(null, Messages.getString("FILL_NAME_FIELD"));
			return;
		}

		boolean editMode = true;
		if (column == null) {
			column = new ReportTemplateColumn();
			editMode = false;
		}
		// Set values
		column.setName(txtName.getText());
		column.setVisible(btnVisible.getSelection());
		column.setColumnOrder(spnOrder.getSelection());
		column.setWidth(spnWidth.getSelection());

		// Get previous columns...
		List<ReportTemplateColumn> columns = (List<ReportTemplateColumn>) tableViewer.getInput();
		if (columns == null) {
			columns = new ArrayList<ReportTemplateColumn>();
		}

		if (editMode) {
			int index = tableViewer.getTable().getSelectionIndex();
			if (index > -1) {
				// Override previous column!
				columns.set(index, column);
			}
		} else {
			// New parameter!
			columns.add(column);
		}

		tableViewer.setInput(columns);
		tableViewer.refresh();

		close();
	}

}
