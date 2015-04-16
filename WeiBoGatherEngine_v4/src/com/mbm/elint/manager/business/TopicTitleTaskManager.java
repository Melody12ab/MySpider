package com.mbm.elint.manager.business;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.weibo.sina.cookie.topic.title.TopicTitleTask;

@Component
public class TopicTitleTaskManager {
	private ArrayList<TopicTitleTask> topicTitleTasks;

	public ArrayList<TopicTitleTask> getTopicTitleTasks() {
		return topicTitleTasks;
	}

	public void setTopicTitleTasks(ArrayList<TopicTitleTask> topicTitleTasks) {
		this.topicTitleTasks = topicTitleTasks;
	}

}
