package com.bccv.boxcomic.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.bccv.boxcomic.tool.ConstantValue;
import com.bccv.boxcomic.tool.Des3;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.MD5Util;
import com.bccv.boxcomic.tool.StringUtils;

;

/**
 * 通用的连接工具（wap）
 * 
 * @author
 * 
 */
public class HttpClientUtil {
	private HttpGet get;
	private static HttpPost post;

	private static HttpResponse response;

	private static HttpClient client;
	private static Header[] headers = new Header[13];

	public HttpClientUtil() {

		client = new DefaultHttpClient();

		// 判断是否读取到了ip信息（代理）
		if (!StringUtils.isEmpty(GlobalParams.PROXY_IP)) {
			// wap方式上网
			HttpHost host = new HttpHost(GlobalParams.PROXY_IP,
					GlobalParams.PROXY_PORT);
			client.getParams()
					.setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
		}

		// 设置头的数据
		headers[0] = new BasicHeader("Appkey", "12343");
		headers[1] = new BasicHeader("Udid", "");// 手机串号
		headers[2] = new BasicHeader("Os", "android");//
		headers[3] = new BasicHeader("Osversion", "");//
		headers[4] = new BasicHeader("Appversion", "");// 1.0
		headers[5] = new BasicHeader("Sourceid", "");//
		headers[6] = new BasicHeader("Ver", "");

		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");

		headers[9] = new BasicHeader("Unique", "");
		
		long time = System.currentTimeMillis();
		
		headers[10] = new BasicHeader("key", MD5Util.string2MD5(time + GlobalParams.md5String));
		headers[11] = new BasicHeader("secret", time + "");
		headers[12] = new BasicHeader("user-agent", "BCCV/" + GlobalParams.user_agentString);
	}

	public HttpClientUtil(Context context) {
		InputStream ins = null;
		String result = "";

		try {
			ins = context.getAssets().open("comic.cer");
			// 下载的证书放到项目中的assets目录中
			CertificateFactory cerFactory = CertificateFactory
					.getInstance("X.509");
			Certificate cer = cerFactory.generateCertificate(ins);
			KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);

			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
			Scheme sch = new Scheme("https", socketFactory, 443);
			client = new DefaultHttpClient();
			client.getConnectionManager().getSchemeRegistry().register(sch);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 判断是否读取到了ip信息（代理）
		if (!StringUtils.isEmpty(GlobalParams.PROXY_IP)) {
			// wap方式上网
			HttpHost host = new HttpHost(GlobalParams.PROXY_IP,
					GlobalParams.PROXY_PORT);
			client.getParams()
					.setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
		}
		// 设置头的数据
		headers[0] = new BasicHeader("Appkey", "12343");
		headers[1] = new BasicHeader("Udid", "");// 手机串号
		headers[2] = new BasicHeader("Os", "android");//
		headers[3] = new BasicHeader("Osversion", "");//
		headers[4] = new BasicHeader("Appversion", "");// 1.0
		headers[5] = new BasicHeader("Sourceid", "");//
		headers[6] = new BasicHeader("Ver", "");

		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");

		headers[9] = new BasicHeader("Unique", "");
		
		headers[10] = new BasicHeader("key", MD5Util.string2MD5(GlobalParams.md5String));
		headers[11] = new BasicHeader("secret", System.currentTimeMillis() + "");
		headers[12] = new BasicHeader("user-agent", "BCCV/" + GlobalParams.user_agentString);
	}

