package com.limingyilr.easyfilespro.dao;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class GetThumbnail {

    //获取图标缩略图
	public static Bitmap getThumbnail(File file) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		Log.v("getThumbnail-1", "successed");
		Bitmap bitmapImage = BitmapFactory.decodeFile(file.getAbsolutePath().toString(), options);
		Log.v("getThumbnail-2", "successed");
		options.inJustDecodeBounds = false;
		options.inSampleSize = bitmapImage.getWidth() / 60;
		Log.v("getThumbnail-3", "successed");
		return BitmapFactory.decodeFile(file.getAbsolutePath().toString(), options);
	}

    //获取APK的图标
	public static Drawable getApkIcon(String apkPath, Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }

}
