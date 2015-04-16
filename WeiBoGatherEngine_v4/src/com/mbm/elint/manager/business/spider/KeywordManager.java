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
import com.weibo.common.utils.FileOperatorUtil;
import com.weibo.common.utils.StaticValue;

@Component
public class KeywordManager {
	public static Logger logger = Logger.getLogger(KeywordManager.class);
	public static LinkedList<KeywordPojo> toVisitKeyWords = null;
	public static LinkedList<KeywordPojo> toVisitKeyWords4QQ = new LinkedList<KeywordPojo>();
	public static HashSet<KeywordPojo> visitedKeyWords = null;
	public static LinkedList<KeywordPojo> visitingKeyWords = new LinkedList<KeywordPojo>();
	/**
	 * 以下是对周期任务的设置
	 */
	public static LinkedList<KeywordPojo> toVisitCircleKeyWords = new LinkedList<KeywordPojo>();
	public static LinkedList<KeywordPojo> toVisitCircleKeyWords4QQ = new LinkedList<KeywordPojo>();

	/**
	 * 专为更新同步锁控制而添加---开始
	 */

	static final Lock toVisitKeyWordsLock = new ReentrantLock();
	static final Lock toVisitKeyWords4QQLock = new ReentrantLock();
	static final Lock visitingKeyWordsLock = new ReentrantLock();
	static final Lock visitedKeyWordsLock = new ReentrantLock();
	static final Lock toVisitCircleKeyWordsLock = new ReentrantLock();
	static final Lock toVisitCircleKeyWords4QQLock = new ReentrantLock();

	/**
	 * 专为更新同步锁控制而添加---结束
	 */

