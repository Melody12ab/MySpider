package com.weibo.qq.cookie.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.TruncatedChunkException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import com.weibo.zel.entity.util.LoginPojo;
import com.weibo.zel.utils.qq.MyHttpConnectionManager;

/**
 * 测试获得的cookies是否是正确的
 */
public class GrabPageSource4QQ {
	private static final Logger logger = Logger
			.getLogger(GrabPageSource4QQ.class);
	private String cookieString;

	public GrabPageSource4QQ() {

	}

	HttpGet get = null;
	HttpClient httpClient = null;

	public HttpResponse getHttpResponse(String url, String referer, String host)
			throws Exception {
		// 发送一个get请求
		get = new HttpGet(url);
		// 网页头信息
		get
				.setHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");
		if (host != null) {
			get.setHeader("Host", host);
		} else if (url.contains("t.qq.com")) {
			get.setHeader("Host", "t.qq.com");
		}
		get.setHeader("Cookie", cookieString);
		get.setHeader("Connection", "Keep-Alive");
		get
				.setHeader(
						"Accept",
						"image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/QVOD, application/QVOD, */*");
		if (referer != null) {
			get.setHeader("Referer", referer);
		}
		return httpClient.execute(get);
	}

	boolean flag = true;

	String ss = null;

	public String getPageSourceOfQQ(String url, String referer, String host) {
		if (flag) {
			try {
				HttpResponse response = getHttpResponse(url, referer, host);
				InputStream urlStream = response.getEntity().getContent();
				// 创建一个使用默认大小输入缓冲区的缓冲字符输入流
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlStream, "UTF-8"));
				String line;
				// 读取网页内容
				StringBuilder pageBuffer = new StringBuilder();// 线程安全的可变字符序列
				while ((line = reader.readLine()) != null) {
					pageBuffer.append(line);
				}
				urlStream.close();// 网络流
				ss = pageBuffer.toString();
				// 把验证抓取到的页面内容忽略，直接返回
				// if (validate(ss)) {
				// flag = false;
				// } else {
				// Thread.sleep(5000);
				// }
				// Thread.sleep(1000);
			} catch (SocketTimeoutException e) {
				logger.error(e.getMessage());
				return null;
			} catch (TruncatedChunkException e) {// 线程睡眠
				e.printStackTrace();
				return "网速问题进入一页!";
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (InterruptedException e) {// 线程睡眠
				e.printStackTrace();
				return null;
			} catch (Exception e) {// 线程睡眠
				e.printStackTrace();
				return null;
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
		// httpClient = MyHttpConnectionManager.getHttpClient();
		this.httpClient = loginPojo.getHttpClient();
	}

	public static void main(String[] args) throws Exception {
		GrabPageSource4QQ grabPageSource4QQ = new GrabPageSource4QQ();
		grabPageSource4QQ.httpClient = MyHttpConnectionManager.getHttpClient();
		/**
		 * 登陆部分
		 */
		String qq = null;
		String password = null;

		qq = "2524459161";
		password = "zhouking";
		LoginPojo loginPojo = new LoginPojo(qq, password);

		grabPageSource4QQ.cookieString = "ac=1,021,; uin_cookie=623143506; euin_cookie=B5BE297C0248296D741F27EB68903435CA229CC9911E42F9; pgv_info=ssid=s3449402157; pgv_pvid=3073734972; o_cookie=623143506; ptisp=cnc; ptui_loginuin=623143506; pt2gguin=o0623143506; uin=o0623143506; skey=@j7XH1TbQe; RK=vgdSw2v1eE; ptcz=7686d9c5f8d134217b7b3ccf2a1cb03040ec8d68f853bf163ed7235470709d99; luin=o0623143506; lskey=00010000c83bfd8386f4f0a401eb7bab337fbb6d5ea73ca8ea1cd8035e2c7e94c380eb69740f76c178f7585c; mb_reg_from=8; wbilang_10000=zh_CN; ts_last=t.qq.com/; ts_uid=6831161615; ad_play_index=58; p_uin=o0623143506; p_skey=liH5HQnezxax9Si9Srq-mDW0fchciwKkV2t-z1TVUJ0_; pt4_token=8-YzbRS6XJqggZzaEthjDQ__; p_luin=o0623143506; p_lskey=000400006fcedde1ea330bdfea4293c04d86218b627a364d0fb456b5674a793329df7af8ffac7ae049415afe";

		String uid = "kaifulee";
		String targetURL = "http://t.qq.com/" + uid;

		String content = grabPageSource4QQ.getPageSourceOfQQ(targetURL, null,
				null);

		/**
		 * 将得到的content内容，放到editplus编辑器或其它可预览html代码的编辑器中，一看便知是否为真正的登陆后的页面了
		 */
		System.out.println("测试得到的源码---" + content);

	}
}
