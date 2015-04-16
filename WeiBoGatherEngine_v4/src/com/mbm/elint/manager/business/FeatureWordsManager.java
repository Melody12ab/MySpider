package com.mbm.elint.manager.business;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mbm.util.JedisOperatorUtil;
import com.mbm.util.ObjectAndByteArrayConvertUtil;
import com.mbm.util.StringTransform;
import com.weibo.common.utils.ResultPage;
import com.weibo.common.utils.StaticValue;

@Component
public class FeatureWordsManager<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger(FeatureWordsManager.class);
	public static Map<String, ResultPage> featureMap = null;
	static {
		Object temp_obj = ObjectAndByteArrayConvertUtil
		.ByteArrayToObject(JedisOperatorUtil.getObj(StringTransform
				.getByteArray(StaticValue.featureWords_cache_file_path,
						StaticValue.charset)));
		// 初始化featureMap
		if (featureMap == null && temp_obj == null) {
			featureMap = new HashMap<String, ResultPage>();
			logger.info("featureMap初始化完成,此时是空列表!");
		} else {
			try {
				featureMap = (HashMap) temp_obj;
				logger.info("成功加载featureMap列表!");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("****失败加载featureMap重新初始化完成");
			}
		}
	}

	public static int getFeatureMapSize() {
		return featureMap.size();
	}

	public void addFeatureMap(String sendUrl, ResultPage<T> page) {
		featureMap.put(sendUrl, page);
	}
	
	public boolean isInFeatureMap(String sendUrl) {
		if (featureMap.containsKey(sendUrl)) {
			return true;
		}
		return false;
	}

	public ResultPage<T> getOneFeature(String sendUrl) {
		return featureMap.get(sendUrl);
	}

	public static void main(String[] args) {
		// FeatureWordsManager<FeatureWord> fm = new
		// FeatureWordsManager<FeatureWord>();
		// ResultPage<FeatureWord> page1 = new ResultPage<FeatureWord>(3);
		//
		// ArrayList<FeatureWord> list1 = new ArrayList<FeatureWord>();
		// FeatureWord word1 = new FeatureWord("one", 1);
		// FeatureWord word11 = new FeatureWord("one1", 2);
		// list1.add(word1);
		// list1.add(word11);
		// page1.setResult(list1);
		// fm.addFeatureMap("1", page1);
		//
		// page1 = new ResultPage<FeatureWord>(3);
		// ArrayList<FeatureWord> list2 = new ArrayList<FeatureWord>();
		// FeatureWord word2 = new FeatureWord("two", 2);
		// list2.add(word2);
		// page1.setResult(list2);
		//
		// fm.addFeatureMap("2", page1);
		//
		// ResultPage<FeatureWord> all = (ResultPage<FeatureWord>) fm
		// .getOneFeature("1");
		// System.out.println("value 1 totalCount----------------"
		// + all.getResult().size());

	}

}
