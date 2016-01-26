package tr.org.liderahenk.liderconsole.core.task;

import java.io.Serializable;
import java.util.List;

/**
 * @author Emre Akkaya <emre.akkaya@agem.com.tr>
 *
 */
public class ParentTask implements Serializable {

	private static final long serialVersionUID = 448419179741713315L;

	private String id;
	private String creationDate;
	private String pluginId;
	private List<Task> tasks;

	public ParentTask() {
		super();
	}

	public ParentTask(String id, String creationDate, String pluginId,
			List<Task> tasks) {
		super();
		this.id = id;
		this.creationDate = creationDate;
		this.pluginId = pluginId;
		this.tasks = tasks;
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

}
