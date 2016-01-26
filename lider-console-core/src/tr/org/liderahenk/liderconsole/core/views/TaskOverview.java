package tr.org.liderahenk.liderconsole.core.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.directory.SearchResult;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import tr.org.liderahenk.liderconsole.core.task.ParentTask;
import tr.org.liderahenk.liderconsole.core.task.Task;
import tr.org.liderahenk.liderconsole.core.task.TaskCommState;
import tr.org.liderahenk.liderconsole.core.task.TaskState;

public class TaskOverview extends ViewPart {

	public static final String ID = "tr.org.liderahenk.liderconsole.core.views.taskOverview.TaskOverview";

	private TreeViewer viewer;

	private final IEventBroker eventBroker = (IEventBroker) PlatformUI
			.getWorkbench().getService(IEventBroker.class);

	// Keeps track of the current selection in the LDAP result list
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
				// Read parent task file
				ArrayList<ParentTask> parentTaskList = readParentTaskList();
				// Create our new parent task:
				ParentTask parent = new ParentTask();
				parent.setPluginId((String) data);
				parent.setCreationDate(getCurrentDate());
				parent.setTasks(createTasksFromCurrentSelection(parent,
						(String) data));
				// Append latest parent task to list and refresh the list
				parentTaskList.add(parent);
				refresh(parentTaskList);
				// Save back to parent task file
				writeFile(parentTaskList);
				// TODO Create a thread to update tasks as completed (or failed)
			}
		}

		private String getCurrentDate() {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			Date d = new Date();
			return dateFormat.format(d);
		}

		private void writeFile(ArrayList<ParentTask> parentTaskList) {
			FileOutputStream fout = null;
			ObjectOutputStream oos = null;
			try {
				fout = new FileOutputStream("parent-task-list");
				oos = new ObjectOutputStream(fout);
				oos.writeObject(parentTaskList);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (oos != null) {
					try {
						oos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private ArrayList<Task> createTasksFromCurrentSelection(
				ParentTask parent, String pluginId) {
			if (currentSelection != null && !currentSelection.isEmpty()) {
				ArrayList<Task> tasks = new ArrayList<Task>();
				Iterator iterator = currentSelection.iterator();
				while (iterator.hasNext()) {
					Object obj = iterator.next();
					if (obj instanceof javax.naming.directory.SearchResult) {
						javax.naming.directory.SearchResult result = (SearchResult) obj;
						String dn = result.getName();
						Task t = new Task();
						t.setPluginId(pluginId);
						t.setTargetObjectDN(dn);
						t.setState(TaskState.CREATED);
						long currentTimeMillis = System.currentTimeMillis();
						t.setCreationDate(currentTimeMillis);
						t.setChangedDate(currentTimeMillis);
						t.setCommState(TaskCommState.AGENT_ONLINE);
						t.setParent(parent);
						tasks.add(t);
						System.out.println(result);
					}
				}
				return tasks;
			}
			return null;
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

		TreeViewerColumn mainColumn = new TreeViewerColumn(viewer, SWT.NONE);
		mainColumn.getColumn().setText("İsim");
		mainColumn.getColumn().setWidth(200);
		mainColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new ViewLabelProvider()));

		TreeViewerColumn statusColumn = new TreeViewerColumn(viewer, SWT.NONE);
		statusColumn.getColumn().setText("Durum");
		statusColumn.getColumn().setWidth(80);
		statusColumn.getColumn().setAlignment(SWT.CENTER);
		statusColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new StatusLabelProvider()));

		TreeViewerColumn creationDateColumn = new TreeViewerColumn(viewer,
				SWT.NONE);
		creationDateColumn.getColumn().setText("Başlangıç");
		creationDateColumn.getColumn().setWidth(150);
		creationDateColumn.getColumn().setAlignment(SWT.CENTER);
		creationDateColumn
				.setLabelProvider(new DelegatingStyledCellLabelProvider(
						new CreateDateLabeProvider()));

		TreeViewerColumn lastChangeDateColumn = new TreeViewerColumn(viewer,
				SWT.NONE);
		lastChangeDateColumn.getColumn().setText("Son Güncelleme");
		lastChangeDateColumn.getColumn().setWidth(150);
		lastChangeDateColumn.getColumn().setAlignment(SWT.CENTER);
		lastChangeDateColumn
				.setLabelProvider(new DelegatingStyledCellLabelProvider(
						new LastChangeDateLabelProvider()));

		viewer.setInput(readParentTaskArrayAsCompleted());
	}

	/**
	 * @return
	 */
	private ParentTask[] readParentTaskArray() {
		ArrayList<ParentTask> list = readParentTaskList();
		return list.toArray(new ParentTask[] {});
	}
	
	private ParentTask[] readParentTaskArrayAsCompleted() {
		ArrayList<ParentTask> list = readParentTaskList();
		for (ParentTask parent : list) {
			List<Task> tasks = parent.getTasks();
			for (Task task : tasks) {
				TaskState state = task.getState();
			}
		}
		return list.toArray(new ParentTask[] {});
	}

	@SuppressWarnings("unchecked")
	private ArrayList<ParentTask> readParentTaskList() {
		ArrayList<ParentTask> parentTaskList = null;
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {
			File tmp = new File("parent-task-list");
			if (tmp.exists()) {
				fin = new FileInputStream(tmp);
				ois = new ObjectInputStream(fin);
				Object obj = ois.readObject();
				if (obj instanceof ArrayList<?>) {
					parentTaskList = (ArrayList<ParentTask>) obj;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return parentTaskList == null ? new ArrayList<ParentTask>()
				: parentTaskList;
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * 
	 */
	public void refresh(ArrayList<ParentTask> parentTaskList) {
		viewer.setInput(parentTaskList);
		viewer.refresh();
	}

	@Override
	public void dispose() {
		eventBroker.unsubscribe(updateList);
		eventBroker.unsubscribe(updateOverview);
		super.dispose();
	}

}
