package com.melody.manager.httpclient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 包括单例 池 代理的配置 本例为单例
 * 
 * @author Administrator
 *
 */
public class HttpClientManager {
    private static CloseableHttpClient httpclient;

    private HttpClientManager() {
    }

    public static CloseableHttpClient getHttpClient() {
        if (httpclient == null) {
            return HttpClients.createDefault();
        }
        return httpclient;
    }
}
