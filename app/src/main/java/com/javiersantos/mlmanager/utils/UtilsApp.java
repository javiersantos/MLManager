package com.javiersantos.mlmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class UtilsApp {

    public static File getAppFolder() {
        return new File(Environment.getExternalStorageDirectory() + "/MLManager");
    }

    public static File copyFile(String apk, String source) {
        File initialFile = new File(source);
        File finalFile = new File(getAppFolder().getPath() + "/" + apk + ".apk");

        try {
            FileUtils.copyFile(initialFile, finalFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalFile;
    }

    public static Boolean existCacheFolder(String data) {
        File f = new File(data + "/cache");
        if (f.exists() && f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean removeCacheFolder(String data) {

        return false;
    }

    public static long getCacheFolderSize(String data) {
        long size = 0;
        File f = new File(data + "/cache");
        Log.i("App", f.toString());
        File[] files = f.listFiles();
        for (File file : files) {
            size += file.length();
        }

        Log.i("App", Long.toString(size));
        return size;
    }

    public static Boolean deleteAppFiles() {
        Boolean res = false;
        File f = getAppFolder();
        if (f.exists() && f.isDirectory()) {
            File[] files = f.listFiles();
            for (File file : files) {
                file.delete();
            }
            if (f.listFiles().length == 0) {
                return true;
            }
        }
        return res;
    }

    public static Intent goToGooglePlay(String id) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + id));

        return intent;
    }

    public static String getAppVersionName(Context context) {
        String res = "0.0.0.0";
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static int getAppVersionCode(Context context) {
        int res = 0;
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

}
