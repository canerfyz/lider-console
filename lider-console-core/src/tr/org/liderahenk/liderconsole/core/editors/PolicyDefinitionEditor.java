package tr.org.liderahenk.liderconsole.core.editors;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.dialogs.PolicyDefinitionDialog;
import tr.org.liderahenk.liderconsole.core.editorinput.DefaultEditorInput;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Policy;
import tr.org.liderahenk.liderconsole.core.rest.utils.PolicyUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Editor class for policy definitions.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class PolicyDefinitionEditor extends EditorPart {

	private static final Logger logger = LoggerFactory.getLogger(PolicyDefinitionEditor.class);

	private TableViewer tableViewer;
	private Button btnAddPolicy;
	private Button btnEditPolicy;
	private Button btnDeletePolicy;
	private Button btnRefreshPolicy;

	private Policy selectedPolicy;

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
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parent.setLayout(new GridLayout(1, false));
		createButtonsArea(parent);
		createTableArea(parent);
	}

	/**
	 * Create add, edit, delete button for the table.
	 * 
	 * @param composite
	 */
	private void createButtonsArea(final Composite parent) {

		final Composite composite = new Composite(parent, GridData.FILL);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		composite.setLayout(new GridLayout(4, false));

		btnAddPolicy = new Button(composite, SWT.NONE);
		btnAddPolicy.setText(Messages.getString("ADD"));
		btnAddPolicy.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnAddPolicy.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/add.png"));
		btnAddPolicy.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PolicyDefinitionDialog dialog = new PolicyDefinitionDialog(Display.getDefault().getActiveShell(),
						getSelf());
				dialog.create();
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnEditPolicy = new Button(composite, SWT.NONE);
		btnEditPolicy.setText(Messages.getString("EDIT"));
		btnEditPolicy.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/edit.png"));
		btnEditPolicy.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnEditPolicy.setEnabled(false);
		btnEditPolicy.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null == getSelectedPolicy()) {
					Notifier.warning(null, Messages.getString("PLEASE_SELECT_POLICY"));
					return;
				}
				PolicyDefinitionDialog dialog = new PolicyDefinitionDialog(composite.getShell(), getSelectedPolicy(),
						getSelf());
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnDeletePolicy = new Button(composite, SWT.NONE);
		btnDeletePolicy.setText(Messages.getString("DELETE"));
		btnDeletePolicy.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/delete.png"));
		btnDeletePolicy.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnDeletePolicy.setEnabled(false);
		btnDeletePolicy.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null == getSelectedPolicy()) {
					Notifier.warning(null, Messages.getString("PLEASE_SELECT_POLICY"));
					return;
				}
				try {
					PolicyUtils.delete(getSelectedPolicy().getId());
					refresh();
				} catch (Exception e1) {
					logger.error(e1.getMessage(), e1);
					Notifier.error(null, Messages.getString("ERROR_ON_DELETE"));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnRefreshPolicy = new Button(composite, SWT.NONE);
		btnRefreshPolicy.setText(Messages.getString("REFRESH"));
		btnRefreshPolicy.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/refresh.png"));
		btnRefreshPolicy.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnRefreshPolicy.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	/**
	 * Create main widget of the editor - table viewer.
	 * 
	 * @param parent
	 */
	private void createTableArea(final Composite parent) {

		tableViewer = SWTResourceManager.createTableViewer(parent);
		createTableColumns();
		populateTable();

		// Hook up listeners
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Object firstElement = selection.getFirstElement();
				firstElement = (Policy) firstElement;
				if (firstElement instanceof Policy) {
					setSelectedPolicy((Policy) firstElement);
				}
				btnEditPolicy.setEnabled(true);
				btnDeletePolicy.setEnabled(true);
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				PolicyDefinitionDialog dialog = new PolicyDefinitionDialog(parent.getShell(), getSelectedPolicy(),
						getSelf());
				dialog.open();
			}
		});
	}

	/**
	 * Create table columns related to policy database columns.
	 * 
	 */
	private void createTableColumns() {

		String[] titles = { Messages.getString("LABEL"), Messages.getString("DESCRIPTION"),
				Messages.getString("CREATE_DATE"), Messages.getString("MODIFY_DATE"), Messages.getString("ACTIVE") };
		int[] bounds = { 100, 400, 150, 150, 10 };

		TableViewerColumn labelColumn = SWTResourceManager.createTableViewerColumn(tableViewer, titles[0], bounds[0]);
		labelColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Policy) {
					return ((Policy) element).getLabel();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn descColumn = SWTResourceManager.createTableViewerColumn(tableViewer, titles[1], bounds[1]);
		descColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Policy) {
					return ((Policy) element).getDescription();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn createDateColumn = SWTResourceManager.createTableViewerColumn(tableViewer, titles[2],
				bounds[2]);
		createDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Policy) {
					return ((Policy) element).getCreateDate() != null ? ((Policy) element).getCreateDate().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn modifyDateColumn = SWTResourceManager.createTableViewerColumn(tableViewer, titles[3],
				bounds[3]);
		modifyDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Policy) {
					return ((Policy) element).getModifyDate() != null ? ((Policy) element).getModifyDate().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn activeColumn = SWTResourceManager.createTableViewerColumn(tableViewer, titles[4], bounds[4]);
		activeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Policy) {
					return ((Policy) element).isActive() ? Messages.getString("YES") : Messages.getString("NO");
				}
				return Messages.getString("UNTITLED");
			}
		});
	}

	/**
	 * Search policy by plugin name and version, then populate specified table
	 * with policy records.
	 * 
	 */
	private void populateTable() {
		try {
			List<Policy> policies = PolicyUtils.list(null, null);
			if (policies != null) {
				tableViewer.setInput(policies);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}
	}

	@Override
	public void setFocus() {
		btnAddPolicy.setFocus();
	}

	/**
	 * Re-populate table with policies.
	 * 
	 */
	public void refresh() {
		populateTable();
		tableViewer.refresh();
	}

	public PolicyDefinitionEditor getSelf() {
		return this;
	}

	public Policy getSelectedPolicy() {
		return selectedPolicy;
	}

	public void setSelectedPolicy(Policy selectedPolicy) {
		this.selectedPolicy = selectedPolicy;
	}

}
