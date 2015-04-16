package com.mbm.elint.manager.database;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import bloomFilter.BloomFilter;

import com.dw.party.mbmsupport.dto.Result;
import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.dao.PersonInfoDao;
import com.mbm.elint.entity.util.TypePojo;
import com.mbm.elint.manager.business.UidToPicUrlManager;
import com.mbm.elint.manager.business.UrlToUidManager;
import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.StaticValue;

@Component
@Transactional
public class PersonInfoManager {
	private static Logger logger = LoggerFactory
			.getLogger(PersonInfoManager.class);
	@Autowired
	private PersonInfoDao personInfoDao;
	@Autowired
	private UrlToUidManager uuMapManager;
	@Autowired
	private UidToPicUrlManager upMapManager;

	public PersonInfoDao getPersonInfoDao() {
		return personInfoDao;
	}

	private Result result;

	public Result getResult() {
		return result;
	}

	@Autowired
	public void setResult(Result result) {
		this.result = result;
	}

	@Autowired
	public void setPersonInfoDao(PersonInfoDao personInfoDao) {
		this.personInfoDao = personInfoDao;
	}

	public static long saved_repeat_sina_personInfo_number = 0;
	public static BloomFilter bloomFilter;
	public static BloomFilter check_repeat_sina_personInfo_bloomFilter;
	public static long saved_personInfo_number = 0;
	// 初始化布隆过滤器
	static {
		Object temp_obj = null;
		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil
						.getObj(StringTransform
								.getByteArray(
										StaticValue.sina_personInfo_bloomFilter_file_path,
										StaticValue.charset)));
		if (bloomFilter == null && (temp_obj == null)) {
			bloomFilter = new BloomFilter(
					StaticValue.sina_personInfo_filter_size);
			logger.info("sina personInfo人物过滤器初始化完成");
		} else {
			try {
				bloomFilter = (BloomFilter) temp_obj;
				logger.info("redis--成功加载sina personInfo人物过滤器!");
			} catch (Exception e) {
				logger.info("****加载失败sina personInfo人物过滤器");
				e.printStackTrace();
			}
		}

		temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil
						.getObj(StringTransform
								.getByteArray(
										StaticValue.check_repeat_sina_personInfo_bloomFilter_file_path,
										StaticValue.charset)));
		if (check_repeat_sina_personInfo_bloomFilter == null
				&& (temp_obj == null)) {
			check_repeat_sina_personInfo_bloomFilter = new BloomFilter(
					StaticValue.check_repeat_sina_personInfo_filter_size);
			logger.info("check repeat sina personInfo人物过滤器初始化完成");
		} else {
			try {
				check_repeat_sina_personInfo_bloomFilter = (BloomFilter) temp_obj;
				logger.info("redis--成功加载check repeat sina personInfo人物过滤器!");
			} catch (Exception e) {
				logger.info("****加载失败check repeat sina personInfo人物过滤器");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 专为更新同步锁控制而添加---开始
	 */
	final Lock bloomFilterLock = new ReentrantLock();

	/**
	 * 专为更新同步锁控制而添加---结束
	 */

	public PersonInfo load(String id) {
		try {
			List<PersonInfo> list = this.personInfoDao
					.find("select p from PersonInfo p where p.uid='" + id + "'");
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void update(PersonInfo personInfo) {
		this.personInfoDao.getSession().update(personInfo);
	}

	public void updatePersonInfo(PersonInfo person) {
		person.setUpdateTime(new Date());
		this.personInfoDao.getSession().update(person);
	}

	public boolean savePersonInfo(boolean isAttention, PersonInfo person) {
		String sinaPersonInfoKey = null;
		String sinaKeyInfo = null;
		bloomFilterLock.lock();
		try {
			// 用于sina_personInfo抓取去重
			sinaKeyInfo = person.getName() + person.getUid()
					+ person.getFansNum() + person.getWbNum()
					+ person.getGzNum();
			sinaPersonInfoKey = person.getUid();
			/**
			 * 在这里做下UUMap的缓存,为保完全对应，总是要做一次映射
			 */
			if (!uuMapManager.isInUUMap(person.getUrl())) {
				uuMapManager.addUUMap(person.getUrl(), person.getUid());
			}
			/**
			 * 在这里做下UPMap的缓存,为保完全对应，总是要做一次映射
			 */
			if (TypePojo.isQQ(person.getUrl())
					&& (!upMapManager.isInUPMap(person.getUid()))) {
				upMapManager.addUPMap(person.getUid(), person.getPic_url());
			}

			if (bloomFilter.contains(sinaPersonInfoKey)) {
				saved_repeat_sina_personInfo_number++;
				/**
				 * 说明抓取过，比较是否有变化
				 */
				if (isAttention) {// 如果是关注产生，则一定要更新数据库中的时间
					List<PersonInfo> list = this.personInfoDao
							.find("select p from PersonInfo p where p.uid='"
									+ person.getUid() + "'");
					if (list.size() > 0) {
						PersonInfo oldPerson = list.get(0);
						oldPerson.update(person);
						this.personInfoDao.getSession().update(oldPerson);
					}
					check_repeat_sina_personInfo_bloomFilter.add(sinaKeyInfo);
					logger.info(person.getName()
							+ "-----个人信息已经抓取过了,由于是关注产生的帐号，更新帐号信息的时间后再次保存!");
				} else if (check_repeat_sina_personInfo_bloomFilter
						.contains(sinaKeyInfo)) {
					logger.info(person.getName()
							+ "-----个人信息已经抓取过了,而且此次抓取并没有变化信息，将跳过!");
				} else {
					List<PersonInfo> list = this.personInfoDao
							.find("select p from PersonInfo p where p.uid='"
									+ person.getUid() + "'");
					if (list.size() > 0) {
						PersonInfo oldPerson = list.get(0);
						oldPerson.update(person);
						this.personInfoDao.getSession().update(oldPerson);
					} else {
						this.personInfoDao.save(person);
						bloomFilter.add(sinaPersonInfoKey);
						check_repeat_sina_personInfo_bloomFilter
								.add(sinaKeyInfo);
						saved_personInfo_number++;
					}
					check_repeat_sina_personInfo_bloomFilter.add(sinaKeyInfo);
					logger.info(person.getName()
							+ "-----个人信息已经抓取过了,但其信息出现了变化，已成功更新完成!");
				}
			} else {
				// 说明没有抓取过，直接插入就可以了!
				this.personInfoDao.save(person);
				bloomFilter.add(sinaPersonInfoKey);
				check_repeat_sina_personInfo_bloomFilter.add(sinaKeyInfo);
				saved_personInfo_number++;
			}
			return true;
		} catch (HibernateException e) {
			e.printStackTrace();
			// logger.info("保存个人信息时,出现字符集不兼容问题，暂认为是summary字段出现特殊字符，暂置空");
			// PersonInfo person2=new PersonInfo();
			// person2.update(person);
			// this.personInfoDao.save(person);
			// person.setSummary("简介内容包含特殊字符!");
			// bloomFilter.add(sinaPersonInfoKey);
			// check_repeat_sina_personInfo_bloomFilter.add(sinaKeyInfo);
			// saved_personInfo_number++;
			return false;
		} finally {
			bloomFilterLock.unlock();
		}
	}

	public boolean savePersonInfo4System(PersonInfo person) {
		String sinaPersonInfoKey = null;
		String sinaKeyInfo = null;
		bloomFilterLock.lock();
		try {
			// synchronized (bloomFilter) {
			// 用于sina_personInfo抓取去重
			sinaKeyInfo = person.getName() + person.getUid()
					+ person.getFansNum() + person.getWbNum()
					+ person.getGzNum();
			sinaPersonInfoKey = person.getUid();
			/**
			 * 在这里做下UUMap的缓存,为保完全对应，总是要做一次映射
			 */
			if (!uuMapManager.isInUUMap(person.getUrl())) {
				uuMapManager.addUUMap(person.getUrl(), person.getUid());
			}
			/**
			 * 在这里做下UPMap的缓存,为保完全对应，总是要做一次映射
			 */
			if (TypePojo.isQQ(person.getUrl())
					&& (!upMapManager.isInUPMap(person.getUid()))) {
				upMapManager.addUPMap(person.getUid(), person.getPic_url());
			}

			// 说明没有抓取过，直接插入就可以了!
			this.personInfoDao.save(person);
			bloomFilter.add(sinaPersonInfoKey);
			check_repeat_sina_personInfo_bloomFilter.add(sinaKeyInfo);
			saved_personInfo_number++;
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		} finally {
			bloomFilterLock.unlock();
		}
		return true;
	}
}
