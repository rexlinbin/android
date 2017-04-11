package com.bccv.zhuiying.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.fragment.FancyFragment;
import com.bccv.zhuiying.fragment.FoundFragment;
import com.bccv.zhuiying.fragment.SpecialFragment;
import com.bccv.zhuiying.fragment.YMyFrament;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.http.HttpHandler.State;
import com.tencent.stat.StatService;
import com.tendcloud.tenddata.TCAgent;
import com.utils.download.DownloadInfo;
import com.utils.download.DownloadManager;
import com.utils.download.DownloadService;
import com.utils.model.DownloadMovie;
import com.utils.tools.AppManager;
import com.utils.tools.FileUtils;
import com.utils.tools.SerializationUtil;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnClickListener {
	private RelativeLayout fancyRe, foundRe, specialRe, myRe;
	private boolean isFancy, isFound, isSpecial, isMy;
	private ImageView fancyIm, foundIm, specialIm, myIm;
	private TabHost tabhost;
	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TCAgent.onPageStart(getApplicationContext(), "MainActivity");
		StatService.trackCustomEvent(this, "onCreate", "");

		PushManager.getInstance().initialize(this.getApplicationContext());

		fancyRe = (RelativeLayout) findViewById(R.id.main_bottom_fancy);
		foundRe = (RelativeLayout) findViewById(R.id.main_bottom_found);

		specialRe = (RelativeLayout) findViewById(R.id.main_bottom_special);
		myRe = (RelativeLayout) findViewById(R.id.main_bottom_my);

		fancyIm = (ImageView) findViewById(R.id.main_bottom_fancyIm);
		foundIm = (ImageView) findViewById(R.id.main_bottom_foundIm);
		specialIm = (ImageView) findViewById(R.id.main_bottom_specialIm);
		myIm = (ImageView) findViewById(R.id.main_bottom_myIm);

		fancyRe.setOnClickListener(this);

		foundRe.setOnClickListener(this);
		specialRe.setOnClickListener(this);
		myRe.setOnClickListener(this);

		setDefaultFragment();

	}

	private void setDefaultFragment() {

		tabhost = getTabHost();

		tabhost.addTab(
				tabhost.newTabSpec("fancy").setIndicator("fancy").setContent(new Intent(this, FancyFragment.class)));
		tabhost.addTab(
				tabhost.newTabSpec("found").setIndicator("found").setContent(new Intent(this, FoundFragment.class)));
		tabhost.addTab(tabhost.newTabSpec("special").setIndicator("special")
				.setContent(new Intent(this, SpecialFragment.class)));
		tabhost.addTab(tabhost.newTabSpec("my").setIndicator("my").setContent(new Intent(this, YMyFrament.class)));
		fancyIm.setImageResource(R.drawable.fancy_select);
		foundIm.setImageResource(R.drawable.found);
		specialIm.setImageResource(R.drawable.special);
		myIm.setImageResource(R.drawable.my);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.main_bottom_fancy:
			isFancy = true;
			isFound = false;
			isSpecial = false;
			isMy = false;
			tabhost.setCurrentTabByTag("fancy");
			break;

		case R.id.main_bottom_found:
			isFancy = false;
			isFound = true;
			isSpecial = false;
			isMy = false;
			tabhost.setCurrentTabByTag("found");
			break;
		case R.id.main_bottom_special:
			isFancy = false;
			isFound = false;
			isSpecial = true;
			isMy = false;
			tabhost.setCurrentTabByTag("special");
			break;

		case R.id.main_bottom_my:
			isFancy = false;
			isFound = false;
			isSpecial = false;
			isMy = true;
			tabhost.setCurrentTabByTag("my");
			break;

		}

		if (isFancy) {
			fancyIm.setImageResource(R.drawable.fancy_select);
		} else {
			fancyIm.setImageResource(R.drawable.fancy);
		}

		if (isFound) {
			foundIm.setImageResource(R.drawable.found_select);
		} else {
			foundIm.setImageResource(R.drawable.found);
		}

		if (isSpecial) {
			specialIm.setImageResource(R.drawable.special_select);
		} else {
			specialIm.setImageResource(R.drawable.special);
		}

		if (isMy) {
			myIm.setImageResource(R.drawable.my_select);
		} else {
			myIm.setImageResource(R.drawable.my);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序！", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				AppManager.getAppManager().finishAllActivity();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序！", Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					DownloadService.unBindService(getApplicationContext());
					StatService.trackCustomEndEvent(this, " playTime", "app");
					finish();
					AppManager.getAppManager().finishAllActivity();
					saveDownload();
					System.exit(0);
				}
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "MainActivity");
	}

	private void saveDownload() {
		Map<String, List<DownloadInfo>> map = new HashMap<String, List<DownloadInfo>>();
		List<String> keyList = new ArrayList<String>();
		DownloadManager downloadManager = DownloadService.getDownloadManager(getApplicationContext());
		Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
		for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
			DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
			String key = downloadInfo.getModel_id() + "," + downloadInfo.getModelType_id() + ","
					+ downloadInfo.getEpisode_id();
			if (map.containsKey(key)) {
				map.get(key).add(downloadInfo);
			} else {
				List<DownloadInfo> list = new ArrayList<DownloadInfo>();
				list.add(downloadInfo);
				map.put(key, list);
				keyList.add(key);
			}
		}
		for (Map.Entry<String, List<DownloadInfo>> entry : map.entrySet()) {
			List<DownloadInfo> list = entry.getValue();
			boolean isSuccess = true;
			for (int j = 0; j < list.size(); j++) {

				DownloadInfo downloadInfo = list.get(j);
				if (downloadInfo.getState() != State.SUCCESS) {
					isSuccess = false;
				}
			}
			DownloadInfo downloadInfo = list.get(0);
			downloadInfo.setSuccess(isSuccess);
		}

		boolean hasDownload = false;
		long lastSize = 0;
		for (int i = 0; i < keyList.size(); i++) {
			List<DownloadInfo> list = map.get(keyList.get(i));
			boolean isSuccess = true;
			long totalSize = 0;
			long downloadTotalSize = 0;
			long downloadSize = 0;
			int fileTotalSize = 0;
			int fileSize = 0;
			boolean isWaiting = true;
			boolean isFailure = true;
			boolean isPause = true;

			for (int j = 0; j < list.size(); j++) {

				DownloadInfo downloadInfo = list.get(j);
				if (downloadInfo.getState() == State.SUCCESS) {
					totalSize += downloadInfo.getFileLength();
					downloadTotalSize += downloadInfo.getFileLength();
					downloadSize += downloadInfo.getFileLength();
					lastSize += downloadInfo.getFileLength();
					fileTotalSize += 1;
					fileSize += 1;
				} else {
					if (downloadInfo.getState() == State.LOADING) {
						isPause = false;
					}
					isSuccess = false;
					hasDownload = true;
					downloadTotalSize += downloadInfo.getFileLength();
					downloadSize += downloadInfo.getProgress();
					lastSize += downloadInfo.getLastProgress();
					downloadInfo.setLastProgress(downloadInfo.getProgress());
					fileTotalSize += 1;
				}
				if (downloadInfo.getState() != State.WAITING) {
					isWaiting = false;
				}
				if (downloadInfo.getState() != State.FAILURE && downloadInfo.getState() != State.SUCCESS) {
					isFailure = false;
				}
			}
			DownloadInfo downloadInfo = list.get(0);
			if (isSuccess) {
				DownloadMovie downloadMovie = new DownloadMovie();
				downloadMovie.setDownloading(false);
				downloadMovie.setEpisode(downloadInfo.isEpisode());
				downloadMovie.setEpisode_id(downloadInfo.getEpisode_id());
				downloadMovie.setImageUrl(downloadInfo.getModelImage_url());
				downloadMovie.setModel_id(downloadInfo.getModel_id());
				downloadMovie.setModelType_id(downloadInfo.getModelType_id());
				downloadMovie.setTitle(downloadInfo.getModel_title());
				downloadMovie.setSpeed("完成");
				downloadMovie.setSize(FileUtils.formatFileSize(totalSize));
				downloadMovie.setLocalUrl(downloadInfo.getLocalUrl());
				downloadMovie.setSuccess(true);
				movieMap.put(keyList.get(i), downloadMovie);
			} else {
				DownloadMovie downloadMovie = new DownloadMovie();
				downloadMovie.setEpisode(downloadInfo.isEpisode());
				downloadMovie.setEpisode_id(downloadInfo.getEpisode_id());
				downloadMovie.setImageUrl(downloadInfo.getModelImage_url());
				downloadMovie.setModel_id(downloadInfo.getModel_id());
				downloadMovie.setModelType_id(downloadInfo.getModelType_id());
				downloadMovie.setTitle(downloadInfo.getModel_title());
				downloadMovie.setHd(downloadInfo.getHd());
				downloadMovie.setSource_id(downloadInfo.getSource_id());
				downloadMovie.setSource_name(downloadInfo.getSource_name());
				downloadMovie.setSuccess(false);
				downloadMovie.setM3U8(downloadInfo.isM3U8());
				if (isFailure) {
					downloadMovie.setDownloading(false);
					downloadMovie.setSpeed("失败");
				} else if (isWaiting) {
					downloadMovie.setDownloading(false);
					downloadMovie.setSpeed("等待");
				} else if (isPause) {
					downloadMovie.setDownloading(false);
					downloadMovie.setSpeed("暂停");
				} else {
					downloadMovie.setDownloading(true);
					if (downloadTotalSize != 0) {
						long speed = downloadSize - lastSize;
						downloadMovie.setSpeed(FileUtils.formatFileSize(speed) + "/S");
						downloadMovie.setSpeedNum(speed);
						downloadMovie.setProgress(fileSize * 100 / fileTotalSize);
						downloadMovie.setLastSize(downloadSize * 100 / downloadTotalSize);
					} else {
						downloadMovie.setSpeed("0KB/S");
						downloadMovie.setProgress(0);
						downloadMovie.setLastSize(0);
						downloadMovie.setSpeedNum(0);
					}
				}

				downloadMovie.setSize(FileUtils.formatFileSize(downloadTotalSize));
				if (downloadInfo.isM3U8()) {
					downloadMovie.setFileNum(fileTotalSize);
				} else {
					downloadMovie.setFileNum(1);
				}
				downloadMovie.setFileSize(downloadTotalSize);
				downloadMovie.setLocalUrl(downloadInfo.getLocalUrl());

				movieMap.put(keyList.get(i), downloadMovie);
			}
		}

		if (movieMap.size() > 0) {
			SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
		}

	}
}
