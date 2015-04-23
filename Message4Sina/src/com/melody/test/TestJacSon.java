package com.melody.test;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class TestJacSon {
	public static void main(String[] args) {
		/**
		 * 解析 json 格式字符串
		 */
//		try {
//			String str = "{\"data\":{\"birth_day\":7,\"birth_month\":6},\"errcode\":0,\"msg\":\"ok\",\"ret\":0}";
//
//			ObjectMapper mapper = new ObjectMapper();
//			JsonNode root = mapper.readTree(str);
//
//			JsonNode data = root.path("data");
//
//			JsonNode birth_day = data.path("birth_day");
//			System.out.println(birth_day.asInt());
//
//			JsonNode birth_month = data.path("birth_month");
//			System.out.println(birth_month.asInt());
//
//			JsonNode msg = root.path("msg");
//			System.out.println(msg.textValue());
//		} catch (IOException e) {
//		}
	}
}
