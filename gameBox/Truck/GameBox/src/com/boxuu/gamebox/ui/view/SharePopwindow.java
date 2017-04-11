package com.boxuu.gamebox.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.boxuu.gamebox.R;
import com.boxuu.gamebox.sns.SNSShareManager;
import com.boxuu.gamebox.sns.bean.ShareInfo;

public class SharePopwindow implements OnClickListener {

	private Activity activity;

	private PopupWindow popuWindow;

	private View wechat;
	private View friend;
	private View Qzone;
	private View QQ;
	private View cancel_btn;

	private ShareInfo shareInfo;

	private View shadowView;
	
	public SharePopwindow(Activity activity,View shadowView) {
		this.activity = activity;
		this.shadowView = shadowView;
		initView();
	}

	private void initView() {
		Context context = activity.getApplicationContext();
		View shareView = View.inflate(context, R.layout.sharepop, null);
		wechat = shareView.findViewById(R.id.share_wchat);
		friend = shareView.findViewById(R.id.share_wchat_friend);
		Qzone = shareView.findViewById(R.id.share_qqzone);
		QQ = shareView.findViewById(R.id.share_qq);
		cancel_btn = shareView.findViewById(R.id.cancel_btn);

		wechat.setOnClickListener(this);
		friend.setOnClickListener(this);
		Qzone.setOnClickListener(this);
		QQ.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);

		popuWindow = new PopupWindow(shareView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popuWindow.setAnimationStyle(R.style.anim_share_select);
		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		ColorDrawable cd = new ColorDrawable(-0000);
		popuWindow.setBackgroundDrawable(cd);

		popuWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				if(shadowView!=null){
					shadowView.setVisibility(View.GONE);
				}
			}
		});
		
	}

	/**
	 * 弹出popwindow
	 * 
	 * @param view
	 */
	public void show(View view, ShareInfo shareInfo) {
		this.shareInfo = shareInfo;
		popuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		if(shadowView!=null){
			shadowView.setVisibility(View.VISIBLE);
		}
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

		case R.id.share_wchat:
			shareInfo.setSNSType(SNSShareManager.TENCENT_WEIXIN_TYPE);
			break;
		case R.id.share_wchat_friend:
			shareInfo.setSNSType(SNSShareManager.TENCENT_WEIXIN_FRIEND_TYPE);
			break;
		case R.id.share_qqzone:
			shareInfo.setSNSType(SNSShareManager.TENCENT_QZONE_TYPE);
			break;
		case R.id.share_qq:
			shareInfo.setSNSType(SNSShareManager.TENCENT_QQ_TYPE);
			break;
		case R.id.cancel_btn:
			dismiss();
			break;

		default:
			break;
		}

		SNSShareManager.getInstance().shareMessage(activity, shareInfo);
	}
	
	public void clearViews(){
		shadowView = null;
	}

}