	// 初始化布隆过滤器
	static {
		/**
		 * 首先把根路径搞定
		 */
		if (FileOperatorUtil.createRootDir(StaticValue.root_path)) {
			// logger.info("存储数据的根路径初始化完毕!");
		} else {
			// logger.info("存储数据的根路径出错，请重新确认"+StaticValue.root_path+" 路径的存在!");
			System.exit(-1);
		}
		Object temp_obj = null;
		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(StaticValue.toVisitKeyWords_file_path,
								StaticValue.charset)));

		// 初始化toVisitKeyWords List
		if (toVisitKeyWords == null && (temp_obj == null)) {
			toVisitKeyWords = new LinkedList<KeywordPojo>();
			logger.info("toVisitKeyWords初始化完成,此时是空列表!");
		} else {
			try {
				toVisitKeyWords = (LinkedList) temp_obj;
				logger.info("redis--成功加载toVisitKeyWords列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载toVisitKeyWords列表");
			}
		}
		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil
						.getObj(StringTransform
								.getByteArray(
										StaticValue.circle_visitedKeyWords_cache_file_path,
										StaticValue.charset)));
		// 初始化visitedKeyWords
		if (visitedKeyWords == null && (temp_obj == null)) {
			visitedKeyWords = new HashSet<KeywordPojo>();
			logger.info("visitedKeyWords初始化完成,此时是空列表!");
		} else {
			try {
				visitedKeyWords = (HashSet) temp_obj;
				logger.info("redis--成功加载visitedKeyWords列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载visitedKeyWords列表，即将重新初始化");
			}
		}
	}

	private KeywordPojo temp_keyword = null;

	// 得到一个toVisitKeyword
	public KeywordPojo getOneKeyWord() {
		// synchronized (toVisitKeyWords) {
		// temp_keyword = toVisitKeyWords.pollFirst();
		// if (temp_keyword != null) {
		// addVisitingKeyWord(temp_keyword);
		// }
		// }
		toVisitKeyWordsLock.lock();
		try {
			temp_keyword = toVisitKeyWords.pollFirst();
			if (temp_keyword != null) {
				addVisitingKeyWord(temp_keyword);
			}
		} finally {
			toVisitKeyWordsLock.unlock();
		}
		return temp_keyword;
	}

	private KeywordPojo temp_keyword_QQ = null;

	public KeywordPojo getOneKeyWord4QQ() {
		// synchronized (toVisitKeyWords4QQ) {
		// temp_keyword_QQ = toVisitKeyWords4QQ.pollFirst();
		// if (temp_keyword_QQ != null) {
		// addVisitingKeyWord(temp_keyword_QQ);
		// }
		// }
		toVisitKeyWords4QQLock.lock();
		try {
			temp_keyword_QQ = toVisitKeyWords4QQ.pollFirst();
			if (temp_keyword_QQ != null) {
				addVisitingKeyWord(temp_keyword_QQ);
			}
		} finally {
			toVisitKeyWords4QQLock.unlock();
		}
		return temp_keyword_QQ;
	}

	public boolean removeToVisitKeyWord(List<KeywordPojo> keywords) {
		// synchronized (toVisitKeyWords) {
		// for (KeywordPojo keyword : keywords) {
		// removeToVisitKeyWord(keyword);
		// }
		// }
		toVisitKeyWordsLock.lock();
		try {
			for (KeywordPojo keyword : keywords) {
				removeToVisitKeyWord(keyword);
			}
		} finally {
			toVisitKeyWordsLock.unlock();
		}
		return true;
	}

	public boolean removeToVisitKeyWord(KeywordPojo keyword) {
		// synchronized (toVisitKeyWords) {
		// if (toVisitKeyWords.remove(keyword)) {
		// logger.info("将" + keyword + " 从toVisitKeyWords中去除!");
		// return true;
		// }
		// return false;
		// }
		toVisitKeyWordsLock.lock();
		try {
			if (toVisitKeyWords.remove(keyword)) {
				logger.info("将" + keyword + " 从toVisitKeyWords中去除!");
				return true;
			}
		} finally {
			toVisitKeyWordsLock.unlock();
		}
		return false;
	}

	public void addVisitingKeyWord(KeywordPojo keyword) {
		// synchronized (visitingKeyWords) {
		// visitingKeyWords.add(keyword);
		// }
		visitingKeyWordsLock.lock();
		try {
			visitingKeyWords.add(keyword);
		} finally {
			visitingKeyWordsLock.unlock();
		}
	}

	public void removeVisitingKeyWord(KeywordPojo keyword) {
		// synchronized (visitingKeyWords) {
		// visitingKeyWords.remove(keyword);
		// }
		visitingKeyWordsLock.lock();
		try {
			visitingKeyWords.remove(keyword);
		} finally {
			visitingKeyWordsLock.unlock();
		}
	}

	public void addVisitedKeyWord(KeywordPojo keyword) {
		visitedKeyWordsLock.lock();
		try {
			removeVisitingKeyWord(keyword);
			if (visitedKeyWords.contains(keyword.toString())) {
				logger.info("该关键字已保过,不再保存至visitedKeyWords!");
			} else {
				keyword.setReGrabBeginTime(DateUtil.getLongByDate()
						+ StaticValue.keyword_task_circle_time);
				keyword.setType(1);
				visitedKeyWords.add(keyword);
			}
		} finally {
			visitedKeyWordsLock.unlock();
		}
	}

	// 随时加入新的urls集合
	public void addKeyWordsList(List<KeywordPojo> newKeyWordsList) {
		toVisitKeyWordsLock.lock();
		try {
			for (KeywordPojo keyword : newKeyWordsList) {
				this.addOneKeyWord(keyword);
			}
		} finally {
			toVisitKeyWordsLock.unlock();
		}
	}

	public void addKeyWordsList4QQ(List<KeywordPojo> newKeyWordsList) {
		// synchronized (toVisitKeyWords4QQ) {
		// for (KeywordPojo keyword : newKeyWordsList) {
		// this.addOneKeyWord4QQ(keyword);
		// }
		// }
		toVisitKeyWords4QQLock.lock();
		try {
			for (KeywordPojo keyword : newKeyWordsList) {
				this.addOneKeyWord4QQ(keyword);
			}
		} finally {
			toVisitKeyWords4QQLock.unlock();
		}
	}

	// 加入一个keyword
	public void addOneKeyWord(KeywordPojo keyword) {
		// synchronized (toVisitKeyWords) {
		// toVisitKeyWords.add(keyword);
		// }
		toVisitKeyWordsLock.lock();
		try {
			toVisitKeyWords.add(keyword);
		} finally {
			toVisitKeyWordsLock.unlock();
		}
	}

	public void addOneKeyWord4QQ(KeywordPojo keyword) {
		// synchronized (toVisitKeyWords4QQ) {
		// toVisitKeyWords4QQ.add(keyword);
		// }
		toVisitKeyWords4QQLock.lock();
		try {
			toVisitKeyWords4QQ.add(keyword);
		} finally {
			toVisitKeyWords4QQLock.unlock();
		}
	}

	public KeywordManager() {

	}

	/**
	 * 以下是对新增的周期任务的设置
	 */
	// 加入一个keyword
	public static void addOneCircleKeyWord(KeywordPojo keyword) {
		// synchronized (toVisitCircleKeyWords) {
		// toVisitCircleKeyWords.add(keyword);
		// }
		toVisitCircleKeyWordsLock.lock();
		try {
			toVisitCircleKeyWords.add(keyword);
		} finally {
			toVisitCircleKeyWordsLock.unlock();
		}
	}

	public static void addOneCircleKeyWord4QQ(KeywordPojo keyword) {
		// synchronized (toVisitCircleKeyWords4QQ) {
		// toVisitCircleKeyWords4QQ.add(keyword);
		// }
		toVisitCircleKeyWords4QQLock.lock();
		try {
			toVisitCircleKeyWords4QQ.add(keyword);
		} finally {
			toVisitCircleKeyWords4QQLock.unlock();
		}
	}

	// 从周期任务中移除部分任务,remove它之后，visitedKeyWord中的该对象将被删除
	public static boolean removeOneCircleKeyWord(KeywordPojo keyword) {
		// synchronized (visitedKeyWords) {
		// if (visitedKeyWords.remove(keyword)) {
		// logger.info("删除" + keyword.getKeyWord() + "成功");
		// } else {
		// logger.info("删除" + keyword.getKeyWord() + "失败");
		// }
		// }
		visitedKeyWordsLock.lock();
		try {
			if (visitedKeyWords.remove(keyword)) {
				logger.info("删除" + keyword.getKeyWord() + "成功");
			} else {
				logger.info("删除" + keyword.getKeyWord() + "失败");
			}
		} finally {
			visitedKeyWordsLock.unlock();
		}
		return true;
	}

	private KeywordPojo temp_circle_keyword = null;

	// 得到一个toVisitKeyword
	public KeywordPojo getOneCircleKeyWord() {
		// synchronized (toVisitCircleKeyWords) {
		// temp_circle_keyword = toVisitCircleKeyWords.pollFirst();
		// if (temp_circle_keyword != null) {
		// addVisitingKeyWord(temp_circle_keyword);
		// }
		// }
		toVisitCircleKeyWordsLock.lock();
		try {
			temp_circle_keyword = toVisitCircleKeyWords.pollFirst();
			if (temp_circle_keyword != null) {
				addVisitingKeyWord(temp_circle_keyword);
			}
		} finally {
			toVisitCircleKeyWordsLock.unlock();
		}

		return temp_circle_keyword;
	}

	private KeywordPojo temp_circle_keyword4QQ = null;

	public KeywordPojo getOneCircleKeyWord4QQ() {
		// synchronized (toVisitCircleKeyWords4QQ) {
		// temp_circle_keyword4QQ = toVisitCircleKeyWords4QQ.pollFirst();
		// if (temp_circle_keyword4QQ != null) {
		// addVisitingKeyWord(temp_circle_keyword4QQ);
		// }
		// }
		toVisitCircleKeyWords4QQLock.lock();
		try {
			temp_circle_keyword4QQ = toVisitCircleKeyWords4QQ.pollFirst();
			if (temp_circle_keyword4QQ != null) {
				addVisitingKeyWord(temp_circle_keyword4QQ);
			}
		} finally {
			toVisitCircleKeyWords4QQLock.unlock();
		}
		return temp_circle_keyword4QQ;
	}

	public static void main(String[] args) {
		// HashSet<KeywordPojo> kset = new HashSet<KeywordPojo>();
		// // LinkedList<KeywordPojo> kset=new LinkedList<KeywordPojo>();
		// KeywordPojo key1 = new KeywordPojo("123", 0);
		// KeywordPojo key2 = new KeywordPojo("123", 0);
		//
		// kset.add(key1);
		// kset.add(key2);
		//
		// System.out.println(kset.contains(key1));
		// System.out.println(kset.contains(key2));
	}

}
