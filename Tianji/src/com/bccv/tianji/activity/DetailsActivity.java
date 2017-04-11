package com.bccv.tianji.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bccv.tianji.R;
import com.bccv.tianji.api.GameApi;
import com.bccv.tianji.fragment.CommentFragment;
import com.bccv.tianji.fragment.IntroductionFragment;
import com.bccv.tianji.fragment.RequestFragment;
import com.bccv.tianji.model.Gameinfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;

public class DetailsActivity extends FragmentActivity {
	private ViewPager pager;
	private Button back;
	List<Fragment> listViews;
	Context context = null;
	private Animation animation;
	private int offSet;
	private int currentItem = 0;
	private int bmWidth;
	private ImageView imageView1,imageView2,imageView3;
	private Button btn1, btn2, btn3;
	private String GameID;
	private Gameinfo data;
	public static String requst;
	Handler handler;
	ImageView image;
	TextView title;
	TextView content;
	ImageView icon;
	TextView downNum;
	TextView downSpeed;

	public static String sart1, sart2, sart3, sart4, sart5, totalStar, Point;
	private ImageLoader imageLoader = ImageLoader.getInstance();;
	Button soreBtn;
	private TextView Downtime;
	public static List<String> imageData = new ArrayList<String>();
	CommentFragment commentFragment;
	IntroductionFragment introductionFrament;
	RequestFragment recommendFragment;
	public static String introduce;
	MyPageAdapter pagerAdapter;
int gameId;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_details);

		Button back = (Button) findViewById(R.id.btn_open_menu);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		pager = (ViewPager) findViewById(R.id.details_viewpager);

		GameID = getIntent().getStringExtra("gameID");
		
		
		// 定放一个放view的list，用于存放viewPager用到的view
		listViews = new ArrayList<Fragment>();
		imageView1 = (ImageView) findViewById(R.id.line_imageView);
		imageView2=(ImageView) findViewById(R.id.line_imageView2);
		imageView3=(ImageView) findViewById(R.id.line_imageView3);
		
		
		
		image = (ImageView) findViewById(R.id.main_list_image);
		title = (TextView) findViewById(R.id.main_list_title);
		content = (TextView) findViewById(R.id.main_list_content);

		icon = (ImageView) findViewById(R.id.main_list_icon);
		downNum = (TextView) findViewById(R.id.main_list_num);
		downSpeed = (TextView) findViewById(R.id.main_list_speed);
		soreBtn = (Button) findViewById(R.id.main_list_score);
		AssetManager mgr = DetailsActivity.this.getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Georgia.ttf");// 根据路径得到Typeface
		soreBtn.setTypeface(tf);// 设置字体
		Downtime = (TextView) findViewById(R.id.main_list_time);

		new IntroductionTask().execute();
		setFragments();

		pagerAdapter = new MyPageAdapter(getSupportFragmentManager(), listViews);
		pager.setAdapter(pagerAdapter);
		pager.setOffscreenPageLimit(3);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 当viewPager发生改变时，同时改变tabhost上面的currentTab

				switch (position) {
				case 0:

//					if (currentItem == 1) {
//						animation = new TranslateAnimation(
//								offSet * 2 + bmWidth, 0, 0, 0);
//					} else if (currentItem == 2) {
//						animation = new TranslateAnimation(offSet * 4 + 2
//								* bmWidth, 0, 0, 0);
//					}
					
					imageView1.setVisibility(View.VISIBLE);
					imageView2.setVisibility(View.INVISIBLE);
					imageView3.setVisibility(View.INVISIBLE);
					
					
					
					
					break;
				case 1:

//					if (currentItem == 0) {
//						animation = new TranslateAnimation(0, offSet * 2
//								+ bmWidth, 0, 0);
//					} else if (currentItem == 2) {
//						animation = new TranslateAnimation(4 * offSet + 2
//								* bmWidth, offSet * 2 + bmWidth, 0, 0);
//					}
					imageView1.setVisibility(View.INVISIBLE);
					imageView2.setVisibility(View.VISIBLE);
					imageView3.setVisibility(View.INVISIBLE);
					break;
				case 2:

//					if (currentItem == 0) {
//						animation = new TranslateAnimation(0, 4 * offSet + 2
//								* bmWidth, 0, 0);
//					} else if (currentItem == 1) {
//						animation = new TranslateAnimation(
//								offSet * 2 + bmWidth, 4 * offSet + 2 * bmWidth,
//								0, 0);
//					}
					imageView1.setVisibility(View.INVISIBLE);
					imageView2.setVisibility(View.INVISIBLE);
					imageView3.setVisibility(View.VISIBLE);
					break;
				}

