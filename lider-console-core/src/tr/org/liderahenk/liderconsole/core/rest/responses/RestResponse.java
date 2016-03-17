package tr.org.liderahenk.liderconsole.core.rest.responses;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import tr.org.liderahenk.liderconsole.core.rest.enums.RestResponseStatus;
import tr.org.liderahenk.liderconsole.core.rest.requests.TaskRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponse implements IResponse {

	private static final long serialVersionUID = -4862055188412498189L;

	/**
	 * Contains result status. This is the only status code that can be used for
	 * handling responses.
	 */
	private RestResponseStatus status;

	/**
	 * Response messages can be used along with status to notify result.
	 */
	private List<String> messages;

	/**
	 * Contains result parameters which can be used by the plugin (e.g.
	 * displaying results)
	 */
	private Map<String, Object> resultMap;

	public RestResponse() {
	}

	public RestResponse(RestResponseStatus status, List<String> messages, Map<String, Object> resultMap) {
		this.status = status;
		this.messages = messages;
		this.resultMap = resultMap;
	}

	@Override
	public RestResponseStatus getStatus() {
		return status;
	}

	public void setStatus(RestResponseStatus status) {
		this.status = status;
	}

	@Override
	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override
	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	@Override
	public String toString() {
		return "RestResponse [status=" + status + ", messages=" + messages + ", resultMap=" + resultMap + "]";
	}

	@Override
	public Object toObject(String json) throws Exception {
		return new ObjectMapper().readValue(json, TaskRequest.class);
	}

}
