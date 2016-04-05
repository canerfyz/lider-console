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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.dialogs.ExecutedTaskDialog;
import tr.org.liderahenk.liderconsole.core.editorinput.DefaultEditorInput;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Command;
import tr.org.liderahenk.liderconsole.core.model.ExecutedTask;
import tr.org.liderahenk.liderconsole.core.rest.utils.TaskUtils;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ExecutedTaskEditor extends EditorPart {

	private static final Logger logger = LoggerFactory.getLogger(ExecutedTaskEditor.class);

	private TableViewer tableViewer;

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

		// Search params

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

		// Populate table with tasks
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
					ExecutedTask task = getSelectedTask();
					Command command = TaskUtils.get(task.getId());
					ExecutedTaskDialog dialog = new ExecutedTaskDialog(composite.getShell(), task, command);
					dialog.open();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
				}
			}
		});

	}

	/**
	 * Create table columns related to policy database columns.
	 * 
	 */
	private void createTableColumns() {

		String[] titles = { Messages.getString("PLUGIN"), Messages.getString("TASK"), Messages.getString("CREATE_DATE"),
				Messages.getString("RECEIVED_STATUS"), Messages.getString("SUCCESS_STATUS"),
				Messages.getString("ERROR_STATUS") };
		int[] bounds = { 200, 200, 250, 100, 100, 100 };

		TableViewerColumn pluginColumn = createTableViewerColumn(titles[0], bounds[0]);
		pluginColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedTask) {
					return ((ExecutedTask) element).getPluginName() + " - "
							+ ((ExecutedTask) element).getPluginVersion();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn taskColumn = createTableViewerColumn(titles[1], bounds[1]);
		taskColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedTask) {
					return ((ExecutedTask) element).getCommandClsId();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn createDateColumn = createTableViewerColumn(titles[2], bounds[2]);
		createDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedTask) {
					return ((ExecutedTask) element).getCreateDate() != null
							? ((ExecutedTask) element).getCreateDate().toString() : Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn receivedColumn = createTableViewerColumn(titles[3], bounds[3]);
		receivedColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedTask) {
					return ((ExecutedTask) element).getReceivedResults() != null
							? ((ExecutedTask) element).getReceivedResults().toString() : Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn successColumn = createTableViewerColumn(titles[4], bounds[4]);
		successColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedTask) {
					return ((ExecutedTask) element).getSuccessResults() != null
							? ((ExecutedTask) element).getSuccessResults().toString() : Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn errorColumn = createTableViewerColumn(titles[5], bounds[5]);
		errorColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ExecutedTask) {
					return ((ExecutedTask) element).getErrorResults() != null
							? ((ExecutedTask) element).getErrorResults().toString() : Messages.getString("UNTITLED");
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
		// TODO
		// TODO
		// TODO
		// TODO
		try { // TODO add search param widgets!!!!!
			List<ExecutedTask> tasks = TaskUtils.list(null, null, null, null, null);
			if (tasks != null) {
				tableViewer.setInput(tasks);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}
	}

	/**
	 * 
	 * @return selected task record, null otherwise.
	 */
	protected ExecutedTask getSelectedTask() {
		ExecutedTask task = null;
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		if (selection != null && selection.getFirstElement() instanceof ExecutedTask) {
			task = (ExecutedTask) selection.getFirstElement();
		}
		return task;
	}

	@Override
	public void setFocus() {
	}

}
