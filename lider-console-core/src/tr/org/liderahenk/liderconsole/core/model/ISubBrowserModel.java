package tr.org.liderahenk.liderconsole.core.model;

import java.util.List;


public interface ISubBrowserModel {
	public SubBrowserItem getRoot();
	public List<SubBrowserItem> getChildren(SubBrowserItem parentNode); 
}
