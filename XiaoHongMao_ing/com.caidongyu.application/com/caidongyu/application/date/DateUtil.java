package com.caidongyu.application.date;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author 黑云压城
 * @see 时间日期处理工具
 */
public class DateUtil {
	public final static String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public final static String YMDHM = "yyyy-MM-dd HH:mm";
	
	private DateUtil() {}
	
	public static void main(String[] args) {
		Date date1 = new Date();
		Date date2 = new Date(date1.getTime()-46400000);
		System.out.println(daysBetween(date2, date1));
	}
	
	/**
	 * 比较两个日期相差的天数
	 * day1>day2时返回正整数
	 * day1<day2时返回负整数
	 * @param day1
	 * @param day2
	 */
	public static double daysBetween(Date bigDate, Date smallDate) {
		if ((bigDate == null) || (smallDate == null)) {
			return 0;
		}
		BigDecimal x = new BigDecimal(bigDate.getTime()) ;
		BigDecimal y = new BigDecimal(smallDate.getTime());
		BigDecimal z = new BigDecimal(86400000);
		MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
		BigDecimal m = x.subtract(y).divide(z,mc);
		return m.doubleValue();
	}
	
	/**
	 * @see 获取系统时间
	 * @param formatStr 如： yyyy-MMM-dd hh:mm:ss
	 */
	public static String getFormatedSysTime(String formatStr)throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(new Date());
	}
	
	/**
	 * 格式化给定的时间
	 */
	public static String formatDateToStr(Date date,String formatStr){
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(date);
	}
	
	/**
	 * 格式化给定的数据库时间
	 */
	public static String formatTimeStampToStr(Timestamp data,String formatStr)throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(new Date(data.getTime()));
	}
	
	/**
	 * 把字符串转化成日期时间对象
	 * @throws ParseException 
	 */
	public static Date formatStrToDate(String dateStr,String formatStr) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.parse(dateStr);
	}
	
	/**
	 * 把字符串转化成数据库日期时间对象
	 */
	public static Timestamp formatStrToTimeStamp(String dateStr,String formatStr)throws Exception{
		Date date = formatStrToDate(dateStr, formatStr);
		return new Timestamp(date.getTime());
	}
	
}