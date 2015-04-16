package com.mbm.elint.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * Mutualfans entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mutualfans")
@Component
public class Mutualfans implements java.io.Serializable {

	// Fields

	private Long id;
	private String uid;
	private String fid;
	private Date updatetime;

	// Constructors

	/** default constructor */
	public Mutualfans() {
	}

	/** full constructor */
	public Mutualfans(String uid, String fid, Date updatetime) {
		this.uid = uid;
		this.fid = fid;
		this.updatetime = updatetime;
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

	@Column(name = "uid", length = 100)
	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(name = "fid", length = 100)
	public String getFid() {
		return this.fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	@Column(name = "updatetime")
	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

}