package com.weibo.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static SimpleDateFormat rangDate = new SimpleDateFormat(
			"yyyy/MM/dd_HH:mm:ss");
	public static SimpleDateFormat mysqlDate = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	public static String getRangDateFormat(Date date){
		return rangDate.format(date);
	}
    public static String getMysqlDateFormat(Date date){
    	return mysqlDate.format(date);
    }
    
    public static Date getBeforeIntervalDate(Date date,long intervalTime){
    	return new Date(date.getTime()-intervalTime);
    }
    
    public static Date getAfterIntervalDate(Date date,long intervalTime){
    	return new Date(date.getTime()+intervalTime);
    }
}
