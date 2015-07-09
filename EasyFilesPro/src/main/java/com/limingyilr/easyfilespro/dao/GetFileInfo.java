package com.limingyilr.easyfilespro.dao;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import android.os.StatFs;
import android.util.Log;

import com.limingyilr.easyfilespro.R;

public class GetFileInfo {

	public static String GetFileDayTime(File file) {
		java.sql.Date timeY = new java.sql.Date(file.lastModified());
		return String.valueOf(timeY);
	}
	
	public static String GetFileHoursTime(File file) {
		java.util.Date timeD = new java.util.Date(file.lastModified());
		return String.valueOf(getNumString(timeD.getHours()) + ":"
				+ getNumString(timeD.getMinutes()) + ":"
				+ getNumString(timeD.getSeconds()));
	}
	
	public static String GetFileNum(File file) {
		if(file.isDirectory()
				&& file.canRead()) {
			File[] files = file.listFiles();
			return String.valueOf(files.length);
		}
		return "--";
	}
	public static String changeToMemoryCapacity(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		if (size == 0) {
			return "0.00B";
		} else if (size < 1024) {
			return String.valueOf(df.format((float) size)) + "B";
		} else if (size < 1024 * 1024) {
			return String.valueOf(df.format((float) size /  1024)) + "KB";
		} else if (size < 1024 * 1024 * 1024) {
			return String.valueOf(df.format((float) size / (1024 * 1024))) + "MB";
		} else {
			return String.valueOf(df.format((float) size / (1024 * 1024 * 1024)))
					+ "GB";
		}
	}
	
	public static String GetFileSize(File file) {
		long size = RecursionCountFileSize(file);
		return changeToMemoryCapacity(size);
	}
	
	public static String GetFileSize(List<File> files) {
		long size = 0;
		for(File f: files) {
			size = size + RecursionCountFileSize(f);
		}
		return changeToMemoryCapacity(size);
	}
	
	public static String getFileBlockSize(File file) {
		StatFs statFs=new StatFs(file.getPath());
        //Block 的 size
		long blockSize=(long) RecursionCountFileBlockSize(file);
		Long availableBlocks=(long) statFs.getBlockCount();
		return changeToMemoryCapacity(availableBlocks * blockSize);
	}
	
	public static String getFileBlockSize(List<File> files) {
		long blockSize = 0;
		for(File f: files) {
			blockSize = blockSize + RecursionCountFileBlockSize(f);
		}
		return changeToMemoryCapacity(blockSize);
	}

    //递归法计算文件或文件夹大小
	public static long RecursionCountFileSize(File file) {
		long size = 0;
		if(file.isFile()) {
			size = file.length() + size;
		}
		if (file.isDirectory()
				&& file.canRead()) {
			File[] childFile = file.listFiles();
			if (childFile != null || childFile.length != 0) {
				for (int i = 0; i < childFile.length; i ++) {
					if(childFile[i] != null) {
						size = RecursionCountFileSize(childFile[i]) + size;
					}
				}
			}
			
		}
		return size;
	}

    // 递归法计算文件或文件夹Block大小
	public static long RecursionCountFileBlockSize(File file) {
		StatFs statFs = new StatFs(file.getPath());
		long blockSize = 0;
        // Block 的 size
		if (file.isFile()) {
			blockSize = statFs.getBlockSize() + blockSize;
		}
		if (file.isDirectory() && file.canRead()) {
			File[] childFile = file.listFiles();
			if (childFile != null || childFile.length != 0) {
				for (int i = 0; i < childFile.length; i++) {
					if (childFile[i] != null) {
						blockSize = RecursionCountFileSize(childFile[i])
								+ blockSize;
					}
				}
			}

		}
		return blockSize;
	}

