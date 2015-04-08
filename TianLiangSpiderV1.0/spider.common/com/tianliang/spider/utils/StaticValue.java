package com.tianliang.spider.utils;

/**
 * 静态变量定义
 * 
 * @author zel
 * 
 */
public class StaticValue {
	public static String default_encoding = "utf-8";
	public static String gbk_encoding = "gbk";
	public static final String gb2312_encoding = "gb2312";

	public static String default_refer = "http://www.baidu.com/";

	public static String baidu_index = "http://www.baidu.com";

	/**
	 * 符号定义
	 */
	public static String separator_tab = "\t";
	public static String separator_next_line = "\n";
	public static String separator_space = " ";
	public static String separator_file_path = "/";

	public static String separator_left_bracket = "(";
	public static String separator_right_bracket = ")";

	/**
	 * 专为解决网页编码提取而添加
	 */
	// 单点定义
	public static final char point = '.';

	public static int url_data_min_byte_length = 500;

	/**
	 * 以下是对网页的charset下的charset相应定义
	 */
	// 默认编码方式
	public static final String SYSTEM_ENCODING = "utf-8";
	// 默认gbk中文的处理编码
	public static final String GBK_ENCODING = "gbk";
	// 默认gb2312中文的处理编码
	public static final String GB2312_ENCODING = "gb2312";
	// 台湾big5编码
	public static final String BIG5_ENCODING = "big5";
	// 日本Shift_JIS
	public static final String Japan_Shift_ENCODING = "shift_jis";
	public static final String Japan_Euc_ENCODING = "euc-jp";
	// 西里尔文window
	public static final String Xili_Window_ENCODING = "windows-1251";
	/**
	 * 以下是对网页的charset部分的lang来定义
	 */
	public static final String Japan_Lang = "ja";
	public static final String Japan_Lang_First = Japan_Shift_ENCODING;

}
