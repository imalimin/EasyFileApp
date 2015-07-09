package com.limingyilr.easyfilespro.dao;

import java.io.File;

import android.content.Intent;
import android.net.Uri;

public class OpenFile {

	public static Intent OpenFileT(File file) {
		if(FileType.fileTypeJudge(file) == "mp3" || FileType.fileTypeJudge(file) == "wav"
				|| FileType.fileTypeJudge(file) == "wma" || FileType.fileTypeJudge(file) == "mid") {
			Intent intent = new Intent("android.intent.action.VIEW");  
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			intent.putExtra("oneshot", 0);  
			intent.putExtra("configchange", 0);  
			Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));  
			intent.setDataAndType(uri, "audio/*"); 
			return intent;
		}else if(FileType.fileTypeJudge(file) == "txt" || FileType.fileTypeJudge(file) == "ini"
				|| FileType.fileTypeJudge(file) == "lrc" || FileType.fileTypeJudge(file) == "xml"
				|| FileType.fileTypeJudge(file) == "log") {
			Intent intent = new Intent("android.intent.action.VIEW");  
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));
			intent.setDataAndType(uri, "text/plain");  
			return intent;
		}else if(FileType.fileTypeJudge(file) == "apk") {
			Intent intent = new Intent();    
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
			intent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));  
			intent.setDataAndType(uri,"application/vnd.android.package-archive");   
			return intent;
		}else if(FileType.fileTypeJudge(file) == "jpg" || FileType.fileTypeJudge(file) == "bmp"
				|| FileType.fileTypeJudge(file) == "png" || FileType.fileTypeJudge(file) == "gif"
				|| FileType.fileTypeJudge(file) == "tiff") {
			Intent intent = new Intent("android.intent.action.VIEW");
		    intent.addCategory("android.intent.category.DEFAULT");
		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));
		    intent.setDataAndType(uri, "image/*");  
		    return intent;
		}else if(FileType.fileTypeJudge(file) == "mp4" || FileType.fileTypeJudge(file) == "rmvb"
				|| FileType.fileTypeJudge(file) == "wmv" || FileType.fileTypeJudge(file) == "3gp"
				|| FileType.fileTypeJudge(file) == "ktv" || FileType.fileTypeJudge(file) == "flv"
				|| FileType.fileTypeJudge(file) == "f4v" || FileType.fileTypeJudge(file) == "mpeg"
				|| FileType.fileTypeJudge(file) == "avi") {
			Intent intent = new Intent("android.intent.action.VIEW");  
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			intent.putExtra("oneshot", 0);  
			intent.putExtra("configchange", 0);  
			Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));  
			intent.setDataAndType(uri, "video/*");  
			return intent;
		}else if(FileType.fileTypeJudge(file) == "zip") {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri uri = Uri.fromFile(file);
			intent.setDataAndType(uri, "application/zip");
			return intent;
		}else if(FileType.fileTypeJudge(file) == "rar") {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri uri = Uri.fromFile(file);
			intent.setDataAndType(uri, "application/rar");
			return intent;
		}else if(FileType.fileTypeJudge(file) == "doc") {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));
			intent.setDataAndType(uri, "application/msword");
			return intent;
		}else if(FileType.fileTypeJudge(file) == "wps") {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri uri = Uri.fromFile(file);
			intent.setDataAndType(uri, "application/wps");
			return intent;
		}else if(FileType.fileTypeJudge(file) == "xls") {
			Intent intent = new Intent("android.intent.action.VIEW");
		    intent.addCategory("android.intent.category.DEFAULT");
		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		    Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));  
		    intent.setDataAndType(uri, "application/vnd.ms-excel");
		    return intent;
		}else if(FileType.fileTypeJudge(file) == "ppt") {
			Intent intent = new Intent("android.intent.action.VIEW");  
			intent.addCategory("android.intent.category.DEFAULT");  
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));  
			intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
			return intent;
		}else if(FileType.fileTypeJudge(file) == "pdf") {
			 Intent intent = new Intent("android.intent.action.VIEW");  
			 intent.addCategory("android.intent.category.DEFAULT");  
			 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			 Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));  
			 intent.setDataAndType(uri, "application/pdf");
			 return intent;
		} else {
//			Intent intent = new Intent("android.intent.action.VIEW");
//			intent.addCategory("android.intent.category.DEFAULT");
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			Uri uri = Uri.fromFile(new File(file.getAbsoluteFile().toString()));
//			intent.setDataAndType(uri, "unknown/*");
//			startActivityForResult(intent, 1);
			return null;
		}
	}
}
