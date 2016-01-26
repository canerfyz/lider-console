package tr.org.liderahenk.liderconsole.core.xmpp.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "tr.org.liderahenk.liderconsole.core.xmpp.i18n.messages"; //$NON-NLS-1$
	public static String Notification_TASK_MANAGEMENT;
	public static String Notification_TASK_NAME;
	public static String Notification_TASK_WAS_SUCCESSFULLY_PROCESSED;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			if(key.equals("lider.screenshot"))
				return "Ekran Görüntüsü";			
			if(key.equals("lider.usb"))
				return "Usb"; 
			if(key.equals("lider.printer"))
				return "Yaz\u0131c\u0131"; 
			if(key.equals("lider.firewall"))
				return "Ate\u015F Duvar\u0131"; 
			if(key.equals("lider.quota"))
				return "Kota"; 
			if(key.equals("lider.sample"))
				return "Örnek Eklenti"; 
			if(key.equals("lider.networkmanager"))
				return "A\u011F Yönetimi"; 
			if(key.equals("lider.script"))
				return "Betik"; 
			if(key.equals("lider.service"))
				return "Servis"; 
			if(key.equals("lider.packagemanager"))
				return "Paket Yönetimi"; 
			if(key.equals("lider.report"))
				return "Raporlama";			
			if(key.equals("lider.resourceusage"))
				return "Kaynak Yönetimi"; 
			if(key.equals("NA"))
				return "Bilinmiyor"; 
			if(key.equals("lider.opticaldrive"))
				return "Optik Sürücü Yönetimi"; 
			if(key.equals("SampleDbList"))
				return "Örnek - Veritaban\u0131ndan Liste Çek"; 
			if(key.equals("SampleDbSave"))
				return "Örnek - Veritaban\u0131na Kaydet";			
			if(key.equals("UpdateServiceStatus"))
				return "Servis Durumunu Güncelle"; 
			if(key.equals("GetServiceStatus"))
				return "Servis Durumunu Getir"; 
			if(key.equals("EditFirewallRule"))
				return "Ate\u015F Duvar\u0131 Kural\u0131 Düzenle"; 
			if(key.equals("SendFirewallRule"))
				return "Ate\u015F Duvar\u0131 Kural\u0131 Gönder"; 			
			if(key.equals("lider.printer"))
				return "Yaz\u0131c\u0131";
			
			if(key.equals("EditNicConfig"))
				return "A\u011F Arayüzü Ayarlar\u0131n\u0131 Düzenle";
			if(key.equals("EditHostsConfig"))
				return "Kullan\u0131c\u0131 Sunucu Ayarlar\u0131n\u0131 Düzenle"; 
			if(key.equals("EditHostnameConfig"))
				return "Kullan\u0131c\u0131 Sunucu Ad\u0131 De\u011Fi\u015Ftir"; 
			if(key.equals("EditDnsConfig"))
				return "Kullan\u0131c\u0131 Dns Ayarlar\u0131 Düzenle"; 
			if(key.equals("EditProfile"))
				return "Paket Profili Düzenle"; 
			if(key.equals("UpdateAgentPackage"))
				return "Kullan\u0131c\u0131 Paketi Güncelle"; 
			if(key.equals("SetPrinterQuota"))
				return "Yaz\u0131c\u0131 Kotas\u0131 Belirle"; 
			if(key.equals("GetPrinterQuota"))
				return "Yaz\u0131c\u0131 Kotas\u0131 Getir"; 
			if(key.equals("SetQuota"))
				return "Kota Belirle"; 
			if(key.equals("GetQuota"))
				return "Kota Bilgisi Getir"; 
			if(key.equals("SendResorceUsage"))
				return "Kaynak Kullan\u0131m\u0131 Gönders"; 
			if(key.equals("GetResourceUsage"))
				return "Kaynak Kullan\u0131m\u0131n\u0131 Getir"; 
			if(key.equals("EditScript"))
				return "Betik Düzenle"; 
			if(key.equals("ExecuteScript"))
				return "Betik Çall\u0131\u015Ft\u0131r"; 
			if(key.equals("ListScreenshot"))
				return "Ekran Görünsütüsü Listele"; 
			if(key.equals("TakeScreenshot"))
				return "Ekran Görüntüsü Al"; 
			if(key.equals("SendUsbRule"))
				return "Usb Kural\u0131 Gönder"; 
			if(key.equals("ExecuteReport"))
				return "Rapor Çal\u0131\u015Ft\u0131r"; 
			if(key.equals("EditReport"))
				return "Rapor Düzenle"; 
			if(key.equals("GetLogCommand"))
				return "Islem Tarihçesini Getir"; 
			if(key.equals("GetPluginList"))
				return "Eklenti Listesini Getir"; 
			if(key.equals("GetXmppServerIp"))
				return "GetXmppServerIp=XMPP Sunucu IP Getir"; 
			if(key.equals("GetLogs"))
				return "Islem Tarihçesini Getir"; 
			if(key.equals("MoveUserHome"))
				return "Kullan\u0131c\u0131 Ta\u015F\u0131"; 
			if(key.equals("ScheduledRequestList"))
				return "Zamanlanm\u0131\u015F Görev Listesi"; 
			
			if(key.equals("TaskList"))
				return "Görev Listesi";
			if(key.equals("UpdateScheduledJob"))
				return "Zamanlanm\u0131\u015F Görevleri Güncelle";
			if(key.equals("UpdateAgentPackageList"))
				return "Kullan\u0131c\u0131 Yaz\u0131l\u0131m Paketi Güncelle";
			if(key.equals("EditPackageProfile"))
				return "Yaz\u0131l\u0131m Paket Profili Düzenle";
			if(key.equals("GetUsbRule"))
				return "Usb Kural\u0131 Görüntüle";
			if(key.equals("GetOpticalDriveRule"))
				return "Optik Sürücü Kurallar\u0131n\u0131 Görüntüle";
			if(key.equals("SendOpticalDriveRule"))
				return "Optik Sürücü Kuralı Gönder";
			if(key.equals("cdrom"))
				return "Optik Sürücü";
			if(key.equals("resourceusage"))
				return "Kaynak Kullan\u0131m\u0131";
			if(key.equals("loginpolicy"))
				return "Oturum Yönetimi";
			
			if(key.equals("backupahenk"))
				return "Ahenk Yedekleme Yönetimi";
			
			if(key.equals("installer"))
				return "Ahenk Güncelleme";			
			
			if(key.equals("Installer"))
				return "Ahenk Güncelleme";
			
			if(key.equals("UpdateAgent"))
				return "Ahenk Güncelle";
			
			return "!" + key + "!";	
		}
	}
}
