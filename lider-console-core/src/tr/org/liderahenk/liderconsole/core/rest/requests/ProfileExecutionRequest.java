package tr.org.liderahenk.liderconsole.core.rest.requests;

import java.util.Date;
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

	private Date activationDate;

	public ProfileExecutionRequest() {
	}

	public ProfileExecutionRequest(Long id, List<String> dnList, RestDNType dnType, Date activationDate) {
		super();
		this.id = id;
		this.dnList = dnList;
		this.dnType = dnType;
		this.activationDate = activationDate;
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

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	@Override
	public String toString() {
		return "ProfileExecutionRequest [id=" + id + ", dnList=" + dnList + ", dnType=" + dnType + ", activationDate="
				+ activationDate + "]";
	}

	@Override
	public String toJson() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

}
