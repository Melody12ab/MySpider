package com.dw.party.mbmsupport.dto;

import com.mbm.LJSearch.pojo.LJFieldValue;
import com.weibo.common.utils.JsoupManager;

/**
 * 搜索微博内容信息时的返回pojo对象
 * 
 * @author zel
 */
public class SearchContentResultPojo implements java.io.Serializable {
	// @Override
	// public String toString() {
	// return "\"record:{\"commentNum\":\"" + commentNum + "\",\"content\":\""
	// + content + "\", \"convertNum\":\"" + convertNum
	// + "\", \"grabTime\":\"" + grabTime + "\", \"nickName\":"
	// + nickName + ", \"publishTime\":\"" + publishTime
	// + "\", \"uid\":\"" + uid + "\",\"url\":\"" + url + "\"}";
	// // return "record:{commentNum:" + commentNum + ",content:"
	// // + content + ",convertNum:" + convertNum
	// // + ",grabTime:" + grabTime + ",nickName:" + nickName
	// // + ", publishTime:" + publishTime + ",uid:" + uid
	// // + ",url:" + url + "}";
	// }

	/**
	 * 微博内容
	 */
	private String content;
	/**
	 * 发微博的用户的uid
	 */
	private String uid;
	/**
	 * 微博用户的名字,也就是昵称
	 */
	private String nickName;
	/**
	 * 评论数
	 */
	private String commentNum;
	/**
	 * 转发数
	 */
	private String convertNum;
	/**
	 * 发布时间
	 */
	private String publishTime;
	/**
	 * 采集时间
	 */
	private String grabTime;
	/**
	 * 微博内容地址
	 */
	private String url;
	/**
	 * 抓取方式，模拟浏览器方式抓取，即cookies
	 */
	private String grabMethod;
	/**
	 * 代表是否为转发性微博，0代表原创，1代表转发
	 */
	private String isForward;
	/**
	 * 代表微博文章的ID号
	 */
	private String doc_article_id;

	public String getIsForward() {
		return isForward;
	}

	public void setIsForward(String isForward) {
		this.isForward = isForward;
	}

	/**
	 * 为抽取at信息而添加
	 */
	private String atInfo;

	public String getAtInfo() {
		return atInfo;
	}

	public void setAtInfo(String atInfo) {
		this.atInfo = atInfo;
	}

	/**
	 * 若为转发，则该属性代表转发的原博文的docUrl
	 */
	private String forwardDocUrl;

	public String getForwardDocUrl() {
		return forwardDocUrl;
	}

	public void setForwardDocUrl(String forwardDocUrl) {
		this.forwardDocUrl = forwardDocUrl;
	}

	public String getGrabMethod() {
		return grabMethod;
	}

	public void setGrabMethod(String grabMethod) {
		this.grabMethod = grabMethod;
	}

	/**
	 * 发送微博信息的客户端,是新浪微博或是皮皮时光机等微博发布客户端
	 */
	private String origin;

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * 将搜索结果赋值给暂存对象
	 */
	public void setLJFieldValue(LJFieldValue ljFieldValue) {
		this.content = ljFieldValue.getField(1);
		this.content=JsoupManager.getNormalText(this.content);
		this.uid = ljFieldValue.getField(2);
		this.nickName = ljFieldValue.getField(3);
		this.commentNum = ljFieldValue.getField(4);
		this.convertNum = ljFieldValue.getField(5);
		this.publishTime = ljFieldValue.getField(6);
		this.grabTime = ljFieldValue.getField(7);
		this.url = ljFieldValue.getField(8);
		this.grabMethod = ljFieldValue.getField(9);
		this.origin = ljFieldValue.getField(10);
		this.doc_article_id = ljFieldValue.getField(11);
		this.isForward = ljFieldValue.getField(12);
		this.forwardDocUrl = ljFieldValue.getField(13);
		this.atInfo = ljFieldValue.getField(14);
		// String str = "content---" + this.content + "\n" + "uid---" + this.uid
		// + "\n" + "nickName---" + this.nickName + "\n" + "commentNum---"
		// + this.commentNum + "\n" + "convertNum---" + this.convertNum
		// + "\n" + "publishTime---" + this.publishTime + "\n"
		// + "grabTime---" + this.grabTime + "\n" + "url----------"
		// + this.url;
		String str = "content---" + this.content + "\n" + "nickName---"
				+ this.nickName;
		System.out.println(str);
	}

	/**
	 * 得到发布该篇博文的博主的uid
	 */
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * 得到昵称
	 */
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * 得到该篇博文的评论数量
	 */
	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	/**
	 * 得到该篇博文的内容
	 */
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 得到该篇博文的转发数
	 */
	public String getConvertNum() {
		return convertNum;
	}

	public void setConvertNum(String convertNum) {
		this.convertNum = convertNum;
	}

	/**
	 * 得到该篇博文的抓取时间
	 */
	public String getGrabTime() {
		return grabTime;
	}

	public void setGrabTime(String grabTime) {
		this.grabTime = grabTime;
	}

	/**
	 * 得到该篇博文的发布时间
	 */
	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	/**
	 * 得到该篇博文的ID
	 */
	public String getBlogid() {
		return doc_article_id;
	}

	public void setBlogid(String doc_article_id) {
		this.doc_article_id = doc_article_id;
	}

	/**
	 * 此为临时对象，不做存储
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private static long serialVersionUID = 39293232300L;

	public static void main(String[] args) {
		// SearchContentResultPojo pojo=new SearchContentResultPojo();
		// System.out.println(pojo);
	}

}
