package com.utils.tools;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static final String CipherMode = "AES/CBC/PKCS7Padding";
	private static String keyString = "3c817ce736f2d23d";
	private static String ivString = "f9f5d465c74872da";

	/** 创建密钥 **/
	private static SecretKeySpec createKey(String key) {
		byte[] data = null;
		if (key == null) {
			key = "";
		}
		StringBuffer sb = new StringBuffer(16);
		sb.append(key);
		while (sb.length() < 16) {
			sb.append("0");
		}
		if (sb.length() > 16) {
			sb.setLength(16);
		}

		try {
			data = sb.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new SecretKeySpec(data, "AES");
	}

	private static IvParameterSpec createIV(String iv) {
		byte[] data = null;
		if (iv == null) {
			iv = "";
		}
		StringBuffer sb = new StringBuffer(16);
		sb.append(iv);
		while (sb.length() < 16) {
			sb.append("0");
		}
		if (sb.length() > 16) {
			sb.setLength(16);
		}

		try {
			data = sb.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new IvParameterSpec(data);
	}

	/** 加密字节数据 **/
	public static byte[] encrypt(byte[] content, String keyString, String ivString) {
		try {
			SecretKeySpec key = createKey(keyString);
			Cipher cipher = Cipher.getInstance(CipherMode);
			cipher.init(Cipher.ENCRYPT_MODE, key, createIV(ivString));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 加密字节数据 **/
	public static byte[] encrypt(byte[] content) {
		try {
			SecretKeySpec key = createKey(keyString);
			Cipher cipher = Cipher.getInstance(CipherMode);
			cipher.init(Cipher.ENCRYPT_MODE, key, createIV(ivString));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 加密(结果为16进制字符串) **/
	public static String encrypt(String content, String keyString, String ivString) {
		byte[] data = null;
		try {
			data = content.getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = encrypt(data, keyString, ivString);
		String result = byte2hex(data);
		return result;
	}
	
	/** 加密(结果为16进制字符串) **/
	public static String encrypt(String content) {
		byte[] data = null;
		try {
			data = content.getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = encrypt(data, keyString, ivString);
		String result = byte2hex(data);
		return result;
	}

	/** 解密字节数组 **/
	public static byte[] decrypt(byte[] content, String keyString, String ivString) {
		try {
			SecretKeySpec key = createKey(keyString);
			Cipher cipher = Cipher.getInstance(CipherMode);
			cipher.init(Cipher.DECRYPT_MODE, key, createIV(ivString));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 解密字节数组 **/
	public static byte[] decrypt(byte[] content) {
		try {
			SecretKeySpec key = createKey(keyString);
			Cipher cipher = Cipher.getInstance(CipherMode);
			cipher.init(Cipher.DECRYPT_MODE, key, createIV(ivString));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 解密(输出结果为字符串) **/
	public static String decrypt(String content, String keyString, String ivString) {
		byte[] data = null;
		try {
			data = hex2byte(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = decrypt(data, keyString, ivString);
		if (data == null)
			return null;
		String result = null;
		try {
			result = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** 解密(输出结果为字符串) **/
	public static String decrypt(String content) {
		byte[] data = null;
		try {
			data = hex2byte(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = decrypt(data, keyString, ivString);
		if (data == null)
			return null;
		String result = null;
		try {
			result = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/** 字节数组转成16进制字符串 **/
	public static String byte2hex(byte[] b) { // 一个字节的数，
		StringBuffer sb = new StringBuffer(b.length * 2);
		String tmp = "";
		for (int n = 0; n < b.length; n++) {
			// 整数转成十六进制表示
			tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
		}
		return sb.toString().toUpperCase(); // 转成大写
	}

	/** 将hex字符串转换成字节数组 **/
	private static byte[] hex2byte(String inputString) {
		if (inputString == null || inputString.length() < 2) {
			return new byte[0];
		}
		inputString = inputString.toLowerCase();
		int l = inputString.length() / 2;
		byte[] result = new byte[l];
		for (int i = 0; i < l; ++i) {
			String tmp = inputString.substring(2 * i, 2 * i + 2);
			result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
		}
		return result;
	}
}