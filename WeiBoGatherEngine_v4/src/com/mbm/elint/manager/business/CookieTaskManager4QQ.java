package com.mbm.elint.manager.business;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.weibo.qq.cookie.common.CookieTask4QQ;

@Component
public class CookieTaskManager4QQ {
	private ArrayList<CookieTask4QQ> cookieTasks;

	public ArrayList<CookieTask4QQ> getCookieTasks() {
		return cookieTasks;
	}

	public void setCookieTasks(ArrayList<CookieTask4QQ> cookieTasks) {
		this.cookieTasks = cookieTasks;
	}
}
