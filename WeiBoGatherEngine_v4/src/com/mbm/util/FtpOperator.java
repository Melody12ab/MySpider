package com.mbm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpOperator {
	private static FTPClient ftpTool = null;

	public static void connectFtpServer(String s_IP, String s_user,
			String s_password) throws IOException {
		try {
			ftpTool = new FTPClient();
			ftpTool.connect(s_IP);
			ftpTool.login(s_user, s_password);
			ftpTool.setFileType(ftpTool.BINARY_FILE_TYPE);
			ftpTool.enterLocalPassiveMode();
			ftpTool.enterLocalActiveMode();
			ftpTool.setControlEncoding("gbk");
			ftpTool.setSoTimeout(6000); // timeout is 6 seconds.
		} catch (IOException ex) {
			throw new IOException("can   not   login   ftp   server,   " + ex);
		}
	}

	public static void getDirectoryNames() throws IOException {
		 FTPFile[] fs=ftpTool.listFiles();
	}

	public static void closeConnection() {
		if (ftpTool != null) {
			if (ftpTool.isConnected()) {
				try {
					ftpTool.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ftpTool = null;
			}
		}
	}

	public static void listFiles() {
		try {
			FTPFile[] fs = ftpTool.listFiles();
			int i = 0;
			for (i = 0; i < fs.length; i++) {
				FTPFile f = fs[i];
				if(f.isDirectory()){
					System.out.println("第" + (i + 1) + "个目录的名字是" + f.getName());		
				}else {
					System.out.println("第" + (i + 1) + "个文件的名字是" + f.getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean downloadFile(String remoteFile, String localFile)
			throws IOException {
		if(new File(localFile).isFile()){
			System.out.println("该标准已经在本地系统中，不用重新从ftp端下载了!	");
			return true;
		}
		connectFtpServer(ReadProperties.readValue("server_ip"), ReadProperties.readValue("ftp_username"), ReadProperties.readValue("ftp_password"));
		System.out.println("连接FTP服务器成功");
		boolean flag = false;
		File outfile = new File(localFile);
		FileOutputStream out = null;
//		if (outfile.exists()) {
		System.out.println(remoteFile);
			String[] names = ftpTool.listNames(remoteFile);
			if (names.length != 0) {
				//中文字符处理
				remoteFile= new String(remoteFile.getBytes("GBK"),"ISO-8859-1");
				try {
					out = new FileOutputStream(outfile);
					flag = ftpTool.retrieveFile(remoteFile, out);
					
					remoteFile=new String(remoteFile.getBytes("ISO-8859-1"),"GBK");
					System.out.println("name is "+remoteFile);
				} catch (IOException e) {
					flag = false;
					return flag;
				}
//			}
				out.flush();
				out.close();
		}
			closeConnection();
			System.out.println("从ftp下载pdf文件成功!");
		return flag;
	}

	public static boolean findFile(String fileName){
		try {
			FTPFile[] fs = ftpTool.listFiles("14");
			int i = 0;
			for (i = 0; i < fs.length; i++) {
				FTPFile f = fs[i];
					if(f.getName().equals(fileName)){
						System.out.println("找到了");
						System.out.println("========"+f.getName());
					  	return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean delTempPdf(String path){
	   try{
		   File f=new File(path);
		   
		   if(f.isFile()&&f.canRead()){
			   f.delete();
			   System.out.println("删除文件成功");
			   return true;
		   }else {
			   System.out.println("此文件不存在");
		   }
		   System.out.println("删除此文件没有成功");
		   return true;
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	  	return false;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println("连接成功");
			connectFtpServer("121.28.48.213", "lingjoin", "zhanghui965");
			//downloadFile("YY%T%0316-2003#医疗器械%风险管理对医疗器械的应用.pdf","d:\\1.pdf");//YY%T%0316-2003#医疗器械%风险管理对医疗器械的应用.pdf,CB%3189-1983#污水井盖板.pdf
//			String remote= new String("1\\CB%3189-1983#污水井盖板.pdf".getBytes("GBK"),"ISO-8859-1");
//			String returnRemote=new String(remote.getBytes("ISO-8859-1"),"GBK");
//			downloadFile("1\\CB%3189-1983#污水井盖板.pdf","d:\\1.pdf");
//			System.out.println("转化回来的结果:"+returnRemote);
//			listFiles();
			if(!findFile("BB%T% 0003-1994#耐高温蒸煮膜%袋.pdf")){
				System.out.println("没有找到");
			}
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
