package test.others;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.dw.party.mbmsupport.dto.SupportSort;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.dw.party.mbmsupport.service.mbm.ISearchService;
import com.weibo.common.utils.ResultPage;

public class BolgContentInfo {
	public static String remoteUrl = "http://124.238.192.8:8888/";

	public static void main(String[] args) throws Exception {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();// 初始化接口调用工具类
		soapFactoryBean.setAddress(remoteUrl
				+ "MbmServer/services/SearchService?wsdl"); // 绑定services接口
		soapFactoryBean.setServiceClass(ISearchService.class);// 接口本地化
		ISearchService searchService = (ISearchService) soapFactoryBean
				.create();// 真正的创建service连接
		/**
		 * 通过本地文件，将6000 URL加载到一个LinkedList当中---开始
		 */
		LinkedList<String> urlList = new LinkedList<String>();// 首先实例化一个队列，存放加载进的URL
		// FileReader fr = new FileReader("URL地址的文件路径");
		FileReader fr = new FileReader("D://dratio.txt");
		BufferedReader br = new BufferedReader(fr);

		String temp_url = null;

		while ((temp_url = br.readLine()) != null) {
			if (temp_url.trim().length() > 0) {
				urlList.add(temp_url);
			}
		}
		br.close();// 关闭流
		fr.close();
		/**
		 * 通过本地文件，将6000 URL加载到一个LinkedList当中 ---结束
		 */
		List<SupportSort> sortList = new ArrayList<SupportSort>();// 实例化一个排序集合，此为按发布时间降序
		sortList.add(new SupportSort(
				BlogRemoteDefine.SORT_NAME_INFO.SORT_ARG_NAME_PUBLISHTIME,
				BlogRemoteDefine.SORT_TYPE.ASC));
		ResultPage<SearchContentResultPojo> sendPage = new ResultPage<SearchContentResultPojo>(// 初始化存放返回微博数据的集合page
				20);
		LinkedList<String> req_Url_List = new LinkedList<String>();
		List<SearchContentResultPojo> response_list = null;// 定义接收数据集合的集合
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("D:/201317"), "utf-8"));
		String str = "用户名" + "," + "发布时间" + "," + "微博内容" + "," + "转发数目" + ","
				+ "评论次数"+ ","+"Uid"+ ","+"Url"+ ","+"抓取时间";
		out.write(str);
		out.newLine();
		//取一段时间内的微博，beginPublishDate为开始时间，endPublishDate为结束时间
		Date beginPublishDate = new Date(113, 0, 7, 0, 0, 0);
		Date endPublishDate = new Date();
		BufferedWriter ex = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("D:/work201317.txt"), "utf-8"));//记录程序运行过程中的日志
		ex.write(new Date().toLocaleString() + "开始运行程序……");
		long start = System.currentTimeMillis();
		ex.newLine();
		while ((temp_url = urlList.pollFirst()) != null) {// 此时说明队列中依然有未采集的URL
			ex.write(new Date().toLocaleString() + "开始取" + temp_url + "数据");
			ex.newLine();
			ex.flush();
			req_Url_List.add(temp_url);
			try {
				sendPage.setPageNo(1);
				sendPage = searchService.findNewContentInfoList(req_Url_List,
						null, beginPublishDate, endPublishDate, null, null,
						sortList, sendPage);// 首先进行一次请求
			} catch (Exception e) {
				ex.write(new Date().toLocaleString());
				ex.newLine();
				ex.write(temp_url);
				ex.newLine();
				ex.write(e.toString());
				ex.newLine();
				ex.flush();
				e.printStackTrace();
			}
			response_list = sendPage.getResult();
			// 做对response_list的保存，此时是首页
			// save(response_list);
			if (response_list.size() == 0) {
				ex.write(temp_url + "的微博数为0");
				ex.newLine();
				ex.flush();
			} else {
				ex.write(temp_url + "的微博数为" + sendPage.getTotalCount());
				ex.newLine();
				ex.flush();
			}
			System.out.println(response_list.size());
			System.out.println(response_list);
			for (SearchContentResultPojo pojo1 : response_list) {
				String st1 = pojo1.getNickName() + "," + pojo1.getPublishTime()
						+ "," + pojo1.getContent().replaceAll(",", "，").replaceAll("[\n\r]","") + ","
						+ pojo1.getConvertNum() + "," + pojo1.getCommentNum()+ ","+pojo1.getUid()+ ","+pojo1.getUrl()+ ","+pojo1.getGrabTime();
				
				out.write(st1);
				out.newLine();
				out.flush();
			}
			sendPage.getResult().clear();// 清空已有的数据集，减小请求负担
			// System.out.println("first response_list---"+response_list);
			Thread.sleep(1500);// 为了良好的交互，最好sleep一下，如1s,或更多，保证有请求间隔
			for (int page_number = 2; page_number <= sendPage.getTotalPages(); page_number++) {
				sendPage.setPageNo(page_number);
				try {
					sendPage = searchService.findNewContentInfoList(
							req_Url_List, null, beginPublishDate,
							endPublishDate, null, null, sortList, sendPage);// 首先进行一次请求
				} catch (Exception e) {
					ex.write(new Date().toLocaleString());
					ex.newLine();
					ex.write(temp_url);
					ex.newLine();
					ex.write(e.toString());
					ex.newLine();
					ex.write(e.getMessage());
					ex.newLine();
					ex.flush();
					e.printStackTrace();
				}
				response_list = sendPage.getResult();
				// System.out.println("not first response_list---"+response_list);
				// 做对response_list的保存，此时是首页
				// save(response_list);
				for (SearchContentResultPojo pojo : response_list) {
					String st = pojo.getNickName() + ","
							+ pojo.getPublishTime() + ","
							+ pojo.getContent().replaceAll(",", "，").replaceAll("[\n\r]","") + ","
							+ pojo.getConvertNum() + "," + pojo.getCommentNum()+ ","+pojo.getUid()+ ","+pojo.getUrl()+ ","+pojo.getGrabTime();
					out.write(st);
					out.newLine();
					out.flush();
				}
				sendPage.getResult().clear();// 清空已有的数据集，减小请求负担
			}
			req_Url_List.remove(temp_url);
			ex.write(new Date().toLocaleString() + "取" + temp_url + "的微博结束");
			ex.newLine();
			// 到此一个URL的所有的对应搜索数据已取到，如此反复即可全部获得。
		}
		long end = System.currentTimeMillis();
		ex.write(new Date().toLocaleString() + "程序运行结束" + "共用时" + (end - start)
				/ 1000 + "秒");
		out.close();

		ex.close();
	}
}
