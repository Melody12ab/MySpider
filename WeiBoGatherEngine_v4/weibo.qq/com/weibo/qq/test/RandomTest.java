package com.weibo.qq.test;

import java.io.FileReader;
import java.util.Date;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import junit.framework.TestCase;

public class RandomTest extends TestCase {
	public void testJavaAddJsEngine() throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");

		String jsFileName = "resources.qq/password.js"; // 指定md5加密文件

		// 读取js文件

		FileReader reader;

		reader = new FileReader(jsFileName);

		engine.eval(reader);

		if (engine instanceof Invocable) {
			Invocable invoke = (Invocable) engine;
			// 调用preprocess方法，并传入两个参数密码和验证码
			// pass = (String)invoke.invokeFunction("preprocess",
			// 密码,checkNum[1]);
			// System.out.println("c = " + pass);
			String password = "zhou1234**";
			String hexCode = "\\x00\\x00\\x00\\x00\\x25\\x24\\x6a\\x52";
			String verifyCode = "!P9A";
			// String pass=(String)invoke.invokeFunction("encoding",new
			// Object[]{password,hexCode,verifyCode});
			String pass = (String) invoke.invokeFunction("firstEncoding",
					new Object[] { password, hexCode, verifyCode });
			System.out.println("第一次的pass--------" + pass);
			pass = (String) invoke.invokeFunction("secondEncoding",
					new Object[] { password, hexCode, verifyCode });
			System.out.println("第二次的pass--------" + pass);
			pass = (String) invoke.invokeFunction("thirdEncoding",
					new Object[] { password, hexCode, verifyCode });
			System.out.println("第三次的pass--------" + pass);
		}
	}

	public static void main(String[] args) throws Exception {
		JSONObject jsonObj = new JSONObject();
//		String source="{"+"\"info"+"\":[{\"name"+"\":\"zel\"},{\"name\":\"ma\"}]}";
		String source="[{\"name"+"\":\"zel\"},{\"name\":\"ma\"}]";
//		jsonObj=jsonObj.fromObject(source);
		JSONArray array=JSONArray.fromObject(source);
//		JSONArray array=jsonObj.getJSONArray(source);
		System.out.println(array.size());
		for(Object obj:array){
           System.out.println(obj.toString());			
		}
		
	}
}
