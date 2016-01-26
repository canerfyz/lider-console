package tr.org.liderahenk.liderconsole.core.task;

import java.util.Date;

public class ScheduleRequest {
	
	private ScheduleOperation operation;
	private String scheduleId;
	private String cronString;
	private Long startDate;
	private Long endDate;
	private boolean isActive;
	
	public ScheduleOperation getOperation() {
		return operation;
	}
	
	public String getScheduleId() {
		return scheduleId;
	}
	
	public String getCronString() {
		return cronString;
	}
	
	public Date getStartDate() {
		return new Date(startDate);
	}
	
	public Date getEndDate() {
		return new Date(endDate);
	}
	
	public Boolean isActive() {
		return isActive;
	}

	public void setOperation(ScheduleOperation operation) {
		this.operation = operation;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public void setCronString(String cronString) {
		this.cronString = cronString;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
