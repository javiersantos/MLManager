package com.javiersantos.mlmanager;

import android.app.Application;

import com.javiersantos.mlmanager.utils.AppPreferences;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;

public class MLManagerApplication extends Application {
    private static AppPreferences sAppPreferences;
    private static boolean isPro;

    @Override
    public void onCreate() {
        super.onCreate();

        // Load Shared Preference
        sAppPreferences = new AppPreferences(this);

        // Check if there is the Pro version
        isPro = this.getPackageName().equals(getProPackage());

        // Register custom fonts like this (or also provide a font definition file)
        Iconics.registerFont(new GoogleMaterial());
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
