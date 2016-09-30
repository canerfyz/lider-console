package tr.org.liderahenk.liderconsole.core.model;

import java.util.List;

public class LiderPrivilege {

	private String taskTargetEntry;

	private List<String> taskCodes;

	private List<String> reportCodes;

	public String getTaskTargetEntry() {
		return taskTargetEntry;
	}

	public void setTaskTargetEntry(String taskTargetEntry) {
		this.taskTargetEntry = taskTargetEntry;
	}

	public List<String> getTaskCodes() {
		return taskCodes;
	}

	public void setTaskCodes(List<String> taskCodes) {
		this.taskCodes = taskCodes;
	}

	public List<String> getReportCodes() {
		return reportCodes;
	}

	public void setReportCodes(List<String> reportCodes) {
		this.reportCodes = reportCodes;
	}

}
