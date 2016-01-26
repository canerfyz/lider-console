package tr.org.liderahenk.liderconsole.core.task;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 * 
 */
public enum TaskState {

	CREATED, TASK_RECEIVED, TASK_PUT_IN_QUEUE, TASK_PROCESSING, TASK_PROCESSED, TASK_TIMEOUT, TASK_WARNING, TASK_ERROR, TASK_KILLED;

	private static final Map<String, TaskState> map;

	static {
		map = new HashMap<String, TaskState>();
		map.put("processed_task", TaskState.TASK_PROCESSED);
		map.put("processing_task", TaskState.TASK_PROCESSING);
		map.put("task_put_in_queue", TaskState.TASK_PUT_IN_QUEUE);
		map.put("task_timeout", TaskState.TASK_TIMEOUT);
		map.put("task_received", TaskState.TASK_RECEIVED);
	}

	public static TaskState match(String updateType) {
		return map.get(updateType);
	}

}
