package tr.org.liderahenk.liderconsole.core.handlers;

import org.eclipse.swt.widgets.Display;

import tr.org.liderahenk.liderconsole.core.dialogs.AgentDetailDialog;

/**
 * Handle policy execution dialog which is responsible for execution of
 * already-existing policies.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class AgentDetailInfoHandler extends SingleSelectionHandler {

	@Override
	public void executeWithDn(String dn) {
		AgentDetailDialog dialog = new AgentDetailDialog(Display.getDefault().getActiveShell(), dn);
		dialog.create();
		dialog.open();
	}

}
