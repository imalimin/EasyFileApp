package com.limingyilr.easyfilespro.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.limingyilr.easyfilespro.R;
import com.limingyilr.easyfilespro.bean.SelectView;
import com.limingyilr.easyfilespro.bean.TitleButton;
import com.limingyilr.easyfilespro.dao.BatchRemane;
import com.limingyilr.easyfilespro.dao.FileHandle;
import com.limingyilr.easyfilespro.dao.FileType;
import com.limingyilr.easyfilespro.dao.GetFileInfo;
import com.limingyilr.easyfilespro.dao.GetThumbnail;
import com.limingyilr.easyfilespro.dao.MyStorage;
import com.limingyilr.easyfilespro.dao.OpenFile;
import com.limingyilr.easyfilespro.util.MessengeHandle;

/**
 * 展现全部文件的Activity 按返回键可以向上回溯文件路径，
 * */
public class ListAllFileActivity extends Activity {

	private List<File> fileNameList;
	private List<File> pasteFileList;
	private List<File> fileHistory;
	private List<File> storageList;
	private List<HashMap<String, Object>> fileData;

	private RelativeLayout list_layout;
	private RelativeLayout operateOneRelativeLayout;
	private RelativeLayout operateTwoRelativeLayout;
	private LinearLayout list_title_button_layout;
	private RelativeLayout list_title_select_layout;
	private RelativeLayout operate_layout;

	private ListView filesListView;
	private ListView miniFilesListView;
	private CheckBox file_list_checkbox;

	private Button operate_newfileOne;
	private Button operate_copy;
	private Button operate_shear;
	private Button operate_delete;
	private Button operate_rename;
	private Button operate_checkAll;
	private Button operate_getInfo;

	private Button operate_paste;
	private Button operate_newfileTwo;
	private Button operate_cancel;

	private Button title_cancel_btn;
	private TextView title_selectNum_text;

	private BaseAdapter listAdapter;
	private BatchRemane batchRemane = new BatchRemane();
	private SelectView selectView = new SelectView();
	private TitleButton titleButton = new TitleButton();
	private ProgressDialog progressDialog;

	private static int MaxHistoryNum = 12;// 最大历史浏览记录

	private static int OPEN = 1;
	private static int SELECT = 2;
	private static int COPY = 3;
	private static int SHEAR = 4;
	private static int DELETE = 5;
	private static int PASTE = 6;
	private int MODE = OPEN;

	private int height;
	private int reHeight;

	private int[] savePositionTemp = { 0, 0 };
	private List<String> savePosition;
	private String[] filePathList;

	private boolean MOVE = false;

	private FileHandle fileHandle;

	private String fileCanonicalPath;

