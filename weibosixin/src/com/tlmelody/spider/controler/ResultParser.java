package com.tlmelody.spider.controler;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ResultParser {
	
	public static  String parser(String htmlSource) {
		JSONObject jsonObject=(JSONObject) JSONValue.parse(htmlSource);
		if (jsonObject.containsKey("msg")) {
			return jsonObject.get("msg").toString();
		} else {
			return "发送失败";
		}
    }
}
