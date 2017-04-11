package com.bccv.threedimensionalworld.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.activity.AboutAcitvity;
import com.bccv.threedimensionalworld.activity.BlueQuXiaoActivity;
import com.bccv.threedimensionalworld.activity.SettingActivity;
import com.bccv.threedimensionalworld.model.BlueToothBeaN;

public class BlueToothAdapter extends BaseAdapter {

	private List<BlueToothBeaN> data;
	private Context context;

	public BlueToothAdapter(List<BlueToothBeaN> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub

		final BlueToothBeaN item = data.get(arg0);

		View view = LayoutInflater.from(context).inflate(
				R.layout.item_buletooth, null);

		ImageView im = (ImageView) view.findViewById(R.id.buleTooth_image);

		TextView text = (TextView) view.findViewById(R.id.buleTooth_name);
		TextView stateText = (TextView) view.findViewById(R.id.buleTooth_state);
		Button btn = (Button) view.findViewById(R.id.buleTooth_Btn);

//		if (item.getType() == 0) {
//
//			im.setBackgroundResource(R.drawable.bluethooth_shoubing);
//		}
//		if (item.getType() == 1) {
//
//			im.setBackgroundResource(R.drawable.bluethooth_tv);
//		}
//		if (item.getType() == 2) {
//
//			im.setBackgroundResource(R.drawable.bluethooth_phone);
//		}

		text.setText(item.getName());

		if (item.isSiri()) {

			btn.setVisibility(View.VISIBLE);
			stateText.setText("已配对");
		} else {

			btn.setVisibility(View.INVISIBLE);
			stateText.setText("未配对");
		}

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(context, BlueQuXiaoActivity.class);

				intent.putExtra("adress", item.getAdress());

				context.startActivity(intent);

			}
		});

		return view;

	}

}
