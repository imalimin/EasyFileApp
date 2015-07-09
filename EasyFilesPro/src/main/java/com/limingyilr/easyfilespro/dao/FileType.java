package com.limingyilr.easyfilespro.dao;

import java.io.File;

public class FileType {

    //判断文件类型，并返回相应文件的后缀名
	public static String fileTypeJudge(File file) {
		String fileName = file.getName().toLowerCase();//把英文字母转化为小写以方便接下来的判断
		if (fileName.endsWith(".mp3")) {
			return "mp3";
		}else if(fileName.endsWith(".wav")) {
			return "wav";
		}else if(fileName.endsWith(".wma")) {
			return "wma";
		}else if(fileName.endsWith(".mid")) {
			return "mid";
		}else if(fileName.endsWith(".txt")) {
			return "txt";
		}else if(fileName.endsWith(".ini")) {
			return "ini";
		}else if(fileName.endsWith(".lrc")) {
			return "lrc";
		}else if(fileName.endsWith(".xml")) {
			return "xml";
		}else if(fileName.endsWith(".log")) {
			return "log";
		}else if(fileName.endsWith(".apk")) {
			return "apk";
		}else if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
			return "jpg";
		}else if(fileName.endsWith(".bmp")) {
			return "bmp";
		}else if(fileName.endsWith(".png")) {
			return "png";
		}else if(fileName.endsWith(".gif")) {
			return "gif";
		}else if(fileName.endsWith(".tiff")) {
			return "tiff";
		}else if(fileName.endsWith(".mp4")) {
			return "mp4";
		}else if(fileName.endsWith(".rmvb")) {
			return "rmvb";
		}else if(fileName.endsWith(".wmv")) {
			return "wmv";
		}else if(fileName.endsWith(".3gp")) {
			return "3gp";
		}else if(fileName.endsWith(".mkv")) {
			return "mkv";
		}else if(fileName.endsWith(".flv")) {
			return "flv";
		}else if(fileName.endsWith(".f4v")) {
			return "f4v";
		}else if(fileName.endsWith(".mpeg")) {
			return "mpeg";
		}else if(fileName.endsWith(".avi")) {
			return "avi";
		}else if(fileName.endsWith(".mov")) {
			return "mov";
		}else if(fileName.endsWith(".zip")) {
			return "zip";
		}else if(fileName.endsWith(".rar")) {
			return "rar";
		}else if(fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
			return "doc";
		}else if(fileName.endsWith(".wps")) {
			return "wps";
		}else if(fileName.endsWith(".xls")) {
			return "xls";
		}else if(fileName.endsWith(".ppt")) {
			return "ppt";
		}else if(fileName.endsWith(".pdf")) {
			return "pdf";
		}else {
			return "unknown";
		}
	}
}
