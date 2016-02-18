package tr.org.liderahenk.liderconsole.core.task;

public interface ITask {

	String getTaskName();
	TaskState getState();
	String getCreationDate();
	String getChangedDate();
}
