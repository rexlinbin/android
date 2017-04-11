package com.utils.download;

import android.content.Context;
import android.database.Cursor;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.callback.HttpRedirectHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.utils.model.DownloadMovie;
import com.utils.tools.Logger;
import com.utils.tools.SerializationUtil;
import com.utils.tools.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.EntityEnclosingRequestWrapper;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;

/**
 * Author: wyouflf Date: 13-11-10 Time: 下午8:10
 */
public class DownloadManager {

	private List<DownloadInfo> downloadInfoList;
	private Map<String, DownloadMovie> movieMap;

	private int maxDownloadThread = 5;

	private Context mContext;
	// private DbUtils db;
	private int id = 0;

	/* package */ DownloadManager(Context appContext) {
		ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class, new HttpHandlerStateConverter());
		mContext = appContext;
		// db = DbUtils.create(mContext);
		// try {
		// downloadInfoList = db.findAll(Selector.from(DownloadInfo.class));
		// } catch (DbException e) {
		// LogUtils.e(e.getMessage(), e);
		// }

		movieMap = SerializationUtil.readDownloadCache(mContext);
		if (movieMap == null) {
			movieMap = new HashMap<String, DownloadMovie>();
		}

		if (downloadInfoList == null) {
			downloadInfoList = new ArrayList<DownloadInfo>();
		}

