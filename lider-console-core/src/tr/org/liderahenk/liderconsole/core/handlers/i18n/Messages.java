package tr.org.liderahenk.liderconsole.core.handlers.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "tr.org.liderahenk.liderconsole.core.handlers.i18n.messages"; //$NON-NLS-1$

//	private static ResourceBundle RESOURCE_BUNDLE = null;
//	
//	static {
//		 RESOURCE_BUNDLE = ResourceBundle
//					.getBundle(BUNDLE_NAME);
//	}

	public static String getString(String key) {
		try {
			return ResourceBundle
					.getBundle(BUNDLE_NAME).getString(key);
		} catch (MissingResourceException e) {
			if(key.equals("Screenshot"))
				return "Ekran Görüntüsü";			
			if(key.equals("Usb"))
				return "Usb"; 
			if(key.equals("Printer"))
				return "Yaz\u0131c\u0131"; 
			if(key.equals("Firewall"))
				return "Ate\u015F Duvar\u0131"; 
			if(key.equals("Quota"))
				return "Kota"; 
			if(key.equals("Sample"))
				return "Örnek Eklenti"; 
			if(key.equals("Network"))
				return "A\u011F Yönetimi"; 
			if(key.equals("Script"))
				return "Betik"; 
			if(key.equals("Service"))
				return "Servis"; 
			if(key.equals("PackageManager"))
				return "Paket Yönetimi"; 
			if(key.equals("Report"))
				return "Raporlama";			
			if(key.equals("ResourceUsage"))
				return "Kaynak Yönetimi"; 
			if(key.equals("NA"))
				return "Bilinmiyor"; 
			if(key.equals("OpticalDrive"))
				return "Optik Sürücü Yönetimi"; 
			if(key.equals("SampleDbList"))
				return "Örnek - Veritaban\u0131ndan Liste Çek"; 
			if(key.equals("SampleDbSave"))
				return "Örnek - Veritaban\u0131na Kaydet";			
			if(key.equals("UpdateServiceStatus"))
				return "Servis Çalıştır/Durdur"; 
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
				return "Kaynak Kullan\u0131m\u0131 Göster"; 
			if(key.equals("GetResourceUsage"))
			if(key.equals("EditScript"))
				return "Betik Düzenle"; 
			if(key.equals("ExecuteScript"))
				return "Betik Çal\u0131\u015Ft\u0131r"; 
			if(key.equals("ListScreenshot"))
				return "Ekran Görüntüsü Listele"; 
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
			
			if(key.equals("Backup"))
				return "Yedekleme Yönetimi";
			
			if(key.equals("LoginPolicy"))
				return "Oturum Yönetimi";
			
			if(key.equals("SetLoginPolicy"))
				return "Kural Çalıştır";
			
			if(key.equals("GetLoginPolicy"))
				return "Kuralları Listele";
			
			if(key.equals("GetBackups"))
				return "Yedekleri Listele";
			
			if(key.equals("GetBackupRule"))
				return "Yedek Yükle";
			
			if(key.equals("SendBackupRule"))
				return "Yedek Al";
			
			if(key.equals("LIDERMENU"))
				return "Lider Menüsü";
			
			if(key.equals("TASKS"))
				return "Görevler";
			if(key.equals("SCHEDULEDTASKS"))
				return "Zamanlanmış Görevler";
			if(key.equals("SYSLOGS"))
				return "Syslog";
			if(key.equals("LOGS"))
				return "İşlem Tarihçesi";
			
			if(key.equals("EditScript"))
				return "Betik Düzenle";
			
			if(key.equals("SendResourceUsageLimit"))
				return "Aşırı Kullanım Limiti Gönder";
			
			if(key.equals("ListNotifications"))
				return "Aşırı Kullanım Bildirimlerini Listele";
			
			if(key.equals("SendBackupRuleAhenk"))
				return "Ahenk Yede\u011Fi Al";
			
			if(key.equals("GetBackupRuleAhenk"))
				return "Ahenk Yükle";
			
			if(key.equals("GetResourceUsage"))
				return "Kaynak Kullanımı Görüntüle";
			
			if(key.equals("SendResorceUsage"))
				return "Kaynak Kullanımı İsteği Gönder";
			
			if(key.equals("ListNotifications"))
				return "Aşırı Kullanım Bildirimleri Listele";
			
			if(key.equals("ListReport(*)"))
				return "Rapor Listeleme";
			
			if (key.equals("RemoveLoginPolicy")) {
				return "Kural Kaldırma";
			}
			
			if (key.equals("ViewNetworkConfig")) {
				return "Mevcut Konfigürasyonu Göster";
			}
			
			if (key.equals("Installer")) {
				return "Ahenk Güncelleme";
			}
			
			if (key.equals("UpdateAgent")) {
				return "Ahenk Güncelle";
			}
			
			if (key.equals("Password")) {
				return "Parola";
			}
			
			if (key.equals("ChangePassword")) {
				return "Parola Değiştir";
			}
			
			if (key.equals("CapturePolicy")) {
				return "Görüntü Ses Kaydı Politikaları";
			}
			
			if (key.equals("SendCommand")) {
				return "Kayıt Politikası Belirle";
			}
			
			if (key.equals("Antivirus")) {
				return "Antivirüs";
			}
			
			if (key.equals("ChangeStatusAntiVirus")) {
				return "Antivirüs Aç/Kapa";
			}
			
			if (key.equals("ScanFolderAntiVirus")) {
				return "Dizin Tara";
			}
			
			if (key.equals("GetStatusAntiVirus")) {
				return "Antivirüs Durumunu Göster";
			}
			
			if (key.equals("Localuser")) {
				return "Yerel Kullanıcı";
			}
			
			if (key.equals("ListDbGroupLocalUser")) {
				return "Yerel Kullanıcı Gruplarını Listele";
			}
			
			if (key.equals("RemovePrivilegeLocalUser")) {
				return "Ayrıcalıklı Kullanıcı Yetkisini Sil";
			}
			
			if (key.equals("AddPrivilegeLocalUser")) {
				return "Ayrıcalıklı Kullanıcı Yetkisi Ver";
			}
			
			if (key.equals("ListDbLocalUser")) {
				return "Yerel Kullanıcıları Listele";
			}
			
			if (key.equals("SendCommandLocalUser")) {
				return "Yerel Kullanıcı Ayar Komutu Gönder";
			}
			
			if (key.equals("GetLocalUserGroup")) {
				return "Yerel Kullanıcının Gruplarını Görüntüle";
			}
			
			if (key.equals("GetLocalUser")) {
				return "Yerel Kullanıcıyı Görüntüle";
			}
			
			if (key.equals("OpenCloseFirewallRule")) {
				return "Ateş Duvarını Aç/Kapa";
			}
			
			if (key.equals("Wireless")) {
				return "Kablosuz Bağlantı";
			}
			
			if (key.equals("SendWirelessRule")) {
				return "Kablosuz Bağlantı Kuralını Gönder";
			}
			
			if (key.equals("GetWirelessRule")) {
				return "Kablosuz Bağlantı Kuralını Al";
			}
			
			if (key.equals("Desktopsettings")) {
				return "Masaüstü Ayarları";
			}
			
			if (key.equals("GetDesktopUser")) {
				return "Kullanıcı Ayarlarını Getir";
			}
			
			if (key.equals("ListDesktopUser")) {
				return "Kullanıcıları Listele";
			}
			
			if (key.equals("ListDesktopUser")) {
				return "Kullanıcı Haklarını Listele";
			}
			
			if (key.equals("SetDesktopPrivilages")) {
				return "Kullanıcı Masaüstü Haklarını Düzenle";
			}
			
			if (key.equals("SetSystemPrivilages")) {
				return "Kullanıcı Sistem Ayar Haklarını Düzenle";
			}
			
			if (key.equals("SetPanelPrivilages")) {
				return "Kullanıcı Panel Ayar Haklarını Düzenle";
			}
			
			if (key.equals("Sudouser")) {
				return "Ayrıcalıklı Kullanıcı";
			}

			if (key.equals("EditSudoUserRight")) {
				return "Ayrıcalıklı Kullanıcı Yetkilerini Düzenle";
			}
			
			if (key.equals("PPolicy")) {
				return "Parola Politikası";
			}
			
			if (key.equals("PPolicy")) {
				return "Parola Politikası";
			}
			
			if (key.equals("EditPolicy")) {
				return "Parola Düzenle";
			}
			
			if (key.equals("PasswordComplexity")) {
				return "Parola Karmaşıklığı Düzenle";
			}
			
			if (key.equals("SetAccountLockRules")) {
				return "Hesap Kitleme kurallarını Düzenle";
			}
			
			if (key.equals("getLocalComplexity")) {
				return "Yerel Kullanıcı Parola Karmaşıklığı Kurallarını Görüntüle";
			}
			
			if (key.equals("setLocalPasswordExpiration")) {
				return "Yerel Kullanıcı Parola Karmaşıklığı Kurallarını Düzenle";
			}
			
			if (key.equals("getLocalExpiration")) {
				return "Yerel Kullanıcı Parola Geçerliliği Kurallarını Görüntüle";
			}
			
			if (key.equals("ListPolicy")) {
				return "Parola Politikalarını Listele";
			}
			
			if (key.equals("getLocalLockRules")) {
				return "Yerel Kullanıcı Hesap Kitleme Kurallarını Görüntüle";
			}
			
			if (key.equals("AssignPolicy")) {
				return "Kullanıcıya Parola Politikası Ata";
			}
			
			if (key.equals("GetPasswordComplexity")) {
				return "Parola Karmaşıklığı Kuralını Görüntüle";
			}
			
			if (key.equals("setLocalPasswordComplexity")) {
				return "Yerel Kullancı Parola Karmaşıklığı Kuralını Düzenle";
			}
			
			if (key.equals("ListCachePolicy")) {
				return "Ön Bellek Giriş Politikalarını Listele";
			}
			
			if (key.equals("SetCachePolicy")) {
				return "Ön Bellek Giriş Politikalarını Düzenle";
			}
			
			if (key.equals("DeleteCachePolicy")) {
				return "Ön Bellek Giriş Politikalarını Sil";
			}
			
			if (key.equals("Filecreate")) {
				return "Dosya Oluşturma";
			}
			
			if (key.equals("SendFileCreate")) {
				return "Dosya Oluştur";
			}
			
			if (key.equals("Fileaudit")) {
				return "Dosya Denetimi";
			}
			
			if (key.equals("SaveAuditLog")) {
				return "Denetim Kütüğünü Kaydet";
			}
			
			if (key.equals("SetAuditRule")) {
				return "Denetim Kuralını Düzenle";
			}
			
			if (key.equals("ListAuditRule")) {
				return "Denetim Kurallarını Listele";
			}
			
			if (key.equals("DeleteAuditRule")) {
				return "Denetim Kuralını Sil";
			}
			
			if (key.equals("ListAuditLog")) {
				return "Denetim Kural Kütüğünü Görüntüle";
			}
			
			if (key.equals("Desktoppolicy")) {
				return "Masaüstü Politikası";
			}
			
			if (key.equals("SendDesktopPolicy")) {
				return "Masaüstü Politikası Oluştur";
			}
			
			if (key.equals("ListUsbBlackListGroupDetail")) {
				return "Kara Liste Grup Detaylarını Görüntüle";
			}
			
			if (key.equals("SendUsbBlackListGroup")) {
				return "Kara Liste Grubu Düzenle";
			}
			
			if (key.equals("ListUsbBlackListGroup")) {
				return "Kara Liste Gruplarını Listele";
			}
			
			if (key.equals("SaveBlackList")) {
				return "Kara Listeyi Kaydet";
			}
			
			if (key.equals("ListUsbLog")) {
				return "USB Kütüğünü Görüntüle";
			}
			
			if (key.equals("ListUsbLog")) {
				return "USB Kütüğünü Görüntüle";
			}
			
			if (key.equals("SendUsbCommand")) {
				return "USB Kuralını Çalıştır";
			}
			
			if (key.equals("SendUsbCommand")) {
				return "USB Kuralını Çalıştır";
			}
			
			if (key.equals("ListUsbBlackList")) {
				return "Kara Listeyi Görüntüle";
			}
			
			if (key.equals("SaveBlackListGroup")) {
				return "Kara Liste Grubunu Kaydet";
			}
			
			if (key.equals("GroupDeleteUsbRecord")) {
				return "USB Grup Kayıtlarını Sil";
			}
			
			return "!" + key + "!";			
		}
	}
}
