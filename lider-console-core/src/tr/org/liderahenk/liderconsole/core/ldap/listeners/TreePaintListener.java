package tr.org.liderahenk.liderconsole.core.ldap.listeners;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import org.apache.directory.api.ldap.model.schema.ObjectClass;
import org.apache.directory.studio.ldapbrowser.core.model.IBookmark;
import org.apache.directory.studio.ldapbrowser.core.model.IEntry;
import org.apache.directory.studio.ldapbrowser.core.model.impl.SearchResult;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.current.UserSettings;
import tr.org.liderahenk.liderconsole.core.ldap.utils.LdapUtils;

/**
 * This class is used to paint online/offline status images on LDAP tree while
 * listening to XMPP events.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class TreePaintListener implements Listener {

	private static final Logger logger = LoggerFactory.getLogger(TreePaintListener.class);

	private static TreePaintListener instance = null;

	private Tree tree;
	private Map<String, Boolean> presenceMap;
	private boolean xmppConnected = false;

	private final Image offlineImage;
	private final Image onlineImage;
	private final Image agentImage;
	private final Image userImage;

	public static synchronized TreePaintListener getInstance() {
		if (instance == null) {
			instance = new TreePaintListener();
		}
		return instance;
	}

	private TreePaintListener() {
		offlineImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/32/offline-red-mini.png"));
		onlineImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/32/online-mini.png"));
		agentImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/16/computer.png"));
		userImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/16/user.png"));
		presenceMap = new Hashtable<String, Boolean>();
	}

	public boolean put(String dn, Boolean presence) {
		return this.presenceMap.put(dn, presence);
	}

	public void redraw() {
		try {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					tree.redraw();
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void handleEvent(Event event) {

		switch (event.type) {
		case SWT.MeasureItem: {
			TreeItem item = (TreeItem) event.item;
			String text = item.getText();
			Point size = event.gc.textExtent(text);
			event.width = size.x + 24;
			event.height = Math.max(event.height, size.y);
			break;
		}
		case SWT.PaintItem: {
			TreeItem item = (TreeItem) event.item;
			String text = item.getText();

			Object data = item.getData();
			if (data instanceof SearchResult) {
				data = ((SearchResult) data).getEntry();
			}

			//
			// Draw agent/user icon
			//
			Image originalImage = item.getImage();
			if (originalImage != agentImage && originalImage != userImage && data instanceof IEntry) {
				Collection<ObjectClass> classes = ((IEntry) data).getObjectClassDescriptions();
				if (LdapUtils.getInstance().isAgent(classes)) {
					item.setImage(agentImage);
					originalImage = item.getImage();
				} else if (LdapUtils.getInstance().isUser(classes)) {
					item.setImage(userImage);
					originalImage = item.getImage();
				}
			}
			if (originalImage != null) {
				event.gc.drawImage(originalImage, event.x + 6, event.y - 1);
			}

			//
			// Draw online/offline icon
			//
			if (data instanceof IBookmark) {
				data = ((IBookmark) data).getEntry();
			}
			if (data instanceof IEntry) {
				IEntry entry = (IEntry) data;
				String dn = entry.getDn().getName();

				if (presenceMap.containsKey(dn)) {
					Image miniIcon;
					if (presenceMap.get(dn) && xmppConnected) {
						miniIcon = onlineImage;
					} else {
						miniIcon = offlineImage;
					}
					event.gc.drawImage(miniIcon, event.x, event.y + 8);
				}
			}

			event.gc.drawText(text, event.x + 24, event.y, true);
			break;
		}
		case SWT.EraseItem: {
			event.detail &= ~SWT.FOREGROUND;
			break;
		}
		default: {
			break;
		}
		}

	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public void setXmppConnected(boolean xmppConnected) {
		this.xmppConnected = xmppConnected;
		if (xmppConnected) {
			this.presenceMap.put(UserSettings.USER_DN, true);
		}
		this.redraw();
	}

}
