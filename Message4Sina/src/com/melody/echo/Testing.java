package com.melody.echo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Testing {

	public static void main(String[] args) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		BufferedReader br = null;
		try {
			HttpGet httpGet = new HttpGet("http://www.baidu.com");
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			br = new BufferedReader(new InputStreamReader(response1.getEntity()
					.getContent(), "UTF-8"));
			while(br.readLine()!=null){
				
			}
		} finally {
			httpclient.close();
		}
	}

}
