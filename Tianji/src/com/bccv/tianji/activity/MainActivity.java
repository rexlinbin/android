package com.bccv.tianji.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bccv.tianji.R;
import com.bccv.tianji.activity.dialog.LogInDialog;
import com.bccv.tianji.activity.dialog.LogInDialog.Dialogcallback;
import com.bccv.tianji.adapter.MainAdapter;
import com.bccv.tianji.api.GameApi;
import com.bccv.tianji.api.RegisterApi;
import com.bccv.tianji.api.TcpApi;
import com.bccv.tianji.model.Game;
import com.bccv.tianji.model.User;
import com.mining.app.zxing.activity.MipcaActivityCapture;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.net.NetUtil;
import com.utils.pulltorefresh.FooterLoadingLayout;
import com.utils.pulltorefresh.PullToRefreshBase;
import com.utils.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.utils.pulltorefresh.PullToRefreshListView;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.PromptManager;
import com.utils.tools.ScreenUtil;
import com.utils.tools.StringUtils;
import com.utils.views.DragLayout;
import com.utils.views.DragLayout.DragListener;

public class MainActivity extends BaseActivity implements OnClickListener {
	private LogInDialog loginDialog;
	private MainActivity mContext;
	private List<Game> data, getList;
	private PullToRefreshListView listView;
	private Animation animation;
	private int offSet;
	private int currentItem = 0;
	private int page = 1, count = 10;
	private LinearLayout ll_dots;
	private ImageLoader imageLoader = ImageLoader.getInstance();;
	private ImageLoader iconLoader = ImageLoader.getInstance();;
	private MainAdapter adapter;
	MyAdapter myAdapter;
	private ViewPager viewPager;
	private View view;
	private List<View> dots; // 图片标题正文的那些点

	private List<String> headpicUrls;// 图片地址
	private static final int CHANGE_VIEWPAGER = 1;
	private List<Game> topData;
	private ListView lv;
	private DragLayout dl;
	private ImageView Icon;
	Button btn_open;
	TextView Name;

	private Button ReBtn, logBtn, outBtn;
	private LinearLayout reLog;
	private LinearLayout indexLin, commentLin, DownLin, controlLin, signOUtLin,
			gameFenlei;
	View view7;
	Button titleEdit;
	Button erWeima;

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_VIEWPAGER:
				if (topData.size() > 0) {
					viewPager.setCurrentItem(currentItem + 1, true);// 切换当前显示的图片
				} else {

				}

				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (AppConfig.getPrefUserInfo() != null) {
			GlobalParams.user = AppConfig.getPrefUserInfo();
			GlobalParams.hasLogin = true;

		} else {

			User userInfo = new User();

			GlobalParams.user = userInfo;
			GlobalParams.hasLogin = false;

		}

		view7 = findViewById(R.id.view7);

		mContext = this;
		setUpViews();
		setData();
		initView();
		initDragLayout();
		getTopData();

		listView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						Intent aItent = new Intent(MainActivity.this,
								DetailsActivity.class);

						aItent.putExtra("gameID", data.get(arg2 - 1)
								.getGame_id());

