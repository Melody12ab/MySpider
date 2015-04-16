package com.mbm.elint.manager.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.FeatureWord;
import com.dw.party.mbmsupport.dto.SearchAccountResultPojo;
import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.dw.party.mbmsupport.dto.SupportSort;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.dw.party.mbmsupport.global.Emotion;
import com.mbm.LJSearch.manager.LJSearchManager;
import com.mbm.elint.entity.mongoDB.TopicDoc;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.manager.database.PersonAttentionManager;
import com.weibo.common.utils.ResultPage;
import com.weibo.common.utils.StaticValue;
import com.weibo.utils.sina.MyStringUtils;

@Component
public class SearchControlerManager {
	private static Logger logger = LoggerFactory
			.getLogger(SearchControlerManager.class);
	@Autowired
	private MyJson myJSON;
	/**
	 * 为处理从数据库中查询关注或是粉丝列表而添加
	 */
	@Autowired
	private PersonAttentionManager personAttentionManager;
	/**
	 * 统一改成NLPIR的分词
	 */
//	public static NlpirManager nlpirManager = null;
//	static {
//		nlpirManager = new NlpirManager();
//	}

	@Autowired
	private LJSearchManager ljSearchManager;
	@Autowired
	private FeatureWordsManager<FeatureWord> featureWordsManager;
	@Autowired
	private HotTopicManager<TopicDoc> hotTopicManager;
	@Autowired
	private UrlToUidManager uuMapManager;

	public ResultPage<SearchAccountResultPojo> findAccountInfoListByUids(
			List<String> uids, ResultPage<SearchAccountResultPojo> page) {
		String query = "";
		for (String temp : uids) {
			query += (temp + " ");
		}
		query = query.trim();
		page = ljSearchManager.findAccountInfoPageByUids(query, page);

		return page;
	}

	public ResultPage<SearchAccountResultPojo> findAccountInfoList(
			List<String> accountUrls, ResultPage<SearchAccountResultPojo> page) {
		/**
		 * 此map用来做url-uid的暂存集合，保证请的url和返回的url一致
		 */
		Map<String, String> temp_map = new HashMap<String, String>();
		String query = "";
		String temp_map_value = "";
		for (String temp : accountUrls) {
			temp_map_value = uuMapManager.getValueByKey(MyStringUtils
					.getUrlRemoveSlash(temp));
			temp_map.put(temp_map_value, temp);
			query += temp_map_value + " ";
		}
		query = query.trim();
		page = ljSearchManager.findAccountInfoPage(query, page);
		/**
		 * 在这里对page中的account信息做下过滤，保证请求过来的url和返回的url是一个地址
		 */
		List<SearchAccountResultPojo> list = page.getResult();

		for (SearchAccountResultPojo accountPojo : list) {
			accountPojo
					.setUrl(temp_map.get(accountPojo.getUid()) == null ? accountPojo
							.getUrl()
							: temp_map.get(accountPojo.getUid()));
		}

		temp_map = null;// 返还资源

		return page;
	}

	public ResultPage<SearchAccountResultPojo> findAccountAttentionList(
			String accountUrl, ResultPage<SearchAccountResultPojo> sendPage) {
		accountUrl = MyStringUtils.getUrlRemoveSlash(accountUrl);
		ResultPage<SearchAccountResultPojo> resultPage = ljSearchManager
				.findAccountAttentionList(accountUrl, sendPage);
		return resultPage;
	}

	public ResultPage<SearchContentResultPojo> findContentInfoList(
			List<String> accountUrls, List<String> keys, String group,
			java.util.Date startPublishTime, java.util.Date endPublishTime,
			java.util.Date startGrabTime, java.util.Date endGrabTime,
			java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage) {
		sendPage = ljSearchManager.findContentInfoPage(accountUrls, keys,
				group, startPublishTime, endPublishTime, startGrabTime,
				endGrabTime, sort, sendPage);
		return sendPage;
	}

	public ResultPage<SearchContentResultPojo> findNewestContentInfoPage(
			List<String> accountUrls, List<String> keys,
			java.util.Date startPublishTime, java.util.Date endPublishTime,
			java.util.Date startGrabTime, java.util.Date endGrabTime,
			java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage, int flag) {
		sendPage = ljSearchManager.findNewestContentInfoPage(accountUrls, keys,
				startPublishTime, endPublishTime, startGrabTime, endGrabTime,
				sort, sendPage, flag);
		// sendPage.setPageNo(pageNo)
		return sendPage;
	}

	public ResultPage<SearchContentResultPojo> findNewestContentInfoPageWithEmotion(
			List<String> accountUrls, List<String> keys,
			java.util.Date startPublishTime, java.util.Date endPublishTime,
			java.util.Date startGrabTime, java.util.Date endGrabTime,
			java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage, Emotion emotion) {
		sendPage = ljSearchManager.findNewestContentInfoPageWithEmotion(
				accountUrls, keys, startPublishTime, endPublishTime,
				startGrabTime, endGrabTime, sort, sendPage, emotion);
		return sendPage;
	}

