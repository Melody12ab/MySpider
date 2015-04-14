package com.cdy.qzone.interfaced;

import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 * <p>提供在请求前后，设置请求参数的接/p>
 * @category  类名
 * @version   1.0     (2013-2-15 下午1:48:46)
 * @since     1.0
 * @author    liyang  (liyangchiyue@gmail.com)
 * 
 */
public interface SetHttpConnection{
	/** 设置发送请求前的处理请求方法 
	 * @throws ProtocolException */
	public String before(HttpURLConnection httpConn) throws ProtocolException;
	/** 设置发送后的请求处理方法 */
	public String after(HttpURLConnection httpConn);
}