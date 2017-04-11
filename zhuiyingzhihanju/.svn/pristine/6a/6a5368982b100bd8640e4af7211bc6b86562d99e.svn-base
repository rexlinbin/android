package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.CommentAdapter;
import com.bccv.zhuiyingzhihanju.api.MSGApi;
import com.bccv.zhuiyingzhihanju.model.Msg;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseFragment;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CommentFragment extends BaseFragment {

	View view;
ListView listView;
CommentAdapter adapter;
private List<Msg> data;
private List<Msg> list;

boolean isDelect;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_comment, container, false);

			listView=(ListView) view.findViewById(R.id.comment_listView);
			
			
			data = new ArrayList<Msg>();
			

			adapter = new CommentAdapter(getActivity(), data);
			listView.setAdapter(adapter);
			listView.setSelector(
					new ColorDrawable(android.R.color.transparent));
			
			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					DeleteDialog(position);
					return true;
				}
			});
			
			
			
			getData();
			
			
		}
		return view;

	}



	private void getData() {
		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (list != null) {
					data.addAll(list);

					adapter.notifyDataSetChanged();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {

				MSGApi api = new MSGApi();
//Log.e("userID", GlobalParams.user.getUid());
				list = api.getUserMSgList(GlobalParams.user.getUid(), "1", 10 + "",GlobalParams.user.getToken());

				return "true";
			}
		}.execute("");

	}
	

	/**
	 * 长按删除
	 * 
	 * @param position
	 */
	private void DeleteDialog(final int position) {
		AlertDialog.Builder builder = new Builder(getActivity());

		builder.setMessage("确定删除文件？");
		builder.setTitle("提示");

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				
				DelectDate(position);
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}

	private void DelectDate(final int position) {

		// TODO Auto-generated method stub

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("true")) {
					data.remove(position);

					adapter.notifyDataSetChanged();
					Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {

				MSGApi api = new MSGApi();
				try {

					isDelect = api.sendDelect(GlobalParams.user.getUid(), list.get(position).getId(),list.get(position).getTyp_id(),GlobalParams.user.getToken());

					if (isDelect) {
						return "true";
					}
					return "false";

				} catch (Exception e) {
					// TODO: handle exception
				}
				return null;

			}
		}.execute("");

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TCAgent.onPageStart(getContext(), "CommentFragment");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TCAgent.onPageEnd(getContext(), "CommentFragment");
	}

}