package com.bccv.threedimensionalworld.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.StringUtils;
import com.bccv.threedimensionalworld.view.MyKeyBoard;
import com.bccv.threedimensionalworld.view.MyKeyBoard.KeyBoardListener;

@SuppressLint("NewApi")
public class MovieSearchInputActivity extends BaseActivity {
	
	private MyKeyBoard leftMyKeyBoard, rightMyKeyBoard;
	
	private TextView leftSearchButton, rightSearchButton, leftinfoTextView, rightinfoTextView;
	
	private String searchTextString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moviesearchinput);
		initBack();
		
		leftinfoTextView = (TextView) findViewById(R.id.left_searchinfo_textView);
		rightinfoTextView = (TextView) findViewById(R.id.right_searchinfo_textView);
		leftinfoTextView.setScaleX(0.5f);
		rightinfoTextView.setScaleX(0.5f);
		
		
		leftSearchButton = (TextView) findViewById(R.id.left_search_button);
		rightSearchButton = (TextView) findViewById(R.id.right_search_button);
		
		leftSearchButton.setScaleX(0.5f);
		rightSearchButton.setScaleX(0.5f);
		
		
		leftMyKeyBoard = (MyKeyBoard) findViewById(R.id.left_myKeyBoard);
		rightMyKeyBoard = (MyKeyBoard) findViewById(R.id.right_myKeyBoard);
		leftMyKeyBoard.setKeyBoardListener(new KeyBoardListener() {
			
			@Override
			public void onKey(String text) {
				// TODO Auto-generated method stub
				searchTextString = text;
				leftSearchButton.setText(text);
				rightSearchButton.setText(text);
			}

			@Override
			public void hideKeyBoard() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSure() {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(searchTextString)) {
					Intent intent = new Intent(getApplicationContext(), MovieNoSearchInputListActivity.class);
					intent.putExtra("name", searchTextString);
					startActivity(intent);
					finish();
				}
				
			}
		});
		leftMyKeyBoard.setRightButtons(rightMyKeyBoard.getRightInputKeyButtons(), rightMyKeyBoard.getRightFunctionKeyButtons());
		leftMyKeyBoard.setInputKeyListener();
		leftMyKeyBoard.setFunctionKeyButtons();
	}
	
	private void initBack() {
		Button leftbackButton = (Button) findViewById(R.id.left_back_button);
		final Button rightbackButton = (Button) findViewById(R.id.right_back_button);
		leftbackButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightbackButton.setSelected(hasFocus);
			}
		});
		
		
		leftbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		rightbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
