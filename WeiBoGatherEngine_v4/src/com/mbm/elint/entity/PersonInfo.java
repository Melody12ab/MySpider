package com.mbm.elint.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "sina_person")
public class PersonInfo implements Serializable {
	private String verifyInfo;

	@Column(name = "verifyInfo")
	public String getVerifyInfo() {
		return verifyInfo;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}

	private String grab_method;

	@Column(name = "grab_method")
	public String getGrab_method() {
		return grab_method;
	}

	public void setGrab_method(String grabMethod) {
		grab_method = grabMethod;
	}

	private String uid;

	@Column(name = "uid")
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public PersonInfo(String uid) {
		this.uid = uid;
	}

	private String tags;

	@Column(name = "tags")
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long id;
	private String url; // 微博地址
	private String name; // 姓名
	private String sex = "0"; // 男，女,0代表性别未知
	private String address; // 家庭地址
	private Integer fansNum; // 粉丝数目

	private String summary; // 介绍
	private Integer wbNum; // 微博数目
	private Integer gzNum; // 关注数

	private String blogUrl; // 博客地址
	private String weiboType;// 微博类型,是teccent,还是sina
	private String realName; // 真实姓名
	private String email; // 邮箱
	private String msn; // MSN
	private String edu; // 教育情况
	private String work; // 工作情况
	private int renZh; // 微博认证
	private String brithday; // 生日
	private String qq; // qq

	private Date updateTime; // 更新时间
	// private Date docUpdateTime; // 原创更新时间
	// private String orAttention;// 临时参数
	private Long uid_long;

	// 博主的帐户类型，暂定义为普通博主、名人类、媒体类、品牌类、政府类、校园类、机构类等不同的博主类
	private String accountType;

	// 在区分自定义的校园版、机构版时有作用,以后会在细认证时用到
	private String verifyType;

	@Transient
	public String getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(String verifyType) {
		this.verifyType = verifyType;
	}

	@Column(name = "accountType")
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Column(name = "uid_long")
	public Long getUid_long() {
		return uid_long;
	}

	public void setUid_long(Long uidLong) {
		uid_long = uidLong;
	}

	public PersonInfo(String url, int type) {
		this.url = url;
	}

	public PersonInfo() {
		this.sex = "0";
	}

	@Column(name = "weiboType")
	public String getWeiboType() {
		return weiboType;
	}

