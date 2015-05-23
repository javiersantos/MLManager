package com.javiersantos.mlmanager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
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
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // Load Settings
    AppPreferences appPreferences;

    // General variables
    private List<String> appListName = new ArrayList<String>();
    private List<String> appListApk = new ArrayList<String>();
    private List<String> appListSource = new ArrayList<String>();
    private List<String> appListData = new ArrayList<String>();
    private List<Drawable> appListIcon = new ArrayList<Drawable>();

    // Configuration variables
    private Boolean doubleBackToExitPressedOnce = false;
    Toolbar toolbar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.appPreferences = new AppPreferences(getApplicationContext());

        setInitialConfiguration();
        setAppDir();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.appList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        getInstalledApps();
        AppAdapter appAdapter = new AppAdapter(createList(appListName, appListApk, appListSource, appListData, appListIcon), this);
        recyclerView.setAdapter(appAdapter);
        recyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
//                toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                fab.hide();
            }
            @Override
            public void onShow() {
//                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                fab.show();
            }
        });

    }

    private void setInitialConfiguration() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(appPreferences.getKeyPrimaryColorPref());
            getWindow().setStatusBarColor(appPreferences.getKeyPrimaryColorPref());
            toolbar.setBackgroundColor(appPreferences.getKeyPrimaryColorPref());
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings));
        fab.setBackgroundColor(appPreferences.getKeyFABColorPref());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
            }
        });

    }

    private void getInstalledApps() {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        Collections.sort(packages, new ApplicationInfo.DisplayNameComparator(packageManager));
        for(ApplicationInfo packageInfo : packages) {
            if(packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                // Non System Apps
                Log.i("App", packageManager.getApplicationLabel(packageInfo).toString());
                Log.i("App", packageInfo.packageName);
                Log.i("App", packageInfo.sourceDir);
                Log.i("App", packageInfo.dataDir);
                appListName.add(packageManager.getApplicationLabel(packageInfo).toString());
                appListApk.add(packageInfo.packageName);
                appListSource.add(packageInfo.sourceDir);
                appListData.add(packageInfo.dataDir);
                appListIcon.add(packageManager.getApplicationIcon(packageInfo));
            } else {
                // System Apps
            }
        }
    }

    private void setAppDir() {
        File appDir = new File(Environment.getExternalStorageDirectory() + "/MLManager");
        if(!appDir.exists()) {
            appDir.mkdir();
        }
    }

    private List<AppInfo> createList(List<String> apps, List<String> apks, List<String> sources, List<String> data, List<Drawable> icons) {
        List<AppInfo> res = new ArrayList<AppInfo>();
        for (int i=0; i < apps.size(); i++) {
            AppInfo appInfo = new AppInfo();
            appInfo.name = apps.get(i);
            appInfo.apk = apks.get(i);
            appInfo.icon = icons.get(i);
            appInfo.source = sources.get(i);
            appInfo.data = data.get(i);

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
