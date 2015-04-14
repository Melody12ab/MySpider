package com.caidongyu.application.text;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * 正则表达式工具类，方便在正则表达式之上一些提取字符操作
 * 
 * @name        正则表达式工具
 * @author                  ★李洋★     liyangchiyue@gmail.com
 * @createtTime 2013-5-21  下午8:25:37
 * @version     1.0
 * @since       1.0
 *
 */
public class RegexUtil {

	
	/**
	 * 获取第一次出现 b 到 c 中间的字符
	 * 
	 * @param a  原字符
	 * @param b  b开始字符
	 * @param c  c结束字符
	 * @return
	 */
	public static String substring(String a,String b,String c){
		a = a.substring(a.indexOf(b)+b.length());
		return a.substring(0,a.indexOf(c)-c.length()+1);
	}
	
	/**
	 * 
	 * 获取DOM结构中，第N次出现
	 * 
	 * @param v         原字符
	 * @param tagStart  开始字符 如<td>
	 * @param endStart  结束字符 如</td>
	 * @param choice    截取第choice次出现时的结构
	 * @return
	 */
	public static String substring(String v,String tagStart, String endStart, int choice){
		for(int i=1;i<choice;i++){
			v = v.substring(v.indexOf(tagStart)+tagStart.length());
		    v = v.substring(v.indexOf(endStart)+endStart.length());
		}
		return substring(v,tagStart,endStart);
	}
	
	/**
	 * 替换字符  ,获取V中的 name以后的一段字符，该段字符的左右需要有非[a-zA-Z_0-9@]的标记。比如 :<br>
	 * v = {'push_type':'lost','tas123ks'}<br>
	 * name = lost<br>
	 * return = tas123ks<br>
	 * 
	 * @param v      需要操作的字符串
	 * @param name   需要取的字符名
	 * @return  截取完成的字符
	 */
	public static String replaceString(String v, String name){
		return v.replaceAll("[\r\n]*", "").replaceFirst("^.*?"+name, "").replaceFirst("[^a-zA-Z0-9_@\u4E00-\u9FFF]*", "").replaceFirst("[^a-zA-Z0-9_@\u4E00-\u9FFF]+.*$", "");
	}
	
	/**
	 * 替换字符，获取V中的name以后的一个数字。比如：<br>
	 * v = {expToday':2,'expTomorrow':2,'signInDataNum':3,'signTaskType':'65'}}
	 * name = signTaskType
	 * return = 65
	 * 
	 * @param v      需要操作的字符串
	 * @param name   需要取的字符名
	 * @return  截取完成的字符
	 */
	public static String replaceInt(String v,String name){
		return v.replaceAll("[\r\n]*", "").replaceFirst("^.*?"+name, "").replaceFirst("[^\\d]*", "").replaceFirst("[^\\d]+.*$", "");
	}

	public static String replaceStartEnd(String v, String name, String start, String end){
		return v.replaceAll("[\r\n]*", "").replaceFirst("^.*?"+name, "").replaceFirst(".*"+start, "").replaceFirst(end+".*", "");
	}

	public static String replaceStartEnd(String v, String start, String end){
		return v.replaceAll("[\r\n]*", "").replaceFirst("^.*?"+start, "").replaceFirst(end+".*$", "");
	}

	/**
	 * 提取两字符之间的字符，第一次出现的，从a字符开头-b字符结尾的中间部分，不包括a和b<br>
	 * 
	 * @param str  需要提取的字符
	 * @param a    开始字符
	 * @param b    结束字符
	 * @return  若成功,返回(需要提取的字符)<br>否则返回("")
	 * 
	 * @throws NullPointerException 若str为null的时候
	 * @see #substringMiddle(String, String, String);
	 */
	public static String substringMiddle(String str, char a, char b){
		return substringMiddle( str, String.valueOf(a), String.valueOf(b) );
	}
	/**
	 * 提取两字符之间的字符，第一次出现的，从a字符开头-b字符结尾的中间部分，不包括a和b<br>
	 * 
	 * @param str  需要提取的字符
	 * @param a    开始字符
	 * @param b    结束字符
	 * @return  若成功,返回(需要提取的字符)<br>否则返回("")
	 * 
	 * @throws NullPointerException 若str为null的时候
	 */
	public static String substringMiddle(String str, String a, String b){
		if( str == null )
			throw new NullPointerException();
		if( !a.equals(b) && str.indexOf(b) < str.indexOf(a) )    //若第二个字符比第一个字符先出现
			str = str.substring( str.indexOf( b )+1 );           //剪去第一次出现第二个字符之前的字符
		StringTokenizer st = new StringTokenizer(str.substring( str.indexOf(a)+1 ) );
		try{
			return st.nextToken(b);
		}catch(NoSuchElementException e){
			return "";
		}
	}
}
