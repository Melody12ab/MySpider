package com.mbm.elint.manager.business;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.util.DateUtil;
import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.StaticValue;

@Component
public class CircleTaskUrlManager {
	public static Logger logger = Logger.getLogger(CircleTaskUrlManager.class);

	public static HashSet<UrlPojo> circleUrls = null;

	static {
		// 初始化circleUrls
		Object temp_obj = null;
		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(StaticValue.circle_urls_cache_file_path,
								StaticValue.charset)));
		// 初始化visitedAttentionUrls Set
		if (circleUrls == null && (temp_obj == null)) {
			circleUrls = new HashSet<UrlPojo>();
			logger.info("circle_urls_cache_file_path初始化完成,此时是空列表!");
		} else {
			try {
				circleUrls = (HashSet) temp_obj;
				logger.info("redis--成功加载circleUrls列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载circleUrls重新初始化完成");
			}
		}
	}

	public void addUrlPojo(UrlPojo pojo) {
		synchronized (circleUrls) {
			circleUrls.add(pojo);
		}
	}

	int type = 0;

	public void addUrlPojoToCircle(UrlPojo pojo) {
		synchronized (circleUrls) {
			/**
			 * 在这里加一层是否重复的判断
			 */
			if (circleUrls.contains(pojo)) {
				return;
			}
			/**
			 * 这里对它做reGrab时间的设置
			 */
			type = pojo.getType();
			if (pojo.getType() == 7) {
				// 重复一下，8代表周期抓取微言信息
				this.addUrlPojo(new UrlPojo(pojo.getUrl(), 8, DateUtil
						.getLongByDate()
						+ DateUtil.getTimeByLevel(pojo.getTaskLevel()), pojo
						.getTaskLevel()));
				// 重复一下，9代表周期抓取帐号信息
				this.addUrlPojo(new UrlPojo(pojo.getUrl(), 9, DateUtil
						.getLongByDate()
						+ DateUtil.getTimeByLevel(pojo.getTaskLevel()), pojo
						.getTaskLevel()));
			} else if (pojo.getType() == 11) {
				// 重复一下，11代表周期抓取企业版帐号信息
				this.addUrlPojo(new UrlPojo(pojo.getUrl(), 12, DateUtil
						.getLongByDate()
						+ DateUtil.getTimeByLevel(pojo.getTaskLevel()), pojo
						.getTaskLevel()));
				// 重复一下，9代表周期抓取企业版微言信息
				this.addUrlPojo(new UrlPojo(pojo.getUrl(), 13, DateUtil
						.getLongByDate()
						+ DateUtil.getTimeByLevel(pojo.getTaskLevel()), pojo
						.getTaskLevel()));
			} else if (pojo.getType() == 15) {
				// 重复一下，16代表周期抓取专业版帐号信息
				this.addUrlPojo(new UrlPojo(pojo.getUrl(), 16, DateUtil
						.getLongByDate()
						+ DateUtil.getTimeByLevel(pojo.getTaskLevel()), pojo
						.getTaskLevel()));
				// 重复一下，17代表周期抓取专业版微言信息
				this.addUrlPojo(new UrlPojo(pojo.getUrl(), 17, DateUtil
						.getLongByDate()
						+ DateUtil.getTimeByLevel(pojo.getTaskLevel()), pojo
						.getTaskLevel()));
			} else if (pojo.getType() == 2) {
				{
					// 重复一下，5代表周期抓取微言信息
					this.addUrlPojo(new UrlPojo(pojo.getUrl(), 4, DateUtil
							.getLongByDate()
							+ DateUtil.getTimeByLevel(pojo.getTaskLevel()),
							pojo.getTaskLevel()));
					// 重复一下，4代表周期抓取帐号信息
					this.addUrlPojo(new UrlPojo(pojo.getUrl(), 5, DateUtil
							.getLongByDate()
							+ DateUtil.getTimeByLevel(pojo.getTaskLevel()),
							pojo.getTaskLevel()));
				}
			} else if (pojo.getType() == 101) {
				{
					// 重复一下，18代表周期抓取评论列表的标志位
					this.addUrlPojo(new UrlPojo(pojo.getUrl(), 18, DateUtil
							.getLongByDate()
							+ DateUtil.getTimeByLevel(pojo.getTaskLevel()),
							pojo.getTaskLevel()));
				}
			} else if (pojo.getType() == 102) {
				{
					// 重复一下，18代表周期抓取评论列表的标志位
					this.addUrlPojo(new UrlPojo(pojo.getUrl(), 19, DateUtil
							.getLongByDate()
							+ DateUtil.getTimeByLevel(pojo.getTaskLevel()),
							pojo.getTaskLevel()));
				}
			} else if (pojo.getType() == 20) {
				{
					// 重复一下，21代表周期抓取评论列表的标志位
					this.addUrlPojo(new UrlPojo(pojo.getUrl(), 21, DateUtil
							.getLongByDate()
							+ DateUtil.getTimeByLevel(pojo.getTaskLevel()),
							pojo.getTaskLevel()));
				}
			}
		}
	}

	public static int getListSize() {
		return circleUrls.size();
	}

	public void removeUrlPojo(UrlPojo pojo) {
		synchronized (circleUrls) {
			circleUrls.remove(pojo);
		}
	}

	public static boolean removeToVisitCircleUrl(UrlPojo pojo) {
		synchronized (circleUrls) {
			/**
			 * 要将帐号和内容采集都要删除
			 */
			if (circleUrls.remove(new UrlPojo(pojo.getUrl(), 4))) {
				logger.info("删除周期任务----" + pojo + "---成功");
			} else {
				logger.info("删除周期任务----" + pojo + "---失败");
			}
			if (circleUrls.remove(new UrlPojo(pojo.getUrl(), 5))) {
				logger.info("删除周期任务----" + pojo + "---成功");
			} else {
				logger.info("删除周期任务----" + pojo + "---失败");
			}
		}
		return true;
	}

	public static void main(String[] args) {
		// UrlPojo url1 = new UrlPojo("one-http", 1);
		// UrlPojo url2 = new UrlPojo("two-http", 1);
		// UrlPojo url3 = new UrlPojo("three-http", 1);
		//
		// // LinkedList<UrlPojo> list1 = new LinkedList<UrlPojo>();
		// List<UrlPojo> list1 = new ArrayList<UrlPojo>();
		// list1.add(url1);
		// list1.add(url2);
		// list1.add(url3);
		//
		// // LinkedList<UrlPojo> list2 = new LinkedList<UrlPojo>();
		// List<UrlPojo> list2 = new ArrayList<UrlPojo>();
		// list2.addAll(list1);
		//
		// System.out.println("删除之前的list1.size=" + list1.size());
		// for (UrlPojo pojo : list1) {
		// System.out.println("----------" + pojo);
		// }
		// // list2.pollFirst();
		// list2.remove(0);
		// System.out.println("删除之前的list2.size=" + list2.size());
		// list2.clear();
		// System.out.println("删除之后的list1.size=" + list1.size());
		// for (UrlPojo pojo : list1) {
		// System.out.println("----------" + pojo);
		// }
		// System.out.println("删除之后的list1.size=" + list2.size());
	}
}
