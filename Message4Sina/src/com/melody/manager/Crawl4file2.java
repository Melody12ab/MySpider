package com.melody.manager;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.melody.impl.HttpClientCrawl;
import com.melody.impl.parse2ResultStr;

public class Crawl4file2 {
	Logger logger=Logger.getLogger(Crawl4file2.class);
	HttpClientCrawl clientCrawl=new HttpClientCrawl(); 
	parse2ResultStr parser2result=new parse2ResultStr();
	FileWriter writer=null;
	BufferedReader br=null;
	
	
	public String getResult(String name) throws InterruptedException{
		StringBuilder sb=new StringBuilder();
		int num=1;
		while(true){
			String source=clientCrawl.crawlOnePage(name, num);
			
			TimeUnit.SECONDS.sleep(2);
			
			if(source.contains("你的行为有些异常")){
				logger.info("要输入验证码了");
				System.exit(0);
			}
			
			if(Jsoup.parse(source).select("	").isEmpty()){
				break;
			}
			
			logger.info(Jsoup.parse(source).select("div.person_detail").size());
			
			sb.append(parser2result.parser(source));
			
			logger.info("解析完成第"+num+"页！");
			num++;
		}
		return sb.toString();
	}
	
	public void saveResult(String result){
		try {
			writer=new FileWriter("result.txt", true);
			writer.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		logger.info("存储成功！");
	}
	
	public List<String> getNameFromTxt(){
		ArrayList<String> list=new ArrayList<>();
		br=new BufferedReader(new InputStreamReader(Crawl4file2.class.getClassLoader().getResourceAsStream("search_name.txt")));
		String line=null;
		try {
			while((line=br.readLine())!=null){
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		logger.info("得到了查询列表！----------------");
		return list;
	}
	
	public static void main(String[] args) throws InterruptedException {
		Crawl4file2 C4F=new Crawl4file2();
		Iterator<String> it=C4F.getNameFromTxt().iterator();
		while(it.hasNext()){
			String rel=C4F.getResult(it.next());
			C4F.saveResult(rel);
		}
	}
	
}
