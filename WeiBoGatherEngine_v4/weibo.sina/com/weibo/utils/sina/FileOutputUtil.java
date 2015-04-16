package com.weibo.utils.sina;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import com.mbm.elint.entity.util.UrlPojo;

public class FileOutputUtil {
	
	public static boolean printErrorUrlsList(String filePath,List<UrlPojo> errorUrls) {
		File f = new File(filePath);
		try {
			FileWriter fos = new FileWriter(f,true);
			PrintWriter pw=new PrintWriter(fos);
			for(UrlPojo temp_url : errorUrls){
				pw.println(temp_url);
			}
			pw.flush();
			pw.close();
			fos.close();
			f=null;
		} catch (Exception e) {
            e.printStackTrace();
            f=null;
            return false;
		}
        return true;
	}
}
