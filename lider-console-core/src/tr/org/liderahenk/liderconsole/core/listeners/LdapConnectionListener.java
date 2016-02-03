package tr.org.liderahenk.liderconsole.core.listeners;

import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.directory.studio.common.core.jobs.StudioProgressMonitor;
import org.apache.directory.studio.connection.core.Connection;
import org.apache.directory.studio.connection.core.ConnectionParameter.AuthenticationMethod;
import org.apache.directory.studio.connection.core.IConnectionListener;
import org.apache.directory.studio.connection.core.io.StudioNamingEnumeration;
import org.apache.directory.studio.connection.core.jobs.CloseConnectionsRunnable;
import org.apache.directory.studio.connection.core.jobs.StudioConnectionJob;
import org.apache.directory.studio.ldapbrowser.ui.views.browser.BrowserView;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.current.RestSettings;
import tr.org.liderahenk.liderconsole.core.current.UserSettings;
import tr.org.liderahenk.liderconsole.core.editors.LdapSearchEditor;
import tr.org.liderahenk.liderconsole.core.ldap.LdapUtils;
import tr.org.liderahenk.liderconsole.core.rest.RestClient;
import tr.org.liderahenk.liderconsole.core.ui.GenericEditorInput;
import tr.org.liderahenk.liderconsole.core.widgets.notifier.Notifier;
import tr.org.liderahenk.liderconsole.core.xmpp.XMPPClient;

public class LdapConnectionListener implements IConnectionListener {

	private final IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	private static Connection conn;
	private static StudioProgressMonitor monitor;

