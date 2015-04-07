package com.melody.control;

import java.util.ArrayList;
import java.util.List;

import com.melody.manager.CrawlerManager;
import com.melody.pojos.CrawlResultPojo;
import com.melody.pojos.UrlPojo;

/**
 * 系统控制器
 * 
 * 
 */
public class SystemControler {
    public static void main(String[] args) {
        List<UrlPojo> urlPojoList=new ArrayList<UrlPojo>();
        
        UrlPojo urlPojo1 = new UrlPojo("http://www.qq.com");
        UrlPojo urlPojo2 = new UrlPojo("http://www.baidu.com");
        
        urlPojoList.add(urlPojo1);
        urlPojoList.add(urlPojo2);
        
        
        for(UrlPojo pojo:urlPojoList){
            CrawlerManager crawlerManager = new CrawlerManager(false,pojo);
            CrawlResultPojo crawlResultPojo = crawlerManager.crawl();
//          System.out.println("CrawlResultPojo---"
//                  + crawlResultPojo.getPageContent());
            
            System.out.println("抓取任务为  "+pojo.toString());
            System.out.println("抓取结果为  "+crawlResultPojo.isSuccess());
            
        }
    }
}
