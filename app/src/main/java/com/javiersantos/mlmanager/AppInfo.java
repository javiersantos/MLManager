package com.javiersantos.mlmanager;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String name;
    private String apk;
    private String version;
    private String source;
    private String data;
    private Drawable icon;

    public AppInfo(String name, String apk, String version, String source, String data, Drawable icon) {
        this.name = name;
        this.apk = apk;
        this.version = version;
        this.source = source;
        this.data = data;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getAPK() {
        return apk;
    }

    public String getVersion() {
        return version;
    }

    public String getSource() {
        return source;
    }

    public String getData() {
        return data;
    }

    public Drawable getIcon() {
        return icon;
    }

}
