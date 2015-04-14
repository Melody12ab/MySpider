package com.cdy.qzone.entity;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.List;

import com.caidongyu.application.http.HttpUtil;
import com.caidongyu.application.http.SendRequestUtil;
import com.cdy.qzone.interfaced.SetHttpConnection;

/**
 * Http请求服务类,提供简单的基本请求信息封装
 * 
 * @name        Http请求服务类
 * @author                  ★李洋★     liyangchiyue@gmail.com
 * @createtTime 2013-6-17  上午9:12:50
 * @version     1.0
 * @since       1.0
 *
 */
@SuppressWarnings("serial")
public abstract class HttpService implements Serializable{
	/** 默认手机浏览器 */
	public static final String PHONE_AGENT = 
			"Mozilla/5.0 (Linux; U; Android 0.5; en-us) AppleWebKit/522+ (KHTML, like Gecko) Safari/419.3";
	/** 默认电脑浏览器 */
	public static final String CONPUTER_AGENT = 
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0";
	
	/** 编码方式 */
	protected String charset="UTF-8";
	/** 浏览器 */
	protected String userAgent;
	/** 代理 */
	protected Proxy proxy;
	/** 当前用户Cookie */
	public String cookie = "";
	/** IP */
	protected String ip;

	/**
	 * 一个HTTP请求，包括该对象的 proxy属性，cookie属性，userAgent属性<br>
	 * <ul>
	 * <li>1.默认发送该对象的{@link #cookie Cookie}。</li>
	 * <li>2.默认保存从服务器接手到的{@link #cookie Cookie}</li>
	 * <li>3.默认使用该对象的{@link #proxy 代理}，若没有{@link #proxy 代理}，则直接请求</li>
	 * <li>4.默认使用该对象，通过{{@link #userAgent}指定的浏览器</li>
	 * <ul>
	 * @param urlStr  请求地址
	 * @param type    请求类型 "post","get",默认"get"
	 * @param sc      
	 * @param params  请求参数，Post方法才会使用
	 * @param userAgent 指定浏览器参数
	 * 
	 * @see {@SendRequestUtil#doRequest(String urlStr, String type, SetHttpConnection sc, String params, String cookie, Proxy proxy, String userAgent)}
	 * @return  请求获得的网页内容
	 */
	public String request(String urlStr, String type, SetHttpConnection sc, String params, String userAgent){
		return SendRequestUtil.doRequest(urlStr, type, sc!=null?sc:new SetHttpConnection() {
			
			@Override
			public String before(HttpURLConnection httpConn) throws ProtocolException {
				return null;
			}
			
			@Override
			public String after(HttpURLConnection httpConn) {
				List<String> lists = httpConn.getHeaderFields().get("Set-Cookie");
				cookie = HttpUtil.mergeCookies(cookie, lists);
				return cookie;
			}
		}, params, cookie, proxy, userAgent);
	}
	

	/**
	 * 一个HTTP请求，包括该对象的 proxy属性，cookie属性，userAgent属性<br>
	 * <ul>
	 * <li>1.默认发送该对象的{@link #cookie Cookie}。</li>
	 * <li>2.默认保存从服务器接手到的{@link #cookie Cookie}</li>
	 * <li>3.默认使用该对象的{@link #proxy 代理}，若没有{@link #proxy 代理}，则直接请求</li>
	 * <li>4.默认使用该对象，通过{{@link #userAgent}指定的浏览器</li>
	 * <ul>
	 * @param urlStr  请求地址
	 * @param type    请求类型 "post","get",默认"get"
	 * @param sc      
	 * @param params  请求参数，Post方法才会使用
	 * 
	 * @see {@SendRequestUtil#doRequest(String urlStr, String type, SetHttpConnection sc, String params, String cookie, Proxy proxy, String userAgent)}
	 * @return  请求获得的网页内容
	 */
	public String request(String urlStr, String type, SetHttpConnection sc, String params){
		return request(urlStr, type, sc, params, this.userAgent);
	}
	
	
	/**
	 * 发送一个下载请求
	 * 带上当前的Cookie
	 * 默认执行Cookie合并{@link HttpUtil#mergeCookies(String cookie,List list)}
	 * 
	 * @param url      地址
	 * @param outPath  输出地址
	 * @param sc       
	 */
	public void requestHttpDownload(String url, String outPath, SetHttpConnection sc){
		SendRequestUtil.httpDownload(url, outPath, sc!=null?sc:new SetHttpConnection() {
			
			@Override
			public String before(HttpURLConnection httpConn) throws ProtocolException {
				return null;
			}
			
			@Override
			public String after(HttpURLConnection httpConn) {
				List<String> lists = httpConn.getHeaderFields().get("Set-Cookie");
				cookie = HttpUtil.mergeCookies(cookie, lists);
				return cookie;
			}
		}, cookie, proxy, userAgent);
	}
	
