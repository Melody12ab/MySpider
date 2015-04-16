package com.dw.party.mbmsupport.service.lingjoin;

import java.util.List;

import javax.jws.WebService;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.dto.ResultGrab;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.UrlPojo;
@WebService
public interface ITaskService4LingJoin {
	/**
	 * 增加关注用户采集任务(一次性任务) 根据微博用户的地址采集该微博用户所关注的所有微博用户
	 * 如果该微博用户地址无法完成关注的采集，请记录这些地址并将其状态置为该地址无法采集。
	 * 
	 * @param accountUrl
	 * @return
	 */
	public Result addAccountAttentionGrabTask(UrlPojo accountUrl);
	public Result addAccountListAttentionGrabTask(List<UrlPojo> accountUrlList);
	/**
	 * 增加微博用户采集任务(一次性任务) 根据微博用户的地址列表采集这些微博用户，如果这些微博用户已存在，请忽略。
	 * 
	 * @param accountUrls
	 * @return
	 */
	public Result addAccountGrabTask(List<UrlPojo> accountUrls);

	/**
	 * 增加采集微博用户的微博内容的任务(周期任务)
	 * 
	 * @param accountUrls
	 * @return
	 */
	public Result addAccountInfoGrabTask(List<UrlPojo> accountUrls);
	/**
	 * 增加关键词组的采集任务(周期任务) 如果关键词组列表中的某一个关键词组已经建立了采集任务，请忽略。
	 * 
	 * @param keyWords
	 * @return
	 */
	public Result addKeyWordsGrabTask(List<KeywordPojo> keyWords);

	/**
	 *检索关注用户是否采集完成
	 */
	public List<ResultGrab> isFinishedAccountAttentionGrab(
			List<UrlPojo> accountUrls);

	/**
	 * 检索微博用户是否采集完成
	 * 
	 * @param accountUrls
	 * @return
	 */
	public List<ResultGrab> isFinishedAccountGrab(
			List<UrlPojo> accountUrls);

	/**
	 * 删除采集微博用户的微博内容的任务
	 * 
	 * @param accountUrls
	 * @return
	 */
	public List<Result> removeAccountInfoGrabTask(
			List<UrlPojo> accountUrls);

	/**
	 * 删除关键词组的采集任务 如果关键词组列表中的某一个关键词组已经建立了采集任务，请删除。
	 * 
	 * @param keyWords
	 * @return
	 */
	public List<Result> removeKeyWordsGrabTask(
			List<KeywordPojo> keyWordPojos);
	
	// 检索搜索的关键词的关态
	public List<ResultGrab> isFinishedKeyWordGrab(List<KeywordPojo> keywords) ;
	/**
	 * 添加新词，便于取得个人的特征
	 * @param accountUrls
	 * @return
	 */
	public Result addNewWordTask(String word);
	public Result addNewWordListTask(List<String> wordList);
//	void test();
}