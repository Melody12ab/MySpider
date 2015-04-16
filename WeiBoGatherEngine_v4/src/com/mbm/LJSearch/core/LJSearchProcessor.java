package com.mbm.LJSearch.core;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.SearchAccountResultPojo;
import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.mbm.LJSearch.LJSearchServer;
import com.mbm.LJSearch.pojo.LJFieldValue;
import com.mbm.LJSearch.pojo.LJSearchResult;
import com.mbm.elint.entity.util.TypePojo;
import com.mbm.elint.manager.business.UidToPicUrlManager;
import com.weibo.common.utils.ResultPage;
import com.weibo.common.utils.StaticValue;

@Component
public class LJSearchProcessor {
	@Autowired
	private LJSearchServer searchServer;
	@Autowired
	private UidToPicUrlManager upMapManager;

	public ResultPage<SearchAccountResultPojo> searchAccountInfo(
			ResultPage<SearchAccountResultPojo> page, String query) {
		System.out.println(query);
		if (StringUtils.isBlank(query)) {
			return page;
		}
		searchServer.init(StaticValue.search_accountInfo_ip,
				StaticValue.search_accountInfo_port);
		LJSearchResult ljSearchResult = null;
		try {
			ljSearchResult = searchServer.search(query, page.getFirst() - 1,
					page.getPageSize());
		} catch (Exception e) {
			try {
				searchServer.init(StaticValue.search_accountInfo_ip_bak,
						StaticValue.search_accountInfo_port_bak);
				ljSearchResult = searchServer.search(query,
						page.getFirst() - 1, page.getPageSize());
			} catch (Exception e2) {
				System.out.println("备用account搜索时出错，即将抛出异常!");
				e2.printStackTrace();
			}
		}

		ArrayList<SearchAccountResultPojo> all = new ArrayList<SearchAccountResultPojo>();
		SearchAccountResultPojo searchAccountPojo = null;
		if (ljSearchResult == null)
			return page;
		LJFieldValue[] lJFieldValue = ljSearchResult.getArray();

		for (int i = 0; i < lJFieldValue.length; i++) {
			searchAccountPojo = new SearchAccountResultPojo();
			searchAccountPojo.setLJFieldValue(lJFieldValue[i]);
			/**
			 * 在此处理图片路径和微博type问题
			 */
			if (TypePojo.isQQ(searchAccountPojo.getUrl())) {
				searchAccountPojo.setBlogType("2");
				String temp_pic = upMapManager.getValueByKey(searchAccountPojo
						.getUid());
				searchAccountPojo
						.setPhotoUrl(temp_pic == null ? StaticValue.QQ_None_Pic_Path
								: temp_pic);
			} else {
				searchAccountPojo.setBlogType("1");
				searchAccountPojo.setPhotoUrl("http://tp3.sinaimg.cn/"
						+ searchAccountPojo.getUid() + "/50/5601300688/");
			}

			all.add(searchAccountPojo);
		}
		page.setTotalCount(ljSearchResult.getTotalCount());
		page.setResult(all);
		return page;
	}

