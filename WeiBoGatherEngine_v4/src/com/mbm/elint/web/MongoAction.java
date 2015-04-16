package com.mbm.elint.web;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.Assert;

import com.mbm.elint.entity.dao.mongoDB.CommentDocDao;
import com.mbm.elint.entity.mongoDB.CommentDoc;

@Namespace("/")
@Results( { @Result(name = "success", location = "index.jsp"),
		@Result(name = "addTask", location = "addTask.jsp") })
public class MongoAction extends SimpleActionSupport {
	private static Logger logger = Logger.getLogger(MongoAction.class);
	@Autowired
	private CommentDoc commentDoc;
	@Autowired
	private CommentDocDao commentDao;

	public String insert() {
		// System.out.println("mongoTemplate----" + mongoTemplate);
		// mongoTemplate.getDb().authenticate("zel","zhouking".toCharArray());

//		commentDoc.setCommentUser("zel");

		commentDao.save(commentDoc);

		return "success";
	}

	public static void main(String[] args) {
		String str = " ";
		// Assert.hasLength(str);
		Assert.hasLength(str, "str字符串长度不对");
	}
}
