package com.tianliang.spider.utils;

import java.util.LinkedList;
import java.util.List;

import com.tianliang.spider.pojos.ProxyPojo;

/**
 * 系统参数配置
 * 
 * @author zel
 * 
 */
public class SystemParas {
	// 日志
	public static MyLogger logger = new MyLogger(SystemParas.class);

	public static ReadConfigUtil readConfigUtil = new ReadConfigUtil(
			"crawl.properties", true);

	// http请求遇到error时，重复请求的次数
	public static int http_req_error_repeat_number = Integer
			.parseInt(readConfigUtil.getValue("http_req_error_repeat_number"));
	// 一次http请求要等待的时间
	public static int http_req_once_wait_time = Integer.parseInt(readConfigUtil
			.getValue("http_req_once_wait_time"));

	/**
	 * 可以抓取url的最大重复次数
	 */
	public static int crawl_page_repeat_number = Integer
			.parseInt(readConfigUtil.getValue("crawl_page_repeat_number"));

	/**
	 * 代理参数设置
	 */

	// proxy代理是否启用
	public static boolean proxy_open = Boolean.parseBoolean(readConfigUtil
			.getValue("proxy_open"));
	public static boolean proxy_self = Boolean.parseBoolean(readConfigUtil
			.getValue("proxy_self"));
	// 从文件中取得每对proxy ip的ip与port并加入proxyList集合
	public static String ip_proxy_file_path = readConfigUtil
			.getValue("ip_proxy_file_path");

	// 读取代理列表，并加入到proxy中
	public static List<ProxyPojo> proxyList = new LinkedList<ProxyPojo>();
	public static int proxy_fail_max_count = Integer.parseInt(readConfigUtil
			.getValue("proxy_fail_max_count"));

	// 设置链接超时时间,这个是链接时间
	public static int http_connection_timeout = Integer.parseInt(readConfigUtil
			.getValue("connection_timeout"));
	// 这个时读取超时时间
	public static int http_read_timeout = Integer.parseInt(readConfigUtil
			.getValue("read_timeout"));

	static {
		try {
			if (proxy_open) {
				logger.info("proxy server has been used!");
				ReadConfigUtil readProxyConfig = new ReadConfigUtil(
						ip_proxy_file_path, false);
				String temp_proxy_list = readProxyConfig.getLineConfigTxt();
				String temp_proxy_paras[] = null;
				ProxyPojo proxyPojo = null;
				if (temp_proxy_list.trim().length() > 0) {
					for (String proxy_line : temp_proxy_list.split("\n")) {
						temp_proxy_paras = proxy_line.split("	");
						if (temp_proxy_paras.length == 2) {
							proxyPojo = new ProxyPojo(temp_proxy_paras[0],
									Integer.parseInt(temp_proxy_paras[1]));
							proxyPojo.setAuthEnable(false);// 无需用户名和密码
							proxyList.add(proxyPojo);
						} else {
							proxyPojo = new ProxyPojo(temp_proxy_paras[0],
									Integer.parseInt(temp_proxy_paras[1]),
									temp_proxy_paras[2], temp_proxy_paras[3]);
							proxyPojo.setAuthEnable(true);// 需要用户名和密码
							proxyList.add(proxyPojo);
						}

					}
				}
			} else {
				logger.info("proxy server is forbidden!");
			}
		} catch (Exception e) {
			logger.info("读取代理服务器列表参数时抛出异常，请检查!");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// System.out.println(proxyList);
	}
}
