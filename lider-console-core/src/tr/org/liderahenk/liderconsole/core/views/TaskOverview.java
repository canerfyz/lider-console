package tr.org.liderahenk.liderconsole.core.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.rest.RestRequest;
import tr.org.liderahenk.liderconsole.core.rest.RestResponse;
import tr.org.liderahenk.liderconsole.core.task.ITask;
import tr.org.liderahenk.liderconsole.core.task.ParentTask;
import tr.org.liderahenk.liderconsole.core.task.Task;
import tr.org.liderahenk.liderconsole.core.widgets.notifier.Notifier;

/**
 * 
 * @author <a href="mailto:bm.volkansahin@gmail.com">volkansahin</a>
 *
 */

public class TaskOverview extends ViewPart {

	public static final String ID = "tr.org.liderahenk.liderconsole.core.views.taskOverview.TaskOverview";
	private static final String RESPONSE_RESULT="result";
	
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
				//TODO CREATE HANDLER FOR TRIGGER OF SENDING TASK
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
					return ((ITask) element).getState() != null ? ((ITask) element).getState().toString() : null;
				
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

	@SuppressWarnings("unchecked")
	private List<ITask> readTaskList(Integer maxResults) {
		
		List<Task> taskList = null;
		final Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("maxResult",maxResults );

		RestRequest request = new RestRequest();
		request.setCommandId("TASK_OBSERVER_LIST");
		request.setPluginName("TASK_OBSERVER");
		request.setPluginVersion("1.0.0");
		request.setParameterMap(parameterMap);
		
		RestResponse response = RestClient.getInstance().post(request);
		
		if( response != null 
				&& response.getResponseBody()!=null && response.getResponseBody().getResultMap()!=null 
				&& !response.getResponseBody().getResultMap().isEmpty() && response.getResponseBody().getResultMap().get(RESPONSE_RESULT)!=null
				&& !"[]".equals(response.getResponseBody().getResultMap().get(RESPONSE_RESULT).toString())){
			taskList=(List<Task>) response.getResponseBody().getResultMap().get(RESPONSE_RESULT);
		}

		return prepareParents(taskList);
	}


	private List<ITask> prepareParents(List<Task> taskList) {

		List<Task> subTasksList = new ArrayList<Task>();
		List<ITask> parentLevelTasksList = new ArrayList<ITask>();
		Map<String, Object> parentLevelTasksMap = new HashMap<String, Object>();
		
		if( taskList != null && !taskList.isEmpty()){
			
			for (Task task : taskList) {
				if(task.getParentTaskId() == null){
					parentLevelTasksMap.put(task.getId(), task);
				}
				else{
					subTasksList.add(task);
				}
			}
			
			for (Task task : subTasksList) {
					
					if(parentLevelTasksMap.get(task.getParentTaskId()) != null){
						Object parent = parentLevelTasksMap.get(task.getParentTaskId());
						
						if(parent instanceof Task){
							Task t = (Task) parent;
							parentLevelTasksMap.put(t.getId(), new ParentTask(t.getId(), t.getCreationDate(), t.getPluginId(), t.getChangedDate(), t.getState(), new ArrayList<Task>()));
						}
						
						ParentTask p = ((ParentTask)parentLevelTasksMap.get(task.getParentTaskId()));
						p.addTask(task);
						
						parentLevelTasksMap.put(p.getId(), p);
						
					}
					else{
						Notifier.error(Messages.getString("PARENT_TASK_ERROR"), Messages.getString("COULD_NOT_FOUND_SOME_PARENT_OF_TASKS"));
					}
			}
		}
		
		if(!parentLevelTasksMap.isEmpty()){
				for (Map.Entry<String, Object> task : parentLevelTasksMap.entrySet())
				{
					parentLevelTasksList.add((ITask)task.getValue());
				}
		}
		
		return parentLevelTasksList;
	}

	private void refresh() {
		
		List<ITask> taskList=null;
		taskList=readTaskList(null);
		
		if(taskList!=null && !taskList.isEmpty()){
			viewer.setInput(taskList);
		}
		
		viewer.refresh();
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
