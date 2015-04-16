package com.mbm.elint.entity.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.mbm.elint.entity.Group;

@Component
public class GroupDao extends HibernateDao<Group, Long>  {

}
