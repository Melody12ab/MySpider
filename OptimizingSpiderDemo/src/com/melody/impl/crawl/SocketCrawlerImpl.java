package com.melody.impl.crawl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.melody.iface.crawl.ICrawler;
import com.melody.pojos.CrawlResultPojo;
import com.melody.pojos.UrlPojo;

public class SocketCrawlerImpl implements ICrawler {
    
    private UrlPojo urlPojo;
    
    public SocketCrawlerImpl(UrlPojo urlPojo) {
        this.urlPojo = urlPojo;
    }

    
    @Override
    public CrawlResultPojo crawl() {
        CrawlResultPojo crawlResultPojo = new CrawlResultPojo();
        if (urlPojo == null || urlPojo.geturl() == null) {
            crawlResultPojo.setSuccess(false);
            crawlResultPojo.setPageContent(null);

            return crawlResultPojo;
        }

        String host = urlPojo.getHost();
        if (host == null) {
            crawlResultPojo.setSuccess(false);
            crawlResultPojo.setPageContent(null);
            return crawlResultPojo;
        }
        BufferedWriter bw = null;
        BufferedReader br = null;

        try {
            @SuppressWarnings("resource")
            Socket socket = new Socket(host, 80);
            socket.setKeepAlive(true);
            // socket.setSoTimeout(1000);
            bw = new BufferedWriter(new OutputStreamWriter(socket
                    .getOutputStream()));

            bw.write("GET " + urlPojo.geturl() + " HTTP/1.0\r\n");
            bw.write("HOST:" + host + "\r\n");
            bw.write("\r\n");// 在行的结束符\r\n之前没有任何数据，说明这时候http head输出给服务器端完成
            bw.flush();// 清空缓存流

            br = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = br.readLine()) != null) {
                // System.out.println(line);
                stringBuilder.append(line + "\n");
            }
            crawlResultPojo.setSuccess(true);
            crawlResultPojo.setPageContent(stringBuilder.toString());

            return crawlResultPojo;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("流最终未关闭，请检查!");
            }
        }
        return null;
    }

    public static void main(String[] args) {
        UrlPojo urlPojo = new UrlPojo("http://www.baidu.com");
        SocketCrawlerImpl socketCrawlerImpl = new SocketCrawlerImpl(urlPojo);
        // UrlPojo urlPojo = new UrlPojo("http://www.qq.com");
        // UrlPojo urlPojo = new UrlPojo(
        // "http://www.hao123.com/?tn=97961594_hao_pg");
        CrawlResultPojo crawlResultPojo=socketCrawlerImpl.crawl();

        System.out.println(crawlResultPojo.getPageContent());
        
        System.out.println("done!");
    }

}
