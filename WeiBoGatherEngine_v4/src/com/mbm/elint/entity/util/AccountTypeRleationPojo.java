package com.mbm.elint.entity.util;

import java.util.HashMap;
import java.util.Map;

import com.mbm.util.StringOperatorUtil;

/**
 * 帐户类型对应的自定义的微博类型
 * 
 * @author zel
 * 
 */
public class AccountTypeRleationPojo {
	// 在博主帐户抓取的时候，页面上的pid与微博所属自定义类型的对应关系
	public static Map<String, String> pidToAccountTypeMap = new HashMap<String, String>();

	public static Map<String, String> verifyCodeToAccountTypeMap = new HashMap<String, String>();

	// 初始化对应关系
	static {
		pidToAccountTypeMap.put("100505", AccountTypePojo.Normal);
		// 100306-->百度人物资料
		pidToAccountTypeMap.put("100306", AccountTypePojo.FamousPerson);
		// 100206-->媒体类博主
		pidToAccountTypeMap.put("100206", AccountTypePojo.Media);
		// 100606-->品牌馆类博主
		pidToAccountTypeMap.put("100606", AccountTypePojo.Brand);
		// 100106-->政府类博主
		pidToAccountTypeMap.put("100106", AccountTypePojo.Goverment);
		// 10002-->校园类博主,由于和机构类相重，故不做高校类
		// pidToAccountTypeMap.put("10002", AccountTypePojo.Campus);

		// 10002-->机关单位类,由于上边的注释，暂把campus和organization并为organization
		pidToAccountTypeMap.put("10002", AccountTypePojo.Organization);
		
		// verifyCode to accountType,当上边的pid无法找到针对的博主所属的自定义类型则要用下边的对应关系
		verifyCodeToAccountTypeMap.put("4", AccountTypePojo.Campus);
		verifyCodeToAccountTypeMap.put("7", AccountTypePojo.Organization);
	}

	public static String getAccountType(String pid,String verify_type) {
		String value=pidToAccountTypeMap.get(pid);
		if(!StringOperatorUtil.isBlank(value)){
			if(AccountTypePojo.Organization.equals(value)){
				return verifyCodeToAccountTypeMap.get(verify_type);
			}
			return value;
		}else {
			return AccountTypePojo.None;			
		}
	}
	
	public static void main(String[] args) {
		System.out.println();
	}

}