	public void setWeiboType(String weiboType) {
		this.weiboType = weiboType;
	}

	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "name", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "sex", length = 2)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		if (StringUtils.isNotBlank(sex))
			this.sex = sex;
	}

	@Column(name = "brithday", length = 16)
	public String getBrithday() {
		return this.brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	@Column(name = "address", length = 256)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "fansNum")
	public Integer getFansNum() {
		return this.fansNum;
	}

	public void setFansNum(Integer fansNum) {
		if (fansNum != null)
			this.fansNum = fansNum;
	}

	@Column(name = "summary", length = 256)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Column(name = "wbNum")
	public Integer getWbNum() {
		return this.wbNum;
	}

	public void setWbNum(Integer wbNum) {
		this.wbNum = wbNum;
	}

	@Column(name = "gzNum")
	public Integer getGzNum() {
		return this.gzNum;
	}

	public void setGzNum(Integer gzNum) {
		this.gzNum = gzNum;
	}

	@Column(name = "blogUrl", length = 64)
	public String getBlogUrl() {
		return this.blogUrl;
	}

	public void setBlogUrl(String blog) {
		this.blogUrl = blog;
	}

	@Column(name = "realName", length = 32)
	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Column(name = "email", length = 64)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "qq", length = 12)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "msn", length = 64)
	public String getMsn() {
		return this.msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	@Column(name = "edu", length = 256)
	public String getEdu() {
		return this.edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	@Column(name = "work", length = 256)
	public String getWork() {
		return this.work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	@Column(name = "renZh")
	public int getRenZh() {
		return this.renZh;
	}

	/**
	 * 认证信息
	 * 
	 * @param renZh
	 */
	public void setRenZh(int renZh) {
		if (renZh > 0)
			this.renZh = renZh;
	}

	@Column(name = "updateTime")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	// public void setDocUpdateTime(Date docUpdateTime) {
	// this.docUpdateTime = docUpdateTime;
	// }
	//
	// @Column(name = "docUpdateTime")
	// public Date getDocUpdateTime() {
	// return docUpdateTime;
	// }

	private String pic_url;// 暂存图片地址

	@Transient
	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String picUrl) {
		pic_url = picUrl;
	}

	private String findPerson;// 找人

	public void setFindPerson(String findPerson) {
		this.findPerson = findPerson;
	}

	@Transient
	public String getFindPerson() {
		return findPerson;
	}

	public void update(PersonInfo entity) {
		if (StringUtils.isNotBlank(entity.getUrl())) {
			this.url = entity.url;
		}
		if (StringUtils.isNotBlank(entity.getName())) {
			this.name = entity.name;
		}

		if (StringUtils.isNotBlank(entity.getSex())) {
			this.sex = entity.sex;
		}

		if (StringUtils.isNotBlank(entity.getAddress())) {
			this.address = entity.address;
		}
		if (fansNum != null && fansNum > 0) {
			this.fansNum = entity.fansNum;
		}
		if (StringUtils.isNotBlank(entity.getSummary())) {
			this.summary = entity.summary;
		}
		if (wbNum != null && wbNum > 0) {
			this.wbNum = entity.wbNum;
		}
		if (gzNum != null && gzNum > 0) {
			this.gzNum = entity.gzNum;
		}
		if (StringUtils.isNotBlank(entity.getBlogUrl())) {
			this.blogUrl = entity.getBlogUrl();
		}
		if (StringUtils.isNotBlank(entity.getRealName())) {
			this.realName = entity.realName;
		}
		if (StringUtils.isNotBlank(entity.getEmail())) {
			this.email = entity.email;
		}
		if (StringUtils.isNotBlank(entity.getMsn())) {
			this.msn = entity.msn;
		}
		if (StringUtils.isNotBlank(entity.getEdu())) {
			this.edu = entity.edu;
		}
		if (StringUtils.isNotBlank(entity.getWork())) {
			this.work = entity.work;
		}
		if (renZh > 0) {
			this.renZh = entity.renZh;
		}
		if (StringUtils.isNotBlank(entity.getBrithday())) {
			this.brithday = entity.brithday;
		}
		if (StringUtils.isNotBlank(entity.getQq())) {
			this.qq = entity.qq;
		}
		this.updateTime = new Date();
		// this.docUpdateTime = entity.getDocUpdateTime();
	}

	@Override
	public String toString() {
		return "PersonInfo [accountType=" + accountType + ", address="
				+ address + ", blogUrl=" + blogUrl + ", brithday=" + brithday
				+ ", domain_id=" + domain_id + ", edu=" + edu + ", email="
				+ email + ", fansNum=" + fansNum + ", findPerson=" + findPerson
				+ ", grab_method=" + grab_method + ", gzNum=" + gzNum + ", id="
				+ id + ",  msn=" + msn + ", name=" + name + ", pic_url="
				+ pic_url + ", qq=" + qq + ", realName=" + realName
				+ ", renZh=" + renZh + ", sex=" + sex + ", summary=" + summary
				+ ", tags=" + tags + ", uid=" + uid + ", uid_long=" + uid_long
				+ ", updateTime=" + updateTime + ", url=" + url
				+ ", verifyInfo=" + verifyInfo + ", verifyType=" + verifyType
				+ ", wbNum=" + wbNum + ", weiboType=" + weiboType + ", work="
				+ work + "]";
	}

	@Transient
	private String domain_id;

	@Transient
	public String getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(String domainId) {
		domain_id = domainId;
	}

}
