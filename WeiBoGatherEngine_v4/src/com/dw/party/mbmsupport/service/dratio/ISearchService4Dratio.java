package com.dw.party.mbmsupport.service.dratio;

import javax.jws.WebService;

@WebService
public interface ISearchService4Dratio {
	/**
	 * 检索微博内容 注意：该方法所有参数均可为空，如果某参数为空时则代表忽略该参数。
	 */
	/**
	 * args参数说明
	 * beginTime 即搜索的开始时间
	 * endTime 即搜索的结束时间
	 * keyWord 即搜索的关键词 
	 */
	public String findContentInfoList(String args[]);

	void test();
}