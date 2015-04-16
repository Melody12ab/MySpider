package com.mbm.elint.manager.business.spider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bloomFilter.BloomFilter;

import com.mbm.elint.entity.util.TypePojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.CircleTaskUrlManager;
import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.FileOperatorUtil;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.StaticValue;

@Component
public class UrlsManager {
	public static Logger logger = Logger.getLogger(UrlsManager.class);

	public static LinkedList<UrlPojo> toVisitUrls = null;
	public static LinkedList<UrlPojo> toVisitUrls4QQ = new LinkedList<UrlPojo>();
	/**
	 * 改成ArrayList，易于查询
	 */
	public static ArrayList<UrlPojo> visitedUrls = new ArrayList<UrlPojo>();
	public static LinkedList<UrlPojo> errorUrls = new LinkedList<UrlPojo>();
	public static LinkedList<UrlPojo> visitingUrls = new LinkedList<UrlPojo>();
	public static long repeat_urls = 0;
	public static BloomFilter visitedUrlsBloomFilter = null;
	public static BloomFilter errorUrlsBloomFilter = null;
	public static long visitedUrls_urls_size = 0;
	public static long errorUrls_urls_size = 0;
	/**
	 * 初始化已抓取的关注,改成ArrayList，易于查询
	 */
	public static LinkedList<UrlPojo> toVisitAttentionUrls = new LinkedList<UrlPojo>();
	public static LinkedList<UrlPojo> toVisitAttentionUrls4QQ = new LinkedList<UrlPojo>();
	public static HashSet<UrlPojo> visitedAttentionUrls = null;
	/**
	 * 是对周期任务新增的变量
	 */
	public static LinkedList<UrlPojo> toVisitCircleUrls = new LinkedList<UrlPojo>();
	public static LinkedList<UrlPojo> toVisitCircleUrls4QQ = new LinkedList<UrlPojo>();

	/**
	 * 为评论列表的新增的集合变量
	 */
	public static LinkedList<UrlPojo> toVisitCommentUrls = null;

	/**
	 * 专为更新同步锁控制而添加---开始
	 */
	static final Lock toVisitUrlsLock = new ReentrantLock();
	static final Lock toVisitUrls4QQLock = new ReentrantLock();
	final Lock visitingUrlsLock = new ReentrantLock();
	final Lock visitedUrlsLock = new ReentrantLock();
	final Lock synObjLock = new ReentrantLock();// 最大的同步锁,防止添URL集合时出现死锁
	static final Lock errorUrlsLock = new ReentrantLock();
	static final Lock toVisitAttentionUrlsLock = new ReentrantLock();
	static final Lock toVisitAttentionUrls4QQLock = new ReentrantLock();
	static final Lock synObj4AttentionLock = new ReentrantLock();
	static final Lock visitedAttentionUrlsLock = new ReentrantLock();
	static final Lock toVisitCircleUrlsLock = new ReentrantLock();
	static final Lock toVisitCircleUrls4QQLock = new ReentrantLock();

	static final Lock toVisitCommentUrlsLock = new ReentrantLock();

	/**
	 * 专为更新同步锁控制而添加---结束
	 */

	/**
	 * 将周期任务管理放在这个位置，不在放在入口处，尽量周期任务的准确有效性
	 */
	@Autowired
	private CircleTaskUrlManager circleTaskUrlManager;

