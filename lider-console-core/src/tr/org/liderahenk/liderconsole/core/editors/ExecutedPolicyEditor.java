package tr.org.liderahenk.liderconsole.core.editors;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.dialogs.ExecutedPolicyDialog;
import tr.org.liderahenk.liderconsole.core.editorinput.DefaultEditorInput;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Command;
import tr.org.liderahenk.liderconsole.core.model.ExecutedPolicy;
import tr.org.liderahenk.liderconsole.core.rest.utils.CommandUtils;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ExecutedPolicyEditor extends EditorPart {

	private static final Logger logger = LoggerFactory.getLogger(ExecutedPolicyEditor.class);

	private Text txtLabel;
	private DateTime dtCreateDateRangeStart;
	private DateTime dtCreateDateRangeEnd;
	private Button btnSearch;
	private Text txtSearch;
	private TableViewer tableViewer;
	private TableFilter tableFilter;

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(((DefaultEditorInput) input).getLabel());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {

		Composite composite = new Composite(parent, GridData.FILL);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(1, false));

		Composite innerComposite = new Composite(composite, SWT.NONE);
		innerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		innerComposite.setLayout(new GridLayout(6, false));

		// Policy label
		Label lblLabel = new Label(innerComposite, SWT.NONE);
		lblLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblLabel.setText(Messages.getString("POLICY_LABEL"));

		// Label input
		txtLabel = new Text(innerComposite, SWT.BORDER);
		txtLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Create date label
		Label lblCreateDateRange = new Label(innerComposite, SWT.NONE);
		lblCreateDateRange.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblCreateDateRange.setText(Messages.getString("CREATE_DATE_RANGE"));

		// Create date range start
		dtCreateDateRangeStart = new DateTime(innerComposite, SWT.DROP_DOWN | SWT.BORDER);
		dtCreateDateRangeStart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Create date range end
		dtCreateDateRangeEnd = new DateTime(innerComposite, SWT.DROP_DOWN | SWT.BORDER);
		dtCreateDateRangeEnd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		btnSearch = new Button(innerComposite, SWT.PUSH);
		btnSearch.setText(Messages.getString("SEARCH"));
		btnSearch.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateTable();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		innerComposite = new Composite(composite, SWT.NONE);
		innerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		innerComposite.setLayout(new GridLayout(2, false));

		// Search label
		Label lblSearch = new Label(innerComposite, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblSearch.setText(Messages.getString("SEARCH_FILTER"));

		// Filter table rows
		txtSearch = new Text(innerComposite, SWT.BORDER | SWT.SEARCH);
		txtSearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		txtSearch.setToolTipText(Messages.getString("SEARCH_TOOLTIP"));
		txtSearch.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				tableFilter.setSearchText(txtSearch.getText());
				tableViewer.refresh();
			}
		});

		createTableArea(composite);
	}

	/**
	 * Create main widget of the editor - table viewer.
	 * 
	 * @param composite
	 */
	private void createTableArea(final Composite composite) {

		GridData dataSearchGrid = new GridData();
		dataSearchGrid.grabExcessHorizontalSpace = true;
		dataSearchGrid.horizontalAlignment = GridData.FILL;

		tableViewer = new TableViewer(composite,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		// Create table columns
		createTableColumns();

		// Configure table layout
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.getVerticalBar().setEnabled(true);
		table.getVerticalBar().setVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());

		// Populate table with policies
		populateTable();

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = 420;
		gridData.horizontalAlignment = GridData.FILL;
		tableViewer.getControl().setLayoutData(gridData);

		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// Query task details and populate dialog with it.
				try {
					ExecutedPolicy policy = getSelectedPolicy();
					Command command = CommandUtils.getPolicyCommand(policy.getId());
					ExecutedPolicyDialog dialog = new ExecutedPolicyDialog(composite.getShell(), policy, command);
					dialog.open();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
				}
			}
		});

		tableFilter = new TableFilter();
		tableViewer.addFilter(tableFilter);
		tableViewer.refresh();
	}

	/**
	 * Create table columns related to policy database columns.
	 * 
	 */
	private void createTableColumns() {

		String[] titles = { Messages.getString("LABEL"), Messages.getString("CREATE_DATE"),
				Messages.getString("RECEIVED_STATUS"), Messages.getString("SUCCESS_STATUS"),
				Messages.getString("ERROR_STATUS") };
		int[] bounds = { 200, 250, 100, 100, 100 };

		TableViewerColumn labelColumn = createTableViewerColumn(titles[0], bounds[0]);
		labelColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedPolicy) {
					return ((ExecutedPolicy) element).getLabel();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn createDateColumn = createTableViewerColumn(titles[1], bounds[1]);
		createDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedPolicy) {
					return ((ExecutedPolicy) element).getCreateDate() != null
							? ((ExecutedPolicy) element).getCreateDate().toString() : Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn receivedColumn = createTableViewerColumn(titles[2], bounds[2]);
		receivedColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedPolicy) {
					return ((ExecutedPolicy) element).getReceivedResults() != null
							? ((ExecutedPolicy) element).getReceivedResults().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn successColumn = createTableViewerColumn(titles[3], bounds[3]);
		successColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedPolicy) {
					return ((ExecutedPolicy) element).getSuccessResults() != null
							? ((ExecutedPolicy) element).getSuccessResults().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn errorColumn = createTableViewerColumn(titles[4], bounds[4]);
		errorColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedPolicy) {
					return ((ExecutedPolicy) element).getErrorResults() != null
							? ((ExecutedPolicy) element).getErrorResults().toString() : Messages.getString("UNTITLED");
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
	private TableViewerColumn createTableViewerColumn(String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		column.setAlignment(SWT.LEFT);
		return viewerColumn;
	}

	private void populateTable() {
		// TODO add status and date params!
		try {
			List<ExecutedPolicy> policies = CommandUtils.listExecutedPolicies(txtLabel.getText(), null, null, null);
			if (policies != null) {
				tableViewer.setInput(policies);
				tableViewer.refresh();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}
	}

	/**
	 * Apply filter to table rows. (Search text can be policy label)
	 *
	 */
	public class TableFilter extends ViewerFilter {

		private String searchString;

		public void setSearchText(String s) {
			this.searchString = ".*" + s + ".*";
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (searchString == null || searchString.length() == 0) {
				return true;
			}
			ExecutedPolicy policy = (ExecutedPolicy) element;
			if (policy.getLabel().matches(searchString)) {
				return true;
			}
			return false;
		}

	}

	/**
	 * 
	 * @return selected policy record, null otherwise.
	 */
	protected ExecutedPolicy getSelectedPolicy() {
		ExecutedPolicy policy = null;
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		if (selection != null && selection.getFirstElement() instanceof ExecutedPolicy) {
			policy = (ExecutedPolicy) selection.getFirstElement();
		}
		return policy;
	}

	@Override
	public void setFocus() {
		txtLabel.setFocus();
	}

}
