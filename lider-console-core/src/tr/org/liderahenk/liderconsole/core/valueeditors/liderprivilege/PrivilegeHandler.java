package tr.org.liderahenk.liderconsole.core.valueeditors.liderprivilege;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import tr.org.liderahenk.liderconsole.core.valueeditors.liderprivilege.LiderPriviligeInfoEditorWidget;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class PrivilegeHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public PrivilegeHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		LiderPriviligeInfoEditorWidget dialog = new LiderPriviligeInfoEditorWidget(Display.getDefault().getActiveShell(), "");
       	dialog.create();
       	dialog.open();
       	return null;
	}
}