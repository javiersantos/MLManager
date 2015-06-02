package com.javiersantos.mlmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.javiersantos.mlmanager.R;

public class AppPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String KeyStartDelete = "prefStartDelete";
    public static final String KeyPrimaryColor = "prefPrimaryColor";
    public static final String KeyFABColor = "prefFABColor";
    public static final String KeyFABShow = "prefFABShow";
    public static final String KeyNavigationBlack = "prefNavigationBlack";
    public static final String KeyCustomFilename = "prefCustomFilename";

    public AppPreferences(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public Boolean getStartDeletePref() {
        return sharedPreferences.getBoolean(KeyStartDelete, false);
    }
    public void setStartDeletePref(Boolean res) {
        editor.putBoolean(KeyStartDelete, res);
        editor.commit();
    }

    public int getPrimaryColorPref() {
        return sharedPreferences.getInt(KeyPrimaryColor, context.getResources().getColor(R.color.primary));
    }
    public void setPrimaryColorPref(Integer res) {
        editor.putInt(KeyPrimaryColor, res);
        editor.commit();
    }

    public int getFABColorPref() {
        return sharedPreferences.getInt(KeyFABColor, context.getResources().getColor(R.color.fab));
    }
    public void setFABColorPref(Integer res) {
        editor.putInt(KeyFABColor, res);
        editor.commit();
    }

    public Boolean getNavigationBlackPref() {
        return sharedPreferences.getBoolean(KeyNavigationBlack, false);
    }
    public void setNavigationBlackPref(Boolean res) {
        editor.putBoolean(KeyNavigationBlack, res);
        editor.commit();
    }

    public Boolean getFABShowPref() {
        return sharedPreferences.getBoolean(KeyFABShow, false);
    }
    public void setFABShowPref(Boolean res) {
        editor.putBoolean(KeyFABShow, res);
        editor.commit();
    }

    public String getCustomFilename() {
        return sharedPreferences.getString(KeyCustomFilename, "1");
    }
    public void setCustomFilename(String res) {
        editor.putString(KeyCustomFilename, res);
        editor.commit();
    }

}
