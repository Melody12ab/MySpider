package com.mbm.elint.entity.util;

/**
 * 类型区分的pojo类， SINA为新浪 QQ为腾读
 * 
 * @author zel
 * 
 */
public class TypePojo {
	public static final String SINA = "1";
	public static final String QQ = "2";

	public static boolean isQQ(String url) {
		if (url.contains("t.qq")) {
			return true;
		}
		return false;
	}

}
