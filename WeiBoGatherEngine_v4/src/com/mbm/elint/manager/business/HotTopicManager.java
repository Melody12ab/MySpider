package com.mbm.elint.manager.business;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.ObjectIoUtil;
import com.weibo.common.utils.ResultPage;
import com.weibo.common.utils.StaticValue;

@Component
public class HotTopicManager<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger(HotTopicManager.class);
	public static Map<String, ResultPage> topicMap = null;
	static {
		Object temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(StaticValue.hotTopic_cache_file_path,
								StaticValue.charset)));
		// 初始化hotTopic
		if (topicMap == null && temp_obj == null) {
			topicMap = new HashMap<String, ResultPage>();
			logger.info("featureMap初始化完成,此时是空列表!");
		} else {
			try {
				topicMap = (HashMap) temp_obj;
				logger.info("成功加载topicMap列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载topicMap重新初始化完成");
			}
		}
	}

	public static int getTopicMapSize() {
		return topicMap.size();
	}

	public void addTopicToMap(String sendUrl, ResultPage<T> page) {
		topicMap.put(sendUrl, page);
	}

	public boolean isInTopicMap(String sendUrl) {
		if (topicMap.containsKey(sendUrl)) {
			return true;
		}
		return false;
	}

	public ResultPage<T> getTopics(String sendUrl) {
		return topicMap.get(sendUrl);
	}

}
