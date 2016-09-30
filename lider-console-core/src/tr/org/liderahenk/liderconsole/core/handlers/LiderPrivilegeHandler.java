package tr.org.liderahenk.liderconsole.core.handlers;

import org.apache.directory.studio.valueeditors.AbstractDialogStringValueEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import tr.org.liderahenk.liderconsole.core.dialogs.LiderPrivilegeDialog;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class LiderPrivilegeHandler extends AbstractDialogStringValueEditor {

	@Override
	protected boolean openDialog(Shell arg0) {
		Object value = getValue();
		if (null != value) {
			LiderPrivilegeDialog dialog = new LiderPrivilegeDialog(Display.getDefault().getActiveShell(),
					getValue().toString());
			dialog.create();
			dialog.open();
			setValue(dialog.getSelectedPrivilege());
			return true;
		}
		return false;
	}

}
