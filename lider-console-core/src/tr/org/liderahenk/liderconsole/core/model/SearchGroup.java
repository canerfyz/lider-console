package tr.org.liderahenk.liderconsole.core.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import tr.org.liderahenk.liderconsole.core.rest.requests.IRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchGroup implements IRequest {

	private static final long serialVersionUID = -1764382674418917627L;

	private Long id;

	private String name;

	private boolean searchAgents;

	private boolean searchUsers;

	private boolean searchGroups;

	private Map<String, String> criteria;

	private Set<SearchGroupEntry> entries;

	private Date createDate;

	public SearchGroup() {
	}

	public SearchGroup(Long id, String name, boolean searchAgents, boolean searchUsers, boolean searchGroups,
			Map<String, String> criteria, Set<SearchGroupEntry> entries, Date createDate) {
		this.id = id;
		this.name = name;
		this.searchAgents = searchAgents;
		this.searchUsers = searchUsers;
		this.searchGroups = searchGroups;
		this.criteria = criteria;
		this.entries = entries;
		this.createDate = createDate;
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

	public boolean isSearchAgents() {
		return searchAgents;
	}

	public void setSearchAgents(boolean searchAgents) {
		this.searchAgents = searchAgents;
	}

	public boolean isSearchUsers() {
		return searchUsers;
	}

	public void setSearchUsers(boolean searchUsers) {
		this.searchUsers = searchUsers;
	}

	public boolean isSearchGroups() {
		return searchGroups;
	}

	public void setSearchGroups(boolean searchGroups) {
		this.searchGroups = searchGroups;
	}

	public Map<String, String> getCriteria() {
		return criteria;
	}

	public void setCriteria(Map<String, String> criteria) {
		this.criteria = criteria;
	}

	public Set<SearchGroupEntry> getEntries() {
		return entries;
	}

	public void setEntries(Set<SearchGroupEntry> entries) {
		this.entries = entries;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "SearchGroup [id=" + id + ", name=" + name + ", searchAgents=" + searchAgents + ", searchUsers="
				+ searchUsers + ", searchGroups=" + searchGroups + ", criteria=" + criteria + ", entries=" + entries
				+ ", createDate=" + createDate + "]";
	}

	@Override
	public String toJson() throws Exception {
		return new ObjectMapper().writeValueAsString(this);
	}

}
