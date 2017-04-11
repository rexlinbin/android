package com.utils.tools;



import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.json.JSONObject;

import android.util.Base64;

/**
 * 
 * 3DES加密工具类
 * 
 * 
 * 
 * @author liufeng
 * 
 * @date 2012-10-11
 */

public class Des3 {

	public void testencode() throws Exception {
		JSONObject js = new JSONObject();
		js.put("username", "中文abc123");
		js.put("password", "0123中文abc123abc");
		String param = js.toString();
		String result = encode(param);
		System.out.println("result:" + result);
		
		String result1 = decode("77TSctFPmXcudF4xnf/s0nWrwzUdaPoMJNevnUgqIoRC5mIqZd32cw==");
		System.out.println("paramsString:" + result1);
	}

	// 密钥

	private final static String secretKey = "xxxxxxxxxxxxxxxxxxxxxxxxxx";

	// 向量

	private final static String iv = "01234567";

	// 加解密统一使用的编码方式

	private final static String encoding = "utf-8";

	/**
	 * 
	 * 3DES加密
	 * 
	 * 
	 * 
	 * @param plainText
	 *            普通文本
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public static String encode(String plainText) throws Exception {

		Key deskey = null;

		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());

		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");

		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");

		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());

		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);

		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));

		return new String(Base64.encode(encryptData, Base64.DEFAULT));

	}

	/**
	 * 
	 * 3DES解密
	 * 
	 * 
	 * 
	 * @param encryptText
	 *            加密文本
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public static String decode(String encryptText) throws Exception {

		Key deskey = null;

		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());

		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");

		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");

		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());

		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText, Base64.DEFAULT));

		return new String(decryptData, encoding);

	}
}
