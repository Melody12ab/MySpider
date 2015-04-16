package com.mbm.elint.manager.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.dto.ResultGrab;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.util.VerifyValidateUtils;
import com.weibo.common.controler.TaskControler;
import com.weibo.common.utils.StaticValue;
import com.weibo.utils.sina.MyStringUtils;

@Component
public class TaskControlerManager {
	public static Logger logger = Logger.getLogger(TaskControlerManager.class);
	@Autowired
	private TaskControler taskControler;
	@Autowired
	private Result result;
	@Autowired
	private ResultGrab resultGrab;
	@Autowired
	private LibraryManager libraryManager;
	private UrlPojo pojo = null;

	public Result addAccountGrabTask(List<UrlPojo> accountUrls) {
		if (!StaticValue.isRunning) {
			return VerifyValidateUtils.getNotRunningResult(result);
		}
		if (VerifyValidateUtils.verifyNullCollections(accountUrls)) {
			return VerifyValidateUtils.getNullCollectionResult(result);
		}
		for (int i = 0; i < accountUrls.size(); i++) {
			try {
				pojo = accountUrls.get(i);
				pojo.setType(1);
				pojo.setUrlSource("1");// 为查询状态新加
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("添加抓取帐户信息时的集合中有空的UrlPojo");
			}
		}
		if (taskControler.addAccountGrabTask(accountUrls)) {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("添加抓取列表成功");
		} else {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			result.setInfo("添加抓取列表成功");
		}
		return result;
	}

	public List<ResultGrab> isFinishedAccountGrab(List<UrlPojo> accountUrls,
			int flag) {
		List<ResultGrab> resultGrabList = null;
		if (!StaticValue.isRunning) {
			resultGrabList = new ArrayList<ResultGrab>(1);
			return VerifyValidateUtils.getNotRunningResultGrabList(
					resultGrabList, resultGrab);
		}
		if (VerifyValidateUtils.verifyNullCollections(accountUrls)) {
			resultGrabList = new ArrayList<ResultGrab>(1);
			ResultGrab resultGrab = new ResultGrab(
					BlogRemoteDefine.GRAB_RESULT.NONE, new KeywordPojo(
							"提交的查询集合为空!", 0));
			resultGrabList.add(resultGrab);
			return resultGrabList;
		}
		if (flag == 0) {// 代表是帐户信息查询
			for (int i = 0; i < accountUrls.size(); i++) {
				pojo = accountUrls.get(i);
				pojo.setType(1);
				pojo.setUrlSource("1");
			}
		} else if (flag == 1) {
			for (int i = 0; i < accountUrls.size(); i++) {
				// accountUrls.get(i).setType(2);
				pojo = accountUrls.get(i);
				pojo.setType(2);
				pojo.setUrlSource("2");
			}
		}
		resultGrabList = taskControler.isFinishedAccountGrab(accountUrls);
		return resultGrabList;
	}

	public Result addAccountInfoGrabTask(List<UrlPojo> accountUrls) {
		if (!StaticValue.isRunning) {
			return VerifyValidateUtils.getNotRunningResult(result);
		}
		if (VerifyValidateUtils.verifyNullCollections(accountUrls)) {
			return VerifyValidateUtils.getNullCollectionResult(result);
		}
		for (int i = 0; i < accountUrls.size(); i++) {
			try {
				pojo = accountUrls.get(i);
				pojo.setType(2);
				pojo.setUrlSource("2");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("添加抓取微博内容时的集合中有空的UrlPojo");
			}
		}
		if (taskControler.addAccountInfoGrabTask(accountUrls)) {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("添加采集微博内容任务成功!");
		} else {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			result.setInfo("添加采集微博内容任务失败!");
		}
		return result;
	}

