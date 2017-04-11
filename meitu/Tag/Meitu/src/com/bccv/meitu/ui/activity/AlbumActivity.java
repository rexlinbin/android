package com.bccv.meitu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.common.GlobalConstants;
import com.bccv.meitu.model.Comment;
import com.bccv.meitu.model.GetSpecialResBean;
import com.bccv.meitu.model.Special;
import com.bccv.meitu.model.Tag;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.ShareSDK;
import com.bccv.meitu.sns.ShareSDK.ShareInfo;
import com.bccv.meitu.sns.ShareSDK.shareCallback;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.StringUtil;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.view.AddLableView;
import com.bccv.meitu.view.DefaultLableView;
import com.bccv.meitu.view.DefaultLableView.ExpandOnClickListener;
import com.bccv.meitu.view.DefaultLableView.LableOnClickListener;
import com.bccv.meitu.view.PullToZoomListView;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;
/**
 * AlbumActivity:专辑类
 * @author ZhaoHaiyang
 * @since 2014年11月7日10:44:20
 */
public class AlbumActivity extends BaseActivity {
	private static String TAG = "AlbumActivity.Exception";
	private String special_id;//根据id获取数据
	private final int DEFAULT_LABLE_NUM = 6;
	private final int LABLE_MAX_NUM = 10;
	
	/****************主页面上的控件**************/
	private ImageView iv_zoom;
	private ImageView album_back;//返回键
	private ImageView album_share;//分享键
	private TextView album_name;//标题
	private PullToZoomListView listView;
	private RelativeLayout waitting_layout;
	/**************************************/
	
	/****************listview中的控件****************/
	private ImageView pro_icon;
	private DefaultLableView myli1;//显示所有标签的控件
	private AddLableView add_lable;//显示当前标签的控件
	private ImageView bt;//增加标签按钮
	private EditText editText1;//编辑标签内容
	private String nowlabe;//当前标签内容
	private ArrayList<String> lableList;//所有标签内容集合
	private ImageView item_iv_icon;
	private TextView item_comment_name;
	private TextView item_tv_comment;
	private TextView item_tv_album_time;
	private ImageView item_see_more;
	private TextView pro_name;
	private TextView pro_time;
	private TextView dZanNum;
	private ImageView iv_guanzhu;
	private ImageView iv_zan;
	private ImageView iv_rela_first;
	private ImageView iv_rela_second;
	private ImageView iv_rela_third;
	private TextView album_no_rela_album;
	private TextView album_no_comment;
	private MyListAdapter mAdapter = new MyListAdapter();
	/**********************************************/
	
