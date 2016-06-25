package tr.org.liderahenk.liderconsole.core.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
import tr.org.liderahenk.liderconsole.core.contentproviders.TaskOverviewContentProvider;
import tr.org.liderahenk.liderconsole.core.current.RestSettings;
import tr.org.liderahenk.liderconsole.core.dialogs.ExecutedTaskDialog;
import tr.org.liderahenk.liderconsole.core.labelproviders.TaskOverviewLabelProvider;
import tr.org.liderahenk.liderconsole.core.model.Command;
import tr.org.liderahenk.liderconsole.core.model.CommandExecution;
import tr.org.liderahenk.liderconsole.core.rest.utils.TaskRestUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.xmpp.notifications.TaskNotification;
import tr.org.liderahenk.liderconsole.core.xmpp.notifications.TaskStatusNotification;

/**
 * View part for tasks.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class TaskOverview extends ViewPart {

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
		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.TASK_NOTIFICATION_RECEIVED, taskNotificationHandler);
		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.TASK_STATUS_NOTIFICATION_RECEIVED,
				taskStatusNotificationHandler);
		eventBroker.subscribe("check_lider_status", connectionHandler);
	}

	@Override
	public void createPartControl(final Composite parent) {

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

		// Executed tasks
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeViewer.setContentProvider(new TaskOverviewContentProvider());
		treeViewer.setLabelProvider(new TaskOverviewLabelProvider());
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection == null || selection.isEmpty()) {
					return;
				}
				final Object sel = selection.getFirstElement();
				if (sel instanceof Command) {
					ExecutedTaskDialog dialog = new ExecutedTaskDialog(parent.getShell(), null, (Command) sel);
					dialog.create();
					dialog.open();
				}
			}
		});
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		eventBroker.unsubscribe(taskNotificationHandler);
		eventBroker.unsubscribe(taskStatusNotificationHandler);
		eventBroker.unsubscribe(connectionHandler);
		super.dispose();
	}

	private EventHandler taskNotificationHandler = new EventHandler() {
		@Override
		public void handleEvent(final Event event) {
			Job job = new Job("TASK_NOTIFICATION") {
				@SuppressWarnings("unchecked")
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("Task", 100);
					try {
						if (treeViewer == null) {
							return Status.OK_STATUS;
						}
						TaskNotification task = (TaskNotification) event.getProperty("org.eclipse.e4.data");
						final List<Command> items = new ArrayList<Command>();
						// Restore previous items
						if (treeViewer.getInput() != null) {
							items.addAll((List<Command>) treeViewer.getInput());
						}
						// Add new item
						items.add(task.getCommand());
						// Refresh tree
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

	private EventHandler taskStatusNotificationHandler = new EventHandler() {
		@Override
		public void handleEvent(final Event event) {
			Job job = new Job("TASK_STATUS_NOTIFICATION") {
				@SuppressWarnings("unchecked")
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("Task", 100);
					try {
						TaskStatusNotification task = (TaskStatusNotification) event.getProperty("org.eclipse.e4.data");
						CommandExecution relatedExecution = task.getCommandExecution();
						final List<Command> items = new ArrayList<Command>();
						// Restore previous items
						if (treeViewer.getInput() != null) {
							items.addAll((List<Command>) treeViewer.getInput());
						}
						if (!items.isEmpty()) {
							// Find related command execution...
							for (Command command : items) {
								if (command.getCommandExecutions() != null
										&& !command.getCommandExecutions().isEmpty()) {
									for (CommandExecution execution : command.getCommandExecutions()) {
										if (execution.equals(relatedExecution)) {
											// ...and append execution result to
											// it.
											execution.getCommandExecutionResults().add(task.getResult());
										}
									}
								}
							}
							// Refresh tree
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									treeViewer.setInput(items);
									treeViewer.refresh();
								}
							});
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

	private EventHandler connectionHandler = new EventHandler() {
		@Override
		public void handleEvent(final Event event) {
			Job job = new Job("QUERY_PREV_TASKS") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("Task", 100);
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
			final List<Command> items = TaskRestUtils
					.listCommands(ConfigProvider.getInstance().getInt(LiderConstants.CONFIG.EXECUTED_TASKS_MAX_SIZE));
			if (items != null && !items.isEmpty()) {
				// Refresh tree
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						treeViewer.setInput(items);
						treeViewer.refresh();
					}
				});
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
