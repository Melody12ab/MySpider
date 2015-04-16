package com.mbm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import org.apache.log4j.Logger;

import com.mbm.elint.entity.util.ProxyPojo;
import com.weibo.common.utils.StaticValue;

public class URLReader {
	private static Logger logger = Logger.getLogger(URLReader.class);

	public static void readAndSave(String url, String fileFullName)
			throws Exception {
		System.out.println("Starting.");
		URL u = new URL(url);
		byte[] buffer = new byte[1024 * 8];
		int read;
		int ava = 0;
		long start = System.currentTimeMillis();
		BufferedInputStream bin = new BufferedInputStream(u.openStream());
		BufferedOutputStream bout = new BufferedOutputStream(
				new FileOutputStream(fileFullName));
		while ((read = bin.read(buffer)) > -1) {
			bout.write(buffer, 0, read);
			ava += read;
			long speed = ava / (System.currentTimeMillis() - start);
			System.out.println("Download: " + ava + " byte(s)"
					+ "    avg speed: " + speed + "  (kb/s)");
		}
		bout.flush();
		bout.close();
		System.out.println("Done. size:" + ava + " byte(s)");
	}

	public static void download(String url, String fileFullName) {
		try {
			/**
			 * 设置代理
			 */
			if (StaticValue.proxy_open) {
				ProxyPojo proxyPojo = StaticValue.proxyList.get(0);
				System.setProperty("http.proxySet", "true");
				System.setProperty("http.proxyHost", proxyPojo.getIp());
				System.setProperty("http.proxyPort", "" + proxyPojo.getPort());
				// System.setProperty("http.proxyUser","admin");
				// System.setProperty("http.proxyPassword","zhou1234");
			}
			logger.info("正在下载---" + url);
			URL u = new URL(url);

			byte[] buffer = new byte[1024 * 8];
			int read;
			File localFile = new File(fileFullName);
			localFile = localFile.getParentFile();
			if (localFile != null) {
				localFile.mkdirs();
			}
			BufferedInputStream bin = new BufferedInputStream(u.openStream());
			BufferedOutputStream bout = new BufferedOutputStream(
					new FileOutputStream(fileFullName));
			while ((read = bin.read(buffer)) > -1) {
				bout.write(buffer, 0, read);
			}
			bout.flush();
			bout.close();
			logger.info("完成下载---" + url);

			/**
			 * 取消代理
			 */
			if (StaticValue.proxy_open) {
				System.setProperty("http.proxySet", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("下载验证码图片时出现错误!" + e.getLocalizedMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		URLReader
				.readAndSave(
						"http://www.gb168.cn/std/saleagent/dpdf.jsp?id=76602771-ff31-4c57-b683-93220d8c2a91",
						"d:/kk.pdf");
	}
}
