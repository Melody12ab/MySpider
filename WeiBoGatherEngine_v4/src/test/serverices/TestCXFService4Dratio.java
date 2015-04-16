package test.serverices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.service.dratio.ISearchService4Dratio;
import com.dw.party.mbmsupport.service.mbm.ISystemOperatorService;
import com.dw.party.mbmsupport.service.mbm.ITaskService;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.entity.util.UrlPojo;

public class TestCXFService4Dratio extends TestCase {
	public static String localUrl = "http://localhost:8080/";
	// public static String remoteUrl = "http://124.238.192.8:9999/";
	public static String remoteUrl = "http://124.238.192.8:8888/";

	public void testStartSpiderControler() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer_V2/services/SystemOperatorService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SystemOperatorService?wsdl");
		soapFactoryBean.setServiceClass(ISystemOperatorService.class);
		ISystemOperatorService systemService = (ISystemOperatorService) soapFactoryBean
				.create();

		Result result = systemService.start();

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testFindTest() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress(localUrl+"MbmServer_V2/services/SearchService4Dratio?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService4Dratio?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService4Dratio.class);
		ISearchService4Dratio searchService = (ISearchService4Dratio) soapFactoryBean
				.create();
		searchService.test();
	}

	public void testAddMediaAccountInfoGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();
		List<String> list = new ArrayList<String>();
		list.add("xinanwanbao");
		list.add("huanqiushibaoguanwei");
		list.add("beijingwanbao");
		list.add("fawan");
		list.add("bjyouth");
		list.add("bjcb");
		list.add("cqtime");
		list.add("cqsb");
		list.add("dnkb");
		list.add("haidu");
		list.add("sunnews");
		list.add("timeweekly");
		list.add("hbqnb");
		list.add("weixiaoshuo");
		list.add("meitixiaolaba");
		list.add("newmediapd");
		list.add("xinhuashidian");
		list.add("masrb");
		list.add("rmrb");
		list.add("rbnews");
		list.add("cqsb");
		list.add("nfrb");
		list.add("milnews");
		list.add("2308711425");
		list.add("dfwb");
		list.add("wemag");
		list.add("cnweek");
		list.add("gzdaily");
		list.add("rbnews");
		list.add("tvcn");
		list.add("bjyouth");
		list.add("1807689562");
		list.add("sdtv");
		list.add("jstvjstv");
		list.add("chinareform");
		list.add("cctvcaijing");
		list.add("haidu");
		list.add("cqsb");
		list.add("dnkb");
		list.add("tetimes");
		list.add("zjsb");
		list.add("shenzheneveningnews");
		list.add("ngzb2010");
		list.add("1777317934");
		list.add("xwbtsjb");
		list.add("pywb2681111");
		list.add("whwbwb");
		list.add("ctdsw");
		list.add("changjiangtimes");
		list.add("hbjmwb");
		list.add("2392759000");
		list.add("mdjcbgf");
		list.add("gzdaily");
		list.add("311201777");
		list.add("szrb");
		list.add("chifengdaily");
		list.add("hubeiribao");
		list.add("tlrb");
		list.add("dongfangweishifqt");
		list.add("gdzjrb");
		list.add("fsrb001");
		list.add("sxdsb");
		list.add("hncswb");
		list.add("kllrw");
		list.add("baotouwanbao");
		list.add("hswbgf");
		list.add("2294220693");
		list.add("xxrbs");
		list.add("jjcb8986060");
		list.add("xinwenhuabao");
		list.add("ccwbs");
		list.add("lswbwb");
		list.add("bandaochenbao");
		list.add("huashangchenbao");
		list.add("dlwb");
		list.add("bfcb");
		list.add("styleweekly");
		list.add("nxxxxb");
		list.add("qlwb");
		list.add("qdwb");
		list.add("tzsjb");
		list.add("youngdaily");
		list.add("shmorningpost");
		list.add("xwwbwb");
		list.add("cdwbwb");
		list.add("chengdunvbao");
		list.add("hxdsb");
		list.add("cdsb");
		list.add("27509527");
		list.add("holiday100tj");
		list.add("xzsb2010");
		list.add("wb96577");
		list.add("xjdsbwb");
		list.add("1615980787");
		list.add("ccwb");
		list.add("nanhuwanbao");
		list.add("wzwbwzwb");
		list.add("nbwb0574");
		list.add("qjwb");
		list.add("hzxingbao");
		list.add("dnsb");
		list.add("xinbao8lou");
		list.add("huashangbao");
		list.add("xytoday");
		list.add("dangdainvbao");
		list.add("nsj168");
		list.add("thechinapress");
		list.add("euchinese");
		list.add("sunpost");
		list.add("dyjm");
		list.add("xwwbwb");
		list.add("chinabusinessjournal");
		list.add("lanqiubao");
		list.add("chinatimes001");
		list.add("zgqybnews");
		list.add("cwnm");
		list.add("ly1798");
		list.add("nfncb");
		list.add("xinzhoubao");
		list.add("dxscgb");
		list.add("dazhongzq");
		list.add("njbyjssb");
		list.add("2516866423");
		list.add("mrjjxw");
		list.add("laodongbao");
		list.add("diyicaijing");
		list.add("shekebao");
		list.add("lsweek");
		list.add("2097319971");
		list.add("2187584504");
		list.add("unitedtimes");
		list.add("juecezazhi");
		list.add("chinanewsweek");
		list.add("liaowangzhoukan");
		list.add("lifeweek");
		list.add("wsckzz");
		list.add("bqweekly");
		list.add("cnweek");
		list.add("jingpinyuedu");
		list.add("globe");
		list.add("nbweekly");
		list.add("cctcbb");
		list.add("winningv");
		list.add("jllw888");
		list.add("lwdfzk");
		list.add("popscichina");
		list.add("ceociomagazine");
		list.add("mc1981");
		list.add("cfstory");
		list.add("ahtv01");
		list.add("ahtv520");
		list.add("ahtvdyls");
		list.add("ahcjxwc");
		list.add("hftvnews");
		list.add("2535760180");
		list.add("cctv1dianshiju");
		list.add("cctvzqt");
		list.add("mingrentang");
		list.add("tctc");
		list.add("cctv15music");
		list.add("xingguang24");
		list.add("cctvdrama");
		list.add("btvbeijing");
		list.add("cntv");
		list.add("cctvjunshijishi");
		list.add("yanglanfangtanlu");
		list.add("btvhld9");
		list.add("zixunkuaiche");
		list.add("cjfw");
		list.add("shuxiangbeijing");
		list.add("feichangdamingren");
		list.add("cctvyexian");
		list.add("2230851664");
		list.add("radio988");
		list.add("beijingjoyfm");
		list.add("cnrzgzs");
		list.add("bjxwgb");
		list.add("zgxczs");
		list.add("crinews");
		list.add("cnr2");
		list.add("radio774");
		list.add("crionline");
		list.add("fm1073");
		list.add("cnr");
		list.add("guofangshikong");
		list.add("zwhq");
		list.add("hakkatw");
		list.add("cqfm955");
		list.add("gsfm1035");
		list.add("cqnewsradio");
		list.add("1709712454");
		list.add("xmfm107");
		list.add("1980070714");
		list.add("cj961");
		list.add("fm1036");
		list.add("1744361825");
		list.add("fm953");
		list.add("zsradio");
		list.add("jmdt");
		list.add("fm951");
		list.add("gxjtt");
		list.add("gxnewsradio910");
		list.add("ylgb978");
		list.add("gzxwgb");
		list.add("2356266070");
		list.add("1985753051");
		list.add("betterlife1038");
		list.add("1750353914");
		list.add("1970150825");
		list.add("hbxwgb");
		list.add("jsnhzs");
		list.add("2197856532");
		list.add("radioyoung");
		list.add("lyxwgb");
		list.add("fanlulu617350829");
		list.add("hnrmgbdt");
		list.add("1931704477");
		list.add("927ssgx");
		list.add("hubeizhisheng");
		list.add("jjgb998");
		list.add("ctbs1179");
		list.add("2218362180");
		list.add("2255162034");
		list.add("financeradio1006");
		list.add("1967023650");
		list.add("jyzs955");
		list.add("chinafm918");
		list.add("xiangtanfm986");
		list.add("radiofm975");
		list.add("changshafm1017");
		list.add("yiyangfm997");
		list.add("dushi105");
		for (String temp : list) {
			accountUrl.add(new UrlPojo("http://media.weibo.com/" + temp, 6));
		}
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

