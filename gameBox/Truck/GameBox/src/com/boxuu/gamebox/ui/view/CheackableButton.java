package com.boxuu.gamebox.ui.view;

import com.boxuu.gamebox.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Checkable;

public class CheackableButton extends Button implements Checkable {

	private static final int TEXT_CHECKED_COLOR = 0;
	private static final int TEXT_UNCHECK_COLOR = 1;
	
	private boolean isChecked = false;
	private int[] colors = new int[2];
	
	public CheackableButton(Context context) {
		super(context);
		initData();
	}

	public CheackableButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
	}

	public CheackableButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData();
	}

	private void initData(){
		colors[TEXT_CHECKED_COLOR] = getResources().getColor(R.color.tap_text_checked);
		colors[TEXT_UNCHECK_COLOR] = getResources().getColor(R.color.tap_text_uncheck);
	}
	
	@Override
	public void setChecked(boolean checked) {

		if (isChecked != checked) {
			isChecked = checked;
			if (isChecked) {
				setTextColor(colors[TEXT_CHECKED_COLOR]);
				setBackgroundResource(R.drawable.navbg_selected);
			} else {
				setTextColor(colors[TEXT_UNCHECK_COLOR]);
				setBackgroundResource(R.drawable.navbg);
			}
		}
	}

	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void toggle() {}

}
