package tr.org.liderahenk.liderconsole.core.handlers;

import java.util.Set;

import org.eclipse.swt.widgets.Display;

import tr.org.liderahenk.liderconsole.core.dialogs.PolicyExecutionSelectDialog;

/**
 * Handle policy execution dialog which is responsible for execution of
 * already-existing policies.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class PolicyExecutionSelectHandler extends MultipleSelectionHandler {

	@Override
	public void executeWithDNSet(Set<String> dnSet) {
		PolicyExecutionSelectDialog dialog = new PolicyExecutionSelectDialog(Display.getDefault().getActiveShell(),
				dnSet);
		dialog.create();
		dialog.open();
	}

}
