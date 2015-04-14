package com.caidongyu.application.io.file;

import java.io.File;

/**
 * @author 黑云压城
 * @see 文件来判断与处理工具类
 * @since 2013/10/31
 */
public class FileUtil {
	
	private FileUtil(){};
	
	public static boolean mkDirs(String parth){
		if(!fileExit(parth)){
			return new File(parth).mkdirs();
		}else{
			return true;
		}
	}
	
	/**
	 * 获取文件后缀名
	 */
	public static String getFileType(File file){
		String fileName = file.getName();
		int fileNameTypeFlag = fileName.indexOf('.');
		if(fileNameTypeFlag >= 0){
			return fileName.substring(fileNameTypeFlag);
		}else{
			return null;
		}
	}
	
	/**
	 * 只获取文件名，不带后缀
	 */
	public static String getFileNameWithoutType(File file){
		String fileName = file.getName();
		int fileNameTypeFlag = fileName.indexOf('.');
		if(fileNameTypeFlag >= 0){
			return fileName.substring(0,fileNameTypeFlag);
		}else{
			return fileName;
		}
	}
	
	/**
	 * @param file
	 * @return true:存在 ; false:不存在
	 */
	public static boolean fileExit(File file){
		return file==null ? false:file.exists() ? true:false;
	}
	
	/**
	 * @param file
	 * @return true:存在 ; false:不存在
	 */
	public static boolean fileExit(String filePath){
		return fileExit(new File(filePath));
	}
	
	/**
	 * 保存内容到文件，一次性
	 * 使用BufferedWriter
	 * @param filePath 文件路径
	 * @param content 文件内容
	 * @throws Exception 
	 */
	public static void saveFile(String filePath,String content) throws Exception{
			new SimpleBufferedWriter().simpleWrite(filePath, content);
	}
	
	/**
	 * 保存内容到文件，一次性
	 * 使用OutPutStream
	 * @param filePath 文件路径
	 * @param content 文件内容
	 * @param append 是否追加
	 */
	public static boolean saveFile(String filePath,String content,boolean append){
		try {
			new SimpleOutPutStream().simpleWrite(filePath, content,append);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
