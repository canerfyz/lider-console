package tr.org.liderahenk.liderconsole.core.sourceproviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import tr.org.liderahenk.liderconsole.core.ldap.enums.DNType;
import tr.org.liderahenk.liderconsole.core.ldap.utils.LdapUtils;
import tr.org.liderahenk.liderconsole.core.model.SearchGroup;
import tr.org.liderahenk.liderconsole.core.model.SearchGroupEntry;

/**
 * LiderSourceProvider provides expressions that can be used to restrict the
 * availability and visibility of commands, handlers and UI contributions. See
 * <code>plugin.xml</code> for examples.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 * @see tr.org.liderahenk.liderconsole.core.constants.LiderConstants
 *
 */
public class LiderSourceProvider extends AbstractSourceProvider {

	private Boolean singleEntrySelected = false;
	private Boolean multipleEntriesSelected = false;
	private Boolean searchSelected = false;
	private Boolean agentEntrySelected = false;
	private Boolean userEntrySelected = false;
	private List<String> privilegesForSelectedItem = new ArrayList<String>();

	/**
	 * System-wide event broker
	 */
	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	public LiderSourceProvider() {
		eventBroker.subscribe(LiderConstants.EVENT_TOPICS.CHECK_LIDER_STATUS, statusHandler);
	}

	@Override
	public void dispose() {
		eventBroker.unsubscribe(statusHandler);
	}

	@Override
	public String[] getProvidedSourceNames() {
		return LiderConstants.EXPRESSIONS.getExpressions();
	}

	@Override
	public Map<String, Object> getCurrentState() {
		HashMap<String, Object> map = new HashMap<String, Object>(1);
		map.put(LiderConstants.EXPRESSIONS.LIDER_AVAILABLE_STATE, RestSettings.isAvailable());
		map.put(LiderConstants.EXPRESSIONS.SEARCH_SELECTED, searchSelected);
		map.put(LiderConstants.EXPRESSIONS.MULTIPLE_ENTRIES_SELECTED, multipleEntriesSelected);
		map.put(LiderConstants.EXPRESSIONS.SINGLE_ENTRY_SELECTED, singleEntrySelected);
		map.put(LiderConstants.EXPRESSIONS.PRIVILEGES_FOR_SELECTED_ITEM, privilegesForSelectedItem);
		// These two expressions are only meaningful, if single entry selected
		// OR all of the entries belong to same DN type (agent or user).
		map.put(LiderConstants.EXPRESSIONS.AGENT_SELECTED, agentEntrySelected);
		map.put(LiderConstants.EXPRESSIONS.USER_SELECTED, userEntrySelected);
		return map;
	}

