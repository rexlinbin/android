package com.bccv.meitu.view;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.meitu.R;

public class DefaultLableView extends LinearLayout {
	private Context context;
	private LinearLayout layout;
	private HashMap<String, View> hshView = new HashMap<String, View>();
	private RelativeLayout localView;
	private LableOnClickListener clickListener;
	private ExpandOnClickListener expandListener;
	
	private List<String> lableList;
	private int defaultShowNum;
	
	public DefaultLableView(Context context) {
		super(context);
		this.context = context;
		Init();
	}

	@SuppressLint("NewApi")
	public DefaultLableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		Init();
	}

	public DefaultLableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		Init();
	}

	private void Init() {
//		getWindowWH();
		layout = new PredicateLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);

		addView(layout);
	}
	
	public void setLableOnClickListener(LableOnClickListener onClickListener){
		this.clickListener = onClickListener;
	}
	public void setExpandOnClickListener(ExpandOnClickListener expandListener){
		this.expandListener = expandListener;
	}

	/**
	 * 添加标签list
	 * @param lableList
	 */
	public void addLable(List<String> lableList,int defaultShowNum,boolean hasBtn){
		if(lableList==null||lableList.size()==0){
			return;
		}
		this.defaultShowNum = defaultShowNum;
		this.lableList = lableList;
		addLable(defaultShowNum, hasBtn);
	}
	
	private void addLable(int showNum,boolean hasBtn){
		layout.removeAllViews();
		for (int i = 0; i < lableList.size() && i < showNum ; i++) {
			addLabel(lableList.get(i));
		}
		localView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(
				R.layout.default_label_view, null);
		ImageView imageView = (ImageView) localView.findViewById(R.id.iv_switch);
//		if(hasBtn){
//			if(hasBtn && (showNum<lableList.size() || defaultShowNum >= lableList.size())){
//				addBtn(false);
//			}else{
//				addBtn(true);
//			}
//		}else{
//			imageView.setVisibility(View.INVISIBLE);
//		}
		addBtn(hasBtn);
	}
	
	
	// 代码添加textview,根据传递进来值添加TextView 
	private void addLabel(final String s) {
		localView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(
				R.layout.default_label_view, null);
		TextView textView = (TextView) localView.findViewById(R.id.labe_text);
		textView.setText(s);
		textView.setTag(s);
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(DefaultLableView.this.clickListener!=null){
					if(s == null || s.equals("")){
						clickListener.OnClick("");
					}else 
						clickListener.OnClick(v.getTag().toString());
				}
			}
		});
		hshView.put(s, localView);
		layout.addView(localView);
	}

	private void addBtn(boolean isOpen){
		
		//TODO 添加btn
		
		localView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(
				R.layout.default_label_view, null);
		TextView textView = (TextView)localView.findViewById(R.id.labe_text);
		ImageView imageView = (ImageView) localView.findViewById(R.id.iv_switch);
		textView.setVisibility(View.GONE);
		imageView.setVisibility(View.VISIBLE);
		//TODO 展示不同btn
		if(!isOpen){
			imageView.setBackgroundResource(R.drawable.lable_switch_selector);
		}else{
			imageView.setBackgroundResource(R.drawable.lable_shouhui_selector);
		}
		
		imageView.setTag(isOpen);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isopen = (Boolean) v.getTag(); 
				if(DefaultLableView.this.expandListener!=null){
					expandListener.OnClick(isopen);
				}
//				if(isopen){//点击之前是打开状态
//					addLable(defaultShowNum, true);
//				}else{//点击之前是关闭状态
//					addLable(lableList.size(), true);
//				}
				if(!isopen){
					addLable(lableList.size(), true);
				}else{
					if(defaultShowNum<lableList.size()){
						addLable(defaultShowNum, false);
					}else{
						addLable(lableList.size(), false);
					}
				}
			}
		});
		layout.addView(localView);
	}
	
	// 代码添加textview,根据传递进来值添加TextView
	public void deleteLabel(String str) {
		layout.removeView(hshView.get(str));
		hshView.remove(str);
	}

	public interface LableOnClickListener {
		abstract void OnClick(String s);
	}
	public interface ExpandOnClickListener {
		abstract void OnClick(boolean isShow);
	}

}
