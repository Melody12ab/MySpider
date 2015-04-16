package com.dw.party.mbmsupport.service.mbm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.FeatureWord;
import com.dw.party.mbmsupport.dto.SearchAccountResultPojo;
import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.dw.party.mbmsupport.dto.SupportSort;
import com.dw.party.mbmsupport.global.Emotion;
import com.mbm.elint.entity.mongoDB.TopicDoc;
import com.mbm.elint.manager.business.SearchControlerManager;
import com.weibo.common.utils.ResultPage;

/**
 * 搜索服务接口类,凡是搜索数据的方法均在此类中
 * 
 * @author zel
 * 
 */
// Generated by MyEclipse
@WebService(endpointInterface = "com.dw.party.mbmsupport.service.mbm.ISearchService", serviceName = "SearchService")
@Component(value = "searchService")
public class SearchServiceImpl implements ISearchService {
	/**
	 * 根据给出的content取得该content的特征词列表,即该content是用客户端直接提供的
	 */
	public ResultPage<FeatureWord> findPersonFeatureWordsByContent(
			String content) {
		// ResultPage<FeatureWord> page = searchControlerManager
		// .findPersonFeatureWordsByContent(content);
		// return page;
		return null;
	}

	/**
	 * 根据给出的content取得该content的特征词列表,即该content是用客户端提供的url在服务器端搜索到相应的微博内容后计算而得，
	 * 该内容列表的有效内容条数由服务端规定，一般为top 100篇博文
	 */
	public ResultPage<FeatureWord> findPersonFeatureWordsBySendUrl(
			String sendUrl, int operatorType) {
		// ResultPage<FeatureWord> page = searchControlerManager
		// .findPersonFeatureWordsBySendUrl(sendUrl, operatorType);
		// return page;
		return null;
	}

	/**
	 * 根据博主的uid列表，得到每个uid对应的帐户信息
	 * page对象设置了返回的数据中每页的博主条目数，其它的参数如pageNo,totalCount由搜索出的结果集自动计算得出
	 */
	public ResultPage<SearchAccountResultPojo> findAccountInfoListByUid(
			List<String> uidList, ResultPage<SearchAccountResultPojo> page) {
		// TODO Auto-generated method stub
		List<String> uidToUrl = new ArrayList<String>();
		for (String uid : uidList) {
			/**
			 * 此处为解决 uid回传搜索
			 */
			uidToUrl.add(uid);
		}
		page = searchControlerManager.findAccountInfoListByUids(uidToUrl, page);
		return page;
	}

	@Autowired
	private SearchControlerManager searchControlerManager;

	/**
	 * 根据发送过来的博主的accountUrl，获取对应的帐户信息，这个是单个博主信息获取
	 * sendPage对象可设置返回的数据中每页的条目数，默认为15条
	 * ,其它参数如pageNo,totalCount由自动计算而得,由于是一个accountUrl,则sendPage的pageSize设为1即可。
	 */
	public ResultPage<SearchAccountResultPojo> findAccountAttentionList(
			String accountUrl, ResultPage<SearchAccountResultPojo> sendPage) {
		// TODO Auto-generated method stub
		ResultPage<SearchAccountResultPojo> resultPage = searchControlerManager
				.findAccountAttentionList(accountUrl, sendPage);
		return resultPage;
	}

	/**
	 * 根据发送过来的博主的accountUrls，获取对应的帐户信息
	 * sort集合是指定的排序规则，如按grabTime抓取时间，fansNum数丝数量,wbNums微博数量等升或降序
	 * sendPage对象可设置返回的数据中每页的条目数，默认为15条,其它参数如pageNo,totalCount由自动计算而得。
	 */
	public ResultPage<SearchAccountResultPojo> findAccountInfoList(
			List<String> accountUrls, java.util.List<SupportSort> sort,
			ResultPage<SearchAccountResultPojo> page) {
		// TODO Auto-generated method stub
		page = searchControlerManager.findAccountInfoList(accountUrls, page);
		return page;
	}

	/**
	 * 该方法已废弃，用findNewContentInfoList来代替
	 */
	@Deprecated
	public ResultPage<SearchContentResultPojo> findContentInfoList(
			List<String> accountUrls, List<String> keys,
			java.lang.String group, java.util.Date startPublishTime,
			java.util.Date endPublishTime, java.util.Date startGrabTime,
			java.util.Date endGrabTime, java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage) {
		sendPage = searchControlerManager.findContentInfoList(accountUrls,
				keys, group, startPublishTime, endPublishTime, startGrabTime,
				endGrabTime, sort, sendPage);
		// this.findNewContentInfoList(accountUrls, keys, startPublishTime,
		// endPublishTime, startGrabTime, endGrabTime, sort, sendPage);
		return sendPage;
	}

