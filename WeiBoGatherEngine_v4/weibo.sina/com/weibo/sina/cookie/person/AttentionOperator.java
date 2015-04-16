package com.weibo.sina.cookie.person;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.mbm.elint.entity.util.UrlPojo;
import com.weibo.sina.cookie.common.GrabPageResource;

@Component
@Scope(value = "prototype")
public class AttentionOperator {
	public static Logger logger = Logger.getLogger(AttentionOperator.class);
	private GrabPageResource grabPageSource = null;
	private UrlPojo urlPojo;
	private String uid;// 要关注的人的uid

	/**
	 * 此处的uid为要关该的帐户的uid
	 * 
	 * @param uid
	 * @return
	 */
	public String addAttentionUid(String uid) {
        
		return "";
	}

	public void init(String uid, GrabPageResource grabPageResource) {
		this.uid = uid;// 是登陆用户的uid
		this.grabPageSource = grabPageResource;
	}
	
	
	
}
