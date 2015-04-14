package com.cdy.qq.Brain;

import java.util.HashMap;

/**
 * @author dongyu.cai
 * 与各个对象交谈的私有的内存空间
 */
public class PrivateMemory {
	
	
	/**
	 * 短期记忆
	 */
	private HashMap<String,String> shortMemory = new HashMap<String,String>();
	
	
	/**
	 * 根据问题到记忆力找结果
	 */
	public String getMemory(String question){
		return shortMemory.get(question);
	}
	
	/**
	 * 添加我不知道记忆
	 */
	public void addMemoryIDontKnow(String question,String answer){
		shortMemory.put(question, answer);
	}
	
	
	
}
