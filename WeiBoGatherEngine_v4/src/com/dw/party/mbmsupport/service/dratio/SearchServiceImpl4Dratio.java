package com.dw.party.mbmsupport.service.dratio;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbm.elint.manager.business.SearchControlerManager;

//Generated by MyEclipse
@WebService(endpointInterface = "com.dw.party.mbmsupport.service.dratio.ISearchService4Dratio", serviceName = "SearchService4Dratio")
@Component(value = "searchService4Dratio")
public class SearchServiceImpl4Dratio implements ISearchService4Dratio {
	@Autowired
	private SearchControlerManager searchControlerManager;
	
    private String retJson;
	public String findContentInfoList(String args[]) {
		retJson = searchControlerManager.findContentInfoList(args);
		return retJson;
	}

	public void test() {
		System.out.println("无参方法调用成功!");
	}
}