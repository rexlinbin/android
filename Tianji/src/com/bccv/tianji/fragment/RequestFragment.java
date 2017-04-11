package com.bccv.tianji.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bccv.tianji.R;
import com.bccv.tianji.api.GameApi;
import com.bccv.tianji.model.Gameinfo;

@SuppressLint("NewApi")
public class RequestFragment extends Fragment {

	Button text;

	String requst;
	Gameinfo data;
	private String GameID;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_request, null);

		text = (Button) rootView.findViewById(R.id.requst_text);
		// Log.e("介绍数据", getRequst());
		
		GameID=getGameID();
		new IntroductionTask().execute();
		return rootView;
	}

	public String getRequst() {
		return requst;
	}

	public void setRequst(String requst) {
		this.requst = requst;
	}

	public String getGameID() {
		return GameID;
	}

	public void setGameID(String gameID) {
		GameID = gameID;
	}

	class IntroductionTask extends AsyncTask<String, Void, Gameinfo> {

		@Override
		protected Gameinfo doInBackground(String... params) {

			GameApi gameApi = new GameApi();

			data = gameApi.getGameInfoList("", GameID);

			return data;

		}

		@Override
		protected void onPostExecute(Gameinfo list) {
			super.onPostExecute(list);
			requst = list.getSystem_demand();
			text.setText(requst);
			try {
				if(requst==null){
					
					
					text.setText("没有硬件需求");		
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				text.setText("没有硬件需求");	
			}
		
			
		}

	}
}
