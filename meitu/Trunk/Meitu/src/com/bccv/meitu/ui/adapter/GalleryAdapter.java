package com.bccv.meitu.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.model.PicInfo;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.ui.activity.LoginActivity;
import com.bccv.meitu.ui.activity.PhotoDetailsActivity;
import com.bccv.meitu.view.MyImageView;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

@SuppressWarnings("deprecation")
public class GalleryAdapter extends BaseAdapter {
	private PhotoDetailsActivity act;
	private Dialog dialog ;
	
	public GalleryAdapter(PhotoDetailsActivity activity) {
		this.act = activity;
		inflater = LayoutInflater.from(activity);
		initList();
		initDialog();
	}
	
	private void initDialog(){
		//此处直接new一个Dialog对象出来，在实例化的时候传入主题
		dialog = new Dialog(act, R.style.MyDialog);
		//设置它的ContentView
		View view = LayoutInflater.from(act).inflate(R.layout.custome_dialog_details, null);
//		TextView tv = (TextView) view.findViewById(R.id.dialog_message);
		TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
		TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
		dialog_enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				if (UserInfoManager.isLogin()) {
					act.buy();
				}else {
					Intent intent = new Intent(act,LoginActivity.class);
					act.startActivity(intent);
				}
			}
		});
		dialog_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
//		tv.setText("");
		view.setMinimumWidth(600);
//		view.setMinimumHeight(400);
		dialog.setContentView(view);
	}

	private LayoutInflater inflater;
    public List<Integer>    imageUrlList;
    private List<PicInfo> picList;
    private boolean isFree = true;
    private int currentItem = -1;
    private void initList(){
    	imageUrlList = new ArrayList<Integer>();
    	imageUrlList.add(R.drawable.details_bg);
    }

    @Override
    public int getCount() {
    	if (isFree) {
    		return picList==null?imageUrlList.size():picList.size();
		}else{
			return picList==null?imageUrlList.size():picList.size()+1;
		}
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.photo_details_gallery_item, null);
			holder = new ViewHolder();
			holder.imageView = (MyImageView)convertView.findViewById(R.id.image_list_image);
			holder.imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			holder.position = position;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
//		System.out.println("holder position : "+holder.position);
		currentItem = holder.position;
		if(picList == null || picList.size() == 0){
			holder.imageView.setBackgroundResource(imageUrlList.get(position));
		}else{
			if (position != picList.size()) {//此处注意position是从0开始的
				ImageLoaderUtil.getInstance(act).displayImage(picList.get(position).getPic(), holder.imageView,R.drawable.details_bg);
			}else{
				holder.imageView.setBackgroundResource(R.drawable.details_bg);
				//查看更多  需要扣除您30金币
				if (!dialog.isShowing()) {
					dialog.show();
				}
			}
		}
        return convertView;
    }
    public void setImageUrlList(List<PicInfo> imageUrlList,boolean is_free) {
        this.picList = imageUrlList;
        this.isFree = is_free;
        this.notifyDataSetChanged();
    }
    
    public int getCurItem(){
    	return currentItem != -1?currentItem:0;
    }

	static class ViewHolder {
    	MyImageView imageView;
    	int position;
    }
}
