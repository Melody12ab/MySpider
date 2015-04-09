package com.melody.sixin;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.weibo.zel.entity.util.LoginPojo;
import com.weibo.zel.main.GetUserCookie;

public class MelodySiXin {
	public static void main(String[] args) throws Exception {
		// 对登陆微博的帐号、密码、uid的设置
		LoginPojo loginPojo = new LoginPojo();

		loginPojo.setUsername("mathmelody@sina.com");
		loginPojo.setPassword("zx8868108//");
		loginPojo.setUid("5311414858");

		// 非代理登陆
		GetUserCookie cookieUtil = new GetUserCookie(loginPojo);

		// 用代理登陆
		// GetUserCookie cookieUtil = new GetUserCookie(loginPojo, proxyPojo);

		String cookieString = cookieUtil.getCookies("7.0");
		if (cookieString == null || cookieString.isEmpty()) {
			System.out.println("最終得到的cookies為空，請檢查!");
		} else {
			System.out.println("cookieString---" + cookieString);
		}
		
		//私信功能
		String cookie="SUS=SID-5311414858-1428572268-GZ-nfoqx-85cb01771eca36e53782c02e6e6551cc; path=/; domain=.weibo.com;SUS=SID-5311414858-1428572268-GZ-nfoqx-85cb01771eca36e53782c02e6e6551cc; path=/; domain=.weibo.com; httponly;SUE=es%3D45ff061ff44779d9b7998d967f62593d%26ev%3Dv1%26es2%3Dffcff298bbba3a84d730a5ac3176f87d%26rs0%3DT2JW9b1g8kWhn8HwsJNJ%252BzxKGyV31zTcpvGfGx8pQ6I4uF1ilAmEAnbhSrZ%252BjZKxHpCLEhV6M4djUzt3SBulE6tiw7EPOnuOB7RqCvP%252Febi8IPzHzE0mRp5LpqhV2tna%252FP9HPn5YSFsNoGqW%252B63TNMTE6f7hVCxX0OMca2gc%252Fbg%253D%26rv%3D0;path=/;domain=.weibo.com;Httponly;SUP=cv%3D1%26bt%3D1428572268%26et%3D1428658668%26d%3Dc909%26i%3D51cc%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26uid%3D5311414858%26name%3Dmathmelody%2540sina.com%26nick%3D%25E7%2594%25A8%25E6%2588%25B75311414858%26fmp%3D%26lcp%3D;path=/;domain=.weibo.com;SUB=_2A254Ijg8DeTxGeNN6lMV8SrEzjSIHXVbVi70rDV8PUNbuNBeLVLukW9cb396PAZ_MohKRqPU4BnoYH6P_A..; path=/; domain=.weibo.com; httponly;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WW4fKlcAQvpUAGRuyZzhZuC5JpX5K2t; expires=Friday, 08-Apr-16 09:37:48 GMT; path=/; domain=.weibo.com;SUHB=0P1crjhMWaQGXN; expires=Friday, 08-Apr-16 09:37:48 GMT; path=/; domain=.weibo.com;SRT=E.vAfsJqmoiDmrKD4tveHwJXmBvXvCvXMPH!YjvnEABvzvvv4m-!6prXVmPN5SxvfCvvAivOmKvAmLvAmMvXvCmXmC*B.vAflW-P9Rc0lR-ykADvnJqiQVbiRVPBtS!r3JZPQVqbgVdWiMZ4siOzu4DbmKPWQiFSIUFiB4QSnP4EG4ePude9fdePDi49ndDPIJeA7; expires=Sunday, 06-Apr-25 09:37:48 GMT; path=/; domain=.passport.weibo.com; httponly;SRF=1428572268; expires=Sunday, 06-Apr-25 09:37:48 GMT; path=/; domain=.passport.weibo.com;ALF=1460108268; expires=Fri, 08-Apr-2016 09:37:48 GMT; path=/; domain=.weibo.com;myuid=5311414858;SinaRot_wb_r_topic=39;UV5PAGE=usr511_179;; UV5=usr319_182;;SSOLoginState=1428572268;;UOR=,,login.sina.com.cn;;_s_tentry=login.sina.com.cn;";
		
	}
}
