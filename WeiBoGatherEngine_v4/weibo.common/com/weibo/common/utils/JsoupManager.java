package com.weibo.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class JsoupManager {
	public static Whitelist NONE = Whitelist.none();

	public static String getNormalText(String doc) {
		if (doc == null) {
			return null;
		}
		return Jsoup.clean(doc, NONE);
	}

	public static void main(String[] args) {
		String str = "<p><a href='网址' onclick='stealCookies()'>开源中国社区</a></p>http://www.baidu.com";
		System.out.println(getNormalText(str));
	}
}
