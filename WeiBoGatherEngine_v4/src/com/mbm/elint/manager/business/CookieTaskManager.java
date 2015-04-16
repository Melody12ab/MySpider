package com.mbm.elint.manager.business;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.weibo.sina.cookie.common.CookieTask;

@Component
public class CookieTaskManager {
	private ArrayList<CookieTask> cookieTasks;

	public ArrayList<CookieTask> getCookieTasks() {
		return cookieTasks;
	}

	public void setCookieTasks(ArrayList<CookieTask> cookieTasks) {
		this.cookieTasks = cookieTasks;
	}


}
