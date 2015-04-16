package test.serverices;

import java.util.ArrayList;
import java.util.Date;
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
import com.dw.party.mbmsupport.global.TaskLevel;
import com.dw.party.mbmsupport.service.mbm.IGroupingService;
import com.dw.party.mbmsupport.service.mbm.ISearchService;
import com.dw.party.mbmsupport.service.mbm.ISystemOperatorService;
import com.dw.party.mbmsupport.service.mbm.ITaskService;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.util.IOUtil;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.ResultPage;

public class TestCXFService4Test extends TestCase {
	public static String localUrl = "http://localhost:8080/";
	public static String localUrl_QQ_V5 = "http://localhost:8080/";
	// public static String remoteUrl = "http://124.238.192.8:9999/";
	public static String remoteUrl = "http://124.238.192.8:8888/";
	public static String remoteUrl_53 = "http://124.238.192.53:8090/";

	public void testAddGroup() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/GroupingService?wsdl"); //
		soapFactoryBean.setServiceClass(IGroupingService.class);
		IGroupingService groupingService = (IGroupingService) soapFactoryBean
				.create();

		Result result = groupingService.addGroup("ABC");

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testRemoveGroup() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean
				.setAddress("http://localhost:9999/MbmServer/services/GroupingService?wsdl"); //
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
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/GroupingService?wsdl"); //
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
		soapFactoryBean.setAddress(localUrl
				+ "MbmServer/services/GroupingService?wsdl"); //
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
		soapFactoryBean.setAddress(localUrl
				+ "MbmServer_QQ_V6/services/SystemOperatorService?wsdl");
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

	public void testAddAccountGrabTask() throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(localUrl
				+ "MbmServer_QQ_V6/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl_53
		// + "MbmServer/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		// accountUrl.add(new UrlPojo("http://e.weibo.com/2559310301", 1));
		accountUrl.add(new UrlPojo("http://www.weibo.com/1912106827", 1));

		// accountUrl.add(new UrlPojo("http://www.weibo.com/1717385891", 1));
		// accountUrl.add(new UrlPojo("http://t.qq.com/jczmdeveloper",
		// 1,TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://t.qq.com/jczmdeveloper", 1));
		// accountUrl.add(new UrlPojo("http://t.qq.com/erliang20088", 1));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/weixiaoshuo", 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "456", 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1197931472",
		// 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 1));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/2518388321", 1));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/005", 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "yinengjing",
		// 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625",
		// 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103",
		// 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1961637472",
		// 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1713926427",
		// 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功", 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2815443080/",
		// 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321/",
		// 1));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "lichengpeng",
		// 2));
		// accountUrl.add(new UrlPojo("http://weibo.com/zhyj", 1));

		// String txt = IOUtil.readFile("D:/url1.txt");
		String txt = IOUtil.readFile("D:/url3.txt");
		String[] urlArray = txt.split("\n");
		for (String url : urlArray) {
			if (url.trim().length() > 0) {
				accountUrl.add(new UrlPojo(url, 1));
			}
		}

		txt = IOUtil.readFile("D:/url4.txt");
		for (String url : urlArray) {
			if (url.trim().length() > 0) {
				accountUrl.add(new UrlPojo(url, 1));
			}
		}

		Result result = taskService.addAccountGrabTask(accountUrl);
		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testIsFinishedAccountAttentionGrab() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		// accountUrl.add(new UrlPojo("http://www.weibo.com/imaruko", 3));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/1889377232", 3));
		accountUrl.add(new UrlPojo("http://www.weibo.com/1586672964", 3));
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

	public void testIsFinishedAccountGrab() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setAddress(localUrl
				+ "MbmServer_QQ_V6/services/TaskService?wsdl");
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		accountUrl.add(new UrlPojo("http://www.weibo.com/2518388321", 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321",
		// 0));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "003", 0));
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

		// List<ResultGrab> resultGrab = taskService
		// .isFinishedAccountGrab(accountUrl);
		List<ResultGrab> resultGrab = taskService
				.isFinishedAccountInfoGrab(accountUrl);

		for (ResultGrab result : resultGrab) {
			System.out.println("resule code" + result.getResult());
			System.out.println("resule info" + result.getUrlPojo());
		}
	}

	public void testRemoveAccountInfoGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress(remoteUrl+"MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setAddress(localUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321", 0));
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
		soapFactoryBean.setAddress(localUrl
				+ "MbmServer_QQ_V6/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl_53
		// + "MbmServer/services/TaskService?wsdl"); //
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		// accountUrl.add(new UrlPojo("http://t.qq.com/erliang20088", 2,
		// TaskLevel.C));

		accountUrl.add(new UrlPojo("http://t.qq.com/erliang20088", 2,
				TaskLevel.C));

		// accountUrl.add(new UrlPojo("http://t.qq.com/kaifulee", 2,
		// TaskLevel.C));
		// accountUrl.add(new UrlPojo("http://t.qq.com/kaifulee", 2,
		// TaskLevel.E));

		// accountUrl.add(new UrlPojo("http://t.qq.com/jczmdeveloper",
		// 2,TaskLevel.A));
		// accountUrl.add(new UrlPojo("http://t.qq.com/erliang20088", 2,
		// TaskLevel.A));
		// accountUrl.add(new UrlPojo("http://t.qq.com/jczmdeveloper", 2,
		// TaskLevel.A));
		// accountUrl.add(new UrlPojo("http://www.weibo.com/2518388321", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/weixiaoshuo", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/meitixiaolaba",
		// 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/newmediapd", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/xinhuashidian",
		// 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/masrb", 6));
		// accountUrl.add(new UrlPojo("http://media.weibo.com/rmrb", 6));
		//		
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
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 1));

