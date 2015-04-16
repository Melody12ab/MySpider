package com.weibo.utils.sina;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

public class MyStringUtils {

	public static String getIdByUrl(String url) {
		String suffix_str = null;
		url=url.trim();
		if (url.lastIndexOf("/") == url.length() - 1) {
			url = url.substring(0, url.length() - 1);
			suffix_str = url.substring(url.lastIndexOf('/') + 1, url.length());
		} else {
			suffix_str = url.substring(url.lastIndexOf('/') + 1, url.length());
		}
		return suffix_str;
	}

	public static String getUrlRemoveSlash(String url) {
		if (url.lastIndexOf("/") == url.length() - 1) {
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	public static String getUrlByKeyWord(String keyWord) {
		try {
			keyWord = URLEncoder.encode(keyWord.trim(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		keyWord = keyWord.replaceAll("%", "%25");
		keyWord = keyWord.replaceAll("\\+", "%2520");
		// keyWord = "http://s.weibo.com/weibo/" + keyWord +
		// "&Refer=weibo_weibo";
//		keyWord = "http://s.weibo.com/weibo/" + keyWord + "&Refer=STopic_box";
		keyWord = "http://s.weibo.com/weibo/" + keyWord + "&xsort=time&Refer=weibo_wb";

		return keyWord;
	}

	public static String getUrlByKeyWord4QQ(String keyWord,String pos) {
		try {
			keyWord = URLEncoder.encode(keyWord.trim(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// keyWord = keyWord.replaceAll("%", "%25");
		// keyWord = keyWord.replaceAll("\\+", "%2520");
		keyWord = "http://search.t.qq.com/index.php?k=" + keyWord + "&pos="+pos;
		// System.out.println("keyword URL--"+keyWord);
		return keyWord;
	}

	public static int getUserType(String uerType) {
		if (StringUtils.isBlank(uerType)) {
			return 0;
		}
		if (uerType.equals("新浪个人认证")) {
			return 1;
		} else if (uerType.equals("新浪认证") || uerType.equals("新浪机构认证")) {
			return 2;
		} else if (uerType.equals("微博达人")) {
			return 3;
		}
		return 0;
	}

	public static boolean isNumber(String str) {

		return false;
	}

	String suffix_str = null;

	public String getPicPassByUrl(String url) {
		if (url.lastIndexOf("/") == url.length() - 1) {
			url = url.substring(0, url.length() - 1);
			suffix_str = url.substring(url.lastIndexOf('/') + 1, url.length());
		} else {
			suffix_str = url.substring(url.lastIndexOf('/') + 1, url.length());
		}
		return suffix_str;
	}
	
	public static void main(String[] args) throws Exception {
		// String url1="http://www.weibo.com/1234/";
		// String url2="http://weibo.com/u/asdfasf";
		// System.out.println(getIdByUrl(url1));
		// System.out.println(getIdByUrl(url2));
		// System.out.println(Long.MAX_VALUE);
		// System.out.println(Double.MAX_VALUE);
		String keyWord="你好";
		keyWord = URLEncoder.encode(keyWord.trim(), "UTF-8");
		System.out.println(keyWord);
	}

}
