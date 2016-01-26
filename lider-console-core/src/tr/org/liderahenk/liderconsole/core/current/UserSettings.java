package tr.org.liderahenk.liderconsole.core.current;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Fethi GÜRCAN <fgurcan@innova.com.tr>
 * @author Sezgin BOZU <sbozu@innova.com.tr>
 * 
 * Application wide user settings class.
 */
public class UserSettings {
	
	private UserSettings()
	{
		// Auto-generated constructor stub
	}

	public static String USER_DN = null;
	public static String USER_PASSWORD = null;
	public static String USER_ID=null;
	public static Map<String,Map<String,Boolean>> USER_PRIVILEGES; 
	
	public static void setCurrentUserDn(String userDn)
	{
		USER_DN = userDn;
	}
	
	public static void setCurrentUserId(String userId)
	{
		USER_ID = userId;
	}
	
	public static void setCurrentUserPassword(String password)
	{
		USER_PASSWORD = password;
	}	
	
	public static void setCurrentUserPrivileges(Map<String,Map<String,Boolean>> privileges)
	{
		USER_PRIVILEGES = privileges;
	}
	
	/**
	 * 
	 * @param dn
	 * @param key
	 * @return true if current user is authorized to given key for given dn.
	 */
	public static Boolean checkPrivilegeFor(String dn,String key){
		return checkPrivilegesFor(dn, new String[] { key });
	}
	
	/**
	 * 
	 * @param dn
	 * @return list of authorization keys for given dn.
	 */
	public static List<String> getPrivilegesFor(String dn){
		List<String> retVal= new ArrayList<String>();
		Map<String,Boolean> tmpMap=new HashMap<String,Boolean>();
		String curdn=dn;
		while(curdn!=null){
			if (USER_PRIVILEGES.containsKey(curdn)){
				Map<String,Boolean> pr=USER_PRIVILEGES.get(curdn);
				for (String key:pr.keySet()){
					if (!tmpMap.containsKey(key))
						tmpMap.put(key, pr.get(key));
				}
			}

			int commaIndex=curdn.indexOf(',');
			if (commaIndex!=-1)
				curdn=curdn.substring(commaIndex+1);
			else
				break;
		}
		
		for(String key:tmpMap.keySet())
			if (tmpMap.get(key))
				retVal.add(key);
		
		return retVal;
	}
	
	/**
	 * 
	 * @param dn
	 * @param keys
	 * @return true if current user is authorized keys for dn.
	 */
	public static Boolean checkPrivilegesFor(String dn,String[] keys){
		Boolean retVal=keys.length>0;
		for(String key:keys){
			String curdn=dn;
			Boolean subFlag=false;
			while(curdn!=null){
				if (USER_PRIVILEGES.containsKey(curdn)){
					Map<String,Boolean> pr=USER_PRIVILEGES.get(curdn);
					if (pr.containsKey(key)){
						subFlag=pr.get(key);
						break;
					} else if (pr.containsKey("ALL")){
						subFlag=pr.get("ALL");
						break;
					}else{
						int commaIndex=curdn.indexOf(',');
						if (commaIndex!=-1)
							curdn=curdn.substring(commaIndex+1);
						else
							break;
					}
				}else{
					int commaIndex=curdn.indexOf(',');
					if (commaIndex!=-1)
						curdn=curdn.substring(commaIndex+1);
					else
						break;
				}
			}
			if (!subFlag){
				retVal=false;
				break;
			}
		}
		return retVal;
	}
}
