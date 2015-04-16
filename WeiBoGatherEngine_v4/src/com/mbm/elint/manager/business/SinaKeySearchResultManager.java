package com.mbm.elint.manager.business;

import org.hibernate.HibernateException;
import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.elint.entity.DocInfo;
import com.mbm.elint.entity.dao.SinaKeySearchResultDao;
import com.mbm.elint.manager.database.DocInfoManager;

@Component
@Transactional
public class SinaKeySearchResultManager {
	private static Logger logger = LoggerFactory
			.getLogger(SinaKeySearchResultManager.class);
	public static long repeat_keywordsDoc_number = 0;
	// public static BloomFilter bloomFilter;
	public static long saved_keywordsDoc_number = 0;
	// 初始化布隆过滤器----与doc文档过滤器合并
	// static {
	// if (bloomFilter == null
	// && (!new File(StaticValue.keyWordDoc_bloomFilter_file_path).exists())) {
	// bloomFilter = new BloomFilter(StaticValue.keyWordsDoc_filter_size);
	// logger.info("keywordDoc滤器初始化完成");
	// } else {
	// try {
	// bloomFilter =(BloomFilter)ObjectIoUtil
	// .readObject(StaticValue.keyWordDoc_bloomFilter_file_path);
	// logger.info("成功加载keywordDoc过滤器!");
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.info("keywordDoc过滤器加载失败，即将重新初始化");
	// bloomFilter = new BloomFilter(StaticValue.keyWordsDoc_filter_size);
	// logger.info("keywordDoc布隆过滤器初始化完成");
	// }
	// }
	// }
	@Autowired
	private SinaKeySearchResultDao sinaKeySearchResultDao;
	public static Object synObj = new Object();
	public boolean saveSearchResultInfo(DocInfo searchResult) {
		/**
		 * 直接用docUrl去重
		 */
		synchronized (synObj) {
			String keyWordDocKey = searchResult.getDocUrl();
			if (DocInfoManager.bloomFilter.contains(keyWordDocKey)) {
				repeat_keywordsDoc_number++;
				logger
						.info(searchResult.getAuthor()
								+ "-----keywordDoc重复抓取了一篇");
				return false;
			} else {
				try {
					DocInfoManager.bloomFilter.add(keyWordDocKey);
					saved_keywordsDoc_number++;
					/**
					 * 此处进行修改将searchResult一并放入doc文档表中。
					 */
					this.sinaKeySearchResultDao.save(searchResult);
					return true;
				} catch (GenericJDBCException jdbcException) {
					jdbcException.printStackTrace();
					this.sinaKeySearchResultDao.getSession().clear();
					logger.info("docInfoDao发生异常后已经清理掉session");
					logger.info("为字符不识别异常，此url算做正常，只将该条微言抛弃!");
					return false;
				} catch (HibernateException e) {
					e.printStackTrace();
					this.sinaKeySearchResultDao.getSession().clear();
					logger.info("sinaKeySearchResultDao发生异常后已经清理掉session");
					return false;
				}
			}
		}
	}
}
