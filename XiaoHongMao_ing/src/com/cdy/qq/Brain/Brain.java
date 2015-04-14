package com.cdy.qq.Brain;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.caidongyu.application.collection.CollectionUtil;
import com.caidongyu.application.log.LogUtil;

/**
 * @author dongyu.cai 
 * 大脑，去网上查知识等等
 */
public class Brain {
	private static Brain instance = new Brain();

	private Brain() {
	}

	public static Brain getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		try {
			getInstance().askBaiDuZhiDao("你好");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public AskAndAnswerMODEL askBaiDuZhiDao(String question){
		try {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			int timeoutSocket = 5000;
			// Set the timeout in milliseconds until a connection is established.
			HttpConnectionParams.setConnectionTimeout(httpParameters,timeoutConnection);
			// in milliseconds which is the timeout for waiting for data.
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpGet getUrl = new HttpGet("http://zhidao.baidu.com/search?pn=0&fr=search&ie=gbk&word="+ URLEncoder.encode(question, "gbk"));
			HttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpResponse response = httpClient.execute(getUrl);

			Document doc = Jsoup.parse(EntityUtils.toString(response.getEntity(), "gbk"));
			Elements dls = doc.select("dl.dl");
			if(CollectionUtil.isEmpty(dls)){
				//CST
				return null;
			}else{
				AskAndAnswerMODEL baiDuZhiDaoSearchResult = new AskAndAnswerMODEL();
			
				for(Element dl:dls){
					StringBuffer ask = new StringBuffer();
					//title
					Elements dts = dl.select("dt");
					if(!CollectionUtil.isEmpty(dts)){
						Element dt = dts.get(0);
						ask.append(Jsoup.clean(dt.html(), new Whitelist()));
					}
					//ask
					Elements dd_asks = dl.select("dd.summary");
					if(!CollectionUtil.isEmpty(dd_asks)){
						Element dd_ask = dd_asks.get(0);
						dd_ask.select("i").remove();
						if(ask.length()>0){
							ask.append("  ");
						}
						ask.append(Jsoup.clean(dd_ask.html(), new Whitelist()));
					}
					//answer
					StringBuffer answer = new StringBuffer();
					Elements dd_answers = dl.select("dd.answer");
					if(!CollectionUtil.isEmpty(dd_answers)){
						Element dd_answer = dd_answers.get(0);
						dd_answer.select("i").remove();
						answer.append(Jsoup.clean(dd_answer.html(), new Whitelist()));
					}
					
					baiDuZhiDaoSearchResult.ask = ask.toString();
					baiDuZhiDaoSearchResult.answer = answer.toString();
					LogUtil.print("\n>>>>>>>>>>>>>>>>>>>>>");
					LogUtil.print("问："+baiDuZhiDaoSearchResult.ask);
					LogUtil.print("答："+baiDuZhiDaoSearchResult.answer);
					LogUtil.print("<<<<<<<<<<<<<<<<<<<<<<\n");
					break;
				}
				return baiDuZhiDaoSearchResult;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


}
