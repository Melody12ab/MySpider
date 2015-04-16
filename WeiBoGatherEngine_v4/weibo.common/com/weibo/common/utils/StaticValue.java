package com.weibo.common.utils;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.mbm.elint.entity.util.ProxyPojo;

public class StaticValue {
	public static Logger logger = Logger.getLogger(StaticValue.class);

	/**
	 * 编码设置
	 */
	public static String default_encoding = "utf-8";
	public static String encoding_gbk = "gbk";

	// 抓取服务是否开启，当客户端发送与抓取服务有关的请求时，先判断此标志位
	public static boolean isRunning = false;
	public static boolean isPausing = false;// 暂停
	// 3个 bloom的存放位置
	public static String toVisitUrls_file_path = ReadSpiderConfig
			.getValue("toVisitedUrls_list_file_path");// toVisit_list存放位置
	public static String doc_bloomFilter_file_path = ReadSpiderConfig
			.getValue("doc_bloomFilter_file_path");
	public static String visitedUrls_bloomFilter_file_path = ReadSpiderConfig
			.getValue("visitedUrls_bloomFilter_file_path");
	public static String errorUrls_bloomFilter_file_path = ReadSpiderConfig
			.getValue("errorUrls_bloomFilter_file_path");
	public static String sina_personInfo_bloomFilter_file_path = ReadSpiderConfig
			.getValue("sina_personInfo_bloomFilter_file_path");
	public static String check_repeat_sina_personInfo_bloomFilter_file_path = ReadSpiderConfig
			.getValue("check_repeat_sina_personInfo_bloomFilter_file_path");

	// 关于commentDoc bloomFilter的设置
	public static String commentDoc_bloomFilter_file_path = ReadSpiderConfig
			.getValue("commentDoc_bloomFilter_file_path");
	public static int commentDoc_filter_size = Integer
			.parseInt(ReadSpiderConfig.getValue("commentDoc_filter_size"));
	public static String toVisitCommentUrls_file_path = ReadSpiderConfig
			.getValue("toVisitCommentUrls_file_path");// toVisit_list存放位置
	// 关于commentDoc bloomFilter的设置--结束

	public static int doc_filter_size = Integer.parseInt(ReadSpiderConfig
			.getValue("doc_filter_size"));
	public static int visitedUrls_filter_size = Integer
			.parseInt(ReadSpiderConfig.getValue("visitedUrls_filter_size"));
	public static int errorUrls_filter_size = Integer.parseInt(ReadSpiderConfig
			.getValue("errorUrls_filter_size"));
	public static int sina_personInfo_filter_size = Integer
			.parseInt(ReadSpiderConfig.getValue("sina_personInfo_filter_size"));
	public static int check_repeat_sina_personInfo_filter_size = Integer
			.parseInt(ReadSpiderConfig
					.getValue("check_repeat_sina_personInfo_filter_size"));

	// errorUrls的存储位置
	public static String errorUrls_file_path = ReadSpiderConfig
			.getValue("error_urls_file_path");

	// 得到bloomFilter经过多少守护线程的周期，保存一次
	public static int output_bloomFilter_circle = Integer
			.parseInt(ReadSpiderConfig
					.getValue("bloomFilter_outputFile_circle"));

	// 守护线程睡的周期长度,单位为ms
	public static long daemon_sleep_interval_time = Long
			.parseLong(ReadSpiderConfig
					.getValue("daemon_monitor_interval_time"));
	public static long daemon_write_success_rest_time = Long
			.parseLong(ReadSpiderConfig
					.getValue("daemon_write_success_rest_time"));

	// 取得api抓取是否禁用
	public static boolean api_spider_enable = Boolean
			.parseBoolean(ReadSpiderConfig.getValue("api_spider_enable"));

