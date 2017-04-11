package com.bccv.meitu.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.meitu.R;

public class AddLableView extends LinearLayout {
	private Context context;
	private LinearLayout layout;
	private int nowWorld = 0;
	// 删除的汉字
	private String deteleWorld;
//	private int screenWidth;
//	private int linelength;
	private HashMap<Integer, View> hshView = new HashMap<Integer, View>();
	private HashMap<Integer, String> hshStr = new HashMap<Integer, String>();
	private RelativeLayout localView;
	private OnDeleteListener deleteListener;

	public AddLableView(Context context) {
		super(context);
		this.context = context;
		Init();
	}

	@SuppressLint("NewApi")
	public AddLableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		Init();
	}

	public AddLableView(Context context, AttributeSet attrs) {
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
	private List<String> allLable = new ArrayList<String>();
	// 代码添加textview,根据传递进来值添加TextView 
	public void addLabel(final String s) {
		this.setVisibility(View.VISIBLE);
		if(allLable != null && allLable.size()>0){
			for (int i = 0; i < allLable.size(); i++) {
				if(allLable.get(i).equals(s)){
					return ;
				}
			}
		}
		allLable.add(s);
		localView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(
				R.layout.label_view, null);
		TextView textView = (TextView) localView.findViewById(R.id.labe_text);
		textView.setText(s);
		textView.setTag(nowWorld);
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteLabel(Integer.parseInt(v.getTag().toString()));
			}
		});
		hshView.put(nowWorld, localView);
		hshStr.put(nowWorld, s);
		layout.addView(localView);
		nowWorld++;

	}

	// 代码添加textview,根据传递进来值添加TextView
	public void deleteLabel(int i) {
		deteleWorld = hshStr.get(i);
		if(allLable != null && allLable.size() > 0){
			int k = -1 ;
			for (int j = 0; j < allLable.size(); j++) {
				if(allLable.get(j).equals(deteleWorld)){
					k = j;
				}
			}
			if(k != -1){
				allLable.remove(k);
			}
		}
		layout.removeView(hshView.get(i));
		if(deleteListener!=null){
			deleteListener.OnChanged(deteleWorld);
		}
		hshView.remove(i);
		hshStr.remove(i);
		if(hshStr.size() == 0){
			this.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置监听器,当状态修改的时候调用
	 * @param listener
	 */
	public void SetOnDeleteListener(OnDeleteListener listener) {
		deleteListener = listener;
	}

	public interface OnDeleteListener {
		abstract void OnChanged(String s);
	}
	
	public List<String> getAllList(){
		if(allLable == null || allLable.size() == 0){
			return null;
		}
		return allLable;
	}

//	// 判断当前行输入textview是否超过宽度
//	public boolean isNewline(TextView t) {
//		TextPaint paint = t.getPaint();
//		int len = (int) paint.measureText(t.getText().toString());
//		if (len < 100) {
//			len = 100;
//		}
//		// 标签背景默认有一定距离,一行第一个标签执行
//		if (linelength == 0) {
//			linelength += screenWidth * 0.06;
//		}
//		// 每添加一个标签要加上标签背景的距离
//		linelength += len + screenWidth * 0.043;
//		// 如果行数大于宽度，必须换行
//		if (linelength > screenWidth) {
//			linelength = 0;
//			return true;
//		}
//		return false;
//	}
//
//	// 获取屏幕宽高
//	private void getWindowWH() {
//		DisplayMetrics dm = new DisplayMetrics();
//		// 获取屏幕信息
//		((Activity) context).getWindowManager().getDefaultDisplay()
//				.getMetrics(dm);
//
//		screenWidth = dm.widthPixels;
//	}

}
