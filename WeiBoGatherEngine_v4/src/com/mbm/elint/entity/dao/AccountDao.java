package com.mbm.elint.entity.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.mbm.elint.entity.SinaAccount;

@Component
public class AccountDao extends HibernateDao<SinaAccount, Long>  {

}
