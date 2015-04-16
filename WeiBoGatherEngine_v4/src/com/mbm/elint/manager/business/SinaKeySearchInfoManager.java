package com.mbm.elint.manager.business;

import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.elint.entity.SinaKeySearchInfo;
import com.mbm.elint.entity.dao.SinaKeySearchInfoDao;

@Component
@Transactional
public class SinaKeySearchInfoManager {
	private static Logger logger = LoggerFactory
			.getLogger(SinaKeySearchInfoManager.class);

	private SinaKeySearchInfoDao sinaKeySearchInfoDao;

	public SinaKeySearchInfoDao getSinaKeySearchInfoDao() {
		return sinaKeySearchInfoDao;
	}

	@Autowired
	public void setSinaKeySearchInfoDao(
			SinaKeySearchInfoDao sinaKeySearchInfoDao) {
		this.sinaKeySearchInfoDao = sinaKeySearchInfoDao;
	}

	public static Object synObj = new Object();
	public boolean saveKeySearchInfo(SinaKeySearchInfo sinaSearchInfo) {
		try {
			synchronized (synObj) {
				List<SinaKeySearchInfo> list = this.sinaKeySearchInfoDao
						.find("select p from SinaKeySearchInfo p where p.keyword='"
								+ sinaSearchInfo.getKeyword() + "'");
				if (list.size() > 0) {
					SinaKeySearchInfo dbResult = list.get(0);
					dbResult.updateSearchInfo(sinaSearchInfo);
					this.sinaKeySearchInfoDao.getSession().saveOrUpdate(
							dbResult);
				} else {
					this.sinaKeySearchInfoDao.save(sinaSearchInfo);
				}
				return true;
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
	}
}
