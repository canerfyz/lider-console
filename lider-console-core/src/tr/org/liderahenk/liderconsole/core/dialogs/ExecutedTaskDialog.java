package tr.org.liderahenk.liderconsole.core.dialogs;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Command;
import tr.org.liderahenk.liderconsole.core.model.CommandExecution;
import tr.org.liderahenk.liderconsole.core.model.CommandExecutionResult;
import tr.org.liderahenk.liderconsole.core.model.ExecutedTask;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ExecutedTaskDialog extends DefaultLiderDialog {

	// Model
	private ExecutedTask task;
	private Command command;

	// Widgets
	private TableViewer tvCmdExec;
	private TableViewer tvExecResult;

	public ExecutedTaskDialog(Shell parentShell, ExecutedTask task, Command command) {
		super(parentShell);
		this.task = task;
		this.command = command;
	}

	/**
	 * Create executed task widgets
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		parent.setLayout(new GridLayout(1, false));

		// Task details label
		Label lblTaskDetails = new Label(parent, SWT.NONE);
		lblTaskDetails.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblTaskDetails.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblTaskDetails.setText(Messages.getString("TASK_DETAILS"));

		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		composite.setLayout(new GridLayout(5, false));

		// Create date label
		Label lblCreateDate = new Label(composite, SWT.NONE);
		lblCreateDate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblCreateDate.setText(Messages.getString("CREATE_DATE"));

		// Create date
		Text txtCreateDate = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtCreateDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtCreateDate.setText(task.getCreateDate().toString());

		// Status label
		Label lblStatus = new Label(composite, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblStatus.setText(Messages.getString("STATUS"));

		// Status
		Text txtStatus = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		txtStatus.setText(generateStatusMessage(task));

		Button btnTaskParams = new Button(composite, SWT.PUSH);
		btnTaskParams.setText(Messages.getString("SHOW_TASK_PARAMETERS"));
		btnTaskParams.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO open dialog for task parameters!
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// Command executions label
		Label lblCmdExecTable = new Label(parent, SWT.NONE);
		lblCmdExecTable.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblCmdExecTable.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblCmdExecTable.setText(Messages.getString("COMMAND_EXECUTION_RECORDS"));

		createTableCmdExec(parent);

		applyDialogFont(composite);
		return composite;
	}

	/**
	 * Create execution table.
	 * 
	 * @param composite
	 */
	private void createTableCmdExec(final Composite composite) {

		GridData dataSearchGrid = new GridData();
		dataSearchGrid.grabExcessHorizontalSpace = true;
		dataSearchGrid.horizontalAlignment = GridData.FILL;

		tvCmdExec = new TableViewer(composite,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		// Create table columns
		createColumnsCmdExec();

		// Configure table layout
		final Table table = tvCmdExec.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.getVerticalBar().setEnabled(true);
		table.getVerticalBar().setVisible(true);
		tvCmdExec.setContentProvider(new ArrayContentProvider());

		// Populate table with command executions
		tvCmdExec.setInput(command.getCommandExecutions());

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = 420;
		gridData.horizontalAlignment = GridData.FILL;
		tvCmdExec.getControl().setLayoutData(gridData);

		// This composite will hold command execution results!
		final Composite resultComposite = new Composite(composite, SWT.BORDER);
		resultComposite.setLayout(new GridLayout(1, false));

		// Hook up listeners
		tvCmdExec.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// Dispose previous results
				disposeChildren(resultComposite);
				// Create command execution result table
				CommandExecution ce = getSelectedCommandExecution();
				if (ce != null) {
					createTableExecResult(resultComposite, ce);
				}
			}
		});
	}

	/**
	 * Create table columns for execution table.
	 * 
	 * @param twCmdExec
	 * 
	 */
	private void createColumnsCmdExec() {
		TableViewerColumn labelColumn = createTableViewerColumn(tvCmdExec, Messages.getString("DN"), 400);
		labelColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof CommandExecution) {
					return ((CommandExecution) element).getDn();
				}
				return Messages.getString("UNTITLED");
			}
		});
	}

	protected void disposeChildren(Composite composite) {
		if (composite != null) {
			for (Control control : composite.getChildren()) {
				control.dispose();
			}
		}
	}

	private CommandExecution getSelectedCommandExecution() {
		IStructuredSelection selection = (IStructuredSelection) tvCmdExec.getSelection();
		return selection != null ? (CommandExecution) selection.getFirstElement() : null;
	}

	/**
	 * Create execution result table
	 * 
	 * @param composite
	 */
	protected void createTableExecResult(final Composite composite, final CommandExecution ce) {

		GridData dataSearchGrid = new GridData();
		dataSearchGrid.grabExcessHorizontalSpace = true;
		dataSearchGrid.horizontalAlignment = GridData.FILL;

		tvExecResult = new TableViewer(composite,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		// Create table columns
		createColumnsExecResult();

		// Configure table layout
		final Table table = tvExecResult.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.getVerticalBar().setEnabled(true);
		table.getVerticalBar().setVisible(true);
		tvExecResult.setContentProvider(new ArrayContentProvider());

		// Populate table with execution results
		tvExecResult.setInput(ce.getCommandExecutionResults());

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = 420;
		gridData.horizontalAlignment = GridData.FILL;
		tvCmdExec.getControl().setLayoutData(gridData);
	}

	/**
	 * Create table columns for execution result table
	 */
	private void createColumnsExecResult() {

		String[] titles = { Messages.getString("CREATE_DATE"), Messages.getString("RESPONSE_MESSAGE"),
				Messages.getString("RESPONSE_CODE") };
		int[] bounds = { 100, 400, 150 };

		TableViewerColumn createDateColumn = createTableViewerColumn(tvExecResult, titles[0], bounds[0]);
		createDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof CommandExecutionResult) {
					return ((CommandExecutionResult) element).getCreateDate() != null
							? ((CommandExecutionResult) element).getCreateDate().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn responseMsgColumn = createTableViewerColumn(tvExecResult, titles[1], bounds[1]);
		responseMsgColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof CommandExecutionResult) {
					return ((CommandExecutionResult) element).getResponseMessage();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn responseCodeColumn = createTableViewerColumn(tvExecResult, titles[2], bounds[2]);
		responseCodeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof CommandExecutionResult) {
					return ((CommandExecutionResult) element).getResponseCode().getMessage();
				}
				return Messages.getString("UNTITLED");
			}
		});

		// TODO response data!!!
		// TODO response data!!!
		// TODO response data!!!
		// TODO response data!!!
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
	 * Generate status message containing number of received, success, error
	 * results.
	 * 
	 * @param t
	 * @return
	 */
	private String generateStatusMessage(ExecutedTask t) {
		if (t != null) {
			StringBuilder msg = new StringBuilder();
			if (t.getReceivedResults() != null) {
				msg.append(Messages.getString("RECEIVED_STATUS")).append(": ").append(t.getReceivedResults())
						.append(" ");
			}
			if (t.getSuccessResults() != null) {
				msg.append(Messages.getString("SUCCESS_STATUS")).append(": ").append(t.getSuccessResults()).append(" ");
			}
			if (t.getErrorResults() != null) {
				msg.append(Messages.getString("ERROR_STATUS")).append(": ").append(t.getErrorResults()).append(" ");
			}
		}
		return null;
	}

}
