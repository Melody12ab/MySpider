package com.caidongyu.application.collection;

import java.util.Collection;
import java.util.Map;

/**
 * @author 黑云压城
 * @see 数据结构判断工具
 * @version 2013/3/20
 */
public class CollectionUtil {

	private CollectionUtil(){};
	
	public static boolean isEmpty(Collection<?> c){
		return c == null || c.size() == 0;
	}

	public static boolean isEmpty(Map<?,?> c){
		return c == null || c.isEmpty();
	}

	public static boolean isEmpty(Object c[]){
		return c == null || c.length == 0;
	}
}
