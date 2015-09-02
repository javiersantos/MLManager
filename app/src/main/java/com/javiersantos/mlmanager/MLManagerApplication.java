package com.javiersantos.mlmanager;

import android.app.Application;

import com.batch.android.Batch;
import com.batch.android.Config;
import com.javiersantos.mlmanager.utils.AppPreferences;

public class MLManagerApplication extends Application {
    private static AppPreferences sAppPreferences;
    private static Boolean isPro = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppPreferences = new AppPreferences(this);
        Batch.setConfig(new Config(Keys.getBatchAPIKey()));

    }

    public static AppPreferences getAppPreferences() {
        return sAppPreferences;
    }

    /**
     * Retrieve ML Manager Pro
     * @return true for ML Manager Pro, false otherwise
     */
    public static Boolean isPro() {
        return isPro;
    }

    public static void setPro(Boolean res) {
        isPro = res;
    }

    public static String getProPackage() {
        return "com.javiersantos.mlmanagerpro";
    }
}
