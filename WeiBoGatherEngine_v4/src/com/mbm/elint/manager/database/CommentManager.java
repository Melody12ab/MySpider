package com.mbm.elint.manager.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.elint.entity.dao.CommentDao;

@Component
@Transactional
public class CommentManager {
	private static Logger logger = LoggerFactory
			.getLogger(CommentManager.class);

	private CommentDao commentDao;

	public CommentDao getCommentDao() {
		return commentDao;
	}

	@Autowired
	public void setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
	}

}
