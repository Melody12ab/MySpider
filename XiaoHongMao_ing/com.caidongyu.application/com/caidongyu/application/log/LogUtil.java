package com.caidongyu.application.log;


/**
 * @author caidongyu_a
 * CaiDongyu
 * 2014/9/3
 * 日志信息工具类
 */
public class LogUtil {
	private volatile static LogUtil log;
	
	
	
	private LogUtil(){
		
	};
	
	public static LogUtil getInstance(){
		if(log == null){
			synchronized(LogUtil.class){
				if(log == null){
					log = new LogUtil();
				}
			}
		}
		return log;
	}
	
	
	public static Object print(Object content){
		System.out.println(content);
		return content;
	}
	
	public static String print(String content){
		System.out.println(content);
		return content;
	}
	
	public static String printParams(Object ...objs){
		StringBuffer buf = new StringBuffer();
		buf.append("参数:| ");
		for(Object obj:objs){
			buf.append(obj+" | ");
		}
		System.out.println(buf.toString());
		return buf.toString();
	}
}
