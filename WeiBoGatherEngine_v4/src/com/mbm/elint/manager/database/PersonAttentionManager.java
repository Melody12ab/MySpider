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

import com.mbm.elint.dao.HibernateGeneralDao;
import com.mbm.elint.entity.Mutualfans;
import com.mbm.elint.entity.PersonAttention;
import com.mbm.elint.entity.Personfans;
import com.mbm.elint.entity.dao.MutualfansDao;
import com.mbm.elint.entity.dao.PersonAttentionDao;
import com.mbm.elint.entity.dao.PersonFansDao;

@Component
@Transactional
public class PersonAttentionManager {
	private static Logger logger = LoggerFactory
			.getLogger(PersonAttentionManager.class);

	private PersonAttentionDao personAttentionDao;
	private PersonFansDao personFansDao;
	private MutualfansDao mutualfansDao;

	public MutualfansDao getMutualfansDao() {
		return mutualfansDao;
	}

	@Autowired
	public void setMutualfansDao(MutualfansDao mutualfansDao) {
		this.mutualfansDao = mutualfansDao;
	}

	private HibernateGeneralDao<Mutualfans> hibernateGeneralDao;

	public HibernateGeneralDao<Mutualfans> getHibernateGeneralDao() {
		return hibernateGeneralDao;
	}

	@Autowired
	public void setHibernateGeneralDao(
			HibernateGeneralDao<Mutualfans> hibernateGeneralDao) {
		this.hibernateGeneralDao = hibernateGeneralDao;
	}

	public PersonFansDao getPersonFansDao() {
		return personFansDao;
	}

	@Autowired
	public void setPersonFansDao(PersonFansDao personFansDao) {
		this.personFansDao = personFansDao;
	}

	public PersonAttentionDao getPersonAttentionDao() {
		return personAttentionDao;
	}

	@Autowired
	public void setPersonAttentionDao(PersonAttentionDao personAttentionDao) {
		this.personAttentionDao = personAttentionDao;
	}

	public PersonAttention getAttentionPojoByUid(String uid) {
		List<PersonAttention> list = this.personAttentionDao
				.find("select person from PersonAttention person where person.uid='"
						+ uid + "'");
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 专为更新同步锁控制而添加---开始
	 */
	static final Lock synObjLock = new ReentrantLock();
	static final Lock synObjLock4Fans = new ReentrantLock();
	static final Lock synObjLock4Mutualfans = new ReentrantLock();

	/**
	 * 专为更新同步锁控制而添加---结束
	 */

	// 专门为同步而加的静态变量
	// public static Object synObj = new Object();
	public boolean saveOrUpdateAttention(PersonAttention personAttention) {
		synObjLock.lock();
		try {
			// synchronized (synObj) {
			List<PersonAttention> list = this.personAttentionDao
					.find("select person from PersonAttention person where person.uid='"
							+ personAttention.getUid() + "'");
			if (list.size() > 0) {
				PersonAttention oldPerson = list.get(0);
				oldPerson.setAttentionIdList(personAttention
						.getAttentionIdList());
				oldPerson.setAttentionSize(personAttention.getAttentionSize());
				oldPerson.setAttentionTime(new Date());
				this.personAttentionDao.getSession().saveOrUpdate(oldPerson);
			} else {
				personAttention.setAttentionTime(new Date());
				this.personAttentionDao.save(personAttention);
			}
			return true;
			// }
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		} finally {
			synObjLock.unlock();
		}
	}

	public boolean saveOrUpdateFans(Personfans personfans) {
		synObjLock4Fans.lock();
		try {
			List<Personfans> list = this.personFansDao
					.find("select person from Personfans person where person.uid='"
							+ personfans.getUid() + "'");
			if (list.size() > 0) {
				Personfans oldPersonFan = list.get(0);
				oldPersonFan.setFansIdList(personfans.getFansIdList());
				oldPersonFan.setFansSize(personfans.getFansSize());
				oldPersonFan.setUpdateTime(new Date());
				this.personFansDao.getSession().saveOrUpdate(oldPersonFan);
			} else {
				personfans.setUpdateTime(new Date());
				this.personFansDao.save(personfans);
			}
			return true;
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		} finally {
			synObjLock4Fans.unlock();
		}
	}

	public String findAttentionListByUid(String uid) {
		List<PersonAttention> list = this.personAttentionDao
				.find("select person from PersonAttention person where person.uid='"
						+ uid + "'");
		if (list.size() > 0) {
			return list.get(0).getAttentionIdList().replaceAll("#", ",");
		}
		return null;
	}

	public String findFansListByUid(String uid) {
		List<Personfans> list = this.personFansDao
				.find("select person from Personfans person where person.uid='"
						+ uid + "'");
		if (list.size() > 0) {
			return list.get(0).getFansIdList().replaceAll("#", ",");
		}
		return null;
	}

	public String findMutualFansListByUid(String uid) {
		List<Mutualfans> list = this.mutualfansDao
				.find("select person from Mutualfans person where person.uid='"
						+ uid + "'");
		StringBuilder stringBuilder = new StringBuilder();
		for (Mutualfans fans : list) {
			stringBuilder.append(fans.getFid() + ",");
		}
		return stringBuilder.toString();
	}

	public void saveMutualfansList(List<Mutualfans> list) {
		try {
			synObjLock4Mutualfans.lock();
			this.hibernateGeneralDao.saveAll(list);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("保存互粉列表出现问题!");
		} finally {
			synObjLock4Mutualfans.unlock();
		}

	}
}
