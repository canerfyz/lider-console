package tr.org.liderahenk.liderconsole.core;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


public class RefreshHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("refresh");
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
