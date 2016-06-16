package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import tr.org.liderahenk.liderconsole.core.xmpp.enums.ContentType;
import tr.org.liderahenk.liderconsole.core.xmpp.enums.StatusCode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandExecutionResult implements Serializable {

	private static final long serialVersionUID = -5686393473804161919L;

	private Long id;

	private Long agentId;

	private StatusCode responseCode;

	private String responseMessage;

	private byte[] responseData;

	private ContentType contentType;

	private Date createDate;

	public CommandExecutionResult() {
	}

	public CommandExecutionResult(Long id, Long agentId, StatusCode responseCode, String responseMessage,
			byte[] responseData, ContentType contentType, Date createDate) {
		this.id = id;
		this.agentId = agentId;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.responseData = responseData;
		this.contentType = contentType;
		this.createDate = createDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agentId == null) ? 0 : agentId.hashCode());
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((responseCode == null) ? 0 : responseCode.hashCode());
		result = prime * result + Arrays.hashCode(responseData);
		result = prime * result + ((responseMessage == null) ? 0 : responseMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandExecutionResult other = (CommandExecutionResult) obj;
		if (agentId == null) {
			if (other.agentId != null)
				return false;
		} else if (!agentId.equals(other.agentId))
			return false;
		if (contentType != other.contentType)
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (responseCode != other.responseCode)
			return false;
		if (!Arrays.equals(responseData, other.responseData))
			return false;
		if (responseMessage == null) {
			if (other.responseMessage != null)
				return false;
		} else if (!responseMessage.equals(other.responseMessage))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public StatusCode getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(StatusCode responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public byte[] getResponseData() {
		return responseData;
	}

	public void setResponseData(byte[] responseData) {
		this.responseData = responseData;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