	// 取得cookie抓取是否被禁用
	public static boolean cookie_spider_enable = Boolean
			.parseBoolean(ReadSpiderConfig.getValue("cookie_spider_enable"));
	// 取该是否禁止切换KeyWord_user,为false代表和cookie_user_spider统一更换，现只提供false方式，若为true代表自己更换，暂不提供该项功能
	public static boolean keyword_single_switch_enable = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("keyword_user_switch_enable"));

	// 取得keyword抓取是否被禁用
	public static boolean keyword_spider_enable = Boolean
			.parseBoolean(ReadSpiderConfig.getValue("keyword_spider_enable"));

	// 取得topic_title抓取是否被禁用
	public static boolean topic_title_spider_enable = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("topic_title_spider_enable"));
	// 取得topic_content抓取是否被禁用
	public static boolean topic_content_spider_enable = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("topic_content_spider_enable"));
	// 取该是否禁止切换topic_user,是指独立切换，为false代表和cookie_user_spider统一更换，现只提供false方式，若为true代表自己更换，暂不提供该项功能
	public static boolean topic_title_single_user_switch_enable = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("topic_title_single_user_switch_enable"));
	public static boolean topic_content_single_user_switch_enable = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("topic_content_single_user_switch_enable"));

	// 得到抓取的时候的每所要抓取的条目数
	public static int pageItems = Integer.parseInt(ReadSpiderConfig
			.getValue("page_items"));

	// 得到某个url抓取出现错误时，要重抓取该url的次数
	public static int url_repeat_count = Integer.parseInt(ReadSpiderConfig
			.getValue("url_repeat_count"));
	public static int keyword_repeat_count = Integer.parseInt(ReadSpiderConfig
			.getValue("keyword_repeat_count"));
	// 取得需要存储web所输出文件的根路径
	public static String root_path = ReadSpiderConfig.getValue("root_dir");
	// 抓取微博关注的最大关注页数,20个/页
	public static int grab_attention_max_pageNum = Integer
			.parseInt(ReadSpiderConfig.getValue("grab_attention_max_pageNum"));
	// 抓取每页的关注数时，每抓一页所要休息的时间
	public static int grab_attention_sleep_interval_time = Integer
			.parseInt(ReadSpiderConfig
					.getValue("grab_attention_sleep_interval_time"));
	// api抓取关注数时，获取时的最大关注条目数
	public static int api_grab_attention_max_sum_count = Integer
			.parseInt(ReadSpiderConfig
					.getValue("api_grab_attention_max_sum_count"));
	// 最大同时在线的用户数量，暂时设为1
	public static int max_api_user_online_number = Integer
			.parseInt(ReadSpiderConfig.getValue("max_api_user_online_number"));
	// switch user grab max interval time
	public static int sina_switch_account_interval_time = Integer
			.parseInt(ReadSpiderConfig
					.getValue("sina_switch_account_interval_time")) * 60 * 1000;

	public static int qq_switch_account_interval_time = Integer
			.parseInt(ReadSpiderConfig
					.getValue("qq_switch_account_interval_time")) * 60 * 1000;

	// switch search grab max interval time
	public static int switch_search_interval_time = Integer
			.parseInt(ReadSpiderConfig.getValue("switch_search_interval_time")) * 60 * 1000;

	// switch user thread sleep time
	public static int switch_user_thread_sleep_time = Integer
			.parseInt(ReadSpiderConfig
					.getValue("switch_user_thread_sleep_time"));

	// 关于keyword搜索的设置
	public static String keyWordDoc_bloomFilter_file_path = ReadSpiderConfig
			.getValue("keyWordDoc_bloomFilter_file_path");
	public static String visitedKeyWords_bloomFilter_file_path = ReadSpiderConfig
			.getValue("visitedKeyWords_bloomFilter_file_path");
	public static int visitedKeyWords_filter_size = Integer
			.parseInt(ReadSpiderConfig.getValue("visitedKeyWords_filter_size"));
	public static String toVisitKeyWords_file_path = ReadSpiderConfig
			.getValue("toVisitKeyWords_file_path");

	public static int keyword_grab_one_page_intervale = Integer
			.parseInt(ReadSpiderConfig
					.getValue("keyword_grab_one_page_intervale"));
	public static int keyWordsDoc_filter_size = Integer
			.parseInt(ReadSpiderConfig.getValue("keyWordsDoc_filter_size"));

	// 关于特征词提取的参数设定
	public static int feature_doc_number = Integer.parseInt(ReadSpiderConfig
			.getValue("feature_doc_number"));
	public static int feature_word_number = Integer.parseInt(ReadSpiderConfig
			.getValue("feature_word_number"));

	public static int hot_topic_number = Integer.parseInt(ReadSpiderConfig
			.getValue("hot_topic_number"));

	public static String new_dic_file_path = ReadSpiderConfig
			.getValue("new_dic_file_path");
	public static int addNewWords_filter_size = Integer
			.parseInt(ReadSpiderConfig.getValue("addNewWords_filter_size"));
	public static String featureWords_cache_file_path = ReadSpiderConfig
			.getValue("featureWords_cache_file_path");

	// 关于url to uid关于映射的配置
	public static String UUMap_cache_file_path = ReadSpiderConfig
			.getValue("UUMap_cache_file_path");
	// 关于uid to picUrl关于映射的配置
	public static String UPMap_cache_file_path = ReadSpiderConfig
			.getValue("UPMap_cache_file_path");

	// 关于周期抓取的时间设置
	public static int monitor_list_circle_time = Integer
			.parseInt(ReadSpiderConfig.getValue("monitor_list_circle_time")) * 60 * 1000;// 以分钟为单位，要先化成秒
	// public static long url_task_circle_time = Integer
	// .parseInt(ReadSpiderConfig.getValue("url_task_circle_time"))*60*60*1000;
	// 现在为测试阶段，时间以分钟计
	public static long url_task_circle_time = Integer.parseInt(ReadSpiderConfig
			.getValue("url_task_circle_time")) * 60 * 1000;
	// 关键词的周期时间
	public static long keyword_task_circle_time = Integer
			.parseInt(ReadSpiderConfig.getValue("keyword_task_circle_time")) * 60 * 1000;

	// 关于媒体URL的周期时间设置
	public static long media_url_task_circle_time = Integer
			.parseInt(ReadSpiderConfig.getValue("media_url_task_circle_time")) * 60 * 1000;

	// 关于媒体URL的周期时间设置
	public static long enterprise_url_task_circle_time = Integer
			.parseInt(ReadSpiderConfig
					.getValue("enterprise_url_task_circle_time")) * 60 * 1000;

	// 关于专业版URL的周期时间设置
	public static long profession_url_task_circle_time = Integer
			.parseInt(ReadSpiderConfig
					.getValue("profession_url_task_circle_time")) * 60 * 1000;

	// 对于执行循环任务的延迟
	public static long circle_task_later = Integer.parseInt(ReadSpiderConfig
			.getValue("circle_task_later")) * 60 * 1000;

	public static String circle_urls_cache_file_path = ReadSpiderConfig
			.getValue("circle_urls_cache_file_path");
	public static String circle_visitedKeyWords_cache_file_path = ReadSpiderConfig
			.getValue("circle_visitedKeyWords_cache_file_path");

	// 关于visitedAttentionUrls Set的设置
	public static String visitedAttentionUrls_set_file_path = ReadSpiderConfig
			.getValue("visitedAttentionUrls_set_file_path");// toVisit_list存放位置

	// LjSearch 设置 for Mbm
	public static String search_accountInfo_ip = ReadSpiderConfig
			.getValue("search_accountInfo_ip");
	public static int search_accountInfo_port = Integer
			.parseInt(ReadSpiderConfig.getValue("search_accountInfo_port"));
	public static String search_contentInfo_ip = ReadSpiderConfig
			.getValue("search_contentInfo_ip");
	public static int search_contentInfo_port = Integer
			.parseInt(ReadSpiderConfig.getValue("search_contentInfo_port"));
	public static String search_attentionInfo_ip = ReadSpiderConfig
			.getValue("search_attentionInfo_ip");
	public static int search_attentionInfo_port = Integer
			.parseInt(ReadSpiderConfig.getValue("search_attentionInfo_port"));

	// LjSearch 设置 for dratio
	public static String search_doc_ip = ReadSpiderConfig
			.getValue("search_doc_ip");
	public static int search_doc_port = Integer.parseInt(ReadSpiderConfig
			.getValue("search_doc_port"));

	/**
	 * 备用搜索配置--开始
	 */
	// LjSearch 设置 for Mbm
	public static String search_accountInfo_ip_bak = ReadSpiderConfig
			.getValue("search_accountInfo_ip_bak");
	public static int search_accountInfo_port_bak = Integer
			.parseInt(ReadSpiderConfig.getValue("search_accountInfo_port_bak"));
	public static String search_contentInfo_ip_bak = ReadSpiderConfig
			.getValue("search_contentInfo_ip_bak");
	public static int search_contentInfo_port_bak = Integer
			.parseInt(ReadSpiderConfig.getValue("search_contentInfo_port_bak"));
	public static String search_attentionInfo_ip_bak = ReadSpiderConfig
			.getValue("search_attentionInfo_ip_bak");
	public static int search_attentionInfo_port_bak = Integer
			.parseInt(ReadSpiderConfig
					.getValue("search_attentionInfo_port_bak"));
	// LjSearch 设置 for dratio
	public static String search_doc_ip_bak = ReadSpiderConfig
			.getValue("search_doc_ip_bak");
	public static int search_doc_port_bak = Integer.parseInt(ReadSpiderConfig
			.getValue("search_doc_port_bak"));
	/**
	 * 备用搜索配置--结束
	 */
	// 关于状态查询时，出现临界查询时的重复查询次数
	public static int status_max_select_number = Integer
			.parseInt(ReadSpiderConfig.getValue("status_max_select_number"));

	// 备份文件存放位置
	public static String errorUrls_bloomFilter_file_path_bak = ReadSpiderConfig
			.getValue("errorUrls_bloomFilter_file_path_bak");
	// public static String doc_bloomFilter_file_path_bak = ReadSpiderConfig
	// .getValue("doc_bloomFilter_file_path_bak");
	public static String visitedUrls_bloomFilter_file_path_bak = ReadSpiderConfig
			.getValue("visitedUrls_bloomFilter_file_path_bak");
	public static String sina_personInfo_bloomFilter_file_path_bak = ReadSpiderConfig
			.getValue("sina_personInfo_bloomFilter_file_path_bak");
	public static String check_repeat_sina_personInfo_bloomFilter_file_path_bak = ReadSpiderConfig
			.getValue("check_repeat_sina_personInfo_bloomFilter_file_path_bak");
	public static String UUMap_cache_file_path_bak = ReadSpiderConfig
			.getValue("UUMap_cache_file_path_bak");
	public static String UPMap_cache_file_path_bak = ReadSpiderConfig
			.getValue("UPMap_cache_file_path_bak");
	public static String circle_urls_cache_file_path_bak = ReadSpiderConfig
			.getValue("circle_urls_cache_file_path_bak");
	public static String circle_visitedKeyWords_cache_file_path_bak = ReadSpiderConfig
			.getValue("circle_visitedKeyWords_cache_file_path_bak");
	public static String visitedAttentionUrls_set_file_path_bak = ReadSpiderConfig
			.getValue("visitedAttentionUrls_set_file_path_bak");

	// 关于各个ie agent中version的取得
	public static LinkedList<String> ieList = new LinkedList<String>();
	static {
		for (String ieVersion : ReadSpiderConfig.getValue("IE_Version").split(
				" ")) {
			ieList.add(ieVersion);
		}
	}

	// 关于每内容页中，指定最大可包括的重复条目数
	public static int max_content_page_repeat_items = Integer
			.parseInt(ReadSpiderConfig
					.getValue("max_content_page_repeat_items"));

	// 关于每内容页中，指定最大可包括的重复条目数,专为评论而加
	public static int max_content_page_repeat_items_comments = Integer
			.parseInt(ReadSpiderConfig
					.getValue("max_content_page_repeat_items_comments"));

	// 每次切换用户失败时，要等待的时间
	public static int switch_user_once_fail_sleep_time = Integer
			.parseInt(ReadSpiderConfig
					.getValue("switch_user_once_fail_sleep_time"));

	// proxy代理是否启用
	public static boolean proxy_open = Boolean.parseBoolean(ReadSpiderConfig
			.getValue("proxy_open"));

	public static boolean keyword_proxy_open = Boolean
			.parseBoolean(ReadSpiderConfig.getValue("keyword_proxy_open"));

	// 从文件中取得每对proxy ip的ip与port并加入proxyList集合
	public static String ip_proxy_file_path = ReadSpiderConfig
			.getValue("ip_proxy_file_path");

	public static LinkedList<ProxyPojo> proxyList = new LinkedList<ProxyPojo>();
	static {
		try {
			if (proxy_open) {
				ReadConfigUtil readProxyConfig = new ReadConfigUtil(
						ip_proxy_file_path, false);
				String temp_proxy_list = readProxyConfig.getTextLines();
				String temp_proxy_paras[] = null;
				ProxyPojo proxyPojo = null;
				if (temp_proxy_list.trim().length() > 0) {
					for (String proxy_line : temp_proxy_list.split("\n")) {
						temp_proxy_paras = proxy_line.split(" ");
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
			}
		} catch (Exception e) {
			logger.info("读取代理服务器列表参数时抛出异常，请检查!");
			e.printStackTrace();
		}
	}

	/**
	 * 关于个人版抓取的设置
	 */
	// 原始模板--http://weibo.com/aj/mblog/mbloglist?_wv=5&page=1&count=15&max_id=3510851908322505&pre_page=1&end_id=3554246966272203&pagebar=1&_k=136316034500345&uid=1586672964&_t=0&__rnd=1363160363364
	public static String Person_Wb_Content_Page_Next_URL = "http://weibo.com/aj/mblog/mbloglist?_wv=5&page=${pageNum}&count=15&max_id=${maxID}&pre_page=1&end_id=${endID}&pagebar=${pageBar}&_k=136316034500345&uid=${uid}&_t=0&__rnd=1363160363364";

	/**
	 * 关于媒体信息的设置
	 * 
	 * @param args
	 */
	// 请求MediaPersonInfo时获取其粉丝、关注、微博数量的url
	public static String Media_Person_Num_URL = "http://media.weibo.com/mblog/aj_eps_getuserinfo.php?oid=${oid}&rnd=${rnd}";
	public static String Media_Wb_Content_Num_URL = "http://media.weibo.com//mblog/aj_comment.php?mids=${mid}&oid=${oid}&rnd=${rnd}";
	// 在媒体版的信息翻页时
	public static String Media_Wb_Content_Page_Next_URL = "http://media.weibo.com/mblog/aj_mymblog.php?page=${pageNum}&oid=${oid}&pagepreview=0&rnd=${rnd}";
	// 在做sina的media版的ajax请求时，所带的referer参数
	public static String Referer_Media = "http://media.weibo.com/weixiaoshuo";
	// 返回给dratio的top多少
	public static int top_doc_number = Integer.parseInt(ReadSpiderConfig
			.getValue("top_doc_number"));

	/**
	 * 关于企业版的设置
	 * 
	 * @param args
	 */
	// "http://weibo.com/aj/mblog/mbloglist?page=1&count=15&pre_pge=1&pagebar=0&uid=2204248463&_t=0"
	public static String Enterprise_Wb_Content_Page_Next_URL = "http://e.weibo.com/aj/mblog/mbloglist?page=${pageNum}&count=15&pre_page=${prePage}&pagebar=${pageBar}&uid=${oid}&_t=0";
	// http://weibo.com/aj/mblog/mbloglist?page=1&count=15&pre_page=1&pagebar=1&uid=2204248463&_t=0
	// 在做sina的enterprise版的ajax请求时，所带的referer参数
	// public static String Referer_Enter = "http://e.weibo.com/ruidemedia";
	public static String Referer_Enter = null;
	public static String Host_Enter = "e.weibo.com";

	/**
	 * 关于论论抓取的参数设置
	 */
	// 个人版和企业版设置
	// http://weibo.com/aj/comment/big?_wv=5&id=3557496809844150&max_id=3557528648706061&page=3&_t=0
	public static String Comment_PersonAndMedia_Wb_Content_Page_Next_URL = "http://weibo.com/aj/comment/big?_wv=5&page=${pageNum}&id=${firstId}&max_id=${max_id}&filter=0&__rnd=${rnd}";
	// http://e.weibo.com/aj/comment/big?id=3557857909551975&max_id=3557886272444317&page=2&_t=0&__rnd=1363749676571
	public static String Comment_ProAndEnter_Wb_Content_Page_Next_URL = "http://e.weibo.com/aj/comment/big?page=${pageNum}&id=${firstId}&_t=0";

	/**
	 * 关于新版新浪微博个人版的设置
	 * 
	 * @param args
	 */
	// public static String Person_Info_Req_URL =
	// "http://www.weibo.com/${uid}/info?from=profile&wvr=5&loc=tabinf&ajaxpagelet=1&_t=1354770347941";
	// public static String Person_Info_Req_URL =
	// "http://www.weibo.com/p/${page_id}/info?from==page_${doamin_id}&mod=TAB&ajaxpagelet=1&__ref=${ref}&_t=FM_137422681098811";
	public static String Person_Info_Req_URL = "http://www.weibo.com/p/${page_id}/info?from==page_${doamin_id}&mod=TAB&ajaxpagelet=1&__ref=${ref}&_t=FM_137422681098811";
	// http://www.weibo.com/2596706840/info?from=profile&wvr=5&loc=tabinf#profile_tab

	/**
	 * 以下为两个平台的公共参数的读取
	 * 
	 * @param args
	 */
	public static boolean sina_spider_enable = Boolean
			.parseBoolean(ReadSpiderConfig.getValue("sina_spider_enable"));
	public static boolean qq_spider_enable = Boolean
			.parseBoolean(ReadSpiderConfig.getValue("qq_spider_enable"));

	// before and after interval time if the time is undefined
	public static int before_after_interval_time = Integer
			.parseInt(ReadSpiderConfig.getValue("before_after_interval_time")) * 60 * 1000;

	// before and after interval time if the time is undefined,for LingJoin
	public static int before_after_interval_time_4_lingjoin = Integer
			.parseInt(ReadSpiderConfig
					.getValue("before_after_interval_time_4_lingjoin")) * 60 * 1000;

	public static String NotQQQuery = " [field] grab_method [or] cookies media enterprise profession ";
	public static String NotQQQuery_new = " [field] grab_method [or] cookies enterprise profession ";
	// public static String NotQQQuery=" [field] grab_method [not] qq ";
	// 要剔除的特殊字符
	// public static String
	// Specical_Char=ReadSpiderConfig.getValue("special_char");
	public static String Specical_Char = "[^,;.，。;!?\\-_A-Za-z\\d\\u4E00-\\u9FA5]";

	// 腾讯微博中，默认的图片网络路径
	public static String QQ_None_Pic_Path = "http://mat1.gtimg.com/www/mb/img/p1/head_normal_50.png";

	// 关于搜索时候的正负面设置
	public static String Good_Effect = "##正面JZSearch##";
	public static String Bad_Effect = "##负面JZSearch##";

	// 关于LJSearch Socket的设置
	public static int person_search_socket_num = Integer
			.parseInt(ReadSpiderConfig.getValue("person_search_socket_num"));
	public static int attention_search_socket_num = Integer
			.parseInt(ReadSpiderConfig.getValue("attention_search_socket_num"));
	public static int doc_search_socket_num = Integer.parseInt(ReadSpiderConfig
			.getValue("doc_search_socket_num"));

	public static int request_socket_fail_times = Integer
			.parseInt(ReadSpiderConfig.getValue("request_socket_fail_times"));

	public static long cookie_sleep_interval_time = Long
			.parseLong(ReadSpiderConfig.getValue("cookie_spider_interval_time"));

	// 关于redis参数
	public static String redis_host = ReadSpiderConfig.getValue("redis_host");
	public static int redis_port = Integer.parseInt(ReadSpiderConfig
			.getValue("redis_port"));
	public static String redis_password = ReadSpiderConfig
			.getValue("redis_password");

	// 字符集设置
	public static String charset = ReadSpiderConfig.getValue("charset");

	/**
	 * 数据挖掘方向的参数
	 */
	public static long Hotwords_Interval = Long.parseLong(ReadSpiderConfig
			.getValue("hotwords_interval")) * 60 * 1000;
	public static int Hotwords_Doc_Number = Integer.parseInt(ReadSpiderConfig
			.getValue("hotwords_doc_number"));
	public static String hotTopic_cache_file_path = ReadSpiderConfig
			.getValue("hotTopic_cache_file_path");
	/**
	 * 话题参数设置
	 */
	public static int topic_interval = Integer.parseInt(ReadSpiderConfig
			.getValue("Topic_Interval")) * 60 * 1000;
	public static String topic_content_page_next_url = "http://huati.weibo.com/aj_topic/list?keyword=${keyword}&all=1&pic=0&hasv=0&atten=0&prov=0&city=0&match_area=0&order=time&topicName=${keyword}&_t=0&p=${pageNumber}&__rnd=${time_long}";
	/**
	 * 话题TopicTitleManager中各变量的设置
	 */
	public static String toVisitTopicTitle_file_path = ReadSpiderConfig
			.getValue("toVisitTopicTitle_file_path");
	public static String circle_visitedTopicTitle_cache_file_path = ReadSpiderConfig
			.getValue("circle_visitedTopicTitle_cache_file_path");
	public static boolean topic_title_add_grab_task_enable = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("topic_title_add_grab_task_enable"));
	public static long topic_content_grab_too_fast_fresh_waitting_time = Long
			.parseLong(ReadSpiderConfig
					.getValue("topic_content_grab_too_fast_fresh_waitting_time")) * 1000;

	/**
	 * NLPIR DATA文件夹路径配置
	 */
	public static String NLPIR_DATA_PATH = ReadSpiderConfig
			.getValue("NLPIR_DATA_PATH");
	public static String NLPIR_DLL_PATH = ReadSpiderConfig
			.getValue("NLPIR_DLL_PATH");
	public static String NLPIR_Process_Encoding = ReadSpiderConfig
			.getValue("NLPIR_Process_Encoding");
	public static int NLPIR_Process_Encoding_Type = Integer
			.parseInt(ReadSpiderConfig.getValue("NLPIR_Process_Encoding_Type")
					.trim());
	public static int NLPIR_Extract_Keyword_Number = Integer
			.parseInt(ReadSpiderConfig.getValue("NLPIR_Extract_Keyword_Number")
					.trim());

	/**
	 * 验证码参数配置
	 */
	public static int verify_max_error_time = Integer.parseInt(ReadSpiderConfig
			.getValue("verify_max_error_time").trim());
	public static int verify_login_pic_index = 0;// 用它来解决ImageIcon图片缓加载的问题
	// 每隔多长时间验证一次，正在等待中的验证码是否已经被人工输入
	public static int verify_print_interval_time = Integer
			.parseInt(ReadSpiderConfig.getValue("verify_print_interval_time")) * 1000;

	// 空任务时候的轮回等待的时间间隔
	public static int keyword_or_cookie_null_task_waiting = Integer
			.parseInt(ReadSpiderConfig
					.getValue("keyword_or_cookie_null_task_waiting"));

	/**
	 * 关注的参数设置 2013-10-11
	 */
	public static String attention_req_url_first = FormatOutput.urlPrefix_no_www
			+ "p/${domain_id+uid}"
			+ "/follow?from=page_${domain_id}&mod=headfans&page=${pageNo}&ajaxpagelet=1&ajaxpagelet_v6=1&__ref=/p/${domain_id+uid}/home";
	public static String attention_req_url = FormatOutput.urlPrefix_no_www
			+ "p/${domain_id+uid}"
			+ "/follow?pids=${pids}&page=${pageNo}&ajaxpagelet=1&ajaxpagelet_v6=1&__ref=/p/${domain_id+uid}/follow";

	public static String fans_req_url_first = FormatOutput.urlPrefix_no_www
			+ "p/${domain_id+uid}"
			+ "/follow?relate=fans&from=${domain_id}&page=1&ajaxpagelet=1&__ref=/p/${domain_id+uid}/follow";
	public static String fans_req_url = FormatOutput.urlPrefix_no_www
			+ "p/${domain_id+uid}"
			+ "/follow?pids=${pids}&relate=fans&page=${pageNo}&ajaxpagelet=1&__ref=/p/${domain_id+uid}/follow";
	public static String nextPageString = "下一页";

	/**
	 * JZSearch command config
	 */
	public static int max_or_command_keyword_number = Integer
			.parseInt(ReadSpiderConfig
					.getValue("max_or_command_keyword_number"));

	/**
	 * 关于日志I/O的设置
	 */
	public static boolean log_output_enable = Boolean
			.parseBoolean(ReadSpiderConfig.getValue("log_output_file_enable"));

	/**
	 * 最后关于爬虫控制的扩展
	 */
	// 是否将转发者的uid加入到待抓取队列
	public static boolean forward_account_uid_add_tovisit = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("forward_account_uid_add_tovisit"));
	// 是否将抓取到的doc url加入到抓取对应的评论列表的队列中
	public static boolean doc_url_add_comment_tovisit = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("doc_url_add_comment_tovisit"));
	// 是否将抓取到的评论的uid加入待抓取帐户的队列
	public static boolean comment_uid_add_account_tovisit = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("comment_uid_add_account_tovisit"));
	// 是否将抓取到的评论者的uid加入待抓取的博文的队列
	public static boolean comment_uid_add_doc_tovisit = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("comment_uid_add_doc_tovisit"));
	// 是否将从搜索页找到的uid等，加入到抓取列表中
	public static boolean is_add_keyword_crawl_result_todo_task = Boolean
			.parseBoolean(ReadSpiderConfig
					.getValue("is_add_keyword_crawl_result_todo_task"));

}
