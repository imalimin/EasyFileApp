package com.limingyilr.easyfilespro.ui;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.limingyilr.easyfilespro.R;
import com.limingyilr.easyfilespro.dao.GetFileInfo;
import com.limingyilr.easyfilespro.dao.GetThumbnail;

public class DisplayFileInfo {

	private Context context;
	private ImageView fileInfo_img;
	private TextView fileInfo_name;
	private TextView fileInfo_path;
	private TextView fileInfo_date;
	private TextView fileInfo_size;
	private TextView fileInfo_blockSize;

	private String size = "";
	private String blockSize = "";

	public DisplayFileInfo(Context context) {
		this.context = context;
	}

	public void DisplayInfo(List<File> fileList) {
		View convertView = LayoutInflater.from(context).inflate(
				R.layout.file_info, null);
		final Builder builder = new Builder(context);
		fileInfo_img = (ImageView) convertView
				.findViewById(R.id.fileInfo_img);
		fileInfo_name = (TextView) convertView
				.findViewById(R.id.fileInfo_name);
		fileInfo_path = (TextView) convertView
				.findViewById(R.id.fileInfo_path);
		fileInfo_date = (TextView) convertView
				.findViewById(R.id.fileInfo_date);
		fileInfo_size = (TextView) convertView
				.findViewById(R.id.fileInfo_size);
		fileInfo_blockSize = (TextView) convertView
				.findViewById(R.id.fileInfo_blockSize);
		
		if (fileList.size() > 0) {
			countFileSize(fileList);
			countFileBlockSize(fileList);
			fileInfo_size.setText("大小: \" + \"正在计算...");
			fileInfo_blockSize.setText("占用空间: \" + \"正在计算...");
			if (fileList.size() == 1) {
				if (fileList.get(0).isFile()
						&& fileList.get(0).getName().endsWith(".apk")) {
					fileInfo_img.setImageDrawable(GetThumbnail.getApkIcon(
							fileList.get(0).getAbsolutePath(), context));
				} else {
					fileInfo_img.setBackgroundResource((Integer) GetFileInfo
							.GetFileIcon(fileList.get(0)).get("img"));
				}
				fileInfo_name.setText(fileList.get(0).getName());
				fileInfo_path.setText("路径: "
						+ fileList.get(0).getAbsolutePath());
				fileInfo_date.setText("时间："
						+ GetFileInfo.GetFileDayTime(fileList.get(0)) + "  "
						+ GetFileInfo.GetFileHoursTime(fileList.get(0)));
			}else {
				fileInfo_img.setBackgroundResource(R.drawable.file_icon_unknown);
				fileInfo_name.setText("多文件");
				fileInfo_path.setText("路径: \" + \"无效");
				fileInfo_date.setText("时间: \" + \"无效");
			}
			builder.setTitle("属性:");
			builder.setView(convertView);
		}else {
			builder.setTitle("请选择文件!");
		}
		
		builder.setNegativeButton("确定", null);
		builder.create().show();
	}
	
	public void countFileSize(final List<File> fileList) {

		new Thread(new Runnable() {

			public void run() {
				size = GetFileInfo.GetFileSize(fileList);
				fileOperationHandler.sendEmptyMessage(1);
			}
		}).start();
	}
	public void countFileBlockSize(final List<File> fileList) {

		new Thread(new Runnable() {

			public void run() {
				if(fileList.size() == 1) {
					blockSize = GetFileInfo.getFileBlockSize(fileList.get(0));
				} else {
					blockSize = GetFileInfo.getFileBlockSize(fileList);
				}
				fileOperationHandler.sendEmptyMessage(2);
			}
		}).start();
	}
	
	final Handler fileOperationHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch(msg.what){
			case 1:
				fileInfo_size.setText("大小: " + size);
				break;
			case 2:
				fileInfo_blockSize.setText("占用空间: " + blockSize);
				break;
			default:
				break;
			}
		}

	};

}
