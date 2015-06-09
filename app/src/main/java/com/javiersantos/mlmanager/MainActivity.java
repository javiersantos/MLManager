package com.javiersantos.mlmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.javiersantos.mlmanager.adapters.AppAdapter;
import com.javiersantos.mlmanager.listeners.HidingScrollListener;
import com.javiersantos.mlmanager.utils.AppPreferences;
import com.javiersantos.mlmanager.utils.UtilsApp;
import com.javiersantos.mlmanager.utils.UtilsUI;
import com.melnykov.fab.FloatingActionButton;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // Load Settings
    private AppPreferences appPreferences;

    // General variables
    private List<String> appListName = new ArrayList<String>();
    private List<String> appListAPK = new ArrayList<String>();
    private List<String> appListVersion = new ArrayList<String>();
    private List<String> appListSource = new ArrayList<String>();
    private List<String> appListData = new ArrayList<String>();
    private List<Drawable> appListIcon = new ArrayList<Drawable>();
    private List<String> appSystemListName = new ArrayList<String>();
    private List<String> appSystemListAPK = new ArrayList<String>();
    private List<String> appSystemListVersion = new ArrayList<String>();
    private List<String> appSystemListSource = new ArrayList<String>();
    private List<String> appSystemListData = new ArrayList<String>();
    private List<Drawable> appSystemListIcon = new ArrayList<Drawable>();

    private AppAdapter appAdapter;
    private AppAdapter appSystemAdapter;

    // Configuration variables
    private Boolean doubleBackToExitPressedOnce = false;
    private Toolbar toolbar;
    private Context context;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProgressWheel progressWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.appPreferences = new AppPreferences(getApplicationContext());
        this.context = this;

        setInitialConfiguration();
        setAppDir();

        recyclerView = (RecyclerView) findViewById(R.id.appList);
        progressWheel = (ProgressWheel) findViewById(R.id.progress);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        setNavigationDrawer(appAdapter, appSystemAdapter, recyclerView);
        setFAB();

        progressWheel.setBarColor(appPreferences.getPrimaryColorPref());
        progressWheel.setVisibility(View.VISIBLE);
        new getInstalledApps().execute();

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

    private void setNavigationDrawer(AppAdapter appAdapter, AppAdapter appSystemAdapter, RecyclerView recyclerView) {
        UtilsUI.setNavigationDrawer(this, getApplicationContext(), toolbar, appAdapter, appSystemAdapter, recyclerView);
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

    class getInstalledApps extends AsyncTask<Void, PackageInfo, Void> {
        public getInstalledApps() {}

        @Override
        protected Void doInBackground(Void... params) {
            final PackageManager packageManager = getPackageManager();
            List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
            // Get Sort Mode
            switch (appPreferences.getSortMode()) {
                default:
                    // Comparator by Name (default)
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            return packageManager.getApplicationLabel(p1.applicationInfo).toString().toLowerCase().compareTo(packageManager.getApplicationLabel(p2.applicationInfo).toString().toLowerCase());
                        }
                    });
                    break;
                case "2":
                    // Comparator by Size
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            String size1 = String.valueOf(new File(p1.applicationInfo.packageName).length());
                            String size2 = String.valueOf(new File(p2.applicationInfo.packageName).length());
                            return size1.compareTo(size2);
                        }
                    });
                    break;
                case "3":
                    // Comparator by Installation Date (default)
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            return Long.toString(p1.firstInstallTime).compareTo(Long.toString(p2.firstInstallTime));
                        }
                    });
                    break;
                case "4":
                    // Comparator by Last Update Date (default)
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            return Long.toString(p1.lastUpdateTime).compareTo(Long.toString(p2.lastUpdateTime));
                        }
                    });
                    break;
            }
            for(PackageInfo packageInfo : packages) {
                if(packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                    try {
                        // Non System Apps
                        appListName.add(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                        appListAPK.add(packageInfo.packageName);
                        appListVersion.add(packageInfo.versionName);
                        appListSource.add(packageInfo.applicationInfo.sourceDir);
                        appListData.add(packageInfo.applicationInfo.dataDir);
                        appListIcon.add(packageManager.getApplicationIcon(packageInfo.applicationInfo));
                    } catch (OutOfMemoryError e) {
                        //TODO Workaround to avoid FC on some devices (OutOfMemoryError). Drawable should be cached before.
                        appListIcon.add(getResources().getDrawable(R.drawable.ic_launcher));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        // System Apps
                        appSystemListName.add(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                        appSystemListAPK.add(packageInfo.packageName);
                        appSystemListVersion.add(packageInfo.versionName);
                        appSystemListSource.add(packageInfo.applicationInfo.sourceDir);
                        appSystemListData.add(packageInfo.applicationInfo.dataDir);
                        appSystemListIcon.add(packageManager.getApplicationIcon(packageInfo.applicationInfo));
                    } catch (OutOfMemoryError e) {
                        //TODO Workaround to avoid FC on some devices (OutOfMemoryError). Drawable should be cached before.
                        appSystemListIcon.add(getResources().getDrawable(R.drawable.ic_launcher));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(PackageInfo... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            appAdapter = new AppAdapter(createList(appListName, appListAPK, appListVersion, appListSource, appListData, appListIcon, false), context);
            appSystemAdapter = new AppAdapter(createList(appSystemListName, appSystemListAPK, appSystemListVersion, appSystemListSource, appSystemListData, appSystemListIcon, true), context);

            recyclerView.setAdapter(appAdapter);
            progressWheel.setVisibility(View.GONE);

            setNavigationDrawer(appAdapter, appSystemAdapter, recyclerView);
        }

    }

    private void setAppDir() {
        File appDir = UtilsApp.getAppFolder();
        if(!appDir.exists()) {
            appDir.mkdir();
        }
    }

    private List<AppInfo> createList(List<String> apps, List<String> apks, List<String> versions, List<String> sources, List<String> data, List<Drawable> icons, Boolean isSystem) {
        List<AppInfo> res = new ArrayList<AppInfo>();
        for (int i=0; i < apps.size(); i++) {
            AppInfo appInfo = new AppInfo(apps.get(i), apks.get(i), versions.get(i), sources.get(i), data.get(i), icons.get(i), isSystem);
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
