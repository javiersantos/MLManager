package com.javiersantos.mlmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.javiersantos.mlmanager.MLManagerApplication;
import com.javiersantos.mlmanager.R;
import com.javiersantos.mlmanager.utils.AppPreferences;
import com.javiersantos.mlmanager.utils.UtilsApp;
import com.javiersantos.mlmanager.utils.UtilsUI;

import yuku.ambilwarna.widget.AmbilWarnaPreference;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    // Load Settings
    private AppPreferences appPreferences;
    private Toolbar toolbar;

    // Settings variables
    private SharedPreferences prefs;
    private Preference prefVersion, prefDeleteAll, prefDefaultValues, prefNavigationBlack;
    private AmbilWarnaPreference prefPrimaryColor, prefFABColor;
    private ListPreference prefCustomFilename, prefSortMode;
    private String versionName;
    private int versionCode;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        this.context = this;
        this.appPreferences = MLManagerApplication.getAppPreferences();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        prefVersion = findPreference("prefVersion");
        prefPrimaryColor = (AmbilWarnaPreference) findPreference("prefPrimaryColor");
        prefFABColor = (AmbilWarnaPreference) findPreference("prefFABColor");
        prefDeleteAll = findPreference("prefDeleteAll");
        prefDefaultValues = findPreference("prefDefaultValues");
        prefNavigationBlack = findPreference("prefNavigationBlack");
        prefCustomFilename = (ListPreference) findPreference("prefCustomFilename");
        prefSortMode = (ListPreference) findPreference("prefSortMode");

        setInitialConfiguration();

        versionName = UtilsApp.getAppVersionName(context);
        versionCode = UtilsApp.getAppVersionCode(context);

        prefVersion.setTitle(getResources().getString(R.string.app_name) + " v" + versionName + " (" + versionCode + ")");
        prefVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
                return true;
            }
        });

        // prefCustomFilename
        setCustomFilenameSummary();

        // prefSortMode
        setSortModeSummary();

        // prefDeleteAll
        prefDeleteAll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                prefDeleteAll.setSummary(R.string.deleting);
                prefDeleteAll.setEnabled(false);
                Boolean deleteAll = UtilsApp.deleteAppFiles();
                if (deleteAll) {
                    prefDeleteAll.setSummary(R.string.deleting_done);
                } else {
                    prefDeleteAll.setSummary(R.string.deleting_error);
                }
                prefDeleteAll.setEnabled(true);
                return true;
            }
        });

        // prefDefaultValues
        prefDefaultValues.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                appPreferences.setPrimaryColorPref(getResources().getColor(R.color.primary));
                appPreferences.setFABColorPref(getResources().getColor(R.color.fab));
                return true;
            }
        });

    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_settings, new LinearLayout(this), false);
        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        //TODO Toolbar should load the default style in XML (white title and back arrow), but doesn't happen
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content_wrapper);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);
        getWindow().setContentView(contentView);

    }

    private void setInitialConfiguration() {
        toolbar.setTitle(getResources().getString(R.string.action_settings));

        // Android 5.0+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));
            toolbar.setBackgroundColor(appPreferences.getPrimaryColorPref());
            if (!appPreferences.getNavigationBlackPref()) {
                getWindow().setNavigationBarColor(appPreferences.getPrimaryColorPref());
            }
        }

        // Pre-Lollipop devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            prefPrimaryColor.setEnabled(false);
            prefNavigationBlack.setEnabled(false);
            prefNavigationBlack.setDefaultValue(true);
        }
    }

    private void setCustomFilenameSummary() {
        int filenameValue = new Integer(appPreferences.getCustomFilename())-1;
        prefCustomFilename.setSummary(getResources().getStringArray(R.array.filenameEntries)[filenameValue]);
    }

    private void setSortModeSummary() {
        int sortValue = new Integer(appPreferences.getSortMode())-1;
        prefSortMode.setSummary(getResources().getStringArray(R.array.sortEntries)[sortValue]);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if (pref == prefCustomFilename) {
            setCustomFilenameSummary();
        } else if (pref == prefSortMode) {
            setSortModeSummary();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
    }

}
