package com.bccv.meitu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.bccv.meitu.R;

public class BindButton extends Button {

	private boolean isChecked = false;
	
	public BindButton(Context context) {
		super(context);
	}
	public BindButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public BindButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public boolean isChecked(){
		return isChecked;
	}
	
	public void setChecked(boolean checked){
		isChecked = checked;
		if(checked){
			this.setBackgroundResource(R.drawable.my_infro_btn_bound);
		}else{
			this.setBackgroundResource(R.drawable.my_infro_btn_bound_un);
			
		}
	}

}
