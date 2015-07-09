package com.limingyilr.easyfilespro.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.util.Log;

import com.limingyilr.easyfilespro.bean.SelectView;

public class BatchRemane {
	
	private List<File> filesHandleList;
	private String filePath;
	
	private String fileName;
	private String fileNameSuffix;
	private String separator;
	private int fileNum = 0;
	List<File>fileList = new ArrayList<File>();
	List<String>fileNames = new ArrayList<String>();
	
	private int FILES = 1;
	private int FOLDERS = 2;
	private int ALLFILES = 3;
	private int MODE = FILES;

    //在重命名前配置重命名方式
	public boolean setInformation(final List<File> filesHandleListT, final SelectView selectView) {

		filesHandleList = new ArrayList<File>();
		filesHandleList = filesHandleListT;
		
		fileNum = filesHandleList.size();
		filePath = filesHandleList.get(0).getAbsolutePath().toString().substring(0, 
				filesHandleList.get(0).getAbsolutePath().toString().length()
				- filesHandleList.get(0).getName().toString().length());
		
		MODE = FILES;
		if(selectView.RadioButtonOne.isChecked()) {
			
			fileName = selectView.fileName;
			separator = selectView.separator;
			if(selectView.fileNumber == "数字(1,2,3...)") {
				
				if(selectView.RadioButtonThree.isChecked()) {
					MODE = FILES;
				}else if(selectView.RadioButtonFour.isChecked()) {
					MODE = FOLDERS;
				}else if(selectView.RadioButtonFive.isChecked()) {
					MODE = ALLFILES;
				}
				return rename(0);
			}else if(selectView.fileNumber == "字母小写(a,b,c...)") {
				
				if(selectView.RadioButtonThree.isChecked()) {
					MODE = FILES;
				}else if(selectView.RadioButtonFour.isChecked()) {
					MODE = FOLDERS;
				}else if(selectView.RadioButtonFive.isChecked()) {
					MODE = ALLFILES;
				}
				return renameForGrapheme(0, "Lower");
			}else if(selectView.fileNumber == "字母大写(A,B,C...)") {
				if(selectView.RadioButtonThree.isChecked()) {
					MODE = FILES;
				}else if(selectView.RadioButtonFour.isChecked()) {
					MODE = FOLDERS;
				}else if(selectView.RadioButtonFive.isChecked()) {
					MODE = ALLFILES;
				}
				return renameForGrapheme(0, "Upper");
			}else {
				return false;
			}
		}else if(selectView.RadioButtonTwo.isChecked()) {
			
			fileName = selectView.nameEditText.getText().toString();
			separator = selectView.separatorEditText.getText().toString();
			int num = Integer.valueOf(selectView.numberEditText.getText().toString());
			
			if(selectView.RadioButtonThree.isChecked()) {
				MODE = FILES;
			}else if(selectView.RadioButtonFour.isChecked()) {
				MODE = FOLDERS;
			}else if(selectView.RadioButtonFive.isChecked()) {
				MODE = ALLFILES;
			}
			return rename(num);
		}else {
			return false;
		}
	}

    //数字式批量重命名
	private boolean rename(int num) {

		int j = num;
		String fileNameSuffixTemp;
		String fileNameTemp;

		for (int i = 0; i < filesHandleList.size(); i++) {
			fileNameSuffixTemp = getSuffix(filesHandleList.get(i).getName());
			if (MODE == FILES && filesHandleList.get(i).isFile()) {
				filesHandleList.get(i).renameTo(
						new File(filePath
								+ "BatchPro"
								+ getString(String.valueOf(j),
										String.valueOf(fileNum).length()) + "."
								+ fileNameSuffixTemp));
				j++;
			} else if (MODE == FOLDERS && filesHandleList.get(i).isDirectory()) {
				filesHandleList.get(i).renameTo(
						new File(filePath
								+ "BatchPro"
								+ getString(String.valueOf(j),
										String.valueOf(fileNum).length()) + "."
								+ fileNameSuffixTemp));
				j++;
			} else if (MODE == ALLFILES) {
				filesHandleList.get(i).renameTo(
						new File(filePath
								+ "BatchPro"
								+ getString(String.valueOf(j),
										String.valueOf(fileNum).length()) + "."
								+ fileNameSuffixTemp));
				j++;
			}
		}
		filesHandleList = refleshFilesList(filePath);
		filesHandleList = sortArrayList(filesHandleList);
		j = num;
		
		for (int i = 0; i < filesHandleList.size(); i++) {
			fileNameSuffixTemp = getSuffix(filesHandleList.get(i).getName());
			fileNameTemp = fileName
					+ separator
					+ getString(String.valueOf(j), String
							.valueOf(fileNum + num).length()) + "."
					+ fileNameSuffixTemp;
			if (MODE == FILES && filesHandleList.get(i).isFile()) {
				filesHandleList.get(i).renameTo(
						new File(filePath + fileNameTemp));
				j++;
			} else if (MODE == FOLDERS && filesHandleList.get(i).isDirectory()) {
				filesHandleList.get(i).renameTo(
						new File(filePath + fileNameTemp));
				j++;
			} else if (MODE == ALLFILES) {
				filesHandleList.get(i).renameTo(
						new File(filePath + fileNameTemp));
				j++;
			}
		}
		return true;

	}