		Result result = taskService.addAccountInfoGrabTask(accountUrl);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddEnterpriseAccountInfoGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();
		List<String> list = new ArrayList<String>();
		// list.add("2001627641");
//		list.add("pingan");
		list.add("2792064314");
		list.add("caijing");
		list.add("cntvsports");
		list.add("cntvzghr");
		list.add("ycwbjinyangwang");
		
		list.add("hebnews2012");
		list.add("northnews");
		list.add("secutimes");
		
		// list.add("sohochina");
		// list.add("cztv88");
		for (String temp : list) {
			accountUrl.add(new UrlPojo("http://e.weibo.com/" + temp, 11));
		}
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

		Result result = taskService.addAccountInfoGrabTask(accountUrl);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddProfessionAccountInfoGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean.setAddress(localUrl
		// + "MbmServer/services/TaskService?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();
		List<String> list = new ArrayList<String>();

		// list.add("1797507374");
		// list.add("afpinternationalnews");
		// list.add("xinhuaun");
		// list.add("hkcna");

		list.add("2531580357");
		list.add("ifengdiscover");
		list.add("globaltimes");
		list.add("ifengastrology");
		list.add("ycwbjinyangwang");
		list.add("sxxw");

		for (String temp : list) {
			accountUrl.add(new UrlPojo("http://e.weibo.com/" + temp, 15));
		}
		Result result = taskService.addAccountInfoGrabTask(accountUrl);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddEnterAccountGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();
		List<String> list = new ArrayList<String>();
		// list.add("cztv88");
		// list.add("2375600747");
		// list.add("10610010");
		list.add("2001627641");

		for (String temp : list) {
			// accountUrl.add(new UrlPojo("http://e.weibo.com/" + temp,10));
			accountUrl.add(new UrlPojo("http://e.weibo.com/" + temp, 10));
		}
		Result result = taskService.addAccountGrabTask(accountUrl);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testAddProfessionAccountGrabTask() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/TaskService?wsdl"); //
		soapFactoryBean.setServiceClass(ITaskService.class);
		ITaskService taskService = (ITaskService) soapFactoryBean.create();

		List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();
		List<String> list = new ArrayList<String>();

		list.add("1797507374");

		for (String temp : list) {
			accountUrl.add(new UrlPojo("http://e.weibo.com/" + temp, 14));
		}
		Result result = taskService.addAccountGrabTask(accountUrl);

		System.out.println("resule code" + result.getResultCode());
		System.out.println("resule info" + result.getInfo());
	}

	public void testFindContentInfoList() {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		// soapFactoryBean
		// .setAddress(localUrl+"MbmServer_V2/services/SearchService4Dratio?wsdl");
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService4Dratio?wsdl"); //
		soapFactoryBean.setServiceClass(ISearchService4Dratio.class);
		ISearchService4Dratio searchService = (ISearchService4Dratio) soapFactoryBean
				.create();

		// ResultPage<SearchContentResultPojo> sendPage = new
		// ResultPage<SearchContentResultPojo>(
		// 15);

		MyJson myJSON = new MyJson();
		Date date = new Date();
		// date.setHours(hours)
		myJSON.put("beginTime", date.getTime() - 1000 * 60 * 60 * 24 * 2);
		// myJSON.put("endTime", new Date().getTime());
		myJSON.put("keyWord", "十八大");

		String json = searchService.findContentInfoList(new String[] { myJSON
				.toString() });

		System.out.println("json--------" + json);

		// List<SearchContentResultPojo> pojos = sendPage.getResult();
		// for (SearchContentResultPojo pojo : pojos) {
		// System.out.println(pojo.getNickName());
		// }
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
