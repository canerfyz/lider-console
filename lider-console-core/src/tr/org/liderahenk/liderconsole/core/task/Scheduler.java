package tr.org.liderahenk.liderconsole.core.task;

import java.util.Date;

/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 * 
 * return json object for listing of schedeled tasks is deserialized to Scheduler object.
 */
public class Scheduler {
	private String id;
	private Long startDate;
	private Long endDate;
	private Long lastUpdate;
	private String systemRequest;
	private Boolean active;
	private String cronFormat;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getStartDate() {
		return new Date(startDate);
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return new Date(endDate);
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	public Date getLastUpdate() {
		return new Date(lastUpdate);
	}
	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getSystemRequest() {
		return systemRequest;
	}
	public void setSystemRequest(String systemRequest) {
		this.systemRequest = systemRequest;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getCronFormat() {
		return cronFormat;
	}
	public void setCronFormat(String cronFormat) {
		this.cronFormat = cronFormat;
	}
}
