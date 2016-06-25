package tr.org.liderahenk.liderconsole.core.labelproviders;

import javax.naming.directory.SearchResult;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.ldap.listeners.LdapConnectionListener;
import tr.org.liderahenk.liderconsole.core.ldap.utils.LdapUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class LdapSearchEditorLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof SearchResult) {
			return ((SearchResult) element).getName();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if (element instanceof SearchResult) {
			if (LdapUtils.getInstance().isAgent(((SearchResult) element).getName(),
					LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor())) {
				return SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE,
						"icons/16/computer.png");
			} else if (LdapUtils.getInstance().isUser(((SearchResult) element).getName(),
					LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor())) {
				return SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/user.png");
			}
		}
		return SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/folder-open.png");
	}

}
