package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import tr.org.liderahenk.liderconsole.core.ldap.enums.DNType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Command implements Serializable {

	private static final long serialVersionUID = 8822586436564699620L;

	private Long id;

	private Task task;

	private List<String> dnList;

	private DNType dnType;

	private String commandOwnerUid;

	private Date activationDate;

	private Date createDate;

	private List<CommandExecution> commandExecutions;

	public Command() {
	}

	public Command(Long id, Task task, List<String> dnList, DNType dnType, String commandOwnerUid, Date activationDate,
			Date createDate, List<CommandExecution> commandExecutions) {
		this.id = id;
		this.task = task;
		this.dnList = dnList;
		this.dnType = dnType;
		this.commandOwnerUid = commandOwnerUid;
		this.createDate = createDate;
		this.activationDate = activationDate;
		this.commandExecutions = commandExecutions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public List<String> getDnList() {
		return dnList;
	}

	public void setDnList(List<String> dnList) {
		this.dnList = dnList;
	}

	public DNType getDnType() {
		return dnType;
	}

	public void setDnType(DNType dnType) {
		this.dnType = dnType;
	}

	public String getCommandOwnerUid() {
		return commandOwnerUid;
	}

	public void setCommandOwnerUid(String commandOwnerUid) {
		this.commandOwnerUid = commandOwnerUid;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<CommandExecution> getCommandExecutions() {
		return commandExecutions;
	}

	public void setCommandExecutions(List<CommandExecution> commandExecutions) {
		this.commandExecutions = commandExecutions;
	}

}
