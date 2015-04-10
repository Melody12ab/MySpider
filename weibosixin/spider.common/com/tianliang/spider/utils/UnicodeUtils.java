package com.tianliang.spider.utils;

public class UnicodeUtils {
	/**
	  *  unicode 转换成 中文
	  * @param theString
	  * @return
	  * @author: test  
	  * @Createtime: May 27, 2013
	  */
	 public static String decodeUnicode(String theString) {
	     char aChar;
	     int len = theString.length();
	     StringBuffer outBuffer = new StringBuffer(len);
	     for (int x = 0; x < len;) {
	         aChar = theString.charAt(x++);
	         if (aChar == '\\') {
	             aChar = theString.charAt(x++);
	             if (aChar == 'u') {
	                 // Read the xxxx
	                 int value = 0;
	                 for (int i = 0; i < 4; i++) {
	                     aChar = theString.charAt(x++);
	                     switch (aChar) {
	                     case '0':
	                     case '1':
	                     case '2':
	                     case '3':
	                     case '4':
	                     case '5':
	                     case '6':
	                     case '7':
	                     case '8':
	                     case '9':
	                         value = (value << 4) + aChar - '0';
	                         break;
	                     case 'a':
	                     case 'b':
	                     case 'c':
	                     case 'd':
	                     case 'e':
	                     case 'f':
	                         value = (value << 4) + 10 + aChar - 'a';
	                         break;
	                     case 'A':
	                     case 'B':
	                     case 'C':
	                     case 'D':
	                     case 'E':
	                     case 'F':
	                         value = (value << 4) + 10 + aChar - 'A';
	                         break;
	                     default:
	                         throw new IllegalArgumentException(
	                                 "Malformed   \\uxxxx   encoding.");
	                     }
	                 }
	                 outBuffer.append((char) value);
	             } else {
	                 if (aChar == 't')
	                     aChar = '\t';
	                 else if (aChar == 'r')
	                     aChar = '\r';
	 
	                 else if (aChar == 'n')
	 
	                     aChar = '\n';
	 
	                 else if (aChar == 'f')
	 
	                     aChar = '\f';
	 
	                 outBuffer.append(aChar);
	 
	             }
	 
	         } else
	             outBuffer.append(aChar);
	     }
	     return outBuffer.toString();
	 }
	 
	 /**
	  * 中文 转换成 unicode
	  * @param s
	  * @return
	  * @author: PLT  
	  * @Createtime: May 27, 2013
	  */
	 public static String toUnicode(String s) {
	     StringBuilder sb = new StringBuilder();
	     for (int i = 0; i < s.length(); ++i) {
	         if (s.charAt(i) <= 256) {
	             sb.append("\\u00");
	         } else {
	             sb.append("\\u");
	         }
	         sb.append(Integer.toHexString(s.charAt(i)));
	     }
	     return sb.toString();
	 }
	 
	 public static void main(String[] args) {
//		 String unicode=toUnicode("你好");
//		System.out.println(unicode);
		System.out.println(decodeUnicode("\u606d\u559c\uff0c\u64cd\u4f5c\u6210\u529f\u5566"));
		
	}
	 
}
