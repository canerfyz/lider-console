package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile implements Serializable {

	private static final long serialVersionUID = 6401256297602058418L;

	private Long id;

	private String label;

	private String description;

	private boolean overridable;

	private boolean active;

	private boolean deleted;

	private byte[] profileData;

	private Date createDate;

	private Date modifyDate;

	public Profile() {
	}

	public Profile(Long id, String label, String description, boolean overridable, boolean active, boolean deleted,
			byte[] profileData, Date createDate, Date modifyDate) {
		super();
		this.id = id;
		this.label = label;
		this.description = description;
		this.overridable = overridable;
		this.active = active;
		this.deleted = deleted;
		this.profileData = profileData;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
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

	public boolean isOverridable() {
		return overridable;
	}

	public void setOverridable(boolean overridable) {
		this.overridable = overridable;
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

	public byte[] getProfileData() {
		return profileData;
	}

	public void setProfileData(byte[] profileData) {
		this.profileData = profileData;
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

}
