package com.melody.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.melody.enumeration.RequestMethod;
import com.melody.iface.parser.IHtmlParser;
import com.melody.iface.save.ISave;
import com.melody.impl.crawl.HttpClientCrawlerImpl;
import com.melody.impl.parser.HtmlParserToObjectImpl4WangYiDai;
import com.melody.impl.save.SaveImplToFile4WangYiDai;
import com.melody.impl.save.SaveImplToMysql4WangYiDai;
import com.melody.pojos.CrawlResultPojo;
import com.melody.pojos.UrlPojo;
import com.melody.pojos.WangYiDaiItemPojo;

public class WangYiDaiCrawlManagerToMysql {
    public static IHtmlParser<List<WangYiDaiItemPojo>> htmlParser = new HtmlParserToObjectImpl4WangYiDai();

    public static String[] column_key = { "platName", "locationAreaName",
            "locationCityName", "platUrl" };
    public static int item_count = 0;

    public static void processWangYiDai(String url, int max_page_number,
            String filePath) {
        // 负责抓取到的文件的存储 默认情况
        ISave<List<WangYiDaiItemPojo>> iSave = new SaveImplToMysql4WangYiDai(filePath);
        // 存储所有的抓取提条目
        List<WangYiDaiItemPojo> ALL_items = new ArrayList<WangYiDaiItemPojo>();
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
                List<WangYiDaiItemPojo> list = htmlParser.parser(content);
                ALL_items.addAll(list);
                have_download_page_count++;
            }
            System.out.println("一共下载" + have_download_page_count + "页");
        }
        System.out.println("数据一共有:" + item_count + "条");
        iSave.save(ALL_items);
        System.out.println("数据储存成功");
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
