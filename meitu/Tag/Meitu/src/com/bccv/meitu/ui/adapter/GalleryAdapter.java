package com.bccv.meitu.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.bccv.meitu.R;
import com.bccv.meitu.model.PicInfo;
import com.bccv.meitu.view.MyImageView;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

@SuppressWarnings("deprecation")
public class GalleryAdapter extends BaseAdapter {
	private Context context;
	public GalleryAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		initList();
	}

	private LayoutInflater inflater;
    public List<Integer>    imageUrlList;
    private List<PicInfo> picList;
    private int currentItem = -1;
    private void initList(){
    	imageUrlList = new ArrayList<Integer>();
    	imageUrlList.add(R.drawable.p1);
    	imageUrlList.add(R.drawable.p2);
    	imageUrlList.add(R.drawable.p3);
    	imageUrlList.add(R.drawable.p4);
    	imageUrlList.add(R.drawable.p5);
    	imageUrlList.add(R.drawable.p6);
    }

    @Override
    public int getCount() {
        return picList==null?imageUrlList.size():picList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        currentItem = position;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.photo_details_gallery_item, null);
            holder = new ViewHolder();
            holder.imageView = (MyImageView)convertView.findViewById(R.id.image_list_image);
            holder.imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if(picList == null || picList.size() == 0){
        	Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), imageUrlList.get(position));
        	holder.imageView.setImageBitmap(bmp);
        }else{
        	ImageLoaderUtil.getInstance(context).displayImage(picList.get(position).getPic(), holder.imageView);
        }
        return convertView;
    }
    
    public void setImageUrlList(List<PicInfo> imageUrlList) {
        this.picList = imageUrlList;
        this.notifyDataSetChanged();
    }
    
    public int getCurItem(){
    	return currentItem != -1?currentItem:-1;
    }

	static class ViewHolder {
    	MyImageView imageView;
    }
}
