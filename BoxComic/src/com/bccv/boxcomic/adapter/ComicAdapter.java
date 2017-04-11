package com.bccv.boxcomic.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ReadComicActivity.LosePageCallback;
import com.bccv.boxcomic.ebook.StringUtil;
import com.bccv.boxcomic.modal.ComicPic;
import com.bccv.boxcomic.photoview.PhotoView;
import com.bccv.boxcomic.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.bccv.boxcomic.photoview.PhotoViewAttacher.OnViewTapListener;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ComicAdapter extends BaseAdapter {

	private List<ComicPic> list;
	private Context context;
	private boolean isLocal = true;
	private boolean isHigh = true;
	private OnTouchListener onPhotoTapListener;

	private LosePageCallback losePageCallback;

	public ComicAdapter(List<ComicPic> list, Context context,
			OnTouchListener onPhotoTapListener,
			LosePageCallback losePageCallback, boolean isLocal) {
		this.list = list;
		this.context = context;
		this.onPhotoTapListener = onPhotoTapListener;
		this.losePageCallback = losePageCallback;
		this.isLocal = isLocal;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			convertView = View.inflate(context, R.layout.listitem_readcomic,
					null);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.photoView);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.num_textView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// viewHolder.imageView.setZoomable(false);
		// viewHolder.imageView.setScaleType(ScaleType.FIT_XY);
		// viewHolder = new ViewHolder();
		// convertView = View.inflate(context, R.layout.listitem_readcomic,
		// null);
		// viewHolder.imageView = (PhotoView)
		// convertView.findViewById(R.id.photoView);
		// viewHolder.textView = (TextView)
		// convertView.findViewById(R.id.num_textView);

		viewHolder.textView.setText(position + 1 + "");

		ImageLoader imageLoader = ImageLoader.getInstance();

		String imageUrlString = null;
		if (list.get(position).isLocal()) {
			imageUrlString = list.get(position).getUrl();
		} else {
			imageUrlString = list.get(position).getUrl();
		}
		if (!list.get(position).isLocal()) {
			try {
				imageUrlString = URLEncoder.encode(imageUrlString, "utf-8")
						.replaceAll("\\+", "%20");
				imageUrlString = imageUrlString.replaceAll("%3A", ":").replaceAll(
						"%2F", "/");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				imageUrlString = URLDecoder.decode(imageUrlString, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		imageLoader.displayImage(imageUrlString, viewHolder.imageView,
				GlobalParams.comicOptions, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						View coView = (View) view.getParent();
						int width = GlobalParams.screenHeight;
						int height = (int) (((float) loadedImage.getHeight())
								/ ((float) loadedImage.getWidth()) * width);
						LayoutParams params = new LayoutParams(width, height);
						coView.setLayoutParams(params);

						android.view.ViewGroup.LayoutParams imageLayoutParams = view
								.getLayoutParams();
						imageLayoutParams.height = height;
						imageLayoutParams.width = width;
						view.requestLayout();
						coView.requestLayout();
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});

		viewHolder.imageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isLocal) {
						GlobalParams.readComicLocalActivity.setListClick(event.getRawX(), event.getRawY());
					}else {
						GlobalParams.readComicActivity.setListClick(event.getRawX(), event.getRawY());
					}
						
				} 

				return true;
			}
		});

		
		if (StringUtils.isEmpty(imageUrlString)) {
			viewHolder.textView.setText("此页暂缺");
			losePageCallback.callback((position + 1) + "");
		}
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView textView;
	}

	public void setIsHigh(boolean isHigh) {
		this.isHigh = isHigh;
	}
}
