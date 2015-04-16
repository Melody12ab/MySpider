package com.mbm.elint.manager.database.mongoDB;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.hibernate.HibernateException;
import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import bloomFilter.BloomFilter;

import com.mbm.elint.entity.dao.mongoDB.TopicDocDao;
import com.mbm.elint.entity.mongoDB.TopicDoc;
import com.mbm.elint.manager.database.DocInfoManager;
import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.StaticValue;

@Component
@Transactional
public class TopicInfoManager {
	private static Logger logger = LoggerFactory
			.getLogger(TopicInfoManager.class);
	@Autowired
	private TopicDocDao topicDocDao;
	public static BloomFilter bloomFilter;
	/**
	 * 专为更新同步锁控制而添加---开始
	 */
	static final Lock bloomFilterLock = new ReentrantLock();

	/**
	 * 专为更新同步锁控制而添加---结束
	 */

	// 初始化布隆过滤器
	static {
		Object temp_obj = null;
		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(StaticValue.doc_bloomFilter_file_path,
								StaticValue.charset)));
		if (bloomFilter == null && (temp_obj == null)) {
			bloomFilter = new BloomFilter(StaticValue.doc_filter_size);
			logger.info("话题列表topicDoc---过滤器初始化完成");
		} else {
			try {
				bloomFilter = (BloomFilter) temp_obj;
				logger.info("redis--成功话题列表topicDoc过滤器!");
			} catch (Exception e) {
				logger.info("****加载话题列表topicDoc过滤器");
				e.printStackTrace();
			}
		}
	}

	public boolean saveOrUpdate(TopicDoc docInfo) throws Exception {
		// 用于doc去重
		/**
		 * 现在抓取过微言的url,固以它为key做映射。
		 */
		bloomFilterLock.lock();
		try {
			String docKey = "topic:"
					+ docInfo.getContent().substring(
							0,
							docInfo.getContent().length() > 8 ? 7 : docInfo
									.getContent().length());
			if (bloomFilter.contains(docKey)) {
				DocInfoManager.repeat_doc_number++;
				return false;
			} else {
				try {
					bloomFilter.add(docKey);
					DocInfoManager.saved_doc_number++;
					this.topicDocDao.save(docInfo);
				} catch (GenericJDBCException jdbcException) {
					jdbcException.printStackTrace();
					// this.docInfoDao.getSession().clear();
					logger.info("topicDocDao发生异常后已经清理掉session");
					logger.info("为字符不识别异常，此url算做正常，只将该条微言抛弃!");
				} catch (HibernateException e) {
					e.printStackTrace();
					// this.docInfoDao.getSession().clear();
					logger.info("topicDocDao发生异常后已经清理掉session");
					throw new Exception();
				}
			}
		} finally {
			bloomFilterLock.unlock();
		}
		return true;
	}
}