	/**
	 * Listener implementation which listens to LDAP entry selections and
	 * updates expression values accordingly.
	 */
	private final INullSelectionListener selectionListener = new INullSelectionListener() {
		@SuppressWarnings("rawtypes")
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {

			Map<String, Object> changedItems = new HashMap<String, Object>();
			// Re-initialize expression values
			singleEntrySelected = false;
			multipleEntriesSelected = false;
			searchSelected = false;
			agentEntrySelected = false;
			userEntrySelected = false;
			privilegesForSelectedItem = new ArrayList<String>();

			// LDAP browser OR search group selection
			if (selection instanceof IStructuredSelection) {

				boolean isFirst = true;
				boolean prevUserEntrySelected = false;
				boolean prevAgentEntrySelected = false;

				// Iterate over all selected entries
				IStructuredSelection sselection = (IStructuredSelection) selection;
				Iterator iterator = sselection.iterator();
				while (iterator.hasNext()) {

					Object selectedItem = iterator.next();
					if (isFirst) {
						// Single entry or multiple entries?
						singleEntrySelected = !iterator.hasNext();
						multipleEntriesSelected = iterator.hasNext();
					}

					if (selectedItem instanceof SearchResult) {
						selectedItem = ((SearchResult) selectedItem).getEntry();
					}
					if (selectedItem instanceof IBookmark) {
						selectedItem = ((IBookmark) selectedItem).getEntry();
					}
					if (selectedItem instanceof IEntry) {
						IEntry entry = (IEntry) selectedItem;

						// Calculate the privileges for the entry
						privilegesForSelectedItem.addAll(UserSettings.getPrivilegesFor(entry.getDn().getName()));

						// User or agent entry?
						// (Set their value only if single entry selected OR all
						// of the entries belong to same DN type)
						if (!(selectedItem instanceof BaseDNEntry)) {
							Collection<ObjectClass> classes = entry.getObjectClassDescriptions();
							prevUserEntrySelected = (prevUserEntrySelected || isFirst)
									& LdapUtils.getInstance().isUser(classes);
							prevAgentEntrySelected = (prevAgentEntrySelected || isFirst)
									& LdapUtils.getInstance().isAgent(classes);
						}
					} else if (selectedItem instanceof ISearch) {
						ISearch search = (ISearch) selectedItem;

						// Read search results
						LdapUtils.getInstance().runISearch(search);
						ISearchResult[] srs = search.getSearchResults();

						// Calculate privileges for all searched entries
						for (ISearchResult iSearchResult : srs) {
							Object selected = ((SearchResult) iSearchResult).getEntry();
							IEntry selectedEntry = (IEntry) selected;
							privilegesForSelectedItem
									.addAll(UserSettings.getPrivilegesFor(selectedEntry.getDn().getName()));
						}

						searchSelected = true;
					} else if (selectedItem instanceof SearchGroupEntry) {
						SearchGroupEntry entry = (SearchGroupEntry) selectedItem;

						// Calculate the privileges for the entry
						privilegesForSelectedItem.addAll(UserSettings.getPrivilegesFor(entry.getDn()));

						// User or agent entry?
						// (Set their value only if single entry selected OR all
						// of the entries belong to same DN type)
						prevUserEntrySelected = (prevUserEntrySelected || isFirst) & (entry.getDnType() == DNType.USER);
						prevAgentEntrySelected = (prevAgentEntrySelected || isFirst)
								& (entry.getDnType() == DNType.AHENK);
					} else if (selectedItem instanceof SearchGroup) {
						SearchGroup searchGroup = (SearchGroup) selectedItem;

						// Calculate privileges for all entries
						for (SearchGroupEntry entry : searchGroup.getEntries()) {
							privilegesForSelectedItem.addAll(UserSettings.getPrivilegesFor(entry.getDn()));
						}
					}

					isFirst = false;
				}

				agentEntrySelected = prevAgentEntrySelected;
				userEntrySelected = prevUserEntrySelected;
			}

			// Single entry selected
			fireSourceChanged(ISources.WORKBENCH, LiderConstants.EXPRESSIONS.SINGLE_ENTRY_SELECTED,
					singleEntrySelected);
			changedItems.put(LiderConstants.EXPRESSIONS.SINGLE_ENTRY_SELECTED, singleEntrySelected);

			// Multiple entries selected
			fireSourceChanged(ISources.WORKBENCH, LiderConstants.EXPRESSIONS.MULTIPLE_ENTRIES_SELECTED,
					multipleEntriesSelected);
			changedItems.put(LiderConstants.EXPRESSIONS.MULTIPLE_ENTRIES_SELECTED, multipleEntriesSelected);

			// Search selected
			fireSourceChanged(ISources.WORKBENCH, LiderConstants.EXPRESSIONS.SEARCH_SELECTED, searchSelected);
			changedItems.put(LiderConstants.EXPRESSIONS.SEARCH_SELECTED, searchSelected);

			// Agent selected
			fireSourceChanged(ISources.WORKBENCH, LiderConstants.EXPRESSIONS.AGENT_SELECTED, agentEntrySelected);
			changedItems.put(LiderConstants.EXPRESSIONS.AGENT_SELECTED, agentEntrySelected);

			// User selected
			fireSourceChanged(ISources.WORKBENCH, LiderConstants.EXPRESSIONS.USER_SELECTED, userEntrySelected);
			changedItems.put(LiderConstants.EXPRESSIONS.USER_SELECTED, userEntrySelected);

			// Privileges for selected item
			fireSourceChanged(ISources.WORKBENCH, LiderConstants.EXPRESSIONS.PRIVILEGES_FOR_SELECTED_ITEM,
					privilegesForSelectedItem);
			changedItems.put(LiderConstants.EXPRESSIONS.PRIVILEGES_FOR_SELECTED_ITEM, privilegesForSelectedItem);

			fireSourceChanged(ISources.WORKBENCH, changedItems);
		}
	};

	/**
	 * Event handler implementation which listens to LDAP connection status (and
	 * thus Lider availability).
	 */
	private final EventHandler statusHandler = new EventHandler() {
		public void handleEvent(Event event) {
			// Update source expression value
			fireSourceChanged(ISources.WORKBENCH, LiderConstants.EXPRESSIONS.LIDER_AVAILABLE_STATE,
					RestSettings.isAvailable());
			// Hook listener (for LDAP browser & search groups)
			if (RestSettings.isAvailable()) {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
				if (windows != null && windows.length > 0) {
					IWorkbenchWindow window = windows[0];
					window.getSelectionService().addPostSelectionListener(LiderConstants.VIEWS.BROWSER_VIEW,
							selectionListener);
					window.getSelectionService().addPostSelectionListener(LiderConstants.VIEWS.SEARCH_GROUP_VIEW,
							selectionListener);
				}
			} else {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
				if (windows != null && windows.length > 0) {
					IWorkbenchWindow window = windows[0];
					window.getSelectionService().removePostSelectionListener(selectionListener);
				}
			}
		}
	};

}
