package com.mbm.elint.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Group entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "group")
public class Group implements java.io.Serializable {

	// Fields

	private Integer id;
	private String groupName;

	// Constructors

	/** default constructor */
	public Group() {
	}

	/** full constructor */
	public Group(Integer id, String groupName) {
		this.id = id;
		this.groupName = groupName;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "groupName", nullable = false, length = 45)
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}