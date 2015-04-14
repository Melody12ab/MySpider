package com.weibo.utils.qq;

public class StaticValue4QQ {
	public static String QQ_PersonInfo_URL_Prefix = "http://p.t.qq.com/m/home_userinfo.php?u=";

	/**
	 * 为腾讯个人帐户和微博内容信息的HOST
	 */
	public static String QQ_PersonInfo_Host = "p.t.qq.com";
	public static String QQ_ContentInfo_Host = "t.qq.com";

	/**
	 * 获取非第一页内容时候,用到的referer
	 */
	public static String QQ_ContentInfo_Referer = "http://t.qq.com/heyanguangzqb";

	/**
	 * 当滚动方式向下翻页时的url构成
	 */
	public static String QQ_Scroll_URL = "http://api1.t.qq.com/asyn/index.php?&time=${time}&page=${currentPage}&id=${aid}&u=${uid}&apiType=8&apiHost=http%3A%2F%2Fapi.t.qq.com&_r=1354255603748";
	public static String QQ_Ajax_Next_Page_Host = "api1.t.qq.com";
	public static String QQ_Ajax_Next_Page_Referer = "http://api1.t.qq.com/proxy.html";

	/**
	 * 获取腾讯博主的关注时的Host,Refer,匹配静态串URL
	 */
	public static String QQ_PersonAttetnion_Host = "api.t.qq.com";
	public static String QQ_PersonAttetnion_Refer = "http://api.t.qq.com/proxy.html";
	public static String QQ_PersonAttetnion_Page_URL = "http://t.qq.com/relations/following.php?u=${uid}&t=0&st=1&p=${pageNo}&apiType=8&apiHost=http://api.t.qq.com&_r=${time}";

	/**
	 * 获取腾讯关键词的Host,Refer
	 */
	public static String QQ_Keyword_Host = "search.t.qq.com";
	
	//获取docUrl的前缀，当是json传送时
	public static String QQ_DocUrl_prefix="http://t.qq.com/p/t/";
	
//	public static String QQ_Keyword_Refer = "http://search.t.qq.com/index.php?k=${keyword}&pos=${pos}&p=1";
	
}
