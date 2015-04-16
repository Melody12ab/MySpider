package com.mbm.util;

import java.util.List;

import com.weibo.common.utils.StaticValue;

public class InsertWordToFileUtil {
	public static String new_dic_file_path = StaticValue.new_dic_file_path;

	public static void insertWord(String word) {
		try {
			IOFileUtil.writeData(new_dic_file_path, word);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("插入新词时出现错误!");
		}
	}
	
	public static void insertWordList(List<String> wordList) {
		try {
			IOFileUtil.writeDataList(new_dic_file_path, wordList);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("插入新词时出现错误!");
		}
	}
	
}
