package com.mbm.elint.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "doc")
public class DocInfo implements Serializable {


	@Override
	public String toString() {
		return "DocInfo [article=" + article + ", author=" + author
				+ ", discuss=" + discuss + ", docArticleId=" + docArticleId
				+ ", docUrl=" + docUrl + ", extAt=" + extAt
				+ ", forwardDocUrl=" + forwardDocUrl + ", grab_method="
				+ grab_method + ", id=" + id + ", insertTime=" + insertTime
				+ ", isForward=" + isForward + ", origin=" + origin
				+ ", personId=" + personId + ", person_id_long="
				+ person_id_long + ", publishtime=" + publishtime
				+ ", sendUrl=" + sendUrl + ", sinaDocParam=" + sinaDocParam
				+ ", transmit=" + transmit + ", weiboType=" + weiboType + "]";
	}

	private Long id;
	private String grab_method;
	private Date publishtime; // 发布时间
	private String origin; // 来源
	private Integer discuss; // 评论
	private String article; // 文章
	private Integer transmit; // 转发数
	private Integer isForward; // 是否转发了别的微博，0代表没有转发，1代表该博文转发了其它博主的微博
	private String personId;
	private Date insertTime;
	private String author;
	private Integer weiboType;
	private String docArticleId;// 腾讯文章id

	private String sinaDocParam;
	private String sendUrl;
	private String docUrl;
	private String forwardDocUrl;//该微博转发的哪个原微博的url
	private String extAt;
	@Column(name = "extAt")
	public String getExtAt() {
		return extAt;
	}

	public void setExtAt(String extAt) {
		this.extAt = extAt;
	}

	@Column(name = "forwardDocUrl")
	public String getForwardDocUrl() {
		return forwardDocUrl;
	}

	public void setForwardDocUrl(String forwardDocUrl) {
		this.forwardDocUrl = forwardDocUrl;
	}

	//新加，做索引的外键关联
	private Long person_id_long;
	@Column(name = "person_id_long")
	public Long getPerson_id_long() {
		return person_id_long;
	}

	public void setPerson_id_long(Long personIdLong) {
		person_id_long = personIdLong;
	}

	@Column(name = "docUrl")
	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	@Column(name = "sendUrl")
	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	@Column(name = "grab_method")
	public String getGrab_method() {
		return grab_method;
	}

	public void setGrab_method(String grabMethod) {
		grab_method = grabMethod;
	}

	@Column(name = "publishtime")
	public Date getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(Date publishtime) {
		this.publishtime = publishtime;
	}

	@Column(name = "doc_article_id")
	public String getDocArticleId() {
		return docArticleId;
	}

	public void setDocArticleId(String docArticleId) {
		this.docArticleId = docArticleId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "origin")
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name = "discuss")
	public Integer getDiscuss() {
		return discuss;
	}

	public void setDiscuss(Integer discuss) {
		this.discuss = discuss;
	}
	
	@Column(name = "isForward")
	public Integer getIsForward() {
		return isForward;
	}

	public void setIsForward(Integer isForward) {
		this.isForward = isForward;
	}
	
	@Column(name = "article")
	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	@Column(name = "transmit")
	public Integer getTransmit() {
		return transmit;
	}

	public void setTransmit(Integer transmit) {
		this.transmit = transmit;
	}

	@Column(name = "person_id")
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Column(name = "insertTime")
	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "author")
	public String getAuthor() {
		return author;
	}

	// @Column(name = "search_id")
	// public Integer getSearchId() {
	// return searchId;
	// }
	//
	// public void setSearchId(Integer searchId) {
	// this.searchId = searchId;
	// }
	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "weibo_type")
	public Integer getWeiboType() {
		return weiboType;
	}

	public void setWeiboType(Integer weiboType) {
		this.weiboType = weiboType;
	}

	@Column(name = "sina_doc_param")
	public String getSinaDocParam() {
		return sinaDocParam;
	}

	public void setSinaDocParam(String sinaDocParam) {
		this.sinaDocParam = sinaDocParam;
	}
}
