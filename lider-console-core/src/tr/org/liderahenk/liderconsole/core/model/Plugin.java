package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * This is a specialized class which is used to list plugins with some
 * additional info.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Plugin implements Serializable {

	private static final long serialVersionUID = 2681636507286701898L;

	private Long id;

	private String name;

	private String version;

	private String description;

	private boolean active = true;

	private boolean deleted = false;

	private boolean machineOriented;

	private boolean userOriented;

	private boolean policyPlugin;

	private boolean taskPlugin;

	private boolean xBased;

	private Date createDate;

	private Date modifyDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public boolean isMachineOriented() {
		return machineOriented;
	}

	public void setMachineOriented(boolean machineOriented) {
		this.machineOriented = machineOriented;
	}

	public boolean isUserOriented() {
		return userOriented;
	}

	public void setUserOriented(boolean userOriented) {
		this.userOriented = userOriented;
	}

	public boolean isPolicyPlugin() {
		return policyPlugin;
	}

	public void setPolicyPlugin(boolean policyPlugin) {
		this.policyPlugin = policyPlugin;
	}

	public boolean isxBased() {
		return xBased;
	}

	public void setxBased(boolean xBased) {
		this.xBased = xBased;
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

	public boolean isTaskPlugin() {
		return taskPlugin;
	}

	public void setTaskPlugin(boolean taskPlugin) {
		this.taskPlugin = taskPlugin;
	}

}
