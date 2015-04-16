package com.mbm.util;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class DownFileByBinaryUtil {
	public static void writeResponse(FileInputStream is,
			HttpServletResponse res, String fileName) {
		String temp = "";
		String show_down="";
		try {
			temp = new String(fileName.getBytes("GBK"), "ISO-8859-1");
			System.out.println("========fileName ============"+fileName);
			System.out.println("第一次temp ============"+temp);
			temp = new String(fileName.getBytes("ISO-8859-1"), "GBK");
			System.out.println("第二次temp ============" + temp);
			temp=temp.substring(0,temp.indexOf('#')).replaceAll("%","_")+".pdf";
			System.out.println("取得标准号之后的fileName ============" + temp);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			// String fileExt = getFileExt(fileName); //取扩展名
			String fileExt = "pdf";// 得到文件类型

			ServletOutputStream out = res.getOutputStream();

			//用于解决下载后的中文乱码
			
			String contentType = getContentType(fileExt);
//			contentType=contentType+";charset=gbk";
			res.setContentType(contentType);
System.out.println("显示下载的名称是：-------------------------------"+temp);
			String contentHeader = "attachment" + ";" + "filename=\""
					+ URLEncoder.encode(temp,"UTF-8")+ "\"";
			res.setHeader("Content-disposition", contentHeader);
			System.out.println("contentHeader is =" + contentHeader);

			// int MAX_FILE_SIZE = 3 * 1024 * 1024; //此处设置文件最大尺寸，对本程序，不设置即可,3M
			int byte_unit = 1024;
			// byte[] buffer = new byte[MAX_FILE_SIZE];
			byte[] buffer = new byte[byte_unit];// 一次性读取文件的数据长度
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			if (is != null)
				is.close();
			if (out != null) {
				out.flush();
				out.close();
			}
			System.out.println("out没有关闭");
			System.out.println("java io流写入客户端成功");
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("从服务器端下载文件时出错，流出错 ");
		}
	}
	
	public static void writePdfToResponse(FileInputStream is,
			HttpServletResponse res, String fileName) {
		String temp = "";
		try {
//			temp = new String(fileName.getBytes("GBK"), "ISO-8859-1");
//			System.out.println("========fileName ============"+fileName);
//			System.out.println("--------第一次temp ============"+temp);
//			temp = new String(fileName.getBytes("ISO-8859-1"), "GBK");
//			System.out.println("---------第二次temp ============" + temp);
//			System.out.println("temp----------------------:"+temp);
			temp=fileName;
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			String fileExt = StringTransform.getExtByPath(fileName);
//			String fileExt = "pdf";// 得到文件类型
			System.out.println("得到的文件后缀为："+fileExt);
			ServletOutputStream out = res.getOutputStream();

			String contentType = getContentType(fileExt);
			res.setContentType(contentType);

			String contentHeader = "charset:UTF-8;"+"attachment" + ";" + "filename=\""
					+ temp + "\"";
			
			res.setHeader("Content-disposition", contentHeader);
			System.out.println("contentHeader is =" + contentHeader);

			// int MAX_FILE_SIZE = 3 * 1024 * 1024; //此处设置文件最大尺寸，对本程序，不设置即可,3M
			int byte_unit = 1024;
			// byte[] buffer = new byte[MAX_FILE_SIZE];
			byte[] buffer = new byte[byte_unit];// 一次性读取文件的数据长度
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			if (is != null)
				is.close();
			if (out != null) {
				out.flush();
				out.close();
			}
			System.out.println("out没有关闭");
			System.out.println("java io流写入客户端成功");
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("从服务器端下载文件时出错，流出错 ");
		}
	}
	
	public static void writeResponseRemote(FileInputStream is,
			HttpServletResponse res, String fileName) {
		String temp = "";
		try {
			System.out.println("========转化前的filename ============"+fileName);
			temp=new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+".pdf";
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			// String fileExt = getFileExt(fileName); //取扩展名
			String fileExt = "pdf";// 得到文件类型

			ServletOutputStream out = res.getOutputStream();

			String contentType = getContentType(fileExt);
			res.setContentType(contentType);

			String contentHeader = "attachment" + ";" + "filename=\""
					+ temp + "\"";
			
			res.setHeader("Content-disposition", contentHeader);
			System.out.println("contentHeader is =" + contentHeader);

			// int MAX_FILE_SIZE = 3 * 1024 * 1024; //此处设置文件最大尺寸，对本程序，不设置即可,3M
			int byte_unit = 1024;
			// byte[] buffer = new byte[MAX_FILE_SIZE];
			byte[] buffer = new byte[byte_unit];// 一次性读取文件的数据长度
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			if (is != null)
				is.close();
			if (out != null) {
				out.flush();
				out.close();
			}
			System.out.println("out没有关闭");
			System.out.println("java io流写入客户端成功");
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("从服务器端下载文件时出错，流出错 ");
		}
	}

	// 获得内容类型
	private static String getContentType(String fileExt) {
		String contentType = " ";
		if (fileExt == null)
			contentType = " ";
		else if (fileExt.equals("doc"))
			contentType = "application/msword ";
		else if (fileExt.equals("pdf"))
			contentType = "application/pdf";
		else if (fileExt.equals("jpg"))
			contentType = "image/jpeg";
		else if (fileExt.equals("gif"))
			contentType = "image/gif ";
		else if (fileExt.equals("bmp"))
			contentType = "image/bmp ";
		else if (fileExt.equals("txt"))
			contentType = "text/plain";
		return contentType;
	}
	
}
