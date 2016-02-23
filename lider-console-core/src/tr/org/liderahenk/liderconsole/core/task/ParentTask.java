package tr.org.liderahenk.liderconsole.core.task;

import java.io.Serializable;
import java.util.List;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class ParentTask implements Serializable,ITask {

	private static final long serialVersionUID = 448419179741713315L;

	private String id;
	private String creationDate;
	private String changedDate;
	private String pluginId;
	private TaskState state;
	private List<Task> tasks;

	public ParentTask() {
		super();
	}

	public ParentTask(String id, String creationDate, String pluginId,String update,TaskState taskState, List<Task> tasks) {
		super();
		this.id = id;
		this.creationDate = creationDate;
		this.pluginId = pluginId;
		this.tasks = tasks;
		this.changedDate = update;
		this.state=taskState;
	}

	public void addTask(Task task) {
		this.tasks.add(task);
	}
	
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public TaskState getState() {
		return state;
	}

	public void setState(TaskState state) {
		this.state = state;
	}

	public String getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(String changedDate) {
		this.changedDate = changedDate;
	}
	
	@Override
	public String getTaskName() {
		return id;
	}
	
	

}
