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

import com.mbm.elint.entity.Comments;
import com.mbm.elint.entity.dao.CommentDao;
import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.StaticValue;

@Component
@Transactional
public class CommentDocInfoManager {
	private static Logger logger = LoggerFactory
			.getLogger(CommentDocInfoManager.class);
	// @Autowired
	// private CommentDocDao commentDocDao;
	@Autowired
	private CommentDao commentDao;
	public static long repeat_commentDoc_number = 0;
	public static BloomFilter bloomFilter;
	public static long saved_commentDoc_number = 0;

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
						.getByteArray(
								StaticValue.commentDoc_bloomFilter_file_path,
								StaticValue.charset)));

		if (bloomFilter == null && (temp_obj == null)) {
			bloomFilter = new BloomFilter(StaticValue.commentDoc_filter_size);
			logger.info("评论列表commentDoc---过滤器初始化完成");
		} else {
			try {
				bloomFilter = (BloomFilter) temp_obj;
				logger.info("redis--成功加载评论列表commentDoc过滤器!");
			} catch (Exception e) {
				logger.info("****加载失败评论列表commentDoc过滤器");
				e.printStackTrace();
			}
		}
	}

	// public boolean saveOrUpdate(CommentDoc docInfo) throws Exception {
	public boolean saveOrUpdate(Comments comment) {
		// 用于doc去重
		/**
		 * 现在抓取过微言的url,固以它为key做映射。
		 */
		bloomFilterLock.lock();
		try {
			String docKey = "mid:"
					+ comment.getMid()
					+ "uid:"
					+ comment.getCommentUid()
					+ comment.getPublishtime()
					+ "content:"
					+ comment.getContent().substring(
							0,
							comment.getContent().length() > 8 ? 7 : comment
									.getContent().length());
			if (bloomFilter.contains(docKey)) {
				repeat_commentDoc_number++;
				return false;
			} else {
				try {
					bloomFilter.add(docKey);
					saved_commentDoc_number++;
					// this.commentDocDao.save(docInfo);
					this.commentDao.save(comment);
				} catch (GenericJDBCException jdbcException) {
					jdbcException.printStackTrace();
					// this.docInfoDao.getSession().clear();
					logger.info("commentDocInfoDao发生异常后已经清理掉session");
					logger.info("为字符不识别异常，此url算做正常，只将该条微言抛弃!");
				} catch (HibernateException e) {
					e.printStackTrace();
					logger.info("commentDocInfoDao发生异常后已经清理掉session");
				} catch (Exception e_final) {
					e_final.printStackTrace();
					logger.info("保存一条语句时出现错误，暂放弃该条数据!");
				}
			}
		} finally {
			bloomFilterLock.unlock();
		}
		return true;
	}
}
