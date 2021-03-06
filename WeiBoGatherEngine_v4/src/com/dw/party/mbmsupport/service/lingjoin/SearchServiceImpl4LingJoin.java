package com.dw.party.mbmsupport.service.lingjoin;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.SearchAccountResultPojo;
import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.dw.party.mbmsupport.dto.SupportSort;
import com.mbm.elint.manager.business.SearchControlerManager;
import com.weibo.common.utils.ResultPage;

//Generated by MyEclipse
@WebService(endpointInterface = "com.dw.party.mbmsupport.service.lingjoin.ISearchService4LingJoin", serviceName = "SearchService4LingJoin")
@Component(value = "searchService4LingJoin")
public class SearchServiceImpl4LingJoin implements ISearchService4LingJoin {

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

	public ResultPage<SearchAccountResultPojo> findAccountInfoList(
			List<String> keyword_name, ResultPage<SearchAccountResultPojo> page) {
		// TODO Auto-generated method stub
		page = searchControlerManager.findAccountInfoListForLingJoin(
				keyword_name, page);
		return page;
	}

	public ResultPage<SearchContentResultPojo> findContentInfoList(
			List<String> accountUrls, List<String> uids, List<String> keys,
			java.lang.String content, java.util.Date startPublishTime,
			java.util.Date endPublishTime, java.util.Date startGrabTime,
			java.util.Date endGrabTime, java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage) {
		sendPage = searchControlerManager.findContentInfoList(accountUrls,
				uids, keys, startPublishTime, endPublishTime, startGrabTime,
				endGrabTime, sort, sendPage);
		return sendPage;
	}
	
	public void test() {
		System.out.println("SearchServiceImpl");
	}

	public ResultPage<SearchAccountResultPojo> findContentInfoListByUids(
			List<String> uidList, ResultPage<SearchAccountResultPojo> page) {
		// List<String> uidToUrl = new ArrayList<String>();
		// for (String uid : uidList) {
		// /**
		// * 此处为解决 uid回传搜索
		// */
		// uidToUrl.add(uid);
		// }
		page = searchControlerManager.findContentInfoList(uidList, page);
		return page;
	}

	public ResultPage<SearchAccountResultPojo> findAllAccountInfoList(
			SupportSort sort, ResultPage<SearchAccountResultPojo> page) {
		page = searchControlerManager.findAllAccountInfoListForHuaWei(sort,
				page);
		return page;
	}

	public ResultPage<SearchContentResultPojo> findAllContentInfoList(
			SupportSort sort, ResultPage<SearchContentResultPojo> page) {

		page = searchControlerManager.findAllContentInfoList(sort, page);

		return page;
	}
}