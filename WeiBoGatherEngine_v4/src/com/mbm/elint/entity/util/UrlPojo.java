package com.mbm.elint.entity.util;

import java.io.Serializable;

import javax.persistence.Transient;

import com.dw.party.mbmsupport.global.TaskLevel;
import com.mbm.elint.entity.PersonInfo;

/**
 * 关于Url任务的封装类
 * 
 * @author zel
 */
public class UrlPojo implements Serializable {
	@Override
	public int hashCode() {
		return 1;
	}

	private int taskLevel = TaskLevel.Default;

	public int getTaskLevel() {
		return taskLevel;
	}

	/**
	 * 设置taskLevel参数，用于周期性任务循环的等级识别中，共分5个等级，从高到低为A、B、C、D、E五级，对应的周期时间分别为10、30、120
	 * 、240、144分钟 该参数只有当提交的是周期任务的时候才有效
	 * 
	 * @param taskLevel
	 */
	public void setTaskLevel(int taskLevel) {
		this.taskLevel = taskLevel;
	}

	/**
	 * 每个url对应着一个person,在此做暂存
	 */
	private PersonInfo person;

	@Transient
	public PersonInfo getPerson() {
		return person;
	}

	public void setPerson(PersonInfo person) {
		this.person = person;
	}

	/**
	 * 用于本次是否在周期循环内的判断
	 */
	private long reGrabBeginTime = 0;

	public long getReGrabBeginTime() {
		return reGrabBeginTime;
	}

	public void setReGrabBeginTime(long reGrabBeginTime) {
		this.reGrabBeginTime = reGrabBeginTime;
	}

	/**
	 * type=0 代表个人信息和内容都抓取 type=1 代表只抓取个人信息 type=2 只抓取微博内容,type=3，只抓取关注列表
	 * type=4代表循环抓取的帐号信息 type=5代表循环抓取的内容信息
	 * 
	 * type=6代表媒体版帐户信息抓取 type=7代表媒体版微言信息抓取 type=8代表媒体版帐户信息抓取--循环
	 * type=9代表媒体版微言信息抓取--循环
	 * 
	 * type=10代表企业版帐户信息抓取 type=11代表企业版微言信息抓取 type=12代表企业版帐户信息抓取--循环
	 * type=13代表企业版微言信息抓取--循环
	 * 
	 * type=14代表企业版帐户信息抓取 type=15代表企业版微言信息抓取 type=16代表企业版帐户信息抓取--循环
	 * type=17代表企业版微言信息抓取--循环
	 * 
	 */
	private int type;

	/**
	 * repeat_count用来代表该url被重抓取几次了，当某url抓取出错后，会首先查看其repeat值，是否达到预定的最大值
	 * 其最大值，取自crawl_control.properties中的值repeat_count
	 */
	private int repeat_count = 0;

	public int getRepeat_count() {
		return repeat_count;
	}

	public void setRepeat_count(int repeatCount) {
		repeat_count = repeatCount;
	}

	/**
	 * 此属性用于区别url的来源，主要用于区别来源于抓取关注所带来的批量帐号抓取 如果为0或1，则代表type的暂存位
	 */
	private String urlSource = "";

	public String getUrlSource() {
		return urlSource;
	}

	/**
	 * 用来标志UrlPojo的来源的不同，如为h开头则是关注抓取产生，其它为非关注产生
	 * @param urlSource
	 */
	public void setUrlSource(String urlSource) {
		this.urlSource = urlSource;
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (obj instanceof UrlPojo) {
	// if (((UrlPojo) obj).getUrlSource().length() > 0) {
	// if (((UrlPojo) obj).getUrlSource().equals(this.getUrlSource()))
	// return true;
	// else {
	// return false;
	// }
	// } else if (((UrlPojo) obj).getType() == this.getType()
	// && ((UrlPojo) obj).getUrl().equals(this.url)
	// && ((UrlPojo) obj).getTaskLevel() == (this.taskLevel)) {
	// return true;
	// }
	// }
	// return false;
	// }
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UrlPojo) {
			UrlPojo pojo = (UrlPojo) obj;
			String temp_source = pojo.getUrlSource();
			if (temp_source.length() > 0) {
				if (temp_source.equals(this.getUrlSource())) {
					if (temp_source.startsWith("h")
							|| pojo.getUrl().equals(this.getUrl())) {
						return true;
					}
				}
				return false;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 构造方法，url代表博主的URL地址,type是指任务类型1代表帐户信息，2代表微博内容，3代表关注，其它系统保留使用
	 * @param url
	 * @param type
	 */
	public UrlPojo(String url, int type) {
		this.url = url;
		this.type = type;
		this.taskLevel=TaskLevel.C;
	}

	// public UrlPojo(String url, int type, long reGrabBeginTime) {
	// this.url = url;
	// this.type = type;
	// this.reGrabBeginTime = reGrabBeginTime;
	// }

	public UrlPojo(String url, int type, long reGrabBeginTime, int taskLevel) {
		this.url = url;
		this.type = type;
		this.reGrabBeginTime = reGrabBeginTime;
		this.taskLevel = taskLevel;
		
	}

	public UrlPojo(String url, int type, int taskLevel) {
		this.url = url;
		this.type = type;
		this.taskLevel = taskLevel;
	}

	public UrlPojo(String url, int type, String urlSource) {
		this.url = url;
		this.type = type;
	}

	public UrlPojo() {

	}

	public UrlPojo(UrlPojo urlPojo) {
		this.setTaskLevel(0);// 先默认个0
		this.setUrl(urlPojo.getUrl());
		this.setType(urlPojo.getType());
		this.setPerson(urlPojo.getPerson());
		this.setUrlSource(urlPojo.getUrlSource());
		this.setReGrabBeginTime(urlPojo.getReGrabBeginTime());
		this.setRepeat_count(urlPojo.getRepeat_count());
	}

	private String url = "";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	// public String toString() {
	// return this.type + this.url + this.urlSource;
	// }
	// 是为查询状态而修改，去掉type
	public String toString() {
		return this.url + this.urlSource;
	}

	public static void main(String args[]) {
		 UrlPojo pojo1 = new UrlPojo("one", 1, 1);
		 UrlPojo pojo2 = new UrlPojo("one", 1, 2);
		 UrlPojo pojo3 = new UrlPojo("one", 1, 3);
	}
}