	public Result addCommentInfoGrabTask(List<UrlPojo> docUrls) {
		if (!StaticValue.isRunning) {
			return VerifyValidateUtils.getNotRunningResult(result);
		}
		if (VerifyValidateUtils.verifyNullCollections(docUrls)) {
			return VerifyValidateUtils.getNullCollectionResult(result);
		}
		for (int i = 0; i < docUrls.size(); i++) {
			try {
				pojo = docUrls.get(i);
				if(pojo.getUrl().contains("e.weibo")){
					pojo.setType(102);
				}else {
					pojo.setType(101);					
				}
				pojo.setUrlSource("none");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("添加抓取评论列表时的集合中有空的UrlPojo");
			}
		}
		if (taskControler.addCommentInfoGrabTask(docUrls)) {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("添加采集评论列表任务成功!");
		} else {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			result.setInfo("添加采集评论列表任务失败!");
		}
		return result;
	}
	
	public Result addTopicTitleInfoGrabTask(List<KeywordPojo> topicTitlePojos) {
		if (!StaticValue.isRunning) {
			return VerifyValidateUtils.getNotRunningResult(result);
		}
		if (VerifyValidateUtils.verifyNullKeySet(topicTitlePojos)) {
			return VerifyValidateUtils.getNullCollectionResult(result);
		}
		if (taskControler.addTopicTitleInfoGrabTask(topicTitlePojos)) {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("添加话题内容采集列表任务成功!");
		} else {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			result.setInfo("添加话题内容采集列表任务失败!");
		}
		return result;
	}
	
	public List<Result> removeAccountInfoGrabTask(List<UrlPojo> accountUrls) {
		List<Result> resultList = null;
		if (!StaticValue.isRunning) {
			resultList = new ArrayList<Result>(1);
			return VerifyValidateUtils.getNotRunningResultList(resultList,
					result);
		}
		if (VerifyValidateUtils.verifyNullCollections(accountUrls)) {
			resultList = new ArrayList<Result>(1);
			return VerifyValidateUtils.getNullConnectionResultList(resultList,
					result);
		}
		for (int i = 0; i < accountUrls.size(); i++) {
			try {
				accountUrls.get(i).setType(2);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("添加抓取微博内容时的集合中有空的UrlPojo");
			}
		}
		resultList = taskControler.removeAccountInfoGrabTask(accountUrls);

		return resultList;
	}

	public Result addAccountAttentionGrabTask(UrlPojo accountUrl) {
		if (!StaticValue.isRunning) {
			return VerifyValidateUtils.getNotRunningResult(result);
		}
		if (accountUrl == null || accountUrl.getUrl() == null) {
			return VerifyValidateUtils.getNullCollectionResult(result);
		}
		if (taskControler.addAccountAttentionGrabTask(accountUrl)) {
			this.result
					.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			this.result.setInfo("添加采集关注列表的任务成功!");
		} else {
			this.result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			this.result.setInfo("添加采集关注列表的任务失败!");
		}
		return this.result;
	}

	public Result addAccountListAttentionGrabTask(List<UrlPojo> urlPojos) {
		if (!StaticValue.isRunning) {
			return VerifyValidateUtils.getNotRunningResult(result);
		}
		if (VerifyValidateUtils.verifyNullCollections(urlPojos)) {
			return VerifyValidateUtils.getNullCollectionResult(result);
		}
		if (taskControler.addAccountListAttentionGrabTask(urlPojos)) {
			this.result
					.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			this.result.setInfo("添加微博用户集合对应的关注列表的任务成功!");
		} else {
			this.result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			this.result.setInfo("添加微博用户集合对应的关注列表的任务失败!");
		}
		return result;
	}

	public List<ResultGrab> isFinishedAccountAttentionGrab(
			List<UrlPojo> accountUrls) {
		List<ResultGrab> resultGrabList = null;
		if (!StaticValue.isRunning) {
			resultGrabList = new ArrayList<ResultGrab>(1);
			return VerifyValidateUtils.getNotRunningResultGrabList(
					resultGrabList, resultGrab);
		}
		if (VerifyValidateUtils.verifyNullCollections(accountUrls)) {
			resultGrabList = new ArrayList<ResultGrab>(1);
			return VerifyValidateUtils.getNotRunningResultGrabList(
					resultGrabList, resultGrab);
		}
		for (int i = 0; i < accountUrls.size(); i++) {
			try {
				accountUrls.get(i).setUrlSource(
						MyStringUtils.getUrlRemoveSlash(accountUrls.get(i)
								.getUrl()));
				// accountUrls.get(i).setType(3);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("添加抓取微博内容时的集合中有空的UrlPojo");
			}
		}
		resultGrabList = taskControler
				.isFinishedAccountAttentionGrab(accountUrls);
		return resultGrabList;
	}

