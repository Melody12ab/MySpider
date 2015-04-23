package com.melody.sinacookie;

import com.weibo.zel.entity.util.LoginPojo;
import com.weibo.zel.main.GetUserCookie4Me;


public class sinaCookie{
	
	public static String getMyCookie(String user,String pass,String uid) throws Exception{
		LoginPojo loginPojo = new LoginPojo();

		loginPojo.setUsername(user);
		loginPojo.setPassword(pass);
		loginPojo.setUid(uid);
		GetUserCookie4Me getUserCookie=new GetUserCookie4Me(loginPojo);
		return getUserCookie.getCookies();
	}
	public static String getMyCookie(){
		LoginPojo loginPojo = new LoginPojo();
		loginPojo.setUsername("mathwebteam@sina.com");
		loginPojo.setPassword("mathwebteam");
		loginPojo.setUid("mathwebteam");
		GetUserCookie4Me getUserCookie=new GetUserCookie4Me(loginPojo);
		String cookie=null;
		try {
			cookie=getUserCookie.getCookies();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取cookie异常");
		}
		return cookie;
	}

}
