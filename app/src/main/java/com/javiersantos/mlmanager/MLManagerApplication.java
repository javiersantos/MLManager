package com.javiersantos.mlmanager;

import android.app.Application;

import com.javiersantos.mlmanager.utils.AppPreferences;

/**
 * Created by vijay.rawat01 on 6/21/15.
 */
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
}
