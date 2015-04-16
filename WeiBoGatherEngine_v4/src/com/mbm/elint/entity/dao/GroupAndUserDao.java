package com.mbm.elint.entity.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.mbm.elint.entity.GroupAndPerson;

@Component
public class GroupAndUserDao extends HibernateDao<GroupAndPerson, Long> {

}
