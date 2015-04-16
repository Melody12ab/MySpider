package com.mbm.elint.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * Personfans entity. @author MyEclipse Persistence Tools
 */
@Entity
@Component
@Table(name = "personfans")
public class Personfans implements java.io.Serializable {

	// Fields

	private Long id;
	private String uid;
	private String sendUrl;
	private String fansIdList;
	private Integer fansSize;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public Personfans() {
	}

	/** full constructor */
	public Personfans(String uid, String sendUrl, String fansIdList,
			Integer fansSize, Timestamp updateTime) {
		this.uid = uid;
		this.sendUrl = sendUrl;
		this.fansIdList = fansIdList;
		this.fansSize = fansSize;
		this.updateTime = updateTime;
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

	@Column(name = "uid", length = 20)
	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(name = "sendUrl", length = 100)
	public String getSendUrl() {
		return this.sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	@Column(name = "fansIdList")
	public String getFansIdList() {
		return this.fansIdList;
	}

	public void setFansIdList(String fansIdList) {
		this.fansIdList = fansIdList;
	}

	@Column(name = "fansSize")
	public Integer getFansSize() {
		return this.fansSize;
	}

	public void setFansSize(Integer fansSize) {
		this.fansSize = fansSize;
	}

	@Column(name = "updateTime")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}