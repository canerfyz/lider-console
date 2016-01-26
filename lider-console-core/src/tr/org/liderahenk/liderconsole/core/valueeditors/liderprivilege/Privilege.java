package tr.org.liderahenk.liderconsole.core.valueeditors.liderprivilege;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 *
 */
public class Privilege {
	
	private String attribute;
	private String action;
	private String command;
	private String pluginId;
	private String pluginVersion;
	private HashMap<String,String> displayName;
	private String pluginTitle;

	private String groupId;

	private String groupTitle;
	
	private Map<String,String> groupTitles;
	
	public Privilege() {

	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public HashMap<String, String> getDisplayName() {
		return displayName;
	}

	public void setDisplayName(HashMap<String, String> displayName) {
		this.displayName = displayName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupTitle() {
		return groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public Map<String, String> getGroupTitles() {
		return groupTitles;
	}

	public void setGroupTitles(Map<String, String> groupTitles) {
		this.groupTitles = groupTitles;
	}

	public String getPluginTitle() {
		return pluginTitle;
	}

	public void setPlıginTitle(String plıginTitle) {
		this.pluginTitle = plıginTitle;
	}
}

