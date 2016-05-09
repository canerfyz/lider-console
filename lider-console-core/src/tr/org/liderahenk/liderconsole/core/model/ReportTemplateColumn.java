package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;

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

	private boolean visible;

	private Integer width;

	private Integer columnOrder;

	public ReportTemplateColumn() {
	}

	public ReportTemplateColumn(Long id, String name, boolean visible, Integer width, Integer columnOrder) {
		this.id = id;
		this.name = name;
		this.visible = visible;
		this.width = width;
		this.columnOrder = columnOrder;
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(Integer columnOrder) {
		this.columnOrder = columnOrder;
	}

}
