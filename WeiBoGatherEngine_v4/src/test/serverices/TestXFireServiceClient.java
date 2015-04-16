package test.serverices;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
//import org.codehaus.xfire.XFireFactory;
//import org.codehaus.xfire.client.XFireProxyFactory;
//import org.codehaus.xfire.service.Service;
//import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.dto.ResultGrab;
import com.dw.party.mbmsupport.service.mbm.IGroupingService;
import com.dw.party.mbmsupport.service.mbm.ISystemOperatorService;
import com.dw.party.mbmsupport.service.mbm.ITaskService;
import com.mbm.elint.entity.util.UrlPojo;
import com.weibo.common.utils.FormatOutput;

public class TestXFireServiceClient extends TestCase {
	/*public void testAddGroup() throws Exception {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(IGroupingService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/GroupingService";
		try {
			// 创建服务对象
			IGroupingService groupService = (IGroupingService) factory.create(
					srvcModel, url);

			Result result = groupService.addGroup("人气聚焦");

			System.out.println("code--------------" + result.getResultCode());
			System.out.println("info--------------" + result.getInfo());
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	// 删除分组
	public void testRemoveGroup() throws Exception {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(IGroupingService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/GroupingService";
		try {
			// 创建服务对象
			IGroupingService groupService = (IGroupingService) factory.create(
					srvcModel, url);

			Result result = groupService.removeGroup("房产大老");

			System.out.println("code--------------" + result.getResultCode());
			System.out.println("info--------------" + result.getInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 向已定义组中添加批量帐户
	public void testAddAccountUrlsToGroup() throws Exception {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(IGroupingService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/GroupingService";
		try {
			// 创建服务对象
			IGroupingService groupService = (IGroupingService) factory.create(
					srvcModel, url);

			List<String> urls = new ArrayList<String>();
			urls.add("one");
			urls.add("two");
			urls.add("five");
			Result result = groupService.addAccountToGroup("房产大老", urls);

			System.out.println("code--------------" + result.getResultCode());
			System.out.println("info--------------" + result.getInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 批量删除组中的帐户列表
	public void testRemoveAccountUrlsFromGroup() throws Exception {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(IGroupingService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/GroupingService";
		try {
			// 创建服务对象
			IGroupingService groupService = (IGroupingService) factory.create(
					srvcModel, url);

			List<String> urls = new ArrayList<String>();
			urls.add("one");
			urls.add("two");
			urls.add("five");
			Result result = groupService.removeAccountFromGroup("房产大老", urls);

			System.out.println("code--------------" + result.getResultCode());
			System.out.println("info--------------" + result.getInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testStartSpiderControler() {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(ISystemOperatorService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/SystemOperatorService";
		try {
			// 创建服务对象
			ISystemOperatorService systemService = (ISystemOperatorService) factory
					.create(srvcModel, url);
			Result result = systemService.start();

			System.out.println("code--------------" + result.getResultCode());
			System.out.println("info--------------" + result.getInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testAddAccountGrabTask() {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(ITaskService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/TaskService";
		try {
			// 创建服务对象
			ITaskService systemService = (ITaskService) factory.create(
					srvcModel, url);

			List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

			accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "001", 0));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "2134671703", 0));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625", 0));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103", 0));
			accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功", 1));
			accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 1));

			Result result = systemService.addAccountGrabTask(accountUrl);

			System.out.println("code--------------" + result.getResultCode());
			System.out.println("info--------------" + result.getInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testIsFinishedAccountGrab() {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(ITaskService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/TaskService";
		try {
			// 创建服务对象
			ITaskService systemService = (ITaskService) factory.create(
					srvcModel, url);

			List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

			accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "001", 0));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "2134671703", 0));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "1691954411", 0));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103", 0));
			accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功", 1));
			accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 1));

			List<ResultGrab> resultGrabList = systemService
					.isFinishedAccountGrab(accountUrl);
			System.out.println(resultGrabList.size());
			for (ResultGrab result : resultGrabList) {
				System.out.println("url--" + result.getUrlPojo() + "  ："
						+ result.getResult());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testRemoveAccountInfoGrabTask() {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(ITaskService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/TaskService";
		try {
			// 创建服务对象
			ITaskService taskService = (ITaskService) factory.create(srvcModel,
					url);

			List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

			accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "001", 0));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "2134671703", 0));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625", 0));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103", 0));
			accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功", 1));
			accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "成功2", 1));

			List<Result> resultList = taskService
					.removeAccountInfoGrabTask(accountUrl);

			for (Result result : resultList) {
				System.out.println("code--------------"
						+ result.getResultCode());
				System.out.println("info--------------" + result.getInfo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testAddAccountInfoGrabTask() {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(ITaskService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/TaskService";
		try {
			// 创建服务对象
			ITaskService taskService = (ITaskService) factory.create(srvcModel,
					url);

			List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

			// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "001", 0));
			// accountUrl
			// .add(new UrlPojo(FormatOutput.urlPrefix + "2134671703", 0));
			// 2518388321
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103", 1));
			accountUrl
					.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321", 1));
			// accountUrl
			// .add(new UrlPojo(FormatOutput.urlPrefix + "1249193625", 0));
			// accountUrl
			// .add(new UrlPojo(FormatOutput.urlPrefix + "1671342103", 0));

			Result result = taskService.addAccountInfoGrabTask(accountUrl);

			System.out.println("code--------------" + result.getResultCode());
			System.out.println("info--------------" + result.getInfo());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testAddAccountAttentionGrabTask() {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(ITaskService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/TaskService";
		try {
			// 创建服务对象
			ITaskService taskService = (ITaskService) factory.create(srvcModel,
					url);
			// List<UrlPojo> accountUrl = new ArrayList<UrlPojo>();

			// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "001", 0));
			// accountUrl
			// .add(new UrlPojo(FormatOutput.urlPrefix + "2134671703", 0));
			// 2518388321
			// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103",
			// 3));
			// accountUrl.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321",
			// 3));
			// accountUrl
			// .add(new UrlPojo(FormatOutput.urlPrefix + "1249193625", 0));
			// accountUrl
			// .add(new UrlPojo(FormatOutput.urlPrefix + "1671342103", 0));
			// 2518388321,1697717391,1802982701,1691954411
			UrlPojo urlPojo = new UrlPojo(
					FormatOutput.urlPrefix + "1802982701", 0);
			Result result = taskService.addAccountAttentionGrabTask(urlPojo);

			System.out.println("code--------------" + result.getResultCode());
			System.out.println("info--------------" + result.getInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// 通过接口类创建Service对象
		Service srvcModel = new ObjectServiceFactory()
				.create(ITaskService.class);
		// 通过XFire的工厂类创建工厂对象
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		// 访问的地址
		String url = "http://localhost:8080/MbmServer/services/TaskService";
		try {
			// 创建服务对象
			ITaskService taskService = (ITaskService) factory.create(srvcModel,
					url);
			List<UrlPojo> accountUrls = new ArrayList<UrlPojo>();

//			accountUrls.add(new UrlPojo(FormatOutput.urlPrefix + "001", 3));
//			accountUrls.add(new UrlPojo(FormatOutput.urlPrefix + "2134671703",
//					3));	
//			accountUrls.add(new UrlPojo(FormatOutput.urlPrefix + "2097024354",3));
			accountUrls.add(new UrlPojo(FormatOutput.urlPrefix + "2518388321",3));
//			accountUrls.add(new UrlPojo(FormatOutput.urlPrefix + "1249193625",3));
//			accountUrls.add(new UrlPojo(FormatOutput.urlPrefix + "1671342103",3));
			// 2518388321,1697717391,1802982701,1691954411
			Result result = taskService
					.addAccountListAttentionGrabTask(accountUrls);

			System.out.println("code--------------" + result.getResultCode());
			System.out.println("info--------------" + result.getInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
}