	/**
	 * 获取特征列表
	 * 
	 * @param uidList
	 * @param page
	 * @return
	 */
//	public ResultPage<FeatureWord> findPersonFeatureWordsBySendUrl(
//			String sendUrl, int operatorType) {
//		ResultPage<FeatureWord> page = new ResultPage<FeatureWord>(
//				StaticValue.feature_word_number);
//		ResultPage<SearchContentResultPojo> contentPage = new ResultPage<SearchContentResultPojo>(
//				StaticValue.feature_doc_number);
//		if (sendUrl == null || sendUrl.trim().length() == 0) {
//			page.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
//			page.setInfo("所传的uid不可为空");
//		} else {
//			if (operatorType == 0) {
//				contentPage = ljSearchManager.findPersonFeatureWordsBySendUrl(
//						sendUrl, contentPage);
//				/**
//				 * 下面对content分析获得个人的特征词
//				 */
//				StringBuilder sb = new StringBuilder();
//				List<SearchContentResultPojo> contentList = contentPage
//						.getResult();
//				for (SearchContentResultPojo pojo : contentList) {
//					sb.append(pojo.getContent() + "\\n");
//				}
//				// logger.info("可分析的doc串-------------"+sb.toString());
//				page.setResult(nlpirManager.getSortFeatureWords(sb.toString(),
//						StaticValue.feature_word_number));
//
//				featureWordsManager.addFeatureMap(sendUrl, page);
//				page.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
//				page.setInfo("该特征集是实时获取的!");
//			} else {
//				if (featureWordsManager.isInFeatureMap(sendUrl)) {
//					page = featureWordsManager.getOneFeature(sendUrl);
//					page
//							.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
//					page.setInfo("此处是从缓存中取得数据!");
//				} else {
//					/**
//					 * 当从缓存中取得数据取不到时,为保证数据,就会实时获取一次!
//					 */
//					contentPage = ljSearchManager
//							.findPersonFeatureWordsBySendUrl(sendUrl,
//									contentPage);
//					/**
//					 * 下面对content分析获得个人的特征词
//					 */
//					StringBuilder sb = new StringBuilder();
//					List<SearchContentResultPojo> contentList = contentPage
//							.getResult();
//					for (SearchContentResultPojo pojo : contentList) {
//						sb.append(pojo.getContent() + "\\n");
//					}
//					page.setResult(nlpirManager.getSortFeatureWords(sb
//							.toString(), StaticValue.feature_word_number));
//
//					page
//							.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
//					page.setInfo("在缓存中没有取到数据，又实时获取了一下!");
//					featureWordsManager.addFeatureMap(sendUrl, page);
//					return page;
//				}
//			}
//		}
//		return page;
//	}

	/**
	 * 获取特征列表 operatorType 是否实时获取热词列表
	 * 
	 * @param operatorType
	 * @return
	 */
//	public ResultPage<FeatureWord> findTopHotWords(int operatorType) {
//		ResultPage<FeatureWord> page = new ResultPage<FeatureWord>(
//				StaticValue.feature_word_number);
//		ResultPage<SearchContentResultPojo> contentPage = new ResultPage<SearchContentResultPojo>(
//				StaticValue.Hotwords_Doc_Number);
//		if (operatorType == 0) {
//			// 时间段组成
//			List<SupportSort> sortList = new ArrayList<SupportSort>();
//			SupportSort sort1 = new SupportSort("publishtime", "desc");
//			sortList.add(sort1);
//
//			Date endTime = new Date();
//			Date beginTime = DateUtil.getBeforeIntervalDate(endTime,
//					StaticValue.Hotwords_Interval);
//			contentPage = ljSearchManager.findNewestContentInfoPage(null, null,
//					beginTime, endTime, null, null, sortList, contentPage, 0);
//			/**
//			 * 下面对content分析获得个人的特征词
//			 */
//			StringBuilder sb = new StringBuilder();
//			List<SearchContentResultPojo> contentList = contentPage.getResult();
//			for (SearchContentResultPojo pojo : contentList) {
//				sb.append(pojo.getContent() + "\\n");
//			}
//			// logger.info("可分析的doc串-------------"+sb.toString());
//			page.setResult(nlpirManager.getSortFeatureWords(sb.toString(),
//					StaticValue.feature_word_number));
//
//			featureWordsManager.addFeatureMap("hotword", page);
//			page.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
//			page.setInfo("该特征集是实时获取的!");
//		} else {
//			if (featureWordsManager.isInFeatureMap("hotword")) {
//				page = featureWordsManager.getOneFeature("hotword");
//				page.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
//				page.setInfo("此处是从缓存中取得数据!");
//			} else {
//				/**
//				 * 当从缓存中取得数据取不到时,为保证数据,就会实时获取一次!
//				 */
//				// 时间段组成
//				List<SupportSort> sortList = new ArrayList<SupportSort>();
//				SupportSort sort1 = new SupportSort("publishtime", "desc");
//				sortList.add(sort1);
//
//				Date endTime = new Date();
//				Date beginTime = DateUtil.getBeforeIntervalDate(endTime,
//						StaticValue.Hotwords_Interval);
//				contentPage = ljSearchManager.findNewestContentInfoPage(null,
//						null, beginTime, endTime, null, null, sortList,
//						contentPage, 0);
//				/**
//				 * 下面对content分析获得个人的特征词
//				 */
//				StringBuilder sb = new StringBuilder();
//				List<SearchContentResultPojo> contentList = contentPage
//						.getResult();
//				for (SearchContentResultPojo pojo : contentList) {
//					sb.append(pojo.getContent() + "\\n");
//				}
//				page.setResult(nlpirManager.getSortFeatureWords(sb.toString(),
//						StaticValue.feature_word_number));
//
//				page.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
//				page.setInfo("在缓存中没有取到数据，又实时获取了一下!");
//				featureWordsManager.addFeatureMap("hotword", page);
//				return page;
//			}
//		}
//		return page;
//	}