		// String txt = IOUtil.readFile("D:/url2.txt");
		// String[] urlArray = txt.split("\n");
		// for (String url : urlArray) {
		// if (url.trim().length() > 0) {
		// accountUrl.add(new UrlPojo(url, 1,TaskLevel.C));
		// }
		// }

		Result result = taskService.addAccountInfoGrabTask(accountUrl);

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

		UrlPojo urlPojo = new UrlPojo(FormatOutput.urlPrefix + "2518388321", 3);
		Result result = taskService.addAccountAttentionGrabTask(urlPojo);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddAccountListAttentionGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(localUrl
				+ "MbmServer_QQ_V5/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625",
		// 3));
		// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321",
		// 3));
		accountUrl.add(new UrlPojo(FormatOutput.urlPrefix_QQ + "erliang20088",
				3));
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
		soapFactoryBean.setAddress(localUrl
				+ "MbmServer_QQ_V6/services/TaskService?wsdl");
		// soapFactoryBean.setAddress(remoteUrl
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<KeywordPojo> keywords = new ArrayList<KeywordPojo>();
		// keywords.add(new KeywordPojo("周克强", 0));
		// keywords.add(new KeywordPojo("刘翔假摔", 0));
		// keywords.add(new KeywordPojo("微笑局长", 0));
		keywords.add(new KeywordPojo("世界末日", 0));
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

		// keywords.add(new KeywordPojo("一", 0));
		// keywords.add(new KeywordPojo("二", 0));
		// keywords.add(new KeywordPojo("三", 0));
		// keywords.add(new KeywordPojo("四", 0));
		// keywords.add(new KeywordPojo("五", 0));
		// keywords.add(new KeywordPojo("六", 0));
		// keywords.add(new KeywordPojo("七", 0));
		// keywords.add(new KeywordPojo("八", 0));
		// keywords.add(new KeywordPojo("九", 0));
		// keywords.add(new KeywordPojo("十", 0));
		// keywords.add(new KeywordPojo("十一", 0));
		// keywords.add(new KeywordPojo("十二", 0));
		// keywords.add(new KeywordPojo("十三", 0));
		// keywords.add(new KeywordPojo("十四", 0));
		// keywords.add(new KeywordPojo("十五", 0));
		// keywords.add(new KeywordPojo("十六", 0));

		// keywords.add(new KeywordPojo("123", 0));
		// keywords.add(new KeywordPojo("搜狗", 0));
		// keywords.add(new KeywordPojo("微博", 0));
		// keywords.add(new KeywordPojo("天气", 0));
		Result result = taskService.addKeyWordsGrabTask(keywords);

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
		keywords.add(new KeywordPojo("刘德华 天天向上", 0));
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

	public void testFindAccountInfoList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl");
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer/services/SearchService?wsdl");
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		List<String> list = new ArrayList<String>();
		// http://weibo.com/u/1365323271 http://weibo.com/lengxiaohua
		// list.add("http://www.weibo.com/1249193625");
		// list.add("http://www.weibo.com/2518388321");
		list.add("http://www.weibo.com/2518388321");

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

	public void testFindContentInfoList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer_QQ_V5/services/SearchService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();

		List<String> urlList = new ArrayList<String>();
		//
		// urlList.add("http://www.weibo.com/1598304682");
		// // String url="http://www.weibo.com/2134671703";
		//
		// urlList.add("http://www.weibo.com/1008823111/");
		// urlList.add("http://www.weibo.com/1496887652/");
		// urlList.add("http://www.weibo.com/2134671703");
		// urlList.add("http://www.weibo.com/1765694800");
		// urlList.add("http://www.weibo.com/2518388321");
		urlList.add("http://t.qq.com/erliang20088");
		// String groupName = "zel";
		// List<String> keyList = null;
		// List<String> keyList = new ArrayList<String>();

		// keyList.add("整天努力");
		// keyList.add("");
		// String url="http://www.weibo.com/1182389073";
		List<SupportSort> sortList = new ArrayList<SupportSort>();
		sortList.add(new SupportSort(
				BlogRemoteDefine.SORT_NAME_INFO.SORT_ARG_NAME_GRABTIME,
				BlogRemoteDefine.SORT_TYPE.DESC));
		ResultPage<SearchContentResultPojo> sendPage = new ResultPage<SearchContentResultPojo>(
				500);
		sendPage = searchService.findContentInfoList(null, null, null, null,
				new Date(), null, null, sortList, sendPage);
		List<SearchContentResultPojo> pojos = sendPage.getResult();
		for (SearchContentResultPojo pojo : pojos) {
			System.out.print(pojo.getContent());
			System.out.println(pojo.getNickName());
		}
	}

