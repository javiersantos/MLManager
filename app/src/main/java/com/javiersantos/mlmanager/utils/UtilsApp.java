package com.javiersantos.mlmanager.utils;

import android.graphics.Color;
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

    public static int darker (int color, double factor) {
        int a = Color.alpha( color );
        int r = Color.red(color);
        int g = Color.green( color );
        int b = Color.blue(color);

        return Color.argb(a, Math.max( (int)(r * factor), 0 ), Math.max( (int)(g * factor), 0 ), Math.max( (int)(b * factor), 0 ) );
    }

}
