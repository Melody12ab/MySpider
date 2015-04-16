package test.serverices;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.dw.party.mbmsupport.dto.FeatureWord;
import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.dto.ResultGrab;
import com.dw.party.mbmsupport.dto.SearchAccountResultPojo;
import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.dw.party.mbmsupport.dto.SupportSort;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.dw.party.mbmsupport.global.Emotion;
import com.dw.party.mbmsupport.service.mbm.IGroupingService;
import com.dw.party.mbmsupport.service.mbm.ISearchService;
import com.dw.party.mbmsupport.service.mbm.ISystemOperatorService;
import com.dw.party.mbmsupport.service.mbm.ITaskService;
import com.mbm.elint.entity.mongoDB.TopicDoc;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.ResultPage;

public class TestCXFService4Mbm extends TestCase {
	public static String localUrl = "http://localhost:8080/";
	public static String localUrl_202 = "http://192.168.1.202:8080/";
	// public static String remoteUrl = "http://124.238.192.8:9999/";
	public static String remoteUrl = "http://202.38.128.93:9999/";
	// public static String remoteUrl = "http://124.238.192.8:8888/";
	public static String remoteUrl_53 = "http://124.238.192.53:8090/";

	public static String pro_name_local = "WeiBoGatherEngine_v4";
	public static String pro_name_remote = "WeiBoMaster_V1";