	public ResultPage<TopicDoc> findTopHotTopics(int operatorType) {
		ResultPage<TopicDoc> resultPage = new ResultPage<TopicDoc>();
		if (operatorType == 0) {
			List<SupportSort> sortList = new ArrayList<SupportSort>();
			SupportSort sort1 = new SupportSort("publishTime", "desc");
			sortList.add(sort1);

			resultPage = ljSearchManager.findHotTopicInfoPage(sortList,
					resultPage);

			hotTopicManager.addTopicToMap("hotTopic", resultPage);
			resultPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			resultPage.setInfo("该特征集是实时获取的!");
		} else {
			if (hotTopicManager.isInTopicMap("hotTopic")) {
				resultPage = hotTopicManager.getTopics("hotTopic");
				resultPage
						.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
				resultPage.setInfo("此处是从缓存中取得数据!");
			} else {
				/**
				 * 当从缓存中取得数据取不到时,为保证数据,就会实时获取一次!
				 */
				List<SupportSort> sortList = new ArrayList<SupportSort>();
				SupportSort sort1 = new SupportSort("publishTime", "desc");
				sortList.add(sort1);

				resultPage = ljSearchManager.findHotTopicInfoPage(sortList,
						resultPage);

				hotTopicManager.addTopicToMap("hotTopic", resultPage);
				resultPage
						.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
				resultPage.setInfo("缓存获取失败后,又实时获取的!");
			}
		}
		return resultPage;
	}

	/**
	 * 获取特征列表--此是给用户特开的一个接口，传进某字符串计算出特征词
	 * 
	 * @param uidList
	 * @param page
	 * @return
	 */
//	public ResultPage<FeatureWord> findPersonFeatureWordsByContent(
//			String content) {
//		ResultPage<FeatureWord> page = new ResultPage<FeatureWord>(
//				StaticValue.feature_word_number);
//		if (content == null || content.trim().length() == 0) {
//			page.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
//			page.setInfo("所传的uid不可为空");
//		} else {
//			/**
//			 * 下面对任意的content分析获得个人的特征词
//			 */
//			// logger.info("可分析的doc串-------------"+sb.toString());
//			page.setResult(nlpirManager.getSortFeatureWords(content,
//					StaticValue.feature_word_number));
//
//			page.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
//			page.setInfo("该特征集是实时获取的!");
//		}
//		return page;
//	}

	/**
	 * 为转发而新加
	 * 
	 * @param docUrls
	 * @param sendPage
	 * @return
	 */
	public ResultPage<SearchContentResultPojo> findDocInfoListByDocUrls(
			List<String> docUrls, ResultPage<SearchContentResultPojo> sendPage) {
		ResultPage<SearchContentResultPojo> resultPage = ljSearchManager
				.findDocInfoListByDocUrls(docUrls, sendPage);
		return resultPage;
	}

