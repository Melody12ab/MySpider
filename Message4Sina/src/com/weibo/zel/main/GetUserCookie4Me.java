package com.weibo.zel.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.security.core.codec.Base64;

import com.weibo.zel.entity.util.LoginPojo;
import com.weibo.zel.utils.sina.MyHttpConnectionManager;
import com.weibo.zel.utils.sina.PasswordUtil4Sina;
import com.weibo.zel.utils.sina.RegexPaserUtil;
import com.weibo.zel.utils.sina.SystemParas;

public class GetUserCookie4Me {
	// 日志处理
	private static Logger logger = Logger.getLogger(GetUserCookie4Me.class);
	private LoginPojo loginPojo;

	public LoginPojo getLoginPojo() {
		return loginPojo;
	}

	public void setLoginPojo(LoginPojo loginPojo) {
		this.loginPojo = loginPojo;
	}

	String servertime = null;
	String nonce = null;
	String rsaKV = null;
	String pubKey = null;
	String pcid = null;
	public boolean isLogin = false;
	public long loginTime = 0;
	String verify_pic_url = null;
	private DefaultHttpClient client = null;

	public HttpClient getClient() {
		return client;
	}

	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}

	public GetUserCookie4Me(LoginPojo loginPojo) {
		this.loginPojo = loginPojo;
		this.client = MyHttpConnectionManager.getNewHttpClient();
	}

	@Override
	public String toString() {
		return "LoginID [client=" + client + ", isLogin=" + isLogin
				+ ", loginTime=" + loginTime + ", nonce=" + nonce + ", pwd="
				+ loginPojo.getPassword() + ", servertime=" + servertime
				+ ", user=" + loginPojo.getUsername() + "]";
	}

	public static int random = 1000000;

	// sso login status的时间,以字符串存储
	public static String SSOLoginState = "";
	// 在prelogin.php得到，之后的请求一直携带，并加入cookies
	public static String cookies_utrs = "";
	// 为最后的cookies设置sus
	public static String cookies_sus = "";

	public void init(boolean isSecond) throws Exception {
		isLogin = false;
		HttpGet hg = new HttpGet(
				"http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.11)&_="
						+ (new Date()).getTime());

		HttpResponse response1 = client.execute(hg);

		HttpEntity entity1 = response1.getEntity();

		String result1 = dump(entity1, SystemParas.default_encoding);
		hg.abort();
		RegexPaserUtil ap = new RegexPaserUtil("servertime\":", ",", result1,
				RegexPaserUtil.TEXTTEGEX);
		servertime = ap.getText();
		ap = new RegexPaserUtil("nonce\":\"", "\"", result1,
				RegexPaserUtil.TEXTTEGEX);
		nonce = ap.getText();
		ap = new RegexPaserUtil("pubkey\":\"", "\"", result1,
				RegexPaserUtil.TEXTTEGEX);
		pubKey = ap.getText();
		ap = new RegexPaserUtil("rsakv\":\"", "\"", result1,
				RegexPaserUtil.TEXTTEGEX);
		rsaKV = ap.getText();
		ap = new RegexPaserUtil("pcid\":\"", "\"", result1,
				RegexPaserUtil.TEXTTEGEX);
		pcid = ap.getText();

		if (isSecond) {
			// 获取验证码地址
			verify_pic_url = "http://login.sina.com.cn/cgi/pin.php?r="
					+ (++random) + "&s=0&p=" + pcid;
		}
	}

	// 得到最终cookie的入口,其中包括了循环获取、验证码识别与人工打码通过
	public String getCookies() throws Exception {
		String cookie = getCookies_2014(false);
		int count = 0;
		if (cookie == null || cookie.isEmpty()) {
			logger.info("首次获取cookie失败!");
			while ((++count) < SystemParas.verify_max_error_time) {
				cookie = getCookies_2014(true);
				if (cookie == null || cookie.isEmpty()) {
					logger.info("第" + count + "次,带验证码获取cookie失败!");
				} else {
					logger.info("第" + count + "次,带验证码获取cookie成功!");
					break;
				}
				// 休息1s種
				logger.info("该次获取cookies失败，休息一段时间继续请求!");
				Thread.sleep(2000);
			}
		} else {
//			logger.info("首次获取cookie成功!");
		}
		return cookie;
	}

	public String getCookies_2014(boolean isSecond)
			throws Exception {
		init(isSecond);
		// 开始登陆
		HttpPost post = new HttpPost(
				"http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.11)");

		// 网页头信息
		post.setHeader("Accept", "textml, application/xhtml+xml, */*");
		post.setHeader("Referer", "http://weibo.com/");
		post.setHeader("Accept-Language", "zh-CN");
		post.setHeader("Connection", "Keep-Alive");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("Host", "login.sina.com.cn");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE " + "9.0"
				+ "; Windows NT 6.1; WOW64; Trident/6.0)");
		post.setHeader("Cookie", cookies_utrs);

		// 登陆表单信息
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("encoding", "UTF-8"));
		qparams.add(new BasicNameValuePair("entry", "weibo"));
		qparams.add(new BasicNameValuePair("from", ""));
		qparams.add(new BasicNameValuePair("gateway", "1"));
		qparams.add(new BasicNameValuePair("nonce", nonce));
		qparams.add(new BasicNameValuePair("prelt", "102"));// 随便写的
		qparams.add(new BasicNameValuePair("pwencode", "rsa2"));
		qparams.add(new BasicNameValuePair("returntype", "META"));
		qparams.add(new BasicNameValuePair("rsakv", rsaKV));
		qparams.add(new BasicNameValuePair("savestate", "0"));
		qparams.add(new BasicNameValuePair("servertime", servertime));
		qparams.add(new BasicNameValuePair("service", "miniblog"));
		qparams.add(new BasicNameValuePair("vsnf", "1"));
		qparams.add(new BasicNameValuePair(
				"url",
				"http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
		qparams.add(new BasicNameValuePair("useticket", "1"));
		qparams.add(new BasicNameValuePair("pagerefer", ""));
		qparams.add(new BasicNameValuePair("su", getUserNameEncode()));
		qparams.add(new BasicNameValuePair("sp", getTimeAndNonceEnc(pubKey,
				servertime, nonce)));

		UrlEncodedFormEntity params = new UrlEncodedFormEntity(qparams, "UTF-8");

		post.setEntity(params);

		HttpResponse response = client.execute(post);

		HttpEntity entity = response.getEntity();
		// 取得登陆第一次正文
		 String content = dumpGZIP(entity);
//		 System.out.println("第一次取得的content---" + content);
		/**
		 * 取出跳转的url 对该content取得其跳转时的链接，设置相应的cookie从而取得其跳转后的cookies作为最后的cookie
		 */
		String location = getRedirectLocation(content);
		// 存储最后的登陆cookies
		String cookies = "";

		// 取得第一次时的cookie,最后与第二次获得的cookie去做与操作
		if (location.contains("retcode=0")) {
			String temp = "";
			int count = 1;
			org.apache.http.Header[] h = response.getAllHeaders();
			for (int i = 0; i < h.length; i++) {
				if (h[i].getName().contains("Set-Cookie")) {
					// System.out.println("第一次---header[" + i + "]------"
					// +h[i]);
					temp = h[i].getValue();
					// 此处为要获取SSOLoginState,此值等LT的值
					if (temp.contains("LT=")) {
						String begin = "LT=";
						String end = ";";

						RegexPaserUtil regexPaserUtil = new RegexPaserUtil(
								begin, end, RegexPaserUtil.TEXTEGEXANDNRT);
						regexPaserUtil.reset(temp);

						SSOLoginState = regexPaserUtil.getText();
					} else if (temp.contains("SUS=")) {
						// 处理SUS值
						String begin = "SUS=";
						String end = ";";

						RegexPaserUtil regexPaserUtil = new RegexPaserUtil(
								begin, end, RegexPaserUtil.TEXTEGEXANDNRT);
						regexPaserUtil.reset(temp);

						cookies_sus = regexPaserUtil.getText();
					}
					// cookie是键值对，此处的";"一定要加上
					if (count == 1) {
						cookies = cookies + temp;
					} else {
						cookies = cookies + ";" + temp;
					}
					count++;
				}
			}
			cookies = cookies.replace("path=/;", "").replace("Httponly", "")
					.replaceAll("domain=.sina.com.cn;", "")
					.replace("httponly", "").replace("path=/", "")
					.replace("domain=.sina.com.cn", "")
					.replaceAll("expires.*?;", "")
					.replace("domain=login.sina.com.cn", "").replace(" ", "")
					+ ";wvr=5; un=" + loginPojo.getUsername();
			cookies = cookies + ";myuid=" + loginPojo.getUid();// 添加的myUid是为解决验证码
			cookies = cookies
					+ ";SinaRot_wb_r_topic=39;UV5PAGE=usr513_90; UV5=usr319_182;";// 添加的SinaRot_wb_r_topic为解决话题内容content的抓取
			cookies = cookies + "SSOLoginState=" + SSOLoginState;

//			logger.info("第一次得到的cookie------------------------" + cookies);
		}

		// 取得lcoation后直接中断前链接即可， abort掉其它的部分,会省掉一些资源
		post.abort();
		// System.out.println("得到的location url---" + location);
		HttpGet getReq = new HttpGet(location);
		// http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.11)
		getReq.setHeader("Referer",
				"http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.11)");
		getReq.setHeader("Cookie", cookies);

		// 不让自动跳转,在这里主要是为了截取到跳转中的cookies
		MyHttpConnectionManager.setHandleRedirect(client, false);
		// MyHttpConnectionManager.setHandleRedirect(client, true);
		response = client.execute(getReq);

		// this.client=MyHttpConnectionManager.getHttpClient();
		// 恢復成htmlclient自行管理請求轉向
		MyHttpConnectionManager.setHandleRedirect(client, true);

		// 清空最上一次的cookies,得到最后返回的全部的cookies
		cookies = "";
		if (location.contains("retcode=0")) {
			String temp = "";
			int count = 1;
			org.apache.http.Header[] h = response.getAllHeaders();
			for (int i = 0; i < h.length; i++) {
//				 System.out.println("最终cookie获得的302请求--header[" + i +"]------"+ h[i]);
				if (h[i].getName().contains("Set-Cookie")) {
					temp = h[i].getValue();
					// 此处为要获取SSOLoginState,此值等LT的值
					if (temp.contains("SRF=")) {
						String begin = "SRF=";
						String end = ";";
						RegexPaserUtil ansjParser = new RegexPaserUtil(begin,
								end, RegexPaserUtil.TEXTEGEXANDNRT);
						ansjParser.reset(temp);
						SSOLoginState = ansjParser.getText();
					}
					// cookie是键值对，此处的";"一定要加上
					if (count == 1) {
						cookies = cookies + temp;
					} else {
						cookies = cookies + ";" + temp;
					}
					count++;
				}
			}
			cookies = cookies.replace("path=/;", "").replace("Httponly", "")
					.replaceAll("domain=.sina.com.cn;", "")
					.replace("httponly", "").replace("path=/", "")
					.replace("domain=.sina.com.cn", "")
					.replaceAll("expires.*?;", "")
					.replaceAll("domain=.weibo.com;", "")
					.replace("domain=login.sina.com.cn", "").replace(" ", "");
			// 最后添加的后补参数
			if ((cookies != null && cookies.length() > 0)) {
				cookies = cookies + ";myuid=" + loginPojo.getUid();// 添加的myUid是为解决验证码
				cookies = cookies
						+ ";SinaRot_wb_r_topic=39;UV5PAGE=usr511_179; UV5=usr319_182;";// 添加的SinaRot_wb_r_topic为解决话题内容content的抓取
				// UUG和SSOLoginState有時在最后返回的cookies中，有時也不在里邊，在此追加一下各參數
				cookies = cookies + ";SSOLoginState=" + SSOLoginState;
				cookies = cookies + ";UOR=,,login.sina.com.cn;";
				cookies = cookies + ";_s_tentry=login.sina.com.cn;";
			}
			// 中断上个get请求
			getReq.abort();
//			logger.info("user cookie------------------------" + cookies);
			return cookies;
		} else if (location.contains("retcode=4049")) {
			logger.info(loginPojo.getUsername() + "-->异地登陆失败,需要验证码!");
			return null;
		} else if (location.contains("retcode=5")) {
			logger.info(loginPojo.getUsername() + "-->异地登陆失败,用户名不存在!");
			return null;
		} else if (location.contains("retcode=2070")) {
			logger.info(loginPojo.getUsername() + "-->异地登陆失败,验证码输入错误!");
			return null;
		} else {
			logger.info(loginPojo.getUsername() + "-->登陆失败,用户名或密码错误!");
			return null;
		}
	}

	private String getRedirectLocation(String content) {
		String regex = "location\\.replace\\([',\"](.*?)[',\"]\\)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		String location = null;
		if (matcher.find()) {
			location = matcher.group(1);
		}
		return location;
	}

	private String dump(HttpEntity entity, String encoding) {
		BufferedReader br = null;
		StringBuilder sb = null;
		try {
			br = new BufferedReader(new InputStreamReader(entity.getContent(),
					encoding));
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

	// 得到用户名的加密结果
	public String getUserNameEncode() throws UnsupportedEncodingException {
		byte[] encoded = Base64.encode(URLEncoder.encode(
				loginPojo.getUsername(), "UTF-8").getBytes());
		String username = new String(encoded);
		// System.out.println("加密后的密文是："+username);
		return username;
	}

	// 得到密码经加密后的密文
	public String getTimeAndNonceEnc(String pubkey, String serverTime,
			String nonce) {
		String realPwd = PasswordUtil4Sina.getPassEncoding(pubKey, serverTime,
				nonce, loginPojo.getPassword());
		// System.out.println("加密后的密码是---" + realPwd);
		return realPwd;
	}

	public boolean pageValidate(String str) {
		if (str.contains("你当前使用的帐号异常")
				|| str.contains("加入新浪微博支持我吧！分享我的最新动向，与我一对一交流哦！")
				|| str.contains("快速开通微博") || str.contains("2.等待验证成功提示")
				|| str.contains("你所访问的用户不存在")
				|| str.contains("抱歉，你的帐号存在异常，暂时无法访问")) {
			isLogin = false;
			return false;
		}

		return true;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public static void main(String[] args) throws ClientProtocolException,
			Exception {
		// 对登陆微博的帐号、密码、uid的设置
		LoginPojo loginPojo = new LoginPojo();

		loginPojo.setUsername("mathmelody@sina.com");
		loginPojo.setPassword("zx8868108//");
		loginPojo.setUid("5311414858");

		GetUserCookie4Me cookieUtil = new GetUserCookie4Me(loginPojo);

		String cookieString = cookieUtil.getCookies();
		if (cookieString == null || cookieString.isEmpty()) {
			System.out.println("最終得到的cookies為空，請檢查!");
		} else {
			System.out.println("cookieString---" + cookieString);
		}

	}
}
