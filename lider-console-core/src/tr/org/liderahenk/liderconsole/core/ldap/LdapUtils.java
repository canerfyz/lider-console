package tr.org.liderahenk.liderconsole.core.ldap;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.directory.studio.common.core.jobs.StudioProgressMonitor;
import org.apache.directory.studio.connection.core.Connection;
import org.apache.directory.studio.connection.core.Connection.AliasDereferencingMethod;
import org.apache.directory.studio.connection.core.Connection.ReferralHandlingMethod;
import org.apache.directory.studio.connection.core.io.StudioNamingEnumeration;
import org.apache.directory.studio.ldapbrowser.core.jobs.SearchRunnable;
import org.apache.directory.studio.ldapbrowser.core.jobs.StudioBrowserJob;
import org.apache.directory.studio.ldapbrowser.core.model.IContinuation;
import org.apache.directory.studio.ldapbrowser.core.model.IContinuation.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.directory.studio.ldapbrowser.core.model.ISearch;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.current.UserSettings;
import tr.org.liderahenk.liderconsole.core.listeners.LdapConnectionListener;

public class LdapUtils {

	private static final Logger logger = LoggerFactory.getLogger(LdapUtils.class);

	/**
	 * 
	 */
	private static LdapUtils instance = null;

	public static synchronized LdapUtils getInstance() {
		if (instance == null) {
			instance = new LdapUtils();
		}
		return instance;
	}

	private LdapUtils() {
	}

	public static final String OBJECT_CLASS_FILTER = "(objectClass=*)";
	private static final String OBJECT_CLASS = "objectClass";

	/**
	 * Main search method for LDAP connections.
	 * 
	 * To gain more performance;
	 * 
	 * keep returning attributes to a minimum number of attributes, set search
	 * scope as ONELEVEL_SCOPE or OBJECT_SCOPE if possible and try to reduce
	 * count limit via paging results.
	 * 
	 * @param baseDn
	 *            the search base. If it is null, then base DN of the DIT will
	 *            be used.
	 * @param filter
	 *            the filter.
	 * @param returningAttributes
	 *            Specifies the attributes that will be returned as part of the
	 *            search. null indicates that all attributes will be returned.
	 *            An empty array indicates no attributes are returned.
	 * @param searchScope
	 *            Sets the search scope to one of: OBJECT_SCOPE, ONELEVEL_SCOPE,
	 *            SUBTREE_SCOPE.
	 * @param countLimit
	 *            Sets the maximum number of entries to be returned as a result
	 *            of the search. 0 indicates no limit: all entries will be
	 *            returned.
	 * @param conn
	 *            LDAP connection.
	 * @return the naming enumeration or null if an exception occurs.
	 */
	public StudioNamingEnumeration search(String baseDn, String filter, String[] returningAttributes, int searchScope,
			long countLimit, Connection conn, StudioProgressMonitor monitor) {

		// TODO handle pagedSearch
		if (conn != null) {
			logger.debug("Searching for attributes: {0} on DN: {1} using filter: {2}",
					new Object[] { returningAttributes.toString(), baseDn, filter });

			SearchControls searchControls = new SearchControls();
			searchControls.setCountLimit(countLimit);
			searchControls.setReturningAttributes(returningAttributes);
			searchControls.setSearchScope(searchScope);

			StudioNamingEnumeration enumeration = conn.getConnectionWrapper().search(
					baseDn == null ? findBaseDn(conn) : baseDn, filter, searchControls, AliasDereferencingMethod.NEVER,
					ReferralHandlingMethod.IGNORE, null, monitor, null);

			return enumeration;
		}

		return null;
	}

	/**
	 * 
	 * @param baseDn
	 * @param filter
	 * @param returningAttributes
	 * @param searchScope
	 * @param countLimit
	 * @param conn
	 * @return
	 */
	public List<SearchResult> searchAndReturnList(String baseDn, String filter, String[] returningAttributes,
			int searchScope, long countLimit, Connection conn, StudioProgressMonitor monitor) {
		StudioNamingEnumeration enumeration = search(baseDn, filter, returningAttributes, searchScope, countLimit, conn,
				monitor);
		return enumeration == null ? null : convertToList(enumeration);
	}

