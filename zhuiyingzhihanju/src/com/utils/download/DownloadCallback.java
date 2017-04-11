package com.utils.download;

import java.io.File;

import org.xutils.common.Callback;

import com.lidroid.xutils.http.HttpHandler.State;

public class DownloadCallback
		implements Callback.ProgressCallback<File>, Callback.Cancelable {

	private DownloadInfo downloadInfo;
	private boolean cancelled = false;
	private Cancelable cancelable;
	
	public DownloadCallback(DownloadInfo downloadInfo) {
		// TODO Auto-generated constructor stub
		this.downloadInfo = downloadInfo;
	}
	
	public void setCancelable(Cancelable cancelable) {
		this.cancelable = cancelable;
	}
	
	@Override
    public void cancel() {
        cancelled = true;
        if (cancelable != null) {
            cancelable.cancel();
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

	@Override
	public void onWaiting() {
		// TODO Auto-generated method stub
		downloadInfo.setState(State.WAITING);
	}

	@Override
	public void onStarted() {
		// TODO Auto-generated method stub
		downloadInfo.setState(State.STARTED);
	}

	@Override
	public void onLoading(long total, long current, boolean isDownloading) {
		// TODO Auto-generated method stub
		if (isDownloading) {
			downloadInfo.setFileLength(total);
//			downloadInfo.setProgress(current);
			downloadInfo.setProgress(current);
			downloadInfo.setState(State.LOADING);
		}
		
	}

	@Override
	public void onSuccess(File result) {
		// TODO Auto-generated method stub
		downloadInfo.setState(State.SUCCESS);
	}

	@Override
	public void onError(Throwable ex, boolean isOnCallback) {
		// TODO Auto-generated method stub
		downloadInfo.setState(State.FAILURE);
	}

	@Override
	public void onCancelled(CancelledException cex) {
		// TODO Auto-generated method stub
		downloadInfo.setState(State.CANCELLED);
	}

	@Override
	public void onFinished() {
		// TODO Auto-generated method stub
		cancelled = false;
	}

}
