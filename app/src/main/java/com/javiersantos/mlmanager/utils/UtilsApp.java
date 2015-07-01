package com.javiersantos.mlmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.MenuItem;

import com.javiersantos.mlmanager.AppInfo;
import com.javiersantos.mlmanager.MLManagerApplication;
import com.javiersantos.mlmanager.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class UtilsApp {
    // Load Settings
    private static AppPreferences appPreferences;

    public static File getAppFolder() {
        return new File(Environment.getExternalStorageDirectory() + "/MLManager");
    }

    public static Boolean copyFile(AppInfo appInfo) {
        Boolean res = false;

        File initialFile = new File(appInfo.getSource());
        File finalFile = getOutputFilename(appInfo);

        try {
            FileUtils.copyFile(initialFile, finalFile);
            res = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String getAPKFilename(AppInfo appInfo) {
        appPreferences = MLManagerApplication.getAppPreferences();
        String res;

        switch (appPreferences.getCustomFilename()) {
            case "1":
                res = appInfo.getAPK() + "_" + appInfo.getVersion();
                break;
            case "2":
                res = appInfo.getName() + "_" + appInfo.getVersion();
                break;
            case "4":
                res = appInfo.getName();
                break;
            default:
                res = appInfo.getAPK();
                break;
        }

        return res;
    }

    public static File getOutputFilename(AppInfo appInfo) {
        return new File(getAppFolder().getPath() + "/" + getAPKFilename(appInfo) + ".apk");
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

    public static Intent goToGooglePlus(String id) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://plus.google.com/" + id));

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

    public static Intent getShareIntent(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    public static Boolean isAppFavorite(String apk, Set<String> appFavorites) {
        Boolean res = false;
        if (appFavorites.contains(apk)) {
           res = true;
        }

        return res;
    }

    public static void setAppFavorite(Context context, MenuItem menuItem, Boolean isFavorite) {
        if (isFavorite) {
            menuItem.setIcon(context.getResources().getDrawable(R.drawable.ic_star_white));
        } else {
            menuItem.setIcon(context.getResources().getDrawable(R.drawable.ic_star_border_white));
        }
    }

}
