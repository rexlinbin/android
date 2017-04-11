package com.bccv.meitu.ui.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.GetUserInfoProvider;
import io.rong.imlib.RongIMClient.UserInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.db.StorageModule;
import com.bccv.meitu.model.Comment;
import com.bccv.meitu.model.GetSpecialResBean;
import com.bccv.meitu.model.Special;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.SNSLoginManager;
import com.bccv.meitu.sns.ShareConstants;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.utils.GetTimeUtil;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.PreferenceHelper;
import com.bccv.meitu.utils.RCUtil;
import com.bccv.meitu.utils.RCUtil.RCConnectCallback;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.view.PullToZoomListView;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

/**
 * AlbumActivity:专辑类
 * 
 * @author ZhaoHaiyang
 * @since 2014年11月7日10:44:20
 */
public class AlbumActivity extends BaseActivity {
	private static String TAG = "AlbumActivity.Exception";
	private String special_id;// 根据id获取数据
//	private final int DEFAULT_LABLE_NUM = 6;
//	private final int LABLE_MAX_NUM = 10;
	private boolean isLogin = false;

	/************** 主页面上的控件 **************/
	private ImageView iv_zoom;
	private ImageView album_back;// 返回键
	private ImageView album_photo;// 分享键
	private ImageView iv_chat_btn;
//	private TextView album_name;// 标题
	private PullToZoomListView listView;
	private RelativeLayout waitting_layout;
	private RelativeLayout album_tishi;
	/**************************************/

	/**************** listView中的控件 ****************/
//	private DefaultLableView myli1;// 显示所有标签的控件
//	private AddLableView add_lable;// 显示当前标签的控件
//	private ImageView bt;// 增加标签按钮
//	private EditText editText1;// 编辑标签内容
//	private String nowlabe;// 当前标签内容
//	private ArrayList<String> lableList;// 所有标签内容集合
	private RelativeLayout album_list_rl;
	private ImageView pro_icon;
	private TextView add_description;
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

