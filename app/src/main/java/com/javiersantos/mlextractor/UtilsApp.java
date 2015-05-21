package com.javiersantos.mlextractor;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class UtilsApp {

    public static File copyFile(String apk, String source) {
        File initialFile = new File(source);
        File finalFile = new File(Environment.getExternalStorageDirectory() + "/MLExtractor/" + apk + ".apk");

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

}
