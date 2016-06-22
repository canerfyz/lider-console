package tr.org.liderahenk.liderconsole.core.rest.requests;

import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportGenerationRequest implements IRequest {

	private static final long serialVersionUID = 2446346120454832435L;

	private Long viewId;

	private Map<String, Object> paramValues;

	private Date timestamp;

	public Long getViewId() {
		return viewId;
	}

	public void setViewId(Long viewId) {
		this.viewId = viewId;
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
		return "ReportGenerationRequest [viewId=" + viewId + ", paramValues=" + paramValues + ", timestamp=" + timestamp
				+ "]";
	}

	@Override
	public String toJson() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

}
