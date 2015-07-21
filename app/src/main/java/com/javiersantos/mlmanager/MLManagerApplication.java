package com.javiersantos.mlmanager;

import android.app.Application;

import com.javiersantos.mlmanager.utils.AppPreferences;

public class MLManagerApplication extends Application {

    private static AppPreferences sAppPreferences;

    @Override
    public void onCreate() {
        sAppPreferences = new AppPreferences(this);
        super.onCreate();
    }

    public static AppPreferences getAppPreferences() {
        return sAppPreferences;
    }

    /**
     * Retrieve ML Manager Pro
     * @return true for ML Manager Pro, false otherwise
     */
    public static Boolean isPro() {
        return false;
    }

    public static String getProPackage() {
        return "com.javiersantos.mlmanagerpro";
    }
}
