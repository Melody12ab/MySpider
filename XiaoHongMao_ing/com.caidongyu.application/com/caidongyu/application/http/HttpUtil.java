package com.caidongyu.application.http;

import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.helper.StringUtil;

import com.caidongyu.application.collection.CollectionUtil;

/**
 * Http解析工具<br>
 * 方便的获取，解析Http相关的参数
 * <br>
 * @author 李洋
 * @author dongyu.cai
 */
public class HttpUtil {

	/**
	 * 获取参数的网站键值对,格式：liyang=023053&pass=123321&name=iljljlfs
	 * 允许：null<br>
	 *     Map<String,List<Object>>集合
	 * @return 返回参数的键值对，若参数为null则返回""
	 */
	@SuppressWarnings("unchecked")
	public static String formatParameters(Map<String,Object> params){
		if(params == null){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(String key : params.keySet() ){
			if( params.get(key) instanceof String){
				sb.append( key+"="+params.get(key)+"&");
			}else if( params.get(key) instanceof List ){
				List<Object> lists = (List<Object>)params.get(key);
				if(lists != null){
					for( Object obj : lists){
						sb.append( key+"="+obj.toString()+"&" );
					}
				}
			}
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	
	/**
	 * 拼接一个集合中的字符，组成Cookie格式
	 * 获取 Cookie 字符串，格式：liyang=023053,0we=gdxg,
	 * 允许 null 参数
	 * @param lists  拼接集合
	 * @return 获取 Cookie 字符串
	 */
	public static String formatCookie(List<String> lists){
		if(null == lists || lists.size() < 1)             //如果集合存在
			return "";                                    //返回""
		StringBuffer sb = new StringBuffer();
		for(String str : lists){
			sb.append( str+";;;" );
		}
		return sb.toString();
	}
	
	
	/**
	 * 合并Cookie，利用Map的数据结构，去掉重复的Cookie，重构后，返回Cookie字符
	 * 
	 * @param olds  以前的Cookie字符串
	 * @param news  本次获得的Cookie集合
	 * @return  合并后的Cookie
	 */
	public static String mergeCookies(String cookie, List<String> news){
		if( CollectionUtil.isEmpty(news))   //如果没有新的Cookie，则使用之前的Cookie
			return cookie;
		
		String[] vs = StringUtil.isBlank(cookie)?new String[0]:cookie.split(";");   
		Map<String,String> map  = new HashMap<String,String>();
		for(String key : vs ){
			map.put( key.replaceFirst("=.*$", "").trim(),key );
		}
		
		for(String keyLong : news ){
			String[] keyShorts = keyLong.split(";"); 
			for(String keyShort:keyShorts){
				map.put( keyShort.replaceFirst("=.*$", "").trim(),keyShort );
			}
		}
        //获取新cookie设置的值
		StringBuffer sb = new StringBuffer();
		for(String key : map.keySet()){
			sb.append( map.get(key)+";" );
		}
		return sb.toString();
	}

	/**
	 * 获取HttpCookie[],
	 * 通过解析一串Cookie格式的字符串，格式：liyang=023053,0we=gdxg
	 * 
	 * @param cookieStr  一个Cookie字符串
	 * @see {@link HttpCookie};
	 */
	public HttpCookie[] addResponceConversion(String cookieStr){
		if( cookieStr == null || cookieStr.isEmpty() ){        //若需要解析的Cookie为空
			return null;                                       //则返回null
		}
//		if( cookieStr.indexOf(",") < 0)                        //检查该字符是否不是一个Cookie
//			return;
		String[] str = cookieStr.split(",");                   //以，分割每一个Cookie
		HttpCookie[] httpCookie = new HttpCookie[str.length];  //创建HttpCookie

		for(int j=0; j<str.length; j++){                       //分解每一个Cookie {for}
			int i = -1;                                        //设定寻找分割的位置
			if( (i = str[j].indexOf("=")) > 0 && i < j ){      //如果找到分割线的位置
				String key = str[j].substring( 0 , i );        //获取"="的前半部分：key
				String value = null;                           //储存值变量
				if( str[j].indexOf(";") > 0){                  //如果找到";"
					value = str[j].substring( i+1 , str[j].indexOf(";") );    //剪掉;获取值：value
				}else{                                                        //否则
					value = str[j].substring( i+1 , str[j].length() );        //直接获取值：value
				}
				httpCookie[j] = new HttpCookie(key, value);                   //生成对象，加入Cookie组
			}
		}
		return httpCookie;
	}
	
	/**
	 * 通过HttpCookie[]，
	 * 获取 Cookie 字符串，格式：liyang=023053,0we=gdxg,
	 * 
	 * @return Cookie拼接成的字符串
	 * @see {@link HttpCookie}
	 */
	public String getCookieString(HttpCookie[] httpCookies){
		if( null == httpCookies || httpCookies.length < 1)                     //如果该对象没有Cookie
			return "";                                                         //返回""
		StringBuffer sb = new StringBuffer();                                  //创建拼接Cookie字符串
		for( HttpCookie httpCookie : httpCookies )                             //遍历每一个Cookie
			sb.append( httpCookie.getName()+"="+httpCookie.getValue()+",");    //拼接Cookie字符串
		return sb.deleteCharAt(sb.length()-1).toString();                      //去掉末尾","并返回Cookie字符串
	}
	
	
	/**
	 * 为GET请求的参数部分转码为HTML格式
	 * <ul>
	 * 	<li>1.若没有?,则返回当前地址</li>
	 * 	<li>2.若有?，则返回转码后的URL地址</li>
	 * </ul>
	 * @param strUrl 需要转码的URL
	 * @return 转码后的URL
	 */
	public static String urlEncoder(String strUrl){
		int place = strUrl.indexOf("?");              //寻找需要编译的参数部分
		if( place < 1)                                //如果没有参数
			return strUrl;                            //返回当前字符
		else{
			try {
				return strUrl.replaceFirst("\\?.*$" , "?"+URLEncoder.encode( strUrl.replaceFirst("^.*?\\?", ""), "UTF-8") );
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	/**
	 * 为字符转码为HTML格式
	 * 
	 * @param str  需要转码的字符
	 * @return  转码后的字符
	 */
	public static String stringEncoder(String str){
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 为HTML格式解码
	 * 
	 * @param str HTML格式字符串
	 * @return 解码后的字符
	 */
	public static String stringDecoder(String str){
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * 把Unicode格式的字符串转换为中文显示
	 * 
	 * @param unicode 如：\u5230\u592a\n\u9891\u7e41
	 * @return 解析后的中文字符串
	 * 
	 * @exception e {link IllegalArgumentException},在检测到"\\u"时候,系统认为其后4个字符为Unicode码，不允许出现格式错误。
	 */
	public static String formatUnicode(String unicode){
		int len = unicode.length();
		char aChar;
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<len; ){                          //循环每一个字符
			aChar = unicode.charAt( i++ );              //获取一个字符
			if(aChar == '\\'){                          //若该字符是\
				aChar = unicode.charAt( i++ );          //获取下一个字符
				if(aChar == 'u'){                       //若该字符是u则进入unicode转换逻辑
					int value=0;
					for(int j=0; j<4; j++){             //循环接下来的字符
						aChar = unicode.charAt( i++ );  //获取下一个字符
						if( aChar <= '9' && aChar >= '0'){          //若是0-9
							value = (value<<4) + aChar -'0';        //获取上一个字符进位后的10进制码  + 当前字符的ASCLL码
						}else if( aChar <= 'F' && aChar >= 'A'){    //
							value = (value<<4) + aChar -'A' + 10;   //16进制码转换为10进制需要+10+偏移量
						}else if( aChar <= 'f' && aChar >= 'a'){
							value = (value<<4) + aChar -'a' + 10;   //16进制码转换为10进制需要+10+偏移量
						}else{
							throw new IllegalArgumentException("it's not unicode coding , need like (\\u7e41\\u4e86\\u70b9) ");
						}
					}
					sb.append( (char)value );
				}else{
					if(aChar == 't')
						aChar ='\t';
					else if(aChar == 'r')
						aChar = '\r';
					else if(aChar == 'n')
						aChar = '\n';
					else if(aChar == 'f')
						aChar = '\f';
					sb.append( aChar );
				}
			}else{
				sb.append( aChar );
			}
		}
		return sb.toString();
	}
	
}
