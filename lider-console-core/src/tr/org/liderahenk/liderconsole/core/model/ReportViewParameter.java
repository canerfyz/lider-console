package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportViewParameter implements Serializable {

	private static final long serialVersionUID = -6165026135024798533L;

	private Long id;

	private Long referencedParameterId;

	private String label;

	private String value;

	private Date timestamp;

	public ReportViewParameter() {
	}

	public ReportViewParameter(Long id, Long referencedParameterId, String label, String value) {
		this.id = id;
		this.referencedParameterId = referencedParameterId;
		this.label = label;
		this.value = value;
		this.timestamp = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReferencedParameterId() {
		return referencedParameterId;
	}

	public void setReferencedParameterId(Long referencedParameterId) {
		this.referencedParameterId = referencedParameterId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
