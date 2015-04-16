package com.mbm.elint.manager.database;

import java.util.Date;
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

import com.mbm.elint.entity.DocInfo;
import com.mbm.elint.entity.dao.DocInfoDao;
import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.StaticValue;

@Component
@Transactional
public class DocInfoManager {
	private static Logger logger = LoggerFactory
			.getLogger(DocInfoManager.class);
	@Autowired
	private DocInfoDao docInfoDao;
	public static long repeat_doc_number = 0;
	public static BloomFilter bloomFilter;
	public static long saved_doc_number = 0;

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
			logger.info("doc文章过滤器初始化完成");
		} else {
			try {
				bloomFilter = (BloomFilter) temp_obj;
				logger.info("redis--成功加载doc文章过滤器!");
			} catch (Exception e) {
				logger.info("****加载失败doc文章过滤器");
				e.printStackTrace();
			}
		}
	}

	public boolean saveOrUpdate(DocInfo docInfo) {
		// 用于doc去重
		/**
		 * 现在抓取过微言的url,固以它为key做映射。
		 */
		bloomFilterLock.lock();
		try {
			String docKey = "url:" + docInfo.getDocUrl();
			if (bloomFilter.contains(docKey)) {
				repeat_doc_number++;
				logger.info(docInfo.getAuthor() + "-----重复抓取了一篇");
				return false;
			} else {
				try {
					bloomFilter.add(docKey);
					saved_doc_number++;
					docInfo.setInsertTime(new Date());
					this.docInfoDao.save(docInfo);
				} catch (GenericJDBCException jdbcException) {
					jdbcException.printStackTrace();
					// this.docInfoDao.getSession().clear();
					logger.info("docInfoDao发生异常后已经清理掉session");
					logger.info("为字符不识别异常，此url算做正常，只将该条微言抛弃!");
				} catch (HibernateException e) {
					e.printStackTrace();
					// this.docInfoDao.getSession().clear();
					logger.info("docInfoDao发生异常后已经清理掉session");
					logger.info("为字符不识别异常，此url算做正常，只将该条微言抛弃!");
					// throw new Exception();
				}
			}
		} finally {
			bloomFilterLock.unlock();
		}
		return true;
	}
}
