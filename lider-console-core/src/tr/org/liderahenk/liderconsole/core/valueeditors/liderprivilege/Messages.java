package tr.org.liderahenk.liderconsole.core.valueeditors.liderprivilege;

import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "tr.org.liderahenk.liderconsole.core.valueeditors.liderprivilege.messages"; //$NON-NLS-1$
	public static String AttributeOptionsWizardPage_tr0;
	public static String AttributeOptionsWizardPage_tr1;
	public static String AttributeOptionsWizardPage_tr14;
	public static String AttributeOptionsWizardPage_tr15;
	public static String AttributeOptionsWizardPage_tr5;
	public static String AttributeOptionsWizardPage_tr6;
	public static String AttributeOptionsWizardPage_tr7;
	public static String AttributeOptionsWizardPage_tr8;
	public static String AttributeOptionsWizardPage_tr9;
	public static String AttributeTypeWizardPage_0;
	public static String AttributeTypeWizardPage_1;
	public static String AttributeWizard_tr0;
	public static String AttributeWizard_tr2;
	public static String AttributeWizard_tr3;
	public static String BrowserQuickSearchWidget_tr33;
	public static String BrowserQuickSearchWidget_tr36;
	public static String BrowserQuickSearchWidget_tr37;
	public static String BrowserQuickSearchWidget_tr38;
	public static String BrowserQuickSearchWidget_tr39;
	public static String EntryEditorWidget_tr11;
	public static String EntryEditorWidget_tr12;
	public static String EntryEditorWidget_tr13;
	public static String EntryEditorWidget_tr14;
	public static String EntryEditorWidget_tr4;
	public static String EntryEditorWidget_tr5;
	public static String EntryEditorWidget_tr6;
	public static String EntryEditorWidget_tr7;
	public static String EntryEditorWidgetContentProvider_tr2;
	public static String EntryEditorWidgetTableMetadata_tr0;
	public static String EntryEditorWidgetTableMetadata_tr1;
	public static String LdapRolesTree_14;
	public static String LdapTreeWithAllObjects_tr16;
	public static String MoveEntryDialog_0;
	public static String NewAttributeAction_1;
	public static String NewAttributeAction_6;
	public static String PasswordDialog_0;
	public static String PasswordDialog_2;
	public static String PasswordDialog_4;
	public static String RenameEntryDialog_tr4;
	public static String TargetDnAndPriviligeInfoEditorWidget_tr0;
	public static String TargetDnAndPriviligeInfoEditorWidget_tr1;
	public static String TargetDnAndPriviligeInfoEditorWidget_tr2;
	public static String TargetDnAndPriviligeInfoEditorWidget_tr20;
	public static String TargetDnAndPriviligeInfoEditorWidget_tr4;
	public static String TargetDnAndPriviligeInfoEditorWidget_tr9;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	public static String getString(String key)
	{
		return ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
	}

	private Messages() {
	}
}