	/****************** other *****************/
	private int listSize = 0;
//	private String tag_add = "";
	private long curTime;
	private int careFlag = -1;
	private int zanFlag = -1;
	private static AlbumActivity activity = null;
	/****************************************/
//	private InputMethodManager imm;
//	private SharePopwindow mPopwindow;
//	private SNSShareManager ssManager;
	private StorageModule sModule;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		special_id = getIntent().getExtras().getString("special_id");
		// showShortToast("id = "+special_id);
		if (special_id == null || special_id.equals("")
				|| special_id.equals("-1")) {
			// special_id = "1";
			showShortToast("数据异常");
			return;
		}
//		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		activity = this;
		isLogin = UserInfoManager.isLogin();
		sModule = StorageModule.getInstance();
//		getLocalData();
		// 初始化控件
		initView();
//		ssManager = SNSShareManager.getInstance();
//		ssManager.setShareStateListener(new ShareStateListener() {
//			@Override
//			public void shareAction(int action, String info) {
//				switch (action) {
//				case ShareStateListener.SHARING :
//					waitting_layout.setVisibility(View.GONE);
//					Logger.v(TAG, "iniShareManager", " shareAction : SHARING");
//					break;
//				case ShareStateListener.SHARE_SUCCESS :
//					Logger.v(TAG, "shareAction", "c!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " );
//					Toast.makeText(ApplicationManager.getGlobalContext(), "分享成功", Toast.LENGTH_SHORT).show();
////					mHandler.sendEmptyMessage(SNS_SHARE_SUCCESS);
//					Logger.v(TAG, "iniShareManager", " shareAction : SHARE_SUCCESS");
//					
//					NetWorkAPI.share(getApplicationContext(), Integer.valueOf(special_id), new HttpCallback() {
//						@Override
//						public void onResult(NetResBean response) {
//							if (response.success) {
////								showShortToast("分享上报成功");
//							}
//						}
//						@Override
//						public void onError(String errorMsg) {}
//						@Override
//						public void onCancel() {}
//					});
//					break;
//				case ShareStateListener.SHARE_FAILED :
//					Toast.makeText(ApplicationManager.getGlobalContext(), info, Toast.LENGTH_SHORT).show();
//					waitting_layout.setVisibility(View.GONE);
//					Logger.v(TAG, "iniShareManager", " shareAction : SHARE_FAILED");
////					mPopwindow.dismiss();
////					finish();
//					break;
//				case ShareStateListener.SHARE_CANCEL :
//					waitting_layout.setVisibility(View.GONE);
//					Logger.v(TAG, "iniShareManager", " shareAction : SHARE_CANCEL");
////					mPopwindow.dismiss();
////					finish();
//					break;
//				default:
//					break;
//				}
//			}
//			@Override
//			public void onUserInfoComplete(SNSUserInfo user) {}
//		});
		onRefresh();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		careFlag = isGuanzhu;
		if (!isLogin) {
			boolean cared = sModule.isCared(String.valueOf(author_id));
			if (isCared) {
				if (!cared) {
					iv_guanzhu.setBackgroundResource(R.drawable.jiaguanzhu);
				}
			}else {
				if (cared) {
					iv_guanzhu.setBackgroundResource(R.drawable.yiguanzhu);
				}
			}
			isCared = cared;
		}
	}
	
	public static AlbumActivity getInstance(){
		if (activity == null) {
			return null;
		}
		return activity;
	}
	
	public void onRefresh() {
		// 获取数据
		getData(Integer.valueOf(special_id));
	}

	private void initView() {
		waitting_layout = (RelativeLayout) findViewById(R.id.waitting_layout);
		waitting_layout.setVisibility(View.VISIBLE);
		album_tishi = (RelativeLayout) findViewById(R.id.album_tishi);
		album_back = (ImageView) findViewById(R.id.album_back);
		album_photo = (ImageView) findViewById(R.id.album_photo);
		iv_chat_btn = (ImageView) findViewById(R.id.iv_chat_btn);
//		album_name = (TextView) findViewById(R.id.album_name);
		iv_zoom = (ImageView) findViewById(R.id.iv_zoom);
		listView = (PullToZoomListView) findViewById(R.id.list_view);
		
//		mPopwindow = new SharePopwindow(this);
		
		album_back.setOnClickListener(this);
		album_photo.setOnClickListener(this);
		iv_chat_btn.setOnClickListener(this);
		iv_zoom.setOnClickListener(this);
		album_tishi.setOnClickListener(this);
		listView.setAdapter(mAdapter);
		 
		boolean isFirst = PreferenceHelper.getBoolean("albumIsFirstLogin", true);
		if (isFirst) {
			album_tishi.setVisibility(View.VISIBLE);
		}else{
			album_tishi.setVisibility(View.GONE);
		}
		
	}
	Handler mHandler = new Handler();
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_chat_btn:
			if (isLogin) {
				mHandler.post(new Runnable() {
	                @Override
	                public void run() {
	                    int userId = author_id;
	                    final String title = authorName;
	                    if (author_id == UserInfoManager.getUserId()) {
	                    	showShortToast("无法与自己聊天");
							return ;
						}
	                    if (author_id == -1 ) {
							showShortToast("author id is wrong");
							return ;
						}
	                    if (title == null || title.equals("")) {
							showShortToast("author name is wrong");
							return ;
						}
	                    RongIM im = RongIM.getInstance();
	                    if (im == null) {
	                    	RCUtil.connect(UserInfoManager.getUserToken(), new RCConnectCallback() {
								@Override
								public void onSuccess(String userId) {
									if (RongIM.getInstance() != null) {
										RCUtil.setCommunicationInfo(String.valueOf(author_id), authorName, albumIconUrl);
										RongIM.getInstance().startPrivateChat(AlbumActivity.this, String.valueOf(userId), title);
									}
								}
								@Override
								public void onError(String msg, int errorCode) {
									showShortToast("网络连接失败");
								}
							});
						}else{
							RCUtil.setCommunicationInfo(String.valueOf(author_id), authorName, albumIconUrl);
							im.startPrivateChat(AlbumActivity.this, String.valueOf(userId), title);
						}
	                }
				});
			}else{
				showShortToast("请先登录");
			}
			break;
		case R.id.album_back:// 返回键
			onBackPressed();
			break;
		case R.id.album_photo:// 相机键
