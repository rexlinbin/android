package com.bccv.threedimensionalworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.BaseActivity;

public class MusicSelectActivity extends BaseActivity {
	private Button localBtn, musicBtn;
	private Button localBtn1, musicBtn1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musicselect);

		localBtn = (Button) findViewById(R.id.musicselect_local);
		musicBtn = (Button) findViewById(R.id.musicselect_music);
		localBtn1 = (Button) findViewById(R.id.musicselect_local1);
		musicBtn1 = (Button) findViewById(R.id.musicselect_music1);

		localBtn1.setOnFocusChangeListener(new OnChangeListener());
		localBtn.setOnFocusChangeListener(new OnChangeListener());

		musicBtn.setOnFocusChangeListener(new OnMusicChangeListener());
		musicBtn1.setOnFocusChangeListener(new OnMusicChangeListener());
	}

	class OnChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				LayoutParams params = (LayoutParams) localBtn
						.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				localBtn.setLayoutParams(params);	
				
				localBtn1.setSelected(true);

				localBtn.setSelected(true);

			} else {
				localBtn1.setSelected(false);

				localBtn.setSelected(false);
				LayoutParams params = (LayoutParams) localBtn
						.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				localBtn.setLayoutParams(params);	
				
			}

		}
	}

	class OnMusicChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				LayoutParams params = (LayoutParams) musicBtn
						.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				musicBtn.setLayoutParams(params);	
				
				
				musicBtn.setSelected(true);

				musicBtn1.setSelected(true);

			} else {
				musicBtn.setSelected(false);

				musicBtn1.setSelected(false);

				LayoutParams params = (LayoutParams) musicBtn
						.getLayoutParams();
				params.leftMargin = params.leftMargin -1;
				musicBtn.setLayoutParams(params);	
				
			}

		}

	}

	public void onLocalClick(View view) {
		
		Intent aIntent =new Intent(MusicSelectActivity.this,LocalMusicActivity.class);
		aIntent.putExtra("isLocal", false);
	
		
		startActivity(aIntent);
		
	
	}

	public void onMusicClick(View view) {

		Intent aIntent = new Intent(MusicSelectActivity.this,
				MusicActivity.class);

		startActivity(aIntent);
	

	}

}
