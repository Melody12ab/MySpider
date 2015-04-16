package com.weibo.utils.qq.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.log4j.Logger;

import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.entity.util.ProxyPojo;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.MyHttpConnectionManager;

public class GetUserCookie4QQ {
	private static Logger logger = Logger.getLogger(GetUserCookie4QQ.class);
	private LoginPojo loginPojo;

	public LoginPojo getLoginPojo() {
		return loginPojo;
	}

	public void setLoginPojo(LoginPojo loginPojo) {
		this.loginPojo = loginPojo;
	}

	public boolean isLogin = false;
	private HttpClient client = null;

	/**
	 * 为腾读微博登陆添加的变量
	 * 
	 * @return
	 */
	private String verifyCode;// 提交时候的验证码，非那种验证码
	private String pt_uinCode;// checkVC时候根据qq生成的uin
	private String passEncoding;// 加密后的密码

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}

	public GetUserCookie4QQ(LoginPojo loginPojo, ProxyPojo proxyPojo) {
		/**
		 * 设置ip proxy
		 */
		HttpHost proxy = new HttpHost(proxyPojo.getIp(), proxyPojo.getPort(),
				"http");
		this.client = MyHttpConnectionManager.getNewHttpClient();
		this.client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);

		this.loginPojo = loginPojo;
	}

	public GetUserCookie4QQ() {
		// this.client = MyHttpConnectionManager.getHttpClient();
		this.client = MyHttpConnectionManager.getNewHttpClient();
	}

	public GetUserCookie4QQ(LoginPojo loginPojo) {
		this.loginPojo = loginPojo;
		this.client = MyHttpConnectionManager.getHttpClient();
	}

	/**
	 * 得到用户登陆之前的verifyCode和pt.uin
	 */
	public void getCheckVC() throws Exception {
		HttpGet get = new HttpGet("http://check.ptlogin2.qq.com/check?uin="
				+ loginPojo.getUsername()
				+ "&appid=46000101&ptlang=2052&js_type=2&js_ver=10009");
		get.setHeader("Host", "check.ptlogin2.qq.com");
		get.setHeader("Referer", "http://t.qq.com/");
		get
				.setHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");
		HttpResponse response = client.execute(get);
		HttpEntity entity1 = response.getEntity();
		String result1 = dump(entity1);
		get.abort();
		/**
		 * 从返回的3个参数中，提取verifyCode
		 */
		String beginRegex = ",'";
		String endRegex = "',";
		AnsjPaser verifyBlock = new AnsjPaser(beginRegex, endRegex, result1,
				AnsjPaser.TEXTEGEXANDNRT);

		System.out.println("得到的 verifyCode和uin---" + result1);

		verifyCode = verifyBlock.getText();
		/**
		 * 从返回的3个参数中，提取verifyCode
		 */
		beginRegex = "','" + AnsjPaser.TEXTEGEXANDNRT + "','";
		endRegex = "'\\)";
		AnsjPaser pt_uinBlock = new AnsjPaser(beginRegex, endRegex, result1,
				AnsjPaser.TEXTEGEXANDNRT);
		pt_uinCode = pt_uinBlock.getText();
	}

	/**
	 * 生成密码
	 */
	public void getPassEncoding() {
		pt_uinCode = pt_uinCode.replace("\\x", "");// 转化成js的16进制，用java的不可，暂不明原因
		pt_uinCode = PasswordUtil.getHexString(pt_uinCode);

		passEncoding = PasswordUtil.getPassEncoding(loginPojo.getPassword(),
				pt_uinCode, verifyCode);
		// System.out.println("得到的加密后的password---"+passEncoding);
	}

	public String getCookies() throws ClientProtocolException, IOException,
			Exception {
		getCheckVC();
		getPassEncoding();
		String url_suffix = "ptlang=2052&u="
				+ loginPojo.getUsername()
				+ "&p="
				+ passEncoding
				+ "&verifycode="
				+ verifyCode
				+ "&low_login_enable=1&low_login_hour=720&css=http://imgcache.qq.com/ptcss/b4/wb/46000101/login1.css&aid=46000101&mibao_css=m_weibo&u1=http%3A%2F%2Ft.qq.com&ptredirect=1&h=1&from_ui=1&dumy=&fp=loginerroralert&action=2-15-12621&g=1&t=1&js_type=2&js_ver=10009&dummy=";
		HttpGet cookieGetMethod = new HttpGet("http://ptlogin2.qq.com/login?"
				+ url_suffix);
		cookieGetMethod.setHeader("Host", "ptlogin2.qq.com");
		cookieGetMethod.setHeader("Referer", "http://t.qq.com/");
		cookieGetMethod.setHeader("Accept", "*/*");
		cookieGetMethod.setHeader("Accept-Encoding", "gzip, deflate");
		cookieGetMethod.setHeader("Accept-Language", "zh-CN");
		cookieGetMethod.setHeader("Connection", "Keep-Alive");
		cookieGetMethod
				.setHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");
		HttpResponse response = client.execute(cookieGetMethod);
		HttpEntity entity = response.getEntity();
		Header[] h = response.getAllHeaders();
		String cookies = "";
		String temp = "";
		int count = 1;
		String result = "";
		for (int i = 0; i < h.length; i++) {
			// System.out.println("header[" + i + "]------" + h[i]);
			if (h[i].getName().contains("Set-Cookie")) {
				temp = h[i].getValue();
				// cookie是键值对，此处的";"一定要加上
				if (count == 1) {
					cookies = cookies + temp;
				} else {
					cookies = cookies + ";" + temp;
				}
				count++;
			}
		}
		result = dump(entity);
		cookieGetMethod.abort();
		// System.out.println("ptuiCB('0','0','http://t.qq.com','1','login successful', 'moreAndMore');");
		System.out.println(result);
		if (result.contains("登录成功")) {
			return cookies;
		} else {
			return null;
		}
	}

	/**
	 * 打印页面
	 * 
	 * @param entity
	 * @throws IOException
	 */
	private String dump(HttpEntity entity) {
		BufferedReader br = null;
		StringBuilder sb = null;
		try {
			br = new BufferedReader(new InputStreamReader(entity.getContent(),
					"utf-8"));
			sb = new StringBuilder();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 打印页面
	 * 
	 * @param entity
	 * @throws IOException
	 */
	private String dumpGZIP(HttpEntity entity) {
		BufferedReader br = null;
		StringBuilder sb = null;
		try {
			br = new BufferedReader(new InputStreamReader(new GZIPInputStream(
					entity.getContent()), "GBK"));
			sb = new StringBuilder();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public static void main(String[] args) throws ClientProtocolException,
			Exception {
		// String qq = "2524459161"; //qq account
		// String password = "*******"; //qq passsword
		String qq = "623143506"; // qq account
		String password = "zhou1234**"; // qq passsword

		LoginPojo loginPojo = new LoginPojo(qq, password);
		GetUserCookie4QQ userCookie = new GetUserCookie4QQ(loginPojo);
		String cookie = userCookie.getCookies();

		System.out.println("qq cookie--" + cookie);
	}

}
