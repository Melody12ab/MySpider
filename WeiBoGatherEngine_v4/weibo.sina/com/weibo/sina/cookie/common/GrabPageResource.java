package com.weibo.sina.cookie.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.util.LoginPojo;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.util.DateUtil;
import com.weibo.common.GUI.verify.VerifyGuiUtil;
import com.weibo.common.GUI.verify.iface.IVerifyElement;
import com.weibo.common.utils.MyHttpConnectionManager;
import com.weibo.common.utils.StaticValue;
import com.weibo.sina.cookie.media.GrabMediaPersonInfo;

@Component
@Scope(value = "prototype")
public class GrabPageResource {
	private static final Logger logger = Logger
			.getLogger(GrabPageResource.class);
	private String cookieString;

	public String getCookieString() {
		return cookieString;
	}

	public void setCookieString(String cookieString) {
		this.cookieString = cookieString;
	}

	private String original_cookieString;

	public GrabPageResource() {

	}

	HttpGet get = null;
	private HttpClient httpClient = null;

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public HttpResponse getHttpResponse(String url, String referer)
			throws Exception {
		// 发送一个get请求
		get = new HttpGet(url);
		// 网页头信息
		get
				.setHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");
		if (url.contains("s.weibo.com")) {
			get.setHeader("Host", "s.weibo.com");
		} else if (url.contains("e.weibo.com")) {
			get.setHeader("Host", "e.weibo.com");
		} else if (url.contains("media.weibo.com")) {
			get.setHeader("Host", "media.weibo.com");
		} else if (url.contains("huati.weibo.com")) {
			get.setHeader("Host", "huati.weibo.com");
			// 话题请求为ajax请求，故在此必须设置
			get.setHeader("X-Requested-With", "XMLHttpRequest");
			get.setHeader("Content-Type", "application/x-www-form-urlencoded");
		} else {
			get.setHeader("Host", "www.weibo.com");
		}
		get.setHeader("Cookie", cookieString);
		get.setHeader("Connection", "Keep-Alive");
		get.setHeader("Accept", "text/html, application/xhtml+xml, */*");

		if (referer != null) {
			get.setHeader("Referer", referer);
		}
		return httpClient.execute(get);
	}

	public HttpResponse getHttpResponse(String url, String referer,
			boolean isAjax) throws Exception {
		// 发送一个get请求
		get = new HttpGet(url);
		// 网页头信息
		get
				.setHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");
		if (url.contains("s.weibo.com")) {
			get.setHeader("Host", "s.weibo.com");
		} else if (url.contains("e.weibo.com")) {
			get.setHeader("Host", "e.weibo.com");
		} else if (url.contains("media.weibo.com")) {
			get.setHeader("Host", "media.weibo.com");
		} else if (url.contains("huati.weibo.com")) {
			get.setHeader("Host", "huati.weibo.com");
			// 话题请求为ajax请求，故在此必须设置
			get.setHeader("X-Requested-With", "XMLHttpRequest");
			get.setHeader("Content-Type", "application/x-www-form-urlencoded");
		} else {
			get.setHeader("Host", "www.weibo.com");
		}
		if (isAjax) {
			get.setHeader("X-Requested-With", "XMLHttpRequest");
		}
		get.setHeader("Cookie", cookieString);
		get.setHeader("Connection", "Keep-Alive");
		get.setHeader("Accept", "text/html, application/xhtml+xml, */*");

		if (referer != null) {
			get.setHeader("Referer", referer);
		}
		return httpClient.execute(get);
	}

	boolean flag = true;
	String ss = null;

