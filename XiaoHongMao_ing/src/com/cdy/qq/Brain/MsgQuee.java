package com.cdy.qq.Brain;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.caidongyu.application.log.LogUtil;

/**
 * @author dongyu.cai@renren-inc.com
 * 消息队列
 */
public class MsgQuee<T> {
	
	/**
	 * 小红帽的消息队列
	 */
	private ArrayList<T> msgQueeIn = new ArrayList<T>();
	private ArrayList<T> msgQueeOut = new ArrayList<T>();
	
	private ReentrantLock lock = new ReentrantLock(false);
	private int index = 0;//与msgQueeOut关联
	
	public void pushMsg(T msg){
		lock.lock();
		msgQueeIn.add(msg);
		LogUtil.print("压入MsgQuee 当前size:"+msgQueeIn.size());
		lock.unlock();
	}
	
	private T getMsg(boolean refresh){
		if(index<msgQueeOut.size()){
			LogUtil.print("读取MsgQuee");
			return msgQueeOut.get(index++);
		}else if(refresh){
			lock.lock();
			LogUtil.print("MsgQuee 刷新");
			msgQueeOut = msgQueeIn;
			msgQueeIn = new ArrayList<T>();
			index = 0;
			lock.unlock();
			return  getMsg(false);
		}else{
			return null;
		}
	}
	
	public T getMsg(){
		return getMsg(true);
	}
	
}
