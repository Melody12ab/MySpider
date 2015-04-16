package com.mbm.elint.entity.mongoDB;

import java.io.Serializable;
import java.util.Date;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document(collection = "topic")
public class TopicDoc implements Serializable {
	@Override
	public String toString() {
		return "TopicDoc [content=" + content + ", grabTime=" + grabTime
				+ ", oneHourHotCount=" + oneHourHotCount + ", publishTime="
				+ publishTime + ", topicUrl=" + topicUrl + "]";
	}

	private Date publishTime;
	private int oneHourHotCount;
	private Date grabTime;
	private String content;
	private String topicUrl;

	public String getTopicUrl() {
		return topicUrl;
	}

	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}

	public int getOneHourHotCount() {
		return oneHourHotCount;
	}

	public void setOneHourHotCount(int oneHourHotCount) {
		this.oneHourHotCount = oneHourHotCount;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Date getGrabTime() {
		return grabTime;
	}

	public void setGrabTime(Date grabTime) {
		this.grabTime = grabTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
