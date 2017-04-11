package com.bccv.meitu.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.bccv.meitu.R;
import com.bccv.meitu.sns.SNSShareManager;
import com.bccv.meitu.sns.bean.ShareInfo;

public class SharePopwindow implements OnClickListener {

	private Activity activity;
	
	private PopupWindow popuWindow;
	
	private View wechat;
	private View friend;
	private View sina;
	private View Qzone;
	private View QQ;
	private View share_view;
	
	private ShareInfo shareInfo;
	
	public SharePopwindow(Activity activity){
		this.activity = activity;
		initView();
	}
	
	private void initView(){
		Context context = activity.getApplicationContext();
		View shareView = View.inflate(context, R.layout.sharepop, null);
		wechat = shareView.findViewById(R.id.share_weixin);
		friend = shareView.findViewById(R.id.share_winxinquan);
		sina = shareView.findViewById(R.id.share_sina);
		Qzone = shareView.findViewById(R.id.share_QQspace);
		QQ = shareView.findViewById(R.id.share_QQ);
		share_view = shareView.findViewById(R.id.share_view);
		
		wechat.setOnClickListener(this);
		friend.setOnClickListener(this);
		sina.setOnClickListener(this);
		Qzone.setOnClickListener(this);
		QQ.setOnClickListener(this);
		share_view.setOnClickListener(this);
		
		popuWindow = new PopupWindow(shareView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		ColorDrawable cd = new ColorDrawable(-0000);
		popuWindow.setBackgroundDrawable(cd);
		
	}
	
	/**
	 * 弹出popwindow
	 * 
	 * @param view
	 */
	public void show(View view,ShareInfo shareInfo) {
		this.shareInfo = shareInfo;
		popuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}
	
	/**
	 * 隐藏popwindow
	 */
	public void dismiss() {
		if (popuWindow != null && popuWindow.isShowing()) {
			popuWindow.dismiss();
		}
	}
	
	public boolean isShowing() {
		if (popuWindow != null) {
			return popuWindow.isShowing();
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.share_weixin:
			shareInfo.setSNSType(SNSShareManager.TENCENT_WEIXIN_TYPE);
			break;
		case R.id.share_winxinquan:
			shareInfo.setSNSType(SNSShareManager.TENCENT_WEIXIN_FRIEND_TYPE);
			break;
		case R.id.share_sina:
			shareInfo.setSNSType(SNSShareManager.SINA_WEIBO_TYPE);
			break;
		case R.id.share_QQspace:
			shareInfo.setSNSType(SNSShareManager.TENCENT_QZONE_TYPE);
			break;
		case R.id.share_QQ:
			shareInfo.setSNSType(SNSShareManager.TENCENT_QQ_TYPE);
			break;
		case R.id.share_view:
			dismiss();
			break;

		default:
			break;
		}
		
		SNSShareManager.getInstance().shareMessage(activity, shareInfo);
	}
	
}
