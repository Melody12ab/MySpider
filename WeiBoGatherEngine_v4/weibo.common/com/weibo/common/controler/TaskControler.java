package com.weibo.common.controler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.dto.ResultGrab;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.TypePojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.CircleTaskUrlManager;
import com.mbm.elint.manager.business.LibraryManager;
import com.mbm.elint.manager.business.spider.KeywordManager;
import com.mbm.elint.manager.business.spider.TopicTitleManager;
import com.mbm.elint.manager.business.spider.UrlsManager;
import com.weibo.common.utils.StaticValue;
import com.weibo.utils.sina.MyStringUtils;

@Component
public class TaskControler {
	public static Logger logger = Logger.getLogger(TaskControler.class);
	@Autowired
	private LibraryManager libraryManager;
	public static UrlsManager urlsManager = null;
	public static TopicTitleManager topicTitleManager = null;
	// public static KeywordManager keyWordManager = null;
	public static KeywordManager keyWordManager = new KeywordManager();

	public boolean addAccountGrabTask(List<UrlPojo> accountUrls) {
		urlsManager.addUrlsListFirst(accountUrls);
		return true;
	}

	public List<ResultGrab> isFinishedAccountGrab(List<UrlPojo> accountUrls) {
		List<ResultGrab> resultGrabList = new ArrayList<ResultGrab>();
		ResultGrab resultGrab = null;
		for (UrlPojo temp_url : accountUrls) {
			if (UrlsManager.toVisitUrls.contains(temp_url)) {
				resultGrab = new ResultGrab(
						BlogRemoteDefine.GRAB_RESULT.WAITING, temp_url);
			} else if (UrlsManager.visitingUrls.contains(temp_url)) {
				resultGrab = new ResultGrab(BlogRemoteDefine.GRAB_RESULT.DOING,
						temp_url);
			} else if (UrlsManager.visitedUrlsBloomFilter.contains(temp_url
					.toString())) {
				resultGrab = new ResultGrab(
						BlogRemoteDefine.GRAB_RESULT.FINISHED, temp_url);
			} else if (UrlsManager.errorUrlsBloomFilter.contains(temp_url
					.toString())) {
				resultGrab = new ResultGrab(BlogRemoteDefine.GRAB_RESULT.ERROR,
						temp_url);
			} else {
				resultGrab = new ResultGrab(BlogRemoteDefine.GRAB_RESULT.NONE,
						temp_url);
			}
			resultGrabList.add(resultGrab);
		}
		return resultGrabList;
	}

	public List<Result> removeAccountInfoGrabTask(List<UrlPojo> accountUrls) {
		List<Result> resultList = new ArrayList<Result>();
		Result result = null;
		for (UrlPojo urlPojo : accountUrls) {
			result = new Result();
			if (CircleTaskUrlManager.removeToVisitCircleUrl(urlPojo)) {
				result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
				result.setInfo(urlPojo.toString() + " 移除成功");
			} else {
				/**
				 * 为了需求，将此处改为成功
				 */
				result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
				result.setInfo(urlPojo.toString()
						+ " 移除失败,原因：此url正在抓取当中或已被抓取完成或是未提交过!");
			}
			resultList.add(result);
		}
		return resultList;
	}

	public boolean addAccountInfoGrabTask(List<UrlPojo> accountUrls) {
		urlsManager.addUrlsListFirst(accountUrls);
		/**
		 * 在这里做对循环任务的处理,挪到UrlsManager中,使循环的Url任务准确性和有效性高
		 */
		// circleTaskUrlManager.addUrlPojoList(accountUrls);
		return true;
	}

	public boolean addCommentInfoGrabTask(List<UrlPojo> docUrls) {
		urlsManager.addCommentUrlsListFirst(docUrls);
		return true;
	}

	public boolean addTopicTitleInfoGrabTask(List<KeywordPojo> topicTitlePojos) {
		topicTitleManager.addTopicTitleList(topicTitlePojos);
		return true;
	}

