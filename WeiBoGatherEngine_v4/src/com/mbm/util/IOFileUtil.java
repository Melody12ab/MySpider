package com.mbm.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

import bloomFilter.BloomFilter;

public class IOFileUtil {
	public static boolean initBloomFilter(BloomFilter bloomFilter,String file_path)throws Exception{
		FileInputStream fis=new FileInputStream(file_path);
		BufferedReader br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
		String temp="";
		while((temp=br.readLine())!=null){
			bloomFilter.add(temp);
		}
		br.close();
		fis.close();
		return true;
	}
	
	public static void writeData(String file_path, String data)
			throws Exception {
		FileOutputStream fos=new FileOutputStream(file_path, true);
		fos.write(data.getBytes("UTF-8"));
		fos.write("\n".getBytes("UTF-8"));
		
		fos.flush();
		fos.close();
	}

	public static void writeDataList(String file_path, List<String> dataList)
			throws Exception {
		FileOutputStream fos=new FileOutputStream(file_path, true);
		for (String data : dataList) {
			fos.write(data.getBytes("UTF-8"));
			fos.write("\n".getBytes("UTF-8"));
		}
		fos.flush();
		fos.close();
	}

}
