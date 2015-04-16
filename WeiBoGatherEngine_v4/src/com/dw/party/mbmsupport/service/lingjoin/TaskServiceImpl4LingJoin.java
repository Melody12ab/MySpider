package com.dw.party.mbmsupport.service.lingjoin;

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

@WebService(endpointInterface = "com.dw.party.mbmsupport.service.lingjoin.ITaskService4LingJoin", serviceName = "TaskService4LingJoin")
@Component(value = "taskService4LingJoin")
public class TaskServiceImpl4LingJoin implements ITaskService4LingJoin {
	public static Logger logger = Logger.getLogger(TaskServiceImpl4LingJoin.class);
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

	public Result addAccountAttentionGrabTask(UrlPojo accountUrl) {
		// TODO Auto-generated method stub
		this.result = taskControlerManager
				.addAccountAttentionGrabTask(accountUrl);
		return result;
	}

	public Result addAccountListAttentionGrabTask(List<UrlPojo> accountUrlList) {
		this.result = taskControlerManager
				.addAccountListAttentionGrabTask(accountUrlList);
		return result;
	}

	// 增加微博用户采集任务(一次性任务) 根据微博用户的地址列表采集这些微博用户，如果这些微博用户已存在，请忽略。
	public Result addAccountGrabTask(List<UrlPojo> accountUrls) {
		// TODO Auto-generated method stub
		result = taskControlerManager.addAccountGrabTask(accountUrls);
		return result;
	}

	// 增加微博用户采集内容的任务(周期任务)
	public Result addAccountInfoGrabTask(List<UrlPojo> accountUrls) {
		// TODO Auto-generated method stub
		result = taskControlerManager.addAccountInfoGrabTask(accountUrls);
		return result;
	}

	public Result addKeyWordsGrabTask(List<KeywordPojo> keyWords) {
		// TODO Auto-generated method stub
		this.result = taskControlerManager.addKeyWordsGrabTask(keyWords);
		return result;
	}

	public List<ResultGrab> isFinishedAccountAttentionGrab(
			List<UrlPojo> accountUrls) {
		// TODO Auto-generated method stub
		List<ResultGrab> resultGrab = taskControlerManager
				.isFinishedAccountAttentionGrab(accountUrls);
		return resultGrab;
	}

	// 检索微博用户是否采集完成
	public List<ResultGrab> isFinishedAccountGrab(List<UrlPojo> accountUrls) {
		// TODO Auto-generated method stub
		List<ResultGrab> resultGrabList = taskControlerManager
				.isFinishedAccountGrab(accountUrls,0);
		return resultGrabList;
	}

	// 删除采集微博用户的微博内容的任务
	public List<Result> removeAccountInfoGrabTask(List<UrlPojo> accountUrls) {
		// TODO Auto-generated method stub
		List<Result> resultList = taskControlerManager
				.removeAccountInfoGrabTask(accountUrls);
		return resultList;
	}

	public List<Result> removeKeyWordsGrabTask(List<KeywordPojo> keyWordPojos) {
		// TODO Auto-generated method stub
		List<Result> resultList = taskControlerManager
				.removeKeyWordsGrabTask(keyWordPojos);
		return resultList;
	}

	// 检索搜索的关键词的关态
	public List<ResultGrab> isFinishedKeyWordGrab(List<KeywordPojo> keywords) {
		// TODO Auto-generated method stub
		List<ResultGrab> resultGrabList = taskControlerManager
				.isFinishedKeyWordGrab(keywords);
		return resultGrabList;
	}

	/**
	 * 添加新词
	 */
	public Result addNewWordTask(String word) {
		result = taskControlerManager.addNewWordTask(word);
		return result;
	}

	public Result addNewWordListTask(List<String> wordList) {
		result = taskControlerManager.addNewWordListTask(wordList);
		return result;
	}

}