	/**
	 * Use this method to convert StudioNamingEnumeration to
	 * List&lt;SearchResult&gt;.
	 * 
	 * DO NOT use Collections.asList() method as it will cause an exception by
	 * using hasMoreElements() instead of hasMore().
	 * 
	 * @param enumeration
	 * @return
	 */
	private List<SearchResult> convertToList(StudioNamingEnumeration enumeration) {
		try {
			if (enumeration != null) {
				List<SearchResult> list = new ArrayList<SearchResult>();
				while (enumeration.hasMore()) {
					SearchResult item = enumeration.next();
					list.add(item);
				}
				if (!list.isEmpty()) {
					return list;
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns base DN of the specified LDAP connection.
	 * 
	 * @param conn
	 * @return
	 */
	public String findBaseDn(Connection conn) {
		return conn.getConnectionParameter().getExtendedProperty("ldapbrowser.baseDn");
	}

	private static Map<String, String> uidMap = null;

	public synchronized Map<String, String> getUidMap(Connection conn, StudioProgressMonitor monitor) {
		if (uidMap == null) {
			uidMap = buildUidMap(conn, monitor);
		}
		return uidMap;
	}

	/**
	 * 
	 * @return map of uid and dn
	 * 
	 *         key: uid value: dn
	 */
	private Map<String, String> buildUidMap(Connection conn, StudioProgressMonitor monitor) {

		TreeMap<String, String> retVal = new TreeMap<String, String>();

		String filter = "(|(objectClass=" + LiderConstants.LdapAttributes.PardusAhenkObjectClass + ")(objectClass="
				+ LiderConstants.LdapAttributes.PardusUserObjectClass + "))";
		StudioNamingEnumeration enumeration = search(null, filter,
				new String[] { LiderConstants.LdapAttributes.UserIdentityAttribute }, SearchControls.SUBTREE_SCOPE, 0,
				conn, monitor);

		try {
			while (enumeration.hasMore()) {
				SearchResult item = enumeration.next();
				Attribute attr = item.getAttributes().get(LiderConstants.LdapAttributes.UserIdentityAttribute);
				if (attr != null) {
					Object val = attr.get();
					// store as <UID, DN> pairs
					retVal.put(((String) val).toLowerCase(Locale.ENGLISH), item.getName());
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	/**
	 * Tries to find DN of the first found LDAP entry searching by specified
	 * attribute name-value pair.
	 * 
	 * @param baseDn
	 * @param attrName
	 * @param attrValue
	 * @param conn
	 * @return
	 */
	public String findDnByAttribute(String baseDn, String attrName, String attrValue, Connection conn,
			StudioProgressMonitor monitor) {
		StudioNamingEnumeration enumeration = search(baseDn, createFilter(attrName, attrValue), new String[] {},
				SearchControls.SUBTREE_SCOPE, 1, conn, monitor);
		String dn = null;
		try {
			if (enumeration != null) {
				while (enumeration.hasMore()) {
					SearchResult item = enumeration.next();
					dn = item.getName();
					break;
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return dn;
	}

	/**
	 * 
	 * @param attrName
	 * @param attrValue
	 * @param conn
	 * @return
	 */
	public String findDnByAttribute(String attrName, String attrValue, Connection conn, StudioProgressMonitor monitor) {
		return findDnByAttribute(null, attrName, attrValue, conn, monitor);
	}

	private String createFilter(String attrName, String attrValue) {
		StringBuilder filterExpr = new StringBuilder();
		filterExpr.append("(").append(attrName).append("=").append(attrValue).append(")");
		return filterExpr.toString();
	}

	/**
	 * 
	 * @param uid
	 * @param conn
	 * @return
	 */
	public String findDnByUid(String uid, Connection conn, StudioProgressMonitor monitor) {
		return findDnByAttribute(LiderConstants.LdapAttributes.UserIdentityAttribute, uid, conn, monitor);
	}

	/**
	 * Tries to find attribute of the provided DN.
	 * 
	 * @param dn
	 * @param attrName
	 * @param conn
	 * @return
	 */
	public Attribute findAttributeByDn(String dn, String attrName, Connection conn, StudioProgressMonitor monitor) {
		StudioNamingEnumeration enumeration = this.search(dn, OBJECT_CLASS_FILTER, new String[] { attrName },
				SearchControls.OBJECT_SCOPE, 1, conn, monitor);
		Attribute attr = null;
		try {
			if (enumeration != null) {
				while (enumeration.hasMore()) {
					SearchResult item = enumeration.next();
					if (item.getAttributes() != null && item.getAttributes().get(attrName) != null) {
						return item.getAttributes().get(attrName);
					}
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return attr;
	}

	/**
	 * Tries to find value of the specified attribute.<br/>
	 * This method finds only the first value of the attribute, some attributes
	 * might have multiple values. If that is the case, use
	 * findAttributeValuesByDn() method.
	 * 
	 * @param dn
	 * @param attrName
	 * @param conn
	 * @return
	 */
	public String findAttributeValueByDn(String dn, String attrName, Connection conn, StudioProgressMonitor monitor) {
		Attribute attribute = this.findAttributeByDn(dn, attrName, conn, monitor);
		return findAttributeValue(attribute);
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public String findAttributeValue(Attribute attribute) {
		String attrValue = null;
		if (attribute != null) {
			Object val;
			try {
				val = attribute.get();
				if (val instanceof byte[]) {
					attrValue = new String((byte[]) val, StandardCharsets.UTF_8);
				} else {
					attrValue = val.toString();
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return attrValue;
	}

	/**
	 * 
	 * @param dn
	 * @param attrName
	 * @param conn
	 * @return
	 */
	public List<String> findAttributeValuesByDn(String dn, String attrName, Connection conn,
			StudioProgressMonitor monitor) {
		Attribute attribute = this.findAttributeByDn(dn, attrName, conn, monitor);
		return findAttributeValues(attribute);
	}

	public List<String> findAttributeValues(Attribute attribute) {
		List<String> attrValues = new ArrayList<String>();
		if (attribute != null) {
			try {
				for (int i = 0; i < attribute.size(); i++) {
					Object obj = attribute.get(i);
					if (obj instanceof byte[]) {
						attrValues.add(new String((byte[]) obj, StandardCharsets.UTF_8));
					} else {
						attrValues.add(obj.toString());
					}
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return attrValues;
	}

	/**
	 * 
	 * @param dn
	 * @return true if provided DN is an Ahenk entry. if an entry has
	 *         objectClass attribute with 'pardusAhenk' value, it is an Ahenk
	 *         entry.
	 */
	public boolean isAgent(String dn, Connection conn, StudioProgressMonitor monitor) {
		Attribute attribute = findAttributeByDn(dn, OBJECT_CLASS, conn, monitor);
		return attributeHasValue(attribute, LiderConstants.LdapAttributes.PardusAhenkObjectClass);
	}
	
	public boolean isAgent(String dn) {
		return isAgent(dn, LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
	}

	/**
	 * 
	 * @param dn
	 * @return true if provided dn is user node. if a node has objectClass
	 *         attribute with pardus user value, it is an user node.
	 */
	public boolean isUser(String dn, Connection conn, StudioProgressMonitor monitor) {
		Attribute attribute = findAttributeByDn(dn, OBJECT_CLASS, conn, monitor);
		return attributeHasValue(attribute, LiderConstants.LdapAttributes.PardusUserObjectClass);
	}
	
	public boolean isUser(String dn) {
		return isUser(dn, LdapConnectionListener.getConnection(), LdapConnectionListener.getMonitor());
	}

	/**
	 * Tries to find user DNs under the provided DN.
	 * 
	 * @param dn
	 * @return list of user DNs
	 */
	public List<String> findUsers(String dn, Connection conn, StudioProgressMonitor monitor) {

		String filter = "(objectClass=" + LiderConstants.LdapAttributes.PardusUserObjectClass + ")";
		List<String> dnList = new ArrayList<String>();

		StudioNamingEnumeration enumeration = search(dn, filter, new String[] { OBJECT_CLASS },
				SearchControls.SUBTREE_SCOPE, 0, conn, monitor);
		if (enumeration != null) {
			try {
				while (enumeration.hasMore()) {
					SearchResult item = enumeration.next();
					dnList.add(item.getName());
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		return dnList;
	}

	/**
	 * Tries to find agent DNs under the provided DN
	 * 
	 * @param dn
	 * @return list of agent DNs
	 */
	public List<String> findAgents(String dn, Connection conn, StudioProgressMonitor monitor) {

		String filter = "(objectClass=" + LiderConstants.LdapAttributes.PardusAhenkObjectClass + ")";
		List<String> dnList = new ArrayList<String>();

		StudioNamingEnumeration enumeration = search(dn, filter, new String[] { OBJECT_CLASS },
				SearchControls.SUBTREE_SCOPE, 0, conn, monitor);
		if (enumeration != null) {
			try {
				while (enumeration.hasMore()) {
					SearchResult item = enumeration.next();
					dnList.add(item.getName());
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		return dnList;
	}

	/**
	 * 
	 * @param searchinfo
	 *            To run search and fill searchResults.
	 */
	public void runISearch(ISearch searchinfo) {
		if (searchinfo.getSearchResults() == null) {
			if (searchinfo instanceof IContinuation) {
				IContinuation continuation = (IContinuation) searchinfo;
				if (continuation.getState() != State.RESOLVED) {
					continuation.resolve();
				}
			}
			new StudioBrowserJob(new SearchRunnable(new ISearch[] { searchinfo })).execute();
		}
	}

	/**
	 * 
	 * @param attr
	 * @param value
	 * @return
	 */
	public boolean attributeHasValue(Attribute attr, String value) {
		List<String> retVal = new ArrayList<String>();
		for (int i = 0; i < attr.size(); i++) {
			try {
				Object obj = attr.get(i);
				if (obj instanceof byte[]) {
					retVal.add(new String((byte[]) obj, StandardCharsets.UTF_8));
				} else {
					retVal.add(obj.toString());
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return retVal.contains(value);
	}

	/**
	 * 
	 * @param privileges
	 * @param liderPrivilege
	 */
	private static void calculatePrivilege(Map<String, Map<String, Boolean>> privileges, final String liderPrivilege) {
		String[] splitPirivilegeInfo = liderPrivilege.split(":");
		String pdn = splitPirivilegeInfo[0].substring(1);
		Map<String, Boolean> dnprivileges;
		if (privileges.containsKey(pdn)) {
			dnprivileges = privileges.get(pdn);
		} else {
			dnprivileges = new HashMap<String, Boolean>();
			privileges.put(pdn, dnprivileges);
		}
		String[] keys = splitPirivilegeInfo[1].split(",");
		for (String key : keys) {
			dnprivileges.put(key,
					splitPirivilegeInfo[2].substring(0, splitPirivilegeInfo[2].length() - 1).equals("true"));
		}
	}

	/**
	 * 
	 * @param privileges
	 * @param dn
	 * @param conn
	 * @param monitor
	 */
	private void calculatePrivilegesFromGroups(Map<String, Map<String, Boolean>> privileges, String dn, Connection conn,
			StudioProgressMonitor monitor) {
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(new String[] { LiderConstants.LdapAttributes.liderPrivilegeAttribute });
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		StudioNamingEnumeration sr = conn.getConnectionWrapper().search(findBaseDn(conn),
				"(&(objectClass=pardusLider)(member=" + dn + "))", searchControls, AliasDereferencingMethod.NEVER,
				ReferralHandlingMethod.IGNORE, null, monitor, null);
		try {
			while (sr.hasMore()) {
				SearchResult item = sr.next();
				Attributes attrs = item.getAttributes();
				Attribute attr = attrs.get(LiderConstants.LdapAttributes.liderPrivilegeAttribute);
				if (attr != null) {
					for (int i = 0; i < attr.size(); i++) {
						Object val = attr.get(i);
						calculatePrivilege(privileges, (String) val);
					}
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param conn
	 * @param monitor
	 * @return
	 */
	public Map<String, Map<String, Boolean>> findPrivileges(Connection conn, StudioProgressMonitor monitor) {

		Map<String, Map<String, Boolean>> retVal = new HashMap<String, Map<String, Boolean>>();
		SearchControls searchControls = new SearchControls();
		searchControls.setCountLimit(1);
		searchControls.setReturningAttributes(new String[] { LiderConstants.LdapAttributes.liderPrivilegeAttribute });
		searchControls.setSearchScope(SearchControls.OBJECT_SCOPE);

		calculatePrivilegesFromGroups(retVal, UserSettings.USER_DN, conn, monitor);

		StudioNamingEnumeration sr = conn.getConnectionWrapper().search(UserSettings.USER_DN, OBJECT_CLASS_FILTER,
				searchControls, AliasDereferencingMethod.NEVER, ReferralHandlingMethod.IGNORE, null, monitor, null);
		try {
			while (sr.hasMore()) {
				SearchResult item = sr.next();
				Attributes attrs = item.getAttributes();
				Attribute attr = attrs.get(LiderConstants.LdapAttributes.liderPrivilegeAttribute);
				if (attr != null) {
					for (int i = 0; i < attr.size(); i++) {
						Object val = attr.get(i);
						calculatePrivilege(retVal, (String) val);
					}
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	/**
	 * Removes provided attributes from the provided DN.
	 * 
	 * @param dn
	 * @param attrs
	 */
	public void removeAttribute(String dn, Map<String, Object> attrs, Connection conn, StudioProgressMonitor monitor) {
		int counter = 0;
		ModificationItem[] mods = new ModificationItem[attrs.size()];
		for (Entry<String, Object> attr : attrs.entrySet()) {
			mods[counter] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
					new BasicAttribute(attr.getKey(), attr.getValue()));
			++counter;
		}
		conn.getConnectionWrapper().modifyEntry(dn, mods, null, monitor, null);
	}

	/**
	 * Modifies attributes with values for the provided DN.
	 * 
	 * @param dn
	 * @param attrs
	 */
	public void modifyAttribute(String dn, Map<String, Object> attrs, Connection conn, StudioProgressMonitor monitor) {
		int counter = 0;
		ModificationItem[] mods = new ModificationItem[attrs.size()];
		for (Entry<String, Object> attr : attrs.entrySet()) {
			mods[counter] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute(attr.getKey(), attr.getValue()));
			++counter;
		}
		conn.getConnectionWrapper().modifyEntry(dn, mods, null, monitor, null);
	}

}
