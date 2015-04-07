package com.melody.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.melody.enumeration.RequestMethod;
import com.melody.iface.parser.IHtmlParser;
import com.melody.impl.crawl.HttpClientCrawlerImpl;
import com.melody.impl.parser.HtmlParserImpl4WangYiDai;
import com.melody.impl.parser.HtmlParserToObjectImpl4WangYiDai;
import com.melody.impl.save.SaveImplToFile4WangYiDai;
import com.melody.pojos.CrawlResultPojo;
import com.melody.pojos.UrlPojo;

public class WangYiDaiCrawlManager {
    public static IHtmlParser htmlParser = new HtmlParserImpl4WangYiDai();

    public static String[] column_key = { "platName", "locationAreaName",
            "locationCityName", "platUrl" };
    public static int item_count = 0;

    public static void processWangYiDai(String url, int max_page_number,
            String filePath) {
        // 负责抓取到的文件的存储 默认情况
        SaveImplToFile4WangYiDai saveImpl4WangYiDai = new SaveImplToFile4WangYiDai(
                filePath);
        // 存储所有的抓取提条目
        StringBuilder All_items = new StringBuilder();
        UrlPojo pojo = new UrlPojo(url);
        HttpClientCrawlerImpl httpClientCrawlerImpl = new HttpClientCrawlerImpl(
                pojo);
        httpClientCrawlerImpl.setRequestMethod(RequestMethod.POST);
        Map<String, Object> parasMap = new HashMap<String, Object>();
        int have_download_page_count = 0;
        Set<String> uniqSet = new HashSet<>();
        for (int pageNumber = 1; pageNumber <= max_page_number; pageNumber++) {
            parasMap.put("currPage", pageNumber);
            pojo.setParasMap(parasMap);
            CrawlResultPojo resultPojo = httpClientCrawlerImpl.crawl();

            if (uniqSet.contains(resultPojo.getPageContent())) {
                System.out.println("遇到重复的了，说明抓取结束");
                break;
            } else {
                uniqSet.add(resultPojo.getPageContent());
            }
            if (resultPojo != null) {
                String content = resultPojo.getPageContent();
                String page_items = (String) htmlParser.parser(content);
                All_items.append(page_items);
                have_download_page_count++;
            }
            System.out.println("totle download" + have_download_page_count );
        }
        // System.out.println("all items size---"+All_items.toString().split("\n").length);
        System.out.println("totle data:" + item_count);
        saveImpl4WangYiDai.save(All_items.toString());
        System.out.println("save successful");
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        String url = "http://www.wangdaizhijia.com/front_select-plat";
        int max_page_number = 1;
        String filePath = "a.txt";
        processWangYiDai(url, max_page_number, filePath);

        long end = System.currentTimeMillis();
        System.out.println("一共花时：" + (end - start) / 1000 + "s");

    }
}