	public ResultPage<SearchAccountResultPojo> searchAttentionInfo(
			ResultPage<SearchAccountResultPojo> page, String query) {
		System.out.println(query);
		if (StringUtils.isBlank(query)) {
			return page;
		}
		searchServer.init(StaticValue.search_attentionInfo_ip,
				StaticValue.search_attentionInfo_port);
		LJSearchResult ljSearchResult = null;
		try {
			ljSearchResult = searchServer.search(query, page.getFirst() - 1,
					page.getPageSize());
		} catch (Exception e) {
			try {
				searchServer.init(StaticValue.search_attentionInfo_ip_bak,
						StaticValue.search_attentionInfo_port_bak);
				ljSearchResult = searchServer.search(query,
						page.getFirst() - 1, page.getPageSize());
			} catch (Exception e2) {
				System.out.println("备用attention搜索时出现错误，将抛出异常!");
				e2.printStackTrace();
			}
		}

		ArrayList<SearchAccountResultPojo> all = new ArrayList<SearchAccountResultPojo>();
		SearchAccountResultPojo searchAccountPojo = null;
		if (ljSearchResult == null)
			return page;
		LJFieldValue[] lJFieldValue = ljSearchResult.getArray();

		for (int i = 0; i < lJFieldValue.length; i++) {
			searchAccountPojo = new SearchAccountResultPojo();
			searchAccountPojo.setLJFieldValue(lJFieldValue[i]);

			/**
			 * 在此处理图片路径和微博type问题
			 */
			if (TypePojo.isQQ(searchAccountPojo.getUrl())) {
				searchAccountPojo.setBlogType("2");
				String temp_pic = upMapManager.getValueByKey(searchAccountPojo
						.getUid());
				searchAccountPojo
						.setPhotoUrl(temp_pic == null ? StaticValue.QQ_None_Pic_Path
								: temp_pic);
			} else {
				searchAccountPojo.setBlogType("1");
				searchAccountPojo.setPhotoUrl("http://tp3.sinaimg.cn/"
						+ searchAccountPojo.getUid() + "/50/5601300688/");
			}

			all.add(searchAccountPojo);
		}
		page.setTotalCount(ljSearchResult.getTotalCount());
		page.setResult(all);
		return page;
	}

	public ResultPage<SearchContentResultPojo> searchContentInfo(
			ResultPage<SearchContentResultPojo> page, String query) {
		System.out.println(query);
		if (StringUtils.isBlank(query)) {
			return page;
		}
		// searchServer.init(StaticValue.search_contentInfo_ip,
		// StaticValue.search_contentInfo_port);
		searchServer.init(StaticValue.search_doc_ip,
				StaticValue.search_doc_port);
		LJSearchResult ljSearchResult = null;
		try {
			ljSearchResult = searchServer.search(query, page.getFirst() - 1,
					page.getPageSize());
		} catch (Exception e) {
			try {
				searchServer.init(StaticValue.search_doc_ip_bak,
						StaticValue.search_doc_port_bak);
				ljSearchResult = searchServer.search(query,
						page.getFirst() - 1, page.getPageSize());
			} catch (Exception e2) {
				System.out.println("备用doc搜时出现错误，将抛出异常!");
				e2.printStackTrace();
			}
		}

		ArrayList<SearchContentResultPojo> all = new ArrayList<SearchContentResultPojo>();
		SearchContentResultPojo searchContentResultPojo = null;
		if (ljSearchResult == null)
			return page;
		LJFieldValue[] lJFieldValue = ljSearchResult.getArray();

		for (int i = 0; i < lJFieldValue.length; i++) {
			searchContentResultPojo = new SearchContentResultPojo();
			searchContentResultPojo.setLJFieldValue(lJFieldValue[i]);
			all.add(searchContentResultPojo);
		}
		page.setTotalCount(ljSearchResult.getTotalCount());
		page.setResult(all);
		return page;
	}

	public ResultPage<SearchContentResultPojo> searchNewestContentInfo(
			ResultPage<SearchContentResultPojo> page, String query) {
		System.out.println(query);
		if (StringUtils.isBlank(query)) {
			return page;
		}
		searchServer.init(StaticValue.search_doc_ip,
				StaticValue.search_doc_port);
		LJSearchResult ljSearchResult = null;
		try {
			ljSearchResult = searchServer.search(query, page.getFirst() - 1,
					page.getPageSize());
		} catch (Exception e) {
			// 此时说明出现异常，多因为down机等原因，向备用搜索发送请求
			searchServer.init(StaticValue.search_doc_ip_bak,
					StaticValue.search_doc_port_bak);
			try {
				ljSearchResult = searchServer.search(query,
						page.getFirst() - 1, page.getPageSize());
			} catch (Exception e2) {
				System.out.println("备用搜索出现问题，将抛出异常!");
				e2.printStackTrace();
			}
		}

		ArrayList<SearchContentResultPojo> all = new ArrayList<SearchContentResultPojo>();
		SearchContentResultPojo searchContentResultPojo = null;
		if (ljSearchResult == null)
			return page;
		LJFieldValue[] lJFieldValue = ljSearchResult.getArray();

		for (int i = 0; i < lJFieldValue.length; i++) {
			searchContentResultPojo = new SearchContentResultPojo();
			searchContentResultPojo.setLJFieldValue(lJFieldValue[i]);
			all.add(searchContentResultPojo);
		}
		page.setTotalCount(ljSearchResult.getTotalCount());
		page.setResult(all);
		return page;
	}

