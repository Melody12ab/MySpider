package com.melody.sinacookie;

public class Cookie {
	private Cookie(){}
	private static String cookie=null;
	public static String getCookie(){
		if(cookie==null){
			cookie=sinaCookie.getMyCookie();
		}
		return cookie;
	}
}
