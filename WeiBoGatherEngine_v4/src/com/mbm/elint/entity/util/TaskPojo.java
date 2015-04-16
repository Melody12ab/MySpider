package com.mbm.elint.entity.util;

import java.io.File;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * 任务种类的封状，如新浪、腾讯等的区分
 * @author zel
 *
 */
@Component
@Scope(value = "prototype")
public class TaskPojo {
	private String type;
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
