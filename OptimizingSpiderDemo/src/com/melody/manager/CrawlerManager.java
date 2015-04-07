package com.melody.manager;

import com.melody.iface.crawl.ICrawler;
import com.melody.impl.crawl.HttpUrlConnectionCrawlerImpl;
import com.melody.impl.crawl.SocketCrawlerImpl;
import com.melody.pojos.CrawlResultPojo;
import com.melody.pojos.UrlPojo;

/**
 * 包含业务逻辑的抓取管理器
 * 
 * 
 */
public class CrawlerManager {
	private ICrawler crawler;
	private UrlPojo urlPojo;
	public CrawlerManager(boolean isSocket,UrlPojo urlPojo) {
		if (isSocket) {
			this.crawler = new SocketCrawlerImpl(urlPojo);
		} else {
			this.crawler = new HttpUrlConnectionCrawlerImpl(urlPojo);
		}
	}

	public CrawlResultPojo crawl() {
		return this.crawler.crawl();
	}

	public static void main(String[] args) {
		UrlPojo urlPojo = new UrlPojo("http://www.qq.com");
		CrawlerManager crawlerManager = new CrawlerManager(false,urlPojo);
		CrawlResultPojo crawlResultPojo=crawlerManager.crawl();
		
		System.out.println("CrawlResultPojo---"+crawlResultPojo.getPageContent());
		
	}

}
