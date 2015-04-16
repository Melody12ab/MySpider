package com.mbm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 对象与字节数组转换工具类
 * 
 * @author zel
 * 
 */
public class ObjectAndByteArrayConvertUtil {
	/**
	 * 将字节数组转换为对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object ByteArrayToObject(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		Object obj = null;
		try {
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("ByteArrayToObject字节对象转换为对象时，出现错误--"
					+ e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 将对象转换为字节数组
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] ObjectToByteArray(Object obj) {
		if (obj == null) {
			return null;
		}
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("ObjectToByteArray字节对象转换为对象时，出现错误--"
					+ e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}

}
