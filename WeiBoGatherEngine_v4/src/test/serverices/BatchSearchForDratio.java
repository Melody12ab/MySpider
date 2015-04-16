package test.serverices;

import java.io.BufferedReader;
import java.io.FileReader;
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

public class BatchSearchForDratio {
	/**
	 * 定义远程接口URL
	 */
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
				BlogRemoteDefine.SORT_TYPE.DESC));
		ResultPage<SearchContentResultPojo> sendPage = new ResultPage<SearchContentResultPojo>(// 初始化存放返回微博数据的集合page
				15);
		LinkedList<String> req_Url_List = new LinkedList<String>();
		List<SearchContentResultPojo> response_list = null;// 定义接收数据集合的集合
		Date beginPublishDate = null;
		Date endPublishDate = null;
		while ((temp_url = urlList.getLast()) != null) {// 此时说明队列中依然有未采集的URL
			beginPublishDate = new Date(new Date().getTime() - 365*4* 24 * 60
					* 60 * 1000);
			endPublishDate = new Date();
			req_Url_List.add(temp_url);
			sendPage = searchService.findNewContentInfoList(req_Url_List, null,
					beginPublishDate, endPublishDate, null, null, sortList,
					sendPage);// 首先进行一次请求
			response_list = sendPage.getResult();
			// 做对response_list的保存，此时是首页
			// save(response_list);
			// sendPage.getResult().clear();//清空已有的数据集，减小请求负担
			System.out.println("first response_list---" + response_list);
			Thread.sleep(1000);// 为了良好的交互，最好sleep一下，如1s,或更多，保证有请求间隔
			for (int page_number = 2; page_number <= sendPage.getTotalPages(); page_number++) {
				sendPage.setPageNo(page_number);
				sendPage = searchService.findNewContentInfoList(req_Url_List,
						null, beginPublishDate, endPublishDate, null, null,
						sortList, sendPage);// 首先进行一次请求
				response_list = sendPage.getResult();
				System.out
						.println("not first response_list---" + response_list);
				// 做对response_list的保存，此时是首页
				// save(response_list);
				// sendPage.getResult().clear();//清空已有的数据集，减小请求负担
			}
		//	req_Url_List.remove(temp_url);
			// 到此一个URL的所有的对应搜索数据已取到，如此反复即可全部获得。
		}
	}
}