		// myHttp.configRequestThreadPoolSize(maxDownloadThread);
	}

	public Map<String, DownloadMovie> getMovieMap() {
		return movieMap;
	}

	public void setMovieMap(Map<String, DownloadMovie> movieMap) {
		this.movieMap = movieMap;
	}

	public int getDownloadInfoListCount() {
		return downloadInfoList.size();
	}

	public DownloadInfo getDownloadInfo(int index) {
		return downloadInfoList.get(index);
	}

	/**
	 * 下载
	 * 
	 * @param url
	 *            下载地址
	 * @param fileName
	 *            文件名
	 * @param target
	 *            保存路径
	 * @param autoResume
	 *            自动继续
	 * @param autoRename
	 *            自动重命名
	 * @param callback
	 *            返回事件
	 * @return
	 * @throws DbException
	 */
	public DownloadInfo addNewDownload(String url, String fileName, String target, boolean autoResume,
			boolean autoRename, final RequestCallBack<File> callback) throws DbException {
		final DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.setDownloadUrl(url);
		downloadInfo.setAutoRename(autoRename);
		downloadInfo.setAutoResume(autoResume);
		downloadInfo.setFileName(fileName);
		downloadInfo.setFileSavePath(target);

		HttpUtils http = new HttpUtils();
		http.configRequestThreadPoolSize(maxDownloadThread);
		HttpHandler<File> handler = http.download(url, target, autoResume, autoRename,
				new ManagerCallBack(downloadInfo, callback));
		downloadInfo.setHandler(handler);
		downloadInfo.setState(handler.getState());

		downloadInfoList.add(downloadInfo);
		// db.saveBindingId(downloadInfo);
		return downloadInfo;
	}

	private final Executor executor = new PriorityExecutor(1, true);
	private String useragent = "";
	public void setUserAgent(String useragent){
		this.useragent = useragent;
	}
	
	/**
	 * 下载 xutils3
	 * 
	 * @param url
	 *            下载地址
	 * @param fileName
	 *            文件名
	 * @param target
	 *            保存路径
	 * @param autoResume
	 *            自动继续
	 * @param autoRename
	 *            自动重命名
	 * @param callback
	 *            返回事件
	 * @return
	 * @throws DbException
	 */
	public DownloadInfo addNewestDownload(String url, String fileName, String target, boolean autoResume,
			boolean autoRename, final CommonCallback<File> callback, String model_id, String modelType_id,
			String model_title, String modelImage_url, boolean isEpisode, int episode_id, String localUrl,
			String source, String source_id, int hd, boolean isM3U8) throws DbException {
		final DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.setDownloadUrl(url);
		downloadInfo.setAutoRename(autoRename);
		downloadInfo.setAutoResume(autoResume);
		downloadInfo.setFileName(fileName);
		downloadInfo.setFileSavePath(target);
		downloadInfo.setModel_id(model_id);
		downloadInfo.setModelType_id(modelType_id);
		downloadInfo.setModel_title(model_title);
		downloadInfo.setModelImage_url(modelImage_url);
		downloadInfo.setEpisode(isEpisode);
		downloadInfo.setEpisode_id(episode_id);
		downloadInfo.setLocalUrl(localUrl);
		downloadInfo.setSource_name(source);
		downloadInfo.setSource_id(source_id);
		downloadInfo.setHd(hd);
		downloadInfo.setM3U8(isM3U8);

		DownloadCallback downloadCallback = new DownloadCallback(downloadInfo);
		
		RequestParams params = new RequestParams(url);
		params.setAutoResume(downloadInfo.isAutoResume());
		params.setAutoRename(downloadInfo.isAutoRename());
		params.setSaveFilePath(downloadInfo.getFileSavePath());
		params.setExecutor(executor);
		params.setCancelFast(true);
		if (!StringUtils.isEmpty(useragent)) {
			params.setHeader("User-agent", useragent);
		}
		
		Callback.Cancelable cancelable = x.http().get(params, downloadCallback);
		downloadCallback.setCancelable(cancelable);
		
		downloadInfo.setCancelable(cancelable);

		downloadInfoList.add(downloadInfo);
		// db.saveBindingId(downloadInfo);
		return downloadInfo;
	}

	HttpUtils myHttp = new HttpUtils();

	/**
	 * 下载m3u8
	 * 
	 * @param url
	 *            下载地址
	 * @param fileName
	 *            文件名
	 * @param target
	 *            保存路径
	 * @param autoResume
	 *            自动继续
	 * @param autoRename
	 *            自动重命名
	 * @param callback
	 *            返回事件
	 * @param model_id
	 * @param modelType_id
	 * @param model_title
	 * @param modelImage_url
	 * @param isEpisode
	 * @param episode_id
	 * @param localUrl
	 * @return
	 * @throws DbException
	 */
	public DownloadInfo addNewM3U8Download(String url, String fileName, String target, boolean autoResume,
			boolean autoRename, final RequestCallBack<File> callback, String model_id, String modelType_id,
			String model_title, String modelImage_url, boolean isEpisode, int episode_id, String localUrl,
			String source, String source_id, int hd, boolean isM3U8) throws DbException {
		final DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.setDownloadUrl(url);
		downloadInfo.setAutoRename(autoRename);
		downloadInfo.setAutoResume(autoResume);
		downloadInfo.setFileName(fileName);
		downloadInfo.setFileSavePath(target);
		downloadInfo.setModel_id(model_id);
		downloadInfo.setModelType_id(modelType_id);
		downloadInfo.setModel_title(model_title);
		downloadInfo.setModelImage_url(modelImage_url);
		downloadInfo.setEpisode(isEpisode);
		downloadInfo.setEpisode_id(episode_id);
		downloadInfo.setLocalUrl(localUrl);
		downloadInfo.setSource_name(source);
		downloadInfo.setSource_id(source_id);
		downloadInfo.setHd(hd);
		downloadInfo.setM3U8(isM3U8);

		myHttp.configRequestThreadPoolSize(maxDownloadThread);

		HttpHandler<File> handler = myHttp.download(url, target, autoResume, autoRename,
				new ManagerCallBack(downloadInfo, callback));
		downloadInfo.setHandler(handler);
		downloadInfo.setState(handler.getState());

		downloadInfoList.add(downloadInfo);
		// db.saveBindingId(downloadInfo);
		return downloadInfo;
	}

	/**
	 * 下载mp4
	 * 
	 * @param url
	 *            下载地址
	 * @param fileName
	 *            文件名
	 * @param target
	 *            保存路径
	 * @param autoResume
	 *            自动继续
	 * @param autoRename
	 *            自动重命名
	 * @param callback
	 *            返回事件
	 * @param model_id
	 * @param modelType_id
	 * @param model_title
	 * @param modelImage_url
	 * @param isEpisode
	 * @param episode_id
	 * @param localUrl
	 * @return
	 * @throws DbException
	 */
	public DownloadInfo addNewMp4Download(String url, String fileName, String target, boolean autoResume,
			boolean autoRename, final RequestCallBack<File> callback, String model_id, String modelType_id,
			String model_title, String modelImage_url, boolean isEpisode, int episode_id, String localUrl,
			String source, String source_id, int hd, boolean isM3U8) throws DbException {
		final DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.setDownloadUrl(url);
		downloadInfo.setAutoRename(autoRename);
		downloadInfo.setAutoResume(autoResume);
		downloadInfo.setFileName(fileName);
		downloadInfo.setFileSavePath(target);
		downloadInfo.setModel_id(model_id);
		downloadInfo.setModelType_id(modelType_id);
		downloadInfo.setModel_title(model_title);
		downloadInfo.setModelImage_url(modelImage_url);
		downloadInfo.setEpisode(isEpisode);
		downloadInfo.setEpisode_id(episode_id);
		downloadInfo.setLocalUrl(localUrl);
		downloadInfo.setSource_name(source);
		downloadInfo.setSource_id(source_id);
		downloadInfo.setHd(hd);
		downloadInfo.setM3U8(isM3U8);

		HttpUtils myHttp = new HttpUtils();
		myHttp.configRequestThreadPoolSize(maxDownloadThread);

		HttpHandler<File> handler = myHttp.download(url, target, autoResume, autoRename,
				new ManagerCallBack(downloadInfo, callback));
		downloadInfo.setHandler(handler);
		downloadInfo.setState(handler.getState());

		downloadInfoList.add(downloadInfo);
		// db.saveBindingId(downloadInfo);
		return downloadInfo;
	}

	public void resumeDownload(int index, final RequestCallBack<File> callback) throws DbException {
		final DownloadInfo downloadInfo = downloadInfoList.get(index);
		resumeDownload(downloadInfo, callback);
	}

	public void resumeDownload(DownloadInfo downloadInfo, final RequestCallBack<File> callback) throws DbException {
		HttpUtils http = new HttpUtils();
		http.configRequestThreadPoolSize(maxDownloadThread);
		HttpHandler<File> handler = http.download(downloadInfo.getDownloadUrl(), downloadInfo.getFileSavePath(),
				downloadInfo.isAutoResume(), downloadInfo.isAutoRename(), new ManagerCallBack(downloadInfo, callback));
		downloadInfo.setHandler(handler);
		downloadInfo.setState(handler.getState());
		// db.saveOrUpdate(downloadInfo);
	}

	public void removeDownload(int index) throws DbException {
		DownloadInfo downloadInfo = downloadInfoList.get(index);
		removeDownload(downloadInfo);
	}

	public void removeDownload(DownloadInfo downloadInfo) throws DbException {
		Cancelable cancelable = downloadInfo.getCancelable();
		if (cancelable != null && !cancelable.isCancelled()) {
			cancelable.cancel();
		}
		downloadInfoList.remove(downloadInfo);
		// db.delete(downloadInfo);
		File file = new File(downloadInfo.getFileSavePath());
		if (file.exists()) {
			file.delete();
		}
	}

	// private boolean isDeleteing = false;

	public void removeDownloadAll(List<DownloadInfo> downloadInfos) throws DbException {
		// isDeleteing = true;
		for (int i = 0; i < downloadInfoList.size(); i++) {

			DownloadInfo downloadInfo = downloadInfoList.get(i);
			if (downloadInfos.contains(downloadInfo)) {
				Cancelable cancelable = downloadInfo.getCancelable();
				if (cancelable != null && !cancelable.isCancelled()) {
					cancelable.cancel();
				}
				File file = new File(downloadInfo.getFileSavePath());
				if (file.exists()) {
					file.delete();
				}
			}
		}

		downloadInfoList.removeAll(downloadInfos);
		// db.deleteAll(downloadInfos);
		// isDeleteing = false;

	}

	public void stopDownload(int index) throws DbException {
		DownloadInfo downloadInfo = downloadInfoList.get(index);
		stopDownload(downloadInfo);
	}

	public void stopDownload(DownloadInfo downloadInfo) throws DbException {
		Cancelable cancelable = downloadInfo.getCancelable();
		if (cancelable != null && !cancelable.isCancelled()) {
			cancelable.cancel();
		} else {
			downloadInfo.setState(HttpHandler.State.CANCELLED);
		}
		// db.saveOrUpdate(downloadInfo);
	}

	public void stopAllDownload() throws DbException {
		boolean hasStopped = false;
		for (DownloadInfo downloadInfo : downloadInfoList) {
			if (downloadInfo.getState() != State.SUCCESS && downloadInfo.getState() != State.CANCELLED
					&& downloadInfo.getState() != State.FAILURE) {
				hasStopped = true;
				Cancelable cancelable = downloadInfo.getCancelable();
				if (cancelable != null && !cancelable.isCancelled()) {
					cancelable.cancel();
				} else {
					downloadInfo.setState(HttpHandler.State.CANCELLED);
				}
			}

		}
		if (hasStopped) {
			// db.saveOrUpdateAll(downloadInfoList);
		}

	}

	public void stopAllDownload(List<DownloadInfo> downloadInfos) throws DbException {
		boolean hasStopped = false;
		for (DownloadInfo downloadInfo : downloadInfos) {
			if (downloadInfo.getState() != State.SUCCESS) {
				Cancelable cancelable = downloadInfo.getCancelable();
				if (cancelable != null && !cancelable.isCancelled()) {
					cancelable.cancel();
				} else {
					downloadInfo.setState(HttpHandler.State.CANCELLED);
				}
			}

		}
		if (hasStopped) {
			// db.saveOrUpdateAll(downloadInfoList);
		}

	}

	public void backupDownloadInfoList() throws DbException {
		for (DownloadInfo downloadInfo : downloadInfoList) {
			if (downloadInfo.getState() != State.SUCCESS) {
				Cancelable cancelable = downloadInfo.getCancelable();
				if (cancelable != null && !cancelable.isCancelled()) {
					cancelable.cancel();
				}
			}
		}
		// db.saveOrUpdateAll(downloadInfoList);
	}

	public int getMaxDownloadThread() {
		return maxDownloadThread;
	}

	public void setMaxDownloadThread(int maxDownloadThread) {
		this.maxDownloadThread = maxDownloadThread;
	}

	public class ManagerCallBack extends RequestCallBack<File> {
		private DownloadInfo downloadInfo;
		private RequestCallBack<File> baseCallBack;

		public RequestCallBack<File> getBaseCallBack() {
			return baseCallBack;
		}

		public void setBaseCallBack(RequestCallBack<File> baseCallBack) {
			this.baseCallBack = baseCallBack;
		}

		private ManagerCallBack(DownloadInfo downloadInfo, RequestCallBack<File> baseCallBack) {
			this.baseCallBack = baseCallBack;
			this.downloadInfo = downloadInfo;
		}

		@Override
		public Object getUserTag() {
			if (baseCallBack == null)
				return null;
			return baseCallBack.getUserTag();
		}

		@Override
		public void setUserTag(Object userTag) {
			if (baseCallBack == null)
				return;
			baseCallBack.setUserTag(userTag);
		}

		@Override
		public void onStart() {
			HttpHandler<File> handler = downloadInfo.getHandler();
			if (handler != null) {
				downloadInfo.setState(handler.getState());
			}
			// try {
			// db.saveOrUpdate(downloadInfo);
			// } catch (DbException e) {
			// LogUtils.e(e.getMessage(), e);
			// }
			if (baseCallBack != null) {
				baseCallBack.onStart();
			}
			Logger.e("start", "onStart");
		}

		@Override
		public void onCancelled() {
			HttpHandler<File> handler = downloadInfo.getHandler();
			if (handler != null) {
				downloadInfo.setState(handler.getState());
			}
			// try {
			// db.saveOrUpdate(downloadInfo);
			// } catch (Exception e) {
			// LogUtils.e(e.getMessage(), e);
			// }
			if (baseCallBack != null) {
				baseCallBack.onCancelled();
			}
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			HttpHandler<File> handler = downloadInfo.getHandler();
			if (handler != null) {
				downloadInfo.setState(handler.getState());
			}
			downloadInfo.setFileLength(total);
			downloadInfo.setProgress(current);
			// try {
			// if (!isDeleteing) {
			// db.saveOrUpdate(downloadInfo);
			// }
			//
			// } catch (DbException e) {
			// LogUtils.e(e.getMessage(), e);
			// }
			if (baseCallBack != null) {
				baseCallBack.onLoading(total, current, isUploading);
			}
		}

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			HttpHandler<File> handler = downloadInfo.getHandler();
			if (handler != null) {
				downloadInfo.setState(handler.getState());
			}
			// try {
			// db.saveOrUpdate(downloadInfo);
			// } catch (DbException e) {
			// LogUtils.e(e.getMessage(), e);
			// }
			if (baseCallBack != null) {
				baseCallBack.onSuccess(responseInfo);
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			HttpHandler<File> handler = downloadInfo.getHandler();
			if (handler != null) {
				downloadInfo.setState(handler.getState());
			}
			// try {
			// db.saveOrUpdate(downloadInfo);
			// } catch (DbException e) {
			// LogUtils.e(e.getMessage(), e);
			// }
			if (baseCallBack != null) {
				baseCallBack.onFailure(error, msg);
			}
			Logger.e("error", msg);
		}
	}

	private class HttpHandlerStateConverter implements ColumnConverter<HttpHandler.State> {

		@Override
		public HttpHandler.State getFieldValue(Cursor cursor, int index) {
			return HttpHandler.State.valueOf(cursor.getInt(index));
		}

		@Override
		public HttpHandler.State getFieldValue(String fieldStringValue) {
			if (fieldStringValue == null)
				return null;
			return HttpHandler.State.valueOf(fieldStringValue);
		}

		@Override
		public Object fieldValue2ColumnValue(HttpHandler.State fieldValue) {
			return fieldValue.value();
		}

		@Override
		public ColumnDbType getColumnDbType() {
			return ColumnDbType.INTEGER;
		}
	}
}
