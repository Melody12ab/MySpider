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
 * SinaKeysearchinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Component
@Table(name = "sina_keysearchinfo")
public class SinaKeySearchInfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String keyword;
	private Date updateTime;
	private String comment;
	private String type;

	// Constructors
	@Column(name = "type", length = 10)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/** default constructor */
	public SinaKeySearchInfo() {
	}

	/** full constructor */
	public SinaKeySearchInfo(String keyword, Timestamp updateTime,
			String comment) {
		this.keyword = keyword;
		this.updateTime = updateTime;
		this.comment = comment;
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

	@Column(name = "keyword", length = 512)
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Column(name = "update_time", length = 0)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "comment", length = 512)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
    public void updateSearchInfo(SinaKeySearchInfo searchInfo){
    	this.setUpdateTime(searchInfo.getUpdateTime());
    	//如果this.getComment(即数据库保存的)小于searchInfo.getComment()的话，就不作更新
    	if(Integer.parseInt(this.getComment())<=Integer.parseInt(searchInfo.getComment())){
    		this.setComment(searchInfo.getComment());
    	}
    }
}