package com.mbm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class IOUtil {

	private static final String LINE = "\n";

	/**
	 * 将一个字符串写入到一个文件
	 * 
	 * @param path
	 *            储存的文件路径
	 * @param value
	 *            储存的文件内容
	 * @throws IOException
	 */
	public static synchronized void writeFile(String path, String value)
			throws IOException {
		File f = new File(path);
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(value.getBytes());
		fos.close();
	}

	public static String readFileByInputSteam(InputStream is) throws IOException {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String temp = null;
			while ((temp = br.readLine()) != null && temp.trim().length()>0 && (!temp.trim().startsWith("#"))) {
				sb.append(temp);
				sb.append(LINE);
			}
			br.close();
			return sb.toString();
	}
	
	public static String readFile(String path) throws IOException {
		File f = new File(path);
		if (f.isFile() && f.canRead()) {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
				sb.append(LINE);
			}
			br.close();
			return sb.toString();
		} else {
			System.out.println(f.getAbsolutePath() + " 没有找到");
		}
		return null;
	}

	public static final String BR = "<br>";

	public static String readFileHTML(String path, int Offset, int length)
			throws IOException {
		File f = new File(path);
		if (f.isFile() && f.canRead()) {
			FileInputStream fis = new FileInputStream(f);
			fis.skip(Offset);
			byte[] bytes = new byte[length];
			fis.read(bytes);
			fis.close();
			return new String(bytes, "GBK").replace(LINE, BR);
		} else {
			System.out.println(f.getAbsolutePath() + " 没有找到");
		}
		return null;
	}

	public static String readFileTEXT(String path, int Offset, int length)
			throws IOException {
		File f = new File(path);
		if (f.isFile() && f.canRead()) {
			FileInputStream fis = new FileInputStream(f);
			fis.skip(Offset);
			byte[] bytes = new byte[length];
			fis.read(bytes);
			fis.close();
			return new String(bytes, "GBK").replace(LINE, "");
		} else {
			System.out.println(f.getAbsolutePath() + " 没有找到");
		}
		return null;
	}

	public static Document getDocument(String filePath) throws DocumentException{
		File file = new File(filePath) ;
		if(!file.isFile()){
			System.out.println(filePath+" not fond");
			return null ;
		}
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		return document; 
	}
	public static void main(String[] args)throws Exception {
//		System.out.println(readFile(StaticValue.ip_proxy_file_path));
	}
}
