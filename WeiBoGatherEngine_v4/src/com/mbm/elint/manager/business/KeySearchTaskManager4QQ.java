package com.mbm.elint.manager.business;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.weibo.qq.cookie.keywords.KeySearchTask4QQ;

@Component
public class KeySearchTaskManager4QQ {
	private ArrayList<KeySearchTask4QQ> keySearchTasks;

	public ArrayList<KeySearchTask4QQ> getKeySearchTasks() {
		return keySearchTasks;
	}

	public void setKeySearchTasks(ArrayList<KeySearchTask4QQ> keySearchTasks) {
		this.keySearchTasks = keySearchTasks;
	}

}
