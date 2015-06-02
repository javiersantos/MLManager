package com.javiersantos.mlmanager;

import android.graphics.drawable.Drawable;

public class AppInfo {
    public String name;
    public String apk;
    public String version;
    public String source;
    public String data;
    public Drawable icon;

    public AppInfo(String name, String apk, String version, String source, String data, Drawable icon) {
        this.name = name;
        this.apk = apk;
        this.version = version;
        this.source = source;
        this.data = data;
        this.icon = icon;
    }

}
