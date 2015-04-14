package com.cdy.qzone.core;

import com.caidongyu.application.log.LogUtil;
import com.caidongyu.application.text.NumberUtil;
import com.cdy.qzone.entity.QZoneService;
import com.cdy.qzone.util.SimpleUtil;

/**
 * @author dongyu.cai
 * 目前提供的QZone的一些操作
 */
public class QZone extends QZoneService{
	
	
	/**
	 * 创建相册
	 */
	@Deprecated
	public String addAlbum(String name){
		//POST:
		//url->http://shanghai.photo.qq.com/cgi-bin/common/cgi_add_album_v2?g_tk=195578052
		//params->qzreferrer=http%3A%2F%2Fcnc.qzs.qq.com%2Fqzone%2Fphoto%2Fzone%2FaddAlbum.html&inCharset=gbk&outCharset=gbk&hostUin=2481662874&notice=0&callbackFun=_Callback&format=fs&plat=qzone&source=qzone&appid=4&uin=2481662874&album_type=&birth_time=&degree_type=0&enroll_time=&albumname=1557656909&albumdesc=&albumclass=107&priv=1&question=&answer=&whiteList=&bitmap=10000011
		
		String $url = "http://shanghai.photo.qq.com/cgi-bin/common/cgi_add_album_v2?g_tk="+this.g_tk;
		String $data = "qzreferrer=http%3A%2F%2Fcnc.qzs.qq.com%2Fqzone%2Fphoto%2Fzone%2FaddAlbum.html"
				+ "&inCharset=gbk"
				+ "&outCharset=gbk"
				+ "&hostUin="+this.uin
				+ "&notice=0"
				+ "&callbackFun=_Callback"
				+ "&format=fs"
				+ "&plat=qzone"
				+ "&source=qzone"
				+ "&appid=4"
				+ "&uin="+this.uin
				+ "&album_type="
				+ "&birth_time="
				+ "&degree_type=0"
				+ "&enroll_time="
				+ "&albumname="+name	//相册名称
				+ "&albumdesc="
				+ "&albumclass=107"		//默认分类，107：其他
				+ "&priv=1"
				+ "&question="
				+ "&answer="
				+ "&whiteList="
				+ "&bitmap=10000000";
		return this.requestPost($url, $data);
	}
	
	/**
	 * 给好友发送留言
	 */
	public String sendMsg(String friendQQ,String content){
		//qzreferrer=http://user.qzone.qq.com/79594225&hostUin=79594225&uin=1827985499&format=fs&iNotice=1&inCharset=utf-8&outCharset=utf-8&ref=qzone&json=1&secret=0&content=大王叫我来巡山！
		//qzreferrer=http://user.qzone.qq.com/{0}&hostUin={0}&uin={1}&format=fs&iNotice=1&inCharset=utf-8&outCharset=utf-8&ref=qzone&json=1&secret=0&content=大王叫我来巡山！
		String $url = "http://m.qzone.qq.com/cgi-bin/new/add_msgb?g_tk="+this.g_tk;
		String $params = "qzreferrer=http://user.qzone.qq.com/{0}&hostUin={0}&uin={1}&format=fs&iNotice=1&inCharset=utf-8&outCharset=utf-8&ref=qzone&json=1&secret=0&content={2}";
		//{0}	friendQQ
		//{1}	this.qq
		//{2} 	content
		$params = SimpleUtil.fillString($params, friendQQ,this.uin,content);
		return this.requestPost($url, $params);
	}
	
	
	/**
	 * 获取我的好友关系
	 * 无法获取他人的好友，这个url是需要登录的
	 */
	public String getMyFriendShip(){
		//http://r.qzone.qq.com/cgi-bin/tfriend/friend_ship_manager.cgi?uin=1827985499&do=1&rd=0.3116785184139983&fupdate=1&clean=1&g_tk=1940508927
		String $url = "http://r.qzone.qq.com/cgi-bin/tfriend/friend_ship_manager.cgi?uin={0}&do=1&rd={1}&fupdate=1&clean=1&g_tk={2}";
		//{0} this.uin
		//{1} 随机小数	16位
		//{2} this.g_tk
		$url = SimpleUtil.fillString($url, this.uin,NumberUtil.getRandom(16),this.g_tk);
		return this.requestGet($url);
	}
	
	/**
	 * 获取指定qq号的最近访问者
	 * @param friendQQ
	 * @return
	 */
	public String getVisitorByQQ(String friendQQ){
		//http://g.cnc.qzone.qq.com/cgi-bin/friendshow/cgi_get_visitor_simple?uin=673872882&mask=2&g_tk=1940508927&page=1&fupdate=1
		String $url = "http://g.cnc.qzone.qq.com/cgi-bin/friendshow/cgi_get_visitor_simple?uin={0}&mask=2&g_tk={1}&page=1&fupdate=1";
		//{0} friendQQ
		//{1} this.g_tk
		$url = SimpleUtil.fillString($url, friendQQ,this.g_tk);
		return this.requestGet($url);
	}
	
