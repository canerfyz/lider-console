package tr.org.liderahenk.liderconsole.core.valueeditors.liderprivilege;

import org.apache.directory.studio.valueeditors.AbstractDialogStringValueEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 * 
 * Custom dialog handler to open Lider Privilege Editor when double-clicked on liderPrivilege attribute.
 *
 */
public class LiderPrivilegeEditor extends AbstractDialogStringValueEditor {
	
	@Override
	protected boolean openDialog(Shell arg0) {
        Object value = getValue();
        if (null != value) {
    		LiderPriviligeInfoEditorWidget dialog = new LiderPriviligeInfoEditorWidget(Display.getDefault().getActiveShell(), getValue().toString());
           	dialog.create();
           	dialog.open();
           	setValue(dialog.getLiderPrivilegeInfo());
           	return true;
		}
        return false;
	}
}
