package com.melody.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.melody.impl.HttpClientCrawl;
import com.melody.impl.parse2ResultStr;

public class crawl4file {
	Logger logger = Logger.getLogger(crawl4file.class);
	HttpClientCrawl clientCrawl = new HttpClientCrawl();
	parse2ResultStr parser2result = new parse2ResultStr();
	BufferedReader br = null;

	public String getResult(String name) throws InterruptedException {
		StringBuilder sb = new StringBuilder();
		int num = 1;
		while (num<6) {
			String source = clientCrawl.crawlOnePage(name, num);
//			TimeUnit.SECONDS.sleep(2);
			
			if (source.contains("你的行为有些异常")) {
				logger.info("要输入验证码了");
				System.exit(0);
			}
			String result = parser2result.parser(source);
			
			logger.info("解析完成" + num + "页");
			
			sb.append(result);
			num++;
		}
		return sb.toString();
	}

	public List<String> getNameFromTxt() {
		ArrayList<String> list = new ArrayList<>();
		br = new BufferedReader(new InputStreamReader(crawl4file.class
				.getClassLoader().getResourceAsStream("search_name.txt")));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public static void main(String[] args) throws InterruptedException {
		crawl4file C4F = new crawl4file();
		// Iterator<String> it = C4F.getNameFromTxt().iterator();
		// while (it.hasNext()) {
		// String rel = C4F.getResult(it.next());
		// System.out.println(rel);
		// }
		System.out.println(C4F.getResult("县长"));
	}

}
