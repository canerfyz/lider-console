package tr.org.liderahenk.liderconsole.core.dialogs;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.editors.ReportTemplateEditor;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplate;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplateColumn;
import tr.org.liderahenk.liderconsole.core.model.ReportTemplateParameter;
import tr.org.liderahenk.liderconsole.core.rest.requests.ReportTemplateRequest;
import tr.org.liderahenk.liderconsole.core.rest.utils.ReportUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

public class ReportTemplateDialog extends DefaultLiderDialog {

	private static final Logger logger = LoggerFactory.getLogger(ReportTemplateDialog.class);

	private ReportTemplate selectedTemplate;
	private ReportTemplateParameter selectedParam;
	private ReportTemplateEditor editor;

	private Text txtName;
	private Text txtDesc;
	private Text txtQuery;
	private Text txtReportHeader;
	private Text txtReportFooter;
	private TableViewer tvParam;
	private TableViewer tvCol;
	private Button btnAddParam;
	private Button btnEditParam;
	private Button btnDeleteParam;

	public ReportTemplateDialog(Shell parentShell, ReportTemplateEditor editor) {
		super(parentShell);
		this.editor = editor;
	}

	public ReportTemplateDialog(Shell parentShell, ReportTemplate selectedTemplate, ReportTemplateEditor editor) {
		super(parentShell);
		this.selectedTemplate = selectedTemplate;
		this.editor = editor;
	}

