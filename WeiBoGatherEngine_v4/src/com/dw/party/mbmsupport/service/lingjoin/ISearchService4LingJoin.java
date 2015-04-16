package com.dw.party.mbmsupport.service.lingjoin;

import java.util.List;

import javax.jws.WebService;

import com.dw.party.mbmsupport.dto.SearchAccountResultPojo;
import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.dw.party.mbmsupport.dto.SupportSort;
import com.weibo.common.utils.ResultPage;

@WebService
public interface ISearchService4LingJoin {
	/**
	 * 检索微博用户列表 该方法通过传递1个或者多个微博用户的地址信息来检索这些微博用户的具体情况。
	 */
	public ResultPage<SearchAccountResultPojo> findAccountInfoList(
			List<java.lang.String> keywords_name,
			ResultPage<SearchAccountResultPojo> page);
	
	/**
	 * 检索出所有的人物信息，按抓取时间逆序排列,传了一个sort参数
	 */
	public ResultPage<SearchAccountResultPojo> findAllAccountInfoList(SupportSort sort,ResultPage<SearchAccountResultPojo> page);
	
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
	 * 根据用户提供的uid集合，查询出所有的uid对象的微博内容信息
	 * 
	 * @param uidList
	 * @param page
	 * @return
	 */
	public ResultPage<SearchAccountResultPojo> findContentInfoListByUids(
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
	public ResultPage<SearchContentResultPojo> findContentInfoList(
			List<String> accountUrls, List<String> uids,List<String> keys,
			java.lang.String content,
			java.util.Date startPublishTime, java.util.Date endPublishTime,
			java.util.Date startGrabTime, java.util.Date endGrabTime,
			java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage);
	/**
	 * 找出所有的最新信息,并按sort指定的列的序排列显示
	 * @param sort
	 * @return
	 */
	public ResultPage<SearchContentResultPojo> findAllContentInfoList(SupportSort sort,ResultPage<SearchContentResultPojo> page);
	
	void test();
}