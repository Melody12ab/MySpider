package com.caidongyu.application.text;

import java.util.Random;

/**
 * @author 蔡东余
 * @see 整型工具类
 */
public class NumberUtil {

	private NumberUtil(){};
	
	/**
	 * 取随机数，最高位不含0
	 * 
	 * @param length
	 *            随机数长度，如果为0则为17
	 * @return 随机数文本
	 */
	public static String getRandomBetween(int length,int small,int big) {
		String text = "";// 返回的随机数
		Random random = new Random();
		for (int i = 0; i < length;) {
			int num = random.nextInt(10);
			if (i != 0 || num != 0) {
				if(i==0){
					if(num<small){
						continue;
					}
					if(num>big){
						continue;
					}
				}
				text += num + "";
				i++;
			}
		}
		return text;
	}
	
	/**
	 * 取随机数，最高位不含0
	 * 
	 * @param length
	 *            随机数长度，如果为0则为17
	 * @return 随机数文本
	 */
	public static String getRandom(int length) {
		String text = "";// 返回的随机数
		Random random = new Random();
		for (int i = 0; i < length;) {
			int num = random.nextInt(10);
			if (i != 0 || num != 0) {
				text += num + "";
				i++;
			}
		}
		return text;
	}
	
	/**
	 * 把二进制字符串转成整型数
	 * 比如：二进制1001，转换成十进制int型9
	 * @param binString
	 * @return
	 */
	public static int custBinaryStringToInt(String binStr) throws Exception{
		int sum = 0;//和
		for(int i=binStr.length();i>0;i--){
			int offIndex = binStr.length()-i;//取每个字符
			int n = Integer.parseInt(binStr.substring(offIndex,offIndex+1));//1或0
			int k = 1;
			for(int j=1;j<i;j++){
				k = k*2;
			}
			sum = sum+ n*k;
		}
		return sum;
	}

	/**
	 * 把数字按照给定的二进制数组结构去转换成二进制数组
	 * 比如十进制9，转换成二进制字符串数组{"1","0","0","1"}
	 * 如果给的格式数组，每个模板串的长度相加后的总长，不够二进制串总长，则保留低位
	 * 相反，高位填0
	 * @param num
	 * @param binStrArray
	 */
	public static String[] custNumberToBinaryStrArray(long num, String[] binModelArray) throws Exception{
		String binStr = Long.toBinaryString(num);
		System.out.println(binStr);
		
		int allLen = 0;//实际二进制串的应有总长，根据模板数组得到
		for(String model:binModelArray){
			allLen += model.length();
		}
		//如果binStr二进制串长度不够，高位填0
		while(binStr.length() < allLen){
			binStr = "0"+binStr;
		}
		
		int endIndex = binStr.length();
		for(int i=binModelArray.length-1;i>=0;i--){
			int beginIndex = endIndex-binModelArray[i].length();
			binModelArray[i] = binStr.substring(beginIndex,endIndex);
			endIndex = beginIndex;
		}
		return binModelArray;
	}
}