	/**
	 * web service空方法调用测试接口，判断接口可正常调用
	 */
	public void test() {
		System.out.println("SearchServiceImpl");
	}

	/**
	 * 搜索内容的统一方法，共包括8个参数，参数之间是与的关系 accountUrls指定的博主的url,可不指定 keys指定的关键词，可不指定
	 * startPublishTime指定的博文的发布时间
	 * ，可不指定。若不指定则会根据(startPublishTime=endPublishTime-30
	 * )分钟来计算,若endPublishTime也为空，则按(startPublishTime=当前时间-30分钟)
	 * endPublishTime指定的博文的发布时间
	 * ，可不指定。若不指定则会根据endPublishTime=startPublishTime+30分钟来计算
	 * ,若startPublishTime也为空，则按(endPublishTime=当前时间)
	 * startGrabTime指博文的抓取入库的开始时间，其设置类同于startPublishTime
	 * endGrabTime指博文的结束抓取时间，其设置类同于endPublishTime
	 * sort指搜索出的博文内容的排序规则，如可按commentNum评论数
	 * ，convertNum转发数,endPublishTime结束发布时间的升或降序来排序返回
	 * sendPage指返回结果集的存储集合，其设置类似于已提过的sendPage
	 */
	public ResultPage<SearchContentResultPojo> findNewContentInfoList(
			List<String> accountUrls, List<String> keys, Date startPublishTime,
			Date endPublishTime, Date startGrabTime, Date endGrabTime,
			List<SupportSort> sort, ResultPage<SearchContentResultPojo> sendPage) {

		sendPage = searchControlerManager.findNewestContentInfoPage(
				accountUrls, keys, startPublishTime, endPublishTime,
				startGrabTime, endGrabTime, sort, sendPage, 0);
		return sendPage;
	}

	/**
	 * 此方法除Emotion参数外，同于findNewContentInfoList
	 * Emotion用于做正负面搜索，当关键词搜索加上Emotion枚举出来的情感倾向之后即可得到相应的带有情感色彩的结果
	 */
	public ResultPage<SearchContentResultPojo> findContentInfoListWithEmotion(
			List<String> accountUrls, List<String> keys, Date startPublishTime,
			Date endPublishTime, Date startGrabTime, Date endGrabTime,
			List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage, Emotion emotion) {

		sendPage = searchControlerManager.findNewestContentInfoPageWithEmotion(
				accountUrls, keys, startPublishTime, endPublishTime,
				startGrabTime, endGrabTime, sort, sendPage, emotion);

		return sendPage;
	}

	public ResultPage<FeatureWord> findTopHotWords(int operatorType) {
		// ResultPage<FeatureWord> page = searchControlerManager
		// .findTopHotWords(operatorType);
		// return page;
		return null;
	}

	public ResultPage<TopicDoc> findTopHotTopic(int operatorType) {
		ResultPage<TopicDoc> page = searchControlerManager
				.findTopHotTopics(operatorType);
		return page;
	}

	public ResultPage<SearchContentResultPojo> findDocInfoListByDocUrls(
			List<String> docUrls, ResultPage<SearchContentResultPojo> sendPage) {
		sendPage = searchControlerManager.findDocInfoListByDocUrls(docUrls,
				sendPage);

		return sendPage;
	}

	public ResultPage<SearchAccountResultPojo> findAccountMutualAttentionList(
			String accountUrl, ResultPage<SearchAccountResultPojo> sendPage) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 为华为而添加
	 */
	public ResultPage<SearchAccountResultPojo> findAllAccountInfoListForHuaWei(
			SupportSort sort, ResultPage<SearchAccountResultPojo> page) {
		page = searchControlerManager.findAllAccountInfoListForHuaWei(sort,
				page);
		return page;
	}

	public String findAttentionListByUid(String uid) {
		String uidListString = searchControlerManager
				.findAttentionListByUid(uid);
		return uidListString;
	}

	public String findFansListByUid(String uid) {
		String uidListString = searchControlerManager.findFansListByUid(uid);
		return uidListString;
	}

	public ResultPage<SearchContentResultPojo> findContentInfoList4HuaWei(
			ResultPage<SearchContentResultPojo> sendPage) {
		sendPage = searchControlerManager.findNewestContentInfoPage(null, null,
				null, null, null, null, null, sendPage, 1);
		return sendPage;
	}

	public String findMutualFansListByUid(String uid) {
		String uidListString = searchControlerManager
				.findMutualFansListByUid(uid);
		return uidListString;
	}

}