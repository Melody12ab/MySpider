package com.mbm.elint.entity.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.mbm.elint.entity.PersonInfo;

@Component
public class PersonInfoDao extends HibernateDao<PersonInfo, Long>  {

}
