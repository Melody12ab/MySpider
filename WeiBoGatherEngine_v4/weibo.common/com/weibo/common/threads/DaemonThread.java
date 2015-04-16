package com.weibo.common.threads;

import org.apache.log4j.Logger;

import com.mbm.elint.manager.business.CircleTaskUrlManager;
import com.mbm.elint.manager.business.FeatureWordsManager;
import com.mbm.elint.manager.business.HotTopicManager;
import com.mbm.elint.manager.business.SinaKeySearchResultManager;
import com.mbm.elint.manager.business.UidToPicUrlManager;
import com.mbm.elint.manager.business.UrlToUidManager;
import com.mbm.elint.manager.business.spider.KeywordManager;
import com.mbm.elint.manager.business.spider.TopicTitleManager;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.mbm.elint.manager.database.DocInfoManager;
import com.mbm.elint.manager.database.PersonInfoManager;
import com.mbm.elint.manager.database.mongoDB.CommentDocInfoManager;
import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.StaticValue;
import com.weibo.utils.sina.FileOutputUtil;

public class DaemonThread implements Runnable {
	public static Logger logger = Logger.getLogger(DaemonThread.class);
	private MyThreadGroup threadGroup;
	public static int count = 0;// 计数，用于合理的输出数据

	// 用于记录评论列表数量的变化
	public long last_saveCommentDoc_number = 0;

	public long last_saveDoc_number = 0;
	public long last_saveSinaPersonInfo_number = 0;
	public long last_save_repeat_SinaPersonInfo_number = 0;
	public long last_saveKeyWordsDoc_number = 0;
	private long last_error_urls_number = 0;
	private long last_visited_urls_number = 0;
	private long last_new_words_size = 0;
	// 关于keyword守护
	// private long last_visited_keywords_size = 0;
	// 关于TopicTitle守护
	private long last_circle_visited_topic_title_size = 0;

	// 关于UUMap的守护
	private long last_UUMap_size = 0;

	// 关于topicMap的守护
	private long last_topicMap_size = 0;

	// 关于UUMap的守护
	private long last_UPMap_size = 0;

	// 关于周期任务的守护
	private long last_circle_urls_size = 0;
	private long last_circle_keywords_size = 0;
	// 关注的变量
	private long last_visitedAttentionUrls_size = 0;
	private UrlsManager urlsManager;

	public MyThreadGroup getThreadGroup() {
		return threadGroup;
	}

	public void setThreadGroup(MyThreadGroup threadGroup) {
		this.threadGroup = threadGroup;
	}

	public DaemonThread(MyThreadGroup threadGroup, String flag,
			UrlsManager urlsManager) {
		this.threadGroup = threadGroup;
		this.urlsManager = urlsManager;
	}