    //根据文件类型返回相应文件类型图标的HashMap
	public static HashMap<String, Object> GetFileIcon(File file) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if(file.isDirectory()) {
			hashMap.put("img", R.drawable.file_icon_folder);
		}else if(FileType.fileTypeJudge(file) == "mp3") {
			hashMap.put("img", R.drawable.file_icon_mp3);
		}else if(FileType.fileTypeJudge(file) == "wma") {
			hashMap.put("img", R.drawable.file_icon_wma);
		}else if(FileType.fileTypeJudge(file) == "wav") {
			hashMap.put("img", R.drawable.file_icon_wav);
		}else if(FileType.fileTypeJudge(file) == "mid") {
			hashMap.put("img", R.drawable.file_icon_midi);
		}else if(FileType.fileTypeJudge(file) == "txt") {
			hashMap.put("img", R.drawable.file_icon_txt);
		}else if(FileType.fileTypeJudge(file) == "lrc") {
			hashMap.put("img", R.drawable.file_icon_txt);
		}else if(FileType.fileTypeJudge(file) == "xml") {
			hashMap.put("img", R.drawable.file_icon_txt);
		}else if(FileType.fileTypeJudge(file) == "log") {
			hashMap.put("img", R.drawable.file_icon_set);
		}else if(FileType.fileTypeJudge(file) == "ini") {
			hashMap.put("img", R.drawable.file_icon_ini);
		}else if(FileType.fileTypeJudge(file) == "apk") {
			hashMap.put("img", R.drawable.file_icon_apk);
		}else if(FileType.fileTypeJudge(file) == "jpg") {
			hashMap.put("img", R.drawable.file_icon_jpeg);
		}else if(FileType.fileTypeJudge(file) == "png") {
			hashMap.put("img", R.drawable.file_icon_png);
		}else if(FileType.fileTypeJudge(file) == "bmp") {
			hashMap.put("img", R.drawable.file_icon_bmp);
		}else if(FileType.fileTypeJudge(file) == "gif") {
			hashMap.put("img", R.drawable.file_icon_gif);
		}else if(FileType.fileTypeJudge(file) == "tiff") {
			hashMap.put("img", R.drawable.file_icon_tiff);
		}else if(FileType.fileTypeJudge(file) == "mp4") {
			hashMap.put("img", R.drawable.file_icon_mpeg);
		}else if(FileType.fileTypeJudge(file) == "rmvb") {
			hashMap.put("img", R.drawable.file_icon_mpeg);
		}else if(FileType.fileTypeJudge(file) == "wmv") {
			hashMap.put("img", R.drawable.file_icon_mpeg);
		}else if(FileType.fileTypeJudge(file) == "3gp") {
			hashMap.put("img", R.drawable.file_icon_mpeg);
		}else if(FileType.fileTypeJudge(file) == "flv") {
			hashMap.put("img", R.drawable.file_icon_mpeg);
		}else if(FileType.fileTypeJudge(file) == "f4v") {
			hashMap.put("img", R.drawable.file_icon_mpeg);
		}else if(FileType.fileTypeJudge(file) == "mkv") {
			hashMap.put("img", R.drawable.file_icon_mpeg);
		}else if(FileType.fileTypeJudge(file) == "avi") {
			hashMap.put("img", R.drawable.file_icon_avi);
		}else if(FileType.fileTypeJudge(file) == "mpeg") {
			hashMap.put("img", R.drawable.file_icon_mpeg);
		}else if(FileType.fileTypeJudge(file) == "mov") {
			hashMap.put("img", R.drawable.file_icon_mov);
		}else if(FileType.fileTypeJudge(file) == "zip") {
			hashMap.put("img", R.drawable.file_icon_zip);
		}else if(FileType.fileTypeJudge(file) == "rar") {
			hashMap.put("img", R.drawable.file_icon_rar);
		}else if(FileType.fileTypeJudge(file) == "doc") {
			hashMap.put("img", R.drawable.file_icon_doc);
		}else if(FileType.fileTypeJudge(file) == "wps") {
			hashMap.put("img", R.drawable.file_icon_doc);
		}else if(FileType.fileTypeJudge(file) == "xls") {
			hashMap.put("img", R.drawable.file_icon_xls);
		}else if(FileType.fileTypeJudge(file) == "ppt") {
			hashMap.put("img", R.drawable.file_icon_ppt);
		}else if(FileType.fileTypeJudge(file) == "pdf") {
			hashMap.put("img", R.drawable.file_icon_pdf);
		}else {
			hashMap.put("img", R.drawable.file_icon_unknown);
		}
		return hashMap;
	}
	
	private static String getNumString(int num) {
		String str = String.valueOf(num);
		if(str.length() == 1) {
			return "0" + str;
		}
		return str;
	}
}
