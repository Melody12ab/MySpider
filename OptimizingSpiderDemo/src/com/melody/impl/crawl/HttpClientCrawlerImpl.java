package com.melody.impl.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import com.melody.enumeration.RequestMethod;
import com.melody.iface.crawl.ICrawler;
import com.melody.manager.httpclient.HttpClientManager;
import com.melody.pojos.CrawlResultPojo;
import com.melody.pojos.UrlPojo;

public class HttpClientCrawlerImpl implements ICrawler {
    
    private RequestMethod requestMethod=RequestMethod.GET;
    private UrlPojo pojo;
    
    public HttpClientCrawlerImpl(UrlPojo pojo) {
        this.pojo = pojo;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public CrawlResultPojo crawl() {

        CrawlResultPojo resultPojo = new CrawlResultPojo();

        if (pojo == null || pojo.geturl() == null) {
            resultPojo.setPageContent(null);
            resultPojo.setSuccess(false);
            return resultPojo;
        }

        // HttpGet httpget = new HttpGet(pojo.geturl());
        HttpUriRequest httppost = null;
        try {
            RequestBuilder rb=null;
            if(requestMethod==RequestMethod.GET){
                rb = RequestBuilder.get().setUri(
                        new URI(pojo.geturl()));
            }else{
                rb = RequestBuilder.post().setUri(
                        new URI(pojo.geturl()));
            }
            // .addParameter("IDToken1", "username")
            // .addParameter("IDToken2", "password")
            // .build();
            
            Map<String,Object> parasMap=pojo.getParasMap();
            if(parasMap!=null){
                for(Entry<String, Object> entry:parasMap.entrySet()){
                    rb.addParameter(entry.getKey(), entry.getValue().toString());
                }
            }
            httppost=rb.build();
            

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        BufferedReader bf = null;
        CloseableHttpResponse response1 = null;
        try {
            response1 = HttpClientManager.getHttpClient().execute(httppost);
            HttpEntity entity = response1.getEntity();
            bf = new BufferedReader(new InputStreamReader(entity.getContent()));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bf.readLine()) != null) {
                // System.out.println(line);
                stringBuilder.append(line + "\r\n");
            }

            resultPojo.setSuccess(true);
            resultPojo.setPageContent(stringBuilder.toString());
            return resultPojo;
        } catch (Exception e) {
            e.printStackTrace();
            resultPojo.setPageContent(null);
            resultPojo.setSuccess(false);
        } finally {
            if (response1 != null) {
                try {
                    response1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultPojo;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.wangdaizhijia.com/front_select-plat";
        
        Map<String, Object> parasMap=new HashMap<String, Object>();
        parasMap.put("currPage", "3");
        UrlPojo pojo = new UrlPojo(url,parasMap);
        
        HttpClientCrawlerImpl httpClientCrawlerImpl = new HttpClientCrawlerImpl(pojo);
        
        
        CrawlResultPojo resultPojo = httpClientCrawlerImpl.crawl();
        if (resultPojo != null) {
            System.out.println(resultPojo);
        }
    }
}