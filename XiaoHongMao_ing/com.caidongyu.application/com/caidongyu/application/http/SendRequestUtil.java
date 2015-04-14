package com.caidongyu.application.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import com.cdy.qzone.interfaced.SetHttpConnection;

/**
 * <p>
 * 发送请求工具类
 * </p>
 * 
 * @since 1.0
 * @version 1.0 (2013-2-7 下午5:30:32)
 * @author 李洋
 * @see #downloadHttpPage 下载一个网页页面
 */
public class SendRequestUtil {
	/** 对证书的处理方案 */
	private static X509TrustManager x509TrustManager = new myX509TrustManager();
	/** 验证主机名和服务器验证方案是否匹配 */
	private static HostnameVerifier hostnameVerifier = new myHostnameVerifier();
	
	
	public static void main(String[] args) throws InterruptedException {
		
		//代理设置为true
		
//		String content = SendRequestUtil.doRequestProxy("http://www.lyoxh.com:8079/test/test01" , null , new Proxy(Proxy.Type.HTTP, new InetSocketAddress("111.1.36.166", 80)) );
//		
//		//String content = SendRequestUtil.doRequest("http://www.lyoxh.com:8079/test/test01" , "" );
//		
//		System.out.println(content.replaceAll("<?.*>", ""));
//		
//		
//		Thread.sleep(2000000);
		
		
		
		//下载百度贴吧背景图片
//		String outPath = "C:\\Users\\Administrator\\Desktop\\baidu";
//		for(int i=0; i<1000; i++){
//			for(int j=1; j<=2; j++){
//				String name = "bg_%1_wrap%2.jpg".replace("%1", i+"").replace("%2", j+"");
//				String url = "http://static.tieba.baidu.com/tb/cms/frs/bg/"+name;
//				SendRequestUtil.downloadHttpPage(url, outPath+File.separatorChar+name, "");
//				Thread.sleep(200);
//			}
//		}
		
		//下载QQ空间背景图片
		//http://ctc.i.gtimg.cn/qzone/space_item/orig/11
		//http://ctc.i.gtimg.cn/qzone/space_item/pre/7/61703_1.gif 前景预览图片
		//http://ctc.i.gtimg.cn/qzone/space_item/orig/7/90775.swf //动画
		//12  24200
		
		//热点QQ表情噢
//		http://ctc.qzonestyle.gtimg.cn/qzone/em/e4018.gif
//		//经典QQ表情噢
//		http://ctc.qzonestyle.gtimg.cn/qzone/em/e100.gif
	}
	
	/**
	 * 默认的安全认证
	 */
	static {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLS"); // 或SSL
			X509TrustManager[] xtmArray = new X509TrustManager[]{x509TrustManager};
			sslContext.init(null, xtmArray, new java.security.SecureRandom());
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		if (sslContext != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());
		}
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	}

	/**
	 * 对证书的方案，默认为空 {@link X509TrustManager}
	 **/
	static class myX509TrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// 检查客户端信任
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// 检查服务器信任
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// 得到请求ISS
			return null;
		}

	}
	/**
	 * 验证主机名和服务器是否匹配的方案。默认为空 {@link HostnameVerifier}
	 **/
	static class myHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
