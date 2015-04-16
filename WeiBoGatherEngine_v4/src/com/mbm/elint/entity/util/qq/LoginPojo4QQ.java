package com.mbm.elint.entity.util.qq;

import org.apache.http.client.HttpClient;

/**
 * Loginpojo entity. @author MyEclipse Persistence Tools
 */
public class LoginPojo4QQ implements java.io.Serializable {
	private Integer id;
	private String qq;

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	private String password;
	private HttpClient httpClient;

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * status=1 无法获得cookie，一般是指无法登陆 status=2 可以获得cookie status=3
	 * 可以获得cookie,但无法获得accessToken status=4 帐号可以获得accessToken,即一切正常的帐号
	 */
	private String status;
	private String cookie;

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	private String accessToken;

	// Constructors

	/** default constructor */
	public LoginPojo4QQ() {
	}

	public LoginPojo4QQ(String qq, String password) {
		this.qq = qq;
		this.password = password;
	}

	/** minimal constructor */
	public LoginPojo4QQ(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public LoginPojo4QQ(Integer id, String qq, String password, String status) {
		this.id = id;
		this.qq = qq;
		this.password = password;
		this.status = status;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}