package com.weibo.common.utils;

import org.apache.log4j.Logger;

public class FormatOutput {
	public static Logger logger = Logger.getLogger(FormatOutput.class);
	public static String printPrefix = "====================";
	public static String printSuffix = printPrefix;

	public static String urlPrefix = "http://www.weibo.com/";
	public static String urlPrefix_no_www = "http://weibo.com/";

	public static String topic_urlPrefix = "http://huati.weibo.com/";

	public static String urlPrefix_ProAndEnter = "http://e.weibo.com/";

	public static String urlPrefix_no_slash = "http://www.weibo.com";
	public static String urlPrefix_QQ = "http://t.qq.com/";
	public static String media_urlPrefix = "http://meida.weibo.com/";
	public static String threadPrefix = "----------------------";

	public static void printString(String words) {
		logger.info(printPrefix + words + printSuffix);
	}
}