	public LdapConnectionListener() {

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		if (windows != null && windows.length > 0) {

			IWorkbenchWindow window = windows[0];

			// This code block is used to paint initially 'offline' icons on
			// LDAP user and agent entries.
			BrowserView browserView = (BrowserView) window.getActivePage().findView(LiderConstants.Views.BROWSER_VIEW);
			if (browserView != null) {

				final Tree tree = browserView.getMainWidget().getViewer().getTree();
				final TreePaintListener paintListener = new TreePaintListener(tree);

				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// tree.addPaintListener(paintListener);
						tree.addListener(SWT.MeasureItem, paintListener);
						tree.addListener(SWT.PaintItem, paintListener);
						tree.addListener(SWT.EraseItem, paintListener);
					}
				});

			}
		}
	}

	@Override
	public void connectionClosed(Connection conn, StudioProgressMonitor mon) {

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();

		if (windows != null && windows.length > 0) {
			IWorkbenchWindow window = windows[0];
			IWorkbenchPage activePage = window.getActivePage();
			activePage.closeAllEditors(false);
		}

		XMPPClient.getInstance().disconnect();

		RestSettings.setServerUrl(null);
		UserSettings.setCurrentUserDn(null);
		UserSettings.setCurrentUserPassword(null);
		UserSettings.setCurrentUserPrivileges(null);

		eventBroker.send("check_lider_status", null);

		LdapConnectionListener.conn = null;
		if (monitor != null) {
			monitor.done();
			monitor = null;
		}

	}

	@Override
	public void connectionOpened(Connection conn, StudioProgressMonitor mon) {

		// Open LDAP Search by default editor on startup
		openLdapSearchEditor();

		monitor = new StudioProgressMonitor(mon);

		Connection connWillBeClosed = LdapConnectionListener.conn;
		LdapConnectionListener.conn = conn;
		String baseDn = LdapUtils.getInstance().findBaseDn(conn);

		if (baseDn == null || baseDn.equals("")) {
			// TODO messages_tr/en
			Notifier.notify("DİKKAT",
					"Lider özelliklerini kullanabilmeniz\r\niçin, bağlantı ayarlarınızda Base DN\r\nayarlanmış olmalıdır. Şu anda\r\nlider özelliklerinizi kullanamazsınız! ");
		} else {

			String restFulConfigDN = "cn=liderAhenkConfig," + baseDn;

			try {

				AuthenticationMethod authMethod = conn.getAuthMethod();
				// sets the application wide current user.
				if (authMethod.equals(AuthenticationMethod.SASL_CRAM_MD5)
						|| authMethod.equals(AuthenticationMethod.SASL_DIGEST_MD5)) {
					String uid = conn.getBindPrincipal();
					String principal = LdapUtils.getInstance().findDnByUid(uid, conn, monitor);
					String passwd = conn.getBindPassword();
					UserSettings.setCurrentUserDn(principal);
					UserSettings.setCurrentUserId(uid);
					UserSettings.setCurrentUserPassword(passwd);
				} else {
					String principal = conn.getBindPrincipal();
					String uid = LdapUtils.getInstance().findAttributeValueByDn(principal,
							LiderConstants.LdapAttributes.UserIdentityAttribute, conn, monitor);
					String passwd = conn.getBindPassword();
					UserSettings.setCurrentUserDn(principal);
					UserSettings.setCurrentUserId(uid);
					UserSettings.setCurrentUserPassword(passwd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			Map<String, Map<String, Boolean>> privileges = LdapUtils.getInstance().findPrivileges(conn, monitor);
			UserSettings.setCurrentUserPrivileges(privileges);

			StudioNamingEnumeration enumeration = LdapUtils.getInstance().search(restFulConfigDN,
					LdapUtils.OBJECT_CLASS_FILTER, new String[] {}, SearchControls.OBJECT_SCOPE, 1, conn, monitor);
			try {
				if (enumeration != null && enumeration.hasMore()) {
					SearchResult item = enumeration.next();

					// REST Address
					Attribute attribute = item.getAttributes()
							.get(LiderConstants.LdapAttributes.configRestFulAddressAttribute);
					String restFulAddress = LdapUtils.getInstance().findAttributeValue(attribute);

					// TODO read these attributes from a properties file. (Same
					// as LiderConstants.LdapAttributes)
					// attribute =
					// item.getAttributes().get(LiderConstants.LdapAttributes.configDeviceObjectClassAttribute);
					// String tmp =
					// LdapUtils.getInstance().findAttributeValue(attribute);
					// if (tmp != null && !"".equals(tmp)) {
					// LiderConstants.LdapAttributes.PardusAhenkObjectClass =
					// tmp;
					// }
					//
					// attribute =
					// item.getAttributes().get(LiderConstants.LdapAttributes.configUserObjectClassAttribute);
					// tmp =
					// LdapUtils.getInstance().findAttributeValue(attribute);
					// if (tmp != null && !"".equals(tmp)) {
					// LiderConstants.LdapAttributes.PardusUserObjectClass =
					// tmp;
					// }
					//
					// attribute =
					// item.getAttributes().get(LiderConstants.LdapAttributes.configUserIdentityAttribute);
					// tmp =
					// LdapUtils.getInstance().findAttributeValue(attribute);
					// if (tmp != null && !"".equals(tmp)) {
					// LiderConstants.LdapAttributes.UserIdentityAttribute =
					// tmp;
					// }
					//
					// attribute =
					// item.getAttributes().get(LiderConstants.LdapAttributes.configOwnerAttribute);
					// tmp =
					// LdapUtils.getInstance().findAttributeValue(attribute);
					// if (tmp != null && !"".equals(tmp)) {
					// LiderConstants.LdapAttributes.AhenkUserAttribute = tmp;
					// }

					if ("".equals(UserSettings.USER_DN)) {
						// TODO messages_tr/en
						Notifier.notify("WARNING",
								String.format(
										"Lider özelliklerini kullanabilmeniz\r\niçin, bağlandığınız kullanıcının\r\n'%s' özelliği tanımlı olmalıdır.",
										LiderConstants.LdapAttributes.UserIdentityAttribute));
					} else {
						if (!"".equals(restFulAddress)) {
							// TODO
//							RestSettings.setServerRestUrl(restFulAddress);
//
//							Map<String, Object> XmppInfo = new RestClient().getXmppAddress();
//							if (XmppInfo != null) {
//								// Initialise UID map before connecting to XMPP
//								// server.
//								LdapUtils.getInstance().getUidMap(conn, monitor);
//								XMPPClient.getInstance().connect(UserSettings.USER_ID, UserSettings.USER_PASSWORD,
//										XmppInfo.get("xmppDomain").toString(), XmppInfo.get("xmppIp").toString(), 5222);
//							} else {
//								// TODO messages_tr/en
//								Notifier.notify("WARNING", "XMPP Baglanti Bilgilerine Erisilemiyor.");
//							}
						} else {
							// TODO messages_tr/en
							Notifier.notify("WARNING",
									"Bu LDAP Sunucusu Lider ile calismak uzere konfigure edilmemiş. Kullanabilmek için, lütfen '"
											+ restFulConfigDN + "' oluşturup '"
											+ LiderConstants.LdapAttributes.configRestFulAddressAttribute
											+ "' özelliğe lider web servisi adres bilgilerini giriniz.");
						}
					}
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		eventBroker.send("check_lider_status", null);

		// Close previous connection if it was opened.
		if (connWillBeClosed != null && connWillBeClosed.getConnectionWrapper().isConnected()) {
			new StudioConnectionJob(new CloseConnectionsRunnable(connWillBeClosed)).execute();
		}

	}

	private void openLdapSearchEditor() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		if (windows != null && windows.length > 0) {
			IWorkbenchWindow window = windows[0];
			final IWorkbenchPage activePage = window.getActivePage();
			IPerspectiveDescriptor perspective = activePage.getPerspective();
			if (LiderConstants.Perspectives.MAIN_PERSPECTIVE_ID.equalsIgnoreCase(perspective.getId())) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						try {
							activePage.openEditor(new GenericEditorInput(LdapSearchEditor.ID, "Editor", "none"),
									LdapSearchEditor.ID);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	public static Connection getConnection() {
		return conn;
	}

	public static StudioProgressMonitor getMonitor() {
		return monitor;
	}

}