//			System.out.println("Warning: URL Host: " + hostname + " vs. "
//					+ session.getPeerHost());
			return true;
		}
	}
	
	
	/**
	 * 
	 * @param urlStr    请求地址
	 * @param type      请求类型 "post" "get",若没有则默认get
	 * @param sc        当次请求的需要设置请求前或请求后的操作→{@link SetHttpConnection}
	 * @param params    当次请求的参数
	 * @param cookie    当次请求的Cookie
	 * @param Proxy     {@link Proxy 代理}
	 * @param userAgent 使用浏览器
	 * 
	 * @return 获取请求的网页字符串
	 */
	public static String doRequest(String urlStr, String type, SetHttpConnection sc, String params, String cookie, Proxy proxy, String userAgent) {
		// 下面的index.jsp由<servlet-mapping>映射到
		// 一个Servlet(com.quantanetwork.getClientDataServlet)
		// 该Servlet的注意点下边会提到
		URL url = null;
		URLConnection rulConnection = null;
		HttpURLConnection httpUrlConnection =null;
		OutputStream outStrm = null;
		BufferedReader br = null;
		//如果浏览器没有指定，则使用默认的浏览器
		userAgent = userAgent!=null && !userAgent.equals("")?userAgent:SendRequestUtil.userAgent;
		
		try {
			url = new URL(urlStr);
			//如果需要代理
			if( null != proxy)
				rulConnection = url.openConnection(proxy);
			else
				rulConnection = url.openConnection();
			
			if(urlStr.toLowerCase().indexOf("https") == 0){        //如果是Https请求，更改httpss=https 2013年7月24日 09:51:53
				httpUrlConnection = (HttpsURLConnection) rulConnection;
			}else                                                  //如果是Http请求
				httpUrlConnection = (HttpURLConnection) rulConnection;
				

			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
			// http正文内，因此需要设为true, 默认情况下是false;
			//如果是Post请求
			if(type.equalsIgnoreCase("post"))
				httpUrlConnection.setDoOutput(true);

			// 设置是否从httpUrlConnection读入，默认情况下是true;
			httpUrlConnection.setDoInput(true);

			// 请求不能使用缓存
			httpUrlConnection.setUseCaches(false);

			// 设定传送的内容类型是可序列化的java对象
			// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)

			//如果是Post请求
			if(type.equalsIgnoreCase("post")){
				httpUrlConnection.setRequestProperty("Content-type","application/x-www-form-urlencoded");
				httpUrlConnection.setRequestProperty("Content-Length","1024");
			}
			//如果有Cookie
			if(null != cookie)
				httpUrlConnection.setRequestProperty("Cookie",cookie);
			if (null != sc) {
				sc.before(httpUrlConnection);
			}
			httpUrlConnection.setRequestProperty("Accept-Charset","utf-8, gbk,* ;q=0.1"); // 设置请求头
			httpUrlConnection.setRequestProperty("User-Agent",userAgent); // 设置请求头
			httpUrlConnection.setRequestProperty("accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpUrlConnection.setRequestProperty("accept-language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			httpUrlConnection.setRequestProperty("Connection", "keep-alive");
			
			// 设定请求的方法为"POST"，默认是GET
			//如果是Post请求
			if(type.equalsIgnoreCase("post")){
				httpUrlConnection.setRequestMethod("POST");
				// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
				// httpUrlConnection.connect();
	
				// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
				// 所以在开发中不调用上述的connect()也可以)。
				if (params != null && params.indexOf("=") > 0) {
					outStrm = httpUrlConnection.getOutputStream();
	
					// 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象.
					outStrm.write(params.getBytes("UTF-8"));
	
					outStrm.flush();
					
					outStrm.close();
				}
			}
			try{
				if( httpUrlConnection.getResponseCode()==404
						|| httpUrlConnection.getResponseCode()==403){
					return null;
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
				return null;
			}
			// ObjectOutputStream objOutputStrm = new
			// ObjectOutputStream(outStrm);
			//
			// // 向对象输出流写出数据，这些数据将存到内存缓冲区中
			// objOutputStrm.writeObject(new String("我是测试数据"));
			//
			// // 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
			// objOutputStrm.flush();
			//
			// // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
			// // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
			// objOutputStrm.close();

			// 调用HttpURLConnection连接对象的getInputStream()函数,
			// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
			// <===注意，实际发送请求的代码段就在这里
			br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(),"UTF8"));

			// 上边的httpConn.getInputStream()方法已调用,本次HTTP请求已结束,下边向对象输出流的输出已无意义，
			// 既使对象输出流没有调用close()方法，下边的操作也不会向对象输出流写入任何数据.
			// 因此，要重新发送数据时需要重新创建连接、重新设参数、重新创建流对象、重新写数据、
			// 重新发送数据(至于是否不用重新这些操作需要再研究)
			StringBuffer sb = new StringBuffer();
			String lineStr = "";
			while ((lineStr = br.readLine()) != null) { // 读取信息
				sb.append(lineStr + "\r\n");
			}
			if (null != sc) {
				sc.after(httpUrlConnection);
			}
			return sb.toString();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
				try {
					if(null != br)
						br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}

	/**
	 * 使用 {@link HttpURLConnection} 发出一个http的下载请求
	 * 
	 * @param urlStr    请求地址
	 * @param outPath   下载的东西保存路径，请自带后缀
	 * @param sc        当次请求的需要设置请求前或请求后的操作→{@link SetHttpConnection}
	 * @param cookie    当次请求的Cookie
	 * @param Proxy     {@link Proxy 代理}
	 * @param userAgent 使用浏览器的User-agent
	 */
	public static void httpDownload(String urlStr, String outPath, SetHttpConnection sc, String cookie, Proxy proxy, String userAgent) {
		int chByte = 0;

		URL url = null; // 连接地址

		HttpURLConnection httpConn = null; // 连接对象

		InputStream in = null; // 读取网页的流

		FileOutputStream out = null; // 保存网页文件的流

		URLConnection rulConnection = null; //
		
		//如果浏览器没有指定，则使用默认的浏览器
		userAgent = userAgent!=null && !userAgent.equals("")?userAgent:SendRequestUtil.userAgent;
		
		try {

			url = new URL(urlStr);

			//如果果需要代理
			if( null != proxy)
				rulConnection = url.openConnection(proxy);
			else
				rulConnection = url.openConnection();

			if(urlStr.toLowerCase().indexOf("https") == 0){
				httpConn = (HttpsURLConnection) rulConnection;
			}else                                                  //如果是Http请求
				httpConn = (HttpURLConnection) rulConnection;

			HttpURLConnection.setFollowRedirects(true); // 设置为重定向

			httpConn.setRequestMethod("GET"); // 设置请求方法

			httpConn.setRequestProperty("User-Agent", userAgent); // 设置请求处理

			httpConn.setRequestProperty("accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

			httpConn.setRequestProperty("accept-language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");

			httpConn.setRequestProperty("Cookie", cookie);
			
			httpConn.setRequestProperty("connection", "keep-alive");
			//发送请求前
			if (null != sc) {
				sc.before(httpConn);
			}
			
			if( httpConn.getResponseCode()==404 )
				return;
			
			in = httpConn.getInputStream(); // 接收数据
			out = new FileOutputStream(new File(outPath)); // 创建本地输出文件

			chByte = in.read(); // 读取下一个字节

			while (chByte != -1) { // 如果有下一个字节

				out.write(chByte); // 输出这个自己到文件

				chByte = in.read(); // 读取下一个字节

			}

			//发送请求后
			if (null != sc) {
				sc.after(httpConn);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if(out != null){
					out.flush();
					out.close(); // 关闭输出流
				}
				if(in != null)
					in.close(); // 关闭输入流
				if(httpConn != null)
					httpConn.disconnect(); // 关闭连接
			} catch (Exception ex) {

				ex.printStackTrace();

			}

		}
	}
	
	
	
	
	/** 代理浏览器标识，http协议中的User-Agent */
	//Mozilla/5.0 (Linux; U; Android 0.5; en-us) AppleWebKit/522+ (KHTML, like Gecko) Safari/419.3
	//Mozilla/5.0 (Windows NT 6.1; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0";
//	private static String userAgent = "Mozilla/5.0 (Linux; U; Android 0.5; en-us) AppleWebKit/522+ (KHTML, like Gecko) Safari/419.3";
	/** 0是电脑，1是手机 */
	public static int state = 1;

}