	public void testAddGroup() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl + pro_name_local
				+ "/services/GroupingService?wsdl"); //
		soapFactoryBean.setServiceClass(IGroupingService.class);
		IGroupingService groupingService = (IGroupingService) soapFactoryBean
				.create();

		Result result = groupingService.addGroup("ABC");

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testRemoveGroup() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress("http://localhost:9999/" + pro_name_local
				+ "+/services/GroupingService?wsdl"); //
		soapFactoryBean.setServiceClass(IGroupingService.class);
		IGroupingService groupingService = (IGroupingService) soapFactoryBean
				.create();

		Result result = groupingService.removeGroup("ABC");

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddAccountUrlsToGroup() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress(localUrl+"MbmServer/services/GroupingService?wsdl"); //
		soapFactoryBean.setAddress(remoteUrl + pro_name_local
				+ "/services/GroupingService?wsdl"); //
		soapFactoryBean.setServiceClass(IGroupingService.class);
		IGroupingService groupingService = (IGroupingService) soapFactoryBean
				.create();

		List<String> urls = new ArrayList<String>();
		urls.add("http://www.weibo.com/2518388321");
		// urls.add("two");
		// urls.add("five");
		//		
		Result result = groupingService.addAccountToGroup("zel", urls);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testRemoveAccountUrlsFromGroup() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(localUrl + pro_name_local
				+ "/services/GroupingService?wsdl"); //
		soapFactoryBean.setServiceClass(IGroupingService.class);
		IGroupingService groupingService = (IGroupingService) soapFactoryBean
				.create();

		List<String> urls = new ArrayList<String>();
		// urls.add("one");
		// urls.add("two");
		urls.add("http://www.weibo.com/2518388321");

		Result result = groupingService.removeAccountFromGroup("房产大佬", urls);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testStartSpiderControler() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(localUrl + pro_name_local
				+ "/services/SystemOperatorService?wsdl");
		// soapFactoryBean.setAddress(localUrl_202
		// + "MbmServer/services/SystemOperatorService?wsdl");
		// soapFactoryBean.setAddress(localUrl + pro_name_local
		// + "/services/SystemOperatorService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/SystemOperatorService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl_53
		// + "MbmServer/services/SystemOperatorService?wsdl");
		soapFactoryBean.setServiceClass(ISystemOperatorService.class);
		ISystemOperatorService systemService = (ISystemOperatorService) soapFactoryBean
				.create();

		Result result = systemService.start();

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public static void testAddAccountGrabTask() throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl + pro_name_remote
				+ "/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl_53
		// + "MbmServer/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(localUrl_202
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/SystemOperatorService?wsdl");
		// soapFactoryBean.setServiceClass(ISystemOperatorService.class);
		// ISystemOperatorService systemService = (ISystemOperatorService)
		// soapFactoryBean
		// .create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();
		// FileReader fr = new FileReader("D:/meiqu.txt");
		// BufferedReader br = new BufferedReader(fr);
		// String temp_line = null;
		// int lines = 0;
		// while ((temp_line = br.readLine()) != null) {
		// accountUrl.add(new UrlPojo(temp_line, 0));
		// }
		// br.close();
		// fr.close();

		accountUrl.add(new UrlPojo("http://www.weibo.com/renzhiqiang", 1));

		Result result = taskService.addAccountGrabTask(accountUrl);
		// Result result = systemService.addAccountGrabTask(accountUrl);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testIsFinishedAccountAttentionGrab() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl + pro_name_remote
				+ "/services/TaskService?wsdl"); //
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		// accountUrl.add(new UrlPojo("http://www.weibo.com/imaruko", 3));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/1889377232", 3));
		accountUrl.add(new UrlPojo("http://www.weibo.com/2518388321", 3));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/kaifulee", 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "008", 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "009", 3));

		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1197161814",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "yinengjing",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1182389073",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1961637472",
		// 3));
		// //
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2134671703",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功", 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 3));

		List<ResultGrab> resultGrab = taskService
				.isFinishedAccountAttentionGrab(accountUrl);

		for (ResultGrab result : resultGrab) {
			System.out.println("resule code" + result.getResult());
			System.out.println("resule info" + result.getUrlPojo());
		}
	}

	public void testIsFinishedAccountGrab() throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl + pro_name_remote
				+ "/services/TaskService?wsdl"); //
		// soapFactoryBean.setAddress(localUrl_202
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		// FileReader fr = new FileReader("D:/2a.txt");
		// FileReader fr = new FileReader("D:/meiqu.txt");
		// BufferedReader br = new BufferedReader(fr);
		String temp_line = "http://weibo.com/2827584527";
		// int lines = 0;

		// while ((temp_line = br.readLine()) != null) {
		// if (temp_line.length() != 31) {
		// System.out.println(temp_line);
		// System.out.println(temp_line.length());
		// }
		accountUrl.add(new UrlPojo(temp_line, 2));
		// lines++;
		// }
		// br.close();
		// fr.close();

		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "312130005",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321",
		// 0));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/1641206134", 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "004", 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "005", 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "006", 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功", 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 1));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/2518388321", 1));

		List<ResultGrab> resultGrab = taskService
				.isFinishedAccountGrab(accountUrl);
		int error_items = 0;
		for (ResultGrab result : resultGrab) {
			// System.out.println("resule code" + result.getInfo());
			System.out.println("resule code" + result.getResult());
			System.out.println("resule info" + result.getUrlPojo());
		}
	}

	public void testIsFinishedAccountInoGrab() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl + pro_name_remote
				+ "/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		accountUrl.add(new UrlPojo("http://www.weibo.com/2827584527", 2));

		List<ResultGrab> resultGrab = taskService
				.isFinishedAccountInfoGrab(accountUrl);

		for (ResultGrab result : resultGrab) {
			System.out.println("resule code" + result.getResult());
			System.out.println("resule info" + result.getUrlPojo());
		}
	}

	public void testRemoveAccountInfoGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl + pro_name_local
				+ "/services/TaskService?wsdl"); //
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		accountUrl.add(new UrlPojo("http://www.weibo.com/1304062142", 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "jaychoublog",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2134671703",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功", 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 1));

		List<Result> resultGrab = taskService
				.removeAccountInfoGrabTask(accountUrl);

		for (Result result : resultGrab) {
			System.out.println("resule code" + result.getResultCode());
			System.out.println("resule info" + result.getInfo());
		}
	}

	public void testAddAccountInfoGrabTask() throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl + pro_name_local
		// + "/services/TaskService?wsdl");
		soapFactoryBean.setAddress(remoteUrl + pro_name_remote
				+ "/services/TaskService?wsdl"); //
		// soapFactoryBean.setAddress(localUrl_202 + pro_name_remote
		// + "/services/TaskService?wsdl"); //
		// soapFactoryBean.setAddress(remoteUrl_53
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		// accountUrl.add(new UrlPojo("http://www.weibo.com/1304062142", 2,
		// TaskLevel.A));
		// accountUrl.add(new UrlPojo("http://t.qq.com/taozi90513", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://t.qq.com/wjs790023117", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://t.qq.com/yangqiujin199", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://t.qq.com/liuzhizhong4828", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/2542613193", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/2518388321", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/rmrb", 2,
		// TaskLevel.C));

		// accountUrl.add(new UrlPojo("http://www.weibo.com/weixiaoshuo", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/kaifulee", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/renzhiqiang", 2,
		// TaskLevel.C));

		// accountUrl.add(new UrlPojo("http://weibo.com/weixiaoshuo", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://weibo.com/2437854192", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/meitixiaolaba",
		// 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/newmediapd", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/xinhuashidian",
		// 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/masrb", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/rmrb", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/rbnews", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/nfrb", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/milnews", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/2308711425", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/dfwb", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/wemag", 6));
		// accountUrl.add(new UrlPojo("http://weibo.com/maijiashuoshi", 2));
		// accountUrl.add(new UrlPojo("http://weibo.com/1987718913", 2));
		// accountUrl.add(new UrlPojo("http://weibo.com/1848266067", 2));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/1197161814", 2));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/jczmdeveloper", 2));
		accountUrl.add(new UrlPojo("http://www.weibo.com/kaifulee", 2));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/001", 2));
		// accountUrl.add(new UrlPojo("http://weibo.com/2781961487", 2));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/1752467960", 2));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2134671703",
		// 2));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625",
		// 2));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103",
		// 2));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功", 1));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/shzjxwzx", 1));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/renzhiqiang", 1));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/2518388321", 1));
		// String txt = IOUtil.readFile("D:/url.txt");
		// String[] urlArray = txt.split("\n");
		// int temp_count = 0;
		// for (String url : urlArray) {
		// if (url.trim().length() > 0) {
		// temp_count++;
		// accountUrl.add(new UrlPojo(url, 1, TaskLevel.C));
		// }
		// }
		// System.out.println("temp_count====" + temp_count);
		//
		// txt = IOUtil.readFile("D:/url4.txt");
		// for (String url : urlArray) {
		// if (url.trim().length() > 0) {
		// accountUrl.add(new UrlPojo(url, 1));
		// }
		// }
		Result result = taskService.addAccountInfoGrabTask(accountUrl);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddCommentInfoGrabTask() throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer_MongoDB_V1/services/TaskService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		// soapFactoryBean.setAddress(remoteUrl_53
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix
		// + "1197161814/zohb6Af1D", 2));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix_ProAndEnter
		// + "1726390450/zhj6Abqk0", 2));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix
		// + "1197161814/zohb6Af1D", 2));

		accountUrl.add(new UrlPojo(FormatOutput.urlPrefix_ProAndEnter
				+ "1803182562/zoiYI8yU6", 2));

		//		
		// String txt = IOUtil.readFile("D:/url.txt");
		// String[] urlArray = txt.split("\n");
		// int temp_count = 0;
		// for (String url : urlArray) {
		// if (url.trim().length() > 0) {
		// temp_count++;
		// accountUrl.add(new UrlPojo(url, 1, TaskLevel.C));
		// }
		// }
		// System.out.println("temp_count====" + temp_count);
		//
		Result result = taskService.addCommentInfoGrabTask(accountUrl);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddAccountAttentionGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/TaskService?wsdl");
		// //
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		// UrlPojo urlPojo = new UrlPojo(FormatOutput.urlPrefix + "2518388321",
		// 3);
		// UrlPojo urlPojo = new UrlPojo(FormatOutput.urlPrefix + "renzhiqiang",
		// 3);
		UrlPojo urlPojo = new UrlPojo(FormatOutput.urlPrefix + "yaochen", 3);
		Result result = taskService.addAccountAttentionGrabTask(urlPojo);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddAccountListAttentionGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer_QQ_V7/services/TaskService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625",
		// 3));
		accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321", 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1197161814",
		// 3));

		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "008", 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "009", 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1197161814",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "yinengjing",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2168499337",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1961637472",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功", 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 3));

		Result result = taskService.addAccountListAttentionGrabTask(accountUrl);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddKeyWordsGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer_MongoDB_V1/services/TaskService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl_53
		// + "MbmServer/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(localUrl_202
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<KeywordPojo> keywords = new ArrayList<KeywordPojo>();

		// keywords.add(new KeywordPojo("周克强", 0));
		// keywords.add(new KeywordPojo("刘翔假摔", 0));
		// keywords.add(new KeywordPojo("北理工女尸", 0));
		// keywords.add(new KeywordPojo("良乡女尸", 0));
		// keywords.add(new KeywordPojo("北湖女尸", 0));
		// keywords.add(new KeywordPojo("神探狄仁杰 第五部", 0));
		// keywords.add(new KeywordPojo("代表哥", 0));
		// keywords.add(new KeywordPojo("杨坤 丁丁", 0));
		// keywords.add(new KeywordPojo("杨达才", 0));
		// keywords.add(new KeywordPojo("钓鱼岛", 0));
		// keywords.add(new KeywordPojo("中国好声音", 0));
		// keywords.add(new KeywordPojo("9.18", 0));
		// keywords.add(new KeywordPojo("中日", 0));
		// keywords.add(new KeywordPojo("七夕情人节", 0));
		// keywords.add(new KeywordPojo("你说的 123 abc 从头", 0));
		// keywords.add(new KeywordPojo("成功了 123 456", 0));
		// keywords.add(new KeywordPojo("我们说好的 你在哪里呢", 0));
		// keywords.add(new KeywordPojo("你好我好大家好 123 天儿", 0));
		// keywords.add(new KeywordPojo("刘德华 天天向上", 0));
		// keywords.add(new KeywordPojo("我们说好的 你在哪里呢 123456 678", 0));
		// keywords.add(new KeywordPojo("中国好声音", 0));
		// keywords.add(new KeywordPojo("非诚勿扰", 0));
		// keywords.add(new KeywordPojo("北京青年", 0));
		// keywords.add(new KeywordPojo("天天向上", 0));
		// keywords.add(new KeywordPojo("朱丹", 0));
		// keywords.add(new KeywordPojo("王珞丹", 0));
		// keywords.add(new KeywordPojo("你好 他好 大家好 真的吗", 0));
		// keywords.add(new KeywordPojo("台独", 0));

		// keywords.add(new KeywordPojo("两会", 0));
		// keywords.add(new KeywordPojo("全国人民代表大会", 0));
		// keywords.add(new KeywordPojo("中国人民政治协商会议", 0));
		// keywords.add(new KeywordPojo("全国人民代表大会政治协商会议", 0));
		// keywords.add(new KeywordPojo("证券公司", 0));
		// keywords.add(new KeywordPojo("期货公司", 0));
		// keywords.add(new KeywordPojo("高考", 0));
		// keywords.add(new KeywordPojo("死猪", 0));

		// keywords.add(new KeywordPojo("北京下雨", 0));
		// keywords.add(new KeywordPojo("高考下雨", 0));
		// keywords.add(new KeywordPojo("高考", 0));
		// keywords.add(new KeywordPojo("端午节", 0));
		keywords.add(new KeywordPojo("高考作文", 0));

		Result result = taskService.addKeyWordsGrabTask(keywords);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddTopicTitlesGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(localUrl + pro_name_local
				+ "/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl_53
		// + "MbmServer/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(localUrl_202
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<KeywordPojo> topicTitlePojos = new ArrayList<KeywordPojo>();

		topicTitlePojos.add(new KeywordPojo("新恋爱时代", 0));

		Result result = taskService.addTopicTitleInfoGrabTask(topicTitlePojos);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testIsFinishedKeyWordGrab() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/TaskService?wsdl");
		// //
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<KeywordPojo> keywords = new ArrayList<KeywordPojo>();
		// keywords.add(new KeywordPojo("七夕情人节", 0));
		// keywords.add(new KeywordPojo("北京暴雨", 0));
		// keywords.add(new KeywordPojo("你说的 123 abc 从头", 0));
		// keywords.add(new KeywordPojo("成功了 123 456", 0));
		// keywords.add(new KeywordPojo("我们说好的 你在哪里呢", 0));
		// keywords.add(new KeywordPojo("你好我好大家好 123 天儿", 0));
		// keywords.add(new KeywordPojo("刘德华 天天向上", 0));
		// keywords.add(new KeywordPojo(" 我们说好的 你在哪里呢 123456 678", 0));
		//
		// keywords.add(new KeywordPojo("天天向上", 0));
		// keywords.add(new KeywordPojo("朱丹", 0));
		// keywords.add(new KeywordPojo("王珞丹", 0));
		// keywords.add(new KeywordPojo("你好 他好 大家好 真的吗", 0));

		// keywords.add(new KeywordPojo("周克强", 0));
		// keywords.add(new KeywordPojo("北京暴雨", 0));
		// keywords.add(new KeywordPojo("北京暴雨123", 0));
		//
		keywords.add(new KeywordPojo("北京暴雨123", 0));
		keywords.add(new KeywordPojo("神探狄仁杰 第五部", 0));
		List<ResultGrab> list = taskService.isFinishedKeyWordGrab(keywords);

		for (ResultGrab result : list) {
			System.out.println("resule code" + result.getResult());
			System.out.println("resule info" + result.getKeywordPojo());
		}
	}

	public void testRemoveKeyWordsGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(localUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		// soapFactoryBean
		// .setAddress(remoteUrl+"MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<KeywordPojo> keywords = new ArrayList<KeywordPojo>();
		// keywords.add(new KeywordPojo("台独", 0));
		// keywords.add(new KeywordPojo("周克华", 0));
		// keywords.add(new KeywordPojo("日美军演", 0));
		// keywords.add(new KeywordPojo("钓鱼岛", 0));
		// keywords.add(new KeywordPojo("北京 暴雨", 0));
		// keywords.add(new KeywordPojo("爆头哥", 0));
		// keywords.add(new KeywordPojo("海葵", 0));
		keywords.add(new KeywordPojo("你说的 123 abc 从头", 0));

		List<Result> resultList = taskService.removeKeyWordsGrabTask(keywords);
		for (Result result : resultList) {
			System.out.println("resule code" + result.getResultCode());
			System.out.println("resule info" + result.getInfo());
		}
	}

	public static void testFindAccountInfoList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl");
		// soapFactoryBean.setAddress(localUrl_202
		// + "MbmServer/services/SearchService?wsdl");
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer/services/SearchService?wsdl");
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		List<String> list = new ArrayList<String>();
		// http://weibo.com/u/1365323271 http://weibo.com/lengxiaohua
		// list.add("http://www.weibo.com/1249193625");
		// list.add("http://www.weibo.com/2518388321");
		// list.add("http://www.weibo.com/2559310301");
		// list.add("http://www.weibo.com/renzhiqiang");
		list.add("http://www.weibo.com/1647687670");
		// list.add("http://www.weibo.com/1912106827");
		// list.add("http://weibo.com/lengxiaohua");
		// list.add("http://www.weibo.com/1008823111/");
		// list.add("http://www.weibo.com/1496887652/");
		// 此时的list不要超过60个url串
		ResultPage<SearchAccountResultPojo> sendPage = new ResultPage<SearchAccountResultPojo>(
				15);
		sendPage = searchService.findAccountInfoList(list, null, sendPage);

		List<SearchAccountResultPojo> pojos = sendPage.getResult();
		for (SearchAccountResultPojo pojo : pojos) {
			System.out.println("昵称" + pojo.getNickName());
			System.out.println("微博数目" + pojo.getWbNum());
			System.out.println("关注数目" + pojo.getAttentionNum());
			System.out.println("粉丝数目" + pojo.getFansNum());
		}
		System.out.println(sendPage.getTotalCount());
	}

	public static void testFindAllAccountInfoList4HuaWei() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "WeiBoMaster_V1/services/SearchService?wsdl");
		// soapFactoryBean.setAddress(localUrl_202
		// + "MbmServer/services/SearchService?wsdl");
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer/services/SearchService?wsdl");
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		ResultPage<SearchAccountResultPojo> sendPage = new ResultPage<SearchAccountResultPojo>(
				15);
		sendPage.setPageNo(2);
		sendPage = searchService
				.findAllAccountInfoListForHuaWei(null, sendPage);

		List<SearchAccountResultPojo> pojos = sendPage.getResult();
		for (SearchAccountResultPojo pojo : pojos) {
			System.out.println("昵称" + pojo.getNickName());
			System.out.println("微博数目" + pojo.getWbNum());
			System.out.println("关注数目" + pojo.getAttentionNum());
			System.out.println("粉丝数目" + pojo.getFansNum());
		}
		System.out.println(sendPage.getTotalCount());
	}

	public static void testFindContentInfoList() throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer_QQ_V6/services/SearchService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();
		List<String> urlList = new ArrayList<String>();
		urlList.add("http://www.weibo.com/1665091802");
		// String groupName = "zel";
		// List<String> keyList = null;
		List<String> keyList = new ArrayList<String>();
		keyList.add("生意 123");
		List<SupportSort> sortList = new ArrayList<SupportSort>();
		// sortList.add(new SupportSort(
		// BlogRemoteDefine.SORT_NAME_INFO.SORT_ARG_NAME_GRABTIME,
		// BlogRemoteDefine.SORT_TYPE.DESC));
		sortList.add(new SupportSort(
				BlogRemoteDefine.SORT_NAME_INFO.SORT_ARG_NAME_CommentNum,
				BlogRemoteDefine.SORT_TYPE.DESC));
		ResultPage<SearchContentResultPojo> sendPage = new ResultPage<SearchContentResultPojo>(
				15);
		// sendPage = searchService.findContentInfoList(null, null, null, null,
		// new Date(), null, null, sortList, sendPage);
		for (int i = 0; i < 1; i++) {
			sendPage = searchService.findContentInfoListWithEmotion(urlList,
					null, new Date(new Date().getTime() - 1000 * 24 * 60 * 60
							* 1000), new Date(), null, null, sortList,
					sendPage, Emotion.Good_Effect);
			List<SearchContentResultPojo> pojos = sendPage.getResult();
			for (SearchContentResultPojo pojo : pojos) {
				System.out.println(pojo.getNickName());
				System.out.println(pojo.getContent());
			}
			System.out.println("总数---" + sendPage.getTotalCount());
			Thread.sleep(1000);
		}
	}

	public static void testFindContentInfoList4HuaWei() throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer_QQ_V6/services/SearchService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "WeiBoMaster_V1/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();
		ResultPage<SearchContentResultPojo> sendPage = new ResultPage<SearchContentResultPojo>(
				100);
		// sendPage = searchService.findContentInfoList(null, null, null, null,
		// new Date(), null, null, sortList, sendPage);
		Date startPublishTime = new Date(109, 00, 01);
		Date endPublishTime = new Date();

		Date startGrabTime = new Date(113, 9, 10, 20, 28, 20);
		long timeInterval = 60 * 1000;
		Date endGrabTime = new Date(startGrabTime.getTime() + timeInterval);

		for (int i = 0; i < 1; i++) {
			sendPage.setPageNo(5);
			sendPage = searchService.findContentInfoList4HuaWei(sendPage);
			List<SearchContentResultPojo> pojos = sendPage.getResult();
			for (SearchContentResultPojo pojo : pojos) {
				System.out.println(pojo.getNickName());
				System.out.println(pojo.getContent());
			}
			System.out.println("总数---" + sendPage.getTotalCount());
//			Thread.sleep(1000);
		}
	}

	public void testFindAttentionListByUid4HuaWei() {
		String uid = "1182389073";
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:8080/"+pro_name_local+"/services/SearchService?wsdl");
		soapFactoryBean.setAddress(remoteUrl + pro_name_remote
				+ "/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		String uidListString = searchService.findAttentionListByUid(uid);

		System.out.println("得到的uidListString---"
				+ uidListString.split(",").length);
	}

	public void testFindMutualFansByUid4HuaWei() {
		String uid = "1586672964";
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:8080/"+pro_name_local+"/services/SearchService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		String uidListString = searchService.findMutualFansListByUid(uid);

		System.out.println("得到的uidListString---"
				+ uidListString.split(",").length);
	}

	public void testFindFansByUid4HuaWei() {
		String uid = "1182389073";
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress("http://localhost:8080/" + pro_name_local
				+ "/services/SearchService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		String uidListString = searchService.findFansListByUid(uid);

		System.out.println("得到的uidListString---"
				+ uidListString.split(",").length);
	}

	public void testFindHotTopicInfoList() throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer_QQ_V6/services/SearchService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl"); //
		// soapFactoryBean.setAddress(localUrl_202
		// + "MbmServer/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();
		ResultPage<TopicDoc> sendPage = new ResultPage<TopicDoc>(15);
		for (int i = 0; i < 1; i++) {
			sendPage = searchService.findTopHotTopic(0);
			List<TopicDoc> pojos = sendPage.getResult();
			for (TopicDoc pojo : pojos) {
				System.out.println(pojo);
			}
			// System.out.println("第" + i + "个---" + sendPage.getTotalCount());
			Thread.sleep(1000);
		}

	}

	public void testFindAccountAttentionList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/SearchService?wsdl");
		// //
		soapFactoryBean.setAddress(remoteUrl + pro_name_remote
				+ "/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();
		// List<String> urlList=new ArrayList<String>();
		// urlList.add("http://www.weibo.com/1598304682");
		// String url = "http://www.weibo.com/2231283485";
		// String url = "http://www.weibo.com/sunhonglei";
		// String url = "http://weibo.com/leehom";
		String url = "http://www.weibo.com/2518388321";
		// String url = "http://www.weibo.com/1651168261";

		// urlList.add("http://www.weibo.com/1008823111/");
		// urlList.add("http://www.weibo.com/1496887652/");
		// urlList.add("http://www.weibo.com/2134671703");

		// List<String> keyList = new ArrayList<String>();
		// keyList.add("写作");
		// keyList.add("李敖");
		// keyList.add("整天努力");

		// keyList.add("");
		// String url="http://www.weibo.com/1182389073";
		ResultPage<SearchAccountResultPojo> sendPage = new ResultPage<SearchAccountResultPojo>(
				200);
		sendPage = searchService.findAccountAttentionList(url, sendPage);

		List<SearchAccountResultPojo> pojos = sendPage.getResult();
		HashSet<String> set = new HashSet<String>();
		for (SearchAccountResultPojo pojo : pojos) {
			set.add(pojo.getNickName());
			System.out.println(pojo.getNickName());
		}
		System.out.println("set-----" + set.size());
		System.out.println("返回的条数" + pojos.size());
		System.out.println("返回的总条数" + sendPage.getTotalCount());

	}

	public void testFindAccountInfoListByUid() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean
				.setAddress("http://localhost:9999/MbmServer/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		List<String> uidList = new ArrayList<String>();
		uidList.add("2518388321");
		ResultPage<SearchAccountResultPojo> sendPage = new ResultPage<SearchAccountResultPojo>(
				15);
		sendPage = searchService.findAccountInfoListByUid(uidList, sendPage);
		List<SearchAccountResultPojo> pojos = sendPage.getResult();
		for (SearchAccountResultPojo pojo : pojos) {
			System.out.println(pojo.getNickName());
		}
		System.out.println("total size---" + sendPage.getTotalCount());
	}

	public void testFindAllAccountInfoListForHuaWei() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		// List<String> uidList = new ArrayList<String>();
		// uidList.add("2518388321");
		ResultPage<SearchAccountResultPojo> sendPage = new ResultPage<SearchAccountResultPojo>(
				15);
		int i = 1;
		while (i < 5) {
			sendPage.setPageNo(i);
			sendPage = searchService.findAllAccountInfoListForHuaWei(null,
					sendPage);
			List<SearchAccountResultPojo> pojos = sendPage.getResult();
			for (SearchAccountResultPojo pojo : pojos) {
				System.out.print(pojo.getUid());
				System.out.println(pojo.getNickName());
			}
			System.out.println("total size---" + sendPage.getTotalCount());

			i++;
		}
	}

	public void testFindContentInfoListByDocUrls() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl_202 + pro_name_remote
		// + "/services/SearchService?wsdl"); //
		soapFactoryBean.setAddress(remoteUrl + pro_name_remote
				+ "/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		List<String> docUrls = new ArrayList<String>();
		docUrls.add("http://www.weibo.com/1197161814/zBhcu05fP");
		docUrls.add("http://www.weibo.com/2656274875/zBg2JiYhw");
		docUrls.add("http://www.weibo.com/1585354514/zB1X5zrPR");

		ResultPage<SearchContentResultPojo> sendPage = new ResultPage<SearchContentResultPojo>(
				15);
		sendPage = searchService.findDocInfoListByDocUrls(docUrls, sendPage);
		List<SearchContentResultPojo> pojos = sendPage.getResult();
		for (SearchContentResultPojo pojo : pojos) {
			System.out.println(pojo);
		}
		System.out.println("total size---" + sendPage.getTotalCount());
	}

	public void testFindPersonFeatureWordsBySendUrl() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(localUrl_202
				+ "MbmServer/services/SearchService?wsdl"); //
		// soapFactoryBean
		// .setAddress(localUrl+"/MbmServer_V2/services/SearchService?wsdl");
		// //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();
		// ResultPage<FeatureWord>
		// wordsList=searchService.findPersonFeatureWordsByUids("1197931472");
		// wordsList=searchService.findPersonFeatureWordsByUids("1765694800");
		ResultPage<FeatureWord> wordsList = searchService
				.findPersonFeatureWordsBySendUrl(
						"http://www.weibo.com/renzhiqiang", 0);
		// ResultPage<FeatureWord>
		// wordsList=searchService.findPersonFeatureWordsByUids("2518388321");

		System.out.println(wordsList.getResult().size());

		for (FeatureWord word : wordsList.getResult()) {
			// System.out.println(word.getWord() + "------" + word.getCount());
			System.out.println(word);
		}

		System.out.println("resule code---" + wordsList.getResultCode());
		System.out.println("resule info---" + wordsList.getInfo());
	}

	public static void testFindHotWords() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl_202
		// + "MbmServer/services/SearchService?wsdl"); //
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl"); //
		// soapFactoryBean
		// .setAddress(localUrl+"/MbmServer_V2/services/SearchService?wsdl");
		// //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();
		// ResultPage<FeatureWord>
		// wordsList=searchService.findPersonFeatureWordsByUids("1197931472");
		// wordsList=searchService.findPersonFeatureWordsByUids("1765694800");
		ResultPage<FeatureWord> wordsList = searchService.findTopHotWords(0);
		// ResultPage<FeatureWord>
		// wordsList=searchService.findPersonFeatureWordsByUids("2518388321");

		System.out.println(wordsList.getResult().size());

		for (FeatureWord word : wordsList.getResult()) {
			System.out.println(word);
		}

		System.out.println("resule code---" + wordsList.getResultCode());
		System.out.println("resule info---" + wordsList.getInfo());
	}

	public static void testInsertNewWord() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean
				.setAddress("http://124.238.192.8:9999/MbmServer/services/TaskService?wsdl"); //
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/TaskService?wsdl");
		// //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		// Result result=taskService.addNewWordTask("微博");
		Result result = taskService.addNewWordTask("苏宁易购123");
		// Result result=taskService.addNewWordTask("中国人");

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testInsertNewWordList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/TaskService?wsdl");
		// //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<String> wordList = new ArrayList<String>();

		wordList.add("天天");
		wordList.add("向上");
		wordList.add("试试水");
		wordList.add("微博");

		Result result = taskService.addNewWordListTask(wordList);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());

	}

	public void testFindPersonFeatureWordsByContent() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/SearchService?wsdl");
		soapFactoryBean.setAddress(localUrl_202
				+ "MbmServer/services/SearchService?wsdl");
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer_MongoDB_V1/services/SearchService?wsdl");
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService taskService = (ISearchService) soapFactoryBean.create();

		// String content =
		// "你是我的玫瑰花,毛主席是我们的大恩人,大恩人，思念,也许这才是真正的爱性，希望每个人都能快快乐乐,现在的房价真的没法搞了，太贵了太贵了";
		String content = "东方网12月4日消息：2009年10月21日,辽宁省阜新市委收到举报信,举报以付玉红为首吸毒、强奸、聚众淫乱,阜新市委政法委副书记于洋等参与吸毒、强奸、聚众淫乱等。对此,阜新市委高度重视,责成阜新市公安局立即成立调查组,抽调精干力量展开调查。　　调查期间,署名举报人上官宏祥又通过尹东方(女)向阜新市公安局刑警支队提供书面举报,举报于洋等参与吸毒、强奸、聚众淫乱。11月19日,正义网发表上官宏祥接受记者专访,再次实名举报于洋等参与吸毒、强奸、聚众淫乱,引起网民广泛关注。对此辽宁省政法委、省公安厅高度重视。当日,责成有关领导专程赴阜新听取案件调查情况。为加强对案件的督办和指导,省有关部门迅速成立工作组,赴阜新督办、指导案件调查工作,并将情况上报有关部门。　　经前一段调查证明,举报事实不存在,上官宏祥行为触犯《刑法》第243条,涉嫌诬告陷害罪。根据《刑事诉讼法》有关规定,阜新市公安局已于11月27日依法立案侦查。上官宏祥已于2009年12月1日到案,12月2日阜新市海州区人大常委会已依法停止其代表资格,阜新市公安局对其进行刑事拘留,并对同案人尹东方进行监视居住。现侦查工作正在进行中。";
		// String content =
		// "张华平推出的NLPIR分词系统，又名ICTCLAS2013，新增新词识别、关键词提取、微博分词功能。";
		ResultPage<FeatureWord> wordsList = taskService
				.findPersonFeatureWordsByContent(content);

		System.out.println(wordsList.getResult().size());

		for (FeatureWord word : wordsList.getResult()) {
			System.out.println(word);
		}

		System.out.println("resule code---" + wordsList.getResultCode());
		System.out.println("resule info---" + wordsList.getInfo());
	}

	public void testStopSpiderControler() {
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

	public static void main(String[] args) throws Exception {
		// JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// // soapFactoryBean.setAddress(localUrl
		// // + "MbmServer/services/SystemOperatorService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/SystemOperatorService?wsdl");
		// soapFactoryBean.setServiceClass(ISystemOperatorService.class);
		// ISystemOperatorService systemService = (ISystemOperatorService)
		// soapFactoryBean
		// .create();
		//
		// Result result = systemService.stop();
		//
		// System.out.println("resule code" + result.getResultCode());
		// System.out.println("resule info" + result.getInfo());

		// JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/SearchService?wsdl");
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer/services/SearchService?wsdl");
		// soapFactoryBean.setServiceClass(ISearchService.class);
		// ISearchService taskService = (ISearchService)
		// soapFactoryBean.create();
		//
		// String content =
		// "你是我的玫瑰花,毛主席是我们的大恩人,大恩人，思念,也许这才是真正的爱性，希望每个人都能快快乐乐,现在的房价真的没法搞了，太贵了太贵了";
		//
		// ResultPage<FeatureWord> wordsList = taskService
		// .findPersonFeatureWordsByContent(content);
		//
		// System.out.println(wordsList.getResult().size());
		//
		// for (FeatureWord word : wordsList.getResult()) {
		// System.out.println(word);
		// }
		//
		// System.out.println("resule code---" + wordsList.getResultCode());
		// System.out.println("resule info---" + wordsList.getInfo());
		// testInsertNewWord();
		// testFindHotWords();
		testFindContentInfoList();
		// testFindAccountInfoList();
		// testAddAccountGrabTask();
	}
}