//			ShareInfo sInfo = new ShareInfo();
//			sInfo.text = albumNames;
//			sInfo.title = albumName;
//			sInfo.titleUrl = GlobalConstants.SHARE_URL+special_id;
//			sInfo.url = sInfo.titleUrl;
//			mPopwindow.show(album_share, sInfo);
			if (isLogin) {
				Intent intent = new Intent(this,UploadActivity.class);
				startActivity(intent);
			}else{
				showShortToast("请先登录");
			}
			break;
		case R.id.iv_zoom:// 专辑封面
			if (System.currentTimeMillis() - curTime > 500) {
				Intent intent2 = new Intent(this, PhotoDetailsActivity.class);
				intent2.putExtra("special_id", special_id);
				intent2.putExtra("special_name", albumName == null ? ""
						: albumName);
				intent2.putExtra("special_names", albumNames == null ? ""
						: albumNames);
				startActivity(intent2);
				curTime = System.currentTimeMillis();
			}
			break;
		case R.id.album_tishi:
			album_tishi.setVisibility(View.GONE);
			PreferenceHelper.putBoolean("albumIsFirstLogin", false);
			break;
		default:
			break;
		}
	}

	class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listSize + 1;
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
			if (position == 0) {
				view = View.inflate(getApplicationContext(),
						R.layout.album_content_view, null);
				add_description = (TextView)view.findViewById(R.id.add_description);
				pro_icon = (ImageView) view.findViewById(R.id.promulgator_icon);
				pro_name = (TextView) view.findViewById(R.id.promulgator_name);
				pro_time = (TextView) view.findViewById(R.id.promulgate_time);
				dZanNum = (TextView) view.findViewById(R.id.album_tv_favorite);
				iv_guanzhu = (ImageView) view.findViewById(R.id.album_guanzhu);
				iv_zan = (ImageView) view.findViewById(R.id.album_favorite);
				album_no_comment = (TextView) view
						.findViewById(R.id.album_no_comment);
				//此处是标签相关控件
//				add_lable = (AddLableView) view
//						.findViewById(R.id.test_addlable);
				iv_rela_first = (ImageView) view
						.findViewById(R.id.iv_rela_album_first);
				iv_rela_second = (ImageView) view
						.findViewById(R.id.iv_rela_album_second);
				iv_rela_third = (ImageView) view
						.findViewById(R.id.iv_rela_album_third);
				album_no_rela_album = (TextView) view
						.findViewById(R.id.album_no_rela_album);
				//此处是标签相关控件
//				myli1 = (DefaultLableView) view
//						.findViewById(R.id.test_lableview);
//				editText1 = (EditText) view.findViewById(R.id.test_et);
//				bt = (ImageView) view.findViewById(R.id.test_bt);
//				editText1.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
//				                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//						imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
//					}
//				});
//				bt.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						nowlabe = editText1.getText().toString();
//						if (nowlabe.length() > 8) {
//							showShortToast("标签不能超过8个字");
//							return;
//						}
//						if (nowlabe == null || nowlabe.equals("")) {
//							showShortToast("标签不能为空");
//							return;
//						}
//						editText1.setText("");
//						if (add_lable.getAllList() == null
//								|| add_lable.getAllList().size() < LABLE_MAX_NUM) {
//							add_lable.addLabel(nowlabe);
//						} else {
//							showShortToast("只能添加十个标签");
//						}
//					}
//				});
				//添加描述文字,使用RichTextView
				if (albumNames == null || albumNames.equals("")) {
					add_description.setText("暂无描述");
				}else{
					add_description.setText(albumNames);
				}
				setTextWithPic(add_description);
				
				pro_icon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (author_id == -1 || author_id == 0) {
//							showShortToast("See author_id is wrong!");
						} else {
							if (System.currentTimeMillis() - curTime > 500) {
								if (author_id == UserInfoManager.getUserId()) {
//									Intent intent = new Intent(AlbumActivity.this,UserActivity.class);
//									startActivity(intent);
								}else{
									Intent intent = new Intent(AlbumActivity.this,
											ProZoneActivity.class);
									intent.putExtra("author_id", author_id);
									startActivity(intent);
								}
								curTime = System.currentTimeMillis();
							}
						}
					}
				});
				if (isLogin) {
					if (isGuanzhu == 1) {
						iv_guanzhu.setBackgroundResource(R.drawable.yiguanzhu);
					} else {
						iv_guanzhu.setBackgroundResource(R.drawable.jiaguanzhu);
					}
					if (isZan == 1) {
						iv_zan.setBackgroundResource(R.drawable.xihuan_select);
					} else {
						iv_zan.setBackgroundResource(R.drawable.xihuan);
					}
				} else {
					if (isCared) {
						iv_guanzhu.setBackgroundResource(R.drawable.yiguanzhu);
					} else {
						iv_guanzhu.setBackgroundResource(R.drawable.jiaguanzhu);
					}
					if (isLiked) {
						iv_zan.setBackgroundResource(R.drawable.xihuan_select);
					} else {
						iv_zan.setBackgroundResource(R.drawable.xihuan);
					}
//					iv_guanzhu.setBackgroundResource(R.drawable.jiaguanzhu);
//					iv_zan.setBackgroundResource(R.drawable.xihuan);
				}
				iv_guanzhu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isLogin) {
							if (author_id != UserInfoManager.getUserId()) {
								if (isGuanzhu == 1) {
									iv_guanzhu
									.setBackgroundResource(R.drawable.jiaguanzhu);
									isGuanzhu = 2;
//								care();
								} else {
									iv_guanzhu
									.setBackgroundResource(R.drawable.yiguanzhu);
									isGuanzhu = 1;
//								care();
								}
							}else{
								showShortToast("无法关注自己");
							}
						} else {
//							Intent intent = new Intent(AlbumActivity.this,
//									LoginActivity.class);
//							startActivity(intent);
							if (isCared) {
								iv_guanzhu
									.setBackgroundResource(R.drawable.jiaguanzhu);
								sModule.removeLocalCare(String.valueOf(author_id));
								isCared = false;
							}else{
								iv_guanzhu
									.setBackgroundResource(R.drawable.yiguanzhu);
								sModule.addLocalCare(String.valueOf(author_id));
								isCared = true;
							}
						}
					}
				});
				iv_zan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isLogin ) {
							if (author_id != UserInfoManager.getUserId()) {
								if (isZan == 1) {
									iv_zan.setBackgroundResource(R.drawable.xihuan);
									isZan = 2;
//								zan();
								} else {
									iv_zan.setBackgroundResource(R.drawable.xihuan_select);
									isZan = 1;
//								zan();
								}
							}else {
								showShortToast("无法给自己点赞");
							}
						} else {
//							Intent intent = new Intent(AlbumActivity.this,
//									LoginActivity.class);
//							startActivity(intent);
							if (isLiked) {
								iv_zan.setBackgroundResource(R.drawable.xihuan);
								sModule.removeLocalLike(special_id);
								isLiked = false;
							}else{
								iv_zan.setBackgroundResource(R.drawable.xihuan_select);
								sModule.addLocalLike(special_id);
								isLiked = true;
							}
						}
					}
				});
				if (albumIconUrl != null && !albumIconUrl.equals("")) {
					ImageLoaderUtil.getInstance(getApplicationContext())
							.displayImage(albumIconUrl, pro_icon,
									ImageLoaderUtil.getRoundedImageOptions());
				}
				if (authorName != null && !authorName.equals("")) {
					pro_name.setText(authorName);
				}
				if (authorTime != null && !authorTime.equals("")) {
					pro_time.setText(GetTimeUtil.getTime(Integer.valueOf(authorTime)));
				}
