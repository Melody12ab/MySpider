package com.cdy.qzone.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.caidongyu.application.http.HttpUtil;

/**
 * 
 * @category 简单工具
 * @author   李洋
 * @version  1.0
 * @date     2012-12-9
 */
public class SimpleUtil {
	/** 格式日期 yyyy_MM_dd */
	public static final SimpleDateFormat formatDateBy_ = new SimpleDateFormat("yyyy_MM_dd");
	/** 格式日期 yyyy-MM-dd*/
	public static final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	/** 格式日期 yyyy-MM-dd HH:mm:ss*/
	public static final SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** 格式小时 */
	public static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
	/** 获取项目的根路径 **/
	public static String root;

	static{
		root = HttpUtil.stringDecoder( SimpleUtil.class.getClassLoader().getResource("").getPath() );
		root = new File(root).getParent();
	}
	/**
	 * 格式日期   2012_1_1
	 * @param date  日期
	 * @return  格式该日期的字符串
	 */
	public static String formatDateBy_(Date date){
		return formatDateBy_.format(date);
	}
	
	/**
	 * 格式日期 2012-1-1
	 * @param date 日期
	 * @return 格式该日期的字符串
	 */
	public static String formatDate(Date date){
		return formatDate.format(date);
	}
	
	/**
	 * 格式日期 yyyy-MM-dd HH:mm:ss
	 * @param date 日期对象
	 * @return 格式该日期的字符串
	 */
	public static String formatDateTime(Date date){
		return formatDateTime.format(date);
	}
	
	/**
	 * 格式时间  HH:mm:ss，把一个日期对象的毫秒，转换为小时，最多24小时
	 * @param date 日期对象
	 * @return 格式该日期的字符串
	 */
	public static String formatTime(Date date){
		return formatTime.format(date);
	}
	
	/**
	 * 格式化路径，以/(反斜杠)做为路径分割符
	 * @param path  需要格式的路径字符串
	 * @return 格式完成的路径字符串
	 */
	public static String formatPath(String path){
		return path.replace("\\", "/");
	}
	
	/**
	 * 根据传入的内容，判断内容的长度，并换行
	 * @param content
	 * @return
	 */
	public static String getContent(String content){
		if(content.length() > 30){
			StringBuffer ss = new StringBuffer();
			for(int k = 0;k<content.length();k++){
				ss.append(content.charAt(k));
				if(k%16 == 0){
					ss.append("<br />");
				}
			}
			content = ss.toString();
		}
		return content;
	}
	/**
	 * 计算，现在离下一个时间还有多少毫秒
	 * 
	 * 
	 * @param afterTime  还有多少毫秒，到这个时间 参数如：3：00，到明天的3：00还有多少毫秒
	 * @return  多少毫秒
	 * 
	 * @throws RuntimeException 时间格式错误
	 */
	public static long afterDay(String afterTime){
		if( !java.util.regex.Pattern.compile("\\d{1,2}:\\d{1,2}+").matcher(afterTime).matches())
			throw new RuntimeException("format error");
		
		long now = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt( afterTime.split(":")[0] ));
		c.set(Calendar.MINUTE, Integer.parseInt( afterTime.split(":")[1] ));
		c.set(Calendar.SECOND,0);
		return c.getTime().getTime() - now;
	}
	
	/**
	 * 替换一串字符串中的占位符{0},{1},{2}
	 * 为动态字符
	 * 
	 * 注意：从{0}开始
	 * 
	 * @param fill  原字符串  如：http://check.ptlogin2.qq.com/check?regmaster=&uin=448163451&appid={1}&js_ver={2}&js_type=1
	 * @param str   动态填充站位字符
	 * @return 填充完成的新字符串
	 */
	public static String fillString(String fill,String ...str){
		if(null == fill ||null == str)
			return "";
		
		int $i = 0;
		for(String v : str ){
			fill = fill.replace("{"+$i+++"}", v);
		}
		return fill;
	}
	
	/**
	 * 一般返回的内容，如果是类似_callBack({....});这样的，可以获取到json内容
	 */
	public static String getCallBackContent(String callBackHtml){
		int start = callBackHtml.indexOf("(");
		int end = callBackHtml.lastIndexOf(")");
		String data_json = callBackHtml.substring(start+1,end);
		return data_json;
	}
}
