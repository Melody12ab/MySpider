package com.weibo.common.utils;
/*字符串 DESede(3DES) 加密*/
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.net.*;
import org.omg.IOP.*;
import org.springframework.security.core.codec.Base64;

import java.io.*;

public class ThreeDes {
	private static String spkey = "123456789abcdefGHIJKLMNO";
	private static final String Algorithm = "DESede"; // 定义 加密算法,可用
														// DES,DESede,Blowfish
	private static final boolean hexcase = false;
	private static final String b64pad = "=";
	private static final int chrsz = 8;

	// private static BASE64Coding base64=new BASE64Coding ();
	// keybyte为加密密钥，长度为24字节
	// src为被加密的数据缓冲区（源）
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {

			keybyte = spkey.getBytes();
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// keybyte为加密密钥，长度为24字节
	// src为加密后的缓冲区
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {

			keybyte = spkey.getBytes();
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// 转换成十六进制字符串
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	/* Hash算法采用SHA-1 */
	private static String createBase64Digest(String strEncryptValue)
			throws NoSuchAlgorithmException {
		String strSHA1Value = null;

		// *********************************************************************
		byte[] bHashValue = null;
		try {
			MessageDigest mdSHA1 = MessageDigest.getInstance("SHA-1");
			mdSHA1.update(strEncryptValue.getBytes());

			bHashValue = mdSHA1.digest();
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}
		// *********************************************************************

		strSHA1Value = new String(bHashValue);
		return strSHA1Value;
	}

	// 得到字符串SHA-1值的方法
	public static String hex_sha1(String s) {
		s = (s == null) ? "" : s;
		return binb2hex(core_sha1(str2binb(s), s.length() * chrsz));
	}

	public static String b64_hmac_sha1(String key, String data) {
		return binb2b64(core_hmac_sha1(key, data));
	}

	public static String b64_sha1(String s) {
		s = (s == null) ? "" : s;
		return binb2b64(core_sha1(str2binb(s), s.length() * chrsz));
	}

	private static String binb2b64(int[] binarray) {
		String tab = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0123456789+/";
		String str = "";
		binarray = strechbinarray(binarray, binarray.length * 4);

		for (int i = 0; i < binarray.length * 4; i += 3) {
			int triplet = (((binarray[i >> 2] >> 8 * (3 - i % 4)) & 0xff) << 16)
					| (((binarray[i + 1 >> 2] >> 8 * (3 - (i + 1) % 4)) & 0xff) << 8)
					| ((binarray[i + 2 >> 2] >> 8 * (3 - (i + 2) % 4)) & 0xff);

			for (int j = 0; j < 4; j++) {
				if (i * 8 + j * 6 > binarray.length * 32) {
					str += b64pad;
				} else {
					str += tab.charAt((triplet >> 6 * (3 - j)) & 0x3f);
				}
			}
		}

		return cleanb64str(str);
	}

	private static String binb2hex(int[] binarray) {
		String hex_tab = hexcase ? "0123456789abcdef" : "0123456789abcdef";
		String str = "";

		for (int i = 0; i < binarray.length * 4; i++) {
			char a = (char) hex_tab
					.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8 + 4)) & 0xf);
			char b = (char) hex_tab
					.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8)) & 0xf);
			str += (new Character(a).toString() + new Character(b).toString());
		}

		return str;
	}

	private static String binb2str(int[] bin) {
		String str = "";
		int mask = (1 << chrsz) - 1;

		for (int i = 0; i < bin.length * 32; i += chrsz) {
			str += (char) ((bin[i >> 5] >>> (24 - i % 32)) & mask);
		}

		return str;
	}

	private static int bit_rol(int num, int cnt) {
		return (num << cnt) | (num >>> (32 - cnt));
	}

	private static String cleanb64str(String str) {
		str = (str == null) ? "" : str;
		int len = str.length();

		if (len <= 1) {
			return str;
		}

		char trailchar = str.charAt(len - 1);
		String trailstr = "";

		for (int i = len - 1; i >= 0 && str.charAt(i) == trailchar; i--) {
			trailstr += str.charAt(i);
		}

		return str.substring(0, str.indexOf(trailstr));
	}

	private static int[] complete216(int[] oldbin) {
		if (oldbin.length >= 16) {
			return oldbin;
		}

		int[] newbin = new int[16 - oldbin.length];

		for (int i = 0; i < newbin.length; newbin[i] = 0, i++)
			;

		return concat(oldbin, newbin);
	}

	private static int[] concat(int[] oldbin, int[] newbin) {
		int[] retval = new int[oldbin.length + newbin.length];

		for (int i = 0; i < (oldbin.length + newbin.length); i++) {
			if (i < oldbin.length) {
				retval[i] = oldbin[i];
			} else {
				retval[i] = newbin[i - oldbin.length];
			}
		}

		return retval;
	}

	private static int[] core_hmac_sha1(String key, String data) {
		key = (key == null) ? "" : key;
		data = (data == null) ? "" : data;
		int[] bkey = complete216(str2binb(key));

		if (bkey.length > 16) {
			bkey = core_sha1(bkey, key.length() * chrsz);
		}

		int[] ipad = new int[16];
		int[] opad = new int[16];

		for (int i = 0; i < 16; ipad[i] = 0, opad[i] = 0, i++)
			;

		for (int i = 0; i < 16; i++) {
			ipad[i] = bkey[i] ^ 0x36363636;
			opad[i] = bkey[i] ^ 0x5c5c5c5c;
		}

		int[] hash = core_sha1(concat(ipad, str2binb(data)), 512
				+ data.length() * chrsz);

		return core_sha1(concat(opad, hash), 512 + 160);
	}

	private static int[] core_sha1(int[] x, int len) {
		int size = (len >> 5);
		x = strechbinarray(x, size);
		x[len >> 5] |= 0x80 << (24 - len % 32);
		size = ((len + 64 >> 9) << 4) + 15;
		x = strechbinarray(x, size);
		x[((len + 64 >> 9) << 4) + 15] = len;

		int[] w = new int[80];
		int a = 1732584193;
		int b = -271733879;
		int c = -1732584194;
		int d = 271733878;
		int e = -1009589776;

		for (int i = 0; i < x.length; i += 16) {
			int olda = a;
			int oldb = b;
			int oldc = c;
			int oldd = d;
			int olde = e;

			for (int j = 0; j < 80; j++) {
				if (j < 16) {
					w[j] = x[i + j];
				} else {
					w[j] = rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);
				}

				int t = safe_add(safe_add(rol(a, 5), sha1_ft(j, b, c, d)),
						safe_add(safe_add(e, w[j]), sha1_kt(j)));

				e = d;
				d = c;
				c = rol(b, 30);
				b = a;
				a = t;
			}

			a = safe_add(a, olda);
			b = safe_add(b, oldb);
			c = safe_add(c, oldc);
			d = safe_add(d, oldd);
			e = safe_add(e, olde);
		}

		int[] retval = new int[5];

		retval[0] = a;
		retval[1] = b;
		retval[2] = c;
		retval[3] = d;
		retval[4] = e;

		return retval;
	}

	private static void dotest() {
		String key = "key";
		String data = "data";
		System.out.println("hex_sha1(" + data + ")=" + hex_sha1(data));
		System.out.println("b64_sha1(" + data + ")=" + b64_sha1(data));
		System.out.println("str_sha1(" + data + ")=" + str_sha1(data));
		System.out.println("hex_hmac_sha1(" + key + "," + data + ")="
				+ hex_hmac_sha1(key, data));
		System.out.println("b64_hmac_sha1(" + key + "," + data + ")="
				+ b64_hmac_sha1(key, data));
		System.out.println("str_hmac_sha1(" + key + "," + data + ")="
				+ str_hmac_sha1(key, data));
	}

	public static String hex_hmac_sha1(String key, String data) {
		return binb2hex(core_hmac_sha1(key, data));
	}

	private static int rol(int num, int cnt) {
		return (num << cnt) | (num >>> (32 - cnt));
	}

	private static int safe_add(int x, int y) {
		int lsw = (int) (x & 0xffff) + (int) (y & 0xffff);
		int msw = (x >> 16) + (y >> 16) + (lsw >> 16);

		return (msw << 16) | (lsw & 0xffff);
	}

	private static int sha1_ft(int t, int b, int c, int d) {
		if (t < 20)
			return (b & c) | ((~b) & d);

		if (t < 40)
			return b ^ c ^ d;

		if (t < 60)
			return (b & c) | (b & d) | (c & d);

		return b ^ c ^ d;
	}

	private static int sha1_kt(int t) {
		return (t < 20) ? 1518500249 : (t < 40) ? 1859775393
				: (t < 60) ? -1894007588 : -899497514;
	}

	private static boolean sha1_vm_test() {
		return hexcase ? hex_sha1("abc").equals(
				"a9993e364706816aba3e25717850c26c9cd0d89d") : hex_sha1("abc")
				.equals("a9993e364706816aba3e25717850c26c9cd0d89d");
	}

	public static String str_hmac_sha1(String key, String data) {
		return binb2str(core_hmac_sha1(key, data));
	}

	public static String str_sha1(String s) {
		s = (s == null) ? "" : s;

		return binb2str(core_sha1(str2binb(s), s.length() * chrsz));
	}

	private static int[] str2binb(String str) {
		str = (str == null) ? "" : str;

		int[] tmp = new int[str.length() * chrsz];
		int mask = (1 << chrsz) - 1;

		for (int i = 0; i < str.length() * chrsz; i += chrsz) {
			tmp[i >> 5] |= ((int) (str.charAt(i / chrsz)) & mask) << (24 - i % 32);
		}

		int len = 0;
		for (int i = 0; i < tmp.length && tmp[i] != 0; i++, len++)
			;

		int[] bin = new int[len];

		for (int i = 0; i < len; i++) {
			bin[i] = tmp[i];
		}

		return bin;
	}

	private static int[] strechbinarray(int[] oldbin, int size) {
		int currlen = oldbin.length;

		if (currlen >= size + 1) {
			return oldbin;
		}

		int[] newbin = new int[size + 1];
		for (int i = 0; i < size; newbin[i] = 0, i++)
			;

		for (int i = 0; i < currlen; i++) {
			newbin[i] = oldbin[i];
		}

		return newbin;
	}

	public static void main(String[] args) {
		// 添加新安全算法,如果用JCE就要把它添加进去
		try {
			
//			String digestSrc = hex_sha1(szSrc);
			
//			byte[] nameBytes = Base64.encode(URLEncoder.encode("wysxz34450878@sina.com").getBytes()) ;
//			
//			System.out.println(new String(nameBytes));
//
//			String password = hex_sha1("" + hex_sha1(hex_sha1("wysxzw")) + 1321513327 + "YO1G6A") ;
//			System.out.println(password);
			
//			System.out.println("http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack&retcode=101&reason=%B5%C7%C2%BC%C3%FB%BB%F2%C3%DC%C2%EB%B4%ED%CE%F3");
			
			System.out.println("\u767b\u5f55\u540d\u6216\u5bc6\u7801\u9519\u8bef");

		} catch (Exception e) {

			System.out.println(e);
		}
	}

}