package tr.org.liderahenk.liderconsole.core.model;

import java.util.List;

public class SubBrowserModel implements ISubBrowserModel{

	final SubBrowserItem root;
	
	public SubBrowserModel(){
		root=new SubBrowserItem();
		root.setId("#root");
		root.setTitle("Root");
		root.setType("ROOT");
	}
	
	@Override
	public SubBrowserItem getRoot() {
		return root;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see tr.org.liderahenk.liderconsole.core.model.ISubBrowserModel#getChildren(tr.org.liderahenk.liderconsole.core.model.SubBrowserItem)
	 */
	public List<SubBrowserItem> getChildren(SubBrowserItem parentNode) {
		return parentNode.getChildren();
	}

}
