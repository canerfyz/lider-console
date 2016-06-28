package tr.org.liderahenk.liderconsole.core.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.contentproviders.SearchGroupViewContentProvider;
import tr.org.liderahenk.liderconsole.core.current.RestSettings;
import tr.org.liderahenk.liderconsole.core.labelproviders.SearchGroupViewLabelProvider;
import tr.org.liderahenk.liderconsole.core.model.SearchGroup;
import tr.org.liderahenk.liderconsole.core.rest.utils.SearchGroupRestUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;

/**
 * View part for search groups.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 * @see tr.org.liderahenk.liderconsole.core.sourceproviders.LiderSourceProvider
 *
 */
public class SearchGroupView extends ViewPart {

	private static final Logger logger = LoggerFactory.getLogger(TaskOverview.class);

	private Button btnRefresh;
	private TreeViewer treeViewer;

	/**
	 * System-wide event broker
	 */
	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.SEARCH_GROUP_CREATED, searchGroupHandler);
		eventBroker.subscribe("check_lider_status", connectionHandler);
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parent.setLayout(new GridLayout(1, false));

		// Refresh button
		btnRefresh = new Button(parent, SWT.NONE);
		btnRefresh.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/refresh.png"));
		btnRefresh.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
		btnRefresh.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// Search groups
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeViewer.setContentProvider(new SearchGroupViewContentProvider());
		treeViewer.setLabelProvider(new SearchGroupViewLabelProvider());

		// Context menu
		// Create a menu manager and create context menu
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(treeViewer.getTree());
		// Set the menu on the SWT widget
		treeViewer.getTree().setMenu(menu);
		// Register the menu
		getSite().registerContextMenu(menuManager, treeViewer);
		// Make the viewer selection available
		getSite().setSelectionProvider(treeViewer);
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		eventBroker.unsubscribe(searchGroupHandler);
		eventBroker.unsubscribe(connectionHandler);
		super.dispose();
	}

	private EventHandler searchGroupHandler = new EventHandler() {
		@Override
		public void handleEvent(final Event event) {
			Job job = new Job("SEARCH_GROUP_CREATED") {
				@SuppressWarnings("unchecked")
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("SEARCH_GROUP", 100);
					try {
						if (treeViewer == null) {
							return Status.OK_STATUS;
						}
						SearchGroup searchGroup = (SearchGroup) event.getProperty("org.eclipse.e4.data");
						final List<SearchGroup> items = new ArrayList<SearchGroup>();
						// Restore previous items
						if (treeViewer.getInput() != null) {
							items.addAll((List<SearchGroup>) treeViewer.getInput());
						}
						// Add new item
						items.add(searchGroup);
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								treeViewer.setInput(items);
								treeViewer.refresh();
							}
						});
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					monitor.worked(100);
					monitor.done();
					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
		}
	};

	private EventHandler connectionHandler = new EventHandler() {
		@Override
		public void handleEvent(final Event event) {
			Job job = new Job("QUERY_PREV_SEARCH_GROUPS") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("SEARCH_GROUP", 100);
					try {
						if (treeViewer == null) {
							return Status.OK_STATUS;
						}
						if (RestSettings.isAvailable()) {
							refresh();
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					monitor.worked(100);
					monitor.done();
					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
		}
	};

	private void refresh() {
		try {
			final List<SearchGroup> searhGroups = SearchGroupRestUtils.list(null,
					ConfigProvider.getInstance().getInt(LiderConstants.CONFIG.SEARCH_GROUPS_MAX_SIZE));
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					treeViewer.setInput(searhGroups);
					treeViewer.refresh();
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
