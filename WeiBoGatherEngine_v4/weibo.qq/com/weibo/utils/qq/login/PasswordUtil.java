package com.weibo.utils.qq.login;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class PasswordUtil {
	public static Invocable invoke = null;
	static {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine jsEngine = manager.getEngineByName("javascript");
		// String jsFileName = "resources.qq/password.js"; // 指定md5加密文件
		String jsFileName = "password.js"; // 指定md5加密文件
		Reader reader;
		try {
			InputStream in = PasswordUtil.class.getClassLoader()
					.getResourceAsStream(jsFileName);
			reader = new InputStreamReader(in);
			jsEngine.eval(reader);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		invoke = (Invocable) jsEngine;

	}

	

	public static String getPassEncoding(String password, String hexCode,
			String verifyCode) {
		String pass = null;
		try {
			pass = (String) invoke.invokeFunction("encoding", new Object[] {
					password, hexCode, verifyCode });
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return pass;
	}

	
	
	public static String getHexString(String hexCode) {
		String pass = null;
		try {
			pass = (String) invoke.invokeFunction("hexchar2bin",
					new Object[] { hexCode });
		} catch (Exception e) {
			System.out.println("将字符串转化为16进制时，出现异常!");
			e.printStackTrace();
		}
		return pass;
	}
	public static void main(String[] args) {
		String pass = null;
		try {
			String pt_uinCode="\\x00\\x00\\x00\\x00\\x96\\x78\\x30\\x99"; 
			pt_uinCode = pt_uinCode.replace("\\x", "");// 转化成js的16进制，用java的不可，暂不明原因
			pt_uinCode = PasswordUtil.getHexString(pt_uinCode);
			pass = (String) invoke.invokeFunction("encoding",
					new Object[] { "zhouking",pt_uinCode,"kppk" });
			System.out.println("pass---"+pass);
		} catch (Exception e) {
			System.out.println("将字符串转化为16进制时，出现异常!");
			e.printStackTrace();
		}
	}

}
