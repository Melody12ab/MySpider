package com.melody.control;

import com.melody.manager.WangYiDaiCrawlManager;

/**
 * 系统控制器
 * 
 * 
 */
public class WangYiDaiSystemControler {
    public static void main(String[] args) {

        String url = "http://www.wangdaizhijia.com/front_select-plat";
        int max_page_number=Integer.parseInt(args[0]);
        String filePath=args[1]; 
        WangYiDaiCrawlManager.processWangYiDai(url, max_page_number, filePath);
    }
}
