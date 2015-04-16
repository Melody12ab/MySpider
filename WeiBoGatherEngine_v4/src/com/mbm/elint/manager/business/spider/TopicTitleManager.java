package com.mbm.elint.manager.business.spider;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.util.DateUtil;
import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.StaticValue;

@Component
public class TopicTitleManager {
	public static Logger logger = Logger.getLogger(TopicTitleManager.class);
	public static LinkedList<KeywordPojo> toVisitTopicTitle = null;
	public static HashSet<KeywordPojo> visitedTopicTitle = null;
	public static LinkedList<KeywordPojo> visitingTopicTitle = new LinkedList<KeywordPojo>();
	/**
	 * 以下是对周期任务的设置
	 */
	public static LinkedList<KeywordPojo> toVisitCircleTopicTitle = new LinkedList<KeywordPojo>();

	/**
	 * 专为更新同步锁控制而添加---开始
	 */
	static final Lock toVisitTopicTitleLock = new ReentrantLock();
	static final Lock visitingTopicTitleLock = new ReentrantLock();
	static final Lock visitedTopicTitleLock = new ReentrantLock();
	static final Lock toVisitCircleTopicTitleLock = new ReentrantLock();
	/**
	 * 专为更新同步锁控制而添加---结束
	 */

	// 初始化布隆过滤器
	static {
		Object temp_obj = null;
		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(StaticValue.toVisitTopicTitle_file_path,
								StaticValue.charset)));

		// 初始化toVisitKeyWords List
		if (toVisitTopicTitle == null && (temp_obj == null)) {
			toVisitTopicTitle = new LinkedList<KeywordPojo>();
			logger.info("toVisitTopicTitle_file_path初始化完成,此时是空列表!");
		} else {
			try {
				toVisitTopicTitle = (LinkedList) temp_obj;
				logger.info("redis--成功加载toVisitTopicTitle列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载toVisitTopicTitle列表");
			}
		}
		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil
						.getObj(StringTransform
								.getByteArray(
										StaticValue.circle_visitedTopicTitle_cache_file_path,
										StaticValue.charset)));
		// 初始化visitedKeyWords
		if (visitedTopicTitle == null && (temp_obj == null)) {
			visitedTopicTitle = new HashSet<KeywordPojo>();
			logger.info("circle_visitedTopicTitle_cache初始化完成,此时是空列表!");
		} else {
			try {
				visitedTopicTitle = (HashSet) temp_obj;
				logger.info("redis--成功加载circle_visitedTopicTitle_cache列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载circle_visitedTopicTitle_cache列表，即将重新初始化");
			}
		}
	}

	private KeywordPojo temp_keyword = null;

	// 得到一个toVisitKeyword
	public KeywordPojo getOneTopicTitle() {
		toVisitTopicTitleLock.lock();
		try {
			temp_keyword = toVisitTopicTitle.pollFirst();
			if (temp_keyword != null) {
				addVisitingTopicTitle(temp_keyword);
			}
		} finally {
			toVisitTopicTitleLock.unlock();
		}
		return temp_keyword;
	}

	public boolean removeToVisitTopicTitle(List<KeywordPojo> keywords) {
		toVisitTopicTitleLock.lock();
		try {
			for (KeywordPojo keyword : keywords) {
				removeToVisitTopicTitle(keyword);
			}
		} finally {
			toVisitTopicTitleLock.unlock();
		}
		return true;
	}

	public boolean removeToVisitTopicTitle(KeywordPojo keyword) {
		toVisitTopicTitleLock.lock();
		try {
			if (toVisitTopicTitle.remove(keyword)) {
				logger.info("将" + keyword + " 从toVisitKeyWords中去除!");
				return true;
			}
		} finally {
			toVisitTopicTitleLock.unlock();
		}
		return false;
	}

	public void addVisitingTopicTitle(KeywordPojo keyword) {
		visitingTopicTitleLock.lock();
		try {
			visitingTopicTitle.add(keyword);
		} finally {
			visitingTopicTitleLock.unlock();
		}
	}

	public void removeVisitingTopicTitle(KeywordPojo keyword) {
		visitingTopicTitleLock.lock();
		try {
			visitingTopicTitle.remove(keyword);
		} finally {
			visitingTopicTitleLock.unlock();
		}
	}

	public void addVisitedTopicTitle(KeywordPojo keyword) {
		visitedTopicTitleLock.lock();
		try {
			removeVisitingTopicTitle(keyword);
			if (visitedTopicTitle.contains(keyword.toString())) {
				logger.info("该关键字已保过,不再保存至visitedKeyWords!");
			} else {
				keyword.setReGrabBeginTime(DateUtil.getLongByDate()
						+ StaticValue.keyword_task_circle_time);
				keyword.setType(1);
				visitedTopicTitle.add(keyword);
			}
		} finally {
			visitedTopicTitleLock.unlock();
		}
	}

	// 随时加入新的urls集合
	public void addTopicTitleList(List<KeywordPojo> newKeyWordsList) {
		toVisitTopicTitleLock.lock();
		try {
			for (KeywordPojo keyword : newKeyWordsList) {
				this.addOneTopicTitle(keyword);
			}
		} finally {
			toVisitTopicTitleLock.unlock();
		}
	}

	// 加入一个keyword
	public void addOneTopicTitle(KeywordPojo keyword) {
		toVisitTopicTitleLock.lock();
		try {
			toVisitTopicTitle.add(keyword);
		} finally {
			toVisitTopicTitleLock.unlock();
		}
	}

	public TopicTitleManager() {

	}

	/**
	 * 以下是对新增的周期任务的设置
	 */
	// 加入一个keyword
	public static void addOneCircleKeyWord(KeywordPojo keyword) {
		toVisitCircleTopicTitleLock.lock();
		try {
			toVisitCircleTopicTitle.add(keyword);
		} finally {
			toVisitCircleTopicTitleLock.unlock();
		}
	}

	// 从周期任务中移除部分任务,remove它之后，visitedKeyWord中的该对象将被删除
	public static boolean removeOneCircleTopicTitle(KeywordPojo keyword) {
		visitedTopicTitleLock.lock();
		try {
			if (visitedTopicTitle.remove(keyword)) {
				logger.info("删除" + keyword.getKeyWord() + "成功");
			} else {
				logger.info("删除" + keyword.getKeyWord() + "失败");
			}
		} finally {
			visitedTopicTitleLock.unlock();
		}
		return true;
	}

	private KeywordPojo temp_circle_keyword = null;

	// 得到一个toVisitKeyword
	public KeywordPojo getOneCircleTopicTitle() {
		toVisitCircleTopicTitleLock.lock();
		try {
			temp_circle_keyword = toVisitCircleTopicTitle.pollFirst();
			if (temp_circle_keyword != null) {
				addVisitingTopicTitle(temp_circle_keyword);
			}
		} finally {
			toVisitCircleTopicTitleLock.unlock();
		}

		return temp_circle_keyword;
	}

	public static void main(String[] args) {
	}

}
