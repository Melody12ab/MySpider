package com.mbm.elint.entity.util;

public class StatusPojo {
	//code=1，代表正常
	//code=2,代表异常，不可分发任务了
	private int code;
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
