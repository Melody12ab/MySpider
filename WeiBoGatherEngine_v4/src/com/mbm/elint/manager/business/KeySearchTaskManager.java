package com.mbm.elint.manager.business;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.weibo.sina.cookie.keywords.KeySearchTask;

@Component
public class KeySearchTaskManager {
	private ArrayList<KeySearchTask> keySearchTasks;

	public ArrayList<KeySearchTask> getKeySearchTasks() {
		return keySearchTasks;
	}

	public void setKeySearchTasks(ArrayList<KeySearchTask> keySearchTasks) {
		this.keySearchTasks = keySearchTasks;
	}


}