	public boolean addAccountAttentionGrabTask(UrlPojo accountUrl) {
		accountUrl.setUrlSource(MyStringUtils.getUrlRemoveSlash(accountUrl
				.getUrl()));
		accountUrl.setUrl(MyStringUtils.getUrlRemoveSlash(accountUrl.getUrl()));
		accountUrl.setUrlSource(accountUrl.getUrl());

		accountUrl.setType(3);// 设置为抓取关注的类型
		// accountUrl.setUrlSource("3");// 是为查询状态而加
		// UrlsManager.addUrl(accountUrl);//以前是添加进toVisitUrl，现在只关注toVisitAttentionUrl
		if (TypePojo.isQQ(accountUrl.getUrl())) {
			urlsManager.addToVisitAttentionUrlFirst4QQ(accountUrl);
		} else {
			urlsManager.addToVisitAttentionUrlFirst(accountUrl);
		}
		return true;
	}

	public boolean addAccountListAttentionGrabTask(List<UrlPojo> accountUrls) {
		for (UrlPojo pojo : accountUrls) {
			pojo.setUrl(MyStringUtils.getUrlRemoveSlash(pojo.getUrl()));
			pojo.setUrlSource(pojo.getUrl());

			pojo.setType(3);// 将上步挪到此步来
		}
		/**
		 * 将手动提交的任务统统放在队列的最前边,即优先级最高
		 */
		urlsManager.addToVisitAttentionUrlListFirst(accountUrls);
		return true;
	}

	public List<ResultGrab> isFinishedAccountAttentionGrab(
			List<UrlPojo> accountUrls) {
		List<ResultGrab> resultGrabList = new ArrayList<ResultGrab>();
		ResultGrab resultGrab = null;
		int count = 1;
		for (UrlPojo temp_url : accountUrls) {
			count = 1;// 用于对状态查询出现问题后的纠正
			temp_url.setUrl(MyStringUtils.getUrlRemoveSlash(temp_url.getUrl()));
			// 在此处加了"attention-1"是为了区分开来
			temp_url.setUrlSource(temp_url.getUrl());
			while (count <= StaticValue.status_max_select_number) {
				/**
				 * 第一种情况，在排队
				 */
				if (UrlsManager.toVisitAttentionUrls.contains(temp_url)
						&& (!UrlsManager.visitingUrls.contains(temp_url) && !UrlsManager.visitedAttentionUrls
								.contains(temp_url))) {
					resultGrab = new ResultGrab(
							BlogRemoteDefine.GRAB_RESULT.WAITING, temp_url);
					break;
				}
				/**
				 * 第二种情况，正在采集
				 */
				else if (((UrlsManager.visitingUrls.contains(temp_url) || UrlsManager.visitedAttentionUrls
						.contains(temp_url)) && UrlsManager.toVisitAttentionUrls
						.contains(temp_url))
						|| (!UrlsManager.toVisitAttentionUrls
								.contains(temp_url) && (UrlsManager.visitingUrls
								.contains(temp_url) && !UrlsManager.visitedAttentionUrls
								.contains(temp_url)))) {
					resultGrab = new ResultGrab(
							BlogRemoteDefine.GRAB_RESULT.DOING, temp_url);
					break;
				}
				/**
				 * 第三种情况，采集出现错误
				 */
				else if (UrlsManager.errorUrlsBloomFilter.contains(temp_url
						.toString())) {
					resultGrab = new ResultGrab(
							BlogRemoteDefine.GRAB_RESULT.ERROR, temp_url);
					break;
				}
				/**
				 * 第四种情况，采集完成
				 */
				else if ((!UrlsManager.visitingUrls.contains(temp_url) && UrlsManager.visitedAttentionUrls
						.contains(temp_url))
						&& !UrlsManager.toVisitAttentionUrls.contains(temp_url)) {
					resultGrab = new ResultGrab(
							BlogRemoteDefine.GRAB_RESULT.FINISHED, temp_url);
					break;
				}

				/**
				 * 第五种情况，不在采集队列中
				 */
				else if (!UrlsManager.toVisitAttentionUrls.contains(temp_url)
						&& !UrlsManager.visitingUrls.contains(temp_url)
						&& !UrlsManager.visitedAttentionUrls.contains(temp_url)
						&& !UrlsManager.errorUrlsBloomFilter.contains(temp_url
								.toString())) {
					/**
					 * 此处为权宜之计,因为状态的确不好判断
					 */
					resultGrab = new ResultGrab(
							BlogRemoteDefine.GRAB_RESULT.NONE, temp_url);
					// 这个时候采取的策略是重复判断，因为这个状态有可能是真实的状态，也有可能是游离的状态，当判断到有合理状态时停止，如一直无合理状态则认为这时的不在队列是
					// 真正的状态
					count++;
				}

				/**
				 * 第六种情况，游离状态理解为正在采集，根据以前的判断过来的
				 */
				else {
					/**
					 * 此处为权宜之计,因为状态的确不好判断
					 */
					resultGrab = new ResultGrab(
							BlogRemoteDefine.GRAB_RESULT.DOING, temp_url);
					// 这个时候采取的策略是重复判断，因为这个状态有可能是真实的状态，也有可能是游离的状态，当判断到有合理状态时停止，如一直无合理状态则认为这时的不在队列是
					// 真正的状态
					count++;
				}
			}
			resultGrabList.add(resultGrab);
		}
		return resultGrabList;
	}

