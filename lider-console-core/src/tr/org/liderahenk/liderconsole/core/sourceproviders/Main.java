package tr.org.liderahenk.liderconsole.core.sourceproviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.directory.api.ldap.model.schema.ObjectClass;
import org.apache.directory.studio.ldapbrowser.core.model.IBookmark;
import org.apache.directory.studio.ldapbrowser.core.model.IEntry;
import org.apache.directory.studio.ldapbrowser.core.model.ISearch;
import org.apache.directory.studio.ldapbrowser.core.model.ISearchResult;
import org.apache.directory.studio.ldapbrowser.core.model.impl.BaseDNEntry;
import org.apache.directory.studio.ldapbrowser.core.model.impl.SearchResult;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.current.RestSettings;
import tr.org.liderahenk.liderconsole.core.current.UserSettings;
import tr.org.liderahenk.liderconsole.core.ldap.LdapUtils;
import tr.org.liderahenk.liderconsole.core.listeners.LdapConnectionListener;
import tr.org.liderahenk.liderconsole.core.model.SubBrowserItem;

/**
 * LiderSourceProvider provides expressions that can be used to restrict the
 * availability and visibility of commands, handlers and UI contributions.
 * 
 * @author emre
 *
 */
public class Main extends AbstractSourceProvider {

	public final static String LIDER_AVAILABLE_STATE = "tr.org.liderahenk.liderconsole.core.sourceproviders.main.available";
	public final static String SELECTED_ENTRY_AVAILABLE_STATE = "tr.org.liderahenk.liderconsole.core.sourceproviders.main.isEntrySelected";
	public final static String SELECTED_SEARCH_AVAILABLE_STATE = "tr.org.liderahenk.liderconsole.core.sourceproviders.main.isSearchSelected";
	public final static String SELECTED_AHENK_AVAILABLE_STATE = "tr.org.liderahenk.liderconsole.core.sourceproviders.main.isAhenkSelected";
	public final static String SELECTED_LDAP_USER_AVAILABLE_STATE = "tr.org.liderahenk.liderconsole.core.sourceproviders.main.isLdapUserSelected";
	public final static String PRIVILEGES_FOR_SELECTED_ITEM = "tr.org.liderahenk.liderconsole.core.sourceproviders.main.privilegesForSelectedItem";
	public final static String SELECTED_ITEM = "tr.org.liderahenk.liderconsole.core.sourceproviders.main.selectedItem";
	public final static String SELECTED_USER = "tr.org.liderahenk.liderconsole.core.sourceproviders.main.selectedUser";
	public final static String IS_SELECTED_USER = "tr.org.liderahenk.liderconsole.core.sourceproviders.main.isUserSelected";

	private Boolean isSelectedEntryAVAILABLE = false;
	private Boolean isSelectedSearchAVAILABLE = false;
	private Boolean isSelectedAhenkAVAILABLE = false;
	private Boolean isSelectedLdapUserAVAILABLE = false;
	private Boolean isSelectedUserAVAILABLE = false;

	private final List<String> emptyPrivilegeList = new ArrayList<String>();
	private List<String> privilegesForSelectedItem = emptyPrivilegeList;