	/**
	 * 发送xml文件
	 * 
	 * @param xml
	 * @return
	 */
	public InputStream sendXml(String uri, String xml) {
		post = new HttpPost(uri);

		try {
			StringEntity entity = new StringEntity(xml, ConstantValue.CHARSET);
			post.setEntity(entity);

			response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				return response.getEntity().getContent();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 发送post请求
	 * 
	 * @param uri
	 * @param params
	 *            以Map形式传递参数
	 * @return
	 */
	public static String sendPost(String uri, Map<String, String> params) {
		post = new HttpPost(uri);

		post.setHeaders(headers);
		if (params != null && params.size() > 0) {

			String paramString = "{";

			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> item : params.entrySet()) {
				paramString += "\"" + item.getKey() + "\":" + "\""
						+ item.getValue() + "\",";
				// BasicNameValuePair pair = new
				// BasicNameValuePair(item.getKey(),
				// item.getValue());
				// parameters.add(pair);
			}
			paramString = paramString.substring(0, paramString.length() - 2)
					+ "\"}";
			String encode = "";
			try {
				encode = Des3.encode(paramString);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BasicNameValuePair pair = new BasicNameValuePair("secret", encode);
			parameters.add(pair);

			try {
				HttpEntity entity = new UrlEncodedFormEntity(parameters,
						ConstantValue.CHARSET);
				post.setEntity(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			response = client.execute(post);
			// 判断是否成功处理
			if (response.getStatusLine().getStatusCode() == 200) {
				// EntityUtils//把服务器端给你的信息变成字符串
				return EntityUtils.toString(response.getEntity(),
						ConstantValue.CHARSET);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Toast.makeText(GlobalParams.context, "网络错误", 1).show();

		return null;
	}

	/**
	 * 发送post请求(有json数组)
	 * 
	 * @param uri
	 * @param params
	 *            以Map形式传递参数
	 * @return
	 */
	public static String sendPostJsonArray(String uri,
			Map<String, String> params) {
		post = new HttpPost(uri);

		post.setHeaders(headers);
		if (params != null && params.size() > 0) {

			String paramString = "{";

			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> item : params.entrySet()) {
				if (item.getKey().equals("comment")) {
					paramString += "\"" + item.getKey() + "\":"
							+ item.getValue() + ",";
				} else {
					paramString += "\"" + item.getKey() + "\":" + "\""
							+ item.getValue() + "\",";
				}

				// BasicNameValuePair pair = new
				// BasicNameValuePair(item.getKey(),
				// item.getValue());
				// parameters.add(pair);
			}
			paramString = paramString.substring(0, paramString.length() - 2)
					+ "\"}";
			String encode = "";
			try {
				encode = Des3.encode(paramString);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BasicNameValuePair pair = new BasicNameValuePair("secret", encode);
			parameters.add(pair);

			try {
				HttpEntity entity = new UrlEncodedFormEntity(parameters,
						ConstantValue.CHARSET);
				post.setEntity(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			response = client.execute(post);
			// 判断是否成功处理
			if (response.getStatusLine().getStatusCode() == 200) {
				// EntityUtils//把服务器端给你的信息变成字符串
				return EntityUtils.toString(response.getEntity(),
						ConstantValue.CHARSET);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Toast.makeText(GlobalParams.context, "网络错误", 1).show();

		return null;
	}

	/**
	 * 发送post请求(未加密)
	 * 
	 * @param uri
	 * @param params
	 *            以Map形式传递参数
	 * @return
	 */
	public static String sendPostNoSecret(String uri, Map<String, String> params) {

		post = new HttpPost(uri);

		post.setHeaders(headers);
		if (params != null && params.size() > 0) {
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> item : params.entrySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(item.getKey(),
						item.getValue());
				parameters.add(pair);
			}
			try {
				HttpEntity entity = new UrlEncodedFormEntity(parameters,
						ConstantValue.CHARSET);
				post.setEntity(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			response = client.execute(post);
			// 判断是否成功处理
			if (response.getStatusLine().getStatusCode() == 200) {
				// EntityUtils//把服务器端给你的信息变成字符串
				return EntityUtils.toString(response.getEntity(),
						ConstantValue.CHARSET);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

		return null;
	}

	/**
	 * 发送Get请求
	 * 
	 * @param uri
	 *            请求的uri地址
	 * @param params
	 *            Map参数集合
	 * @return
	 */
	public String sendGet(String uri, Map<String, String> params) {
		
		HttpClient client = new DefaultHttpClient();

		StringBuilder sb = new StringBuilder();
		sb.append(uri);
		sb.append("?");

		// 设置参数
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String> item : params.entrySet()) {
				sb.append(item.getKey() + "=" + item.getValue() + "&");
			}
		}

		get = new HttpGet(sb.toString());
		get.setHeaders(headers);
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
