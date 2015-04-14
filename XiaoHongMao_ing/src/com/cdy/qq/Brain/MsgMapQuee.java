package com.cdy.qq.Brain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dongyu.cai@renren-inc.com
 * 消息緩存隊列
 * @param <T>
 */
public class MsgMapQuee<T>{
	
	private HashMap<String,ArrayList<T>> msgQuee = new HashMap<String,ArrayList<T>>();
	
	private ReentrantLock lock = new ReentrantLock(false);
	
	public void pushMsg(String key,T msg){
		key = key==null?"":key.trim();
		if("".equals(key)){
			System.out.println("!!!!!!!!!!!空key消息，未緩存!!!!!!!!!");
			return;
		}
		ArrayList<T> quee = null;
		lock.lock();
		if(msgQuee.get(key) != null){
			quee = msgQuee.get(key);
		}else{
			quee = new ArrayList<T>();
			msgQuee.put(key, quee);
		}
		quee.add(msg);
			System.out.println("+++++++++++有來自:"+key+"的消息，已緩存+++++++");
		lock.unlock();
	}
	
	public ArrayList<T> getMsg(String friendId){
		if(friendId == null || friendId.trim().equals("")){
			return null;
		}else{
			friendId = friendId.trim();
		}
		ArrayList<T> returnQuee = null;
		lock.lock();
		returnQuee = msgQuee.get(friendId);
		if(returnQuee == null){
			returnQuee = new ArrayList<T>();
		}else{
			System.out.println("+++++++++++讀取:"+friendId+"的消息，共["+returnQuee.size()+"]条，缓存已更新+++++++");
			msgQuee.remove(friendId);
		}
		lock.unlock();
		return returnQuee;
	}
	
}