	/*
	 * 以下是为缔元信而添加的
	 */
	public String findContentInfoList(String args[]) {
		if (args != null && args.length == 1) {
			ResultPage<SearchContentResultPojo> sendPage = new ResultPage<SearchContentResultPojo>(
					StaticValue.top_doc_number);
			/**
			 * 解析该args
			 */
			myJSON.createJson(args[0]);

			sendPage = ljSearchManager.findContentInfoPage(myJSON, sendPage);
			List<SearchContentResultPojo> list = sendPage.getResult();
			if (list == null || list.size() == 0) {
				myJSON.put("address", "dratio");
				myJSON.put("result", "error");
				myJSON.put("size", 0);
				myJSON.put("data", "无数据");
			} else {
				myJSON.put("address", "dratio");
				myJSON.put("result", "success");
				myJSON.put("size", list.size());
				myJSON.put("data", list.toString());
			}
		} else {
			myJSON.put("address", "dratio");
			myJSON.put("result", "error");
			myJSON.put("size", "0");
			myJSON.put("data", "传参有误!");
		}
		return myJSON.toString();
	}

	/**
	 * for LingJoin
	 */
	public ResultPage<SearchContentResultPojo> findContentInfoList(
			List<String> accountUrls, List<String> uids, List<String> keys,
			java.util.Date startPublishTime, java.util.Date endPublishTime,
			java.util.Date startGrabTime, java.util.Date endGrabTime,
			java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage) {
		sendPage = ljSearchManager.findContentInfoPage(accountUrls, uids, keys,
				startPublishTime, endPublishTime, startGrabTime, endGrabTime,
				sort, sendPage);
		return sendPage;
	}

	public ResultPage<SearchAccountResultPojo> findContentInfoList(
			List<String> uids, ResultPage<SearchAccountResultPojo> page) {
		/**
		 * 此map用来做url-uid的暂存集合，保证请的url和返回的url一致
		 */
		Map<String, String> temp_map = new HashMap<String, String>();
		String query = "";
		for (String uid : uids) {
			query += uid + " ";
		}
		query = query.trim();
		page = ljSearchManager.findContentInfoPage(query, page);
		/**
		 * 在这里对page中的account信息做下过滤，保证请求过来的url和返回的url是一个地址
		 */
		List<SearchAccountResultPojo> list = page.getResult();

		for (SearchAccountResultPojo accountPojo : list) {
			accountPojo
					.setUrl(temp_map.get(accountPojo.getUid()) == null ? accountPojo
							.getUrl()
							: temp_map.get(accountPojo.getUid()));
		}

		temp_map = null;// 返还资源

		return page;
	}

	public ResultPage<SearchAccountResultPojo> findAccountInfoListForLingJoin(
			List<String> keywords_name, ResultPage<SearchAccountResultPojo> page) {
		/**
		 * 此map用来做url-uid的暂存集合，保证请的url和返回的url一致
		 */
		Map<String, String> temp_map = new HashMap<String, String>();
		String query = "";
		String temp_map_value = "";
		for (String temp : keywords_name) {
			query += temp + " ";
		}
		query = query.trim();
		page = ljSearchManager.findAccountInfoPageForLingJoin(query, page,
				false);
		/**
		 * 在这里对page中的account信息做下过滤，保证请求过来的url和返回的url是一个地址
		 */
		List<SearchAccountResultPojo> list = page.getResult();

		for (SearchAccountResultPojo accountPojo : list) {
			accountPojo
					.setUrl(temp_map.get(accountPojo.getUid()) == null ? accountPojo
							.getUrl()
							: temp_map.get(accountPojo.getUid()));
		}

		temp_map = null;// 返还资源

		return page;
	}

	/**
	 * for HuaWei
	 * 
	 * @param ss
	 * @param page
	 * @return
	 */
	public ResultPage<SearchAccountResultPojo> findAllAccountInfoListForHuaWei(
			SupportSort ss, ResultPage<SearchAccountResultPojo> page) {
		/**
		 * 为加速直接以list all为query
		 */
		String query = "<index><no>1</no><main>1</main><cmd> [cmd] listall [sort] reverse_docid</cmd></index>";

		query = query.trim();
		page = ljSearchManager
				.findAccountInfoPageForLingJoin(query, page, true);

		return page;
	}

	public String findAttentionListByUid(String uid) {
		if (uid == null || uid.trim().length() == 0 || uid.trim().length() > 30) {
			return "uid is not valid!";
		}
		return this.personAttentionManager.findAttentionListByUid(uid);
	}

	public String findFansListByUid(String uid) {
		if (uid == null || uid.trim().length() == 0 || uid.trim().length() > 30) {
			return "uid is not valid!";
		}
		return this.personAttentionManager.findFansListByUid(uid);
	}

	public String findMutualFansListByUid(String uid) {
		if (uid == null || uid.trim().length() == 0 || uid.trim().length() > 30) {
			return "uid is not valid!";
		}
		return this.personAttentionManager.findMutualFansListByUid(uid);
	}

	public ResultPage<SearchContentResultPojo> findAllContentInfoList(
			SupportSort sort, ResultPage<SearchContentResultPojo> sendPage) {
		sendPage = ljSearchManager.findAllContentInfoPage(sort, sendPage);
		return sendPage;
	}

}