	/**
	 * 为缔元信加
	 */
	public ResultPage<SearchContentResultPojo> searchDocInfo(
			ResultPage<SearchContentResultPojo> page, String query) {
		System.out.println(query);
		if (StringUtils.isBlank(query)) {
			return page;
		}
		searchServer.init(StaticValue.search_doc_ip,
				StaticValue.search_doc_port);
		LJSearchResult ljSearchResult = null;
		try {
			ljSearchResult = searchServer.search(query, page.getFirst() - 1,
					page.getPageSize());
		} catch (Exception e) {
			try {
				searchServer.init(StaticValue.search_doc_ip_bak,
						StaticValue.search_doc_port_bak);
				ljSearchResult = searchServer.search(query,
						page.getFirst() - 1, page.getPageSize());
			} catch (Exception e2) {
				System.out.println("备用doc搜索出现异常，将抛出错误!");
				e2.printStackTrace();
			}
		}

		ArrayList<SearchContentResultPojo> all = new ArrayList<SearchContentResultPojo>();
		SearchContentResultPojo searchContentResultPojo = null;
		if (ljSearchResult == null)
			return page;
		LJFieldValue[] lJFieldValue = ljSearchResult.getArray();

		for (int i = 0; i < lJFieldValue.length; i++) {
			searchContentResultPojo = new SearchContentResultPojo();
			searchContentResultPojo.setLJFieldValue(lJFieldValue[i]);
			all.add(searchContentResultPojo);
		}
		page.setTotalCount(ljSearchResult.getTotalCount());
		page.setResult(all);
		return page;
	}

	public ResultPage<SearchContentResultPojo> searchPersonFeatureWords(
			ResultPage<SearchContentResultPojo> page, String query) {
		System.out.println(query);
		if (StringUtils.isBlank(query)) {
			return page;
		}
		// searchServer.init(StaticValue.search_contentInfo_ip,
		// StaticValue.search_contentInfo_port);
		searchServer.init(StaticValue.search_doc_ip,
				StaticValue.search_doc_port);
		LJSearchResult ljSearchResult = null;
		try {
			ljSearchResult = searchServer.search(query, page.getFirst() - 1,
					page.getPageSize());
		} catch (Exception e) {
			try {
				searchServer.init(StaticValue.search_doc_ip_bak,
						StaticValue.search_doc_port_bak);
				ljSearchResult = searchServer.search(query,
						page.getFirst() - 1, page.getPageSize());
			} catch (Exception e2) {
				System.out.println("备用doc搜索出现异常，即将抛出!");
				e2.printStackTrace();
			}
		}

		ArrayList<SearchContentResultPojo> all = new ArrayList<SearchContentResultPojo>();
		SearchContentResultPojo searchContentResultPojo = null;
		if (ljSearchResult == null)
			return page;
		LJFieldValue[] lJFieldValue = ljSearchResult.getArray();

		for (int i = 0; i < lJFieldValue.length; i++) {
			searchContentResultPojo = new SearchContentResultPojo();
			searchContentResultPojo.setLJFieldValue(lJFieldValue[i]);
			all.add(searchContentResultPojo);
		}
		page.setTotalCount(ljSearchResult.getTotalCount());
		page.setResult(all);
		return page;
	}

	public void deleteGroupAndUserItems(String query) throws IOException {
		System.out.println("delete query---" + query);
		if (StringUtils.isBlank(query)) {
			return;
		}
		// searchServer.init(StaticValue.search_contentInfo_ip,
		// StaticValue.search_contentInfo_port);
		// searchServer.deleteQuery(3, query);
	}
}
