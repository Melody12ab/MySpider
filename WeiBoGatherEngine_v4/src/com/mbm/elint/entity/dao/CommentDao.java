package com.mbm.elint.entity.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.mbm.elint.entity.Comments;

@Component
public class CommentDao extends HibernateDao<Comments, Long>  {

}