	final static int CONTEXT_MENU_1 = Menu.FIRST;
	final static int CONTEXT_MENU_2 = Menu.FIRST + 1;
	final static int CONTEXT_MENU_3 = Menu.FIRST + 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list);

		initView();
	}

	private void initView() {
		filesListView = (ListView) findViewById(R.id.files_list);

		list_layout = (RelativeLayout) findViewById(R.id.list_layout);
		operateOneRelativeLayout = (RelativeLayout) findViewById(R.id.operate_one_layout);
		operateTwoRelativeLayout = (RelativeLayout) findViewById(R.id.operate_two_layout);
		operate_layout = (RelativeLayout) findViewById(R.id.operate_layout);

		list_title_button_layout = (LinearLayout) findViewById(R.id.list_title_button_layout);
		list_title_select_layout = (RelativeLayout) findViewById(R.id.list_title_select_layout);
		title_cancel_btn = (Button) findViewById(R.id.title_cancel_btn);
		title_selectNum_text = (TextView) findViewById(R.id.title_selectNum_text);

		titleButton.backDirectoryOne = (Button) findViewById(R.id.backDirectoryOne);
		titleButton.backDirectoryTwo = (Button) findViewById(R.id.backDirectoryTwo);
		titleButton.backDirectoryThree = (Button) findViewById(R.id.backDirectoryThree);
		titleButton.backDirectoryFour = (Button) findViewById(R.id.backDirectoryFour);

		operate_newfileOne = (Button) findViewById(R.id.operate_newfile_btnOne);
		operate_copy = (Button) findViewById(R.id.operate_copy_btn);
		operate_shear = (Button) findViewById(R.id.operate_shear_btn);
		operate_delete = (Button) findViewById(R.id.operate_delete_btn);
		operate_rename = (Button) findViewById(R.id.operate_rename_btn);
		operate_checkAll = (Button) findViewById(R.id.title_checkAll_btn);
		operate_getInfo = (Button) findViewById(R.id.operate_getInfo_btn);

		operate_paste = (Button) findViewById(R.id.operate_paste_btn);
		operate_newfileTwo = (Button) findViewById(R.id.operate_newfile_btnTwo);
		operate_cancel = (Button) findViewById(R.id.operate_cancel_btn);

		savePosition = new ArrayList<String>();
		fileHandle = new FileHandle();
		fileHistory = new ArrayList<File>();

		initFileList(android.os.Environment.getExternalStorageDirectory()
				.getAbsolutePath());

		setMyStorages();// 设置本地储存器列表
		setButtonListener();// 设置按键监听
	}

	private int newFile(final String info) {

		final EditText DialogView = new EditText(this);
		Builder builder = new Builder(ListAllFileActivity.this);

		builder.setTitle("新建文件夾");
		builder.setView(DialogView);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						String filePath = fileCanonicalPath + "/"
								+ DialogView.getText().toString();
						fileHandle.newDirectory(filePath);
						if (info == "cancelSelecte") {
							cancelSelecte();
						}
						refreshList();
					}
				});
		builder.setNegativeButton("取消", null);
		builder.create().show();
		return MessengeHandle.SUCCESSED;
	}

	private int deleteHandle() {
		final List<File> filesHandleList = getCheckedList();

		Builder builder = new Builder(ListAllFileActivity.this);

		builder.setTitle("确定删除此" + filesHandleList.size() + "项？");
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						MODE = DELETE;
						fileOperationThread(filesHandleList, "");
					}
				});
		builder.setNegativeButton("取消", null);
		builder.create().show();
		return MessengeHandle.SUCCESSED;
	}

	private int renameHandle() {

		List<File> filesHandleList = new ArrayList<File>();
		filesHandleList = getCheckedList();

		if (filesHandleList.size() == 0) {
			Toast.makeText(ListAllFileActivity.this, "请至少选择一个项目",
					Toast.LENGTH_LONG).show();
			return MessengeHandle.FAIL;
		}

		if (filesHandleList.size() == 1) {
			renameOne(filesHandleList);
		}

		if (filesHandleList.size() > 1) {
			batchRename(filesHandleList);
		}
		return MessengeHandle.SUCCESSED;
	}

	private void checkAll() {
		int CHECHALL = 1;
		int CANCEL = 0;
		int CHECKMODE = CANCEL;
		for (int i = 0; i < fileData.size(); i++) {
			if (fileData.get(i).get("check") == Boolean.FALSE) {
				CHECKMODE = CHECHALL;
			}
		}
		if (CHECKMODE == CHECHALL) {
			for (int i = 0; i < fileData.size(); i++) {
				fileData.get(i).remove("check");
				fileData.get(i).put("check", Boolean.TRUE);
			}
		} else if (CHECKMODE == CANCEL) {
			for (int i = 0; i < fileData.size(); i++) {
				fileData.get(i).remove("check");
				fileData.get(i).put("check", Boolean.FALSE);
			}
		}
		listAdapter.notifyDataSetChanged();
		title_selectNum_text.setText(GetFileInfo.GetFileNum(new File(
				fileCanonicalPath)) + "/" + getCheckedList().size());
	}

	public void cancelSelecte() {

		if (MODE == SELECT || MODE == PASTE) {
			reSetListLayout();
			operateOneRelativeLayout.setVisibility(RelativeLayout.INVISIBLE);
			operateTwoRelativeLayout.setVisibility(RelativeLayout.INVISIBLE);
			MODE = OPEN;
		}
		if (MODE == OPEN || MODE == COPY) {
			// reSetListLayout();
			list_title_button_layout.setVisibility(LinearLayout.VISIBLE);
			list_title_select_layout.setVisibility(RelativeLayout.INVISIBLE);
		}
		listAdapter.notifyDataSetChanged();
		for (int i = 0; i < fileData.size(); i++) {
			if (fileData.get(i).get("check") == Boolean.TRUE) {
				fileData.get(i).remove("check");
				fileData.get(i).put("check", Boolean.FALSE);
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (MODE == OPEN || MODE == COPY || MODE == SHEAR) {
				initFile(fileCanonicalPath);
			} else if (MODE == SELECT || MODE == COPY || MODE == SHEAR) {
				cancelSelecte();
			}
			if (savePosition.size() > 0) {
				filesListView.setSelection(Integer.valueOf(savePosition
						.get(savePosition.size() - 1)));
				savePosition.remove(savePosition.size() - 1);
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initFileList(String filePath) {

		File path = new File(filePath);// getExternalStorageDirectory获取外部存储设备的路径
		File[] f = path.listFiles();// 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件
		fill(f);
		try {
			fileCanonicalPath = path.getCanonicalPath();
			setTitleButton(fileCanonicalPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initFile(String filePath) {

		String basePath = "/storage";

		if (filePath.equals(basePath)) {
			finish();
		} else {
			File files2 = new File(fileCanonicalPath);
			files2 = files2.getParentFile(); // 获取上一级目录
			File[] f = files2.listFiles(); // 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件
			fill(f);
			try {
				fileCanonicalPath = files2.getCanonicalPath();
				setTitleButton(fileCanonicalPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

    // 读取当前文件层的列表,并设置listview
	private void fill(File[] files) {

		fileNameList = new ArrayList<File>();
		fileData = new ArrayList<HashMap<String, Object>>();

		for (File file : files) {
			fileNameList.add(file);
		}
		sortArrayList();
		for (int i = 0; i < fileNameList.size(); i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap = GetFileInfo.GetFileIcon(fileNameList.get(i));
			hashMap.put("fileInfo", fileNameList.get(i).getName());
			hashMap.put("check", Boolean.FALSE);
			hashMap.put("imgInfo", "default");
			hashMap.put("fileTime", "--");
			hashMap.put("fileNum", "--");
			hashMap.put("fileSize", "--");
			fileData.add(hashMap);
			//
			//
		}
		fillListThread(fileNameList, fileData);
		fillFileInfoThread(fileNameList, fileData);
		listAdapter = new BaseAdapter() {

			public int getCount() {
				return fileData.size();
			}

			public Object getItem(int arg0) {
				return arg0;
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated catch block
				convertView = LayoutInflater.from(ListAllFileActivity.this)
						.inflate(R.layout.file_list_item, null);

				Log.v("00000", "" + position);
				ImageView file_list_image = (ImageView) convertView
						.findViewById(R.id.file_list_image_item);
				TextView file_list_text = (TextView) convertView
						.findViewById(R.id.file_list_text_item);
				TextView file_list_time = (TextView) convertView
						.findViewById(R.id.file_list_time_item);
				TextView file_list_num = (TextView) convertView
						.findViewById(R.id.file_list_num_item);
				TextView file_list_size = (TextView) convertView
						.findViewById(R.id.file_list_size_item);
				file_list_checkbox = (CheckBox) convertView
						.findViewById(R.id.file_list_checkbox_item);

				savePositionTemp[1] = position;
				if (fileData.get(position).get("imgInfo").toString() == "default"
						&& fileData.get(position).get("img") != null) {
					file_list_image.setBackgroundResource((Integer) fileData
							.get(position).get("img"));
				} else if (fileData.get(position).get("imgInfo").toString() == "ThumbnailBitmap"
						&& fileData.get(position).get("img") != null) {
					file_list_image.setImageBitmap((Bitmap) fileData.get(
							position).get("img"));
				} else if (fileData.get(position).get("imgInfo").toString() == "ThumbnailDrawable"
						&& fileData.get(position).get("img") != null) {
					file_list_image.setImageDrawable((Drawable) fileData.get(
							position).get("img"));
				}

				file_list_text.setText(fileData.get(position).get("fileInfo")
						.toString());
				if (fileData.get(position).get("fileTime") != null) {
					file_list_time.setText(fileData.get(position)
							.get("fileTime").toString());
				}
				if (fileData.get(position).get("fileNum") != null) {
					file_list_num.setText(fileData.get(position).get("fileNum")
							.toString());
				}
				if (fileData.get(position).get("fileSize") != null) {
					file_list_size.setText(fileData.get(position)
							.get("fileSize").toString());
				}
				// Log.v("MODE", String.valueOf(MODE));
				if (MODE == SELECT) {
					file_list_checkbox.setVisibility(CheckBox.VISIBLE);
				}
				file_list_checkbox.setChecked((Boolean) fileData.get(position)
						.get("check"));
				return convertView;
			}
		};
		filesListView.setAdapter(listAdapter);
		filesListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (MODE == OPEN || MODE == COPY || MODE == SHEAR) {
					File file = fileNameList.get(position);
					onListItemClickNormal(file, "");
					savePosition.add(String.valueOf(position));
				} else if (MODE == SELECT) {
					if (fileData.get(position).get("check") == Boolean.TRUE) {
						fileData.get(position).remove("check");
						fileData.get(position).put("check", Boolean.FALSE);
					} else {
						fileData.get(position).remove("check");
						fileData.get(position).put("check", Boolean.TRUE);
					}
					listAdapter.notifyDataSetChanged();
					title_selectNum_text.setText(GetFileInfo
							.GetFileNum(new File(fileCanonicalPath))
							+ "/"
							+ getCheckedList().size());
				}
			}
		});

		filesListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (MODE == OPEN) {
					Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
					long[] pattern = { 0, 20 }; // 设置震动
					vibrator.vibrate(pattern, -1);
					setListLayout();
					operateOneRelativeLayout
							.setVisibility(RelativeLayout.VISIBLE);
					list_title_button_layout
							.setVisibility(LinearLayout.INVISIBLE);
					list_title_select_layout
							.setVisibility(RelativeLayout.VISIBLE);
					listAdapter.notifyDataSetChanged();
				}
				MODE = SELECT;
				return false;
			}
		});
		filesListView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					MOVE = true;
					break;
				case MotionEvent.ACTION_UP:
					MOVE = false;
					break;
				}
				return false;
			}

		});
	}

	protected void onListItemClickNormal(File file, String set) {

		if (file.canRead()) {
			if (file.isDirectory()) {
				File[] f = file.listFiles();
				fill(f);
				try {
					fileCanonicalPath = file.getCanonicalPath();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setTitleButton(fileCanonicalPath);
			} else {
				Intent intent = OpenFile.OpenFileT(file);
				if (intent != null) {
					startActivityForResult(intent, 1);
				}
			}
			if (set == "nusave") {

			} else {
				if (fileHistory.size() < MaxHistoryNum) {
					fileHistory.add(file);
				} else if (fileHistory.size() == MaxHistoryNum) {
					fileHistory.remove(0);
					fileHistory.add(file);
				}
			}
		}
	}

	private String[] fileToStrArr(List<File> fl) {
		ArrayList<String> fnList = new ArrayList<String>();
		for (int i = 0; i < fl.size(); i++) {
			String nameString = fl.get(i).getName();
			fnList.add(nameString);
		}
		return fnList.toArray(new String[0]);// 按适当顺序（从第一个到最后一个元素）返回包含此列表中所有元素的数组；返回数组的运行时类型是指定数组的运行时类型。
	}

	private void sortArrayList() {

		List<File> listTemp = new ArrayList<File>();
		List<File> filesListTemp = new ArrayList<File>();

		for (int i = 0; i < fileNameList.size(); i++) {
			if (fileNameList.get(i).isDirectory()) {
				listTemp.add(fileNameList.get(i));
			}
		}
		for (int i = 0; i < fileNameList.size(); i++) {
			if (fileNameList.get(i).isFile()) {
				filesListTemp.add(fileNameList.get(i));
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

		fileNameList.clear();
		for (int i = 0; i < listTemp.size(); i++) {
			fileNameList.add(listTemp.get(i));
		}
		for (int i = 0; i < filesListTemp.size(); i++) {
			fileNameList.add(filesListTemp.get(i));
		}

	}

	private void batchRename(final List<File> filesHandleList) {
		View convertView = LayoutInflater.from(ListAllFileActivity.this)
				.inflate(R.layout.batch_rename, null);
		selectView.RadioButtonOne = (RadioButton) convertView
				.findViewById(R.id.RadioButtonOne);
		selectView.RadioButtonTwo = (RadioButton) convertView
				.findViewById(R.id.RadioButtonTwo);
		selectView.RadioButtonThree = (RadioButton) convertView
				.findViewById(R.id.RadioButtonThree);
		selectView.RadioButtonFour = (RadioButton) convertView
				.findViewById(R.id.RadioButtonFour);
		selectView.RadioButtonFive = (RadioButton) convertView
				.findViewById(R.id.RadioButtonFive);
		selectView.nameSpinner = (Spinner) convertView
				.findViewById(R.id.nameSpinner);
		selectView.numberSpinner = (Spinner) convertView
				.findViewById(R.id.numberSpinner);
		selectView.separatorSpinner = (Spinner) convertView
				.findViewById(R.id.separatorSpinner);
		selectView.nameEditText = (EditText) convertView
				.findViewById(R.id.nameEditText);
		selectView.separatorEditText = (EditText) convertView
				.findViewById(R.id.separatorEditText);
		selectView.numberEditText = (EditText) convertView
				.findViewById(R.id.numEditText);
		Builder builder = new Builder(ListAllFileActivity.this);

		List<String> list_1 = new ArrayList<String>();
		list_1.add("文件");
		list_1.add("文件夹");
		list_1.add("图片");
		list_1.add("视频");
		list_1.add("文本");
		list_1.add("音乐");
		list_1.add("照片");
		list_1.add("录音");
		final ArrayAdapter<String> adapter_1 = new ArrayAdapter<String>(this,
				R.layout.my_spinner, list_1);
		adapter_1.setDropDownViewResource(R.layout.spinner_list);
		selectView.nameSpinner.setAdapter(adapter_1);
		selectView.nameSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						selectView.fileName = adapter_1.getItem(arg2)
								.toString();
					}

					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		List<String> list_2 = new ArrayList<String>();
		list_2.add("数字(1,2,3...)");
		list_2.add("字母小写(a,b,c...)");
		list_2.add("字母大写(A,B,C...)");
		final ArrayAdapter<String> adapter_2 = new ArrayAdapter<String>(this,
				R.layout.my_spinner, list_2);
		adapter_2.setDropDownViewResource(R.layout.spinner_list);
		selectView.numberSpinner.setAdapter(adapter_2);
		selectView.numberSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						selectView.fileNumber = adapter_2.getItem(arg2)
								.toString();
					}

					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		List<String> list_3 = new ArrayList<String>();
		list_3.add("_");
		list_3.add("-");
		list_3.add("~");
		list_3.add("*");
		list_3.add("#");
		final ArrayAdapter<String> adapter_3 = new ArrayAdapter<String>(this,
				R.layout.my_spinner, list_3);
		adapter_3.setDropDownViewResource(R.layout.spinner_list);
		selectView.separatorSpinner.setAdapter(adapter_3);
		selectView.separatorSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						selectView.separator = adapter_3.getItem(arg2)
								.toString();
					}

					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		builder.setTitle("重命名此" + filesHandleList.size() + "项");
		builder.setView(convertView);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {

						if (selectView.RadioButtonOne.isChecked()) {
							fileOperationThread(filesHandleList, "");
						} else if (selectView.RadioButtonTwo.isChecked()) {

							if ("".equals(selectView.nameEditText.getText()
									.toString())) {

								Toast.makeText(ListAllFileActivity.this,
										"请输入文件名", Toast.LENGTH_LONG).show();

							} else if ("".equals(selectView.numberEditText
									.getText().toString())) {

								Toast.makeText(ListAllFileActivity.this,
										"请输入序号", Toast.LENGTH_LONG).show();

							} else {
								fileOperationThread(filesHandleList, "");
							}
						} else {
							Toast.makeText(ListAllFileActivity.this,
									"请选择一种处理方式", Toast.LENGTH_LONG).show();
						}
					}
				});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

	public void fileOperationThread(final List<File> filesHandleList,
			final String filePath) {
		progressDialog = ProgressDialog.show(ListAllFileActivity.this, null,
				"处理中...");

		new Thread(new Runnable() {

			public void run() {

				boolean tOrF = false;
				int result = MessengeHandle.FAIL;
				if (MODE == SELECT) {
					tOrF = batchRemane.setInformation(filesHandleList,
							selectView);
				} else if (MODE == COPY) {
					result = fileHandle.fileCopy(filesHandleList, filePath);
				} else if (MODE == SHEAR) {
					result = fileHandle.fileShear(filesHandleList, filePath);
				} else if (MODE == DELETE) {
					result = fileHandle.fileDelete(filesHandleList);
				}
				if (tOrF) {
					fileOperationHandler.sendEmptyMessage(1);
				} else if (result == MessengeHandle.SUCCESSED
						&& (MODE == COPY || MODE == SHEAR)) {
					fileOperationHandler.sendEmptyMessage(2);
				} else if (result == MessengeHandle.SUCCESSED && MODE == DELETE) {
					fileOperationHandler.sendEmptyMessage(3);
				} else {
					fileOperationHandler.sendEmptyMessage(0);
				}

			}
		}).start();
	}

	public void fillListThread(final List<File> fileNameList,
			final List<HashMap<String, Object>> fileData) {

		new Thread(new Runnable() {

			public void run() {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_4444;
				for (int i = 0; i < fileNameList.size(); i++) {
					// TODO Auto-generated catch block
					// try {
					// Thread.sleep(200);
					// } catch (InterruptedException e) {
					// e.printStackTrace();
					// }
					if (fileNameList.get(i).isFile()
							&& fileData.get(i).get("imgInfo").toString() == "default"
							&& (FileType.fileTypeJudge(fileNameList.get(i)) == "jpg"
									|| FileType.fileTypeJudge(fileNameList
											.get(i)) == "png" || FileType
									.fileTypeJudge(fileNameList.get(i)) == "bmp")) {
						try {
							savePositionTemp[0] = savePositionTemp[1];
							Thread.sleep(20);
							if (savePositionTemp[0] != savePositionTemp[1]
									&& MOVE) {
								Thread.sleep(300);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Bitmap bitmapImage = BitmapFactory
								.decodeFile(fileNameList.get(i)
										.getAbsolutePath().toString());
						options.inSampleSize = bitmapImage.getWidth() / 70;
						fileData.get(i).remove("img");
						fileData.get(i)
								.put("img",
										BitmapFactory.decodeFile(fileNameList
												.get(i).getAbsolutePath()
												.toString(), options));
						fileData.get(i).remove("imgInfo");
						fileData.get(i).put("imgInfo", "ThumbnailBitmap");
						fileOperationHandler.sendEmptyMessage(4);
					}
					if (fileNameList.get(i).isFile()
							&& fileData.get(i).get("imgInfo").toString() == "default"
							&& FileType.fileTypeJudge(fileNameList.get(i)) == "apk") {
						try {
							savePositionTemp[0] = savePositionTemp[1];
							Thread.sleep(20);
							if (savePositionTemp[0] != savePositionTemp[1]
									&& MOVE) {
								Thread.sleep(300);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						fileData.get(i).remove("img");
						fileData.get(i).put(
								"img",
								GetThumbnail.getApkIcon(fileNameList.get(i)
										.getAbsolutePath().toString(),
										ListAllFileActivity.this));
						fileData.get(i).remove("imgInfo");
						fileData.get(i).put("imgInfo", "ThumbnailDrawable");
						fileOperationHandler.sendEmptyMessage(4);
					}
				}
			}
		}).start();
	}

	public void fillFileInfoThread(final List<File> fileNameList,
			final List<HashMap<String, Object>> fileData) {

		new Thread(new Runnable() {

			public void run() {
				for (int i = 0; i < fileNameList.size(); i++) {
					fileData.get(i).remove("fileTime");
					fileData.get(i).put(
							"fileTime",
							GetFileInfo.GetFileDayTime(fileNameList.get(i))
									+ "  "
									+ GetFileInfo.GetFileHoursTime(fileNameList
											.get(i)));
					fileData.get(i).remove("fileNum");
					fileData.get(i).put("fileNum",
							GetFileInfo.GetFileNum(fileNameList.get(i)));
					if (fileNameList.get(i).isFile()) {
						fileData.get(i).remove("fileSize");
						fileData.get(i).put("fileSize",
								GetFileInfo.GetFileSize(fileNameList.get(i)));
					}
					fileOperationHandler.sendEmptyMessage(4);
				}
			}
		}).start();
	}

	final Handler fileOperationHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 1:
				progressDialog.dismiss();
				Toast.makeText(ListAllFileActivity.this, "批处理成功",
						Toast.LENGTH_SHORT).show();
				cancelSelecte();
				refreshList();
				break;
			case 2:
				progressDialog.dismiss();
				Toast.makeText(ListAllFileActivity.this, "粘贴成功",
						Toast.LENGTH_SHORT).show();
				MODE = PASTE;
				cancelSelecte();
				refreshList();
				break;
			case 3:
				progressDialog.dismiss();
				Toast.makeText(ListAllFileActivity.this, "删除成功",
						Toast.LENGTH_SHORT).show();
				MODE = PASTE;
				cancelSelecte();
				refreshList();
				break;
			case 4:
				// refreshList();
				listAdapter.notifyDataSetChanged();
				break;
			default:
				progressDialog.dismiss();
				Toast.makeText(ListAllFileActivity.this, "处理失败",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};

	private void renameOne(final List<File> filesHandleList) {
		final EditText DialogView = new EditText(this);
		Builder builder = new Builder(ListAllFileActivity.this);

		DialogView.setText(filesHandleList.get(0).getName().toString());
		builder.setTitle("重命名此" + filesHandleList.size() + "\"项？");
		builder.setView(DialogView);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						fileHandle.fileRename(filesHandleList, DialogView
								.getText().toString());
						Toast.makeText(ListAllFileActivity.this, "重命名成功",
								Toast.LENGTH_LONG).show();
						cancelSelecte();
						refreshList();
					}
				});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

    // 刷新列表
	public void refreshList() {
		String filePath = fileCanonicalPath;
		File fileT = new File(filePath);
		if (fileT.isDirectory()) {
			File[] filesT = fileT.listFiles();
			fill(filesT);
		}
	}

    // 获取选择列表
	private List<File> getCheckedList() {
		List<File> filesHandleList = new ArrayList<File>();
		for (int i = 0; i < fileData.size(); i++) {
			if (fileData.get(i).get("check") == Boolean.TRUE) {
				filesHandleList.add(fileNameList.get(i));
			}
		}
		return filesHandleList;
	}

    // 根据文件路径获取文件名
	private String getFileName(String filePath) {
		String filePathT = filePath.substring(filePath.lastIndexOf('/') + 1,
				filePath.length() - 1);
		return filePathT;
	}

    // 设置标题栏按钮
	private void setTitleButton(String filePath) {
		filePathList = fileHandle.getBackDirectoryPath(filePath);

		if (filePathList.length == 1) {
			titleButton.backDirectoryOne.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryOne.setText(getFileName(filePathList[0]));
			titleButton.backDirectoryTwo.setVisibility(Button.INVISIBLE);
			titleButton.backDirectoryThree.setVisibility(Button.INVISIBLE);
			titleButton.backDirectoryFour.setVisibility(Button.INVISIBLE);
		} else if (filePathList.length == 2) {
			titleButton.backDirectoryOne.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryTwo.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryOne.setText(getFileName(filePathList[0]));
			titleButton.backDirectoryTwo.setText(getFileName(filePathList[1]));
			titleButton.backDirectoryThree.setVisibility(Button.INVISIBLE);
			titleButton.backDirectoryFour.setVisibility(Button.INVISIBLE);
		} else if (filePathList.length == 3) {
			titleButton.backDirectoryOne.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryTwo.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryThree.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryOne.setText(getFileName(filePathList[0]));
			titleButton.backDirectoryTwo.setText(getFileName(filePathList[1]));
			titleButton.backDirectoryThree
					.setText(getFileName(filePathList[2]));
			titleButton.backDirectoryFour.setVisibility(Button.INVISIBLE);
		} else if (filePathList.length >= 4) {
			titleButton.backDirectoryOne.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryTwo.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryThree.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryFour.setVisibility(Button.VISIBLE);
			titleButton.backDirectoryOne.setText(getFileName(filePathList[0]));
			titleButton.backDirectoryTwo.setText(getFileName(filePathList[1]));
			titleButton.backDirectoryThree
					.setText(getFileName(filePathList[2]));
			titleButton.backDirectoryFour.setText(getFileName(filePathList[3]));
		}
	}

    // 显示浏览记录
	private void fileHistory(final List<File> fileList) {
		View convertView = LayoutInflater.from(ListAllFileActivity.this)
				.inflate(R.layout.file_history, null);
		ListView listView = (ListView) convertView
				.findViewById(R.id.file_history_list);
		final Builder builder = new Builder(
				ListAllFileActivity.this);

		final List<HashMap<String, Object>> fileHistoryData = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < storageList.size(); i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("img", R.drawable.storage_icon);
			hashMap.put("fileInfo", storageList.get(i).getName());
			hashMap.put("imgInfo", "default");
			hashMap.put("filePath", "储存器");
			fileHistoryData.add(hashMap);
		}

		for (int i = 0; i < fileList.size(); i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap = GetFileInfo.GetFileIcon(fileList.get(i));
			hashMap.put("fileInfo", fileList.get(i).getName());
			hashMap.put("imgInfo", "default");
			hashMap.put("filePath", fileList.get(i).toString());
			fileHistoryData.add(hashMap);
		}
		BaseAdapter historyListAdapter = new BaseAdapter() {

			public int getCount() {
				return fileHistoryData.size();
			}

			public Object getItem(int arg0) {
				return arg0;
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = LayoutInflater.from(ListAllFileActivity.this)
						.inflate(R.layout.file_list_item, null);
				ImageView file_list_image = (ImageView) convertView
						.findViewById(R.id.file_list_image_item);
				TextView file_list_text = (TextView) convertView
						.findViewById(R.id.file_list_text_item);
				TextView file_list_path = (TextView) convertView
						.findViewById(R.id.file_list_time_item);

				file_list_image.setBackgroundResource((Integer) fileHistoryData
						.get(position).get("img"));
				file_list_text.setText(fileHistoryData.get(position)
						.get("fileInfo").toString());
				file_list_path.setText(fileHistoryData.get(position)
						.get("filePath").toString());
				return convertView;
			}
		};
		listView.setAdapter(historyListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				File file;
				if (position < storageList.size()) {
					file = storageList.get(position);
				} else {
					file = fileList.get(position - storageList.size());
				}
				onListItemClickNormal(file, "nusave");
			}
		});

		builder.setView(convertView);
		builder.setPositiveButton("清空记录",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						fileList.clear();
					}
				});
		builder.create().show();
	}

    // 设置ListLayout高度
	private void setListLayout() {
		reHeight = list_layout.getHeight();
		height = operate_layout.getHeight();
		list_layout.getLayoutParams().height = reHeight - height;
		list_layout.requestLayout();
	}

    // 重置ListLayout高度
	private void reSetListLayout() {
		list_layout.getLayoutParams().height = reHeight;
		list_layout.requestLayout();
	}

    // 设置本地储存器列表
	private void setMyStorages() {
		MyStorage myStorage = new MyStorage(ListAllFileActivity.this);
		String[] storage = myStorage.getVolumePaths();
		storageList = new ArrayList<File>();
		for (int i = 0; i < storage.length; i++) {
			File file = new File(storage[i]);
			storageList.add(file);
		}
	}

    // 设置操作栏按键监听
	private void setButtonListener() {
		titleButton.backDirectoryOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (filePathList.length == 1) {
					fileHistory(fileHistory);
				} else {
					File file = new File(filePathList[0]);
					File[] f = file.listFiles();
					fill(f);
					try {
						fileCanonicalPath = file.getCanonicalPath();
					} catch (IOException e) {
						e.printStackTrace();
					}
					setTitleButton(fileCanonicalPath);
				}
			}
		});
		titleButton.backDirectoryTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (filePathList.length == 2) {
					fileHistory(fileHistory);
				} else {
					File file = new File(filePathList[1]);
					File[] f = file.listFiles();
					fill(f);
					try {
						fileCanonicalPath = file.getCanonicalPath();
					} catch (IOException e) {
						e.printStackTrace();
					}
					setTitleButton(fileCanonicalPath);
				}
			}
		});
		titleButton.backDirectoryThree
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (filePathList.length == 3) {
							fileHistory(fileHistory);
						} else {
							File file = new File(filePathList[2]);
							File[] f = file.listFiles();
							fill(f);
							try {
								fileCanonicalPath = file.getCanonicalPath();
							} catch (IOException e) {
								e.printStackTrace();
							}
							setTitleButton(fileCanonicalPath);
						}
					}
				});
		titleButton.backDirectoryFour.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (filePathList.length >= 4) {
					fileHistory(fileHistory);
				} else {
					File file = new File(filePathList[3]);
					File[] f = file.listFiles();
					fill(f);
					try {
						fileCanonicalPath = file.getCanonicalPath();
					} catch (IOException e) {
						e.printStackTrace();
					}
					setTitleButton(fileCanonicalPath);
				}
			}
		});

		title_cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MODE = SELECT;
				cancelSelecte();
			}
		});

		operate_newfileOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newFile("cancelSelecte");
			}

		});

		operate_copy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MODE = COPY;
				pasteFileList = getCheckedList();
				operateTwoRelativeLayout.setVisibility(RelativeLayout.VISIBLE);
				cancelSelecte();
			}

		});

		operate_shear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MODE = SHEAR;
				pasteFileList = getCheckedList();
				operateTwoRelativeLayout.setVisibility(RelativeLayout.VISIBLE);
				cancelSelecte();
			}

		});

		operate_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteHandle();
			}

		});

		operate_rename.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				renameHandle();
			}

		});

		operate_checkAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAll();
			}

		});

		operate_getInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DisplayFileInfo displayFileInfo = new DisplayFileInfo(
						ListAllFileActivity.this);
				displayFileInfo.DisplayInfo(getCheckedList());
			}
		});

		operate_paste.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String filePath = fileCanonicalPath;
				fileOperationThread(pasteFileList, filePath);
				cancelSelecte();
			}

		});

		operate_newfileTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newFile("");
			}

		});

		operate_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MODE = SELECT;
				cancelSelecte();
			}

		});
	}

}