	public void testFindAccountAttentionList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/SearchService?wsdl");
		// //
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();
		// List<String> urlList=new ArrayList<String>();
		// urlList.add("http://www.weibo.com/1598304682");
		// String url = "http://www.weibo.com/2231283485";
		String url = "http://www.weibo.com/2518388321";

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
				15);
		sendPage = searchService.findAccountAttentionList(url, sendPage);

		List<SearchAccountResultPojo> pojos = sendPage.getResult();
		for (SearchAccountResultPojo pojo : pojos) {
			System.out.println(pojo.getNickName());
		}
		// System.out.println(resultPage.getTotalCount());
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
		// uidList.add("1400954293");
		// uidList.add("1709546550");
		// uidList.add("1619058262");
		// uidList.add("1282282193");

		// List<String> urlList=new ArrayList<String>();
		// urlList.add("http://www.weibo.com/1598304682");
		// String url = "http://www.weibo.com/1671asdf42103";
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
				15);
		sendPage = searchService.findAccountInfoListByUid(uidList, sendPage);
		List<SearchAccountResultPojo> pojos = sendPage.getResult();
		for (SearchAccountResultPojo pojo : pojos) {
			System.out.println(pojo.getNickName());
		}
	}

	public void testFindPersonFeatureWordsBySendUrl() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
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
		ResultPage<FeatureWord> wordsList = searchService
				.findPersonFeatureWordsBySendUrl(
						"http://www.weibo.com/2518388321", 0);
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

	public void testInsertNewWord() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean
				.setAddress("http://124.238.192.8/MbmServer/services/TaskService?wsdl"); //
		// soapFactoryBean
		// .setAddress("http://localhost:9999/MbmServer/services/TaskService?wsdl");
		// //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		// Result result=taskService.addNewWordTask("微博");
		Result result = taskService.addNewWordTask("苏宁易购");
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
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl");
		// soapFactoryBean
		// .setAddress("http://localhost:8080/MbmServer/services/SearchService?wsdl");
		soapFactoryBean.setServiceClass(ISearchService.class);
		ISearchService taskService = (ISearchService) soapFactoryBean.create();

		String content = "你是我的玫瑰花,毛主席是我们的大恩人,大恩人，思念,也许这才是真正的爱性，希望每个人都能快快乐乐,现在的房价真的没法搞了，太贵了太贵了";

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
