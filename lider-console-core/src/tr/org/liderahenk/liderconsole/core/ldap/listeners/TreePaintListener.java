package tr.org.liderahenk.liderconsole.core.ldap.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.directory.api.ldap.model.schema.ObjectClass;
import org.apache.directory.studio.ldapbrowser.core.model.IBookmark;
import org.apache.directory.studio.ldapbrowser.core.model.IEntry;
import org.apache.directory.studio.ldapbrowser.core.model.impl.SearchResult;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.EventHandler;

import tr.org.liderahenk.liderconsole.core.config.ConfigProvider;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.current.RestSettings;
import tr.org.liderahenk.liderconsole.core.current.UserSettings;

/**
 * This class is used to paint online/offline status images on LDAP tree while
 * listening to XMPP events.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class TreePaintListener implements Listener {

	private Boolean globalState = false;
	private final Image offlineImage;
	private final Image onlineImage;
	private final Image pardusAhenkImage;

	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);
	private final Map<String, Boolean> onlineInfo;

	public TreePaintListener(final Tree tree) {

		offlineImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/32/offline-red-mini.png"));
		onlineImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/32/online-mini.png"));
		pardusAhenkImage = new Image(Display.getDefault(),
				this.getClass().getClassLoader().getResourceAsStream("icons/32/pardusAhenk.png"));
		onlineInfo = new TreeMap<String, Boolean>();

		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.ROSTER_ONLINE, new EventHandler() {
			public void handleEvent(org.osgi.service.event.Event event) {
				String dn = (String) event.getProperty("org.eclipse.e4.data");
				if (dn != null && !dn.isEmpty()) {
					onlineInfo.put(dn, true);
					try {
						tree.redraw();
					} catch (Exception e) {
						// On system shutdown, tree object might already be
						// disposed!
					}
				}
			}
		});
		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.ROSTER_OFFLINE, new EventHandler() {
			public void handleEvent(org.osgi.service.event.Event event) {
				String dn = (String) event.getProperty("org.eclipse.e4.data");
				if (dn != null && !dn.isEmpty()) {
					onlineInfo.put(dn, false);
					try {
						tree.redraw();
					} catch (Exception e) {
						// On system shutdown, tree object might already be
						// disposed!
					}
				}
			}
		});
		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.XMPP_OFFLINE, new EventHandler() {
			public void handleEvent(org.osgi.service.event.Event event) {
				globalState = false;
				for (Entry<String, Boolean> k : onlineInfo.entrySet()) {
					k.setValue(false);
				}
			}
		});
		eventBroker.subscribe("xmpp_temporaryoffline", new EventHandler() {
			public void handleEvent(org.osgi.service.event.Event event) {
				globalState = false;
				onlineInfo.put(UserSettings.USER_DN, false);
				try {
					tree.redraw();
				} catch (Exception e) {
					// On system shutdown, tree object might already be
					// disposed!
				}
			}
		});
		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.XMPP_ONLINE, new EventHandler() {
			public void handleEvent(org.osgi.service.event.Event event) {
				globalState = true;
				onlineInfo.put(UserSettings.USER_DN, true);
				try {
					tree.redraw();
				} catch (Exception e) {
					// On system shutdown, tree object might already be
					// disposed!
				}
			}
		});
		eventBroker.subscribe("check_lider_status", new EventHandler() {
			public void handleEvent(org.osgi.service.event.Event event) {
				if (!RestSettings.isAvailable()) {
					globalState = false;
					onlineInfo.clear();
				}
			}
		});

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
			// Point size = event.gc.textExtent(text);

			Object data = item.getData();

			if (data instanceof SearchResult)
				data = ((SearchResult) data).getEntry();

			Image originalImage = item.getImage();

			if (originalImage != pardusAhenkImage && data instanceof IEntry) {
				Collection<ObjectClass> classes = ((IEntry) data).getObjectClassDescriptions();
				List<String> agentObjClsArr = new ArrayList<String>(
						ConfigProvider.getInstance().getStringList(LiderConstants.CONFIG.AGENT_LDAP_OBJ_CLS));

				// Remove common elements from the list
				for (Iterator<String> iterator = agentObjClsArr.iterator(); iterator.hasNext();) {
					String agentObjCls = iterator.next();
					for (ObjectClass c : classes) {
						String cName = c.getName();
						if (cName.equals(agentObjCls)) {
							iterator.remove();
							break;
						}
					}
				}
				// If the resulting list is empty, then specified entry belongs
				// to an agent
				if (agentObjClsArr.isEmpty()) {
					item.setImage(pardusAhenkImage);
					originalImage = item.getImage();
				}
			}
			if (originalImage != null)
				event.gc.drawImage(originalImage, event.x - 4, event.y + 4);

			if (data instanceof IBookmark)
				data = ((IBookmark) data).getEntry();

			if (data instanceof IEntry) {
				IEntry entry = (IEntry) data;
				String dn = entry.getDn().getName();

				if (onlineInfo.containsKey(dn)) {
					Image miniIcon;
					if (onlineInfo.get(dn) && globalState)
						miniIcon = onlineImage;
					else
						miniIcon = offlineImage;
					event.gc.drawImage(miniIcon, event.x + 8, event.y + 12);
				}
			}

			event.gc.drawText(text, event.x + 20, event.y, true);
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

}
