package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import tr.org.liderahenk.liderconsole.core.rest.enums.RestDNType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandExecution implements Serializable {

	private static final long serialVersionUID = -4216451810091655736L;

	private Long id;

	private RestDNType dnType;

	private String dn;

	private Date createDate;

	private List<CommandExecutionResult> commandExecutionResults;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RestDNType getDnType() {
		return dnType;
	}

	public void setDnType(RestDNType dnType) {
		this.dnType = dnType;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<CommandExecutionResult> getCommandExecutionResults() {
		return commandExecutionResults;
	}

	public void setCommandExecutionResults(List<CommandExecutionResult> commandExecutionResults) {
		this.commandExecutionResults = commandExecutionResults;
	}

}