    //字母式批量重命名方式
	private boolean renameForGrapheme(int num, String src) {

		int j = num;

		String fileNameSuffixTemp;
		String fileNameTemp;
		
		for (int i = 0; i < filesHandleList.size(); i++) {
			fileNameSuffixTemp = getSuffix(filesHandleList.get(i).getName());
			if (MODE == FILES && filesHandleList.get(i).isFile()) {
				filesHandleList.get(i).renameTo(
						new File(filePath
								+ "BatchPro"
								+ getString(String.valueOf(j),
										String.valueOf(fileNum).length()) + "."
								+ fileNameSuffixTemp));
				j++;
			} else if (MODE == FOLDERS && filesHandleList.get(i).isDirectory()) {
				filesHandleList.get(i).renameTo(
						new File(filePath
								+ "BatchPro"
								+ getString(String.valueOf(j),
										String.valueOf(fileNum).length()) + "."
								+ fileNameSuffixTemp));
				j++;
			} else if (MODE == ALLFILES) {
				filesHandleList.get(i).renameTo(
						new File(filePath
								+ "BatchPro"
								+ getString(String.valueOf(j),
										String.valueOf(fileNum).length()) + "."
								+ fileNameSuffixTemp));
				j++;
			}
		}
		filesHandleList = refleshFilesList(filePath);
		filesHandleList = sortArrayList(filesHandleList);
		j = num;
		for (int i = 0; i < filesHandleList.size(); i++) {
			fileNameSuffixTemp = getSuffix(filesHandleList.get(i).getName());
			if (src == "Upper") {
				fileNameTemp = fileName + separator
						+ getGraphemeString(j, fileNum + num, 0).toUpperCase()
						+ "." + fileNameSuffixTemp;
				if (MODE == FILES && filesHandleList.get(i).isFile()) {
					filesHandleList.get(i).renameTo(
							new File(filePath + fileNameTemp));
					j++;
				} else if (MODE == FOLDERS && filesHandleList.get(i).isDirectory()) {
					filesHandleList.get(i).renameTo(
							new File(filePath + fileNameTemp));
					j++;
				} else if (MODE == ALLFILES) {
					filesHandleList.get(i).renameTo(
							new File(filePath + fileNameTemp));
					j++;
				}
			} else if (src == "Lower") {
				fileNameTemp = fileName + separator
						+ getGraphemeString(j, fileNum + num, 0).toLowerCase()
						+ "." + fileNameSuffixTemp;
				if (MODE == FILES && filesHandleList.get(i).isFile()) {
					filesHandleList.get(i).renameTo(
							new File(filePath + fileNameTemp));
					j++;
				} else if (MODE == FOLDERS && filesHandleList.get(i).isDirectory()) {
					filesHandleList.get(i).renameTo(
							new File(filePath + fileNameTemp));
					j++;
				} else if (MODE == ALLFILES) {
					filesHandleList.get(i).renameTo(
							new File(filePath + fileNameTemp));
					j++;
				}
			}
		}

		return true;
	}

    //刷新文件列表
	private List<File> refleshFilesList(String filsPath) {
		List<File> filesList = new ArrayList<File>();
		File fileT = new File(filsPath);
		File[] filesT = fileT.listFiles();
		for(int i = 0; i < filesT.length; i ++) {
			filesList.add(filesT[i]);
		}
		return filesList;
	}

