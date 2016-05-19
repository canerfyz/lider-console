package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentProperty implements Serializable {

	private static final long serialVersionUID = 5939391958333270115L;

	private Long id;

	private String propertyName;

	private String propertyValue;

	private Date createDate;

	public AgentProperty() {
	}

	public AgentProperty(Long id, String propertyName, String propertyValue, Date createDate) {
		this.id = id;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
