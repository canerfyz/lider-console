package tr.org.liderahenk.liderconsole.core.dialogs;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplate;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplateColumn;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplateParameter;
import tr.org.liderahenk.liderconsole.core.rest.requests.ReportGenerationRequest;
import tr.org.liderahenk.liderconsole.core.rest.utils.ReportUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

public class ReportGenerationDialog extends DefaultLiderDialog {

	private static final Logger logger = LoggerFactory.getLogger(ReportGenerationDialog.class);

	// Model
	private ReportTemplate selectedTemplate;
	// Widgets
	private Composite paramContainer;
	private Composite tableContainer;
	private Button btnGenerateReport;
	private Label lblResult;

	private static final int DEFAULT_COLUMN_WIDTH = 100;

	public ReportGenerationDialog(Shell parentShell, ReportTemplate selectedTemplate) {
		super(parentShell);
		this.selectedTemplate = selectedTemplate;
	}

	/**
	 * Create template input widgets
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parent.setLayout(new GridLayout(1, false));

		// Report parameters label
		Label lblParam = new Label(parent, SWT.NONE);
		lblParam.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblParam.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblParam.setText(Messages.getString("REPORT_PARAMETERS"));

		paramContainer = new Composite(parent, SWT.BORDER);
		paramContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		paramContainer.setLayout(new GridLayout(2, false));

		Set<ReportTemplateParameter> params = selectedTemplate.getTemplateParams();
		if (params != null) {
			for (ReportTemplateParameter param : params) {
				// Param label
				Label lbl = new Label(paramContainer, SWT.NONE);
				lbl.setText(param.getLabel());

				// Param input
				Text txt = new Text(paramContainer, SWT.BORDER);
				txt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				// Associate parameter key with this input
				txt.setData(param.getKey());
			}
		}

		// Report results label
		lblResult = new Label(parent, SWT.NONE);
		lblResult.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblResult.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		lblResult.setText(Messages.getString("REPORT_RESULT"));
		lblResult.setVisible(false);

		tableContainer = new Composite(parent, SWT.NONE);
		tableContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableContainer.setLayout(new GridLayout(2, false));

		applyDialogFont(parent);
		return parent;
	}

	protected void generateTable(List<Object[]> list) {
		// Dispose previous table!
		disposePrev(tableContainer);

		if (list != null && !list.isEmpty()) {

			lblResult.setVisible(true);

			TableViewer tableViewer = new TableViewer(tableContainer,
					SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

			createTableColumns(tableViewer, list);

			// Configure table
			final Table table = tableViewer.getTable();
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			table.getVerticalBar().setEnabled(true);
			table.getVerticalBar().setVisible(true);
			tableViewer.setContentProvider(new ArrayContentProvider());

			// Populate table
			tableViewer.setInput(list);
			tableViewer.refresh();

			// Set table layout
			GridData gridData = new GridData();
			gridData.horizontalSpan = 3;
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			gridData.heightHint = 300;
			gridData.horizontalAlignment = GridData.FILL;
			tableViewer.getControl().setLayoutData(gridData);

			// Redraw table
			tableContainer.layout(true, true);
		} else {
			lblResult.setVisible(false);
			disposePrev(tableContainer);
		}
	}

	static Object[] convertToObjectArray(Object array) {
	    Class ofArray = array.getClass().getComponentType();
	    if (ofArray.isPrimitive()) {
	        List ar = new ArrayList();
	        int length = Array.getLength(array);
	        for (int i = 0; i < length; i++) {
	            ar.add(Array.get(array, i));
	        }
	        return ar.toArray();
	    }
	    else {
	        return (Object[]) array;
	    }
	}
	
	/**
	 * 
	 * 
	 * @param tableViewer
	 * @param list
	 *            a collection of report fields
	 */
	private void createTableColumns(TableViewer tableViewer, List<Object[]> list) {

		Set<ReportTemplateColumn> columns = selectedTemplate.getTemplateColumns();
		if (columns != null && !columns.isEmpty()) {
			for (final ReportTemplateColumn c : columns) {
				TableViewerColumn column = createTableViewerColumn(tableViewer, c.getName(),
						c.getWidth() != null ? c.getWidth() : DEFAULT_COLUMN_WIDTH);
				column.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						if (element instanceof Object[]) {
							Object[] convertToObjectArray = convertToObjectArray(element);
							return convertToObjectArray[c.getColumnOrder()-1] == null ? Messages.getString("UNTITLED") : convertToObjectArray[c.getColumnOrder()-1].toString();
						}
						return Messages.getString("UNTITLED");
					}
				});
			}
		} else {
			// No column defined in the template, we should display all the
			// fields!
			for (int i = 0; i < list.get(0).length; i++) {
				TableViewerColumn column = createTableViewerColumn(tableViewer, "", DEFAULT_COLUMN_WIDTH);
				final int j = i;
				column.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						if (element instanceof Object[]) {
							Object[] curRow = (Object[]) element;
							return curRow[j] != null ? curRow[j].toString() : "";
						}
						return Messages.getString("UNTITLED");
					}
				});
			}
		}
	}

	/**
	 * Create new table viewer column instance.
	 * 
	 * @param title
	 * @param bound
	 * @return
	 */
	private TableViewerColumn createTableViewerColumn(TableViewer tableViewer, String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		column.setAlignment(SWT.LEFT);
		return viewerColumn;
	}

	/**
	 * Dispose previous table if exists
	 * 
	 * @param composite
	 */
	private void disposePrev(Composite composite) {
		Control[] children = composite.getChildren();
		if (children != null) {
			for (Control child : children) {
				child.dispose();
			}
		}
	}

	protected boolean validateInputs() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// Button for generating report table
		btnGenerateReport = createButton(parent, 5000, Messages.getString("GENERATE_REPORT"), false);
		btnGenerateReport.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/report.png"));
		GridData gridData = new GridData();
		gridData.widthHint = 140;
		btnGenerateReport.setLayoutData(gridData);
		btnGenerateReport.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!validateInputs()) {
					return;
				}
				// Collect parameter values
				Map<String, Object> paramValues = new HashMap<String, Object>();

				Control[] children = paramContainer.getChildren();
				for (Control control : children) {
					if (control instanceof Text) {
						Text t = (Text) control;
						String key = t.getData().toString();
						paramValues.put(key, t.getText());
					}
				}

				// Send parameter values and template ID to generate report!
				ReportGenerationRequest report = new ReportGenerationRequest();
				report.setTemplateId(selectedTemplate.getId());
				report.setParamValues(paramValues);

				try {
					List<Object[]> list = ReportUtils.generate(report);
					generateTable(list);
				} catch (Exception e1) {
					logger.error(e1.getMessage(), e1);
					Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("CANCEL"), true);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

}
