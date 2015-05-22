package com.javiersantos.mlmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String KeyStartDelete = "prefStartDelete";

    public AppPreferences(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
    }

    public Boolean getStartDeletePref() {
        return sharedPreferences.getBoolean(KeyStartDelete, false);
    }
    public void setStartDeletePref(Boolean res) {
        editor.putBoolean(KeyStartDelete, res);
        editor.commit();
    }

}
