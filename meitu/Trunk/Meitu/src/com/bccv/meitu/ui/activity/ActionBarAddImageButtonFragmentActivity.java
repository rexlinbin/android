package com.bccv.meitu.ui.activity;

import io.rong.imkit.view.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.bccv.meitu.R;

/**
 * RongYun Conversation list activity.
 */
public class ActionBarAddImageButtonFragmentActivity extends FragmentActivity {

    ActionBar mActionBar;
    ImageButton imageButton;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getIntent().setData(Uri.parse("rong://com.example.actionbar").buildUpon().appendPath("conversationlist").appendPath("private")
                .appendQueryParameter("targetId","user1").build());
        setContentView(R.layout.activity_actionbar_addimage_fragment);
        mActionBar = (ActionBar)findViewById(android.R.id.custom);
        mActionBar.getTitleTextView().setText("会话列表");
        mActionBar.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        LayoutInflater inflater = LayoutInflater.from(this);
//        imageButton = (ImageButton)inflater.inflate(R.layout.rc_imagebutton_selector,mActionBar,false);
//        
//        mActionBar.addView(imageButton);
//        
//        imageButton.setOnClickListener(this);
    }
}