	/******************other*****************/
	private int listSize = 0;
	private String tag_add="";
	/****************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		special_id = getIntent().getExtras().getString("special_id");
//		showShortToast("id = "+special_id);
		if(special_id == null || special_id.equals("") || special_id.equals("-1")){
//			special_id = "1";
			showShortToast("数据异常");
			return ;
		}
		//初始化控件
		initView();
		//获取数据
		getData();
	}

	private void initView(){
		waitting_layout = (RelativeLayout) findViewById(R.id.waitting_layout);
		waitting_layout.setVisibility(View.VISIBLE);
		album_back = (ImageView) findViewById(R.id.album_back);
		album_share = (ImageView) findViewById(R.id.album_share);
		album_name = (TextView) findViewById(R.id.album_name);
		iv_zoom = (ImageView) findViewById(R.id.iv_zoom);
		listView = (PullToZoomListView) findViewById(R.id.list_view);
		album_back.setOnClickListener(this);
		album_share.setOnClickListener(this);
		iv_zoom.setOnClickListener(this);
		listView.setAdapter(mAdapter);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.album_back://返回键
			onBackPressed();
			break;
		case R.id.album_share://分享键
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.setContentString( albumNames == null ? "" : albumNames);
			shareInfo.setTitleString(albumName == null ? "" : albumName);
			shareInfo.setTagUrl(GlobalConstants.SHARE_URL+special_id);
			ShareSDK.showShare(this, album_share, shareInfo, new shareCallback() {
				@Override
				public void callback(boolean success) {
					if (success) {
						//XXX :
					}
				}
			});
			break;
		case R.id.iv_zoom://专辑封面
			Intent intent2 = new Intent(this,PhotoDetailsActivity.class);
			intent2.putExtra("special_id", special_id);
			intent2.putExtra("special_name", albumName == null ? "" : albumName);
			intent2.putExtra("special_names", albumNames == null ? "" : albumNames);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	
	class MyListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listSize+1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(position == 0){
				view = View.inflate(getApplicationContext(), R.layout.album_content_view, null);
				pro_icon = (ImageView) view.findViewById(R.id.promulgator_icon);
				pro_name = (TextView) view.findViewById(R.id.promulgator_name);
				pro_time = (TextView) view.findViewById(R.id.promulgate_time);
				dZanNum = (TextView) view.findViewById(R.id.album_tv_favorite);
				iv_guanzhu= (ImageView) view.findViewById(R.id.album_guanzhu);
				iv_zan= (ImageView) view.findViewById(R.id.album_favorite);
				album_no_comment = (TextView) view.findViewById(R.id.album_no_comment);
				add_lable = (AddLableView) view.findViewById(R.id.test_addlable);
				iv_rela_first = (ImageView) view.findViewById(R.id.iv_rela_album_first);
				iv_rela_second = (ImageView) view.findViewById(R.id.iv_rela_album_second);
				iv_rela_third = (ImageView) view.findViewById(R.id.iv_rela_album_third);
				album_no_rela_album  = (TextView) view.findViewById(R.id.album_no_rela_album);
				myli1 = (DefaultLableView) view.findViewById(R.id.test_lableview);
				editText1 = (EditText) view.findViewById(R.id.test_et);
				bt = (ImageView) view.findViewById(R.id.test_bt);
				bt.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						nowlabe= editText1.getText().toString();
						if(nowlabe.length() > 8){
							showShortToast("标签不能超过8个字");
							return ;
						}
						if(nowlabe == null || nowlabe.equals("")){
							showShortToast("标签不能为空");
							return ;
						}
						editText1.setText("");
						if(add_lable.getAllList() == null || add_lable.getAllList().size() < LABLE_MAX_NUM){
							add_lable.addLabel(nowlabe);
						}else{
							showShortToast("只能添加十个标签");
						}
					}
				});
				pro_icon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(author_id == -1 || author_id == 0){
							showShortToast("See author_id is wrong!");
						}else{
							Intent intent = new Intent(AlbumActivity.this,ProZoneActivity.class);
							intent.putExtra("author_id", author_id);
							startActivity(intent);
						}
					}
				});
				iv_guanzhu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showShortToast("关注");
					}
				});
				iv_zan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showShortToast("赞!");
					}
				});
				if(albumIconUrl != null && !albumIconUrl.equals("")){
					ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(albumIconUrl, pro_icon,ImageLoaderUtil.getRoundedImageOptions());
				}
				if(authorName != null && !authorName.equals("")){
					pro_name.setText(authorName);
				}
				if(authorTime != null && !authorTime.equals("")){
					pro_time.setText(StringUtil.friendly_time(authorTime));
				}
				if(zanNum != -1){
					dZanNum.setText(String.valueOf(zanNum));
				}
				if(relaAlbunList != null){
					int size = relaAlbunList.size();
					if(size > 0){
						album_no_rela_album.setVisibility(View.GONE);
						for (int i = 0; i < size; i++) {
							if(i== 0){
								iv_rela_first.setVisibility(View.VISIBLE);
								ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(relaAlbunList.get(i).getSpecial_pic(), iv_rela_first);
								final int id = relaAlbunList.get(i).getSpecial_id();
								iv_rela_first.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(getApplicationContext(),AlbumActivity.class);
										intent.putExtra("special_id", id);
										startActivity(intent);
									}
								});
							}else if(i == 1){
								iv_rela_second.setVisibility(View.VISIBLE);
								ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(relaAlbunList.get(i).getSpecial_pic(), iv_rela_second);
								final int id = relaAlbunList.get(i).getSpecial_id();
								iv_rela_second.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(getApplicationContext(),AlbumActivity.class);
										intent.putExtra("special_id", id);
										startActivity(intent);
									}
								});
							}else if(i == 2){
								iv_rela_third.setVisibility(View.VISIBLE);
								ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(relaAlbunList.get(i).getSpecial_pic(), iv_rela_third);
								final int id = relaAlbunList.get(i).getSpecial_id();
								iv_rela_third.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(getApplicationContext(),AlbumActivity.class);
										intent.putExtra("special_id", id);
										startActivity(intent);
									}
								});
							}else{
								break;
							}
						}
					}else{
						album_no_rela_album.setVisibility(View.VISIBLE);
					}
				}else{
					album_no_rela_album.setVisibility(View.VISIBLE);
				}
				//初始化标签
				initLable();
				if(listSize == 0){
					album_no_comment.setVisibility(View.VISIBLE);
				}else{
					album_no_comment.setVisibility(View.GONE);
				}
			}else{
				view = View.inflate(getApplicationContext(), R.layout.album_list_item, null);
				item_iv_icon = (ImageView) view.findViewById(R.id.item_iv_icon);
				item_comment_name = (TextView) view.findViewById(R.id.item_comment_name);
				item_tv_comment = (TextView) view.findViewById(R.id.item_tv_comment);
				item_tv_album_time = (TextView) view.findViewById(R.id.item_tv_album_time);
				item_see_more = (ImageView) view.findViewById(R.id.item_see_more);
				if(position == (listSize)){
					item_see_more.setVisibility(View.VISIBLE);
					item_see_more.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							showShortToast("查看更多评论");
						}
					});
				}else{
					item_see_more.setVisibility(View.GONE);
				}
				int hot = hotCommentsList.size();
				if(hot >= 5){
					setView(hotCommentsList.get(position - 1).getUser_name()
							, hotCommentsList.get(position - 1).getComments_content(), 
							hotCommentsList.get(position - 1).getCtime(), 
							hotCommentsList.get(position - 1).getUser_icon());
				}else{
					if(hot >= position){
						setView(hotCommentsList.get(position - 1).getUser_name()
								, hotCommentsList.get(position - 1).getComments_content()
								, hotCommentsList.get(position - 1).getCtime()
								, hotCommentsList.get(position - 1).getUser_icon());
					}else{
						setView(commentsList.get(position-hot-1).getUser_name()
								, commentsList.get(position-hot-1).getComments_content()
								, commentsList.get(position-hot-1).getCtime()
								, commentsList.get(position-hot-1).getUser_icon());
					}
				}
			}
			return view;
		}
		private void setView(String name,String comment,int time,String url){
			item_comment_name.setText(name);
			item_tv_comment.setText(comment);
			item_tv_album_time.setText(StringUtil.friendly_time(String.valueOf(time)));
			ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(url,
					item_iv_icon,ImageLoaderUtil.getRoundedImageOptions());
		}
	}
	
	private void initLable() {
		if (lableList == null) {
			lableList = new ArrayList<String>();
		}else{
			lableList.clear();
		}
		if(tagList == null || tagList.size() == 0){
			lableList.add("美女");
			lableList.add("飞机场");
			lableList.add("萌妹子");
			lableList.add("女神");
			lableList.add("小清新");
			lableList.add("文艺范");
			lableList.add("女汉子");
			lableList.add("不忍直视");
		}else{
			for (int i = 0; i < tagList.size(); i++) {
				lableList.add(tagList.get(i).getTag_name());
			}
		}
		myli1.setLableOnClickListener(new LableOnClickListener() {
			@Override
			public void OnClick(String s) {
//				add_lable.setVisibility(View.VISIBLE);
				if(s == null || s.equals("")){
					showShortToast("标签内容不能为空");
					return ;
				}
				if(add_lable.getAllList() == null || add_lable.getAllList().size() < LABLE_MAX_NUM){
					add_lable.addLabel(s);
				}else{
					showShortToast("只能添加十个标签");
				}
//				add_lable.addLabel(s);
			}
		});
		myli1.setExpandOnClickListener(new ExpandOnClickListener() {
			@Override
			public void OnClick(boolean isShow) {
				if(isShow){
					editText1.setVisibility(View.GONE);
					bt.setVisibility(View.GONE);
				}else{
					editText1.setVisibility(View.VISIBLE);
					bt.setVisibility(View.VISIBLE);
				}
			}
		});
		if (lableList.size() > DEFAULT_LABLE_NUM) {
			myli1.addLable(lableList, DEFAULT_LABLE_NUM, false);
		}else if (lableList.size()>0 && lableList.size()<= DEFAULT_LABLE_NUM) {
			myli1.addLable(lableList, DEFAULT_LABLE_NUM, false);
		}
	}
	
	private void getData(){
		if(SystemUtil.isNetOkWithToast(getApplicationContext())){
			NetWorkAPI.getSpecial(getApplicationContext(), Integer.valueOf(special_id), new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					waitting_layout.setVisibility(View.GONE);
					GetSpecialResBean data = (GetSpecialResBean)response;
//					System.out.println("data : "+data.toString());
					getAllData(data);
				}
				
				@Override
				public void onError(String errorMsg) {
					waitting_layout.setVisibility(View.GONE);
					Logger.e(TAG, "net_error", errorMsg);
					Toast.makeText(getApplicationContext(), "网络或数据错误", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onCancel() {}
			});
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	private void setViewData(){
//		showShortToast("set view");
		ImageLoaderUtil.getInstance(this).displayImage(albumPicUrl, iv_zoom);
//		System.out.println("author_icon : "+albumIconUrl+"\r\n pro_name : "+authorName+"\r\n protime : "+authorTime);
		album_name.setText(albumName);
		if(getListSize() >= 5){
			listSize = 5;
		}else{
			listSize = getListSize();
		}
		mAdapter.notifyDataSetChanged();
	}
	
	private int getListSize(){
		int hotCommentSize = hotCommentsList == null ? 0 : hotCommentsList.size();
		int commentSize = commentsList == null ? 0 : commentsList.size();
		int allComments = hotCommentSize + commentSize;
//		System.out.println("hot = "+hotCommentSize +"\r\n com = "+commentSize+"\r\n all = "+allComments);
		return allComments;
	}
	
	private String albumName;//专辑名称
	private String albumNames;//分享语句
	private String albumPicUrl;//专辑封面
	private String albumIconUrl;//作者头像
	private String authorName;//上传专辑作者
	private String authorTime;//上传专辑时间
	private int careNum;//关注数量
	private int zanNum = -1;//点赞数量
	private int commentNum;//评论数量
	private int isCare;//是否关注
	private int isZan;//是否点赞
	private int author_id;//作者id
	private List<Comment> hotCommentsList;
	private List<Comment> commentsList ;
	private List<Special> relaAlbunList ;
	private List<Tag> tagList;
	private void getAllData(GetSpecialResBean data){
		if(data != null){
			isCare = data.getIsfavorite();
			isZan = data.getIszan();
			commentsList = data.getComment();
			hotCommentsList = data.getComment_hot();
			relaAlbunList = data.getList();
		}else{
//			System.out.println("data is null");
			return ;
		}
		if(data.getSpecial() != null){
			albumName = data.getSpecial().getSpecial_name();
			albumNames = data.getSpecial().getSpecial_names();
			albumPicUrl = data.getSpecial().getSpecial_pic();
			albumIconUrl = data.getSpecial().getAuthor_icon();
			authorName = data.getSpecial().getAuthor_name();
			authorTime = data.getSpecial().getCtime();
			careNum = data.getSpecial().getCare_num();
			zanNum = data.getSpecial().getZan_num();
			commentNum = data.getSpecial().getComment_num();
			author_id = data.getSpecial().getAuthor_id();
			tagList = data.getSpecial().getTag();
		}else{
//			System.out.println("data.getSpecial is null");
			return ;
		}
		setViewData();
	}
	
	private void sendTagToServer(String tag_add){
		if(SystemUtil.isNetOkWithToast(getApplicationContext())){
			NetWorkAPI.addTag(mContext, Integer.valueOf(special_id), tag_add, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					boolean success = response.success;
					if(success){
//						showShortToast("哎呦我擦");
					}else{
//						showShortToast("哎呦我去");
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					showShortToast("error : "+errorMsg);
				}
				
				@Override
				public void onCancel() {
				}
			});
		}
	}
	
	@Override
	protected void onDestroy() {
		if(add_lable != null){
			List<String> allList = add_lable.getAllList();
			if(allList != null && allList.size() > 0){
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < allList.size(); i++) {
					sb.append(allList.get(i));
					if(i < allList.size()-1)
						sb.append(",");
				}
//			System.out.println(sb.toString());
				sendTagToServer(sb.toString());
			}
		}
		super.onDestroy();
	}
}
