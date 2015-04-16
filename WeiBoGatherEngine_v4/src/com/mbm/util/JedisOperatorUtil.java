package com.mbm.util;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.weibo.common.utils.StaticValue;

public class JedisOperatorUtil {
	public static Logger logger = Logger.getLogger(JedisOperatorUtil.class);
	static Jedis jedis = null;
	static {
		jedis = new Jedis(StaticValue.redis_host, StaticValue.redis_port);// 链接上redis
		jedis.auth(StaticValue.redis_password);
		logger.info("Redis客户端已成功连接至服务器端!");
	}

	public static boolean putObj(byte[] key, byte[] value) {
		try {
			jedis.set(key, value);
		} catch (Exception e) {
			logger.info("redis在set时，出现异常，将重新联接一次");
			jedis = new Jedis(StaticValue.redis_host, StaticValue.redis_port);// 链接上redis
			jedis.auth(StaticValue.redis_password);
			logger.info("Redis客户端已成功连接至服务器端!");
			jedis.set(key, value);
		}
		return true;
	}

	public static byte[] getObj(byte[] key) {
		try {
			if(key==null){
				return null;
			}
			return jedis.get(key);
		} catch (Exception e) {
			logger.info("redis在getObj时，出现异常，将重新联接一次");
			jedis = new Jedis(StaticValue.redis_host, StaticValue.redis_port);// 链接上redis
			jedis.auth(StaticValue.redis_password);
			logger.info("Redis客户端已成功连接至服务器端!");
			return jedis.get(key);
		}
	}

	public static void saveAll() {
		try {
			jedis.save();
		} catch (Exception e) {
			logger.info("redis在getObj时，出现异常，将重新联接一次");
			jedis = new Jedis(StaticValue.redis_host, StaticValue.redis_port);// 链接上redis
			jedis.auth(StaticValue.redis_password);
			logger.info("Redis客户端已成功连接至服务器端!");
			jedis.save();
		}
	}
}
