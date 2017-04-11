package com.bccv.threedimensionalworld.Net;

import java.io.InputStream;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.bccv.threedimensionalworld.tool.ConstantValue;
import com.bccv.threedimensionalworld.tool.Des3;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.bccv.threedimensionalworld.tool.StringUtils;


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
	private static Header[] headers = new Header[10];

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
		}

		return null;
	}
	
	/**
	 * 发送Get请求
	 * @param uri 请求的uri地址
	 * @param params Map参数集合
	 * @return
	 */
	public String sendGet(String uri, Map<String, String> params){

		HttpClient client = new DefaultHttpClient();
		
		StringBuilder sb = new StringBuilder();
		sb.append(uri);
		
		
		// 设置参数
		if (params != null && params.size() > 0) {
			sb.append("?");
			for (Map.Entry<String, String> item : params.entrySet()) {
				sb.append(item.getKey()+"="+item.getValue()+"&");
			}
		}
		
		get = new HttpGet(sb.toString());

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
