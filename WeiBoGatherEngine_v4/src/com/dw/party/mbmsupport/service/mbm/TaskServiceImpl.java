package com.dw.party.mbmsupport.service.mbm;

import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.dto.ResultGrab;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.TaskControlerManager;

/**
 * 用于向服务器端发送抓取任务以及任务的状态查询的接口类
 * 
 * @author zel
 * 
 */
@WebService(endpointInterface = "com.dw.party.mbmsupport.service.mbm.ITaskService", serviceName = "TaskService")
@Component(value = "taskService")
public class TaskServiceImpl implements ITaskService {
	public static Logger logger = Logger.getLogger(TaskServiceImpl.class);
	@Autowired
	private Result result;
	private TaskControlerManager taskControlerManager;

	public TaskControlerManager getTaskControlerManager() {
		return taskControlerManager;
	}

	@Autowired
	public void setTaskControlerManager(
			TaskControlerManager taskControlerManager) {
		this.taskControlerManager = taskControlerManager;
	}

	/**
	 * 添加博主的关注列表的抓取任务,其数据封装在UrlPojo中 此接口的抓取任务是逐个添加
	 */
	public Result addAccountAttentionGrabTask(UrlPojo accountUrl) {
		// TODO Auto-generated method stub
		this.result = taskControlerManager
				.addAccountAttentionGrabTask(accountUrl);
		return result;
	}

	/**
	 * 添加博主的关注列表的抓取任务,其数据封装在UrlPojo中 此接口的抓取任务可以批量添加
	 */
	public Result addAccountListAttentionGrabTask(List<UrlPojo> accountUrlList) {
		this.result = taskControlerManager
				.addAccountListAttentionGrabTask(accountUrlList);
		return result;
	}

	/**
	 * 增加微博用户采集任务(一次性任务) 根据微博用户的地址列表采集这些微博用户，如果这些微博用户已存在，则忽略。
	 * 一次性任务，是指不加入周期循环的任务列表,即只抓一次不再重复抓取并更新
	 */
	public Result addAccountGrabTask(List<UrlPojo> accountUrls) {
		// TODO Auto-generated method stub
		result = taskControlerManager.addAccountGrabTask(accountUrls);
		return result;
	}

	/**
	 * 增加微博用户采集内容的任务(周期任务) 根据微博用户的地址列表采集这些微博用户，如果这些微博用户已存在，则忽略。
	 * 周期任务，是指该任务列表将加入周期循环的任务列表,即将重复抓取并更新
	 */
	public Result addAccountInfoGrabTask(List<UrlPojo> accountUrls) {
		// TODO Auto-generated method stub
		result = taskControlerManager.addAccountInfoGrabTask(accountUrls);
		return result;
	}

	/**
	 * 该任务本身是周期任务 提交要抓取的微博搜索中词，封装至KeyWordPojo对象中 在提交关键词任务时，每个关键词都将做为一个新词增量加入词典中
	 */
	public Result addKeyWordsGrabTask(List<KeywordPojo> keyWords) {
		// TODO Auto-generated method stub
		this.result = taskControlerManager.addKeyWordsGrabTask(keyWords);
		return result;
	}

	/**
	 * 关注任务的状态查询接口 发送的每UrlPojo都会得到服务器返回的状态结果对象ResultGrab，
	 * 其result和UrlPojo是客户要的数据，其它不必关心 result是常量，
	 * 值来自于BlogRemoteDefine.GRAB_RESULT类的定义
	 */
	public List<ResultGrab> isFinishedAccountAttentionGrab(
			List<UrlPojo> accountUrls) {
		// TODO Auto-generated method stub
		List<ResultGrab> resultGrab = taskControlerManager
				.isFinishedAccountAttentionGrab(accountUrls);
		return resultGrab;
	}

	/**
	 * 帐号信息采集任务的状态查询接口 发送的每UrlPojo都会得到服务器返回的状态结果对象ResultGrab，
	 * 其result和UrlPojo是客户要的数据，其它不必关心 result是常量，
	 * 值来自于BlogRemoteDefine.GRAB_RESULT类的定义
	 */
	public List<ResultGrab> isFinishedAccountGrab(List<UrlPojo> accountUrls) {
		// TODO Auto-generated method stub
		List<ResultGrab> resultGrabList = taskControlerManager
				.isFinishedAccountGrab(accountUrls, 0);
		return resultGrabList;
	}

	/**
	 * 微博内容采集任务的状态查询接口 发送的每UrlPojo都会得到服务器返回的状态结果对象ResultGrab，
	 * 其result和UrlPojo是客户要的数据，其它不必关心 result是常量，
	 * 值来自于BlogRemoteDefine.GRAB_RESULT类的定义
	 */
	public List<ResultGrab> isFinishedAccountInfoGrab(List<UrlPojo> accountUrls) {
		List<ResultGrab> resultGrabList = taskControlerManager
				.isFinishedAccountGrab(accountUrls, 1);
		return resultGrabList;
	}

	/**
	 * 删除采集微博内容的任务
	 */
	public List<Result> removeAccountInfoGrabTask(List<UrlPojo> accountUrls) {
		List<Result> resultList = taskControlerManager
				.removeAccountInfoGrabTask(accountUrls);
		return resultList;
	}

	/**
	 * 删除关键词采集任务
	 */
	public List<Result> removeKeyWordsGrabTask(List<KeywordPojo> keyWordPojos) {
		// TODO Auto-generated method stub
		List<Result> resultList = taskControlerManager
				.removeKeyWordsGrabTask(keyWordPojos);
		return resultList;
	}

	/**
	 * 关键词采集任务的状态查询接口 发送的每UrlPojo都会得到服务器返回的状态结果对象ResultGrab，
	 * 其result和KeywordPojo是客户要的数据，其它不必关心 result是常量，
	 * 值来自于BlogRemoteDefine.GRAB_RESULT类的定义
	 */
	public List<ResultGrab> isFinishedKeyWordGrab(List<KeywordPojo> keywords) {
		// TODO Auto-generated method stub
		List<ResultGrab> resultGrabList = taskControlerManager
				.isFinishedKeyWordGrab(keywords);
		return resultGrabList;
	}

	/**
	 * 添加新词至用户词典中，帮助分词更精确,只可单个添加,用于特征词提取时的分词准确计算 在提交关键词任务时，每个关键词都将做为一个新词增量加入词典中
	 */
	public Result addNewWordTask(String word) {
		result = taskControlerManager.addNewWordTask(word);
		return result;
	}

	/**
	 * 添加新词至用户词典中，帮助分词更精确,可批量添加,用于特征词提取时的分词准确计算 在提交关键词任务时，每个关键词都将做为一个新词增量加入词典中
	 */
	public Result addNewWordListTask(List<String> wordList) {
		result = taskControlerManager.addNewWordListTask(wordList);
		return result;
	}
	/**
	 * 微言的URL
	 * 周期任务，是指该任务列表将加入周期循环的任务列表,即将重复抓取并更新
	 */
	public Result addCommentInfoGrabTask(List<UrlPojo> docUrls) {
		result = taskControlerManager.addCommentInfoGrabTask(docUrls);
		return result;
	}

	public Result addTopicTitleInfoGrabTask(List<KeywordPojo> topicTitlePojos) {
		result = taskControlerManager.addTopicTitleInfoGrabTask(topicTitlePojos);
		return result;
	}

}