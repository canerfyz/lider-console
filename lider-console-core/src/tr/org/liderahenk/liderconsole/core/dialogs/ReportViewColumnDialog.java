package tr.org.liderahenk.liderconsole.core.dialogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplateColumn;
import tr.org.liderahenk.liderconsole.core.model.ReportViewColumn;
import tr.org.liderahenk.liderconsole.core.model.ViewColumnType;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

public class ReportViewColumnDialog extends DefaultLiderTitleAreaDialog {

	// Model
	private ReportViewColumn column;
	private ArrayList<ReportTemplateColumn> columns;
	// Table
	private TableViewer tableViewer;
	// Widgets
	private Combo cmbReferencedColumn;
	private Combo cmbType;
	private Text txtLegend;
	private Spinner spnWidth;

	public ReportViewColumnDialog(Shell parentShell, TableViewer tableViewer, ArrayList<ReportTemplateColumn> columns) {
		super(parentShell);
		this.tableViewer = tableViewer;
		this.columns = columns;
	}

	public ReportViewColumnDialog(Shell parentShell, ReportViewColumn column, TableViewer tableViewer,
			ArrayList<ReportTemplateColumn> columns) {
		super(parentShell);
		this.column = column;
		this.tableViewer = tableViewer;
		this.columns = columns;
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.getString("VIEW_COLUMN"));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));

		// Referenced column
		Label lblReferencedColumn = new Label(composite, SWT.NONE);
		lblReferencedColumn.setText(Messages.getString("REFERENCED_COLUMN"));

		cmbReferencedColumn = new Combo(composite, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		if (columns != null) {
			boolean selected = false;
			for (int i = 0; i < columns.size(); i++) {
				ReportTemplateColumn col = columns.get(i);
				cmbReferencedColumn.add(col.getColumnOrder() + " - " + col.getName());
				cmbReferencedColumn.setData(i + "", col);
				if (!selected && column != null && column.getReferencedColumnId().equals(col.getId())) {
					cmbReferencedColumn.select(i);
					selected = true;
				}
			}
			if (!selected) {
				cmbReferencedColumn.select(0);
			}
		}

		// Type
		Label lblType = new Label(composite, SWT.NONE);
		lblType.setText(Messages.getString("COLUMN_TYPE"));

		cmbType = new Combo(composite, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		ViewColumnType[] types = ViewColumnType.values();
		boolean selected = false;
		for (int i = 0; i < types.length; i++) {
			ViewColumnType type = types[i];
			cmbType.add(type.getMessage());
			cmbType.setData(i + "", type);
			if (!selected && column != null && column.getType() == type) {
				cmbType.select(i);
				selected = true;
			}
		}
		if (!selected) {
			cmbType.select(0);
		}

		// Legend
		Label lblLegend = new Label(composite, SWT.NONE);
		lblLegend.setText(Messages.getString("LEGEND"));

		txtLegend = new Text(composite, SWT.BORDER);
		txtLegend.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		if (column != null && column.getLegend() != null) {
			txtLegend.setText(column.getLegend());
		}

		Label lblWidth = new Label(composite, SWT.NONE);
		lblWidth.setText(Messages.getString("WIDTH"));

		// Width
		spnWidth = new Spinner(composite, SWT.BORDER);
		spnWidth.setMinimum(0);
		spnWidth.setMaximum(1000);
		spnWidth.setSelection(100);
		if (column != null && column.getWidth() != null) {
			spnWidth.setSelection(Integer.parseInt(column.getWidth().toString()));
		}

		return composite;
	}

	@SuppressWarnings("unchecked")
	protected void okPressed() {

		setReturnCode(OK);

		if (cmbType.getText().isEmpty()) {
			Notifier.error(null, Messages.getString("SELECT_COLUMN_TYPE"));
			return;
		}
		if (cmbReferencedColumn.getSelectionIndex() < 0) {
			Notifier.error(null, Messages.getString("SELECT_REFERENCED_COLUMN"));
			return;
		}

		boolean editMode = true;
		if (column == null) {
			column = new ReportViewColumn();
			editMode = false;
		}
		// Set values
		column.setLegend(txtLegend.getText());
		column.setTimestamp(new Date());
		column.setWidth(spnWidth.getSelection());
		column.setReferencedCol((ReportTemplateColumn) getSelectedValue(cmbReferencedColumn));
		column.setType((ViewColumnType) getSelectedValue(cmbType));

		// Get previous parameters...
		List<ReportViewColumn> columns = (List<ReportViewColumn>) tableViewer.getInput();
		if (columns == null) {
			columns = new ArrayList<ReportViewColumn>();
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

	private Object getSelectedValue(Combo combo) {
		int selectionIndex = combo.getSelectionIndex();
		if (selectionIndex > -1 && combo.getItem(selectionIndex) != null
				&& combo.getData(selectionIndex + "") != null) {
			return combo.getData(selectionIndex + "");
		}
		return null;
	}

}
