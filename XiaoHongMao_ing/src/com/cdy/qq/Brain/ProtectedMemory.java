package com.cdy.qq.Brain;

import java.util.HashMap;

/**
 * @author dongyu.cai
 * 与各个对象交谈的私有的内存空间
 */
public class ProtectedMemory {
	
	
	/**
	 * 短期记忆
	 */
	private HashMap<String,String> shortMemory = new HashMap<String,String>();
	
	
	//CST  初始化一些记忆
	{
		shortMemory.put("你是谁?", "我是小红帽~");
		shortMemory.put("你爸爸是谁?", "蔡东余~");
		shortMemory.put("你妈妈呢?", "黄亚红~");
		shortMemory.put("爱我吗?", "炒鸡爱~");
		shortMemory.put("小红帽", "是妈妈吗~");
	}
	
	/**
	 * 根据问题到记忆力找结果
	 */
	public String getMemory(String question){
		return shortMemory.get(question);
	}
	
	/**
	 * 添加我不知道记忆
	 */
	public void saveMemory(String question,String answer){
		shortMemory.put(question, answer);
	}
	
	
	
}
