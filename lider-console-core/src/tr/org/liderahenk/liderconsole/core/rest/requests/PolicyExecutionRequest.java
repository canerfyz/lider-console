package tr.org.liderahenk.liderconsole.core.rest.requests;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import tr.org.liderahenk.liderconsole.core.ldap.enums.DNType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyExecutionRequest implements IRequest {

	private static final long serialVersionUID = -8667471731742525283L;

	private Long id;

	private List<String> dnList;

	private DNType dnType;

	private Date activationDate;

	public PolicyExecutionRequest() {
	}

	public PolicyExecutionRequest(Long id, List<String> dnList, DNType dnType, Date activationDate) {
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

	public DNType getDnType() {
		return dnType;
	}

	public void setDnType(DNType dnType) {
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
		return "PolicyExecutionRequest [id=" + id + ", dnList=" + dnList + ", dnType=" + dnType + ", activationDate="
				+ activationDate + "]";
	}

	@Override
	public String toJson() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

}
