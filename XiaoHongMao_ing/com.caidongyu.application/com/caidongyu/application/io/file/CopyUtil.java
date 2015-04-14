package com.caidongyu.application.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件复制工具
 * @author caidongyu
 * @since 2013/10/30
 */
public class CopyUtil {
	public static final int  NO_FILTEROPT = -1;//文件未找到
	public static final int FAILED = -2;//拷贝失败，过程报错
	
	public static final int  FIEL_NOT_FOUND = 0;//文件未找到
	public static final int  AIM_FILE_EXIST = 1;//目录下存在同名文件

	public static  final int COVER = 3;//覆盖
	public static  final int LEAVE = 4;//保留两个文件
	public static  final int CANCEL = 5;//放弃拷贝
	
	
	public int copyFile(File file,String aim,int filterOpt) throws Exception{
			if(aim.endsWith("\\")){
				aim = aim.substring(0,aim.length()-1);
			}
			File aimFile = new File(aim+"\\"+file.getName());
			//如果文件不存在
			if(!FileUtil.fileExit(file)){
				return FIEL_NOT_FOUND;
			}
			//如果目标路径存在同名文件
			if(FileUtil.fileExit(aimFile)){
				//有操作条件
				if(filterOpt == NO_FILTEROPT){
					return AIM_FILE_EXIST;
				}else if(filterOpt ==  CANCEL){
					//放弃拷贝了
					return CANCEL;
				}else if(filterOpt == LEAVE){
					//余下的情况，要么就是覆盖，要么就是保留两者
					//覆盖不用判断
					//判断是否保留两者，需要改名
					String fileType = FileUtil.getFileType(file);
					String plainFileName = FileUtil.getFileNameWithoutType(file);
					int index = 1;
					aimFile = new File(aim+"\\"+plainFileName+"-"+index+fileType);
					while(FileUtil.fileExit(aimFile)){
						index++;
						aimFile = new File(aim+"\\"+plainFileName+"-"+index+fileType);
					}
				}
			}
			
			
			
			
			FileInputStream reader = new FileInputStream(file);
			FileOutputStream writer = new FileOutputStream(aimFile);
			byte[] data = new byte[1024];
			int len = 0;
			while((len = reader.read(data, 0, data.length)) > 0){
				writer.write(data,0,len);
			}
			writer.flush();
			writer.close();
			reader.close();
			
			return filterOpt;
	}

}
