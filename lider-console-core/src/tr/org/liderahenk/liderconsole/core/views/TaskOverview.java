package tr.org.liderahenk.liderconsole.core.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.contentproviders.TaskOverviewContentProvider;
import tr.org.liderahenk.liderconsole.core.labelproviders.TaskOverviewLabelProvider;
import tr.org.liderahenk.liderconsole.core.model.Command;
import tr.org.liderahenk.liderconsole.core.xmpp.notifications.TaskNotification;
import tr.org.liderahenk.liderconsole.core.xmpp.notifications.TaskStatusNotification;

public class TaskOverview extends ViewPart {

	private static final Logger logger = LoggerFactory.getLogger(TaskOverview.class);

	private TreeViewer treeViewer;

	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.TASK_NOTIFICATION_RECEIVED, taskNotificationHandler);
		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.TASK_STATUS_NOTIFICATION_RECEIVED,
				taskStatusNotificationHandler);
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
						List<Command> items = null;
						// Restore previous items
						if (treeViewer.getInput() != null) {
							items = (List<Command>) treeViewer.getInput();
						}
						if (items == null) {
							items = new ArrayList<Command>();
						}
						// Add new item
						items.add(task.getCommand());
						treeViewer.setInput(items);
						treeViewer.refresh();
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
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("Task", 100);
					try {
						TaskStatusNotification task = (TaskStatusNotification) event.getProperty("org.eclipse.e4.data");
						System.out.println("TASK: " + task);
						// TODO
						// TODO
						// TODO
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

	@Override
	public void createPartControl(Composite parent) {

		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.setContentProvider(new TaskOverviewContentProvider());
		treeViewer.setLabelProvider(new TaskOverviewLabelProvider());
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		eventBroker.unsubscribe(taskNotificationHandler);
		eventBroker.unsubscribe(taskStatusNotificationHandler);
		super.dispose();
	}

}
