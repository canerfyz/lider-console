package tr.org.liderahenk.liderconsole.core.rest.requests;

import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportGenerationRequest implements IRequest {

	private static final long serialVersionUID = 2446346120454832435L;

	private Long templateId;

	private Map<String, Object> paramValues;

	private Date timestamp;

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Map<String, Object> getParamValues() {
		return paramValues;
	}

	public void setParamValues(Map<String, Object> paramValues) {
		this.paramValues = paramValues;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ReportGenerationRequest [templateId=" + templateId + ", paramValues=" + paramValues + ", timestamp="
				+ timestamp + "]";
	}

	@Override
	public String toJson() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

}