//				if (zanNum != -1) {
//					dZanNum.setText(String.valueOf(zanNum));
//				}
				if (relaAlbunList != null) {
					int size = relaAlbunList.size();
					if (size > 0) {
						album_no_rela_album.setVisibility(View.GONE);
						for (int i = 0; i < size; i++) {
							if (i == 0) {
								iv_rela_first.setVisibility(View.VISIBLE);
								ImageLoaderUtil.getInstance(
										getApplicationContext()).displayImage(
										relaAlbunList.get(i).getSpecial_pic(),
										iv_rela_first);
								final int id = relaAlbunList.get(i)
										.getSpecial_id();
								iv_rela_first
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
//												Intent intent = new Intent(
//														getApplicationContext(),
//														AlbumActivity.class);
//												intent.putExtra("special_id",
//														id);
//												startActivity(intent);
												getData(id);
												special_id = String.valueOf(id);
											}
										});
							} else if (i == 1) {
								iv_rela_second.setVisibility(View.VISIBLE);
								ImageLoaderUtil.getInstance(
										getApplicationContext()).displayImage(relaAlbunList.get(i).getSpecial_pic(),
										iv_rela_second);
								final int id = relaAlbunList.get(i)
										.getSpecial_id();
								iv_rela_second
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
//												Intent intent = new Intent(
//														getApplicationContext(),
//														AlbumActivity.class);
//												intent.putExtra("special_id",
//														id);
//												startActivity(intent);
												getData(id);
												special_id = String.valueOf(id);
											}
										});
							} else if (i == 2) {
								iv_rela_third.setVisibility(View.VISIBLE);
								ImageLoaderUtil.getInstance(
										getApplicationContext()).displayImage(
										relaAlbunList.get(i).getSpecial_pic(),
										iv_rela_third);
								final int id = relaAlbunList.get(i)
										.getSpecial_id();
								iv_rela_third
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
//												Intent intent = new Intent(
//														getApplicationContext(),
//														AlbumActivity.class);
//												intent.putExtra("special_id",
//														id);
//												startActivity(intent);
												getData(id);
												special_id = String.valueOf(id);
											}
										});
							} else {
								break;
							}
						}
					} else {
						album_no_rela_album.setVisibility(View.VISIBLE);
					}
				} else {
					album_no_rela_album.setVisibility(View.VISIBLE);
				}
				// 初始化标签
