package com.mbm.elint.manager.business;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.ObjectIoUtil;
import com.weibo.common.utils.StaticValue;

@Component
public class UidToPicUrlManager {
	public static Logger logger = Logger.getLogger(UidToPicUrlManager.class);
	public static Map<String, String> UPMap = null;
	static {
		// 初始化UUMap_cache_file_path
		Object temp_obj = ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
						.getByteArray(StaticValue.UUMap_cache_file_path,
								StaticValue.charset)));
		/**
		 * 主要解决QQ的uid到pic url的对应关系
		 */
		if (UPMap == null && temp_obj == null) {
			UPMap = new HashMap<String, String>();
			logger.info("UPMap_cache_file_path初始化完成,此时是空列表!");
		} else {
			try {
				UPMap = (HashMap) temp_obj;
				logger.info("成功加载UPMap_cache_file_path列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载UPMap_cache_file_path重新初始化完成");
			}
		}
	}

	public static int getUPMapSize() {
		return UPMap.size();
	}

	public synchronized void addUPMap(String uid, String sendUrl) {
		UPMap.put(uid, sendUrl);
	}

	public String getValueByKey(String uid) {
		return UPMap.get(uid);
	}

	public boolean isInUPMap(String uid) {
		if (UPMap.containsKey(uid)) {
			return true;
		}
		return false;
	}
}
