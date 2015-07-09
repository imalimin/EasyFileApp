package com.limingyilr.easyfilespro.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.limingyilr.easyfilespro.util.MessengeHandle;

public class FileHandle {
	
	private int copyProgress = 0;
	private int deleteProgress = 0;
	
	public int newFile(String filePath) {
		
		return MessengeHandle.SUCCESSED;
	}
	
	public int newDirectory(String filePath) {
		
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
			return MessengeHandle.SUCCESSED;
		}
		return MessengeHandle.FAIL;
	}
	
	public int fileCopy(List<File> filesHandleList, String filePath) {
		for(int i = 0; i < filesHandleList.size(); i ++) {
			RecursionCopyFile(filesHandleList.get(i), filePath);
		}
		return MessengeHandle.SUCCESSED;
	}
	
	public int fileShear(List<File> filesHandleList, String filePath) {
		for(int i = 0; i < filesHandleList.size(); i ++) {
			RecursionCopyFile(filesHandleList.get(i), filePath);
		}
		for(int i = 0; i < filesHandleList.size(); i ++) {
			RecursionDeleteFile(filesHandleList.get(i));
		}
		return MessengeHandle.SUCCESSED;
	}

    //粘贴文件
	public int filePaste(List<File> filesHandleList, String filePath) {
		for(int i = 0; i < filesHandleList.size(); i ++) {
			RecursionCopyFile(filesHandleList.get(i), filePath);
		}
		return MessengeHandle.SUCCESSED;
	}

    //删除文件
	public int fileDelete(List<File> filesHandleList) {
		
		for(int i = 0; i < filesHandleList.size(); i ++) {
			RecursionDeleteFile(filesHandleList.get(i));
		}
		return MessengeHandle.SUCCESSED;
	}
    //单文件重命名
	public int fileRename(List<File> filesHandleList, String newName) {
		String filePath = filesHandleList.get(0).getAbsolutePath().toString().substring(0, 
				filesHandleList.get(0).getAbsolutePath().toString().length()
				- filesHandleList.get(0).getName().toString().length());
		
		filesHandleList.get(0).renameTo(new File(filePath + newName));
		return MessengeHandle.SUCCESSED;
	}

    //递归法删除文件和文件夹
	public void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    //递归法复制文件和文件夹
	public int RecursionCopyFile(File fromFile, String filePath) {

		if (fromFile.isFile()) {
			File toFile = new File(filePath + "/" + fromFile.getName());
			byte bt[] = new byte[1024];
			int c;
			try {
				FileInputStream fosfrom = new FileInputStream(fromFile);
				FileOutputStream fosto = new FileOutputStream(toFile);

				if (toFile.exists()) {
					while ((c = fosfrom.read(bt)) > 0) {
						fosto.write(bt, 0, c); // ������д�����ļ�����
						copyProgress = copyProgress + 1024;
					}
					fosfrom.close();
					fosto.close();
					return MessengeHandle.SUCCESSED;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (fromFile.isDirectory()) {
			String filePathAdd = fromFile.getAbsolutePath().substring(
					fromFile.getAbsolutePath().lastIndexOf("/"),
					fromFile.getAbsolutePath().length());
			File toFile = new File(filePath + filePathAdd);
			if (!toFile.exists()) {
				toFile.mkdirs();
			}
			File[] childFile = fromFile.listFiles();
			for (File f : childFile) {
				RecursionCopyFile(f, filePath + filePathAdd);
			}
			return MessengeHandle.SUCCESSED;
		}
		return MessengeHandle.FAIL;
	}

    //根据路径获取上一级目录
	public String[] getBackDirectoryPath(String filePath) {
		int count = 0;
		List<String> filePathList = new ArrayList<String>();
		
		filePathList.add(filePath);
		String filePathT = filePath;
		while (filePathT.lastIndexOf('/') != 0 && count < 3){
			filePathT = filePathT.substring(0, filePathT.lastIndexOf('/'));
			filePathList.add(filePathT);
			count ++;
		}
		int length = filePathList.size();
		String[] filePathListT = new String[length];
		for(int i = 0; i < length; i ++) {
			filePathListT[i] = filePathList.get(length - 1 - i);
		}
		
		return filePathListT;
	}

	public int getCopyProgress() {
		return copyProgress;
	}

	public void setCopyProgress(int copyProgress) {
		this.copyProgress = copyProgress;
	}

	public int getDeleteProgress() {
		return deleteProgress;
	}

	public void setDeleteProgress(int deleteProgress) {
		this.deleteProgress = deleteProgress;
	}
	
}
