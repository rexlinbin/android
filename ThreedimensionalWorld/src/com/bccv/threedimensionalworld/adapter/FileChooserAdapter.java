package com.bccv.threedimensionalworld.adapter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import com.bccv.threedimensionalworld.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileChooserAdapter extends BaseAdapter {

	private ArrayList<FileInfo> mFileLists;
	private LayoutInflater mLayoutInflater = null;

	private static ArrayList<String> PIC_SUFFIX = new ArrayList<String>();
	private static ArrayList<String> MOVIES_SUFFIX = new ArrayList<String>();
	private static ArrayList<String> MUSIC_SUFFIX = new ArrayList<String>();
	static {
		PIC_SUFFIX.add(".jpg");
		PIC_SUFFIX.add(".png");
		PIC_SUFFIX.add(".bmp");
	}
	static {
		MOVIES_SUFFIX.add(".mp4");
		MOVIES_SUFFIX.add(".avi");
		MOVIES_SUFFIX.add(".flv");
		MOVIES_SUFFIX.add(".wmv");
		MOVIES_SUFFIX.add(".mkv");
		MOVIES_SUFFIX.add(".mov");
		MOVIES_SUFFIX.add(".rmvb");

	}

	static {
		MUSIC_SUFFIX.add(".mp3");
	}

	public FileChooserAdapter(Context context, ArrayList<FileInfo> fileLists) {
		super();
		mFileLists = fileLists;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFileLists.size();
	}

	@Override
	public FileInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mFileLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = mLayoutInflater.inflate(R.layout.item_usb_list, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		FileInfo fileInfo = getItem(position);
		// TODO

		holder.tvFileName.setText(fileInfo.getFileName());
		holder.tvFileName.setScaleX(0.5f);
		holder.imgFileIcon.setScaleX(0.5f);
		if (fileInfo.isDirectory()) { // 文件夹
			holder.imgFileIcon.setImageResource(R.drawable.file);

		} else if (fileInfo.isPicile()) { // 图片文件
			holder.imgFileIcon.setImageResource(R.drawable.pic2x);

		} else if (fileInfo.isMovieFile()) { // 视频文件
			holder.imgFileIcon.setImageResource(R.drawable.video2x);

		} else if (fileInfo.isMusicFile()) { // 音乐文件
			holder.imgFileIcon.setImageResource(R.drawable.music2x);

		} else { // 未知文件
			holder.imgFileIcon.setImageResource(R.drawable.folder);
			
		}
		return view;
	}

	static class ViewHolder {
		ImageView imgFileIcon;
		TextView tvFileName;

		public ViewHolder(View view) {
			imgFileIcon = (ImageView) view.findViewById(R.id.imgFileIcon);
			tvFileName = (TextView) view.findViewById(R.id.tvFileName);
		}
	}

	enum FileType {
		FILE, DIRECTORY;
	}

	// =========================
	// Model
	// =========================
	public static class FileInfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private FileType fileType;
		private String fileName;
		private String filePath;

		public FileInfo(String filePath, String fileName, boolean isDirectory) {
			this.filePath = filePath;
			this.fileName = fileName;
			fileType = isDirectory ? FileType.DIRECTORY : FileType.FILE;
		}

		public boolean isPicile() {
			if (fileName.lastIndexOf(".") < 0) // Don't have the suffix
				return false;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if (!isDirectory() && PIC_SUFFIX.contains(fileSuffix))
				return true;
			else
				return false;
		}

		public boolean isMusicFile() {
			if (fileName.lastIndexOf(".") < 0) // Don't have the suffix
				return false;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if (!isDirectory() && MUSIC_SUFFIX.contains(fileSuffix))
				return true;
			else
				return false;
		}

		public boolean isMovieFile() {
			if (fileName.lastIndexOf(".") < 0) // Don't have the suffix
				return false;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if (!isDirectory() && MOVIES_SUFFIX.contains(fileSuffix))
				return true;
			else
				return false;
		}

		public boolean isDirectory() {
			if (fileType == FileType.DIRECTORY)
				return true;
			else
				return false;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public String toString() {
			return "FileInfo [fileType=" + fileType + ", fileName=" + fileName
					+ ", filePath=" + filePath + "]";
		}
	}

}