	private String selectedItem = "";
	private String selectedUser = "";

	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	private final INullSelectionListener browserSelectionListener = new INullSelectionListener() {
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {

			eventBroker.send("selected_entry_changed", selection);

			Boolean isEntry = false;
			Boolean isAhenk = false;
			Boolean isLdapUser = false;
			Boolean isUser = false;
			String _selectedUser = "";
			String _selectedItem = null;
			Boolean isSearch = false;
			Map<String, Object> changedItems = new HashMap<String, Object>();

			if (selection instanceof IStructuredSelection) {

				IStructuredSelection sselection = (IStructuredSelection) selection;
				Object selectedItem = sselection.getFirstElement();

				if (selectedItem instanceof SearchResult) {
					selectedItem = ((SearchResult) selectedItem).getEntry();
				}
				if (selectedItem instanceof IBookmark) {
					selectedItem = ((IBookmark) selectedItem).getEntry();
				}
				if (selectedItem instanceof IEntry) {
					IEntry entry = (IEntry) selectedItem;
					_selectedItem = entry.getDn().getName();

					// Calculate the privileges for selected entry (BaseDNEntrys
					// are also valid dn's for privileges)
					getSelf().privilegesForSelectedItem = UserSettings.getPrivilegesFor(_selectedItem);
					changedItems.put(PRIVILEGES_FOR_SELECTED_ITEM, getSelf().privilegesForSelectedItem);
					getSelf().fireSourceChanged(ISources.WORKBENCH, PRIVILEGES_FOR_SELECTED_ITEM,
							getSelf().privilegesForSelectedItem);

					if (!(selectedItem instanceof BaseDNEntry)) {
						isEntry = true;
						Collection<ObjectClass> classes = entry.getObjectClassDescriptions();
						for (ObjectClass c : classes) {
							String cname = c.getName();
							if (cname.equals(LiderConstants.LdapAttributes.PardusAhenkObjectClass)) {
								isAhenk = true;
							}
							if (cname.equals(LiderConstants.LdapAttributes.PardusUserObjectClass)) {
								isLdapUser = true;
							}
						}
					}
				} else if (selectedItem instanceof ISearch) {
					ISearch search = (ISearch) selectedItem;
					LdapUtils.getInstance().runISearch(search);
					ISearchResult[] srs = search.getSearchResults();

					List<String> privs = new ArrayList<String>();
					for (ISearchResult iSearchResult : srs) {
						Object selected = ((SearchResult) iSearchResult).getEntry();
						IEntry selectedEntry = (IEntry) selected;
						String DN = selectedEntry.getDn().getName();
						privs.addAll(UserSettings.getPrivilegesFor(DN));
					}

					getSelf().privilegesForSelectedItem = privs;
					_selectedItem = search.getName();
					isSearch = true;
				} else {
					_selectedItem = "";
					getSelf().privilegesForSelectedItem = getSelf().emptyPrivilegeList;
					getSelf().fireSourceChanged(ISources.WORKBENCH, PRIVILEGES_FOR_SELECTED_ITEM,
							getSelf().privilegesForSelectedItem);
					changedItems.put(PRIVILEGES_FOR_SELECTED_ITEM, getSelf().privilegesForSelectedItem);
				}
			}

			getSelf().isSelectedEntryAVAILABLE = isEntry;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_ENTRY_AVAILABLE_STATE,
					getSelf().isSelectedEntryAVAILABLE);
			changedItems.put(SELECTED_ENTRY_AVAILABLE_STATE, getSelf().isSelectedEntryAVAILABLE);

			getSelf().isSelectedSearchAVAILABLE = isSearch;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_SEARCH_AVAILABLE_STATE,
					getSelf().isSelectedSearchAVAILABLE);
			changedItems.put(SELECTED_SEARCH_AVAILABLE_STATE, getSelf().isSelectedSearchAVAILABLE);

