package com.mbm.util;


public class MyStringFilter {

	public static String getBothEdgeString(String source){
		try{
			String temp=source.substring(0,source.indexOf("--"))+source.substring(source.lastIndexOf("--")+2,source.length());
			return temp;
		}catch(Exception e){
			return source;			
		}
	}
	
	public static String removeFlagString(String source){
		return source.replaceAll("--","");
	}
	
	public static void main(String[] args) {
		String str = "abc,--你他--,def";
        System.out.println(str.indexOf("--"));
        System.out.println(str.charAt(str.indexOf("--")));
        
        System.out.println("第二个++++:"+str.lastIndexOf("--"));
        System.out.println("第二个++++:"+str.charAt(str.lastIndexOf("--")));
        
        System.out.println("取得所要是："+getBothEdgeString(str));
        System.out.println("去除flag是："+removeFlagString(str));
	}
}
