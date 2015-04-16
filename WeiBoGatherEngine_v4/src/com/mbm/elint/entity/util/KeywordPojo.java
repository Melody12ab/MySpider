package com.mbm.elint.entity.util;

import java.io.Serializable;

/**
 * 提交关键词相关任务的pojo类
 * 
 * @author zel
 * 
 */
public class KeywordPojo implements Serializable {
	/**
	 * set中删除对象数据,要hashCode和equals都相同
	 */
	@Override
	public int hashCode() {
		return 1;
	}

	/**
	 * 用于本次是否在周期循环内的判断
	 */
	private long reGrabBeginTime = 0;
	/**
	 * 记录重复抓取的次数
	 */
	private int repeat_count = 0;

	public int getRepeat_count() {
		return repeat_count;
	}

	public void setRepeat_count(int repeatCount) {
		repeat_count = repeatCount;
	}

	public long getReGrabBeginTime() {
		return reGrabBeginTime;
	}

	public void setReGrabBeginTime(long reGrabBeginTime) {
		this.reGrabBeginTime = reGrabBeginTime;
	}

	/**
	 * 1代表此时的抓取是周期性执行的抓取 0代表首次提交
	 */
	private int type = 0;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KeywordPojo) {
			// 因为前后有key的变动，故不作type对于关键词的相等判断
			if (((KeywordPojo) obj).getKeyWord().trim().equals(
					this.keyWord.trim())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * keyword：关键词 type:代表是关键词抓取时的类型，0代表周期抓取，2代表一次性关键词任务抓取，1做系统保留用
	 * 
	 * @param keyword
	 * @param type
	 */
	public KeywordPojo(String keyword, int type) {
		this.keyWord = keyword;
		this.type = type;
	}

	public KeywordPojo() {

	}

	private String keyWord;

	/**
	 * 获取pojo对象中的keyword串
	 * 
	 * @return
	 */
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public int getType() {
		return type;
	}

	/**
	 * 设置keywordPojo对象的type属性 type=0代表周期抓取， type=2代表一次性抓取， type=1系统保留使用
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String toString() {
		return this.keyWord;
	}
}
