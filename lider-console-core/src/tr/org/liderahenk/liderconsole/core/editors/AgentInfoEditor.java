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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.dialogs.AgentDetailDialog;
import tr.org.liderahenk.liderconsole.core.editorinput.DefaultEditorInput;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Agent;
import tr.org.liderahenk.liderconsole.core.rest.utils.AgentRestUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class AgentInfoEditor extends EditorPart {

	private static final Logger logger = LoggerFactory.getLogger(AgentInfoEditor.class);

	private TableViewer tableViewer;
	private TableFilter tableFilter;
	private Text txtSearch;
	private Button btnViewDetail;
	private Button btnRefreshAgent;

	private Agent selectedAgent;

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

		btnViewDetail = new Button(composite, SWT.NONE);
		btnViewDetail.setText(Messages.getString("VIEW_DETAIL"));
		btnViewDetail.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnViewDetail.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/report.png"));
		btnViewDetail.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null == getSelectedAgent()) {
					Notifier.warning(null, Messages.getString("PLEASE_SELECT_RECORD"));
					return;
				}
				AgentDetailDialog dialog = new AgentDetailDialog(Display.getDefault().getActiveShell(),
						getSelectedAgent());
				dialog.create();
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnRefreshAgent = new Button(composite, SWT.NONE);
		btnRefreshAgent.setText(Messages.getString("REFRESH"));
		btnRefreshAgent.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/refresh.png"));
		btnRefreshAgent.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnRefreshAgent.addSelectionListener(new SelectionListener() {
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

		createTableFilterArea(parent);

		tableViewer = SWTResourceManager.createTableViewer(parent);
		createTableColumns();
		populateTable();

		// Hook up listeners
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Object firstElement = selection.getFirstElement();
				if (firstElement instanceof Agent) {
					setSelectedAgent((Agent) firstElement);
				}
				btnViewDetail.setEnabled(true);
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				AgentDetailDialog dialog = new AgentDetailDialog(parent.getShell(), getSelectedAgent());
				dialog.open();
			}
		});

		tableFilter = new TableFilter();
		tableViewer.addFilter(tableFilter);
		tableViewer.refresh();
	}

	/**
	 * Create table filter area
	 * 
	 * @param parent
	 */
	private void createTableFilterArea(Composite parent) {
		Composite filterContainer = new Composite(parent, SWT.NONE);
		filterContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		filterContainer.setLayout(new GridLayout(2, false));

		// Search label
		Label lblSearch = new Label(filterContainer, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		lblSearch.setText(Messages.getString("SEARCH_FILTER"));

		// Filter table rows
		txtSearch = new Text(filterContainer, SWT.BORDER | SWT.SEARCH);
		txtSearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		txtSearch.setToolTipText(Messages.getString("SEARCH_AGENT_TOOLTIP"));
		txtSearch.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				tableFilter.setSearchText(txtSearch.getText());
				tableViewer.refresh();
			}
		});
	}

	/**
	 * Apply filter to table rows. (Search text can be agent DN, hostname, JID,
	 * IP address or MAC address)
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
			Agent agent = (Agent) element;
			return agent.getDn().matches(searchString) || agent.getHostname().matches(searchString)
					|| agent.getJid().matches(searchString) || agent.getIpAddresses().matches(searchString)
					|| agent.getMacAddresses().matches(searchString);
		}
	}

	/**
	 * Create table columns related to agent database columns.
	 * 
	 */
	private void createTableColumns() {

		// DN
		TableViewerColumn dnColumn = SWTResourceManager.createTableViewerColumn(tableViewer, Messages.getString("DN"),
				200);
		dnColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Agent) {
					return ((Agent) element).getDn();
				}
				return Messages.getString("UNTITLED");
			}
		});

		// JID
		TableViewerColumn jidColumn = SWTResourceManager.createTableViewerColumn(tableViewer, Messages.getString("JID"),
				200);
		jidColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Agent) {
					return ((Agent) element).getJid();
				}
				return Messages.getString("UNTITLED");
			}
		});

		// Hostname
		TableViewerColumn hostnameColumn = SWTResourceManager.createTableViewerColumn(tableViewer,
				Messages.getString("HOSTNAME"), 100);
		hostnameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Agent) {
					return ((Agent) element).getHostname();
				}
				return Messages.getString("UNTITLED");
			}
		});

		// IP addresses
		TableViewerColumn ipColumn = SWTResourceManager.createTableViewerColumn(tableViewer,
				Messages.getString("IP_ADDRESS"), 150);
		ipColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Agent) {
					return ((Agent) element).getIpAddresses();
				}
				return Messages.getString("UNTITLED");
			}
		});

		// MAC addresses
		TableViewerColumn macColumn = SWTResourceManager.createTableViewerColumn(tableViewer,
				Messages.getString("MAC_ADDRESS"), 150);
		macColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Agent) {
					return ((Agent) element).getMacAddresses();
				}
				return Messages.getString("UNTITLED");
			}
		});

		// Create date
		TableViewerColumn createDateColumn = SWTResourceManager.createTableViewerColumn(tableViewer,
				Messages.getString("CREATE_DATE"), 100);
		createDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Agent) {
					return ((Agent) element).getCreateDate() != null ? ((Agent) element).getCreateDate().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		// Modify date
		TableViewerColumn modifyDateColumn = SWTResourceManager.createTableViewerColumn(tableViewer,
				Messages.getString("MODIFY_DATE"), 100);
		modifyDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Agent) {
					return ((Agent) element).getModifyDate() != null ? ((Agent) element).getModifyDate().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});
	}

	/**
	 * Get agents and populate the table with them.
	 */
	private void populateTable() {
		try {
			List<Agent> agents = AgentRestUtils.list(null, null);
			if (agents != null) {
				tableViewer.setInput(agents);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}
	}

	/**
	 * Re-populate table with policies.
	 * 
	 */
	public void refresh() {
		populateTable();
		tableViewer.refresh();
	}

	@Override
	public void setFocus() {
	}

	public Agent getSelectedAgent() {
		return selectedAgent;
	}

	public void setSelectedAgent(Agent selectedAgent) {
		this.selectedAgent = selectedAgent;
	}

}
