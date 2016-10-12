package tr.org.liderahenk.liderconsole.core.handlers;

import org.apache.directory.studio.valueeditors.AbstractDialogStringValueEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import tr.org.liderahenk.liderconsole.core.current.UserSettings;
import tr.org.liderahenk.liderconsole.core.dialogs.LiderPrivilegeDialog;
import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.ldap.listeners.LdapConnectionListener;
import tr.org.liderahenk.liderconsole.core.ldap.utils.LdapUtils;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class LiderPrivilegeHandler extends AbstractDialogStringValueEditor {

	@Override
	protected boolean openDialog(Shell arg0) {
		if (!LdapUtils.getInstance().isAdmin(UserSettings.USER_DN, LdapConnectionListener.getConnection(),
				LdapConnectionListener.getMonitor())) {
			Notifier.error(null, Messages.getString("NEED_ADMIN_PRIVILEGE"));
			return false;
		}
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
