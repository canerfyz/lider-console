package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * This class represents a report column defined in a report template.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportTemplateColumn implements Serializable {

	private static final long serialVersionUID = 7125324167860199185L;

	private Long id;

	private String name;

	private Integer columnOrder;

	private Date timestamp;

	public ReportTemplateColumn() {
	}

	public ReportTemplateColumn(Long id, String name, Integer columnOrder) {
		super();
		this.id = id;
		this.name = name;
		this.columnOrder = columnOrder;
		this.timestamp = new Date();
	}

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

	public Integer getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(Integer columnOrder) {
		this.columnOrder = columnOrder;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
