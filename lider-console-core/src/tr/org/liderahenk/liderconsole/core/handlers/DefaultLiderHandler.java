package tr.org.liderahenk.liderconsole.core.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.directory.studio.ldapbrowser.core.model.IBookmark;
import org.apache.directory.studio.ldapbrowser.core.model.IEntry;
import org.apache.directory.studio.ldapbrowser.core.model.ISearch;
import org.apache.directory.studio.ldapbrowser.core.model.impl.SearchResult;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.ldap.LdapUtils;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;

/**
 * Provides convenience method for finding all agents, users and groups for
 * given LDAP selection. Other handler implementations may extend this class.
 * 
 * @author <a href="mailto:caner.feyzullahoglu@agem.com.tr">Caner
 *         Feyzullahoğlu</a>
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public abstract class DefaultLiderHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Set<String> dnSet = null;

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IWorkbenchPage page = window.getActivePage();
		ISelection selection = page.getSelection();
		if (selection == null) {
			// Under certain circumstances, selection may be null (This might
			// be an eclipse bug?) In that case, this line below can also
			// provide the selection.
			selection = HandlerUtil.getCurrentSelection(event);
		}

		if (selection instanceof IStructuredSelection) {

			// Iterate over all selected items
			IStructuredSelection sselection = (IStructuredSelection) selection;
			Object[] selectedElements = sselection.toArray();

			// For each selected element, add their child DNs to a list
			// and send the list to dialog.
			if (selectedElements != null && selectedElements.length > 0) {
				dnSet = new HashSet<String>();
				for (Object selectedItem : selectedElements) {
					if (selectedItem instanceof SearchResult) {
						addChildDNs(((SearchResult) selectedItem).getDn().toString(), dnSet);
					} else if (selectedItem instanceof IBookmark) {
						addChildDNs(((IBookmark) selectedItem).getDn().toString(), dnSet);
					} else if (selectedItem instanceof javax.naming.directory.SearchResult) {
						addChildDNs(((javax.naming.directory.SearchResult) selectedItem).getName(), dnSet);
					} else if (selectedItem instanceof IEntry) {
						addChildDNs(((IEntry) selectedItem).getDn().toString(), dnSet);
					} else if (selectedItem instanceof ISearch) {
						addChildDNs(((ISearch) selectedItem).getName(), dnSet);
					}
				}
			}

		}

		if (dnSet == null || dnSet.isEmpty()) {
			Notifier.error(null, Messages.getString("ERROR_ON_LDAP_SELECTION"));
		} else {
			executeWithDNSet(dnSet);
		}

		return null;
	}

	/**
	 * This method searches for all agents, users and groups under a given DN
	 * and adds their DN to the given DN set.
	 * 
	 * @param dn
	 * @param dnSet
	 */
	private void addChildDNs(String dn, Set<String> dnSet) {

		// TODO We can improve this method by simply generating a complex filter
		// expression that helps us find agents,users,groups at the same time.

		List<String> resultList = LdapUtils.getInstance().findAgents(dn);
		if (resultList != null && !resultList.isEmpty()) {
			dnSet.addAll(resultList);
		}

		resultList.clear();
		resultList = LdapUtils.getInstance().findUsers(dn);

		if (resultList != null && !resultList.isEmpty()) {
			dnSet.addAll(resultList);
		}

		resultList.clear();
		resultList = LdapUtils.getInstance().findGroups(dn);

		if (resultList != null && !resultList.isEmpty()) {
			dnSet.addAll(resultList);
		}
	}

	/**
	 * Extending class should override this method to execute event.
	 * 
	 * @param dnSet
	 *            a set of selected LDAP DNs. This set may contain user, agent
	 *            or groupOfNames DNs.
	 */
	public abstract void executeWithDNSet(Set<String> dnSet);

}
