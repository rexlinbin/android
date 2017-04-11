package com.bccv.zhuiyingzhihanju.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.CollectActivity;
import com.bccv.zhuiyingzhihanju.activity.DownloadActivity;
import com.bccv.zhuiyingzhihanju.activity.HistoryActivity;
import com.bccv.zhuiyingzhihanju.activity.LoginActivity;
import com.bccv.zhuiyingzhihanju.activity.MoreListActivity;
import com.bccv.zhuiyingzhihanju.activity.MovieSearchActivity;
import com.bccv.zhuiyingzhihanju.activity.PersonalDataActivity;
import com.bccv.zhuiyingzhihanju.activity.SetActivity;
import com.bccv.zhuiyingzhihanju.activity.Video2DPlayerActivity;
import com.bccv.zhuiyingzhihanju.adapter.HistoryAdapter;
import com.bccv.zhuiyingzhihanju.api.MSGApi;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MyMsg;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.SerializationUtil;
import com.utils.tools.StringUtils;
import com.utils.views.MyGridView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class YMyFrament extends BaseActivity {

	private MyGridView Hlist;

	private List<Movie> data;
	private MyMsg msg = new MyMsg();
	private BaseAdapter hisAdapter;

	private RelativeLayout HGo, CGo, MGo, DGo;
	private RelativeLayout setR;
	private TextView hText;
	private List<Movie> HlistData;
	private ImageView goIm, cgoIm, DGoIm, MgoIm;

	private TextView name, sign;
	private ImageView icon, msg_dot;
	private RelativeLayout MYlogin;
	private ImageLoader iconLoader = ImageLoader.getInstance();
	private String GeMsg="不要懒，想一个个性签名吧~";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);

		ImageButton collectBtn = (ImageButton) findViewById(R.id.title_search_edit);

		ImageButton DownBtn = (ImageButton) findViewById(R.id.title_search_btnDown);

		collectBtn.setVisibility(View.INVISIBLE);

		DownBtn.setVisibility(View.INVISIBLE);
		ImageButton hisBtn=(ImageButton) findViewById(R.id.title_search_btnhis);
		hisBtn.setVisibility(View.INVISIBLE);
		DownBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (GlobalParams.hasLogin) {
					Intent aIntent = new Intent(YMyFrament.this, MovieSearchActivity.class);

					startActivity(aIntent);
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("type", "D");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}
			}
		});

		MYlogin = (RelativeLayout) findViewById(R.id.my_login);

		msg_dot = (ImageView) findViewById(R.id.msg_dot);
		ImageButton setBtn = (ImageButton) findViewById(R.id.my_set_goBtn);
		setBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(YMyFrament.this, SetActivity.class);

				startActivity(aIntent);
			}
		});

		name = (TextView) findViewById(R.id.my_name);
		sign = (TextView) findViewById(R.id.my_sign);
		icon = (ImageView) findViewById(R.id.my_icon);

		if (GlobalParams.hasLogin) {
			
		
		
			name.setText(GlobalParams.user.getNick_name());
			if(StringUtils.isEmpty(GlobalParams.user.getDes())){
				sign.setText(GeMsg);
			
			}else{
				sign.setText(GlobalParams.user.getDes());

			}
		
			iconLoader.displayImage(GlobalParams.user.getAvatars(), icon);

		} else {

		}

		MYlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GlobalParams.hasLogin) {

					Intent aIntent = new Intent(YMyFrament.this, PersonalDataActivity.class);
					startActivityForResult(aIntent, 0);
				} else {

					Intent aIntent = new Intent(YMyFrament.this, LoginActivity.class);
					aIntent.putExtra("type", "Y");
					startActivityForResult(aIntent, 0);

				}
			}
		});

		data = new ArrayList<Movie>();

		hText = (TextView) findViewById(R.id.my_history_text2);

		Hlist = (MyGridView) findViewById(R.id.my_history_grid);

		hisAdapter = new HistoryAdapter(this, data);

		Hlist.setAdapter(hisAdapter);

		Hlist.setSelector(new ColorDrawable(android.R.color.transparent));
		Hlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				Movie movie = HlistData.get(position);

				HlistData.remove(movie);
				HlistData.add(0, movie);
				SerializationUtil.wirteHistorySerialization(YMyFrament.this, (Serializable) HlistData);

				Intent aIntent = new Intent(YMyFrament.this, Video2DPlayerActivity.class);

				aIntent.putExtra("movie_id", movie.getId());
				aIntent.putExtra("type_id", movie.getType_id());
				aIntent.putExtra("episodes_id", movie.getEpisode_id());


				startActivity(aIntent);

			}
		});

		HGo = (RelativeLayout) findViewById(R.id.my_history_rl);
		setR = (RelativeLayout) findViewById(R.id.my_set_rl);
		// 下载
		DGoIm = (ImageButton) findViewById(R.id.my_huancun_goBtn);
		DGo = (RelativeLayout) findViewById(R.id.my_down_rl);
		CGo = (RelativeLayout) findViewById(R.id.my_collect_rl);
		MGo = (RelativeLayout) findViewById(R.id.my_msg_rl);
		goIm = (ImageView) findViewById(R.id.my_history_goBtn);
		cgoIm = (ImageView) findViewById(R.id.my_collect_goBtn);
		MgoIm = (ImageView) findViewById(R.id.my_msg_goBtn);
		HGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivityWithSlideAnimation(HistoryActivity.class);

			}
		});
		goIm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivityWithSlideAnimation(HistoryActivity.class);

			}
		});
		CGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(CollectActivity.class);

				} else {
					Bundle bundle = new Bundle();
					bundle.putString("type", "C");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}

			}
		});
		cgoIm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(CollectActivity.class);

				} else {
					Bundle bundle = new Bundle();
					bundle.putString("type", "C");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}
			}
		});
		MGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(MoreListActivity.class);
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("type", "M");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}

			}
		});
		MgoIm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(MoreListActivity.class);
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("type", "M");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}

			}
		});
		DGoIm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(DownloadActivity.class);
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("type", "D");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}

			}
		});

		DGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(DownloadActivity.class);
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("type", "D");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}

			}
		});

		setR.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent aIntent = new Intent(YMyFrament.this, SetActivity.class);
				startActivityForResult(aIntent, 0);
			}
		});

		getData();

	}

	public void getData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {

				return "true";
			}
		}.execute("");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		data.clear();
		HlistData = SerializationUtil.readHistoryCache(this.getApplicationContext());

		if (HlistData != null) {
			if (HlistData.size() > 0) {
				goIm.setVisibility(View.VISIBLE);
				hText.setVisibility(View.INVISIBLE);
				Hlist.setVisibility(View.VISIBLE);
				if (HlistData.size() > 5) {
					data.add(HlistData.get(0));
					data.add(HlistData.get(1));
					data.add(HlistData.get(2));
					data.add(HlistData.get(3));
					data.add(HlistData.get(4));
				} else {
					data.addAll(HlistData);
				}
				hisAdapter.notifyDataSetChanged();
			} else {

				hText.setVisibility(View.VISIBLE);
				goIm.setVisibility(View.INVISIBLE);
				Hlist.setVisibility(View.GONE);
			}

		} else {
			hText.setVisibility(View.VISIBLE);
			goIm.setVisibility(View.INVISIBLE);
			Hlist.setVisibility(View.GONE);

		}
		if (GlobalParams.hasLogin) {
//			getIsMsg();

			name.setText(GlobalParams.user.getNick_name());
			if(StringUtils.isEmpty(GlobalParams.user.getDes())){
				sign.setText(GeMsg);
			
			}else{
				sign.setText(GlobalParams.user.getDes());

			}

			iconLoader.displayImage(GlobalParams.user.getAvatars(), icon);

		} else {
			name.setText("登陆/注册");
			sign.setText("据说，登录后看片更流畅~");

			icon.setImageResource(R.drawable.head_default);
		}
		TCAgent.onPageStart(getApplicationContext(), "YMyFrament");
	}

	public void getIsMsg() {
		
		if (!GlobalParams.hasMSG) {
			msg_dot.setVisibility(View.INVISIBLE);
			
		} else {
			msg_dot.setVisibility(View.VISIBLE);
			
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//		Log.e("requestCode", resultCode + "");
//		System.out.println("request : " + requestCode + "\r\n" + "result : " + resultCode + "data  :"
//				+ data.getExtras().getString("result"));
		try {
			if (AppConfig.getPrefUserInfo() != null) {
				GlobalParams.user = AppConfig.getPrefUserInfo();
			}

			if (resultCode == 1) {
				if (GlobalParams.hasLogin) {

					name.setText(GlobalParams.user.getNick_name());
					if(StringUtils.isEmpty(GlobalParams.user.getDes())){
						sign.setText(GeMsg);
					
					}else{
						sign.setText(GlobalParams.user.getDes());

					}

					iconLoader.displayImage(GlobalParams.user.getAvatars(), icon);

				}

			}
			if (resultCode == 2) {
				if (GlobalParams.hasLogin) {

					name.setText(GlobalParams.user.getNick_name());
					if(StringUtils.isEmpty(GlobalParams.user.getDes())){
						sign.setText(GeMsg);
					
					}else{
						sign.setText(GlobalParams.user.getDes());

					}

					iconLoader.displayImage(GlobalParams.user.getAvatars(), icon);
				}

			}
			if (resultCode == 3) {
				if (!GlobalParams.hasLogin) {

					name.setText("登陆/注册");
					sign.setText("据说，登录后看片更流畅~");

					icon.setImageResource(R.drawable.head_default);
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(YMyFrament.this, "注册失败", Toast.LENGTH_SHORT);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TCAgent.onPageEnd(getApplicationContext(), "YMyFrament");
	}
}
