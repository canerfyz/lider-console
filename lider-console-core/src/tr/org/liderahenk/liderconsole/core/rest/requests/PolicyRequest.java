package tr.org.liderahenk.liderconsole.core.rest.requests;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyRequest implements IRequest {

	private static final long serialVersionUID = 5808245859229954722L;

	private Long id;

	private String label;

	private String description;

	private boolean active;

	private List<Long> profileIdList;

	private Date timestamp;

	public PolicyRequest() {
	}

	public PolicyRequest(Long id, String label, String description, boolean active, List<Long> profileIdList,
			Date timestamp) {
		super();
		this.id = id;
		this.label = label;
		this.description = description;
		this.active = active;
		this.profileIdList = profileIdList;
		this.timestamp = timestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Long> getProfileIdList() {
		return profileIdList;
	}

	public void setProfileIdList(List<Long> profileIdList) {
		this.profileIdList = profileIdList;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "PolicyRequest [id=" + id + ", label=" + label + ", description=" + description + ", active=" + active
				+ ", profileIdList=" + profileIdList + ", timestamp=" + timestamp + "]";
	}

	@Override
	public String toJson() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

}
