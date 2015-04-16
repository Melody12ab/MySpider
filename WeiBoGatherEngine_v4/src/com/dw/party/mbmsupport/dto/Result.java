package com.dw.party.mbmsupport.dto;

import org.springframework.stereotype.Component;

/**
 * 此类为客户端向服务器添加抓取任务的返回结果对象
 * 
 * @author zel
 */
@Component
public class Result implements java.io.Serializable {
	/**
	 * 可以为空 该结果信息在实际使用时可以为空或是对结果编号做出补充
	 */
	protected String info;

	/**
	 * 得取返回的结果对象的info，是对返回结果的code的描述
	 */
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * 不能为空,代表返回的结果的状态,分为0,1
	 */
	protected String resultCode;

	/**
	 * 得取返回的结果对象的code码，为success或是fault
	 */
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * 序列号ID
	 */
	private static long serialVersionUID = 123456789L;

}