	public String getPageSourceOfSina(String url, String referer)
			throws SocketTimeoutException {
		ss = null;
		if (flag) {
			InputStream urlStream = null;
			try {
				HttpResponse response = getHttpResponse(url, referer);
				urlStream = response.getEntity().getContent();
				// 创建一个使用默认大小输入缓冲区的缓冲字符输入流
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlStream, "UTF-8"));
				String line;
				// 读取网页内容
				StringBuilder pageBuffer = new StringBuilder();// 线程安全的可变字符序列
				while ((line = reader.readLine()) != null) {
					pageBuffer.append(line);
				}
				ss = pageBuffer.toString();
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				// return null;
				throw e;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (InterruptedException e) {// 线程睡眠
				e.printStackTrace();
				return null;
			} catch (Exception e) {// 线程睡眠
				e.printStackTrace();
				return null;
			} finally {
				if (urlStream != null) {
					try {
						urlStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}// 网络流
				}
			}
		}
		return ss;
	}

	/**
	 * 只抛出超时链接，有可能是因为短网等引起，给一定的修复时间
	 * 
	 * @param url
	 * @param referer
	 * @param isAjax
	 * @return
	 * @throws SocketTimeoutException
	 */
	public String getPageSourceOfSina(String url, String referer, boolean isAjax)
			throws Exception {
		ss = null;
		if (flag) {
			InputStream urlStream = null;
			try {
				HttpResponse response = getHttpResponse(url, referer, isAjax);
				urlStream = response.getEntity().getContent();
				// 创建一个使用默认大小输入缓冲区的缓冲字符输入流
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlStream, "UTF-8"));
				String line;
				// 读取网页内容
				StringBuilder pageBuffer = new StringBuilder();// 线程安全的可变字符序列
				while ((line = reader.readLine()) != null) {
					pageBuffer.append(line);
				}
				ss = pageBuffer.toString();
			} catch (Exception e) {
				// e.printStackTrace();
				throw e;
				// return null;
			} finally {
				if (urlStream != null) {
					try {
						urlStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}// 网络流
				}
			}
		}
		return ss;
	}

	private boolean validate(String content) {
		if (content
				.contains("location.replace(\"http://weibo.com/aj/mblog/mbloglist?")
				&& content.contains("&retcode=6102\");")) {
			// cookies失效需处理
			return false;
		}
		if (content.contains("location.replace(\"http://weibo.com/")
				&& content.contains("follow?retcode=6102\");")) {
			// cookies失效需处理
			return false;
		}
		if (content.contains("location.replace(\"http://weibo.com/")
				&& content.contains("&retcode=6102\");")) {
			// cookies失效需处理
			return false;
		}
		if (content.contains("location.replace(\"http://s.weibo.com/")
				&& content.contains("?retcode=6102\");")) {
			// cookies失效需处理
			return false;
		}

		if (content.contains("/info/?retcode=6102")) {
			// cookies失效需处理
			return false;
		}
		if (content.contains("http://i.sso.sina.com.cn/js/ssologin.js")) {
			// cookies处于半失效状态
			return false;
		}
		if (content.contains("/weibo.com/sso/login.php")
				&& content.contains("&retcode=0")) {
			return false;
		}
		return true;
	}

	public void init(LoginPojo loginPojo) {
		this.cookieString = loginPojo.getCookie();
		original_cookieString = this.cookieString;
		this.httpClient = loginPojo.getHttpClient();
	}

	public static String dumpGZIP(HttpEntity entity) {
		BufferedReader br = null;
		StringBuilder sb = null;
		try {
			br = new BufferedReader(new InputStreamReader(entity.getContent(),
					"GBK"));
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

	// 带有cookies的用get方式，下载微博平台的一个验证图片
	public void download(String url, String fileFullName, String searchUrl) {
		// 发送一个get请求
		HttpGet downFileGetMethod = new HttpGet(url);
		// 网页头信息
		downFileGetMethod.setHeader("Host", "s.weibo.com");
		downFileGetMethod.setHeader("Cookie", cookieString);
		downFileGetMethod.setHeader("Connection", "Keep-Alive");
		downFileGetMethod.setHeader("Accept",
				"image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
		downFileGetMethod.setHeader("Referer", searchUrl);
		downFileGetMethod.setHeader("Accept-Encoding", "gzip, deflate");
		downFileGetMethod.setHeader("User-Agent",
				"Mozilla/5.0 (compatible; MSIE " + "9.0"
						+ "; Windows NT 6.1; WOW64; Trident/6.0)");
		// logger.info("正在下载---" + url);
		System.out.println("正在下载---" + url);
		try {
			HttpResponse response = httpClient.execute(downFileGetMethod);

			String temp = "";
			org.apache.http.Header[] h = response.getAllHeaders();
			for (int i = 0; i < h.length; i++) {
				// System.out.println("header[" + i + "]------" + h[i]);
				if (h[i].getName().contains("Set-Cookie")) {
					temp = h[i].getValue();
				}
			}
			this.cookieString = this.original_cookieString + ";" + temp + ";";

			byte[] buffer = new byte[1024 * 8];
			int read;
			File localFile = new File(fileFullName);
			localFile = localFile.getParentFile();
			if (localFile != null) {
				localFile.mkdirs();
			}
			BufferedInputStream bin = new BufferedInputStream(response
					.getEntity().getContent());
			BufferedOutputStream bout = new BufferedOutputStream(
					new FileOutputStream(fileFullName));
			while ((read = bin.read(buffer)) > -1) {
				bout.write(buffer, 0, read);
			}
			bout.flush();
			bout.close();
			// logger.info("完成下载---" + url);
			System.out.println("完成下载---" + url);
		} catch (Exception e) {
			// logger.info("下载验证码图片时出现错误!" + e.getLocalizedMessage());
			System.out.println("下载验证码图片时出现错误!" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			downFileGetMethod.abort();
		}
	}

	private long temp_timestamp = 0;

	public boolean isValidVerifyCode(String timeStamp, String searchUrl,
			VerifyGuiUtil verifyGuiUtil, IVerifyElement verifyElement) {
		String verifyCode = null;
		MyJson myJson = new MyJson();
		String result = null;
		int count = 0;
		while (count < StaticValue.verify_max_error_time) {
			/**
			 * 因为要用到cookie等参数，故在此处实现下载
			 */
			// 关键词抓取时，需要实时下载验证图片
			verifyElement.setVerify_keyword_pic_index(verifyElement
					.getVerify_keyword_pic_index() + 1);
			if (count == 0) {
				this.download(
						"http://s.weibo.com/ajax/pincode/pin?type=sass&ts="
								+ timeStamp, "temp_pic/"
								+ verifyElement.getCurrent_main_thread_name()
								+ (verifyElement.getVerify_keyword_pic_index())
								+ ".png", searchUrl);
			} else if (count == 1) {
				temp_timestamp = DateUtil.getLongByDate();
				this.download("http://s.weibo.com/ajax/pincode/pin?type=sass"
						+ temp_timestamp + "10", "temp_pic/"
						+ verifyElement.getCurrent_main_thread_name()
						+ (verifyElement.getVerify_keyword_pic_index())
						+ ".png", searchUrl);
			} else {
				this.download("http://s.weibo.com/ajax/pincode/pin?type=sass"
						+ (temp_timestamp) + "1" + (count - 1), "temp_pic/"
						+ verifyElement.getCurrent_main_thread_name()
						+ (verifyElement.getVerify_keyword_pic_index())
						+ ".png", searchUrl);
			}

			verifyCode = verifyGuiUtil.getVerifyCode("keyword", verifyElement,
					timeStamp, count);
			// System.out.println("verifyCode---" + verifyCode);
			result = getVerifyResult(verifyCode, searchUrl);
			myJson.createJson(result);
			if (myJson.getStringByKey("code").equals("100000")) {
				// 说明验证成功
				return true;
			}
			count++;
		}
		return false;
	}

	public String getVerifyResult(String verifyCode, String searchURL) {
		// this.httpClient = MyHttpConnectionManager.getHttpClient();
		HttpPost post = new HttpPost(
				"http://s.weibo.com/ajax/pincode/verified?__rnd="
						+ DateUtil.getLongByDate());
		// 网页头信息
		post.setHeader("Accept", "*/*");
		post.setHeader("Accept-Language", "zh-CN");
		post.setHeader("Cache-Control", "no-cache");
		post.setHeader("Connection", "Keep-Alive");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("Referer", searchURL);
		post.setHeader("x-requested-with", "XMLHttpRequest");
		post.setHeader("Host", "s.weibo.com");
		post.setHeader("Cookie", cookieString);
		post.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE " + "9.0"
				+ "; Windows NT 6.1; WOW64; Trident/6.0)");
		// 网页头信息--结束

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("_t", "0"));
		qparams.add(new BasicNameValuePair("pageid", "weibo"));
		qparams.add(new BasicNameValuePair("secode", verifyCode));
		qparams.add(new BasicNameValuePair("type", "sass"));

		UrlEncodedFormEntity params = null;
		try {
			params = new UrlEncodedFormEntity(qparams, "UTF-8");
			post.setEntity(params);
			HttpResponse response = this.httpClient.execute(post);
			HttpEntity entity = response.getEntity();

			// 取得登陆第一次正文
			String content = dumpGZIP(entity);
			// System.out.println("keyword search content----" + content);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		GrabPageResource grabPageSource = new GrabPageResource();// 抓取网页源码
		grabPageSource.httpClient = MyHttpConnectionManager.getHttpClient();// 初始化httpclient

		grabPageSource.cookieString = "SUS=SID-3057467163-1420757721-GZ-0a6mr-f4c143bf50277fc1022da03b97f5bf0c; path=/; domain=.weibo.com;SUS=SID-3057467163-1420757721-GZ-0a6mr-f4c143bf50277fc1022da03b97f5bf0c; path=/; domain=.weibo.com; httponly;SUE=es%3Df31918f723811471473f1caf2ad7d807%26ev%3Dv1%26es2%3Dcc5a9d19d509a64d4ab33329dc240359%26rs0%3DjfK1sA8mBQK14Y2HX7T4ndpoadcx95DyOdQA7LODYR2J5i8DNK4afhUYNW%252B6nLoGiKnieetUwhPRi7ibyQ34teXGN8v%252Botg%252Ft3QW2M3I6yT6bucO2WkxNWe7KuG8QBPiU9o6rqkcCDZ9EllErSNhS99rD%252FvBXgdXqM0paaezK2k%253D%26rv%3D0;path=/;domain=.weibo.com;Httponly;SUP=cv%3D1%26bt%3D1420757721%26et%3D1420844121%26d%3Dc909%26i%3Dbf0c%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D0%26uid%3D3057467163%26name%3Dda04xiang%2540126.com%26nick%3Dda04xiang%26fmp%3D%26lcp%3D;path=/;domain=.weibo.com;SUB=_2A255q3qJDeTxGeVO7lUV9inNzT-IHXVawBrBrDV8PUNbuNBeLVf1kW9XwwODXIr7Lx3esjkZ8-wVKiw5eg..; path=/; domain=.weibo.com; httponly;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWwqiSB0x6ks32W94_jMKZl5JpX5K2t; expires=Friday, 08-Jan-16 22:55:21 GMT; path=/; domain=.weibo.com;SRT=E.vAfqJD43iDR3JORqveWYInmBvXvCvXM4hnhSvnEABvzvvv4mT8a2eAVmPIFk-AfCvvAivOmKvAmLvAmMvXvC*B.vAflW-P9Rc0lR-ykADvnJqiQVbiRVPBtS!r3JZPQVqbgVdWiMZ4siOzu4DbmKPWQW3E94rzn5DShVqJpPq!rQ-9iNs9ji49ndDPIJeA7; expires=Sunday, 05-Jan-25 22:55:21 GMT; path=/; domain=.passport.weibo.com; httponly;SRF=1420757721; expires=Sunday, 05-Jan-25 22:55:21 GMT; path=/; domain=.passport.weibo.com;ALF=1452293720; expires=Fri, 08-Jan-2016 22:55:20 GMT; path=/; domain=.weibo.com;SSOLoginState=1420757721; path=/; domain=.weibo.com;myuid=3057467163;SinaRot_wb_r_topic=39;UV5PAGE=usr511_179;; UV5=usr319_182;;SSOLoginState=1420757720;;UOR=,,login.sina.com.cn;;_s_tentry=login.sina.com.cn;YF-Page-G0=19f6802eb103b391998cb31325aed3bc;";

		// System.out.println(grabPageSource.getPageSourceOfSina("http://www.weibo.com/",
		// "http://www.weibo.com/"));
		// if(true){
		// return;
		// }

		PersonInfo person = new PersonInfo();
		// String targetURL = "http://www.weibo.com/leehom";
		// String uid = "2827584527";// 测试现在帐户抓取的可用性
		// String uid = "renzhiqiang";
		// String uid = "2827584527";// imath
		// String uid = "jczmdeveloper";// 小明nick
		// String uid = "515581258";// 小明uid
		// String uid = "2613974171";// 高老师uid
		// String uid = "1774978073";// 关注人数4人

		/**
		 * 普通博主已在上边部分测试过
		 */

		/**
		 * 名人堂--成员
		 */
		// String uid = "1266321801";// 姚晨uid
		// String uid = "yaochen";// 姚晨uid
		// String uid = "leijun";// 雷军nick

		/**
		 * 媒体版
		 */
		// String uid = "qlwb";// 齐鲁晚报nick
		// String uid = "bjylxb";// 信报nick
		String uid = "523499686";// 三峡都市报微博uid

		/**
		 * 品牌板
		 */
		// String uid = "googlev";// Google黑板报nick
		// String uid = "chinaamc1998";// Google黑板报nick
		// String uid = "2401679590";// Google黑板报nick

		/**
		 * 政务版
		 */
		// String uid = "mtgjd";// 门头沟禁毒
		// String uid = "3446379482";// 房山工商
		// String uid = "3252194525";// 房山工商
		/**
		 * 校园版
		 */
		// String uid = "2806927712";// 中国传媒大学凤凰学院
		// String uid = "hfut1945";// 合肥工业大学
		// String uid = "1934691193";// 重庆邮电大学
		/**
		 * 机构版
		 */
		// String uid = "1933895942";// 宝贝回家,不合法的uid
		// String uid = "shdrama";// 上海话剧艺术中心
		// String uid = "1983327567";// 杭州绿城
		// String uid = "officialasroma";// 罗马足球俱乐部Roma
		/**
		 * 网站版,暂不作为重点，故先忽略
		 */
		// String uid = "hoopchina";// 罗马足球俱乐部Roma

		// String uid = "1687870094";// 任意个人版
		// String uid = "1182389073";// 任志强
		// String uid = "kaifulee";// 媒体版,新华视点
		// String uid = "1803558593";// 任意uid
		// String uid = "1793285524";// 力宏
		// String uid = "1195242865"; // 杨幂
		// String uid = "weixiaoshuo";// 微小说
		// String uid = "kaifulee";// 李开复
		// String uid = "shzjxwzx";// 上海质监局
		// String uid = "2270166931";// 企业版--云端

		// String uid = "jgwbxh2012";// 企业版
		// String uid = "1990303441";// 企业版,锦江之星
		// String uid = "2675964001";// 企业版,花猫食堂
		// String uid = "1553416024";// 网站版，暂自定义为媒体牌，主要因为其模板是以前的媒体版,以海力网为例

		// String uid = "1797507374";// 专业版
		// String uid = "bianjingfengyun";// 专业版
		// String uid = "1197161814/zo7KMFiUC";
		// String uid = "1197161814/zohb6Af1D";
		// String uid = "1837086667/znJaeCxwj";//媒体版
		// String uid = "1889339641/zmVsOjxup";// 专业版
		// String uid = "1653689003/zogWzkhHF";// 专业版
		// String uid = "1977610261/zo892r6Tf";// 企业版
		// String uid = "1726390450/zhj6Abqk0";// 企业版
		// String uid="2518388321";
		// String targetURL = StaticValue.Person_Info_Req_URL.replace("${uid}",
		// uid);
		// String targetURL = "http://www.weibo.com/jczmdeveloper";
		// String targetURL = "http://www.weibo.com/u/" + uid;
		String targetURL = "http://www.weibo.com/" + uid;
		// String targetURL = "http://e.weibo.com/" + uid;
		// UrlPojo pojo = new UrlPojo(targetURL, 1);
		UrlPojo pojo = new UrlPojo(targetURL, 2);
		/**
		 * 得到微博个人版页面源码
		 */
		// String htmlSource = grabPageSource.getPageSourceOfSina(targetURL,
		// null);
		// 通过文件读取，不再耗网络流量，会更快些
		// String htmlSource = IOUtil.readFile("D:/test.txt");
		// System.out.println("得到的源码是------------" + htmlSource);
		/**
		 * 新浪新版微博，帐户信息的抓取
		 */
		// GrabPersonInfoV2 grabPersonInfoV2 = new GrabPersonInfoV2();
		// grabPersonInfoV2.init(person, grabPageSource, pojo);
		// grabPersonInfoV2.savePersonInfo();

		/**
		 * 新浪新版微博，微博内容信息的抓取
		 */
		// GrabPersonInfoV2 grabPersonInfoV2 = new GrabPersonInfoV2();
		// grabPersonInfoV2.init(person, grabPageSource, pojo);
		// person = grabPersonInfoV2.getPersonHead();
		//		
		// GrabContentInfo grabContentInfo = new GrabContentInfo();
		// grabContentInfo.init(person, grabPageSource, pojo, null);
		// grabContentInfo.grabArticleContents4All();
		/**
		 * 新浪新浪微博，关注抓取--旧版
		 */
		// GrabAttentionInfo grabAttentionInfo = new GrabAttentionInfo();
		// grabAttentionInfo.init(uid, grabPageSource, pojo);
		// grabAttentionInfo.savePersonAttentionInfo();
		/**
		 * 新浪新浪微博，关注抓取--新版
		 */
		// String domain_id = "100505";
		// String domain_id = "100306";
		// // String domain_id="103505";
		// GrabAttentionInfoV2 grabAttentionInfo = new GrabAttentionInfoV2();
		// grabAttentionInfo.init(domain_id, uid, grabPageSource, pojo);
		// grabAttentionInfo.savePersonAttentionInfo();

		/**
		 * 新浪新浪微博，关注抓取--新版
		 */
		// String domain_id = "100505";
		// String domain_id = "103505";
		// String domain_id = "100306";
		// GrabFansInfo grabFansInfo = new GrabFansInfo();
		// grabFansInfo.init(domain_id, uid, grabPageSource, pojo);
		// grabFansInfo.savePersonFansInfo();

		/**
		 * 得到微博媒体版页面源码
		 */
		// grabPageSource.cookieString =null;
		// String oid = "1837086667";
		// String rnd = Math.random() + "";
		/**
		 * 这是获取帐户个人信息数量的URL
		 */
		// String targetURL =
		// "http://media.weibo.com/mblog/aj_eps_getuserinfo.php?oid=1837086667&rnd=0.6434819642097033";
		/**
		 * 获取媒体版第2页时ajax请求
		 */
		// String targetURL =
		// "http://media.weibo.com/mblog/aj_mymblog.php?page=2&oid=1837086667&pagepreview=0&rnd=0.6812808279900127";
		/**
		 * ajax获取每条微博内容的评论、转发等数量信息
		 */
		// 抓取媒体版的帐户信息
		// GrabMediaPersonInfo grabMediaPersonInfo = new GrabMediaPersonInfo();
		// grabMediaPersonInfo.init(person, grabPageSource, pojo);
		// // grabMediaPersonInfo.saveMediaPersonInfo();
		// grabMediaPersonInfo.saveMediaPersonInfo("v2");

		/*
		 * // 抓取媒体版的内容信息 // GrabMediaContentInfo grabMediaContentInfo = new
		 * GrabMediaContentInfo(); // PersonInfo person = new PersonInfo(); //
		 * person.setUid(oid); // person.setName("微小说"); //
		 * person.setUid("1567443202"); // person.setName("乐市场"); //
		 * grabMediaContentInfo.init(grabPageSource, person, targetURL); //
		 * grabMediaContentInfo.grabArticleContents(new UrlPojo(targetURL, 7));
		 */
		// 抓取企业版的帐户信息
		// GrabEnterPersonInfo grabEnterPersonInfo = new GrabEnterPersonInfo();
		// person = new PersonInfo();
		// pojo = new UrlPojo(targetURL, 1);
		// grabEnterPersonInfo.init(person, grabPageSource, pojo);
		// grabEnterPersonInfo.grabEnterprisePersonInfoPage(htmlSource);
		// grabEnterPersonInfo.getPersonHead();
		// grabEnterPersonInfo.saveEnterprisePersonInfo();

		// 抓取新版企业版帐户信息--V2
		// GrabEnterPersonInfoV2 grabEnterPersonInfoV2 = new
		// GrabEnterPersonInfoV2();
		// grabEnterPersonInfoV2.init(person, grabPageSource, pojo);
		// grabEnterPersonInfoV2.saveEnterPersonInfo();

		// 抓取新版企业版微博信息--V2
		// GrabEnterPersonInfoV2 grabEnterPersonInfoV2 = new
		// GrabEnterPersonInfoV2();
		// grabEnterPersonInfoV2.init(person, grabPageSource, pojo);
		// person = grabEnterPersonInfoV2.getPersonHead();
		//
		// GrabEnterContentInfoV2 grabEnterContentInfoV2 = new
		// GrabEnterContentInfoV2();
		// grabEnterContentInfoV2.init(person, grabPageSource, pojo, null);
		// grabEnterContentInfoV2.grabArticleContents4All();

		// 抓取企业版的微博内容
		// 解析源码中关于数据列表的json串
		// GrabEnterContentInfo grabEnterContentInfo = new
		// GrabEnterContentInfo();
		// person.setUid("1990303441");
		// person.setName("北京中视瑞德文化传媒有限公司");
		// grabEnterContentInfo.init(person, grabPageSource, pojo);
		// grabEnterContentInfo.grabArticleContents(pojo);// 全部抓取

		// 抓取专业版的帐户信息
		// GrabProPersonInfo grabProPersonInfo = new GrabProPersonInfo();
		// grabProPersonInfo.init(person, grabPageSource, pojo);
		// grabProPersonInfo.saveProfessionPersonInfo();

		// 抓取专业版的微博内容
		// 解析源码中关于数据列表的json串
		/*
		 * GrabProContentInfo grabProContentInfo = new GrabProContentInfo();
		 * person.setUid("1797507374"); person.setName("新华网络电视CNC");
		 * grabProContentInfo.init(person, grabPageSource, pojo);
		 * grabProContentInfo.grabArticleContents(pojo);// 全部抓取
		 */
		/**
		 * PersonAndMedia评论信息列表的抓取
		 */
		// GrabCommentInfo4PersonAndMedia grabCommentInfo = new
		// GrabCommentInfo4PersonAndMedia();
		// grabCommentInfo.init(grabPageSource, pojo);
		// grabCommentInfo.grabArticleComments();

		/**
		 * Profession评论信息列表的抓取
		 */
		// GrabCommentInfo4ProAndEnter grabCommentInfo = new
		// GrabCommentInfo4ProAndEnter();
		// grabCommentInfo.init(grabPageSource, pojo);
		// grabCommentInfo.grabArticleComments();

		/**
		 * 政府版--government--微博内容抓取
		 */
		// GrabGovContentInfo grabGovContentInfo = new GrabGovContentInfo();
		// person.setUid("2270166931");
		// person.setName("云端");
		// grabGovContentInfo.init(person, grabPageSource, pojo);
		// grabGovContentInfo.grabArticleContents(pojo);// 全部抓取
		/**
		 * 话题title列表抓取测试
		 */
		// GrabTopicTitle grabTopicInfo = new GrabTopicTitle();
		// grabTopicInfo.init(grabPageSource,null);
		// grabTopicInfo.grabTopicTitleItems();
		/**
		 * 话题具体内容的抓取
		 */
		// GrabTopicContent grabTopicContent = new GrabTopicContent();
		// grabTopicContent.init(grabPageSource);
		// grabTopicContent.grabTopicContentItems(new KeywordPojo("搁浅海豚合影", 0));

		/**
		 * 关键词抓取---在其自己类的GrabSearchInfo
		 */

		/**
		 * cookies测试
		 */

		// String url =
		// "http://www.weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack&sudaref=www.weibo.com";
		// String refer =
		// "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.11)";
		// HttpResponse httpResponse = grabPageSource.getHttpResponse(url,
		// refer);
		// String source = grabPageSource.dumpGZIP(httpResponse.getEntity());
		// System.out.println(source);

		/**
		 * 关于评论的测试
		 */
		// targetURL = "http://weibo.com/1665335994/Axu11bilM";
		// targetURL = "http://weibo.com/1780676151/A94WwtyOg";
		// targetURL = "http://weibo.com/1974808274/AxJszD7Dn";
		// targetURL = "http://weibo.com/1780676151/A1u7j1hX6";
		// pojo = new UrlPojo(targetURL, 1);
		// GrabCommentInfo4PersonAndMedia grabComment = new
		// GrabCommentInfo4PersonAndMedia();
		// grabComment.init(grabPageSource, pojo, null);
		// grabComment.grabArticleComments();
	}
}
