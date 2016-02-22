package tr.org.liderahenk.liderconsole.core.views;

import java.util.ArrayList;

import org.apache.directory.studio.ldapbrowser.ui.views.connection.Messages;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import tr.org.liderahenk.liderconsole.core.task.ITask;
import tr.org.liderahenk.liderconsole.core.task.ParentTask;
import tr.org.liderahenk.liderconsole.core.task.Task;
import tr.org.liderahenk.liderconsole.core.task.TaskCommState;
import tr.org.liderahenk.liderconsole.core.task.TaskState;

/**
 * 
 * @author <a href="mailto:bm.volkansahin@gmail.com">volkansahin</a>
 *
 */

public class TaskOverview extends ViewPart {

	public static final String ID = "tr.org.liderahenk.liderconsole.core.views.taskOverview.TaskOverview";

	private TreeViewer viewer;
	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	private IStructuredSelection currentSelection = null;
	
	private final EventHandler updateList = new EventHandler() {
		public void handleEvent(Event event) {
			Object selectedItem = event.getProperty(IEventBroker.DATA);
			if (selectedItem instanceof IStructuredSelection) {
				currentSelection = (IStructuredSelection) selectedItem;
			}
		}
	};

	private final EventHandler updateOverview = new EventHandler() {
		public void handleEvent(Event event) {
			Object data = event.getProperty(IEventBroker.DATA);
			if (data instanceof String) {
				
				//TODO 
			}
		}
	};

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		eventBroker.subscribe("task_service_update_dn_list", updateList);
		eventBroker.subscribe("update_task_overview", updateOverview);
	}

	@Override
	public void createPartControl(Composite parent) {

		
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.getTree().setHeaderVisible(true);
		createColumns();
		
		refresh();
		
	}

	private void refresh() {
		
		ArrayList<ITask> taskList=null;
		taskList=readParentTaskList();
		
		if(taskList!=null && !taskList.isEmpty()){
			viewer.setInput(taskList);
		}
		
		viewer.refresh();
		
	}

	private void createColumns() {

		TreeViewerColumn nameColumn = new TreeViewerColumn(viewer, SWT.NONE);
		nameColumn.getColumn().setText(Messages.getString("NAME"));
		nameColumn.getColumn().setAlignment(SWT.LEFT);
		nameColumn.getColumn().setWidth((int)((int)viewer.getControl().getShell().getSize().x*0.33));
		nameColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element){
				if(element instanceof ITask)
					return ((ITask) element).getTaskName();
				
				return null;
			}
		});

		TreeViewerColumn statusColumn = new TreeViewerColumn(viewer, SWT.NONE);
		statusColumn.getColumn().setText(Messages.getString("STATE"));
		statusColumn.getColumn().setWidth((int)((int)viewer.getControl().getShell().getSize().x*0.33));
		statusColumn.getColumn().setAlignment(SWT.LEFT);
		statusColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element){
				if(element instanceof ITask)
					return ((ITask) element).getState().toString();
				
				return null;
			}
		});

		TreeViewerColumn creationDateColumn = new TreeViewerColumn(viewer,SWT.NONE);
		creationDateColumn.getColumn().setText(Messages.getString("START_DATE"));
		creationDateColumn.getColumn().setWidth((int)((int)viewer.getControl().getShell().getSize().x*0.33));
		creationDateColumn.getColumn().setAlignment(SWT.CENTER);
		creationDateColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element){
				if(element instanceof ITask)
					return ((ITask) element).getCreationDate();
				
				return null;
			}
		});
		
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				
				IStructuredSelection selectedItem = (IStructuredSelection)event.getSelection();
				
				if(selectedItem.getFirstElement() instanceof ParentTask){
					ParentTask selectedParentTask = (ParentTask)selectedItem.getFirstElement();
					ParentTaskDetailDialog detailDialog = new ParentTaskDetailDialog(Display.getDefault().getActiveShell(),selectedParentTask);
					detailDialog.create();
					detailDialog.open();
				}
				if(selectedItem.getFirstElement() instanceof Task){
					Task selectedTask = (Task)selectedItem.getFirstElement();
					TaskDetailDialog detailDialog = new TaskDetailDialog(Display.getDefault().getActiveShell(),selectedTask);
					detailDialog.create();
					detailDialog.open();
				}
			}
		});
		
	}

	private ArrayList<ITask> readParentTaskList() {
		
		ArrayList<ITask> taskList = new ArrayList<ITask>();
		
				
		Task t = new Task("a", true, new Long(100), new Long(100), 1, 1, 1, TaskState.TASK_PROCESSED, TaskCommState.AGENT_OFFLINE, new Long(100),
				null, "a", "s", null, "d", "f", "g");
		
		ArrayList<Task> innerTaskList = new ArrayList<Task>();
		innerTaskList.add(t);
		innerTaskList.add(t);
		innerTaskList.add(t);
		
		ParentTask p = new ParentTask("1", "2", "3", "4", TaskState.CREATED, innerTaskList);
		
		taskList.add(p);
		taskList.add(p);
		taskList.add(t);
		taskList.add(t);
		taskList.add(t);

		return taskList;
	}


	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}


	@Override
	public void dispose() {
		eventBroker.unsubscribe(updateList);
		eventBroker.unsubscribe(updateOverview);
		super.dispose();
	}

}
