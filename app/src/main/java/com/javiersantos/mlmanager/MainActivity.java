package com.javiersantos.mlmanager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.javiersantos.mlmanager.adapters.AppAdapter;
import com.javiersantos.mlmanager.listeners.HidingScrollListener;
import com.javiersantos.mlmanager.utils.AppPreferences;
import com.javiersantos.mlmanager.utils.UtilsApp;
import com.javiersantos.mlmanager.utils.UtilsUI;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // Load Settings
    AppPreferences appPreferences;

    // General variables
    private List<String> appListName = new ArrayList<String>();
    private List<String> appListApk = new ArrayList<String>();
    private List<String> appListVersion = new ArrayList<String>();
    private List<String> appListSource = new ArrayList<String>();
    private List<String> appListData = new ArrayList<String>();
    private List<Drawable> appListIcon = new ArrayList<Drawable>();

    // Configuration variables
    private Boolean doubleBackToExitPressedOnce = false;
    Toolbar toolbar;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.appPreferences = new AppPreferences(getApplicationContext());

        setInitialConfiguration();
        setAppDir();

        recyclerView = (RecyclerView) findViewById(R.id.appList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        getInstalledApps();
        AppAdapter appAdapter = new AppAdapter(createList(appListName, appListApk, appListVersion, appListSource, appListData, appListIcon), this);
        recyclerView.setAdapter(appAdapter);

        setNavigationDrawer(appAdapter);
        setFAB();

    }

    private void setInitialConfiguration() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));
            toolbar.setBackgroundColor(appPreferences.getPrimaryColorPref());
            if (!appPreferences.getNavigationBlackPref()) {
                getWindow().setNavigationBarColor(appPreferences.getPrimaryColorPref());
            }
        }
    }

    private void setNavigationDrawer(AppAdapter appAdapter) {
        UtilsUI.setNavigationDrawer(this, getApplicationContext(), toolbar, appAdapter);
    }

    private void setFAB() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (appPreferences.getFABShowPref()) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings));
            fab.setBackgroundColor(appPreferences.getFABColorPref());
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
                }
            });

            recyclerView.setOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    fab.hide();
                }

                @Override
                public void onShow() {
                    fab.show();
                }
            });
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }

    private void getInstalledApps() {
        final PackageManager packageManager = getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        Collections.sort(packages, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo p1, PackageInfo p2) {
                return packageManager.getApplicationLabel(p1.applicationInfo).toString().compareTo(packageManager.getApplicationLabel(p2.applicationInfo).toString());
            }
        });
        for(PackageInfo packageInfo : packages) {
            if(packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                // Non System Apps
                Log.i("App", packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                Log.i("App", packageInfo.packageName);
                Log.i("App", packageInfo.versionName);
                Log.i("App", packageInfo.applicationInfo.packageName);
                Log.i("App", packageInfo.applicationInfo.sourceDir);
                Log.i("App", packageInfo.applicationInfo.dataDir);
                appListName.add(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                appListApk.add(packageInfo.packageName);
                appListVersion.add(packageInfo.versionName);
                appListSource.add(packageInfo.applicationInfo.sourceDir);
                appListData.add(packageInfo.applicationInfo.dataDir);
                appListIcon.add(packageManager.getApplicationIcon(packageInfo.applicationInfo));
            } else {
                // System Apps
            }
        }
    }

    private void setAppDir() {
        File appDir = UtilsApp.getAppFolder();
        if(!appDir.exists()) {
            appDir.mkdir();
        }
    }

    private List<AppInfo> createList(List<String> apps, List<String> apks, List<String> versions, List<String> sources, List<String> data, List<Drawable> icons) {
        List<AppInfo> res = new ArrayList<AppInfo>();
        for (int i=0; i < apps.size(); i++) {
            AppInfo appInfo = new AppInfo(apps.get(i), apks.get(i), versions.get(i), sources.get(i), data.get(i), icons.get(i));
            res.add(appInfo);
        }

        return res;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.tap_exit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
