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

    public int getKeyPrimaryColorPref() {
        return sharedPreferences.getInt(KeyPrimaryColor, context.getResources().getColor(R.color.primary));
    }
    public void setKeyPrimaryColorPref(Integer res) {
        editor.putInt(KeyPrimaryColor, res);
        editor.commit();
    }

    public int getKeyFABColorPref() {
        return sharedPreferences.getInt(KeyFABColor, context.getResources().getColor(R.color.pink));
    }
    public void setKeyFABColorPref(Integer res) {
        editor.putInt(KeyFABColor, res);
        editor.commit();
    }

}
