package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Policy implements Serializable {

	private static final long serialVersionUID = 7155303948904448104L;

	private Long id;

	private String label;

	private String description;

	private boolean active = true;

	private boolean deleted = false;

	private Set<Profile> profiles = new HashSet<Profile>();

	private Date createDate;

	private Date modifyDate;

	private String policyVersion;

	public Policy() {
	}

	public Policy(Long id, String label, String description, boolean active, boolean deleted, Set<Profile> profiles,
			Date createDate, Date modifyDate, String policyVersion) {
		this.id = id;
		this.label = label;
		this.description = description;
		this.active = active;
		this.deleted = deleted;
		this.profiles = profiles;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		this.policyVersion = policyVersion;
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Set<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Set<Profile> profiles) {
		this.profiles = profiles;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getPolicyVersion() {
		return policyVersion;
	}

	public void setPolicyVersion(String policyVersion) {
		this.policyVersion = policyVersion;
	}

}