	private boolean isRunning = true;

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void run() {
		while (isRunning) {
			try {
				Thread.sleep(StaticValue.daemon_sleep_interval_time);// 睡多少s
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (count == 0) {
				// 关于KeyWord
				// last_visited_keywords_size = KeywordManager.visitedKeyWords
				// .size();
				last_saveKeyWordsDoc_number = SinaKeySearchResultManager.saved_keywordsDoc_number;
				// 关于topicTitle的守护
				last_circle_visited_topic_title_size = TopicTitleManager.visitedTopicTitle
						.size();

				// 关于sina_personInfo
				last_saveSinaPersonInfo_number = PersonInfoManager.saved_personInfo_number;
				last_save_repeat_SinaPersonInfo_number = PersonInfoManager.saved_repeat_sina_personInfo_number;

				// 关于特征词提取的缓存设置
				last_new_words_size = FeatureWordsManager.getFeatureMapSize();

				// 关于UUMap的size的保存
				last_UUMap_size = UrlToUidManager.getUUMapSize();

				// 关于UUMap的size的保存
				last_topicMap_size = HotTopicManager.getTopicMapSize();

				// 关于UPMap的size的保存
				last_UPMap_size = UidToPicUrlManager.getUPMapSize();

				// 关于周期任的参数
				last_circle_urls_size = CircleTaskUrlManager.getListSize();
				last_circle_keywords_size = KeywordManager.visitedKeyWords
						.size();

				// 关于已采集关注的记录
				last_visitedAttentionUrls_size = UrlsManager.visitedAttentionUrls
						.size();

				last_saveDoc_number = DocInfoManager.saved_doc_number;
				// 记录commentDoc列表数量的变化
				last_saveCommentDoc_number = CommentDocInfoManager.saved_commentDoc_number;

				last_error_urls_number = UrlsManager.errorUrls_urls_size;
				last_visited_urls_number = UrlsManager.visitedUrls_urls_size;
				logger.info("正在运行的grab threads number------"
						+ this.threadGroup.activeCount());
				logger
						.info("待抓取url共------"
								+ (UrlsManager.toVisitUrls.size() + UrlsManager.toVisitAttentionUrls
										.size()));
				logger.info("正在抓取url共------" + UrlsManager.visitingUrls.size());
				logger.info("已抓取url共------" + UrlsManager.visitedUrls.size());
				logger.info("重复的url共------" + UrlsManager.repeat_urls);
				logger.info("错误urls共------" + (last_error_urls_number));

				// keyword守护输出
				logger.info("待抓取keywords共------"
						+ KeywordManager.toVisitKeyWords.size());
				logger.info("正在抓取keywords共------"
						+ KeywordManager.visitingKeyWords.size());
				logger.info("已抓取keywords共------"
						+ KeywordManager.visitedKeyWords.size());
				logger.info("已抓取内容列表的topic title共------"
						+ TopicTitleManager.visitedTopicTitle.size());
				count++;
			} else if (count <= StaticValue.output_bloomFilter_circle) {
				logger.info("守护线程运行中");
				count++;
			} else {
				/**
				 * 将doc_bloomFilter写入文件
				 */
				if (DocInfoManager.saved_doc_number > last_saveDoc_number
						|| SinaKeySearchResultManager.saved_keywordsDoc_number > last_saveKeyWordsDoc_number) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.doc_bloomFilter_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(DocInfoManager.bloomFilter))) {
							logger.info("redis--本次的定时将doc_bloomFilter现状写入成功");
							rest();// 休息一下
						} else {
							logger.info("redis--本次的定时将doc_bloomFilter现状写入失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("redis--本次的定时将doc_bloomFilter现状写入失败");
					}
				} else {
					logger
							.info("redis--本次没有新增的抓取成功的doc文档,无需重新将doc_bloomFilter写入");
				}

				/**
				 * 将commentDoc_bloomFilter写入文件 先注释掉，为202而改变
				 */
				/*
				 * if (CommentDocInfoManager.saved_commentDoc_number >
				 * last_saveCommentDoc_number) { try { if (JedisOperatorUtil
				 * .putObj( StringTransform .getByteArray(
				 * StaticValue.commentDoc_bloomFilter_file_path,
				 * StaticValue.charset), ObjectAndByteArrayConvertUtil
				 * .ObjectToByteArray(CommentDocInfoManager.bloomFilter))) {
				 * logger .info("redis--本次的定时将commentDoc_bloomFilter现状写入成功");
				 * rest();//休息一下 } else { logger
				 * .info("redis--本次的定时将commentDoc_bloomFilter现状写入失败"); } } catch
				 * (Exception e) { e.printStackTrace(); logger
				 * .info("redis--本次的定时将commentDoc_bloomFilter现状写入失败"); } } else
				 * { logger
				 * .info("redis--本次没有新增的抓取成功的评论列表内容,无需重新将commentDoc_bloomFilter写入"
				 * ); }
				 */
				/**
				 * 将visitedUrls_bloomFilter写入文件
				 */
				if (UrlsManager.visitedUrls_urls_size > last_visited_urls_number) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.visitedUrls_bloomFilter_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(UrlsManager.visitedUrlsBloomFilter))) {
							logger
									.info("redis--本次的定时将visitedUrls_bloomFilter现状写入文件成功");
							rest();// 休息一下
						} else {
							logger
									.info("redis--本次的定时将visitedUrls_bloomFilter现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger
								.info("redis--本次的定时将visitedUrls_bloomFilter现状写入文件失败");
					}
				} else {
					logger
							.info("redis--本次没有新增的抓取成功的url,无需重新将visitedUrls_bloomFilter写入至文件中");
				}
				/**
				 * 将sina_personInfo_bloomFilter写入文件
				 */
				if (PersonInfoManager.saved_personInfo_number > last_saveSinaPersonInfo_number) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.sina_personInfo_bloomFilter_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(PersonInfoManager.bloomFilter))) {
							logger
									.info("redis--本次的定时将sina_personInfo_bloomFilter现状写入文件成功");
							rest();// 休息一下
						} else {
							logger
									.info("redis--本次的定时将sina_personInfo_bloomFilter现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger
								.info("redis--本次的定时将sina_personInfo_bloomFilter现状写入文件失败");
					}
				} else {
					logger
							.info("redis--本次没有新增的抓取成功的sina_personInfo文档,无需重新将sina_personInfo_bloomFilter写入至文件中");
				}
				/**
				 * 将check_repeat_sina_personInfo_bloomFilter写入文件
				 */
				if (PersonInfoManager.saved_repeat_sina_personInfo_number > last_save_repeat_SinaPersonInfo_number) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.check_repeat_sina_personInfo_bloomFilter_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(PersonInfoManager.check_repeat_sina_personInfo_bloomFilter))) {
							logger
									.info("redis--本次的定时将check_repeat_sina_personInfo_bloomFilter现状写入文件成功");
							rest();// 休息一下
						} else {
							logger
									.info("redis--本次的定时将check_repeat_sina_personInfo_bloomFilter现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger
								.info("redis--本次的定时将check_repeat_sina_personInfo_bloomFilter现状写入文件失败");
					}
				} else {
					logger
							.info("redis--本次没有新增的抓取成功的check_repeat_sina_personInfo文档,无需重新将check_repeat_sina_personInfo_bloomFilter写入至文件中");
				}

				/**
				 * UUMap(Url to Uid)所做存储的路径
				 */
				if (UrlToUidManager.getUUMapSize() > last_UUMap_size) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.UUMap_cache_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(UrlToUidManager.UUMap))) {
							logger.info("redis--本次的定时将UUMap现状写入文件成功");
							rest();// 休息一下
						} else {
							logger.info("redis--本次的定时将UUMap现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("redis--本次的定时将UUMap现状写入文件失败");
					}
					// 此处的没必要清除,故不做else的其它操作
				} else {
					logger.info("此时的UUMap没有变化，无需写入文件!");
				}

				/**
				 * HotTopicMap所做存储的路径
				 */
				if (HotTopicManager.getTopicMapSize() > last_topicMap_size) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.hotTopic_cache_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(HotTopicManager.topicMap))) {
							logger.info("redis--本次的定时将topicMap现状写入文件成功");
							rest();// 休息一下
						} else {
							logger.info("redis--本次的定时将topicMap现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("redis--本次的定时将topicMap现状写入文件失败");
					}
					// 此处的没必要清除,故不做else的其它操作
				} else {
					logger.info("此时的topicMap没有变化，无需写入文件!");
				}

				/**
				 * UPMap(qq uid to pic path)所做存储的路径
				 */
				if (UidToPicUrlManager.getUPMapSize() > last_UPMap_size) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.UPMap_cache_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(UidToPicUrlManager.UPMap))) {
							logger.info("redis--本次的定时将UPMap现状写入文件成功");
							rest();// 休息一下
						} else {
							logger.info("redis--本次的定时将UPMap现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("本次的定时将UPMap现状写入文件失败");
					}
					// 此处的没必要清楚,故不做else的其它操作
				} else {
					logger.info("此时的UPMap没有变化，无需写入文件!");
				}
				/**
				 * CircleTaskUrlManager 所做存储的路径
				 */
				if (CircleTaskUrlManager.getListSize() != last_circle_urls_size) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.circle_urls_cache_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(CircleTaskUrlManager.circleUrls))) {
							logger.info("redis--本次的定时将circleUrls现状写入文件成功");
							rest();// 休息一下
						} else {
							logger.info("redis--本次的定时将circleUrls现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("本次的定时将circleUrls现状写入文件失败");
					}
					// 此处的没必要清楚,故不做else的其它操作
				} else {
					logger.info("此时的circleUrls没有变化，无需写入文件!");
				}

				/**
				 * visitedKeyWords 所做存储的路径
				 */
				if (KeywordManager.visitedKeyWords.size() != last_circle_keywords_size) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.circle_visitedKeyWords_cache_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(KeywordManager.visitedKeyWords))) {
							logger.info("redis--本次的定时将visitedKeyWords现状写入文件成功");
							rest();// 休息一下
						} else {
							logger.info("redis--本次的定时将visitedKeyWords现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("本次的定时将visitedKeyWords现状写入文件失败");
					}
					// 此处的没必要清楚,故不做else的其它操作
				} else {
					logger.info("此时的visitedKeyWords没有变化，无需写入文件!");
				}

				/**
				 * circle_visited_topic_tile 所做存储的路径
				 */
				if (TopicTitleManager.visitedTopicTitle.size() != last_circle_visited_topic_title_size) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.circle_visitedTopicTitle_cache_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(TopicTitleManager.visitedTopicTitle))) {
							logger
									.info("redis--本次的定时将visitedTopicTitle set现状写入文件成功");
							rest();// 休息一下
						} else {
							logger
									.info("redis--本次的定时将visitedTopicTitle set现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("本次的定时将visitedTopicTitle set现状写入文件失败");
					}
					// 此处的没必要清楚,故不做else的其它操作
				} else {
					logger.info("此时的visitedTopicTitle set没有变化，无需写入文件!");
				}

				/**
				 * visitedAttentionUrls 所做存储的路径
				 */
				if (UrlsManager.visitedAttentionUrls.size() > last_visitedAttentionUrls_size) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.visitedAttentionUrls_set_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(UrlsManager.visitedAttentionUrls))) {
							logger
									.info("redis--本次的定时将visitedAttentionUrls现状写入文件成功");
							rest();// 休息一下
						} else {
							logger
									.info("redis--本次的定时将visitedAttentionUrls现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger
								.info("redis--本次的定时将visitedAttentionUrls现状写入文件失败");
					}
					// 此处的没必要清楚,故不做else的其它操作
				} else {
					logger.info("此时的visitedAttentionUrls没有变化，无需写入文件!");
				}

				/**
				 * 将toVisitKeyWords_bloomFilter写入文件，主要是为了当大数据量提交出现异常时，
				 * 作为对未完成任务的保护
				 */
				if (KeywordManager.toVisitKeyWords.size() > 0) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.toVisitKeyWords_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(KeywordManager.toVisitKeyWords))) {
							logger.info("redis--本次的定时将toVisitKeyWords现状写入文件成功");
							rest();// 休息一下
						} else {
							logger.info("redis--本次的定时将toVisitKeyWords现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("redis--本次的定时将toVisitKeyWords现状写入文件失败");
					}
				} else {
					try {
						JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.toVisitKeyWords_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(KeywordManager.toVisitKeyWords));
					} catch (Exception e) {
						e.printStackTrace();
					}
					rest();// 休息一下
					logger
							.info("此时的toVisitKeyWords为空，已将保存的toVisitKeyWords文件清空!");
				}
				/**
				 * 将toVisitedUrls_bloomFilter写入文件，主要是为了当大数据量提交出现异常时，作为对未完成任务的保护
				 */
				if (UrlsManager.toVisitUrls.size() > 0) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.toVisitUrls_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(UrlsManager.toVisitUrls))) {
							logger
									.info("redis--本次的定时将toVisitUrls_list现状写入文件成功");
							rest();// 休息一下
						} else {
							logger
									.info("redis--本次的定时将toVisitUrls_list现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("redis--本次的定时将toVisitUrls_list现状写入文件失败");
					}
				} else {
					try {
						JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.toVisitUrls_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(UrlsManager.toVisitUrls));
						rest();// 休息一下
					} catch (Exception e) {
						e.printStackTrace();
					}
					logger
							.info("redis--此时的toVisitUrls_list为空，已将保存的toVisitedUrls文件清空!");
				}

				/**
				 * 将toVisitCommentUrls写入文件，主要是为了当大数据量提交出现异常时，作为对未完成任务的保护
				 */
				if (UrlsManager.toVisitCommentUrls.size() > 0) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.toVisitCommentUrls_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(UrlsManager.toVisitCommentUrls))) {
							logger
									.info("redis--本次的定时将toVisitCommentUrls现状写入文件成功");
							rest();// 休息一下
						} else {
							logger
									.info("redis--本次的定时将toVisitCommentUrls现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("redis--本次的定时将toVisitCommentUrls现状写入文件失败");
					}
				} else {
					try {
						JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.toVisitCommentUrls_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(UrlsManager.toVisitCommentUrls));
						rest();// 休息一下
					} catch (Exception e) {
						e.printStackTrace();
					}
					logger
							.info("redis--此时的toVisitCommentUrls为空，已将保存的toVisitCommentUrls文件清空!");
				}

				/**
				 * 特征词所做存储的路径
				 */
				if (FeatureWordsManager.getFeatureMapSize() > last_new_words_size) {
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.featureWords_cache_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(FeatureWordsManager.featureMap))) {
							logger.info("redis--本次的定时将featureMap现状写入文件成功");
							rest();// 休息一下
						} else {
							logger.info("redis--本次的定时将featureMap现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("redis--本次的定时将featureMap现状写入文件失败");
					}
					// 此处的没必要清除,故不做else的其它操作
				} else {
					logger.info("此时的featureMap没有变化，无需写入文件!");
				}

				/**
				 * 将error_urls写入文件,并将errorUrls的bloomFilter过滤
				 */
				if (UrlsManager.errorUrls.size() == 0) {
					logger.info("errorUrls列表为空，不需要写入文件了");
				} else {
					logger.info("正将错误的urls写入到errorUrls.txt文件");
					last_error_urls_number += UrlsManager.errorUrls.size();
					FileOutputUtil.printErrorUrlsList(
							StaticValue.errorUrls_file_path,
							UrlsManager.errorUrls);
					this.urlsManager.clearErrorList();
					logger.info("已将error_list清空!");
					logger.info("errorUrls写入文件成功");
					logger.info("正将错误的errorUrlsBloomFilter写入到文件中");
					try {
						if (JedisOperatorUtil
								.putObj(
										StringTransform
												.getByteArray(
														StaticValue.errorUrls_bloomFilter_file_path,
														StaticValue.charset),
										ObjectAndByteArrayConvertUtil
												.ObjectToByteArray(UrlsManager.errorUrlsBloomFilter))) {
							logger
									.info("redis---本次的定时将errorUrlbloom过滤器现状写入文件成功");
							rest();// 休息一下
						} else {
							logger.info("redis---本次的定时将errorUrlsbloom现状写入文件失败");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("redis---本次的定时将errorUrlbloom过滤器现状写入文件失败");
					}
				}
				count = 0;
			}
			// 暂无退出
			// if (UrlsManager.visitingUrls.size() == 0
			// && UrlsManager.toVisitUrls.size() == 0
			// && ApiTask.isRunning == false
			// && CookieTask.isRunning == false) {
			//
			// logger.info("程序执行完毕，即将退出");
			// System.exit(0);
			// }
		}
	}

	public void rest() {
		try {
			logger.info("缓存写入成功之后，即将休息指定时间");
			Thread.sleep(StaticValue.daemon_write_success_rest_time);
			logger.info("缓存写入成功之后，休息完成");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
