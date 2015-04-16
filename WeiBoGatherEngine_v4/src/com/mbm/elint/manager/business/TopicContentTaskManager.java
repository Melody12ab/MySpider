package com.mbm.elint.manager.business;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.weibo.sina.cookie.topic.content.TopicContentTask;

@Component
public class TopicContentTaskManager {
	private ArrayList<TopicContentTask> topicContentTasks;

	public ArrayList<TopicContentTask> getTopicContentTasks() {
		return topicContentTasks;
	}

	public void setTopicContentTasks(ArrayList<TopicContentTask> topicContentTasks) {
		this.topicContentTasks = topicContentTasks;
	}
}
