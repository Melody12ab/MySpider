package com.mbm.elint.entity;

// default package

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Comments entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "comments", catalog = "weibospider")
public class Comments implements java.io.Serializable {

	// Fields

	private Long id;
	private String commentUid;
	private String commentUsername;
	private String docurl;
	@Override
	public String toString() {
		return "Comments [commentUid=" + commentUid + ", commentUsername="
				+ commentUsername + ", content=" + content + ", docurl="
				+ docurl + ", grabtime=" + grabtime + ", id=" + id + ", mid="
				+ mid + ", publishtime=" + publishtime + "]";
	}

	private String content;
	private Date grabtime;
	private Date publishtime;
	private String mid;

	// Constructors

	/** default constructor */
	public Comments() {
	}

	/** full constructor */
	public Comments(String commentUid, String commentUsername, String docurl,
			String content, Timestamp grabtime, Timestamp publishtime,
			String mid) {
		this.commentUid = commentUid;
		this.commentUsername = commentUsername;
		this.docurl = docurl;
		this.content = content;
		this.grabtime = grabtime;
		this.publishtime = publishtime;
		this.mid = mid;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "commentUid")
	public String getCommentUid() {
		return this.commentUid;
	}

	public void setCommentUid(String commentUid) {
		this.commentUid = commentUid;
	}

	@Column(name = "commentUsername")
	public String getCommentUsername() {
		return this.commentUsername;
	}

	public void setCommentUsername(String commentUsername) {
		this.commentUsername = commentUsername;
	}

	@Column(name = "docurl")
	public String getDocurl() {
		return this.docurl;
	}

	public void setDocurl(String docurl) {
		this.docurl = docurl;
	}

	@Column(name = "content", length = 500)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "grabtime", length = 19)
	public Date getGrabtime() {
		return this.grabtime;
	}

	public void setGrabtime(Date grabtime) {
		this.grabtime = grabtime;
	}

	@Column(name = "publishtime", length = 19)
	public Date getPublishtime() {
		return this.publishtime;
	}

	public void setPublishtime(Date publishtime) {
		this.publishtime = publishtime;
	}

	@Column(name = "mid")
	public String getMid() {
		return this.mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

}