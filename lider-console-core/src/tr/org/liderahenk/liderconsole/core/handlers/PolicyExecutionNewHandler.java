package tr.org.liderahenk.liderconsole.core.handlers;

import java.util.Set;

import org.eclipse.swt.widgets.Display;

import tr.org.liderahenk.liderconsole.core.dialogs.PolicyExecutionNewDialog;

/**
 * 
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class PolicyExecutionNewHandler extends MultipleSelectionHandler {

	@Override
	public void executeWithDNSet(Set<String> dnSet) {
		PolicyExecutionNewDialog dialog = new PolicyExecutionNewDialog(Display.getDefault().getActiveShell(), dnSet);
		dialog.create();
		dialog.open();
	}

}
