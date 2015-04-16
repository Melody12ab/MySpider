package com.mbm.util;

import java.util.regex.Pattern;

public class RegexUtil {
	public static boolean isInChinese(String aid,String regex){
		Pattern pattern = Pattern.compile(regex);
		boolean flag=!pattern.matcher(aid).find();
		System.out.println("匹配结果为--------:"+flag);
		return flag;
	}
	public static void main(String args[]) {
		String aid="农业部.-";
		String regex="[0-9a-zA-Z\\-]";
		
		
	}
}
