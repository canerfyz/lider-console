package tr.org.liderahenk.liderconsole.core.schedule;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 * Request class for rest requests. Lider server expects ScheduleRequest object in order to schedule task.
 */
public class ScheduleRequest {
		
	private ScheduleOperation operation;
	private String scheduleId;
	private String cronString;
	private Long startDate;
	private Long endDate;
	private Boolean active;
	
	public ScheduleOperation getOperation() {
		return operation;
	}
	
	public String getScheduleId() {
		return scheduleId;
	}
	
	public String getCronString() {
		return cronString;
	}
	
	public Long getStartDate() {
		return startDate;
	}
	
	public Long getEndDate() {
		return endDate;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public void setOperation(final ScheduleOperation operation) {
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

	public void setActive(Boolean active) {
		this.active = active;
	}
}