	/**
	 * Create template input widgets
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		parent.setLayout(new GridLayout(1, false));

		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		composite.setLayout(new GridLayout(2, false));

		// Name
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setText(Messages.getString("REPORT_NAME"));

		txtName = new Text(composite, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		if (selectedTemplate != null) {
			txtName.setText(selectedTemplate.getName());
		}

		// Description
		Label lblDesc = new Label(composite, SWT.NONE);
		lblDesc.setText(Messages.getString("DESCRIPTION"));

		txtDesc = new Text(composite, SWT.BORDER);
		txtDesc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		if (selectedTemplate != null) {
			txtDesc.setText(selectedTemplate.getDescription());
		}

		// Query
		Label lblQuery = new Label(composite, SWT.NONE);
		lblQuery.setText(Messages.getString("REPORT_QUERY"));

		txtQuery = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.heightHint = 80;
		txtQuery.setLayoutData(data);
		if (selectedTemplate != null) {
			txtQuery.setText(selectedTemplate.getQuery());
		}

		// Template Parameters
		Label lblTemplateParams = new Label(parent, SWT.NONE);
		lblTemplateParams.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblTemplateParams.setText(Messages.getString("TEMPLATE_PARAMS"));

		createButtonsForParams(parent);
		createTableForParams(parent);

		composite = (Composite) super.createDialogArea(parent);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		composite.setLayout(new GridLayout(2, false));

		// Report Header
		Label lblHeader = new Label(composite, SWT.NONE);
		lblHeader.setText(Messages.getString("REPORT_HEADER"));

		txtReportHeader = new Text(composite, SWT.BORDER);
		txtReportHeader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		if (selectedTemplate != null) {
			txtReportHeader.setText(selectedTemplate.getReportHeader());
		}

		// Report Footer
		Label lblFooter = new Label(composite, SWT.NONE);
		lblFooter.setText(Messages.getString("REPORT_FOOTER"));

		txtReportFooter = new Text(composite, SWT.BORDER);
		txtReportFooter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		if (selectedTemplate != null) {
			txtReportFooter.setText(selectedTemplate.getReportFooter());
		}

		// Template Columns
		Label lblTemplateCols = new Label(parent, SWT.NONE);
		lblTemplateCols.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblTemplateCols.setText(Messages.getString("TEMPLATE_COLUMNS"));

		// TODO table

		applyDialogFont(parent);
		return parent;
	}

	/**
	 * Create add, edit, delete buttons for the template parameters table
	 * 
	 * @param parent
	 */
	private void createButtonsForParams(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));

		btnAddParam = new Button(composite, SWT.NONE);
		btnAddParam.setText(Messages.getString("ADD"));
		btnAddParam.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnAddParam.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/add.png"));
		btnAddParam.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportTemplateParamDialog dialog = new ReportTemplateParamDialog(Display.getDefault().getActiveShell(),
						tvParam);
				dialog.create();
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnEditParam = new Button(composite, SWT.NONE);
		btnEditParam.setText(Messages.getString("EDIT"));
		btnEditParam.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/edit.png"));
		btnEditParam.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnEditParam.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null == getSelectedParam()) {
					Notifier.warning(null, Messages.getString("PLEASE_SELECT_ITEM"));
					return;
				}
				ReportTemplateParamDialog dialog = new ReportTemplateParamDialog(composite.getShell(),
						getSelectedParam(), tvParam);
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnDeleteParam = new Button(composite, SWT.NONE);
		btnDeleteParam.setText(Messages.getString("DELETE"));
		btnDeleteParam.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/delete.png"));
		btnDeleteParam.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnDeleteParam.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null == getSelectedParam()) {
					Notifier.warning(null, Messages.getString("PLEASE_SELECT_ITEM"));
					return;
				}
				@SuppressWarnings("unchecked")
				List<ReportTemplateParameter> items = (List<ReportTemplateParameter>) tvParam.getInput();
				items.remove(tvParam.getTable().getSelectionIndex());
				tvParam.setInput(items);
				tvParam.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void createTableForParams(final Composite parent) {
		tvParam = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		// Create table columns
		createTableColumnsForParams();

		// Configure table layout
		final Table table = tvParam.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.getVerticalBar().setEnabled(true);
		table.getVerticalBar().setVisible(true);
		tvParam.setContentProvider(new ArrayContentProvider());

		populateTableWithParams();

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = 140;
		gridData.horizontalAlignment = GridData.FILL;
		tvParam.getControl().setLayoutData(gridData);

		// Hook up listeners
		tvParam.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tvParam.getSelection();
				Object firstElement = selection.getFirstElement();
				firstElement = (ReportTemplateParameter) firstElement;
				if (firstElement instanceof ReportTemplateParameter) {
					setSelectedParam((ReportTemplateParameter) firstElement);
				}
				btnEditParam.setEnabled(true);
				btnDeleteParam.setEnabled(true);
			}
		});
		tvParam.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ReportTemplateParamDialog dialog = new ReportTemplateParamDialog(parent.getShell(), getSelectedParam(),
						tvParam);
				dialog.open();
			}
		});
	}

	/**
	 * Create table columns related to blacklist/whitelist items.
	 * 
	 */
	private void createTableColumnsForParams() {

		String[] titles = { Messages.getString("PARAM_KEY"), Messages.getString("PARAM_LABEL"),
				Messages.getString("PARAM_TYPE") };
		int[] bounds = { 200, 200, 200 };

		TableViewerColumn keyColumn = createTableViewerColumn(tvParam, titles[0], bounds[0]);
		keyColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ReportTemplateParameter) {
					return ((ReportTemplateParameter) element).getKey();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn labelColumn = createTableViewerColumn(tvParam, titles[1], bounds[1]);
		labelColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ReportTemplateParameter) {
					return ((ReportTemplateParameter) element).getLabel();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn typeColumn = createTableViewerColumn(tvParam, titles[2], bounds[2]);
		typeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ReportTemplateParameter) {
					return ((ReportTemplateParameter) element).getType().toString();
				}
				return Messages.getString("UNTITLED");
			}
		});
	}

	/**
	 * Create new table viewer column instance.
	 * 
	 * @param title
	 * @param bound
	 * @return
	 */
	private TableViewerColumn createTableViewerColumn(TableViewer tv, String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(tv, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		column.setAlignment(SWT.LEFT);
		return viewerColumn;
	}

	private void populateTableWithParams() {
		if (selectedTemplate != null && selectedTemplate.getTemplateParams() != null
				&& !selectedTemplate.getTemplateParams().isEmpty()) {
			tvParam.setInput(selectedTemplate.getTemplateParams());
			tvParam.refresh();
		}
	}

	/**
	 * Handle OK button press
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {

		setReturnCode(OK);

		// Check if name is empty
		if (txtName.getText().isEmpty()) {
			Notifier.warning(null, Messages.getString("FILL_REPORT_NAME_FIELD"));
			return;
		}
		if (txtQuery.getText().isEmpty()) {
			Notifier.warning(null, Messages.getString("FILL_REPORT_QUERY_FIELD"));
			return;
		}

		ReportTemplateRequest template = new ReportTemplateRequest();
		if (selectedTemplate != null && selectedTemplate.getId() != null) {
			template.setId(selectedTemplate.getId());
		}
		template.setDescription(txtDesc.getText());
		template.setName(txtName.getText());
		template.setQuery(txtQuery.getText());
		template.setReportFooter(txtReportFooter.getText());
		template.setReportHeader(txtReportHeader.getText());
		template.setTemplateColumns((List<ReportTemplateColumn>) tvCol.getInput());
		template.setTemplateParams((List<ReportTemplateParameter>) tvParam.getInput());
		logger.debug("Template request: {}", template);

		try {
			if (selectedTemplate != null && selectedTemplate.getId() != null) {
				ReportUtils.update(template);
			} else {
				ReportUtils.add(template);
			}
			editor.refresh();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notifier.error(null, Messages.getString("ERROR_ON_SAVE"));
		}

		close();
	}

	public ReportTemplateParameter getSelectedParam() {
		return selectedParam;
	}

	public void setSelectedParam(ReportTemplateParameter selectedParam) {
		this.selectedParam = selectedParam;
	}

}
