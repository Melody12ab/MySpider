package com.mbm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class MyURLReader {
	public static boolean readAndSave(String url, String fileFullName){
		System.out.println("开始下载---"+url);
		byte[] buffer = new byte[1024 * 8];
		int read;
		int ava = 0;
		URL u=null;
		try{
			u = new URL(url);	
			BufferedInputStream bin = new BufferedInputStream(u.openStream());
			BufferedOutputStream bout = new BufferedOutputStream(
					new FileOutputStream(fileFullName));
			while ((read = bin.read(buffer)) > -1) {
				bout.write(buffer, 0, read);
				ava += read;
			}
			bout.flush();
			bout.close();

			System.out.println("下载完毕");
			return true;
		}catch(Exception e){
			System.out.println("将中标社标准下载到本地时出现异常!");
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		MyURLReader.readAndSave("http://www.gb168.cn/std/saleagent/dpdf.jsp?id=76602771-ff31-4c57-b683-93220d8c2a91","d:/kk.pdf");
	}
}
