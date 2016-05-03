package tr.org.liderahenk.liderconsole.core.editors;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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

import tr.org.liderahenk.liderconsole.core.editorinput.DefaultEditorInput;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Plugin;
import tr.org.liderahenk.liderconsole.core.rest.utils.PluginUtils;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class InstalledPluginsEditor extends EditorPart {

	private static final Logger logger = LoggerFactory.getLogger(InstalledPluginsEditor.class);

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

		// Populate table with plugins
		populateTable();

		tableFilter = new TableFilter();
		tableViewer.addFilter(tableFilter);
		tableViewer.refresh();
	}

	/**
	 * Create table columns related to policy database columns.
	 * 
	 */
	private void createTableColumns() {

		String[] titles = { Messages.getString("PLUGIN_NAME"), Messages.getString("PLUGIN_VERSION"),
				Messages.getString("DESCRIPTION"), Messages.getString("CREATE_DATE"),
				Messages.getString("MACHINE_ORIENTED_PLUGIN"), Messages.getString("USER_ORIENTED_PLUGIN"),
				Messages.getString("POLICY_PLUGIN") };
		int[] bounds = { 200, 150, 150, 150, 50, 50, 50 };

		TableViewerColumn pluginNameColumn = createTableViewerColumn(titles[0], bounds[0]);
		pluginNameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Plugin) {
					return ((Plugin) element).getName();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn pluginVersionColumn = createTableViewerColumn(titles[1], bounds[1]);
		pluginVersionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Plugin) {
					return ((Plugin) element).getVersion();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn descriptionColumn = createTableViewerColumn(titles[2], bounds[2]);
		descriptionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Plugin) {
					return ((Plugin) element).getDescription();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn createDateColumn = createTableViewerColumn(titles[3], bounds[3]);
		createDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Plugin) {
					return ((Plugin) element).getCreateDate() != null ? ((Plugin) element).getCreateDate().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn machineOrientedColumn = createTableViewerColumn(titles[4], bounds[4]);
		machineOrientedColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Plugin) {
					return ((Plugin) element).isMachineOriented() ? Messages.getString("YES")
							: Messages.getString("NO");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn userOrientedColumn = createTableViewerColumn(titles[5], bounds[5]);
		userOrientedColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Plugin) {
					return ((Plugin) element).isUserOriented() ? Messages.getString("YES") : Messages.getString("NO");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn policyPluginColumn = createTableViewerColumn(titles[6], bounds[6]);
		policyPluginColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Plugin) {
					return ((Plugin) element).isPolicyPlugin() ? Messages.getString("YES") : Messages.getString("NO");
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

	/**
	 * Apply filter to table rows. (Search text can be plugin name or version)
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
			Plugin plugin = (Plugin) element;
			if (plugin.getName().matches(searchString) || plugin.getVersion().matches(searchString)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * Search plugins by plugin name and version, then populate specified table
	 * with plugin records.
	 * 
	 */
	private void populateTable() {
		try {
			List<Plugin> plugins = PluginUtils.list(null, null);
			if (plugins != null) {
				tableViewer.setInput(plugins);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}
	}

	@Override
	public void setFocus() {
	}

}