						startActivity(aItent);
					}
				});

	}

	private void initView() {
		titleEdit = (Button) findViewById(R.id.title_edit);
		indexLin = (LinearLayout) findViewById(R.id.main_open);

		commentLin = (LinearLayout) findViewById(R.id.main_comment);
		DownLin = (LinearLayout) findViewById(R.id.main_download);
		controlLin = (LinearLayout) findViewById(R.id.main_control);

		signOUtLin = (LinearLayout) findViewById(R.id.main_signOUt);
		gameFenlei = (LinearLayout) findViewById(R.id.main_gameFenlei);
		indexLin.setOnClickListener(this);
		commentLin.setOnClickListener(this);
		controlLin.setOnClickListener(this);
		signOUtLin.setOnClickListener(this);
		DownLin.setOnClickListener(this);
		gameFenlei.setOnClickListener(this);
		reLog = (LinearLayout) findViewById(R.id.main_NoRegit);
		ReBtn = (Button) findViewById(R.id.main_regBtn);
		logBtn = (Button) findViewById(R.id.main_loginBtn);
		erWeima = (Button) findViewById(R.id.btn_Right_menu);
		erWeima.setOnClickListener(this);
		Icon = (ImageView) findViewById(R.id.iv_bottom);
		Icon.setOnClickListener(this);
		Name = (TextView) findViewById(R.id.main_iconName);
		if (GlobalParams.hasLogin) {

			Name.setVisibility(View.VISIBLE);
			reLog.setVisibility(View.GONE);
			signOUtLin.setVisibility(View.VISIBLE);
			view7.setVisibility(View.VISIBLE);
			Name.setText(GlobalParams.user.getNick_name());
			iconLoader.displayImage(GlobalParams.user.getUser_icon(), Icon,
					GlobalParams.iconOptions);
			logBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					loginDialog = new LogInDialog(MainActivity.this);
					loginDialog.setDialogCallback(dialogcallback);
					loginDialog.show();
				}
			});

			ReBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent aIntent = new Intent(MainActivity.this,
							RegisterPhoneActivity.class);
					aIntent.putExtra("isReset", false);

					startActivityForResult(aIntent, 0);
				}
			});

		} else {
			Name.setVisibility(View.GONE);
			signOUtLin.setVisibility(View.GONE);
			view7.setVisibility(View.GONE);
			reLog.setVisibility(View.VISIBLE);
			logBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					loginDialog = new LogInDialog(MainActivity.this);
					loginDialog.setDialogCallback(dialogcallback);
					loginDialog.show();
				}
			});

			ReBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent aIntent = new Intent(MainActivity.this,
							RegisterPhoneActivity.class);
					aIntent.putExtra("isReset", false);

					startActivityForResult(aIntent, 0);
				}
			});
		}
		titleEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(SearchActivity.class);
			}
		});
	}

	private void initDragLayout() {
		// TODO Auto-generated method stub

		dl = (DragLayout) findViewById(R.id.dl);
		dl.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
				// lv.smoothScrollToPosition(new Random().nextInt(30));
			}

			@Override
			public void onClose() {
				// shake();
			}

			@Override
			public void onDrag(float percent) {
				ViewHelper.setAlpha(btn_open, 1 - percent);
			}
		});

	}

	private void shake() {
		btn_open.startAnimation(AnimationUtils
				.loadAnimation(this, R.anim.shake));
	}

	private void getTopData() {
		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub

				if (topData != null && topData.size() > 0) {

					setTopView(topData);

				}

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub

				GameApi GameApi = new GameApi();
				if (GlobalParams.hasLogin) {

					topData = GameApi.getGameListList(
							GlobalParams.user.getUser_id(), page + "", count
									+ "", "game_top");
				} else {

					topData = GameApi.getGameListList("", page + "",
							count + "", "game_top");
				}

				return null;
			}
		}.execute("");

	}

	private void setTopView(List<Game> topData2) {
		// TODO Auto-generated method stub

		dots = new ArrayList<View>();

		headpicUrls = new ArrayList<String>();

		if (view == null) {
			view = this.getLayoutInflater().inflate(R.layout.imageview, null);
			ll_dots = (LinearLayout) view.findViewById(R.id.ll_dots);
			viewPager = (ViewPager) view.findViewById(R.id.main_view);

			lv.addHeaderView(view);

			viewPager.setOnPageChangeListener(new MyPageChangeListener());

			myAdapter = new MyAdapter();
			int temp = topData.size() > 0 ? (myAdapter.getCount()) / 2
					% (topData.size()) : 0;
			currentItem = (myAdapter.getCount()) / 2 - temp;
			initHeadData(topData);
			viewPager.setAdapter(myAdapter);
			viewPager.setCurrentItem(currentItem, false);
			sendChangeMsg(3000);
		} else {
			initHeadData(topData);
		}
		listView.getRefreshableView().setAdapter(adapter);

	}

	/**
	 * 初始化头部viewPager
	 * 
	 * @param freshList
	 */
	private void initHeadData(List<Game> GameList) {

		ll_dots.removeAllViews();
		dots.clear();

		headpicUrls.clear();

		for (Game game : GameList) {
			View dot = addDot(ll_dots);
			dots.add(dot);

			// 初始化图片url
			headpicUrls.add(game.getIcons());

		}
		dots.get(currentItem % (dots.size())).setBackgroundResource(
				R.drawable.dot_focused);

	}

	/**
	 * 添加滑动点
	 * 
	 * @param ll_dots
	 */
	private View addDot(LinearLayout ll_dots) {
		// 初始化圆点
		View view = new View(this, null, R.style.dot_style);
		view.setBackgroundResource(R.drawable.dot_normal);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				ScreenUtil.dp2px(6), ScreenUtil.dp2px(6));
		mLayoutParams.rightMargin = ScreenUtil.dp2px(4);
		mLayoutParams.leftMargin = ScreenUtil.dp2px(4);
		mLayoutParams.topMargin = ScreenUtil.dp2px(12);
		mLayoutParams.bottomMargin = ScreenUtil.dp2px(14);
		ll_dots.addView(view, mLayoutParams);
		return view;
	}

	/**
	 * 发送更改viewpager消息 ， 切换viewper界面
	 */
	private void sendChangeMsg(long delayedTime) {
		Message msg = handler.obtainMessage();
		msg.what = CHANGE_VIEWPAGER;
		handler.removeMessages(CHANGE_VIEWPAGER);
		handler.sendMessageDelayed(msg, delayedTime);
	}

	private Handler toastHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			Log.e("数据handler", msg.what+"dsfsafsaf");
			
			if(msg.what==11){
				Toast.makeText(MainActivity.this.getApplicationContext(), "操作太频繁",
						1).show();
			}
			if(msg.what==1){
				Toast.makeText(MainActivity.this.getApplicationContext(), AppConfig.getDown(),
						1).show();
			}if(msg.what==2){
				Toast.makeText(MainActivity.this.getApplicationContext(), "请先登录",
						1).show();
			}
		};
	};

	private void setData() {
		// TODO Auto-generated method stub

		data = new ArrayList<Game>();

		adapter = new MainAdapter(data, this, toastHandler);

		listView = (PullToRefreshListView) findViewById(R.id.main_list);

		lv = listView.getRefreshableView();

		listView.setPullLoadEnabled(true);
		listView.setPullRefreshEnabled(true);
		listView.getRefreshableView().setSelector(
				new ColorDrawable(android.R.color.transparent));
		listView.getRefreshableView().setDividerHeight(0);
		listView.doPullRefreshing(true, 100);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");

					listView.onPullDownRefreshComplete();
				} else {
					getData(true);

				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout) listView.getFooterLoadingLayout())
							.getmHintView().setText("数据加载中...");
					getData(false);
				} else {
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					listView.onPullUpRefreshComplete();
				}
			}

		});

	}

	private void getData(final boolean isRefresh) {
		if (isRefresh) {
			page = 1;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null) {
					if (isRefresh) {
						data.clear();
					}
					data.addAll(getList);
					adapter.notifyDataSetChanged();
				}

				Timer timer = new Timer();
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TcpApi tcpApi = new TcpApi();
						if (GlobalParams.hasLogin) {
							if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
								tcpApi.getDownloadGameListState(
										GlobalParams.user.getUser_id(),
										GlobalParams.auth_id);
							}

						} else {
							if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
								tcpApi.getDownloadGameListState("",
										GlobalParams.auth_id);
							}
						}
					}
				};

				timer.schedule(task, 1000, 10000);

				if (isRefresh) {
					listView.onPullDownRefreshComplete();
				} else {
					listView.onPullUpRefreshComplete();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				GlobalParams.tcpClientHelper.setHandler(MyHandler);
				TcpApi api = new TcpApi();
				GameApi GameApi = new GameApi();
				if (GlobalParams.hasLogin) {
					api.login(GlobalParams.user.getUser_id());

					getList = GameApi.getGameListList(
							GlobalParams.user.getUser_id(), page + "", count
									+ "", "game_list");

				} else {
					getList = GameApi.getGameListList("", page + "",
							count + "", "game_list");

				}

				page++;
				return null;
			}
		}.execute("");
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = currentItem;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {

			currentItem = position;

			dots.get(oldPosition % (dots.size())).setBackgroundResource(
					R.drawable.dot_normal);
			dots.get(position % (dots.size())).setBackgroundResource(
					R.drawable.dot_focused);
			oldPosition = position;

			sendChangeMsg(3000);
		}

		public void onPageScrollStateChanged(int position) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (topData.size() == 1) {
				return 1;
			}
			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(View container, int progress) {

			int curretIndex = progress % (topData.size());
			ImageView imageView = new ImageView(MainActivity.this);
			imageView.setScaleType(ScaleType.CENTER_CROP);

			if (topData != null && topData.size() > curretIndex) {
				setOnClick4headPic(imageView, topData.get(curretIndex));
			}

			((ViewPager) container).addView(imageView);

			imageLoader.displayImage(headpicUrls.get(curretIndex), imageView,
					GlobalParams.Toppic);

			return imageView;
		}

		@Override
		public void destroyItem(View container, int arg1, Object arg2) {
			((ViewPager) container).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * 为头部viewPager设置点击事件
	 * 
	 * @param view
	 * @param fresh
	 */
	private void setOnClick4headPic(View view, final Game game) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent aItent = new Intent(MainActivity.this,
						DetailsActivity.class);

				aItent.putExtra("gameID", game.getGame_id());

				startActivity(aItent);
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.main_open:
			dl.close();
			break;
		case R.id.main_comment:
			if (GlobalParams.hasLogin) {
				startActivityWithSlideAnimation(MyCommentsActivity.class);
			} else {
				loginDialog = new LogInDialog(MainActivity.this);
				loginDialog.setDialogCallback(dialogcallback);
				loginDialog.show();
			}

			break;
		case R.id.main_download:
			if (GlobalParams.hasLogin) {
				startActivityWithSlideAnimation(MyDownloadActivity.class);
			} else {
				loginDialog = new LogInDialog(MainActivity.this);
				loginDialog.setDialogCallback(dialogcallback);
				loginDialog.show();
			}

			break;
		case R.id.main_signOUt:

			AppConfig.loginOut();

			GlobalParams.user = null;
			GlobalParams.hasLogin = false;
			Name.setVisibility(View.GONE);
			signOUtLin.setVisibility(View.GONE);
			view7.setVisibility(View.GONE);
			reLog.setVisibility(View.VISIBLE);
			iconLoader.displayImage("", Icon, GlobalParams.iconOptions);
			getData(true);
			break;
		case R.id.main_control:
			startActivityWithSlideAnimation(ControllerConnectActivity.class);
			break;
		case R.id.main_gameFenlei:
			startActivityWithSlideAnimation(ClassificationActivity.class);
			break;
		case R.id.btn_Right_menu:
			if (GlobalParams.hasLogin) {
				startActivityWithSlideAnimation(MipcaActivityCapture.class);
			} else {

				loginDialog = new LogInDialog(MainActivity.this);
				loginDialog.setDialogCallback(dialogcallback);
				loginDialog.show();

			}
			break;
		case R.id.iv_bottom:

			Intent aIntent = new Intent(MainActivity.this,
					UpdateUserInfoActivity.class);
			this.startActivityForResult(aIntent, 0);
			break;
		default:
			break;
		}

	}

	private void setUpViews() {

		btn_open = (Button) findViewById(R.id.btn_open_menu);
		btn_open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				dl.open();

			}
		});
	}

	Dialogcallback dialogcallback = new Dialogcallback() {

		@Override
		public void dialogdo(String UserName, String PassWord) {
			// TODO Auto-generated method stub
			getLogInData(UserName, PassWord);

		}

	};

	private void getLogInData(final String userName, final String passWord) {
		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("true")) {
					getData(true);
				} else if (result.equals("false")) {
					Toast.makeText(getApplication(), "登录失败", Toast.LENGTH_SHORT)
							.show();
				}

				else {

					Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT)
							.show();

				}

			}
		};
		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {
					RegisterApi re = new RegisterApi(MainActivity.this);
					User user = re.Login(userName, passWord);
					if (user != null) {
						String userID = user.getUser_id();

						if (user != null
								&& StringUtils.isEmpty(user.getUserErr())) {

							GlobalParams.user = user;
							GlobalParams.tcpClientHelper.setHandler(MyHandler);
							TcpApi api = new TcpApi();
							api.login(GlobalParams.user.getUser_id());

							return "true";
						} else {

							return user.getUserErr();
						}

					}

					return "false";
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}
		}.executeProxy("");

	}

	Handler MyHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 1:

				String data = (String) msg.obj;
				Logger.e("data111", data);
				if (!StringUtils.isEmpty(data)) {
					try {
						JSONObject jsonObject = JSON.parseObject(data);
						String flag = jsonObject.getString("flag");
						if (flag.equals("task_list")) {
							JSONArray jsonArray = jsonObject
									.getJSONArray("info");

						} else if (flag.equals("login")) {
							if (jsonObject.getIntValue("error_code") >= 0) {

								GlobalParams.auth_id = jsonObject
										.getJSONObject("info").getString(
												"auth_id");

								GlobalParams.hasLogin = true;
								AppConfig.setPrefUserInfo(GlobalParams.user);
								Name.setVisibility(View.VISIBLE);
								reLog.setVisibility(View.GONE);
								signOUtLin.setVisibility(View.VISIBLE);
								view7.setVisibility(View.VISIBLE);
								Name.setText(GlobalParams.user.getNick_name());
								iconLoader.displayImage(
										GlobalParams.user.getUser_icon(), Icon,
										GlobalParams.iconOptions);
								loginDialog.dismiss();
								Toast.makeText(MainActivity.this, "登录成功",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplication(), "登录失败",
										Toast.LENGTH_SHORT).show();
							}

						}else if(flag.equals("pc")){
							String eerText = jsonObject.getString("message");
//							downText.setText(eerText);
								AppConfig.isDownLoad(eerText);
							}
							

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.e("requestCode", resultCode + "");
		System.out
				.println("request : " + requestCode + "\r\n" + "result : "
						+ resultCode + "data  :"
						+ data.getExtras().getString("result"));
		try {
			if (AppConfig.getPrefUserInfo() != null) {
				GlobalParams.user = AppConfig.getPrefUserInfo();
			}

			if (resultCode == 1) {
				if (GlobalParams.hasLogin) {
					Name.setVisibility(View.VISIBLE);
					reLog.setVisibility(View.GONE);
					signOUtLin.setVisibility(View.VISIBLE);
					view7.setVisibility(View.VISIBLE);
					Name.setText(GlobalParams.user.getNick_name());
					iconLoader.displayImage(GlobalParams.user.getUser_icon(),
							Icon, GlobalParams.iconOptions);

				}

			}
			if (resultCode == 2) {
				if (GlobalParams.hasLogin) {
					Name.setVisibility(View.VISIBLE);
					reLog.setVisibility(View.GONE);
					signOUtLin.setVisibility(View.VISIBLE);
					view7.setVisibility(View.VISIBLE);
					Name.setText(GlobalParams.user.getNick_name());
					iconLoader.displayImage(GlobalParams.user.getUser_icon(),
							Icon, GlobalParams.iconOptions);
					loginDialog.dismiss();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(MainActivity.this, "注册失败", Toast.LENGTH_SHORT);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
