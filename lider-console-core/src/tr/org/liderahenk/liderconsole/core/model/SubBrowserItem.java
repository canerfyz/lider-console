package tr.org.liderahenk.liderconsole.core.model;

import java.util.ArrayList;
import java.util.List;

public class SubBrowserItem {

	private String id;
	private String title;
	private String type;
	private SubBrowserItem parent;
	private List<SubBrowserItem> children;
	
	public SubBrowserItem() {
		parent = null;
		children = new ArrayList<SubBrowserItem>();
	}
	
	public void add(SubBrowserItem dir) {
		children.add(dir);
		dir.setParent( this );
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setParent(SubBrowserItem parent) {
		this.parent = parent;
	}
	// recursively removes
	public void remove(SubBrowserItem child) {
		for(SubBrowserItem subchild: children){
			child.remove(subchild);
		}
		children.remove(child);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<SubBrowserItem> getChildren() {
		return this.children;
	}

	public void setChildren(List<SubBrowserItem> children) {
		this.children = children;
	}

	public SubBrowserItem getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return getTitle();
	}	
	
}