	public boolean addKeyWordsGrabTask(List<KeywordPojo> keywords) {
		keyWordManager.addKeyWordsList(keywords);
		keyWordManager.addKeyWordsList4QQ(keywords);// 加入腾讯关键词抓取集合
		/**
		 * 将其加入到新词当中
		 */
		// 判断是否是老关键词了
		for (KeywordPojo keyWord : keywords) {
			if (!LibraryManager.addNewWordsBloomFilter.contains(keyWord
					.getKeyWord())) {
				libraryManager.inserWord(keyWord.getKeyWord());
				LibraryManager.addNewWordsBloomFilter.add(keyWord.getKeyWord());
				logger.info(keyWord.getKeyWord() + "-----添加该新词成功");
			} else {
				logger.info(keyWord.getKeyWord() + "-----该词已在词典中，此次添加略过!");
			}
		}
		return true;
	}

	public List<ResultGrab> isFinishedKeyWordGrab(List<KeywordPojo> keywords) {
		List<ResultGrab> resultGrabList = new ArrayList<ResultGrab>();
		ResultGrab resultGrab = null;
		for (KeywordPojo keyword : keywords) {
			if (KeywordManager.toVisitKeyWords.contains(keyword)) {
				resultGrab = new ResultGrab(
						BlogRemoteDefine.GRAB_RESULT.WAITING, keyword);
			} else if (KeywordManager.visitingKeyWords.contains(keyword)) {
				resultGrab = new ResultGrab(BlogRemoteDefine.GRAB_RESULT.DOING,
						keyword);
			} else if (KeywordManager.visitedKeyWords.contains(keyword)) {
				resultGrab = new ResultGrab(
						BlogRemoteDefine.GRAB_RESULT.FINISHED, keyword);
			} else {
				resultGrab = new ResultGrab(BlogRemoteDefine.GRAB_RESULT.NONE,
						keyword);
			}
			resultGrabList.add(resultGrab);
		}
		return resultGrabList;
	}

	public List<Result> removeKeyWordsGrabTask(List<KeywordPojo> keyWordPojos) {
		List<Result> resultList = new ArrayList<Result>();
		Result result = null;
		for (KeywordPojo keyword : keyWordPojos) {
			result = new Result();
			if (keyWordManager.removeOneCircleKeyWord(keyword)) {
				result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
				result.setInfo(keyword.toString() + " 移除成功");
			} else {
				/**
				 * 为了需求，将此处改为成功
				 */
				result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
				result.setInfo(keyword.toString()
						+ " 移除失败,原因：此keyword正在抓取当中或已被抓取完成或是未提交过!");
			}
			resultList.add(result);
		}
		return resultList;
	}

	public static void main(String[] args) {
		TaskControler taskControler = new TaskControler();
		List<KeywordPojo> pojos = new LinkedList<KeywordPojo>();
		KeywordPojo keywordPojo = new KeywordPojo("123", 0);
		pojos.add(keywordPojo);

		taskControler.addKeyWordsGrabTask(pojos);
		pojos.add(new KeywordPojo("kk", 0));
		pojos.add(new KeywordPojo("ee", 0));
		List<ResultGrab> list = taskControler.isFinishedKeyWordGrab(pojos);
		for (ResultGrab result : list) {
			System.out.println(result.getInfo());
			System.out.println(result.getResult());
			System.out.println(result.getResultCode());

			System.out.println(result.getKeywordPojo());
			System.out.println(result.getUrlPojo());
			System.out.println("****************");
		}

		System.out.println("执行到了!");
	}

}
