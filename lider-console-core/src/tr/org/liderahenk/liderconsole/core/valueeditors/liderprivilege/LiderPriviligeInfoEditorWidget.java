package tr.org.liderahenk.liderconsole.core.valueeditors.liderprivilege;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.studio.ldapbrowser.common.widgets.search.EntryWidget;
import org.apache.directory.studio.ldapbrowser.core.BrowserCorePlugin;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import tr.org.liderahenk.liderconsole.core.dialogs.LiderMessageDialog;
import tr.org.liderahenk.liderconsole.core.listeners.LdapConnectionListener;


/**
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 * 
 * Popup dialog to edit privileges of a user.
 * 
 * Usage:
 * 
 *     LiderPriviligeInfoEditorWidget dialog = new LiderPriviligeInfoEditorWidget(Display.getDefault().getActiveShell(), getValue().toString());
 *     dialog.create();
 *     dialog.open();
 */
public class LiderPriviligeInfoEditorWidget extends TitleAreaDialog {
	private static final String GET_PLUGIN_LIST = "GET/PLUGIN/LIST";
	private static final String GET_XMPP_ADDRESS = "GET/XMPP/ADDRESS";
	private Shell shell;
	private String dnText;
	private Button chkCanInherit;
	private String liderPriviligeInfo;
	private String selectedLiderPrivilegeInfo;
	private HashMap<String,ArrayList<Privilege>> groups;
	private Composite composite;
	private EntryWidget ww;
	private Button chkAll;
	
