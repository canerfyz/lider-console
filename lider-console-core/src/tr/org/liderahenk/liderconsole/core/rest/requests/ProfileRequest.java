package tr.org.liderahenk.liderconsole.core.rest.requests;

import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileRequest implements IRequest {

	private static final long serialVersionUID = 7824027431426797086L;

	private String pluginName;

	private String pluginVersion;

	private Long id;

	private String label;

	private String description;

	private boolean overridable;

	private boolean active;

	private Map<String, Object> profileData;

	private Date timestamp;

	public ProfileRequest() {
	}

	public ProfileRequest(String pluginName, String pluginVersion, Long id, String label, String description,
			boolean overridable, boolean active, Map<String, Object> profileData, Date timestamp) {
		this.pluginName = pluginName;
		this.pluginVersion = pluginVersion;
		this.id = id;
		this.label = label;
		this.description = description;
		this.overridable = overridable;
		this.active = active;
		this.profileData = profileData;
		this.timestamp = timestamp;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
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

	public Map<String, Object> getProfileData() {
		return profileData;
	}

	public void setProfileData(Map<String, Object> profileData) {
		this.profileData = profileData;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toJson() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

}
