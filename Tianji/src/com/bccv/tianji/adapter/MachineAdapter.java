package com.bccv.tianji.adapter;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import com.bccv.tianji.R;
import com.utils.net.TcpServerHelper.Machine;
import com.utils.pulltorefresh.IPullToRefresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MachineAdapter extends BaseAdapter {
	
	private Context context;
	private List<Machine> list;

	public MachineAdapter(Context context, List<Machine> list){
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.listitem_machine, null);
			viewHolder.ipTextView = (TextView) convertView.findViewById(R.id.ip_textView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Machine machine = list.get(position);
		viewHolder.ipTextView.setText(machine.getGameBoxName());
		
		return convertView;
	}

	class ViewHolder{
		TextView ipTextView;
	}
}