	public Result addKeyWordsGrabTask(List<KeywordPojo> keyWords) {
		if (!StaticValue.isRunning) {
			return VerifyValidateUtils.getNotRunningResult(result);
		}
		if (VerifyValidateUtils.verifyNullKeySet(keyWords)) {
			return VerifyValidateUtils.getNullCollectionResult(result);
		}
		if (taskControler.addKeyWordsGrabTask(keyWords)) {
			this.result
					.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			this.result.setInfo("添加关键词组抓取任务成功");
		} else {
			this.result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			this.result.setInfo("添加关键词组抓取任务失败");
		}
		return result;
	}

	public List<ResultGrab> isFinishedKeyWordGrab(List<KeywordPojo> keywords) {
		List<ResultGrab> resultGrabList = null;
		if (!StaticValue.isRunning) {
			resultGrabList = new ArrayList<ResultGrab>(1);
			return VerifyValidateUtils.getNotRunningResultGrabList(
					resultGrabList, resultGrab);
		}
		if (VerifyValidateUtils.verifyNullKeySet(keywords)) {
			resultGrabList = new ArrayList<ResultGrab>();
			return VerifyValidateUtils.getNotRunningResultGrabList(
					resultGrabList, resultGrab);
		}
		resultGrabList = taskControler.isFinishedKeyWordGrab(keywords);
		return resultGrabList;
	}

	public List<Result> removeKeyWordsGrabTask(List<KeywordPojo> keyWordPojos) {
		List<Result> resultList = null;
		if (!StaticValue.isRunning) {
			resultList = new ArrayList<Result>(1);
			return VerifyValidateUtils.getNotRunningResultList(resultList,
					result);
		}

		if (VerifyValidateUtils.verifyNullKeySet(keyWordPojos)) {
			resultList = new ArrayList<Result>(1);
			return VerifyValidateUtils.getNullConnectionResultList(resultList,
					result);
		}
		resultList = taskControler.removeKeyWordsGrabTask(keyWordPojos);
		return resultList;
	}

	/**
	 * 添加新词
	 * 
	 * @param word
	 * @return
	 */
	public Result addNewWordTask(String word) {
		if (libraryManager.inserWord(word)) {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("添加新词成功!");
		} else {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("该新词已添加过,此次添加将略过!");
		}
		return result;
	}

	public Result addNewWordListTask(List<String> wordList) {
		libraryManager.insertWordList(wordList);
		result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		result.setInfo("添加新词成功!");
		return result;
	}
	/**
	 * 专为SystemOperation接口而添加，为管理员用
	 * @param accountUrls
	 * @return
	 */
	public Result addAccountGrabTask4System(List<UrlPojo> accountUrls) {
		if (!StaticValue.isRunning) {
			return VerifyValidateUtils.getNotRunningResult(result);
		}
		if (VerifyValidateUtils.verifyNullCollections(accountUrls)) {
			return VerifyValidateUtils.getNullCollectionResult(result);
		}
		for (int i = 0; i < accountUrls.size(); i++) {
			try {
				pojo = accountUrls.get(i);
				pojo.setType(1);//此时提交的帐户一概插入数据库，主要是为因乱码致的缓存与数据库不一致而添加
				pojo.setUrlSource("system");// 为查询状态新加
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("添加抓取帐户信息时的集合中有空的UrlPojo");
			}
		}
		if (taskControler.addAccountGrabTask(accountUrls)) {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("添加抓取列表成功");
		} else {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			result.setInfo("添加抓取列表成功");
		}
		return result;
	}
	

}
