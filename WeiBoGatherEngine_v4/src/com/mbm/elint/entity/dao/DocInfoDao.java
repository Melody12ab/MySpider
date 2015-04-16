package com.mbm.elint.entity.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.mbm.elint.entity.DocInfo;

@Component
public class DocInfoDao extends HibernateDao<DocInfo, Long>  {
}
