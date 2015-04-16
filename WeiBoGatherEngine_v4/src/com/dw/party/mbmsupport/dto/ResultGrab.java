package com.dw.party.mbmsupport.dto;

import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.UrlPojo;

/**
 * 此类是对查询状态时的返回结果的封装
 * 
 * @author zel
 */
@Component
public class ResultGrab extends Result implements java.io.Serializable {
	public ResultGrab(String result, UrlPojo urlPojo) {
		this.result = result;
		this.urlPojo = urlPojo;
	}

	public ResultGrab(String result, KeywordPojo keywordPojo) {
		this.result = result;
		this.keywordPojo = keywordPojo;
	}

	public ResultGrab() {

	}

	/**
	 * 采集结果 不可为空
	 */
	private String result;

	/**
	 * 返回查询的结果的resultGrab的描述 ,其描述值取自BlogRemoteDefine.GRAB_RESULT对象的列举值
	 * 
	 * @return
	 */
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	private KeywordPojo keywordPojo;

	/**
	 * 返回查询的关键词的对象
	 */
	public KeywordPojo getKeywordPojo() {
		return keywordPojo;
	}

	public void setKeywordPojo(KeywordPojo keywordPojo) {
		this.keywordPojo = keywordPojo;
	}

	private static long serialVersionUID = 1234L;
	// 微博用户地址
	// 不能为空
	private UrlPojo urlPojo;

	/**
	 * 返回查询的UrlPojo的对象
	 */
	public UrlPojo getUrlPojo() {
		return urlPojo;
	}

	public void setUrlPojo(UrlPojo urlPojo) {
		this.urlPojo = urlPojo;
	}

	/**
	 * 序列号ID
	 */
}
