package com.dw.party.mbmsupport.dto;

import com.mbm.LJSearch.pojo.LJFieldInterFace;
import com.mbm.LJSearch.pojo.LJFieldValue;

/**
 * 搜索微博用户或关注用户都用些pojo类
 * 
 * @author zel
 * 
 */
public class SearchAccountResultPojo implements java.io.Serializable,
		LJFieldInterFace {
	/**
	 * 微博用户的uid
	 */
	private String uid;
	/**
	 * 微博用户地址，不能为空
	 */
	private String url;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 用户的地址信息，不能为空
	 */
	private String areaName;
	/**
	 * 粉丝数
	 */
	private String fansNum;
	/**
	 * 简介,相当于summary
	 */
	private String intro;
	/**
	 * 发表的微博数目
	 */
	private String wbNum;
	/**
	 * 关注数
	 */
	private String attentionNum;
	
	private String blogType;
	/**
	 * 取得博客类型,1代表新浪，2代表腾讯
	 */
	public String getBlogType() {
		return blogType;
	}
	/**
	 * 设置博客类型,1代表新浪，2代表腾讯
	 */
	public void setBlogType(String blogType) {
		this.blogType = blogType;
	}

	
	private String blogUrl;
	/**
	 * 获取博客地址
	 */
	public String getBlogUrl() {
		return blogUrl;
	}
	/**
	 * 设置博客地址
	 */
	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}

	/**
	 * 特征,暂为null
	 */
	private String feature;
	/**
	 * 照片地址
	 */
	private String photoUrl;
	/**
	 * 用来将搜索到的条目值赋值给相应的接收对象
	 */
	public void setLJFieldValue(LJFieldValue ljFieldValue) {
		this.uid = ljFieldValue.getField(1);
		this.url = ljFieldValue.getField(2);
		this.nickName = ljFieldValue.getField(3);
		this.sex = ljFieldValue.getField(4);
		this.areaName = ljFieldValue.getField(5);
		this.fansNum = ljFieldValue.getField(6);
		this.intro = ljFieldValue.getField(7);
		this.wbNum = ljFieldValue.getField(8);
		this.attentionNum = ljFieldValue.getField(9);
		this.feature = null;
//		this.blogType = "1";
//		this.photoUrl = "http://tp3.sinaimg.cn/" + uid + "/50/5601300688/";
	}
	/**
	 * 获取该微博帐户的uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * 设置微博帐户的uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * 获取该博主的地址
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置博主的地址
	 * @return
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	/**
	 * 获取该博主的关注数
	 */
	public String getAttentionNum() {
		return attentionNum;
	}
	/**
	 * 设置博主的关注数
	 */
	public void setAttentionNum(String attentionNum) {
		this.attentionNum = attentionNum;
	}

	/**
	 * 得到博主的微博数量
	 */
	public String getWbNum() {
		return wbNum;
	}
	public void setWbNum(String wbNum) {
		this.wbNum = wbNum;
	}
	/**
	 * 得到博主的粉丝数
	 */
	public String getFansNum() {
		return fansNum;
	}

	public void setFansNum(String fansNum) {
		this.fansNum = fansNum;
	}

	/**
	 * 得到用户特征，暂为null,没有此字段
	 */
	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	/**
	 * 得到博主的个人介绍
	 */
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	/**
	 * 得到博主的昵称
	 */
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * 得到用户的照片地址，此为合成字符串.
	 */
	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	/**
	 * 得到博主的性别
	 */
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * 得到博主的微博URL
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private long serialVersionUID = 9393938382L;

}
