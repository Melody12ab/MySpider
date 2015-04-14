package com.caidongyu.application.encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.caidongyu.application.text.StringUtil;

/**
 * @author caidongyu_a
 * 一次加密：Des加密 基于 alphatable
 * 二次加密：base64、hex加密，其中base64依赖外部apache的commons-codec.jar
 */
public class DES2Util {
	private static String DES = "DES";
	private static String PASSWORD_CRYPT_KEY = "12345678";
	private static String DES_HS_0 = "0";

	public DES2Util(String key) {
		PASSWORD_CRYPT_KEY = key;
	}

	public String createDecryptor(String data) throws Exception {
		return decrypt(data);
	}

	public String createEncryptor(String data) throws Exception {
		return encrypt(data);
	}

	/**
	 * 加密
	 */
	public static byte[] encrypt(byte[] src, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 */
	public static byte[] decrypt(byte[] src, byte[] key)
			throws InvalidKeySpecException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		return cipher.doFinal(src);
	}

	/**
	 * 数据解密
	 */
	public final static String decrypt(String data) throws Exception {
		System.out.println("待解密的串 :" + data);
		try {
			if (!StringUtil.isBlank(data)) {
				return new String(decrypt(base64Decode(data),
						PASSWORD_CRYPT_KEY.getBytes()));
			}
		} catch (InvalidKeyException e) {
			throw new Exception("解密异常：", e);
		} catch (InvalidKeySpecException e) {
			throw new Exception("解密异常：", e);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("解密异常：", e);
		} catch (NoSuchPaddingException e) {
			throw new Exception("解密异常：", e);
		} catch (IllegalBlockSizeException e) {
			throw new Exception("解密异常：", e);
		} catch (BadPaddingException e) {
			throw new Exception("解密异常：", e);
		}
		return data;
	}

	public final static String base64Encode(byte[] data) {
		Base64 encoder = new Base64();
		return encoder.encodeToString(data);
	}

	public final static byte[] base64Decode(String data) {
		Base64 encoder = new Base64();
		return encoder.decode(data);
	}

	/**
	 * 数据加密
	 * @throws Exception 
	 */
	public final static String encrypt(String data) throws Exception {
		if (!StringUtil.isBlank(data)) {
			try {
				String encStr = base64Encode(encrypt(data.getBytes(),
						PASSWORD_CRYPT_KEY.getBytes()));
				System.out.println("加密后的串 : " + encStr);
				return encStr;
			} catch (InvalidKeyException e) {
				throw new RuntimeException(e);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			} catch (InvalidKeySpecException e) {
				throw new RuntimeException(e);
			} catch (NoSuchPaddingException e) {
				throw new RuntimeException(e);
			} catch (IllegalBlockSizeException e) {
				throw new RuntimeException(e);
			} catch (BadPaddingException e) {
				throw new RuntimeException(e);
			}
		}

		return data;
	}

	/**
	 * 二进制转字符串
	 */
	public static String byte2Hex(byte[] bhex) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < bhex.length; n++) {
			stmp = (java.lang.Integer.toHexString(bhex[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + DES_HS_0 + stmp;
			} else {
				hs = hs + stmp;
			}

		}
		return hs.toUpperCase();
	}

	public static byte[] hex2Byte(byte[] hexb) {
		if ((hexb.length % 2) != 0) {
			System.out.println(hexb + " 的长度为：" + hexb.length);
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] hexb2 = new byte[hexb.length / 2];
		for (int n = 0; n < hexb.length; n += 2) {
			String item = new String(hexb, n, 2);
			hexb2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return hexb2;
	}

	public static void main(String[] args) throws Exception {
		String encryptorString = "fangxu";
		DES2Util des = new DES2Util("123456we");
		String s = des.createEncryptor(encryptorString);
		System.out.println(s);
		s = des.createDecryptor(s);
		System.out.println(s);
	}
}