package tr.org.liderahenk.liderconsole.core.rest.requests;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import tr.org.liderahenk.liderconsole.core.rest.enums.RestDNType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileExecutionRequest implements IRequest {

	private static final long serialVersionUID = 860822129955062004L;

	private Long id;

	private List<String> dnList;

	private RestDNType dnType;

	public ProfileExecutionRequest() {
	}

	public ProfileExecutionRequest(Long id, List<String> dnList, RestDNType dnType) {
		this.id = id;
		this.dnList = dnList;
		this.dnType = dnType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getDnList() {
		return dnList;
	}

	public void setDnList(List<String> dnList) {
		this.dnList = dnList;
	}

	public RestDNType getDnType() {
		return dnType;
	}

	public void setDnType(RestDNType dnType) {
		this.dnType = dnType;
	}

	@Override
	public String toString() {
		return "ProfileExecutionRequest [id=" + id + ", dnList=" + dnList + ", dnType=" + dnType + "]";
	}

	@Override
	public String toJson() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

}
