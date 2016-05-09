package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Model class for report templates.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportTemplate implements Serializable {

	private static final long serialVersionUID = 7168427575474490340L;

	private Long id;

	private String name;

	private String description;

	private String query;

	private List<ReportTemplateParameter> templateParams;

	private List<ReportTemplateColumn> templateColumns;

	private String reportHeader;

	private String reportFooter;

	private Date createDate;

	private Date modifyDate;

	public ReportTemplate() {
	}

	public ReportTemplate(Long id, String name, String description, String query,
			List<ReportTemplateParameter> templateParams, List<ReportTemplateColumn> templateColumns,
			String reportHeader, String reportFooter, Date createDate, Date modifyDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.query = query;
		this.templateParams = templateParams;
		this.templateColumns = templateColumns;
		this.reportHeader = reportHeader;
		this.reportFooter = reportFooter;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<ReportTemplateParameter> getTemplateParams() {
		return templateParams;
	}

	public void setTemplateParams(List<ReportTemplateParameter> templateParams) {
		this.templateParams = templateParams;
	}

	public List<ReportTemplateColumn> getTemplateColumns() {
		return templateColumns;
	}

	public void setTemplateColumns(List<ReportTemplateColumn> templateColumns) {
		this.templateColumns = templateColumns;
	}

	public String getReportHeader() {
		return reportHeader;
	}

	public void setReportHeader(String reportHeader) {
		this.reportHeader = reportHeader;
	}

	public String getReportFooter() {
		return reportFooter;
	}

	public void setReportFooter(String reportFooter) {
		this.reportFooter = reportFooter;
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
