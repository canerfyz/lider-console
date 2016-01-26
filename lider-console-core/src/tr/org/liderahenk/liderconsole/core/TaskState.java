package tr.org.liderahenk.liderconsole.core;

public enum TaskState {
	CREATED,
	TASK_RECEIVED,
	TASK_PUT_IN_QUEUE,
	TASK_PROCESSING,
	TASK_PROCESSED,
	TASK_ERROR,
	TASK_TIMEOUT,
	TASK_KILLED;
}