//				initLable();
				album_no_comment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						if (rl_album_comment.isShown()) {
//							rl_album_comment.setVisibility(View.GONE);
//						}else{
//							rl_album_comment.setVisibility(View.VISIBLE);
//						}
						Intent intent = new Intent(AlbumActivity.this,CommentActivity.class);
						intent.putExtra("author_id", author_id);
						intent.putExtra("special_id", special_id);
						startActivity(intent);
					}
				});
				if (listSize == 0) {
					album_no_comment.setVisibility(View.VISIBLE);
				} else {
					album_no_comment.setVisibility(View.GONE);
				}
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.album_list_item, null);
				album_list_rl = (RelativeLayout) view.findViewById(R.id.album_list_rl);
				item_iv_icon = (ImageView) view.findViewById(R.id.item_iv_icon);
				item_comment_name = (TextView) view
						.findViewById(R.id.item_comment_name);
				item_tv_comment = (TextView) view
						.findViewById(R.id.item_tv_comment);
				item_tv_album_time = (TextView) view
						.findViewById(R.id.item_tv_album_time);
				item_see_more = (ImageView) view
						.findViewById(R.id.item_see_more);
				album_list_rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AlbumActivity.this,CommentActivity.class);
						intent.putExtra("author_id", author_id);
						intent.putExtra("special_id", special_id);
						startActivity(intent);
					}
				});
				if (position == (listSize)) {
					item_see_more.setVisibility(View.VISIBLE);
					item_see_more.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
//							showShortToast("查看更多评论");
							Intent intent = new Intent(AlbumActivity.this,CommentActivity.class);
							intent.putExtra("author_id", author_id);
							intent.putExtra("special_id", special_id);
							startActivity(intent);
						}
					});
				} else {
					item_see_more.setVisibility(View.GONE);
				}
				int hot = hotCommentsList.size();
				if (hot >= 5) {
					setView(hotCommentsList.get(position - 1).getUser_name(),
							hotCommentsList.get(position - 1)
									.getComments_content(), hotCommentsList
									.get(position - 1).getCtime(),
							hotCommentsList.get(position - 1).getUser_icon());
				} else {
					if (hot >= position) {
						setView(hotCommentsList.get(position - 1)
								.getUser_name(),
								hotCommentsList.get(position - 1)
										.getComments_content(), hotCommentsList
										.get(position - 1).getCtime(),
								hotCommentsList.get(position - 1)
										.getUser_icon());
					} else {
						setView(commentsList.get(position - hot - 1)
								.getUser_name(),
								commentsList.get(position - hot - 1)
										.getComments_content(), commentsList
										.get(position - hot - 1).getCtime(),
								commentsList.get(position - hot - 1)
										.getUser_icon());
					}
				}
			}
			return view;
		}

		private void setView(String name, String comment, int time, String url) {
			item_comment_name.setText(name);
			item_tv_comment.setText(comment);
			item_tv_album_time.setText(GetTimeUtil.getTime(time));
			ImageLoaderUtil.getInstance(getApplicationContext())
					.displayImage(url, item_iv_icon,
							ImageLoaderUtil.getRoundedImageOptions());
		}
	}

