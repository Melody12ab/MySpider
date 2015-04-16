package test.serverices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.dto.SearchAccountResultPojo;
import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.dw.party.mbmsupport.dto.SupportSort;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.dw.party.mbmsupport.service.lingjoin.ISearchService4LingJoin;
import com.dw.party.mbmsupport.service.mbm.ISystemOperatorService;
import com.weibo.common.utils.ResultPage;

public class TestCXFService4LingJoin extends TestCase {
	public static String localUrl = "http://localhost:8080/";
	// public static String remoteUrl = "http://124.238.192.8:9999/";
	public static String remoteUrl = "http://124.238.192.8:8888/";
	public static String remoteUrl_53 = "http://124.238.192.53:8080/";

	public void testStartSpiderControler() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer_V2/services/SystemOperatorService?wsdl");
		soapFactoryBean.setAddress(remoteUrl_53
				+ "MbmServer/services/SystemOperatorService?wsdl");
		soapFactoryBean.setServiceClass(ISystemOperatorService.class);
		ISystemOperatorService systemService = (ISystemOperatorService) soapFactoryBean
				.create();

		Result result = systemService.start();

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testFindContentInfoList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/SearchService?wsdl");
		// //
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService4LingJoin?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService4LingJoin.class);
		ISearchService4LingJoin searchService = (ISearchService4LingJoin) soapFactoryBean
				.create();

		List<String> urlList = new ArrayList<String>();
		urlList.add("http://www.weibo.com/1598304682");
		// String url="http://www.weibo.com/2134671703";
		// urlList.add("http://www.weibo.com/1008823111/");
		List<String> keyList = new ArrayList<String>();
//		keyList.add("十八大");
		keyList.add("测试");
		// String url="http://www.weibo.com/1182389073";
		List<SupportSort> sortList = new ArrayList<SupportSort>();
		sortList.add(new SupportSort(
				BlogRemoteDefine.SORT_NAME_INFO.SORT_ARG_NAME_GRABTIME,
				BlogRemoteDefine.SORT_TYPE.ASC));
		ResultPage<SearchContentResultPojo> sendPage = new ResultPage<SearchContentResultPojo>(
				15);
		
		List<String> uids=new ArrayList<String>();
		uids.add("2518388321");
		
		sendPage = searchService.findContentInfoList(null,null, keyList, null, null,
				null, null,null, sortList, sendPage);

		List<SearchContentResultPojo> pojos = sendPage.getResult();
		for (SearchContentResultPojo pojo : pojos) {
			System.out.println(pojo.getNickName());
		}

	}
	

	public void testFindAllContentInfoList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService4LingJoin?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService4LingJoin.class);
		ISearchService4LingJoin searchService = (ISearchService4LingJoin) soapFactoryBean
				.create();

		SupportSort ss=new SupportSort("insertTime",
				BlogRemoteDefine.SORT_TYPE.DESC);
		ResultPage<SearchContentResultPojo> sendPage = new ResultPage<SearchContentResultPojo>(
				15);
		/**
		 * SupportSort为null,代表按insertTime desc
		 */
//		sendPage = searchService.findAllContentInfoList(null, sendPage);
		sendPage = searchService.findAllContentInfoList(ss, sendPage);

		List<SearchContentResultPojo> pojos = sendPage.getResult();
		for (SearchContentResultPojo pojo : pojos) {
			System.out.println(pojo.getNickName());
		}

	}

	
	public void testFindAccountInfoListByKeyWords() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/SearchService?wsdl");
		// //
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService4LingJoin?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService4LingJoin.class);
		ISearchService4LingJoin searchService = (ISearchService4LingJoin) soapFactoryBean
				.create();
		// List<String> urlList = new ArrayList<String>();
		// urlList.add("http://www.weibo.com/1598304682");
		// String url="http://www.weibo.com/2134671703";
		// urlList.add("http://www.weibo.com/1008823111/");
		List<String> keyList = new ArrayList<String>();
		keyList.add("开复");
		// String url="http://www.weibo.com/1182389073";
		List<SupportSort> sortList = new ArrayList<SupportSort>();
		sortList.add(new SupportSort(
				BlogRemoteDefine.SORT_NAME_INFO.SORT_ARG_NAME_GRABTIME,
				BlogRemoteDefine.SORT_TYPE.ASC));
		ResultPage<SearchAccountResultPojo> sendPage = new ResultPage<SearchAccountResultPojo>(
				15);
		sendPage = searchService.findAccountInfoList(keyList, sendPage);

		List<SearchAccountResultPojo> pojos = sendPage.getResult();
		for (SearchAccountResultPojo pojo : pojos) {
			System.out.println(pojo.getNickName());
		}
	}

	public void testFindAllAccountInfoList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService4LingJoin?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService4LingJoin.class);
		ISearchService4LingJoin searchService = (ISearchService4LingJoin) soapFactoryBean
				.create();
		SupportSort ss=new SupportSort("updateTime",
				BlogRemoteDefine.SORT_TYPE.DESC);
		
		ResultPage<SearchAccountResultPojo> sendPage = new ResultPage<SearchAccountResultPojo>(
				15);
		/**
		 * SupportSort传null,即按时间降序
		 */
		sendPage = searchService.findAllAccountInfoList(null, sendPage);
//		sendPage = searchService.findAllAccountInfoList(ss, sendPage);

		List<SearchAccountResultPojo> pojos = sendPage.getResult();
		for (SearchAccountResultPojo pojo : pojos) {
			System.out.println(pojo.getNickName());
		}
	}

	
	public void testFindAccountInfoListByUids() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/SearchService?wsdl");
		// //
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService4LingJoin?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService4LingJoin.class);
		ISearchService4LingJoin searchService = (ISearchService4LingJoin) soapFactoryBean
				.create();
		List<String> uidList = new ArrayList<String>();
//		uidList.add("2518388321");
		uidList.add("2518388");
//		uidList.add("abc");
		ResultPage<SearchAccountResultPojo> sendPage = new ResultPage<SearchAccountResultPojo>(
				15);
		sendPage = searchService.findContentInfoListByUids(uidList,sendPage);

		List<SearchAccountResultPojo> pojos = sendPage.getResult();
		for (SearchAccountResultPojo pojo : pojos) {
			System.out.println(pojo.getNickName());
		}

	}

	public static void main(String[] args) throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer/services/SystemOperatorService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SystemOperatorService?wsdl");
		soapFactoryBean.setServiceClass(ISystemOperatorService.class);
		ISystemOperatorService systemService = (ISystemOperatorService) soapFactoryBean
				.create();

		Result result = systemService.stop();

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());

	}

}
