package com.mbm.elint.entity.util;

import java.io.Serializable;

import net.sf.json.JSONArray;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class MyJsonArray implements Serializable {
	/**
	 * 可认为是第一个版本
	 */
	private static final long serialVersionUID = 1L;

	private JSONArray jsonArray;

	public MyJsonArray() {
		jsonArray = new JSONArray();
	}

	public JSONArray getJsonArray(){
		return jsonArray;
	}
	
	public void createJsonArray(String jsonStr) {
		jsonArray = JSONArray.fromObject(jsonStr);
	}

	@Override
	public String toString() {
		return this.jsonArray.toString();
	}

	public static void main(String[] args) {
		// String source="{"+"" +
		// "\"data\""+":"+"\"123\""+"}";
		// String source = "{﻿\"data\""+":"+"[\"123\"]"+"}";
		// System.out.println("source---" + source);
		// MyJson json = new MyJson();
		// json.createJson(source);
		// System.out.println(json.getStringByKey("data"));
	}
}
