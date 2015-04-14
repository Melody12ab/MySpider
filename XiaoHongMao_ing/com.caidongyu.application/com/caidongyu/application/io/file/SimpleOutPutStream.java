package com.caidongyu.application.io.file;

import java.io.File;
import java.io.FileOutputStream;

public class SimpleOutPutStream {
	public FileOutputStream writer;
	

	/**
	 * @see 简单写写的，参数都很简单
	 */
	public void simpleWrite(File file,String message,boolean append)throws Exception{
		initWriter(file,append);
		byte[] data = message.getBytes();
		writer.write(data,0,data.length);
		writer.flush();
		closeWriter();
	}
	
	/**
	 * @see 简单写写的，参数都很简单
	 */
	public void simpleWrite(String filePath,String message,boolean append)throws Exception{
		simpleWrite(new File(filePath), message,append);
	}
	
	
	
	/**
	 * @see 初始化writer
	 * @param file
	 * @throws Exception
	 */
	public void initWriter(File file,boolean append)throws Exception{
		if(writer == null){
			writer = new FileOutputStream(file, append);
		}
	}
	
	public void closeWriter()throws Exception{
		if(writer != null){
			writer.close();
		}
	}
}
