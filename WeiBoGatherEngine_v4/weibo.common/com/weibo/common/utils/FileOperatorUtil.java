package com.weibo.common.utils;

import java.io.File;
import org.apache.log4j.Logger;

public class FileOperatorUtil {
	public static Logger logger = Logger.getLogger(FileOperatorUtil.class);
	public static boolean createRootDir(String root_path) {
		File f = new File(root_path);
		if (f.exists() && f.isDirectory()) {
           return true;
		}else {
			try {
				if(f.mkdirs()){
					return true;
				}else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
