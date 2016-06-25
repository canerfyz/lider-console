package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import tr.org.liderahenk.liderconsole.core.ldap.enums.DNType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchGroupEntry implements Serializable {

	private static final long serialVersionUID = 5365177242820746144L;

	private Long id;

	private String dn;

	private DNType dnType;

	public SearchGroupEntry() {
	}

	public SearchGroupEntry(Long id, String dn, DNType dnType) {
		this.id = id;
		this.dn = dn;
		this.dnType = dnType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public DNType getDnType() {
		return dnType;
	}

	public void setDnType(DNType dnType) {
		this.dnType = dnType;
	}

	@Override
	public String toString() {
		return "SearchGroupEntry [id=" + id + ", dn=" + dn + ", dnType=" + dnType + "]";
	}

}