//				currentItem = position;
//
//				animation.setDuration(500);
//				animation.setFillAfter(true);
//				imageView1.startAnimation(animation);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	private void setFragments() {
		// TODO Auto-generated method stub

		introductionFrament = new IntroductionFragment();
		introductionFrament.setGameId(GameID);

		listViews.add(introductionFrament);
		commentFragment = new CommentFragment();
		commentFragment.setGameId(GameID);

		listViews.add(commentFragment);

		recommendFragment = new RequestFragment();

		recommendFragment.setGameID(GameID);
		listViews.add(recommendFragment);

		 setTitle();
	}

	private void setTitle() {
		// TODO Auto-generated method stub

		btn1 = (Button) findViewById(R.id.details_intro_btn);

		btn2 = (Button) findViewById(R.id.details_comment_btn);
		btn3 = (Button) findViewById(R.id.details_requst_btn);

		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (currentItem == 1) {
//					animation = new TranslateAnimation(offSet * 2 + bmWidth, 0,
//							0, 0);
//				} else if (currentItem == 2) {
//					animation = new TranslateAnimation(
//							offSet * 4 + 2 * bmWidth, 0, 0, 0);
//				}
//
//				if (currentItem != 0) {
					currentItem = 0;
//
//					animation.setDuration(500);
//					animation.setFillAfter(true);
//					imageView1.startAnimation(animation);
					pager.setCurrentItem(currentItem);
//				}
				
				imageView1.setVisibility(View.VISIBLE);
				imageView2.setVisibility(View.INVISIBLE);
				imageView3.setVisibility(View.INVISIBLE);
				
			}
		});
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (currentItem == 0) {
//					animation = new TranslateAnimation(0, offSet * 2 + bmWidth,
//							0, 0);
//				} else if (currentItem == 2) {
//					animation = new TranslateAnimation(
//							4 * offSet + 2 * bmWidth, offSet * 2 + bmWidth, 0,
//							0);
//				}

//				if (currentItem != 1) {
					currentItem = 1;
//
//					animation.setDuration(500);
//					animation.setFillAfter(true);
//					imageView1.startAnimation(animation);
					pager.setCurrentItem(currentItem);
//				}
				imageView1.setVisibility(View.INVISIBLE);
				imageView2.setVisibility(View.VISIBLE);
				imageView3.setVisibility(View.INVISIBLE);
			}
		});
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (currentItem == 0) {
//					animation = new TranslateAnimation(0, 4 * offSet + 2
//							* bmWidth, 0, 0);
//				} else if (currentItem == 1) {
//					animation = new TranslateAnimation(offSet * 2 + bmWidth, 4
//							* offSet + 2 * bmWidth, 0, 0);
//				}
//				if (currentItem != 2) {
					currentItem = 2;
//
//					animation.setDuration(500);
//					animation.setFillAfter(true);
//					imageView1.startAnimation(animation);
					pager.setCurrentItem(currentItem);
//				}
				imageView1.setVisibility(View.INVISIBLE);
				imageView2.setVisibility(View.INVISIBLE);
				imageView3.setVisibility(View.VISIBLE);
			}

		});

	}

	class IntroductionTask extends AsyncTask<String, Void, Gameinfo> {

		@Override
		protected Gameinfo doInBackground(String... params) {

			GameApi gameApi = new GameApi();

			data = gameApi.getGameInfoList("", GameID);

			return data;

		}

		@Override
		protected void onPostExecute(Gameinfo list) {
			super.onPostExecute(list);
			requst = list.getSystem_demand();
			introduce = list.getIntro();
			imageData = list.getPics();
			Point = list.getPoint();

			imageLoader.displayImage(list.getIcons(), image,
					GlobalParams.iconOptions);

			title.setText(list.getTitle());

			content.setText(list.getType_name());
			downNum.setText(list.getDown_num()+"次下载");
			if(list.getGame_size().equals("null")){
				downSpeed.setText("0GB");
			}else{
				downSpeed.setText(list.getGame_size() + "GB");	
				
			}
		
			soreBtn.setText(Point);
			Downtime.setText("发布日期："+list.getSend_times());

			JSONObject obj = JSON.parseObject(list.getStar_info());

			sart1 = obj.getString("star1");
			sart2 = obj.getString("star2");
			sart3 = obj.getString("star3");
			sart4 = obj.getString("star4");
			sart5 = obj.getString("star5");
			totalStar = obj.getString("total_star");

			
		}

	}

	private class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> list;

		public MyPageAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

	}

}
