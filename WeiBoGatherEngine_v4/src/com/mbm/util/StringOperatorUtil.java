package com.mbm.util;

import java.util.List;

/**
 * String常用操作类
 * 
 * @author zel
 * 
 */
public class StringOperatorUtil {
	public static boolean isBlank(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isBlankCollection(List list){
		if (list == null || list.isEmpty()) {
			return true;
		}
		return false;
	}
}
