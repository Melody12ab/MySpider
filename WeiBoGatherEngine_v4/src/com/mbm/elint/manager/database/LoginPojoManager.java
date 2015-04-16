package com.mbm.elint.manager.database;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.elint.dao.HibernateGeneralDao;
import com.mbm.elint.entity.util.LoginPojo;

@Component
@Transactional
public class LoginPojoManager {
	private static Logger logger = LoggerFactory
			.getLogger(LoginPojoManager.class);
	@Autowired
	private HibernateGeneralDao<LoginPojo> hibernateGeneralDao;

	public LinkedList<LoginPojo> getLoginPojoList(String type) {
		String hql = "select new com.mbm.elint.entity.util.LoginPojo(user.id,user.uid,user.username,user.password,user.status,user.type) from SinaAccount user where user.type='"
				+ type+"' order by user.id asc";
		LinkedList<LoginPojo> list = new LinkedList<LoginPojo>();
		list.addAll(hibernateGeneralDao.find(hql));
		return list;
	}
}
