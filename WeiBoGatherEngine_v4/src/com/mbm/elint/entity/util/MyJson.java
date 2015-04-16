package com.mbm.elint.entity.util;

import java.io.Serializable;
import java.util.Date;

import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class MyJson implements Serializable {
	/**
	 * 可认为是第一个版本
	 */
	private static final long serialVersionUID = 1L;

	private JSONObject jsonObj;

	public MyJson() {
		jsonObj = new JSONObject();
	}

	public void createJson(String jsonStr) {
		jsonObj = JSONObject.fromObject(jsonStr);
	}

	public String getStringByKey(Object key) {
		Object value = jsonObj.get(key);
		if (value == null) {
			return null;
		} else {
			return value.toString();
		}
	}

	public Date getDateByKey(Object key) {
		return new Date(Long.parseLong(this.jsonObj.get(key).toString()));
	}

	public int getIntegerByKey(Object key) {
		return Integer.parseInt(this.jsonObj.get(key).toString());
	}

	public void put(Object key, Object value) {
		this.jsonObj.put(key, value);
	}

	public void getValue(Object key) {
		this.jsonObj.get(key);
	}

	@Override
	public String toString() {
		return this.jsonObj.toString();
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
