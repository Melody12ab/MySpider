package com.mbm.elint.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

public abstract class SimpleActionSupport extends ActionSupport{

	private static final long serialVersionUID = -1653204626115064950L;

	/** 进行增删改操作后,以redirect方式重新打开action默认页的result名. */
	public static final String RELOAD = "reload";

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public List all = null;

	public Long id = null;
	
	public Object obj = null ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List getAll() {
		return all;
	}
	
	public Object getObj() {
		return obj;
	}

	public HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
}