	// 初始化布隆过滤器
	static {
		/**
		 * 首先把根路径搞定
		 */
		if (FileOperatorUtil.createRootDir(StaticValue.root_path)) {
			logger.info("存储数据的根路径初始化完毕!");
		} else {
			logger.info("存储数据的根路径出错，请重新确认" + StaticValue.root_path + " 路径的存在!");
			System.exit(-1);
		}
		Object temp_obj = null;
		// 初始化visitedUrlsBloomFilter

		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(
								StaticValue.visitedUrls_bloomFilter_file_path,
								StaticValue.charset)));

		if (visitedUrlsBloomFilter == null && (temp_obj == null)) {
			visitedUrlsBloomFilter = new BloomFilter(
					StaticValue.visitedUrls_filter_size);
			logger.info("visitedUrlsBloom过滤器初始化完成");
		} else {
			try {
				visitedUrlsBloomFilter = (BloomFilter) temp_obj;
				logger.info("redis--成功加载visitedUrls过滤器!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载visitedUrls过滤器!");
			}
		}

		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(
								StaticValue.errorUrls_bloomFilter_file_path,
								StaticValue.charset)));
		// 初始化errorUrls_bloomFilter_file_path
		if (errorUrlsBloomFilter == null && (temp_obj == null)) {
			errorUrlsBloomFilter = new BloomFilter(
					StaticValue.errorUrls_filter_size);
			logger.info("errorUrlsBloom过滤器初始化完成");
		} else {
			try {
				errorUrlsBloomFilter = (BloomFilter) temp_obj;
				logger.info("redis--成功加载errorUrlsBloom过滤器!");
			} catch (Exception e) {
				try {
					errorUrlsBloomFilter = (BloomFilter) temp_obj;
					logger.info("成功加载errorUrlsBloom过滤器!--备份的");
				} catch (Exception e1) {
					e.printStackTrace();
					logger.info("****失败加载errorUrlsBloom过滤器!");
				}

			}
		}
		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(StaticValue.toVisitUrls_file_path,
								StaticValue.charset)));
		// 初始化toVisitUrls List
		if (toVisitUrls == null && (temp_obj == null)) {
			toVisitUrls = new LinkedList<UrlPojo>();
			logger.info("toVisitUrls初始化完成,此时是空列表!");
		} else {
			try {
				toVisitUrls = (LinkedList) temp_obj;
				logger.info("redis--成功加载toVisitUrls列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载toVisitUrls重新初始化完成");
			}
		}

		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(StaticValue.toVisitCommentUrls_file_path,
								StaticValue.charset)));
		// 初始化toVisitUrls List
		if (toVisitCommentUrls == null && (temp_obj == null)) {
			toVisitCommentUrls = new LinkedList<UrlPojo>();
			logger.info("toVisitCommentUrls初始化完成,此时是空列表!");
		} else {
			try {
				toVisitCommentUrls = (LinkedList) temp_obj;
				logger.info("redis--成功加载toVisitCommentUrls列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载toVisitCommentUrls重新初始化完成");
			}
		}

		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(
								StaticValue.visitedAttentionUrls_set_file_path,
								StaticValue.charset)));
		// 初始化visitedAttentionUrls Set
		if (visitedAttentionUrls == null && (temp_obj == null)) {
			visitedAttentionUrls = new HashSet<UrlPojo>();
			logger.info("visitedAttentionUrls初始化完成,此时是空列表!");
		} else {
			try {
				visitedAttentionUrls = (HashSet) temp_obj;
				logger.info("redis--成功加载visitedAttentionUrls列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载visitedAttentionUrls重新初始化完成");
			}
		}
	}

	private UrlPojo temp_url = null;

	// 得到一个toVisitUrls
	public UrlPojo getOneUrl() {
		toVisitUrlsLock.lock();
		try {
			temp_url = toVisitUrls.pollFirst();
			if (temp_url != null) {
				addVisitingUrls(temp_url);
			}
		} finally {
			toVisitUrlsLock.unlock();
		}
		return temp_url;
	}

	// 得到一个toVisitUrls
	public UrlPojo getOneUrl4QQ() {
		// synchronized (toVisitUrls4QQ) {
		// temp_url = toVisitUrls4QQ.pollFirst();
		// if (temp_url != null) {
		// addVisitingUrls(temp_url);
		// }
		// }
		toVisitUrls4QQLock.lock();
		try {
			temp_url = toVisitUrls4QQ.pollFirst();
			if (temp_url != null) {
				addVisitingUrls(temp_url);
			}
		} finally {
			toVisitUrls4QQLock.unlock();
		}
		return temp_url;
	}

	public boolean removeToVisitUrls(List<UrlPojo> urlPojos) {
		// synchronized (toVisitUrls) {
		// for (UrlPojo urlPojo : urlPojos) {
		// removeToVisitUrl(urlPojo);
		// }
		// }
		toVisitUrlsLock.lock();
		try {
			for (UrlPojo urlPojo : urlPojos) {
				removeToVisitUrl(urlPojo);
			}
		} finally {
			toVisitUrlsLock.unlock();
		}
		return true;
	}

	public boolean removeToVisitUrl(UrlPojo urlPojo) {
		toVisitUrlsLock.lock();
		try {
			if (toVisitUrls.remove(urlPojo)) {
				return true;
			}
		} finally {
			toVisitUrlsLock.unlock();
		}
		return false;
	}

	public void addVisitingUrls(UrlPojo url) {
		visitingUrlsLock.lock();
		try {
			visitingUrls.add(url);
		} finally {
			visitingUrlsLock.unlock();
		}
	}

	public void removeVisitingUrls(UrlPojo url) {
		visitingUrlsLock.lock();
		try {
			visitingUrls.remove(url);
		} finally {
			visitingUrlsLock.unlock();
		}
	}

	public void addVisitedUrls(UrlPojo url) {
		visitedUrlsLock.lock();
		try {
			removeVisitingUrls(url);
			visitedUrls.add(url);
			visitedUrls_urls_size++;
			visitedUrlsBloomFilter.add(url.toString());
			/**
			 * 在这个位置把循环任务加进去
			 */
			circleTaskUrlManager.addUrlPojoToCircle(url);
		} finally {
			visitedUrlsLock.unlock();
		}
	}

	// 随时加入新的urls集合,此时均为手动提交,故放置于队列的最前
	public void addUrlsListFirst(List<UrlPojo> newList) {
		synObjLock.lock();
		try {
			for (UrlPojo urlPojo : newList) {
				if (TypePojo.isQQ(urlPojo.getUrl())) {
					addUrlToFirst4QQ(urlPojo);
				} else {
					addUrlToFirst(urlPojo);
				}
			}
		} finally {
			synObjLock.unlock();
		}
	}

	// 随时加入新的urls集合,此时均为手动提交,故放置于队列的最前
	// 为添加评论列表docUrls集合
	public void addCommentUrlsListFirst(List<UrlPojo> docUrls) {
		synObjLock.lock();
		try {
			for (UrlPojo urlPojo : docUrls) {
				addCommentUrlToFirst(urlPojo);
			}
		} finally {
			synObjLock.unlock();
		}
	}

	// 加入一个url地址
	public static void addUrl(UrlPojo url) {
		// synchronized (toVisitUrls) {
		// toVisitUrls.add(url);
		// }
		toVisitUrlsLock.lock();
		try {
			toVisitUrls.add(url);
		} finally {
			toVisitUrlsLock.unlock();
		}
	}

	public static void addUrl4QQ(UrlPojo url) {
		// synchronized (toVisitUrls4QQ) {
		// toVisitUrls4QQ.add(url);
		// }
		toVisitUrls4QQLock.lock();
		try {
			toVisitUrls4QQ.add(url);
		} finally {
			toVisitUrls4QQLock.unlock();
		}
	}

	// 加入一个url地址至头部
	public static void addUrlToFirst(UrlPojo url) {
		toVisitUrlsLock.lock();
		try {
			toVisitUrls.addFirst(url);
		} finally {
			toVisitUrlsLock.unlock();
		}
	}

	// 加入一个url地址至头部
	public void addCommentUrlToFirst(UrlPojo urlPojo) {
		toVisitCommentUrlsLock.lock();
		try {
			toVisitCommentUrls.addFirst(urlPojo);
		} finally {
			toVisitCommentUrlsLock.unlock();
		}
	}

	// 加入一个url地址至头部
	public static void addUrlToFirst4QQ(UrlPojo url) {
		// synchronized (toVisitUrls4QQ) {
		// toVisitUrls4QQ.addFirst(url);
		// }
		toVisitUrls4QQLock.lock();
		try {
			toVisitUrls4QQ.addFirst(url);
		} finally {
			toVisitUrls4QQLock.unlock();
		}
	}

	// 加入一个由于没有到达指定的最大抓取次数,而被重新放入队列尾部抓取的url
	public void addUrlToTail(UrlPojo url) {
		// synchronized (toVisitUrls) {
		// removeVisitingUrls(url);
		// /**
		// * 此处的设置是为了保证不会在查询状态时出现反复的 排队/正在/完成的现象
		// * 此时的加入toVisitedUrl是正常的加入，与关注加入无关
		// */
		// url.setUrlSource("");
		// toVisitUrls.add(url);
		// }
		toVisitUrlsLock.lock();
		try {
			removeVisitingUrls(url);
			/**
			 * 此处的设置是为了保证不会在查询状态时出现反复的 排队/正在/完成的现象
			 * 此时的加入toVisitedUrl是正常的加入，与关注加入无关
			 */
			url.setUrlSource("");
			toVisitUrls.add(url);
		} finally {
			toVisitUrlsLock.unlock();
		}
	}

	public void addUrlToTail4QQ(UrlPojo url) {
		// synchronized (toVisitUrls4QQ) {
		// removeVisitingUrls(url);
		// /**
		// * 此处的设置是为了保证不会在查询状态时出现反复的 排队/正在/完成的现象
		// * 此时的加入toVisitedUrl是正常的加入，与关注加入无关
		// */
		// url.setUrlSource("");
		// toVisitUrls4QQ.add(url);
		// }
		toVisitUrls4QQLock.lock();
		try {
			removeVisitingUrls(url);
			/**
			 * 此处的设置是为了保证不会在查询状态时出现反复的 排队/正在/完成的现象
			 * 此时的加入toVisitedUrl是正常的加入，与关注加入无关
			 */
			url.setUrlSource("");
			toVisitUrls4QQ.add(url);
		} finally {
			toVisitUrls4QQLock.unlock();
		}
	}

	// 如遇url异常，加入error_urls
	public void addErrorUrl(UrlPojo errorUrl) {
		// synchronized (errorUrls) {
		// removeVisitingUrls(errorUrl);
		// errorUrls.add(errorUrl);
		// errorUrlsBloomFilter.add(errorUrl.toString());
		// errorUrls_urls_size++;
		// }
		errorUrlsLock.lock();
		try {
			removeVisitingUrls(errorUrl);
			errorUrls.add(errorUrl);
			errorUrlsBloomFilter.add(errorUrl.toString());
			errorUrls_urls_size++;
		} finally {
			errorUrlsLock.unlock();
		}
	}

	// 如遇url异常，不再加入error_urls，而是更改type为6做为媒体版为做
	public void addErrorUrlToMedia(UrlPojo errorUrl) {
		toVisitUrlsLock.lock();
		try {
			removeVisitingUrls(errorUrl);
			if (errorUrl.getType() == 1) {
				errorUrl.setType(6);
			} else if (errorUrl.getType() == 2) {
				errorUrl.setType(7);
			}
			addUrlToFirst(errorUrl);
		} finally {
			toVisitUrlsLock.unlock();
		}
	}

	// 如果媒体版的抓取，遇url异常，不再加入error_urls，而是更改type为10做为媒体版为做
	public void addErrorUrlToEnterprise(UrlPojo errorUrl) {
		toVisitUrlsLock.lock();
		try {
			removeVisitingUrls(errorUrl);
			if (errorUrl.getType() == 6 || errorUrl.getType() == 1) {
				errorUrl.setType(10);
			} else if (errorUrl.getType() == 7 || errorUrl.getType() == 2) {
				errorUrl.setType(11);
			}
			addUrlToFirst(errorUrl);
		} finally {
			toVisitUrlsLock.unlock();
		}
	}

	// 如果企业版的抓取，遇url异常，不再加入error_urls，而是更改type为14做为专业版来抓取
	public void addErrorUrlToProfession(UrlPojo errorUrl) {
		toVisitUrlsLock.lock();
		try {
			removeVisitingUrls(errorUrl);
			if (errorUrl.getType() == 10) {
				errorUrl.setType(14);
			} else if (errorUrl.getType() == 20) {
				errorUrl.setType(15);
			}
			addUrlToFirst(errorUrl);
		} finally {
			toVisitUrlsLock.unlock();
		}
	}

	// 如果企业版的抓取，遇url异常，不再加入error_urls，而是更改type为14做为专业版来抓取
	public void addErrorUrlToGovernment(UrlPojo errorUrl) {
		toVisitUrlsLock.lock();
		try {
			removeVisitingUrls(errorUrl);
			// 这里要注意到是从企业版过来的,所以type改成专业版的个人信息就可以了
			if (errorUrl.getType() == 10) {
				errorUrl.setType(14);// 加到专业版的个人信息抓取部分
			} else if (errorUrl.getType() == 11) {
				errorUrl.setType(20);
			}
			addUrlToFirst(errorUrl);
		} finally {
			toVisitUrlsLock.unlock();
		}
	}

	// 重新加载遇到问题的url的抓取
	UrlPojo temp_errorUrl = null;

	public void clearErrorList() {
		// synchronized (errorUrls) {
		// errorUrls.clear();
		// }
		errorUrlsLock.lock();
		try {
			errorUrls.clear();
		} finally {
			errorUrlsLock.unlock();
		}
	}

	public void addToVisitByUidFromKeySearch(String uid) {
		toVisitUrlsLock.lock();
		try {
			// 将uid对应的帐户和博文信息都加入到待抓取列表
			// addUrl(new UrlPojo(FormatOutput.urlPrefix + uid, 1));
			List<UrlPojo> urlPojoList = new ArrayList<UrlPojo>(2);
			urlPojoList.add(new UrlPojo(FormatOutput.urlPrefix + uid, 1));
			urlPojoList.add(new UrlPojo(FormatOutput.urlPrefix + uid, 2));
			addUrlsListFirst(urlPojoList);
		} finally {
			toVisitUrlsLock.unlock();
		}
	}

	public void addToVisitByUidFromTransmit(String uid) {
		toVisitUrlsLock.lock();
		try {
			addUrl(new UrlPojo(FormatOutput.urlPrefix + uid, 1));
			addUrl(new UrlPojo(FormatOutput.urlPrefix + uid, 2));
		} finally {
			toVisitUrlsLock.unlock();
		}
	}

	public void addToVisitByUidFromKeySearch4QQ(String uid) {
		// synchronized (toVisitUrls4QQ) {
		// addUrl4QQ(new UrlPojo(FormatOutput.urlPrefix_QQ + uid, 1));
		// }
		toVisitUrls4QQLock.lock();
		try {
			addUrl4QQ(new UrlPojo(FormatOutput.urlPrefix_QQ + uid, 1));
		} finally {
			toVisitUrls4QQLock.unlock();
		}
	}

	public UrlsManager() {

	}

	/**
	 * 以下是对由关注产生的帐号集合的操作
	 */
	public void addToVisitAttentionUrl(UrlPojo urlPojo) {
		toVisitAttentionUrlsLock.lock();
		try {
			toVisitAttentionUrls.add(urlPojo);
		} finally {
			toVisitAttentionUrlsLock.unlock();
		}
	}

	public void addToVisitAttentionUrl4QQ(UrlPojo urlPojo) {
		toVisitAttentionUrls4QQLock.lock();
		try {
			toVisitAttentionUrls4QQ.add(urlPojo);
		} finally {
			toVisitAttentionUrls4QQLock.unlock();
		}
	}

	/**
	 * 此为手动提交的请求,置于抓取队列的最前端,即为用户请求的url优先
	 * 
	 * @param urlPojo
	 */

	public void addToVisitAttentionUrlFirst(UrlPojo urlPojo) {
		// synchronized (toVisitAttentionUrls) {
		// toVisitAttentionUrls.addFirst(urlPojo);
		// }
		toVisitAttentionUrlsLock.lock();
		try {
			toVisitAttentionUrls.addFirst(urlPojo);
		} finally {
			toVisitAttentionUrlsLock.unlock();
		}
	}

	public void addToVisitAttentionUrlFirst4QQ(UrlPojo urlPojo) {
		// synchronized (toVisitAttentionUrls4QQ) {
		// toVisitAttentionUrls4QQ.addFirst(urlPojo);
		// }
		toVisitAttentionUrls4QQLock.lock();
		try {
			toVisitAttentionUrls4QQ.addFirst(urlPojo);
		} finally {
			toVisitAttentionUrls4QQLock.unlock();
		}
	}

	public void addToVisitAttentionUrlListFirst(List<UrlPojo> urlPojoList) {
		// synchronized (synObj4Attention) {
		// for (UrlPojo pojo : urlPojoList) {
		// if (TypePojo.isQQ(pojo.getUrl())) {
		// addToVisitAttentionUrlFirst4QQ(pojo);
		// } else {
		// addToVisitAttentionUrlFirst(pojo);
		// }
		// }
		// }
		synObj4AttentionLock.lock();
		try {
			for (UrlPojo pojo : urlPojoList) {
				if (TypePojo.isQQ(pojo.getUrl())) {
					addToVisitAttentionUrlFirst4QQ(pojo);
				} else {
					addToVisitAttentionUrlFirst(pojo);
				}
			}
		} finally {
			synObj4AttentionLock.unlock();
		}
	}

	// 得到一个toVisitAttentionUrl
	UrlPojo temp_attention_url = null;

	public UrlPojo getOneAttentionUrl() {
		toVisitAttentionUrlsLock.lock();
		try {
			temp_attention_url = toVisitAttentionUrls.pollFirst();
			if (temp_attention_url != null) {
				addVisitingUrls(temp_attention_url);
			}
		} finally {
			toVisitAttentionUrlsLock.unlock();
		}
		return temp_attention_url;
	}

	UrlPojo temp_attention_url4QQ = null;

	public UrlPojo getOneAttentionUrl4QQ() {
		// synchronized (toVisitAttentionUrls4QQ) {
		// temp_attention_url4QQ = toVisitAttentionUrls4QQ.pollFirst();
		// if (temp_attention_url4QQ != null) {
		// addVisitingUrls(temp_attention_url4QQ);
		// }
		// return temp_attention_url4QQ;
		// }
		toVisitAttentionUrls4QQLock.lock();
		try {
			temp_attention_url4QQ = toVisitAttentionUrls4QQ.pollFirst();
			if (temp_attention_url4QQ != null) {
				addVisitingUrls(temp_attention_url4QQ);
			}
		} finally {
			toVisitAttentionUrls4QQLock.unlock();
		}
		return temp_attention_url4QQ;
	}

	// 向toVisitedUrls加入一个urls的数据列表,此时的url只抓取url的个人信息
	public void addUrlsArray(String[] uid_array, String urlSource) {
		// synchronized (toVisitAttentionUrls) {
		// for (String uid : uid_array)
		// this.addToVisitAttentionUrl(new UrlPojo(FormatOutput.urlPrefix
		// + uid, 1, urlSource));
		// }
		toVisitAttentionUrlsLock.lock();
		try {
			for (String uid : uid_array)
				this.addToVisitAttentionUrl(new UrlPojo(FormatOutput.urlPrefix
						+ uid, 1, urlSource));
		} finally {
			toVisitAttentionUrlsLock.unlock();
		}
	}

	public void addUrlsArray4QQ(String[] uid_array, String urlSource) {
		// synchronized (toVisitAttentionUrls4QQ) {
		// for (String uid : uid_array)
		// this.addToVisitAttentionUrl4QQ(new UrlPojo(
		// FormatOutput.urlPrefix_QQ + uid, 1, urlSource));
		// }
		toVisitAttentionUrls4QQLock.lock();
		try {
			for (String uid : uid_array)
				this.addToVisitAttentionUrl4QQ(new UrlPojo(
						FormatOutput.urlPrefix_QQ + uid, 1, urlSource));
		} finally {
			toVisitAttentionUrls4QQLock.unlock();
		}
	}

	public void addVisitedAttentionUrl(UrlPojo pojo) {
		// synchronized (visitedAttentionUrls) {
		// removeVisitingUrls(pojo);
		// /**
		// * 加此处是为了减少重复提交关注后，对查询状态的影响
		// */
		// if (!visitedAttentionUrls.contains(pojo)) {
		// visitedAttentionUrls.add(pojo);
		// }
		// }
		visitedAttentionUrlsLock.lock();
		try {
			removeVisitingUrls(pojo);
			/**
			 * 加此处是为了减少重复提交关注后，对查询状态的影响
			 */
			if (!visitedAttentionUrls.contains(pojo)) {
				visitedAttentionUrls.add(pojo);
			}
		} finally {
			visitedAttentionUrlsLock.unlock();
		}
	}

	/**
	 * 以下是对周期任务执行所新增的变量设置
	 */
	public static void addToVisitCircleUrl(UrlPojo pojo) {
		// synchronized (toVisitCircleUrls) {
		// toVisitCircleUrls.add(pojo);
		// }
		toVisitCircleUrlsLock.lock();
		try {
			toVisitCircleUrls.add(pojo);
		} finally {
			toVisitCircleUrlsLock.unlock();
		}
	}

	public static void addToVisitCircleUrl4QQ(UrlPojo pojo) {
		// synchronized (toVisitCircleUrls4QQ) {
		// toVisitCircleUrls4QQ.add(pojo);
		// }
		toVisitCircleUrls4QQLock.lock();
		try {
			toVisitCircleUrls4QQ.add(pojo);
		} finally {
			toVisitCircleUrls4QQLock.unlock();
		}
	}

	// 得到一个toVisitCircleUrl
	UrlPojo temp_circle_url = null;

	public UrlPojo getOneCircleUrl() {
		toVisitCircleUrlsLock.lock();
		try {
			temp_circle_url = toVisitCircleUrls.pollFirst();
			if (temp_circle_url != null) {
				addVisitingUrls(temp_circle_url);
			}
		} finally {
			toVisitCircleUrlsLock.unlock();
		}
		return temp_circle_url;
	}

	public UrlPojo getOneCommentUrl() {
		toVisitCommentUrlsLock.lock();
		try {
			temp_circle_url = toVisitCommentUrls.pollFirst();
			if (temp_circle_url != null) {
				addVisitingUrls(temp_circle_url);
			}
		} finally {
			toVisitCommentUrlsLock.unlock();
		}
		return temp_circle_url;
	}

	UrlPojo temp_circle_url4QQ = null;

	public UrlPojo getOneCircleUrl4QQ() {
		// synchronized (toVisitCircleUrls4QQ) {
		// temp_circle_url4QQ = toVisitCircleUrls4QQ.pollFirst();
		// if (temp_circle_url4QQ != null) {
		// addVisitingUrls(temp_circle_url4QQ);
		// }
		// return temp_circle_url4QQ;
		// }
		toVisitCircleUrls4QQLock.lock();
		try {
			temp_circle_url4QQ = toVisitCircleUrls4QQ.pollFirst();
			if (temp_circle_url4QQ != null) {
				addVisitingUrls(temp_circle_url4QQ);
			}
		} finally {
			toVisitCircleUrls4QQLock.unlock();
		}

		return temp_circle_url4QQ;
	}

	public static void main(String[] args) {
		// if (toVisitUrls == null
		// && (!new File(StaticValue.toVisitUrls_file_path).exists())) {
		// toVisitUrls = new LinkedList<UrlPojo>();
		// logger.info("toVisitUrls初始化完成,此时是空列表!");
		// } else {
		// try {
		// toVisitUrls_Up = (LinkedList) ObjectIoUtil
		// .readObject(StaticValue.toVisitUrls_file_path);
		// logger.info("main---成功加载toVisitUrls列表!");
		// } catch (Exception e) {
		// e.printStackTrace();
		// logger.info("main---toVisitUrls列表加载失败，即将重新初始化");
		// toVisitUrls = new LinkedList<UrlPojo>();
		// logger.info("main---toVisitUrls重新初始化完成");
		// }
		// }
	}

}
