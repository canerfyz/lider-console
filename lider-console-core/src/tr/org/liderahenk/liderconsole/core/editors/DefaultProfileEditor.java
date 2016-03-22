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
import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.responses.IResponse;
import tr.org.liderahenk.liderconsole.core.rest.responses.RestResponse;
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
		Composite composite = new Composite(parent, GridData.FILL);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(3, false));
		createButtonsArea(composite);
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
				DefaultProfileDialog dialog = new DefaultProfileDialog(composite.getShell(), getSelectedProfile(),
						getSelf(), editorInput.getProfileDialog());
				if (dialog.open() == SWT.OK) {
					refresh();
				}
			}
		});
	}

	/**
	 * Search profile by plugin name and version, then populate specified table
	 * with profile records.
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void populateTable() {
		try {
			ProfileEditorInput editorInput = (ProfileEditorInput) getEditorInput();
			RestResponse response = (RestResponse) ProfileUtils.list(editorInput.getPluginName(),
					editorInput.getPluginVersion(), null, null);
			if (response.getStatus() == RestResponseStatus.OK) {
				List<Profile> profiles = (List<Profile>) response.getResultMap().get("profiles");
				if (profiles != null) {
					tableViewer.setInput(profiles);
				}
			} else {
				Notifier.error("Profile", "Profiller listelenemedi.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Create table columns related to profile database columns.
	 * 
	 */
	private void createTableColumns() {

		String[] titles = { Messages.getString("LABEL"), Messages.getString("DESCRIPTION"),
				Messages.getString("CREATE_DATE"), Messages.getString("MODIFY_DATE"), Messages.getString("ACTIVE") };
		int[] bounds = { 100, 400, 150, 150, 10 };

		TableViewerColumn nameColumn = createTableViewerColumn(titles[0], bounds[0]);
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
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
					return ((Profile) element).getCreateDate().toString();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn modifyDateColumn = createTableViewerColumn(titles[3], bounds[3]);
		modifyDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Profile) {
					return ((Profile) element).getModifyDate().toString();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn activeColumn = createTableViewerColumn(titles[4], bounds[4]);
		activeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Profile) {
					return ((Profile) element).isActive() ? Messages.getString("TRUE") : Messages.getString("FALSE");
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
	 * Create add, edit, delete button for the table.
	 * 
	 * @param composite
	 */
	private void createButtonsArea(final Composite composite) {
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
						editorInput.getProfileDialog());
				dialog.create();
				dialog.open();
				refresh();
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
					Notifier.error("Profile Editor", Messages.getString("PLEASE_SELECT_PROFILE"));
					return;
				}
				ProfileEditorInput editorInput = (ProfileEditorInput) getEditorInput();
				DefaultProfileDialog dialog = new DefaultProfileDialog(composite.getShell(), getSelectedProfile(),
						getSelf(), editorInput.getProfileDialog());
				dialog.open();
				refresh();
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
					Notifier.error("Profile Editor", Messages.getString("PLEASE_SELECT_PROFILE"));
					return;
				}
				IResponse response;
				try {
					response = ProfileUtils.delete(getSelectedProfile().getId());
					if (response.getStatus() == RestResponseStatus.OK) {
						Notifier.success(null, Messages.getString("RECORD_DELETED"));
						refresh();
					} else {
						Notifier.error(null, response.getMessages().isEmpty() ? Messages.getString("ERROR_ON_DELETE")
								: response.getMessages().get(0));
					}
				} catch (Exception e1) {
					logger.error(e1.getMessage(), e1);
					Notifier.error(null, Messages.getString("ERROR_ON_DELETE"));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
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