    //文件排序
	private List<File> sortArrayList(List<File> filesList) {
		
		List<File>listTemp = new ArrayList<File>();
		List<File>filesListTemp = new ArrayList<File>();
		List<File>filesListT = new ArrayList<File>();
		
		for(int i = 0; i < filesList.size(); i ++) {
			if(filesList.get(i).isDirectory()) {
				listTemp.add(filesList.get(i));
			}
		}
		for(int i = 0; i < filesList.size(); i ++) {
			if(filesList.get(i).isFile()) {
				filesListTemp.add(filesList.get(i));
			}
		}
		
		Collections.sort(listTemp, new Comparator<File>() {

	        public int compare(File s1, File s2) {
	            return s1.getName().compareToIgnoreCase(s2.getName());
	        }
	    });
		
		Collections.sort(filesListTemp, new Comparator<File>() {

	        public int compare(File s1, File s2) {
	            return s1.getName().compareToIgnoreCase(s2.getName());
	        }
	    });
		
		filesList.clear();
		for(int i = 0; i < listTemp.size(); i ++) {
			filesListT.add(listTemp.get(i));
		}
		for(int i = 0; i < filesListTemp.size(); i ++) {
			filesListT.add(filesListTemp.get(i));
		}
		return filesListT;
		
	}

    //获取文件后缀名
	private String getSuffix(String filename) {

		if ((filename != null) && (filename.length() > 0)) { 
			int dot = filename.lastIndexOf('.'); 
			if ((dot >-1) && (dot < (filename.length() - 1))) { 
			return filename.substring(dot + 1).toLowerCase();
			} 
		}
		return "";
	}

    //数字转字符串，并添加前置0
	public String getString(String src , int len){
		
		String srcTemp = src;
		while(srcTemp.length() < len){
			srcTemp = "0" + srcTemp ;
		}
		return srcTemp ;
	}

    //根据数字返回相应的字母字符串
	public String  getGraphemeString(int num, int len, int cou){
		
		int count = 0;
		int num_temp = num;
		
		String srcChar = "";
		
		while(len != 0) {
			len = len/26;
			count++;
		}
		
		count = count + cou;
		
		if(num_temp == 0) {
			srcChar = intToGrapheme(num_temp);
		}

		while(num_temp != 0) {
			
			int temp = num_temp%26;
			srcChar = srcChar + intToGrapheme(temp);
			num_temp = num_temp/26;
			if(num_temp >= 26) {
			
			}
		}
		  
		String reSrcChar = "";
		for(int i = srcChar.length() - 1; i > -1; i --) {
			reSrcChar = reSrcChar + srcChar.charAt(i);
		}
		
		while(reSrcChar.length() < count){
			reSrcChar = "a" + reSrcChar ;
		}
		
		return reSrcChar;
	}

    //根据数字返回相应的字母
	private static String intToGrapheme(int num) {
		
		String Grapheme = "";
		
		if(num%26 == 0){
			Grapheme = "a";
		}else if(num%26 == 1) {
			Grapheme = "b";
		}else if(num%26 == 2) {
			Grapheme = "c";
		}else if(num%26 == 3) {
			Grapheme = "d";
		}else if(num%26 == 4) {
			Grapheme = "e";
		}else if(num%26 == 5) {
			Grapheme = "f";
		}else if(num%26 == 6) {
			Grapheme = "g";
		}else if(num%26 == 7) {
			Grapheme = "h";
		}else if(num%26 == 8) {
			Grapheme = "i";
		}else if(num%26 == 9) {
			Grapheme = "j";
		}else if(num%26 == 10) {
			Grapheme = "k";
		}else if(num%26 == 11) {
			Grapheme = "l";
		}else if(num%26 == 12) {
			Grapheme = "m";
		}else if(num%26 == 13) {
			Grapheme = "n";
		}else if(num%26 == 14) {
			Grapheme = "o";
		}else if(num%26 == 15) {
			Grapheme = "p";
		}else if(num%26 == 16) {
			Grapheme = "q";
		}else if(num%26 == 17) {
			Grapheme = "r";
		}else if(num%26 == 18) {
			Grapheme = "s";
		}else if(num%26 == 19) {
			Grapheme = "t";
		}else if(num%26 == 20) {
			Grapheme = "u";
		}else if(num%26 == 21) {
			Grapheme = "v";
		}else if(num%26 == 22) {
			Grapheme = "w";
		}else if(num%26 == 23) {
			Grapheme = "x";
		}else if(num%26 == 24) {
			Grapheme = "y";
		}else if(num%26 == 25) {
			Grapheme = "z";
		}
		
		return Grapheme;
		
	}

	
}
