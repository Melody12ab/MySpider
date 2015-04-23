package com.melody.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import sun.util.logging.resources.logging;

import com.melody.utils.MD5Util;

public class LoginEcho {
	
	public static final Logger logger=Logger.getLogger(LoginEcho.class);
	
	public static CloseableHttpClient client = HttpClients.createDefault();
	
	public static String getCookie() {
		StringBuilder sb=new StringBuilder();
		HttpPost post = new HttpPost("http://echo.kibey.com/system/site/login");
		post.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("Accept-Language", "zh-CN");
		post.setHeader("Connection", "keep-alive");
		String cookie = "PHPSESSID="
				+ MD5Util.MD5(System.currentTimeMillis() + "");
		post.setHeader("Cookie", cookie);
		post.setHeader("Host", "echo.kibey.com");
		post.setHeader("Referer",
				"http://echo.kibey.com/login?redirect=http%3A%2F%2Fecho.kibey.com%2F");
		post.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("LoginForm[password]", "***"));
		nvps.add(new BasicNameValuePair("LoginForm[rememberMe]", "0"));
		nvps.add(new BasicNameValuePair("LoginForm[username]",
				"*****"));
		nvps.add(new BasicNameValuePair("yt0", "登 录"));
		UrlEncodedFormEntity params = null;

		try {
			params = new UrlEncodedFormEntity(nvps, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		post.setEntity(params);
		HttpResponse response=null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {
			if(header.getName().contains("Set-Cookie")){
				sb.append(header.getValue().split(";")[0]+";");
			}
		}
//		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static boolean TestLogin(){
		StringBuilder source=new StringBuilder();
		String cookie=getCookie();
		logger.info("得到的cookie为"+cookie);
		
		HttpGet hg=new HttpGet("http://echo.kibey.com/");
		hg.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		hg.setHeader("Accept-Encoding", "gzip, deflate");
		hg.setHeader("Accept-Language", "zh-CN");
		hg.setHeader("Connection", "keep-alive");
		hg.setHeader("Host", "echo.kibey.com");
		hg.setHeader("Referer", "http://echo.kibey.com/login?redirect=http%3A%2F%2Fecho.kibey.com%2F");
		hg.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
		hg.setHeader("Cookie", cookie);
		
		HttpResponse response=null;
		BufferedReader br=null;
		String line=null;
		try {
			response=client.execute(hg);
			HttpEntity entity=response.getEntity();
			br=new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
			while((line=br.readLine())!=null){
				source.append(line+"\n");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
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
		return source.toString().contains("欢迎来到echo回声");
	}
	
	public static void main(String[] args){
		if(TestLogin()){
			System.out.println("login success!");
		}else{
			System.out.println("login failed!");
		}
	}
}
