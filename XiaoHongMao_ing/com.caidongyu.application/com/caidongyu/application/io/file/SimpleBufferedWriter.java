package com.caidongyu.application.io.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author 黑云压城
 * @see 输入输出工具
 */
public class SimpleBufferedWriter {
	/**
	 * @see 输出流
	 */
	public BufferedWriter writer;
	
	/**
	 * @see 简单写写的，参数都很简单
	 */
	public void simpleWrite(File file,String message)throws Exception{
		initWriter(file);
		writer.write(message);
		writer.flush();
		closeWriter();
	}
	
	/**
	 * @see 简单写写的，参数都很简单
	 */
	public void simpleWrite(String filePath,String message)throws Exception{
		simpleWrite(new File(filePath), message);
	}
	
	
	
	/**
	 * @see 初始化writer
	 * @param file
	 * @throws Exception
	 */
	public void initWriter(File file)throws Exception{
		if(writer == null){
			writer = new BufferedWriter(new FileWriter(file));
		}
	}
	
	public void closeWriter()throws Exception{
		if(writer != null){
			writer.close();
		}
	}
}