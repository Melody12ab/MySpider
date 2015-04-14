package com.cdy.qzone.core;

import org.json.JSONArray;
import org.json.JSONObject;

import com.caidongyu.application.collection.CollectionUtil;
import com.caidongyu.application.log.LogUtil;
import com.cdy.qzone.entity.HttpService;
import com.cdy.qzone.util.SimpleUtil;

public class OperationTask extends HttpService{
	private static final long serialVersionUID = 1L;
	
	private QZone qzone;
	private String[] friendsQQ;
	
	public OperationTask(QZone qzone,String[] friendsQQ) {
		this.qzone = qzone;
		this.friendsQQ = friendsQQ;
	}
	
	public void praiseFriendsNewShuoShuoTimely(){
		if(qzone != null && !CollectionUtil.isEmpty(this.friendsQQ)){
			//把登陆后的cookie保存起来了，
			//因为每次http请求之后呢，会把cookie更新，但是奇怪的是，
			//一更新，点赞、获取说说就会变成未登录状态
			//所以我干脆把登陆后的cookie给本地保存起来，在不好使的请求中，使用登陆后的cookie
			final String cookie = qzone.cookie;
			new Thread(){
				public void run() {
					while(true){
						for(String friendQQ:friendsQQ){
//							LogUtil.print(html);
//							String html = "callback({\"msglist\":[{\"tid\":\"123321\",\"name\":\"好友昵称\",\"createTime\":\"00.1\",\"content\":\"内容...\"}]});";
							try {
								qzone.cookie = cookie;
								String shuoshuoListHtml = SimpleUtil.getCallBackContent(qzone.getLastShuoShuo(friendQQ));
								JSONObject shushuoJson = new JSONObject(shuoshuoListHtml);
								if(shushuoJson.has("msglist")){
									JSONArray shuoshuoList = shushuoJson.getJSONArray("msglist");
									for(int i=0;i<shuoshuoList.length();){
										JSONObject shuoshuo = shuoshuoList.getJSONObject(i);
										String tid = shuoshuo.getString("tid");
										qzone.cookie = cookie;
										String praiseInfoHtml = SimpleUtil.getCallBackContent(qzone.getShuoShuoPraiseInfo(friendQQ, tid));
										JSONObject shuoShuoPraiseInfoJson = new JSONObject(praiseInfoHtml);
										if(shuoShuoPraiseInfoJson.has("data")){
											JSONArray shuoShuoPraiseInfo = shuoShuoPraiseInfoJson.getJSONArray("data");
											if(shuoShuoPraiseInfo.length() > 0){
												JSONObject currentShuoShuoPraiseInfo = shuoShuoPraiseInfo.getJSONObject(0);
												int ilike = currentShuoShuoPraiseInfo.getJSONObject("current").getJSONObject("likedata").getInt("ilike");
												if(ilike == 0){
													LogUtil.print("点赞->"+shuoshuo.getString("name")+" "+shuoshuo.getString("createTime")+" 的说说:"+shuoshuo.getString("content"));
													qzone.praise(friendQQ, tid);
												}else{
													//LogUtil.print("已赞->"+shuoshuo.getString("name")+" "+shuoshuo.getString("createTime")+" 的说说:"+shuoshuo.getString("content"));
												}
											}else{
												throw new Exception("异常的点赞信息:"+praiseInfoHtml);
											}
										}else{
											throw new Exception("异常的点赞信息:"+praiseInfoHtml);
										}
										break;
									}
								}else{
									throw new Exception("异常的说说列表:"+shuoshuoListHtml);
								}
							} catch (Exception e) {
								LogUtil.print("[点赞异常]"+e.getMessage());
							}
						}
						
						try {
							System.out.print("+");
							Thread.sleep(2000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}
	
	
}
