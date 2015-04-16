package com.dw.party.mbmsupport.service.mbm;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import com.dw.party.mbmsupport.dto.FeatureWord;
import com.dw.party.mbmsupport.dto.SearchAccountResultPojo;
import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.dw.party.mbmsupport.dto.SupportSort;
import com.dw.party.mbmsupport.global.Emotion;
import com.mbm.elint.entity.mongoDB.TopicDoc;
import com.weibo.common.utils.ResultPage;

/**
 * 搜索功能服务类
 * 
 * @author zel
 * 
 */
@WebService
public interface ISearchService {
	/**
	 * 给任意字符串数据，返回给客户端分词结果
	 */
	public ResultPage<FeatureWord> findPersonFeatureWordsByContent(
			String content);

	/**
	 * 根据某个uid，获得个人的特征性集合
	 * operatorType就指获取个人特征集的方式，0代表获取实时的，1代表非实时的，即：缓存中有则取之，无则现计算取之
	 */
	public ResultPage<FeatureWord> findPersonFeatureWordsBySendUrl(
			String sendUrl, int operatorType);

	/**
	 * 获取按发布时间倒序1000篇的top n个热词
	 * 
	 * @param operatorType
	 * @return
	 */
	public ResultPage<FeatureWord> findTopHotWords(int operatorType);

	/**
	 * 获取按发布时间倒序1000篇的top n个热词
	 * 
	 * @param operatorType
	 * @return
	 */
	public ResultPage<TopicDoc> findTopHotTopic(int operatorType);

	/**
	 * 检索互相关注的关注列表
	 * 
	 * 为方便客户端编写代码，暂为定义，未实现
	 */
	public ResultPage<SearchAccountResultPojo> findAccountMutualAttentionList(
			String accountUrl, ResultPage<SearchAccountResultPojo> sendPage);

	/**
	 * 检索关注的微博用户列表 该方法通过一个微博用户的地址检索该微博用户所关注的所有其他微博用户情况。
	 */
	public ResultPage<SearchAccountResultPojo> findAccountAttentionList(
			String accountUrl, ResultPage<SearchAccountResultPojo> sendPage);

	/**
	 * 检索微博用户列表 该方法通过传递1个或者多个微博用户的地址信息来检索这些微博用户的具体情况。
	 */
	public ResultPage<SearchAccountResultPojo> findAccountInfoList(
			List<java.lang.String> accountUrls,
			java.util.List<SupportSort> sort,
			ResultPage<SearchAccountResultPojo> page);

	/**
	 * 根据用户提供的uid集合，查询出所有的uid对象的帐户信息列表
	 * 
	 * @param uidList
	 * @param page
	 * @return
	 */
	public ResultPage<SearchAccountResultPojo> findAccountInfoListByUid(
			List<String> uidList, ResultPage<SearchAccountResultPojo> page);

	/**
	 * 检索微博内容 注意：该方法所有参数均可为空，如果某参数为空时则代表忽略该参数。
	 * 
	 * @param accountUrls
	 * @param keys
	 * @param group
	 * @param content
	 * @param startPublishTime
	 * @param endPublishTime
	 * @param startGrabTime
	 * @param endGrabTime
	 * @param sort
	 * @param startIndex
	 * @param maxSize
	 * @return
	 */
	@Deprecated
	public ResultPage<SearchContentResultPojo> findContentInfoList(
			List<String> accountUrls, List<String> keys, String group,
			Date startPublishTime, Date endPublishTime, Date startGrabTime,
			Date endGrabTime, List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage);

	/**
	 * 为华为专门添加,获取数据库中的所有博文，止前限于前100万条
	 * 
	 * @param accountUrls
	 * @param keys
	 * @param group
	 * @param startPublishTime
	 * @param endPublishTime
	 * @param startGrabTime
	 * @param endGrabTime
	 * @param sort
	 * @param sendPage
	 * @return
	 */
	public ResultPage<SearchContentResultPojo> findContentInfoList4HuaWei(
			ResultPage<SearchContentResultPojo> sendPage);

	/**
	 * 搜索最新的入库的消息，与findContentInfoList不的是，减少了group参数，其它无变化
	 * 
	 * @param accountUrls
	 * @param keys
	 * @param startPublishTime
	 * @param endPublishTime
	 * @param startGrabTime
	 * @param endGrabTime
	 * @param sort
	 * @param sendPage
	 * @return
	 */
	public ResultPage<SearchContentResultPojo> findNewContentInfoList(
			List<String> accountUrls, List<String> keys, Date startPublishTime,
			Date endPublishTime, Date startGrabTime, Date endGrabTime,
			List<SupportSort> sort, ResultPage<SearchContentResultPojo> sendPage);

	/**
	 * 客户端提交一个uid，服务器端返回一个uid列表集合的string形式
	 */
	public String findAttentionListByUid(String uid);

	/**
	 * 客户端提交一个uid，服务器端返回一个uid列表集合的string形式
	 */
	public String findFansListByUid(String uid);

	/**
	 * 客户端提交一个uid，服务器端返回一个uid列表集合的string形式
	 */
	public String findMutualFansListByUid(String uid);

	/**
	 * 正负面情感搜索
	 * 
	 * @param accountUrls
	 * @param keys
	 * @param startPublishTime
	 * @param endPublishTime
	 * @param startGrabTime
	 * @param endGrabTime
	 * @param sort
	 * @param sendPage
	 * @param emotion
	 * @return
	 */
	public ResultPage<SearchContentResultPojo> findContentInfoListWithEmotion(
			List<String> accountUrls, List<String> keys, Date startPublishTime,
			Date endPublishTime, Date startGrabTime, Date endGrabTime,
			List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage, Emotion emotion);

	public ResultPage<SearchContentResultPojo> findDocInfoListByDocUrls(
			List<String> docUrls, ResultPage<SearchContentResultPojo> page);

	/**
	 * webservice调用空测试方法
	 */
	void test();

	/**
	 * 为华为查询数据添加
	 */
	public ResultPage<SearchAccountResultPojo> findAllAccountInfoListForHuaWei(
			SupportSort sort, ResultPage<SearchAccountResultPojo> page);
}