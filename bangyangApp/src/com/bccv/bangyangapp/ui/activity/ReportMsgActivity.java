package com.bccv.bangyangapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.ui.view.BackGroundView;
import com.bccv.bangyangapp.utils.L;
import com.bccv.bangyangapp.utils.SystemUtil;

public class ReportMsgActivity extends BaseActivity {

	public static final String REPORT_MSG_CATE = "report_Msg_cate";

	public static final int FEEDBACK_CATE = 1;
	public static final int QUESTION_CATE = 2;

	private BackGroundView background_view;
	private Button common_edit_cancel, common_edit_confirm;
	private TextView common_edit_title_tv, feedback_content_num;
	private EditText feedback_content_edit;

	private int[] background;

	private int cate = FEEDBACK_CATE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		cate = intent.getIntExtra(REPORT_MSG_CATE, FEEDBACK_CATE);
		initView();
	}

	private void initView() {
		background_view = (BackGroundView) findViewById(R.id.background_view);
		common_edit_cancel = (Button) findViewById(R.id.common_edit_cancel);
		common_edit_confirm = (Button) findViewById(R.id.common_edit_confirm);
		common_edit_title_tv = (TextView) findViewById(R.id.common_edit_title_tv);
		feedback_content_num = (TextView) findViewById(R.id.feedback_content_num);
		feedback_content_edit = (EditText) findViewById(R.id.feedback_content_edit);

		background_view.setGradient(background[0], background[1]);

		if (cate == FEEDBACK_CATE) {
			common_edit_title_tv.setText("意见反馈");
		} else if (cate == QUESTION_CATE) {
			common_edit_title_tv.setText("发布疑问");
			feedback_content_edit.setHint("在这里输入你的疑问...");
		}

		feedback_content_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String content = s.toString();

				if (content.length() > 120) {
					feedback_content_edit.setText(content.substring(0, 120));
					feedback_content_edit.setSelection(120);
					return;
				}

				feedback_content_num.setText("还可以输入" + (120 - content.length())
						+ "字");
			}
		});

		common_edit_cancel.setOnClickListener(this);
		common_edit_confirm.setOnClickListener(this);

	}
	
	public void closedSoftInput(){
		/* 隐藏软键盘 */
		InputMethodManager imm1 = (InputMethodManager) feedback_content_edit
				.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm1.isActive()) {
			imm1.hideSoftInputFromWindow(
					feedback_content_edit.getApplicationWindowToken(), 0);
		}
	}

	@Override
	public void finish() {
		closedSoftInput();
		super.finish();
		if (cate == QUESTION_CATE) {
			overridePendingTransition(R.anim.in_from_none, R.anim.out_to_bottom);
		}
	}
	
	
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.common_edit_cancel:

//			closedSoftInput();

			finish();
			break;
		case R.id.common_edit_confirm:
			if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {
				
				String msg = feedback_content_edit.getText().toString();
				
				if(TextUtils.isEmpty(msg)){
					showShortToast("内容不能为空");
					return;
				}
				
				closedSoftInput();
				// TODO
				if (cate == FEEDBACK_CATE) {
					// 意见反馈
					common_edit_confirm.setClickable(false);
					NetWorkAPI.feedback(mContext, msg, new MyHttpCallBack());
				} else if (cate == QUESTION_CATE) {
					// 发布疑问 
					common_edit_confirm.setClickable(false);
					NetWorkAPI.question(mContext, msg, new MyHttpCallBack());
					
				}
				
			}

			break;

		default:
			break;
		}

	}

	private class MyHttpCallBack implements HttpCallback{

		@Override
		public void onResult(NetResBean response) {
			L.i(TAG, "onResult", response.toString());
			if(response.success){
				showShortToast("提交成功");
				finish();
			}else{
				showShortToast("提交失败,请重试");
			}
			common_edit_confirm.setClickable(true);
		}

		@Override
		public void onCancel() {
			common_edit_confirm.setClickable(true);
		}

		@Override
		public void onError(String errorMsg) {
			L.e(TAG, "onError", errorMsg+"");
			showShortToast("提交失败,请重试");
			common_edit_confirm.setClickable(true);
		}
		
	}
	
}
