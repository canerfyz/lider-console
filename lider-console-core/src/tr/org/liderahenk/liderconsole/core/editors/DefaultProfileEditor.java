package tr.org.liderahenk.liderconsole.core.editors;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.dialogs.DefaultProfileDialog;
import tr.org.liderahenk.liderconsole.core.editorinput.ProfileEditorInput;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.model.Profile;
import tr.org.liderahenk.liderconsole.core.rest.utils.ProfileUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Default profile editor implementation that can be used by plugins in order to
 * provide profile management GUI automatically.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 * @see tr.org.liderahenk.liderconsole.core.editorinput.ProfileEditorInput
 * @see tr.org.liderahenk.liderconsole.core.dialogs.DefaultProfileDialog
 *
 */
public class DefaultProfileEditor extends EditorPart {

	private static final Logger logger = LoggerFactory.getLogger(DefaultProfileEditor.class);

	private TableViewer tableViewer;
	private Button btnAddProfile;
	private Button btnEditProfile;
	private Button btnDeleteProfile;
	private Button btnRefreshProfile;

	private Profile selectedProfile;

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
		setPartName(((ProfileEditorInput) input).getLabel());
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

		btnAddProfile = new Button(composite, SWT.NONE);
		btnAddProfile.setText(Messages.getString("ADD"));
		btnAddProfile.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnAddProfile.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/add.png"));
		btnAddProfile.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProfileEditorInput editorInput = (ProfileEditorInput) getEditorInput();
				DefaultProfileDialog dialog = new DefaultProfileDialog(Display.getDefault().getActiveShell(), getSelf(),
						editorInput);
				dialog.create();
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnEditProfile = new Button(composite, SWT.NONE);
		btnEditProfile.setText(Messages.getString("EDIT"));
		btnEditProfile.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/edit.png"));
		btnEditProfile.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnEditProfile.setEnabled(false);
		btnEditProfile.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null == getSelectedProfile()) {
					Notifier.warning(null, Messages.getString("PLEASE_SELECT_PROFILE"));
					return;
				}
				ProfileEditorInput editorInput = (ProfileEditorInput) getEditorInput();
				DefaultProfileDialog dialog = new DefaultProfileDialog(composite.getShell(), getSelectedProfile(),
						getSelf(), editorInput);
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnDeleteProfile = new Button(composite, SWT.NONE);
		btnDeleteProfile.setText(Messages.getString("DELETE"));
		btnDeleteProfile.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/delete.png"));
		btnDeleteProfile.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnDeleteProfile.setEnabled(false);
		btnDeleteProfile.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null == getSelectedProfile()) {
					Notifier.warning(null, Messages.getString("PLEASE_SELECT_PROFILE"));
					return;
				}
				try {
					ProfileUtils.delete(getSelectedProfile().getId());
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

		btnRefreshProfile = new Button(composite, SWT.NONE);
		btnRefreshProfile.setText(Messages.getString("REFRESH"));
		btnRefreshProfile.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/refresh.png"));
		btnRefreshProfile.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnRefreshProfile.addSelectionListener(new SelectionListener() {
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

		GridData dataSearchGrid = new GridData();
		dataSearchGrid.grabExcessHorizontalSpace = true;
		dataSearchGrid.horizontalAlignment = GridData.FILL;

		tableViewer = new TableViewer(parent,
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

		// Populate table with profiles
		populateTable();

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = 420;
		gridData.horizontalAlignment = GridData.FILL;
		tableViewer.getControl().setLayoutData(gridData);

		// Hook up listeners
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Object firstElement = selection.getFirstElement();
				firstElement = (Profile) firstElement;
				if (firstElement instanceof Profile) {
					setSelectedProfile((Profile) firstElement);
				}
				btnEditProfile.setEnabled(true);
				btnDeleteProfile.setEnabled(true);
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ProfileEditorInput editorInput = (ProfileEditorInput) getEditorInput();
				DefaultProfileDialog dialog = new DefaultProfileDialog(parent.getShell(), getSelectedProfile(),
						getSelf(), editorInput);
				dialog.open();
			}
		});
	}

	/**
	 * Create table columns related to profile database columns.
	 * 
	 */
	private void createTableColumns() {

		String[] titles = { Messages.getString("LABEL"), Messages.getString("DESCRIPTION"),
				Messages.getString("CREATE_DATE"), Messages.getString("MODIFY_DATE"), Messages.getString("ACTIVE") };
		int[] bounds = { 100, 400, 150, 150, 10 };

		TableViewerColumn labelColumn = createTableViewerColumn(titles[0], bounds[0]);
		labelColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Profile) {
					return ((Profile) element).getLabel();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn descColumn = createTableViewerColumn(titles[1], bounds[1]);
		descColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Profile) {
					return ((Profile) element).getDescription();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn createDateColumn = createTableViewerColumn(titles[2], bounds[2]);
		createDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Profile) {
					return ((Profile) element).getCreateDate() != null ? ((Profile) element).getCreateDate().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn modifyDateColumn = createTableViewerColumn(titles[3], bounds[3]);
		modifyDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Profile) {
					return ((Profile) element).getModifyDate() != null ? ((Profile) element).getModifyDate().toString()
							: Messages.getString("UNTITLED");
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn activeColumn = createTableViewerColumn(titles[4], bounds[4]);
		activeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Profile) {
					return ((Profile) element).isActive() ? Messages.getString("YES") : Messages.getString("NO");
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
	 * Search profile by plugin name and version, then populate specified table
	 * with profile records.
	 * 
	 */
	private void populateTable() {
		try {
			ProfileEditorInput editorInput = (ProfileEditorInput) getEditorInput();
			List<Profile> profiles = ProfileUtils.list(editorInput.getPluginName(), editorInput.getPluginVersion(),
					null, null);
			if (profiles != null) {
				tableViewer.setInput(profiles);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notifier.error(null, Messages.getString("ERROR_ON_LIST"));
		}
	}

	@Override
	public void setFocus() {
		btnAddProfile.setFocus();
	}

	/**
	 * Re-populate table with profiles.
	 * 
	 */
	public void refresh() {
		populateTable();
		tableViewer.refresh();
	}

	public DefaultProfileEditor getSelf() {
		return this;
	}

	public Profile getSelectedProfile() {
		return selectedProfile;
	}

	public void setSelectedProfile(Profile selectedProfile) {
		this.selectedProfile = selectedProfile;
	}

}
