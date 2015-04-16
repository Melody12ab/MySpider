package com.mbm.elint.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * Personattention entity. @author MyEclipse Persistence Tools
 */
@Entity
@Component
@Table(name = "personattention")
public class PersonAttention implements java.io.Serializable {
	// Fields
	private Long id;
	private String uid;
	private String attentionIdList;
	private Integer attentionSize;
	private String attentionUrl;
	private Date attentionTime;

	@Column(name = "attentionTime")
	public Date getAttentionTime() {
		return attentionTime;
	}

	public void setAttentionTime(Date attentionTime) {
		this.attentionTime = attentionTime;
	}

	// Constructors
	/** default constructor */
	public PersonAttention() {
	}

	/** full constructor */
	public PersonAttention(String uid, String attentionIdList,
			Integer attentionSize) {
		this.uid = uid;
		this.attentionIdList = attentionIdList;
		this.attentionSize = attentionSize;
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

	@Column(name = "attentionUrl")
	public String getAttentionUrl() {
		return attentionUrl;
	}

	public void setAttentionUrl(String attentionUrl) {
		this.attentionUrl = attentionUrl;
	}

	@Column(name = "uid", length = 20)
	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(name = "attentionIdList", length = 4096)
	public String getAttentionIdList() {
		return this.attentionIdList;
	}

	public void setAttentionIdList(String attentionIdList) {
		this.attentionIdList = attentionIdList;
	}

	@Column(name = "attentionSize")
	public Integer getAttentionSize() {
		return this.attentionSize;
	}

	public void setAttentionSize(Integer attentionSize) {
		this.attentionSize = attentionSize;
	}

}