	protected LiderPriviligeInfoEditorWidget(Shell parentShell,String selectedTargetDnAndPrivilegeInfo) {		
		super(parentShell);
		this.shell = parentShell;
		this.selectedLiderPrivilegeInfo = selectedTargetDnAndPrivilegeInfo;
	}
	@Override
	public void create() {
		super.create();
		setTitle(Messages.TargetDnAndPriviligeInfoEditorWidget_tr0);
		setMessage(Messages.TargetDnAndPriviligeInfoEditorWidget_tr1);
	}
	@Override
	protected org.eclipse.swt.graphics.Point getInitialSize() {
		
		return new Point(1100,800);
	};
	@SuppressWarnings("unused")
	@Override
	protected Control  createDialogArea(final Composite parent) {
		
		ScrolledComposite sc = new ScrolledComposite(parent,  SWT.V_SCROLL | SWT.H_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		composite = new Composite(sc, SWT.NONE);
		
		GridLayout gl = new GridLayout( 4, false );
		
        composite.setLayout( gl );
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Composite cc = new Composite(composite, SWT.NONE);
        
        GridData grdData = new GridData();
        grdData.horizontalSpan = 3;
        grdData.grabExcessHorizontalSpace = true;
        grdData.horizontalAlignment = GridData.FILL;
        
        cc.setLayout(new GridLayout(1, false));
        
        cc.setLayoutData(grdData);        
        Dn dn = Dn.EMPTY_DN;
        try {
        	if (!selectedLiderPrivilegeInfo.isEmpty()) {
        		dn = new Dn(selectedLiderPrivilegeInfo.substring(1, selectedLiderPrivilegeInfo.length()-1).split(":")[0]);	
			}
		} catch (LdapInvalidDnException e1) {
			e1.printStackTrace();
		}
        
		ww = new EntryWidget(BrowserCorePlugin.getDefault().getConnectionManager().getBrowserConnection(LdapConnectionListener.getConnection()), dn, null, true);
		
		ww.createWidget(cc);
 
        String selectedDn = "";
        boolean selectedInherit = false;
        String privileges = "";
        
        try
        {
        if(selectedLiderPrivilegeInfo != null && !selectedLiderPrivilegeInfo.trim().equals("[::]") && !selectedLiderPrivilegeInfo.trim().equals(""))
        {
        	privileges = selectedLiderPrivilegeInfo.substring(1, selectedLiderPrivilegeInfo.length()-1).split(":")[1];
        	selectedDn = selectedLiderPrivilegeInfo.substring(1, selectedLiderPrivilegeInfo.length()-1).split(":")[0];
        	selectedInherit = Boolean.parseBoolean(selectedLiderPrivilegeInfo.substring(1, selectedLiderPrivilegeInfo.length()-1).split(":")[2]);
        }
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        setDnText(selectedDn);

//        BaseWidgetUtils.createLabel(composite, " ", 1);
        // TODO yeni auth ve rest yapisina gore duzenle!!! - emre
//        ServerResult serverResult = new RestClient().getServerResult(new HashMap<String, Object>(), "GET", "NA", "LIST", "PLUGIN", "NA", "Ldap", "NA", false, null); 
//        if(serverResult == null)
//        {
//        	LiderMessageDialog.openError(shell, "Lider sunucu REST servisi", "Lider sunucu REST servisi cevap vermemektedir. Lütfen tekrar deneyiniz.");
//        }
//        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
        ArrayList<Privilege> itemList = new ArrayList<Privilege>();
        PrivilegeObject itemMap = new PrivilegeObject();
//        if (null != serverResult) {
//        
//			Gson gson = gsonBuilder.create(); //$NON-NLS-1$
//			itemMap = (PrivilegeObject) gson.fromJson( serverResult.getResponseBody().getResultMapString(), new TypeToken<PrivilegeObject>() {}.getType());			
//        }		
        
        itemList = (ArrayList<Privilege>) itemMap.getPluginList(); //$NON-NLS-1$
        ArrayList<Privilege> arr = itemList;
// 		itemList.toArray(arr);
//        chkAll = BaseWidgetUtils.createCheckbox(composite, "Hepsini Sec", 1);
// 		chkAll.setData("ALL");
// 		final Button chkClear = BaseWidgetUtils.createCheckbox(composite, "Temizle", 1);
// 		chkClear.setData("CLEAR");
// 		final Button chkInverse = BaseWidgetUtils.createCheckbox(composite, "Tersine Cevir", 2);
// 		chkInverse.setData("INVERSE");
// 		
// 		chkAll.addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if(chkAll.getSelection())
//				{
//				for(Control c: composite.getChildren())
//				{			
//					if(c instanceof Group)								
//					for (Control ctlgrp: ((Group)c).getChildren())
//					{		
//						if(!((Button)ctlgrp).getData().equals(GET_XMPP_ADDRESS) && !((Button)ctlgrp).getData().equals(GET_PLUGIN_LIST))
//						{
//							((Button)ctlgrp).setSelection(true);
//						}
//					}			
//				}
//				chkClear.setSelection(false);
//				chkInverse.setSelection(false);
//				}
//				else
//				{
//					for(Control c: composite.getChildren())
//					{			
//						if(c instanceof Group)								
//						for (Control ctlgrp: ((Group)c).getChildren())
//						{	
//							if(!((Button)ctlgrp).getData().equals(GET_XMPP_ADDRESS) && !((Button)ctlgrp).getData().equals(GET_PLUGIN_LIST))
//							{
//								((Button)ctlgrp).setSelection(false);
//							}
//						}			
//					}
//				}
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				widgetSelected(e);
//				
//			}
//		});
// 		chkClear.addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if(chkClear.getSelection())
//				{
//				for(Control c: composite.getChildren())
//				{			
//					if(c instanceof Group)								
//					for (Control ctlgrp: ((Group)c).getChildren())
//					{
//						if(!((Button)ctlgrp).getData().equals(GET_XMPP_ADDRESS) && !((Button)ctlgrp).getData().equals(GET_PLUGIN_LIST))
//						{
//							((Button)ctlgrp).setSelection(false);	
//						}
//					}			
//				}
//				chkAll.setSelection(false);
//				chkInverse.setSelection(false);
//				}
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				widgetSelected(e);
//				
//			}
//		});
// 		
// 		chkInverse.addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//				for(Control c: composite.getChildren())
//				{							
//					if(c instanceof Group)								
//					for (Control ctlgrp: ((Group)c).getChildren())
//					{
//						if(!((Button)ctlgrp).getData().equals(GET_XMPP_ADDRESS) && !((Button)ctlgrp).getData().equals(GET_PLUGIN_LIST))
//						{
//							if(((Button)ctlgrp).getSelection())
//								((Button)ctlgrp).setSelection(false);
//	 						else
//	 							((Button)ctlgrp).setSelection(true);
//						}
//					}			
//				}
//				chkAll.setSelection(false);
//				chkClear.setSelection(false);
//				
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				widgetSelected(e);				
//			}
//		});
// 		
// 		
// 		groups = new HashMap<String,ArrayList<Privilege>>();
//
// 		ArrayList<Privilege> hGroup = null;
//
// 		for(Object o : arr)
// 		{
// 			Privilege p = (Privilege)o;
// 			if(p.getPluginId() != null && !p.getPluginId().trim().equalsIgnoreCase(""))
// 			{
// 				
// 				if(groups.containsKey(p.getPluginId())) {
// 					hGroup = groups.get(p.getPluginId());  					
// 					hGroup.add(p);
// 					
// 					groups.remove(p.getPluginId());
// 					groups.put(p.getPluginId(),hGroup);
// 				}
// 				else {
// 					hGroup = new ArrayList<Privilege>();
// 					hGroup.add(p);
// 					groups.put(p.getPluginId(),hGroup);
// 				} 				
// 			}
// 			else {
// 				if ( !groups.containsKey("NA") ) {
// 					ArrayList<Privilege> pr = new ArrayList<Privilege>();
// 					pr.add(p); 					
// 					groups.put("NA", pr); 					
//				}
// 				else {
//					groups.get("NA").add(p);
//				}
//			}
// 		}
// 		
// 		createLiderManuPrivileges(groups);
//// 		HashMap<String, Group> naGroups = new HashMap<String, Group>();
// 		for(String s : groups.keySet())
// 		{
// 			Group wGroups = null;
// 			if (!"NA".equals(s) && null != groups.get(s).get(0).getGroupTitle() && !groups.get(s).get(0).getGroupTitle().equals("NA") && groups.get(s).get(0).getPluginId() != null) {
// 				wGroups = BaseWidgetUtils.createGroup(composite, tr.org.liderahenk.liderconsole.core.handlers.i18n.Messages.getString( groups.get(s).get(0).getPluginId() ), 1);
// 				createCheckBoxGroup(s, wGroups);
//			}
// 		}
// 		Group g = BaseWidgetUtils.createGroup(composite, tr.org.liderahenk.liderconsole.core.handlers.i18n.Messages.getString( "NA" ), 1);
// 		g.setVisible(false);
// 		g.setEnabled(false);
// 		
//		createCheckBoxGroup("NA", g);
// 		
// 		if(selectedLiderPrivilegeInfo.contains(":ALL:true"))
// 		{
// 			chkAll.setSelection(true);
// 			for(Control c: composite.getChildren())
//			{			
//				if(c instanceof Group)								
//				for (Control ctlgrp: ((Group)c).getChildren())
//				{			
//					((Button)ctlgrp).setSelection(true);
//				}			
//			}
// 		}
//        chkCanInherit = BaseWidgetUtils.createCheckbox(composite, Messages.TargetDnAndPriviligeInfoEditorWidget_tr20, 3);
//        chkCanInherit.setSelection(selectedInherit);
//        chkCanInherit.setVisible(false);
//        sc.setMinSize(150,composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
//		sc.setContent(composite);
//        composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//        sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        return composite;
	}
	
	private void createCheckBoxGroup(String s, Group wGroups)
	{
		Font f = wGroups.getFont();
		FontData[] fd = f.getFontData();
		fd[0].setStyle(SWT.BOLD);
		fd[0].setName("tahoma");			
		Font f2 = new Font(f.getDevice(), fd);
		wGroups.setFont(f2);
			ArrayList<Privilege> commands = groups.get(s);
			HashMap<String, List<Privilege>> _map = new HashMap<String, List<Privilege>>();
			
			for(Privilege str : commands)
			{
				if ( !_map.containsKey(str.getGroupId()) ) {
					List<Privilege> pr = new ArrayList<Privilege>();
					pr.add(str); 					
					_map.put(str.getGroupId(), pr); 					
			}
				else {
				_map.get(str.getGroupId()).add(str);					
			}
			}
			
			for (Entry<String, List<Privilege>> privilege : _map.entrySet()) {
			List<Privilege> privs = privilege.getValue();
			
			String str = privilege.getKey();
//			Button b = BaseWidgetUtils.createCheckbox(wGroups, tr.org.liderahenk.liderconsole.core.handlers.i18n.Messages.getString( str ), 1);	 				
//			b.addSelectionListener(new SelectionListener() {
//				
//				@Override
//				public void widgetSelected(SelectionEvent e) {					
//					chkAll.setSelection(false);					
//				}
//				
//				@Override
//				public void widgetDefaultSelected(SelectionEvent e) {
//					
//				}
//			});
//			Font chkFont = b.getFont();
//			FontData[] chkFd = chkFont.getFontData();
//			chkFd[0].setStyle(SWT.NORMAL);
//			chkFd[0].setName("tahoma");
//			chkFd[0].setHeight(8);
//			Font chkFont2 = new Font(chkFont.getDevice(), chkFd);
//			b.setFont(chkFont2);
//			List<String> list = new ArrayList<String>();
//				
//				
// 			for(Privilege str1 : privs)
// 			{
// 				if (!list.contains(str1.getAttribute()+"/"+str1.getCommand()+"/"+str1.getAction())) {
// 					list.add(str1.getAttribute()+"/"+str1.getCommand()+"/"+str1.getAction());
// 					
// 		 			b.setData( StringUtils.join(list, ",") );
// 		 			if ((str1.getAttribute()+"/"+str1.getCommand()+"/"+str1.getAction()).equals(GET_XMPP_ADDRESS)) {
// 						b.setSelection(true);
// 						b.setEnabled(false);
// 					}
// 		 			if ((str1.getAttribute()+"/"+str1.getCommand()+"/"+str1.getAction()).equals(GET_PLUGIN_LIST)) {
// 						b.setSelection(true);
// 						b.setEnabled(false);
// 					}
//				}	 				
// 			}
// 			if(!list.contains(GET_XMPP_ADDRESS))
// 				list.add(GET_XMPP_ADDRESS);
// 			if (!list.contains(GET_PLUGIN_LIST)) {
// 				list.add(GET_PLUGIN_LIST);	
//			} 		 			
//
//			if(selectedLiderPrivilegeInfo.contains((String)b.getData()))
//				b.setSelection(true);
		}
	}
	
	private void createLiderManuPrivileges(HashMap<String, ArrayList<Privilege>> groups) {
		ArrayList<Privilege> liderMenuPrivileges = new ArrayList<Privilege>();
 		Privilege taskPrivilege = new Privilege();
 		
 		taskPrivilege.setAttribute("GET");
 		taskPrivilege.setCommand("TASKS");
 		taskPrivilege.setAction("DB");
 		taskPrivilege.setGroupId("TASKS");
 		taskPrivilege.setGroupTitle("LIDERMENU");
 		taskPrivilege.setPluginId("LIDERMENU");
 		
 		liderMenuPrivileges.add(taskPrivilege);
 		
 		Privilege scheduledTaskPrivilege = new Privilege();
 		
 		scheduledTaskPrivilege.setAttribute("GET");
 		scheduledTaskPrivilege.setCommand("SCHEDULEDTASKS");
 		scheduledTaskPrivilege.setAction("DB");
 		scheduledTaskPrivilege.setGroupId("SCHEDULEDTASKS");
 		scheduledTaskPrivilege.setGroupTitle("LIDERMENU");
 		scheduledTaskPrivilege.setPluginId("LIDERMENU");
 		
 		liderMenuPrivileges.add(scheduledTaskPrivilege);
 		
 		Privilege syslogPrivilege = new Privilege();
 		
 		syslogPrivilege.setAttribute("GET");
 		syslogPrivilege.setCommand("SYSLOGS");
 		syslogPrivilege.setAction("DB");
 		syslogPrivilege.setGroupId("SYSLOGS");
 		syslogPrivilege.setGroupTitle("LIDERMENU");
 		syslogPrivilege.setPluginId("LIDERMENU");
 		
 		liderMenuPrivileges.add(syslogPrivilege);
 		
 		Privilege logsPrivilege = new Privilege();
 		
 		logsPrivilege.setAttribute("GET");
 		logsPrivilege.setCommand("LOGS");
 		logsPrivilege.setAction("DB");
 		logsPrivilege.setGroupId("LOGS");
 		logsPrivilege.setGroupTitle("LIDERMENU");
 		logsPrivilege.setPluginId("LIDERMENU");
 		
 		liderMenuPrivileges.add(logsPrivilege);
 		
 		groups.put("LIDERMENU", liderMenuPrivileges);
	}
	@Override
	protected void okPressed() {
		
		if(ww.getDn().isEmpty())
		{
			LiderMessageDialog.openConfirm(shell, "Yetki verilecek düğüm seçilmedi.", "Lütfen yetki vermek istediginiz düğümü seçiniz.");
			return;
		}
		String privilegeStr=""; //$NON-NLS-1$
				
		for(Control c: composite.getChildren())
		{
			if(c instanceof Button)
			{
				if(((Button) c).getData() != null && ((Button) c).getData().equals("ALL") && ((Button)c).getSelection())
				{
					privilegeStr += "ALL" +",";
					break;
				}
			}
			if(c instanceof Group)								
			for (Control ctlgrp: ((Group)c).getChildren())
			{			
				if(((Button)ctlgrp).getSelection())
				{
					privilegeStr += ((Button)ctlgrp).getData() + ","; //$NON-NLS-1$				
				}
			}			
		}
		if(privilegeStr.length()==0 || (privilegeStr.length() > 4 && privilegeStr.startsWith("ALL")))
		{
			LiderMessageDialog.openConfirm(shell, "Yetki verilirken hata" , "Yetki verilirken \"ALL\" seçeğini yada ilgili pluginleri seçerek yetki veriniz.");
			return;
		}
		liderPriviligeInfo = "[" + ww.getDn()+":" + privilegeStr.substring(0, privilegeStr.length()-1) + ":" + "true" +"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if(selectedLiderPrivilegeInfo.equalsIgnoreCase(liderPriviligeInfo))
			super.cancelPressed();
		else
			super.okPressed();
	}
	public String getLiderPrivilegeInfo()
	{		
		return liderPriviligeInfo;
	}
	public String getDnText() {
		return dnText;
	}
	public void setDnText(String dnText) {
		this.dnText = dnText;
	}
}

