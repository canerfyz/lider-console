package tr.org.liderahenk.liderconsole.core.task;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class Task implements Serializable {

	private static final long serialVersionUID = -4609836023281640447L;

	private String id;
	private Boolean active;
	private Long creationDate;
	private Long changedDate;
	private Integer version;
	private Integer order;
	private Integer priority;
	private TaskState state;
	private TaskCommState commState;
	private Long timeout;
	private RestRequest request;
	private String targetObjectDN;
	private String parentTaskId;
	private List<TaskMessage> taskHistory;
	private String owner;
	private String targetJID;
	private String pluginId;
	
	private ParentTask parent;

	public Task() {
		super();
	}

	public Task(String id, Boolean active, Long creationDate, Long changedDate,
			Integer version, Integer order, Integer priority, TaskState state,
			TaskCommState commState, Long timeout, RestRequest request,
			String targetObjectDN, String parentTaskId,
			List<TaskMessage> taskHistory, String owner, String targetJID,
			String pluginId) {
		super();
		this.id = id;
		this.active = active;
		this.creationDate = creationDate;
		this.changedDate = changedDate;
		this.version = version;
		this.order = order;
		this.priority = priority;
		this.state = state;
		this.commState = commState;
		this.timeout = timeout;
		this.request = request;
		this.targetObjectDN = targetObjectDN;
		this.parentTaskId = parentTaskId;
		this.taskHistory = taskHistory;
		this.owner = owner;
		this.targetJID = targetJID;
		this.pluginId = pluginId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public Date getCreationDate() {
		return new Date(creationDate);
	}

	public Date getChangedDate() {
		return new Date(changedDate);
	}

	public void setChangedDate(Long changedDate) {
		this.changedDate = changedDate;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getOrder() {
		return order;
	}

	public Integer getPriority() {
		return priority;
	}

	public TaskState getState() {
		return state;
	}

	public Date getTimeout() {
		return new Date(timeout);
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public RestRequest getRequest() {
		return request;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setState(TaskState state) {
		this.state = state;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public List<TaskMessage> getTaskHistory() {
		return taskHistory;
	}

	public String getTargetObjectDN() {

		return targetObjectDN;
	}

	public void setTargetObjectDN(String targetObjectDN) {
		this.targetObjectDN = targetObjectDN;
	}

	public String getTargetJID() {
		return targetJID;
	}

	public void setTargetJID(String targetJID) {
		this.targetJID = targetJID;
	}

	public TaskCommState getCommState() {
		return commState;
	}

	public void setCommState(TaskCommState commState) {
		this.commState = commState;
	}

	public void addSubTask(Task task) {
		task.setParentTaskId(this.getId());
	}

	public void setTaskHistory(List<TaskMessage> taskHistory) {
		this.taskHistory = taskHistory;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public ParentTask getParent() {
		return parent;
	}

	public void setParent(ParentTask parent) {
		this.parent = parent;
	}
	
}
