package com.melody.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLencode {
	public static String encode(String name){
		String result=null;
		String tmp;
		try {
			tmp = URLEncoder.encode(name, "UTF-8");
			result=URLEncoder.encode(tmp, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
