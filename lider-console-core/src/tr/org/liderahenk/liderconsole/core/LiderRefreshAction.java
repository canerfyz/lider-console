package tr.org.liderahenk.liderconsole.core;

import org.eclipse.jface.action.Action;

public class LiderRefreshAction extends Action{
	
	@Override public String getId() {
		return "org.eclipse.ui.file.refresh";
	};
	
	@Override
	public void run() {
		super.run();
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

}
