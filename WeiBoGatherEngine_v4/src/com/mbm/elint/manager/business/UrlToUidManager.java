package com.mbm.elint.manager.business;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.StaticValue;

@Component
public class UrlToUidManager {
	public static Logger logger = Logger.getLogger(UrlToUidManager.class);
	public static Map<String, String> UUMap = null;
	static {
		Object temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(StaticValue.UUMap_cache_file_path,
								StaticValue.charset)));
		// 初始化UUMap_cache_file_path
		if (UUMap == null && temp_obj == null) {
			UUMap = new HashMap<String, String>();
			logger.info("UUMap_cache_file_path初始化完成,此时是空列表!");
		} else {
			try {
				UUMap = (HashMap) temp_obj;
				logger.info("成功加载UUMap_cache_file_path列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载UUMap_cache_file_path重新初始化完成");
			}
		}
	}

	public static int getUUMapSize() {
		return UUMap.size();
	}

	public void addUUMap(String sendUrl, String uid) {
		UUMap.put(sendUrl, uid);
	}

	public String getValueByKey(String sendUrl) {
		return UUMap.get(sendUrl);
	}

	public boolean isInUUMap(String sendUrl) {
		if (UUMap.containsKey(sendUrl)) {
			return true;
		}
		return false;
	}
}