//	private void initLable() {
//		if (lableList == null) {
//			lableList = new ArrayList<String>();
//		} else {
//			lableList.clear();
//		}
//		if (tagList == null || tagList.size() == 0) {
//			lableList.add("美女");
//			lableList.add("萌妹子");
//			lableList.add("女神");
//			lableList.add("小清新");
//			lableList.add("文艺范");
//			lableList.add("女汉子");
//			lableList.add("不忍直视");
//		} else {
//			for (int i = 0; i < tagList.size(); i++) {
//				Tag tag2 = tagList.get(i);
//				int tag_id = tag2.getTag_id();
//				String tag_name = tag2.getTag_name();
//				if (tag_id <= 0 || tag_name == null || tag_name.equals("")) {
//					continue;
//				}
//				lableList.add(tag_name);
////				System.out.println("lable :"+ tagList.get(i));
//			}
//		}
//		if (lableList.size() == 0) {
//			lableList.add("美女");
//		}
//		myli1.setLableOnClickListener(new LableOnClickListener() {
//			@Override
//			public void OnClick(String s) {
//				// add_lable.setVisibility(View.VISIBLE);
//				if (s == null || s.equals("")) {
//					showShortToast("标签内容不能为空");
//					return;
//				}
//				if (add_lable.getAllList() == null
//						|| add_lable.getAllList().size() < LABLE_MAX_NUM) {
//					add_lable.addLabel(s);
//				} else {
//					showShortToast("只能添加十个标签");
//				}
//				// add_lable.addLabel(s);
//			}
//		});
//		myli1.setExpandOnClickListener(new ExpandOnClickListener() {
//			@Override
//			public void OnClick(boolean isShow) {
//				if (isShow) {
//					editText1.setVisibility(View.GONE);
//					bt.setVisibility(View.GONE);
//				} else {
//					editText1.setVisibility(View.VISIBLE);
//					bt.setVisibility(View.VISIBLE);
//				}
//			}
//		});
//		if (lableList.size() > DEFAULT_LABLE_NUM) {
//			myli1.addLable(lableList, DEFAULT_LABLE_NUM, false);
//		} else if (lableList.size() > 0
//				&& lableList.size() <= DEFAULT_LABLE_NUM) {
//			myli1.addLable(lableList, DEFAULT_LABLE_NUM, false);
//		}
//	}
	
	private boolean isLiked = false;
	private boolean isCared = false;
	private void getLocalData(){
		isLiked = sModule.isLiked(special_id);
		isCared = sModule.isCared(String.valueOf(author_id));
	}

	private void getData(int id) {
		waitting_layout.setVisibility(View.VISIBLE);
		if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
			NetWorkAPI.getSpecial(getApplicationContext(),
					id, new HttpCallback() {

						@Override
						public void onResult(NetResBean response) {
							waitting_layout.setVisibility(View.GONE);
							GetSpecialResBean data = (GetSpecialResBean) response;
							// System.out.println("data : "+data.toString());
							getAllData(data);
						}

						@Override
						public void onError(String errorMsg) {
							waitting_layout.setVisibility(View.GONE);
							Logger.e(TAG, "net_error", errorMsg);
							Toast.makeText(getApplicationContext(), "网络或数据错误",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onCancel() {
							waitting_layout.setVisibility(View.GONE);
						}
					});
		} else {
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	private void setViewData() {
		// showShortToast("set view");
		ImageLoaderUtil.getInstance(this).displayImage(albumPicUrl, iv_zoom,R.drawable.default_album);
//		System.out.println("iv.zoom : "+albumPicUrl);
//		 System.out.println("author_icon : "+albumIconUrl+"\r\n pro_name : "+authorName+"\r\n protime : "+authorTime);
//		album_name.setText(albumName);
		if (getListSize() >= 5) {
			listSize = 5;
		} else {
			listSize = getListSize();
		}
		mAdapter.notifyDataSetChanged();
	}

	private int getListSize() {
		int hotCommentSize = hotCommentsList == null ? 0 : hotCommentsList
				.size();
		int commentSize = commentsList == null ? 0 : commentsList.size();
		int allComments = hotCommentSize + commentSize;
		// System.out.println("hot = "+hotCommentSize
		// +"\r\n com = "+commentSize+"\r\n all = "+allComments);
		return allComments;
	}

	private String albumName;// 专辑名称
	private String albumNames;// 分享语句
	private String albumPicUrl;// 专辑封面
	private String albumIconUrl;// 作者头像
	private String authorName;// 上传专辑作者
	private String authorTime;// 上传专辑时间
	private int careNum;// 关注数量
	private int zanNum = -1;// 点赞数量
	private int commentNum;// 评论数量
	private int isGuanzhu = -1;// 是否关注
	private int isZan = -1;// 是否点赞
	private int author_id = -1;// 作者id
	private List<Comment> hotCommentsList;
	private List<Comment> commentsList = new ArrayList<Comment>();
	private List<Special> relaAlbunList;
//	private List<Tag> tagList;

	private void getAllData(GetSpecialResBean data) {
		if (data != null) {
			isGuanzhu = data.getIsfavorite();
			careFlag = isGuanzhu;
			isZan = data.getIszan();
			zanFlag = isZan;
//			commentsList = data.getComment();
			hotCommentsList = data.getComment_hot();
			relaAlbunList = data.getList();
		} else {
			// System.out.println("data is null");
			return;
		}
		if (data.getSpecial() != null) {
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
//			tagList = data.getSpecial().getTag();
		} else {
			// System.out.println("data.getSpecial is null");
			return;
		}
		if (!isLogin) {
			getLocalData();
		}
		setViewData();
	}

//	private void sendTagToServer(String tag_add) {
//		if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
//			NetWorkAPI.addTag(mContext, Integer.valueOf(special_id), tag_add,
//					new HttpCallback() {
//
//						@Override
//						public void onResult(NetResBean response) {
//							boolean success = response.success;
//							if (success) {
//								// showShortToast("哎呦我擦");
//							} else {
//								// showShortToast("哎呦我去");
//							}
//						}
//						@Override
//						public void onError(String errorMsg) {
//							showShortToast("error : " + errorMsg);
//						}
//						@Override
//						public void onCancel() {}
//					});
//		}else {
////			waitting_layout.setVisibility(View.GONE);
//		}
//	}

	private void zan() {
		if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
			NetWorkAPI.zan(getApplicationContext(), Integer.valueOf(special_id),
				isZan, new HttpCallback() {

					@Override
					public void onResult(NetResBean response) {
						if (response.success && isZan == 1) {
//							showShortToast("已赞");
							// if(zanNum != -1){
							// dZanNum.setText(String.valueOf(zanNum));
							// }
						} else if (response.success && isZan != 1) {
//							showShortToast("已取消赞");
						} else {
							showShortToast("点赞操作失败");
						}
					}

					@Override
					public void onError(String errorMsg) {}

					@Override
					public void onCancel() {}
				});
		}else {
//			waitting_layout.setVisibility(View.GONE);
		}
	}

	private void care() {
		if (author_id > 0) {
			if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
				NetWorkAPI.careauthor(getApplicationContext(), author_id, isGuanzhu,
					new HttpCallback() {

						@Override
						public void onResult(NetResBean response) {
							if (response.success && isGuanzhu == 1) {
//								showShortToast("已关注");
							} else if (response.success && isGuanzhu != 1) {
//								showShortToast("已取消关注");
							} else {
								showShortToast("关注操作失败");
							}
						}

						@Override
						public void onError(String errorMsg) {}

						@Override
						public void onCancel() {}
					});
			}else {
//				waitting_layout.setVisibility(View.GONE);
			}
		} else {
//			showShortToast("author id wrong");
		}
	}

	@Override
	protected void onPause() {
//		if (add_lable != null) {
//			List<String> allList = add_lable.getAllList();
//			if (allList != null && allList.size() > 0) {
//				StringBuilder sb = new StringBuilder();
//				for (int i = 0; i < allList.size(); i++) {
//					sb.append(allList.get(i));
//					if (i < allList.size() - 1)
//						sb.append(",");
//				}
//				// System.out.println(sb.toString());
//				sendTagToServer(sb.toString());
//			}
//		}
		if (isLogin) {
	//		System.out.println("careflag = "+careFlag + " is care = "+isCare);
			if (careFlag != -1 && isGuanzhu != -1) {
				if (careFlag != isGuanzhu) {
					if (careFlag == 0 && isGuanzhu == 2) {
					}else{
						care();
					}
				}
			}
	//		System.out.println("zanFlag = "+zanFlag + " isZan = "+isZan);
			if (zanFlag != -1 && isZan != -1) {
				if (zanFlag != isZan) {
					if (zanFlag == 0 && isZan == 2) {
					}else{
						zan();
					}
				}
			}
		}
//		if (commentList != null && commentList.size()>0) {
//			comment();
//			commentList.clear();
//		}
		super.onPause();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
//		showShortToast("on new intent");
		special_id = intent.getStringExtra("special_id");
		waitting_layout.setVisibility(View.VISIBLE);
		getData(Integer.valueOf(special_id));
		super.onNewIntent(intent);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0 && data != null) 
        	switch (requestCode) {
			case ShareConstants.AMAYA_ACTIVITY_RESULT_SINAWEIBO:
			case ShareConstants.AMAYA_ACTIVITY_RESULT_QQ:
			case ShareConstants.AMAYA_ACTIVITY_RESULT_TXWEIBO:
				SNSLoginManager.onActivityResult(getApplicationContext(), requestCode, resultCode, data);
				break;
			default:
				break;
			}
    }
	
	private void setTextWithPic(TextView tv){
		DynamicDrawableSpan drawableSpan = new DynamicDrawableSpan(
				DynamicDrawableSpan.ALIGN_BOTTOM) {
			@Override
			public Drawable getDrawable() {
				Drawable d = getResources().getDrawable(R.drawable.miaoshu);
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				return d;
			}
		};
        //需要处理的文本，[smile]是需要被替代的文本  
        SpannableString spannable = new SpannableString("[smile]   "+tv.getText().toString());  
        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）  
       //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12  
        spannable.setSpan(drawableSpan, 0,"[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);    
        tv.setText(spannable); 
	}
}
