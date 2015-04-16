package com.mbm.elint.manager.business;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import bloomFilter.BloomFilter;

import com.mbm.util.IOFileUtil;
import com.mbm.util.InsertWordToFileUtil;
import com.sun.jna.Library;
import com.weibo.common.utils.StaticValue;

@Component
public class LibraryManager {
	public static Logger logger = Logger.getLogger(LibraryManager.class);
	public static BloomFilter addNewWordsBloomFilter = new BloomFilter(
			StaticValue.addNewWords_filter_size);
	static {
		// 初始化addNewWordsBloomFilter
		if (!new File(StaticValue.new_dic_file_path).exists()) {
			logger.info("addNewWordsBloomFilter过滤器初始化成功,此时的列表是空的!");
		} else {
			LibraryManager
					.initNewWordBloomFilter(StaticValue.new_dic_file_path);
			logger.info("addNewWordsBloomFilter过滤器初始化成功,将新增词加载进词汇过滤器");
		}
	}

	public static boolean initNewWordBloomFilter(String new_dic_path) {
		try {
			return IOFileUtil.initBloomFilter(addNewWordsBloomFilter,
					new_dic_path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean inserWord(String word) {
		if (!LibraryManager.addNewWordsBloomFilter.contains(word)) {
			// 暂注释
			// Library.insertWord(Library.forest, word);
			InsertWordToFileUtil.insertWord(word);
			LibraryManager.addNewWordsBloomFilter.add(word);
			logger.info(word + "-----添加新词成功!");
			return true;
		}
		return false;
	}

	public void insertWordList(List<String> wordList) {
		// 暂注释
		// Library.insertWordList(Library.forest, wordList);
	}

	public void removeWord(String word) {
		// 暂注释
		// Library.removeWord(Library.forest, word);
	}
}
