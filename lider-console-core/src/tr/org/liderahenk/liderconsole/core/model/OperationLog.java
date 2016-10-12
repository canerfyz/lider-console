package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationLog implements Serializable {

	private static final long serialVersionUID = 2717258293731093402L;

	private Long id;

	private String userId;

	private String crudType;

	private Long taskId;

	private Long policyId;

	private Long profileId;

	private String logMessage;

	private byte[] requestData;

	private String requestIp;

	private Date createDate;
	
	public OperationLog() {
	}

	public OperationLog(Long id, String userId, String crudType, Long taskId, Long policyId, Long profileId,
			String logMessage, byte[] requestData, String requestIp, Date createDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.crudType = crudType;
		this.taskId = taskId;
		this.policyId = policyId;
		this.profileId = profileId;
		this.logMessage = logMessage;
		this.requestData = requestData;
		this.requestIp = requestIp;
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCrudType() {
		return crudType;
	}

	public void setCrudType(String crudType) {
		this.crudType = crudType;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public byte[] getRequestData() {
		return requestData;
	}

	public void setRequestData(byte[] requestData) {
		this.requestData = requestData;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