	/**
	 * 
	 * 发送一个下载请求
	 * 带上当前的Cookie
	 * 默认执行Cookie合并{@link HttpUtil#mergeCookies(String cookie,List list)}
	 * 
	 * @param url      地址
	 * @param outPath  输出地址
	 */
	public void requestDownload(String url, String outPath){
		this.requestHttpDownload(url, outPath, null);
	}
	
	/**
	 * 一次Get请求<br>
	 * <ul>
	 * <li>1.默认发送该对象的{@link #cookie Cookie}。</li>
	 * <li>2.默认保存从服务器接手到的{@link #cookie Cookie}</li>
	 * <li>3.默认使用该对象的{@link #proxy 代理}，若没有{@link #proxy 代理}，则直接请求</li>
	 * <li>4.默认使用该对象，通过{{@link #userAgent}指定的浏览器</li>
	 * <ul>
	 * 
	 * @param urlStr  请求地址
	 * @return  请求获得的网页内容
	 */
	public String requestGet(String urlStr){
		return request(urlStr, "get", null, null);
	}
	/**
	 * 一次Post请求<br>
	 * <ul>
	 * <li>1.默认发送该对象的{@link #cookie Cookie}。</li>
	 * <li>2.默认保存从服务器接手到的{@link #cookie Cookie}</li>
	 * <li>3.默认使用该对象的{@link #proxy 代理}，若没有{@link #proxy 代理}，则直接请求</li>
	 * <li>4.默认使用该对象，通过{{@link #userAgent}指定的浏览器</li>
	 * <ul>
	 * 
	 * @param urlStr  请求地址
	 * @param params  请求参数
	 * @return  请求获得的网页内容
	 */
	public String requestPost(String urlStr, String params){
		return request(urlStr, "post", null, params);
	}

	/**
	 * 一次手机的Get请求<br>
	 * <ul>
	 * <li>1.默认发送该对象的{@link #cookie Cookie}。</li>
	 * <li>2.默认保存从服务器接手到的{@link #cookie Cookie}</li>
	 * <li>3.默认使用该对象的{@link #proxy 代理}，若没有{@link #proxy 代理}，则直接请求</li>
	 * <li>4.默认使用该对象，通过{{@link #userAgent}指定的浏览器</li>
	 * <ul>
	 * 
	 * @param urlStr  请求地址
	 * @return  请求获得的网页内容
	 */
	public String requestGetOfPhone(String urlStr){
		return request(urlStr, "get", null, null, PHONE_AGENT);
	}
	
	/**
	 * 一次手机的Post请求<br>
	 * <ul>
	 * <li>1.默认发送该对象的{@link #cookie Cookie}。</li>
	 * <li>2.默认保存从服务器接手到的{@link #cookie Cookie}</li>
	 * <li>3.默认使用该对象的{@link #proxy 代理}，若没有{@link #proxy 代理}，则直接请求</li>
	 * <li>4.默认使用该对象，通过{{@link #userAgent}指定的浏览器</li>
	 * <ul>
	 * 
	 * @param urlStr  请求地址
	 * @return  请求获得的网页内容
	 */
	public String requestPostOfPhone(String urlStr,String params){
		return request(urlStr, "post", null, params, PHONE_AGENT);
	}
}
