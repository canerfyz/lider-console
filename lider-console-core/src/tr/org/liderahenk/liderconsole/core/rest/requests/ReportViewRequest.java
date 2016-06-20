package tr.org.liderahenk.liderconsole.core.rest.requests;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import tr.org.liderahenk.liderconsole.core.model.ReportType;
import tr.org.liderahenk.liderconsole.core.model.ReportViewColumn;
import tr.org.liderahenk.liderconsole.core.model.ReportViewParameter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportViewRequest implements IRequest {

	private static final long serialVersionUID = -8683091527267375178L;

	private Long id;

	private Long templateId;

	private String name;

	private String description;

	private ReportType type;

	private List<ReportViewParameter> viewParams;

	private List<ReportViewColumn> viewColumns;

	private Date timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}

	public List<ReportViewParameter> getViewParams() {
		return viewParams;
	}

	public void setViewParams(List<ReportViewParameter> viewParams) {
		this.viewParams = viewParams;
	}

	public List<ReportViewColumn> getViewColumns() {
		return viewColumns;
	}

	public void setViewColumns(List<ReportViewColumn> viewColumns) {
		this.viewColumns = viewColumns;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ReportViewRequest [id=" + id + ", templateId=" + templateId + ", name=" + name + ", description="
				+ description + ", type=" + type + ", viewParams=" + viewParams + ", viewColumns=" + viewColumns
				+ ", timestamp=" + timestamp + "]";
	}

	@Override
	public String toJson() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

}