	/**
	 * 发布一条说说
	 * @param content
	 * @return
	 */
	public String publishOneShuoShuo(String content){
		String $url = "http://user.qzone.qq.com/q/taotao/cgi-bin/emotion_cgi_publish_v6?g_tk="+this.g_tk;
		String $param = "syn_tweet_verson=1&paramstr=1&pic_template=&richtype=&richval=&special_url=&subrichtype=&con={0}&feedversion=1&ver=1&ugc_right=1&to_tweet=0&to_sign=0&hostuin={1}&code_version=1&format=fs&qzreferrer=http%3A%2F%2Fuser.qzone.qq.com%2F{1}%2F311";
		//{0}	内容
		//{1}	this.uin
		$param = SimpleUtil.fillString($param, content,this.uin);
		return LogUtil.print(this.requestPost($url, $param));
	}
	
	/**
	 * 及时的点赞好友的新说说
	 * 线程定时，2秒点赞，最近的一条
	 * @param friendsQQ
	 */
	public void praiseFriendsNewShuoShuoTimely(String friendsQQ){
		String[] friendsQQ_ary = friendsQQ.split(",");
		OperationTask task = new OperationTask(this,friendsQQ_ary);
		task.praiseFriendsNewShuoShuoTimely();
	}
	
	
	
	/**
	 * 获取说说的点赞情况
	 * 比如我有没有点赞过
	 * 有多少个人点过赞，都是谁
	 */
	public String getShuoShuoPraiseInfo(String friendQQ,String tid){
		//http://r.cnc.qzone.qq.com/cgi-bin/user/qz_opcnt2?g_tk=195578052
		//qzreferrer=http%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2F311&_stp=1422187332471&unikey=http%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d71004583c5c35482f80400.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d71004583c5c35482f80400.1%3C%7C%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100454e70c25442790a00.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100454e70c25442790a00.1%3C%7C%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100451820c15425410300.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100451820c15425410300.1%3C%7C%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d710045c5d2bf54e8040300.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d710045c5d2bf54e8040300.1%3C%7C%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d710045d487be543da60700.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d710045d487be543da60700.1%3C%7C%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100457396bd54b0400c00.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100457396bd54b0400c00.1%3C%7C%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100455973bc544b5d0e00.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100455973bc544b5d0e00.1%3C%7C%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100457398ba54d0da0e00.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100457398ba54d0da0e00.1%3C%7C%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100453df3b954eb250000.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d7100453df3b954eb250000.1%3C%7C%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d710045d274b85498bb0c00.1%3C.%3Ehttp%3A%2F%2Fuser.qzone.qq.com%2F1157656909%2Fmood%2F4d710045d274b85498bb0c00.1&face=0%3C%7C%3E0%3C%7C%3E0%3C%7C%3E0%3C%7C%3E0%3C%7C%3E0%3C%7C%3E0%3C%7C%3E0%3C%7C%3E0%3C%7C%3E0&fupdate=1
		String $url = "http://r.cnc.qzone.qq.com/cgi-bin/user/qz_opcnt2?g_tk="+this.g_tk;
		String $param = "unikey=http://user.qzone.qq.com/{0}/mood/{1}%3C.%3Ehttp://user.qzone.qq.com/{0}/mood/{1}";

		if(!tid.endsWith(".1")) tid = tid+".1";//加.1是赞
		$param = SimpleUtil.fillString($param, friendQQ,tid);
		return this.requestPost($url, $param);
	}
	
	/**
	 * 得到好友所有说说
	 * @param friendQQ
	 * @return
	 */
	public String getLastShuoShuo(String friendQQ){
					 //http://taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6?uin=79594225&ftype=0&sort=0&pos=0&num=20&replynum=100&g_tk=195578052&callback=_preloadCallback&code_version=1&format=jsonp&need_private_comment=1
		String $url = "http://taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6?uin={0}&ftype=0&sort=0&pos=0&num={2}&replynum=100&g_tk={1}&callback=_preloadCallback&code_version=1&format=jsonp&need_private_comment=1";
		//{0}	friendQQ
		//{1}	this.g_tk
		//{2}	获取的最近的说说条数
		$url = SimpleUtil.fillString($url,friendQQ,this.g_tk,"1");
		return this.requestGet($url);
	}
	
	
	/**
	 * 点赞
	 * dongyu.cai
	 * @param friendQQ	好友qq号
	 * @param tid	好友说说id	example:7b6eb61aa5616f52da7c0b00.1
	 * 									4d7100451941b554f85d0200
	 */
	public void praise(String friendQQ,String tid){
		if(this.status != 0) return;
		String $url = "http://w.cnc.qzone.qq.com/cgi-bin/likes/internal_dolike_app?g_tk="+this.g_tk;
		String $param = "qzreferrer=http%3A%2F%2Fuser.qzone.qq.com%2F{0}&opuin={1}&unikey=http%3A%2F%2Fuser.qzone.qq.com%2F{0}%2Fmood%2F{2}&curkey=http%3A%2F%2Fuser.qzone.qq.com%2F{0}%2Fmood%2F{2}&from=-100&fupdate=1&face=0";
//		{0} 说说发布者的qq号
//		{1}	登陆者uin
//		{2}	说说id  
		if(!tid.endsWith(".1")) tid = tid+".1";//加.1是赞
		$param = SimpleUtil.fillString($param, friendQQ,this.uin,tid);
		LogUtil.print(this.requestPost($url, $param));
	}
	
	
	

	public QZone(String uin,String password){
		this.uin = uin;
		this.password = password;
	}
	private static final long serialVersionUID = 1L;
}