			getSelf().isSelectedAhenkAVAILABLE = isAhenk;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_AHENK_AVAILABLE_STATE,
					getSelf().isSelectedAhenkAVAILABLE);
			changedItems.put(SELECTED_AHENK_AVAILABLE_STATE, getSelf().isSelectedAhenkAVAILABLE);

			getSelf().isSelectedLdapUserAVAILABLE = isLdapUser;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_LDAP_USER_AVAILABLE_STATE,
					getSelf().isSelectedLdapUserAVAILABLE);
			changedItems.put(SELECTED_LDAP_USER_AVAILABLE_STATE, getSelf().isSelectedLdapUserAVAILABLE);

			getSelf().selectedItem = _selectedItem;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_ITEM, getSelf().selectedItem);
			changedItems.put(SELECTED_ITEM, getSelf().selectedItem);

			getSelf().isSelectedUserAVAILABLE = isUser;
			getSelf().fireSourceChanged(ISources.WORKBENCH, IS_SELECTED_USER, getSelf().isSelectedUserAVAILABLE);
			changedItems.put(IS_SELECTED_USER, getSelf().isSelectedUserAVAILABLE);

			getSelf().selectedUser = _selectedUser;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_USER, getSelf().selectedUser);
			changedItems.put(SELECTED_USER, getSelf().selectedUser);

			if (!changedItems.isEmpty()) {
				getSelf().fireSourceChanged(ISources.WORKBENCH, changedItems);
			}

		}
	};

	private final INullSelectionListener userBrowserSelectionListener = new INullSelectionListener() {
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			eventBroker.send("selected_user_changed", selection);

			Boolean isUser = false;
			String _selectedUser = "";
			Map<String, Object> changedItems = new HashMap<String, Object>();

			if (selection instanceof IStructuredSelection) {
				IStructuredSelection sselection = (IStructuredSelection) selection;
				Object selectedItem = sselection.getFirstElement();
				if (selectedItem instanceof SubBrowserItem) {
					SubBrowserItem item = (SubBrowserItem) selectedItem;
					if (item.getType().equals("USER")) {
						_selectedUser = item.getId();
						isUser = true;
					} else {
						_selectedUser = "";
						isUser = false;
					}
				}
			}

			changedItems.put(PRIVILEGES_FOR_SELECTED_ITEM, getSelf().privilegesForSelectedItem);

			getSelf().isSelectedUserAVAILABLE = isUser;
			getSelf().fireSourceChanged(ISources.WORKBENCH, IS_SELECTED_USER, getSelf().isSelectedUserAVAILABLE);
			changedItems.put(IS_SELECTED_USER, getSelf().isSelectedUserAVAILABLE);

			getSelf().selectedUser = _selectedUser;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_USER, getSelf().selectedUser);
			changedItems.put(SELECTED_USER, getSelf().selectedUser);

			getSelf().fireSourceChanged(ISources.WORKBENCH, changedItems);
		}
	};

	/**
	 * This handler is used to set expressions provided by LiderSourceProvider
	 * after an LDAP search in the new perspective (MainPerspective)
	 */
	private final EventHandler checkEntry = new EventHandler() {
		public void handleEvent(Event event) {

			Object selectedItem = event.getProperty(IEventBroker.DATA);

			Boolean isEntry = false;
			Boolean isAhenk = false;
			Boolean isLdapUser = false;
			Boolean isUser = false;
			String _selectedUser = "";
			String _selectedItem = null;
			Map<String, Object> changedItems = new HashMap<String, Object>();

			if (selectedItem instanceof javax.naming.directory.SearchResult) {
				javax.naming.directory.SearchResult result = ((javax.naming.directory.SearchResult) selectedItem);
				_selectedItem = result.getName();

				// Calculate the privileges for selected entry (BaseDNEntrys
				// are also valid dn's for privileges)
				getSelf().privilegesForSelectedItem = UserSettings.getPrivilegesFor(_selectedItem);
				changedItems.put(PRIVILEGES_FOR_SELECTED_ITEM, getSelf().privilegesForSelectedItem);
				getSelf().fireSourceChanged(ISources.WORKBENCH, PRIVILEGES_FOR_SELECTED_ITEM,
						getSelf().privilegesForSelectedItem);

				isEntry = true;
				isAhenk = LdapUtils.getInstance().isAgent(_selectedItem, LdapConnectionListener.getConnection(),
						LdapConnectionListener.getMonitor());
				isLdapUser = LdapUtils.getInstance().isUser(_selectedItem, LdapConnectionListener.getConnection(),
						LdapConnectionListener.getMonitor());
			} else {
				_selectedItem = "";
				getSelf().privilegesForSelectedItem = getSelf().emptyPrivilegeList;
				getSelf().fireSourceChanged(ISources.WORKBENCH, PRIVILEGES_FOR_SELECTED_ITEM,
						getSelf().privilegesForSelectedItem);
				changedItems.put(PRIVILEGES_FOR_SELECTED_ITEM, getSelf().privilegesForSelectedItem);
			}

			getSelf().isSelectedEntryAVAILABLE = isEntry;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_ENTRY_AVAILABLE_STATE,
					getSelf().isSelectedEntryAVAILABLE);
			changedItems.put(SELECTED_ENTRY_AVAILABLE_STATE, getSelf().isSelectedEntryAVAILABLE);

			getSelf().isSelectedAhenkAVAILABLE = isAhenk;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_AHENK_AVAILABLE_STATE,
					getSelf().isSelectedAhenkAVAILABLE);
			changedItems.put(SELECTED_AHENK_AVAILABLE_STATE, getSelf().isSelectedAhenkAVAILABLE);

			getSelf().isSelectedLdapUserAVAILABLE = isLdapUser;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_LDAP_USER_AVAILABLE_STATE,
					getSelf().isSelectedLdapUserAVAILABLE);
			changedItems.put(SELECTED_LDAP_USER_AVAILABLE_STATE, getSelf().isSelectedLdapUserAVAILABLE);

			getSelf().selectedItem = _selectedItem;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_ITEM, getSelf().selectedItem);
			changedItems.put(SELECTED_ITEM, getSelf().selectedItem);

			// /
			getSelf().isSelectedUserAVAILABLE = isUser;
			getSelf().fireSourceChanged(ISources.WORKBENCH, IS_SELECTED_USER, getSelf().isSelectedUserAVAILABLE);
			changedItems.put(IS_SELECTED_USER, getSelf().isSelectedUserAVAILABLE);
			getSelf().selectedUser = _selectedUser;
			getSelf().fireSourceChanged(ISources.WORKBENCH, SELECTED_USER, getSelf().selectedUser);
			changedItems.put(SELECTED_USER, getSelf().selectedUser);
			// /

			if (!changedItems.isEmpty()) {
				getSelf().fireSourceChanged(ISources.WORKBENCH, changedItems);
			}
		}
	};

	private final EventHandler stateChange = new EventHandler() {
		public void handleEvent(Event event) {

			getSelf().fireSourceChanged(ISources.WORKBENCH, LIDER_AVAILABLE_STATE, RestSettings.isAvailable());

			if (RestSettings.isAvailable()) {

				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();

				if (windows != null && windows.length > 0) {
					IWorkbenchWindow window = windows[0];
					window.getSelectionService().addPostSelectionListener(
							"org.apache.directory.studio.ldapbrowser.ui.views.browser.BrowserView",
							browserSelectionListener);
					window.getSelectionService().addSelectionListener(
							"tr.org.liderahenk.liderconsole.core.views.SubBrowserView", userBrowserSelectionListener);
				}
			} else {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();

				if (windows != null && windows.length > 0) {
					IWorkbenchWindow window = windows[0];
					window.getSelectionService().removePostSelectionListener(browserSelectionListener);
					window.getSelectionService().removeSelectionListener(userBrowserSelectionListener);
				}
			}

		}
	};

	public Main() {
		super();
		eventBroker.subscribe("check_lider_status", stateChange);
		eventBroker.subscribe("ldap_entry_selected", checkEntry);
	}

	@Override
	public void dispose() {
		eventBroker.unsubscribe(stateChange);
		eventBroker.unsubscribe(checkEntry);
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { LIDER_AVAILABLE_STATE, SELECTED_ENTRY_AVAILABLE_STATE, SELECTED_SEARCH_AVAILABLE_STATE,
				SELECTED_AHENK_AVAILABLE_STATE, SELECTED_LDAP_USER_AVAILABLE_STATE, PRIVILEGES_FOR_SELECTED_ITEM,
				SELECTED_ITEM, IS_SELECTED_USER, SELECTED_USER };
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getCurrentState() {
		HashMap<String, Object> map = new HashMap<String, Object>(1);
		map.put(LIDER_AVAILABLE_STATE, RestSettings.isAvailable());
		map.put(SELECTED_ENTRY_AVAILABLE_STATE, isSelectedEntryAVAILABLE);
		map.put(SELECTED_SEARCH_AVAILABLE_STATE, isSelectedSearchAVAILABLE);
		map.put(SELECTED_AHENK_AVAILABLE_STATE, isSelectedAhenkAVAILABLE);
		map.put(SELECTED_LDAP_USER_AVAILABLE_STATE, isSelectedLdapUserAVAILABLE);
		map.put(PRIVILEGES_FOR_SELECTED_ITEM, privilegesForSelectedItem);
		map.put(SELECTED_ITEM, selectedItem);
		map.put(IS_SELECTED_USER, isSelectedUserAVAILABLE);
		map.put(SELECTED_USER, selectedUser);
		return map;
	}

	private Main getSelf() {
		return this;
	}

}
