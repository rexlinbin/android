package com.bccv.meitu.download;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
	// 文件下载线程池
	private ExecutorService mFileService;
	private ThreadPoolManager() {
		// Returns the number of processors available to the virtual machine.
		// int num = Runtime.getRuntime().availableProcessors();
		mFileService = Executors.newFixedThreadPool(2);
	}

	private static final ThreadPoolManager manager = new ThreadPoolManager();

	public static ThreadPoolManager getInstance() {
		return manager;
	}

	public void addFileTask(Runnable runnable) {
		mFileService.execute(runnable);
	}

}
