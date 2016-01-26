package tr.org.liderahenk.liderconsole.core.valueeditors.liderprivilege;

import java.util.List;

public class PrivilegeObject {
	private String urls;
	private List<Privilege> pluginList;
	
	public String getUrls() {
		return urls;
	}
	public void setUrls(String urls) {
		this.urls = urls;
	}
	public List<Privilege> getPluginList() {
		return pluginList;
	}
	public void setPluginList(List<Privilege> pluginList) {
		this.pluginList = pluginList;
	}
}
