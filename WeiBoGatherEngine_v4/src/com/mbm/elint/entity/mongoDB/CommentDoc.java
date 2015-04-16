package com.mbm.elint.entity.mongoDB;

import java.util.Date;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document(collection = "comments")
public class CommentDoc {
	@Override
	public String toString() {
		return "CommentDoc [commentUid=" + commentUid + ", commentUsername="
				+ commentUsername + ", commentUserPic=" + commentUserPic
				+ ", content=" + content + ", docUrl=" + docUrl + ", grabTime="
				+ grabTime + ", mid=" + mid + ", publishTime=" + publishTime
				+ "]";
	}

	private String docUrl;
	private Date publishTime;
	private Date grabTime;
	private String content;
	private String commentUid;
	private String commentUsername;

	public String getCommentUsername() {
		return commentUsername;
	}

	public void setCommentUsername(String commentUsername) {
		this.commentUsername = commentUsername;
	}

	@Transient
	private String commentUserPic;
	private String mid;

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
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

	public String getCommentUid() {
		return commentUid;
	}

	public void setCommentUid(String commentUid) {
		this.commentUid = commentUid;
	}

	public String getCommentUserPic() {
		return commentUserPic;
	}

	public void setCommentUserPic(String commentUserPic) {
		this.commentUserPic = commentUserPic;
	}

}
