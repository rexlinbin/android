package com.utils.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import com.lidroid.xutils.exception.DbException;
import com.utils.download.DownloadInfo;
import com.utils.download.DownloadManager;
import com.utils.download.DownloadService;
import com.utils.download.DownloadUtil;
import com.utils.net.HttpClientUtil;

import android.R.integer;
import android.content.Context;

public class MyM3U8Utils {
	private Context context;
	private DownloadManager downloadManager;
	private List<String> downloadList;
	private String localUrl;
	private String downloadPath, downloadRootPath;

	private String model_id;
	private String modelType_id;
	private String modelImage_url;
	private String model_title;
	private String source;
	private boolean isEpisode;
	private int hd;
	private String source_id;

	private boolean isM3U8 = true;

	public MyM3U8Utils(Context context, String model_id, String modelType_id, String modelImage_url, String model_title,
			boolean isEpisode, String source, String source_id, int hd) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.model_id = model_id;
		this.modelType_id = modelType_id;
		this.modelImage_url = modelImage_url;
		this.model_title = model_title;
		this.isEpisode = isEpisode;
		this.source = source;
		this.source_id = source_id;
		this.hd = hd;

		downloadRootPath = GlobalParams.downloadPath + "/" + model_id + "a"+ modelType_id + "/" + source_id + "/";
		downloadManager = DownloadUtil.getDownloadManager();

	}

	public void initDownload(String downloadUrl, int episode_id, boolean isM3U8) {
//		downloadUrl = "https://secure-appldnld.apple.com/itunes12/031-69285-20160802-7E7A134A-552B-11E6-B37E-686CECD541CE/iTunes6464Setup.exe";
		this.isM3U8 = isM3U8;
		if (isEpisode) {
			downloadPath = downloadRootPath + episode_id + "/";
		}else{
			downloadPath = downloadRootPath;
		}
		FileUtils.deleteDirectory(new File(downloadPath));
		if (isM3U8) {
			downloadList = parseStringFromUrl(downloadUrl);
			if (downloadList == null) {
				downloadList = new ArrayList<String>();
			}
			String content = getNativeM3u(downloadUrl, downloadPath);
			File file = new File(downloadPath + model_id + ".m3u8");
			if (!file.exists()) {
				FileUtils.createFile(downloadPath, model_id + ".m3u8");
			}
			write(downloadPath + model_id + ".m3u8", content);
			localUrl = downloadPath + model_id + ".m3u8";
		} else {
			// downloadUrl = getLocationUrl(downloadUrl);
			if (StringUtils.isEmpty(downloadUrl)) {
				return;
			}
			downloadList = new ArrayList<String>();
			downloadList.add(downloadUrl);
			int index = downloadUrl.lastIndexOf("/");
			if (index > 0) {
				String fileName = model_id + ".mp4";
				localUrl = downloadPath + fileName;
			}
		}

	}

	public void download(int episode_id) {
		
		if (isM3U8) {
			startDownloadM3U8(episode_id);
		} else {
			startDownload(episode_id);
		}

	}

	public String getLocalUrl() {
		return localUrl;
	}

	private void startDownload(int episode_id) {
		// DownloadManager downloadManager =
		// DownloadService.getDownloadManager(context);
		try {
			for (int i = 0; i < downloadList.size(); i++) {// downloadList.size()
				String url = downloadList.get(i);
				// url = URLEncoder.encode(url, "utf-8").replaceAll("\\+",
				// "%20");
				// url = url.replaceAll("%3A", ":").replaceAll("%2F", "/");
				int index = url.lastIndexOf("/");
				if (index > 0) {
					String fileName = model_id + ".mp4";
					downloadManager.addNewM3U8Download(url, fileName + "", downloadPath + fileName, true, true, null,
							model_id, modelType_id, model_title, modelImage_url, isEpisode, episode_id, localUrl,
							source, source_id, hd, isM3U8);
				}
			}
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void startDownloadM3U8(int episode_id) {
		// DownloadManager downloadManager =
		// DownloadService.getDownloadManager(context);
		try {
			for (int i = 0; i < downloadList.size(); i++) {// downloadList.size()

				downloadManager.addNewM3U8Download(downloadList.get(i), i + "", downloadPath + i + ".ts", true, true,
						null, model_id, modelType_id, model_title, modelImage_url, isEpisode, episode_id, localUrl,
						source, source_id, hd, isM3U8);
			}
			
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveDb() {
	}

	public static List<String> parseStringFromUrl(String url) {
		List<String> resultList = null;
		HttpClientUtil util = new HttpClientUtil();
		String direUrl = "";
		if (!StringUtils.isEmpty(url)) {
			direUrl = url.substring(0, url.lastIndexOf("/") + 1);
		}
		HttpResponse res = util.getResponseFromUrl(url);
		try {
			if (res != null) {
				resultList = new ArrayList<String>();
				InputStream in = res.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("#")) {
					} else if (line.length() > 0 && line.startsWith("http://")) {
						resultList.add(line);
					}else if (line.length() > 0){
						resultList.add(direUrl + line);
					}
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public static String getNativeM3u(String url, String saveFilePath) {
		HttpClientUtil util = new HttpClientUtil();

		HttpResponse res = util.getResponseFromUrl(url);
		int num = 0;
		// 需要生成的目标buff
		StringBuffer buf = new StringBuffer();
		try {
			if (res != null) {
				InputStream in = res.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					if (line.length() > 0 && line.startsWith("http://")) {
						// replce 这行的内容
						buf.append("file:/" + saveFilePath + num + ".ts\r\n");
						num++;
					} else {
						buf.append(line + "\r\n");
					}
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	private String getLocationUrl(String url) {
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestProperty("User-agent",
					"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.215 Safari/535.1");
			conn.setRequestProperty("accept-language", "zh-CN");
			conn.setConnectTimeout(5 * 1000);// 5秒的链接超时
			conn.setReadTimeout(5 * 1000);// 设置从主机读取数据超时（单位：毫秒）
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("GET");
			int code = conn.getResponseCode();
			Logger.e("code", code + "");
			conn.connect();
			String redictURL = conn.getURL().toString();
			Logger.e("redictURL", redictURL);
			conn.disconnect();
			return redictURL;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return url;
	}

	/**
	 * 将内容回写到文件中
	 * 
	 * @param filePath
	 * @param content
	 */
	public static void write(String filePath, String content) {

		BufferedWriter bw = null;

		try {
			// 根据文件路径创建缓冲输出流
			bw = new BufferedWriter(new FileWriter(filePath));
			// 将内容写入文件中
			bw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					bw = null;
				}
			}
		}
	}
}
