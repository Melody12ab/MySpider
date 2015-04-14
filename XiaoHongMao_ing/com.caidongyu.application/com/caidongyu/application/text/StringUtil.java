package com.caidongyu.application.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 黑云压城
 * @see 字符串处理工具
 */
public class StringUtil {
	
	public static void main(String[] args) {
//		System.out.println(StringUtil.cutString("asfasdfa",-1, 4, 3, 4));
		try {
//			System.out.println(StringUtil.countStrExts("你好abc我次奥abc啊啊啊你大爷好吗", "好"));
			System.out.println(StringUtil.countStrExts("update custst.CAIDONGYU_TEST set PAGE_COMPONENT_ID=?,FORM_ENTITY_ID=?,COMPONENT_TYPE_ID=?,STS=?,STS_DATE=?,TAG_ID=?,TAG_NAME=?,TAG_PARAM=?,LABEL_NAME=?,TD_SPAN=?,FLOW_IDX=?,ROW_SPAN=?,HTML_DISPLAY=?", "?"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private StringUtil(){};
	
	/**
     * 判断字符数组，不为空
     *
     * @param values 字符数组
     * @return true or false
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= isBlank(value);
                if (result == false) {
                    return result;
                }
            }
        }
        return result;
    }
	
    
    /**
     * join方法将 Stirng数组，通过separater分隔符进行分割
     *
     * @param resource 源数组
     * @param separater 分隔符
     * @return
     */
    public static String join(String[] resource, String separater) {
        if (resource == null || resource.length == 0) {
            return null;
        }
        int len = resource.length;
        StringBuilder sb = new StringBuilder();
        if (len > 0) {
            sb.append(resource[0]);
        }
        for (int i = 1; i < len; i++) {
            sb.append(separater);
            sb.append(resource[i]);
        }
        return sb.toString();
    }

    public static String getMiddle(String sourse, String first, String last) {
        if (!areNotEmpty(sourse, first, last)) {
            return null;
        }
        int beginIndex = sourse.indexOf(first) + first.length();
        int endIndex = sourse.lastIndexOf(last);
        return sourse.substring(beginIndex, endIndex);
    }

    public static String repaceTabs(String src) {
        return src.trim().replaceAll("\t|\r", " ");
    }
	
	/**
	 * 统计字符串出现次数
	 * @return
	 * @throws Exception 
	 */
	public static int countStrExts(String source,String str) {
		if(!isBlank(source) && !isBlank(str)){
			int count=0;
			int index = -1;
			while((index = source.indexOf(str))>=0){
				count++;
				source = cutString(source, index+str.length(), source.length(), 0, 0);
			}
			return count;
		}else{
			return -1;
		}
	}
	
	/**
	 * 截下字符串前几位
	 * str的长度按双字节2位，单字节1位计算
	 * 如： "你好abc"的长度是7(不含双引号)
	 * @param str 需要截断的字符串
	 * @param start 起始下标
	 * @param len 保留的长度
	 * @param dotNum 追加的点点点的个数
	 * @return
	 * @throws Exception
	 */
	public static String cutStringRealLen(String str,int len,int dotNumL,int dotNumR){
		if(StringUtil.getRelLength(str) > len){
			while(StringUtil.getRelLength(str) > len){
				str = str.substring(0, str.length()-1);
			}
			for(int i=0;i<dotNumL;i++){
				str = "."+str;
			}
			for(int i=0;i<dotNumR;i++){
				str += ".";
			}
		}
		return str;
	}
	
	/**
	 * 截取字符串长度
	 * 双字节和 单字节都是长度为1，按字(字符)计算
	 * 如： "你好abc"的长度是5(不含双引号)
	 * @param str
	 * @param len
	 * @param dotNum
	 * @return
	 */
	public static String cutString(String str,int start,int len,int dotNumL,int dotNumR){
		if(str.length()-start < len){
			len = str.length();
		}else{
			len = start+len;
		}
		str = str.substring(start, len);
		for(int i=0;i<dotNumL;i++){
			str = "."+str;
		}
		for(int i=0;i<dotNumR;i++){
			str += ".";
		}
		return str;
	}	
	
	/**
	 * 获取字符串长度，双字节为2，单字节为1
	 * 如： "你好abc"的长度是7(不含双引号)
	 */
	public static int getRelLength(String str){
		 int len = 0 ;
	       String[] strs = str.split("");
	       for ( int i= 0;i<strs.length;i++) {
	    	   if(!StringUtil.isBlank(strs[i])){
	    		   if ( strs[i].charAt(0)< 299 ) {
	    			   len ++;
	    		   } else {
	    			   len +=2;
	    		   }
	    	   }
	       }
	       return len;

	}
	
	/**
	 * 保留小数点后三位
	 * @param number 要四舍五入的数
	 * @param bit 精确到第几位，个位为0，整数方向为正，小数方向为负，比如精确到十位bit=1，而精确到小数后第一位bit=-1
	 * @return
	 */
	public static String round(String numberStr,int bit) throws Exception{
		if(bit >= 0){//整数位取值
			int point_index = numberStr.indexOf(".");
			//如果有小数，则留下整数部分
			if(point_index >= 0){
				numberStr = numberStr.substring(0, point_index);
			}
			
			//而且长度够
			if(numberStr.length() > bit){
				String numberStrHead = numberStr.substring(0,(numberStr.length()-bit));
				String numberStrTail = "";
				for(int i=0;i<bit;i++){
					numberStrTail += "0";
				}
				numberStr = numberStrHead+numberStrTail;
				if(StringUtil.isBlank(numberStr)){
					numberStr = "0";
				}
				return numberStr;
			}else {//长度不够直接全舍弃
				return "0";
			}
		}else{//小数位取值
			int point_index = numberStr.indexOf(".");
			//如果没有小数，则加上小数和零
			if(point_index < 0){
				numberStr += ".";
			}else if(point_index == 0){
				numberStr = "0"+numberStr;
			}
			point_index = numberStr.indexOf(".");
			String numberStrHead = numberStr.substring(0, point_index+1);
			String numberStrTail = numberStr.substring(point_index+1);
			if(!StringUtil.isBlank(numberStrTail) && numberStrTail.length() >= -bit){
				numberStrTail = numberStrTail.substring(0, -bit);
			}else{
				int dlen = -bit-numberStrTail.length();
				for(int i=0;i<dlen;i++){
					numberStrTail += "0";
				}
			}
			numberStr = numberStrHead+numberStrTail;
			return numberStr;
		}
	}
	
	
	/**
	 * 去掉字符串里的所有空格，包括前后和中间的
	 * @param oldString
	 * @return
	 * @throws Exception
	 */
	public static String trimAll(String oldString) throws Exception{
		StringBuffer newString = new StringBuffer();
		String[] oldStrings = oldString.trim().split(" ");
		for(String str:oldStrings){
			newString.append(str);
		}
		return newString.toString();
	}
	
	/**
	 * 笨方法：String s = "你要去除的字符串";
	 * 1.去除空格：s = s.replace('\\s','');
	 * 2.去除回车：s = s.replace('\n','');
	 * 注：\n 回车(\u000a)
	 * \t 水平制表符(\u0009)
	 * \s 空格(\u0008)
	 * \r 换行(\u000d)
	 * @return
	 */
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

	/**
	 * @see 比较两个字符串是否相同，都为null也算相同
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compare(String str1,String str2) throws Exception{
		if(str1 == null || str2 == null){
			return str1 == null&&str2 == null?true:false;
		}else{
			return str1.equals(str2)?true:false;
		}
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		if(str == null || "".equals(str)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断字符串是否为空串，含trim
	 * @param str
	 * @return
	 */
	public static boolean isBlankTrim(String str) throws Exception{
		if(isBlank(str)){
			return true;
		}else{
			if("".equals(str.trim())){
				return true;
			}else{
				return false;
			}
		}
	}
	
	
    /**
     * 将字符串转换成二进制字符串
     * @param str要转换的字符串，用来分隔的字符串
     * @return
     */
    public static String[] strToBinstr(String str) throws Exception{
        char[] strChar=str.toCharArray();
        String[] result = new String[strChar.length];
        for(int i=0;i<strChar.length;i++){
            result[i] = Integer.toBinaryString(strChar[i]);
        }
        return result;
    }
    
    /**
     * 将二进制字符串转换成Unicode字符串
     * @param binStr要转换的二进制串数组
     * @return
     */
    public static String binStrToStr(String[] binStr) throws Exception{
        char[] tempChar=new char[binStr.length];
        for(int i=0;i<binStr.length;i++) {
            tempChar[i]=binstrToChar(binStr[i]);
        }
        return String.valueOf(tempChar);
    }
    
    /**
     * 将初始二进制字符串转换成字符串数组，以空格相隔
     * @param str
     * @return
     */
    public static String[] strToStrArray(String str,String splitStr) throws Exception{
    	if(!StringUtil.isBlank(splitStr)){
    		return str.split(splitStr);
    	}else{
    		return new String[]{str};
    	}
    }
    
    /**
     * 将二进制字符串转换为char
     * @return
     */
    public static char binstrToChar(String binStr) throws Exception{
        int[] temp=binstrToIntArray(binStr);
        int sum=0;   
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;
        }   
        return (char)sum;
    }
    
    /**
     * 将二进制字符串转换成int数组
     * @return
     */
    public static int[] binstrToIntArray(String binStr) throws Exception{     
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];   
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }
}
