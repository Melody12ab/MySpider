package com.mbm.elint.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Groupanduser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "groupandperson")
public class GroupAndPerson implements java.io.Serializable {

	// Fields

	private long id;
	private String groupName;
	private String userUrl;

	private Long url_id_long;

	// Constructors
	@Column(name = "url_id_long")
	public Long getUrl_id_long() {
		return url_id_long;
	}

	public void setUrl_id_long(Long urlIdLong) {
		url_id_long = urlIdLong;
	}

	/** default constructor */
	public GroupAndPerson() {
	}

	/** full constructor */
	public GroupAndPerson(String groupName, String userUrl) {
		this.groupName = groupName;
		this.userUrl = userUrl;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "groupName", nullable = false, length = 50)
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "userUrl", nullable = false, length = 50)
	public String getUserUrl() {
		return this.userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

}