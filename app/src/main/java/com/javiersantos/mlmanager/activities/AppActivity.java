package com.javiersantos.mlmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.javiersantos.mlmanager.AppInfo;
import com.javiersantos.mlmanager.MLManagerApplication;
import com.javiersantos.mlmanager.R;
import com.javiersantos.mlmanager.async.DeleteDataInBackground;
import com.javiersantos.mlmanager.async.ExtractFileInBackground;
import com.javiersantos.mlmanager.utils.AppPreferences;
import com.javiersantos.mlmanager.utils.UtilsRoot;
import com.javiersantos.mlmanager.utils.UtilsApp;
import com.javiersantos.mlmanager.utils.UtilsDialog;
import com.javiersantos.mlmanager.utils.UtilsUI;

import java.util.Set;

public class AppActivity extends AppCompatActivity {
    // Load Settings
    private AppPreferences appPreferences;

    // General variables
    private AppInfo appInfo;
    private Set<String> appFavorites;

    // Configuration variables
    private int UNINSTALL_REQUEST_CODE = 1;
    private Context context;
    private MenuItem item_favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        this.context = this;
        this.appPreferences = MLManagerApplication.getAppPreferences();

        getInitialConfiguration();
        setInitialConfiguration();
        setScreenElements();

    }

    private void setInitialConfiguration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null ) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));
            toolbar.setBackgroundColor(appPreferences.getPrimaryColorPref());
            if (!appPreferences.getNavigationBlackPref()) {
                getWindow().setNavigationBarColor(appPreferences.getPrimaryColorPref());
            }
        }
    }

    private void setScreenElements() {
        TextView header = (TextView) findViewById(R.id.header);
        ImageView icon = (ImageView) findViewById(R.id.app_icon);
        ImageView icon_googleplay = (ImageView) findViewById(R.id.app_googleplay);
        TextView name = (TextView) findViewById(R.id.app_name);
        TextView version = (TextView) findViewById(R.id.app_version);
        TextView apk = (TextView) findViewById(R.id.app_apk);
        CardView googleplay = (CardView) findViewById(R.id.id_card);
        CardView start = (CardView) findViewById(R.id.start_card);
        CardView extract = (CardView) findViewById(R.id.extract_card);
        CardView uninstall = (CardView) findViewById(R.id.uninstall_card);
        CardView cache = (CardView) findViewById(R.id.cache_card);
        CardView clearData = (CardView) findViewById(R.id.clear_data_card);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        icon.setImageDrawable(appInfo.getIcon());
        name.setText(appInfo.getName());
        apk.setText(appInfo.getAPK());
        version.setText(appInfo.getVersion());

        // Header
        header.setBackgroundColor(appPreferences.getPrimaryColorPref());

        // CardView
        if (appInfo.isSystem()) {
            icon_googleplay.setVisibility(View.GONE);
            start.setVisibility(View.GONE);
            uninstall.setVisibility(View.GONE);

            googleplay.setForeground(null);
            start.setForeground(null);
            uninstall.setForeground(null);
        } else {
            googleplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(UtilsApp.goToGooglePlay(appInfo.getAPK()));
                }
            });

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(appInfo.getAPK());
                    startActivity(intent);
                }
            });

            uninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                    intent.setData(Uri.parse("package:" + appInfo.getAPK()));
                    intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                    startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
                }
            });
        }
        extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog dialog = UtilsDialog.showTitleContentWithProgress(context
                        , String.format(getResources().getString(R.string.dialog_saving), appInfo.getName())
                        , getResources().getString(R.string.dialog_saving_description));
                new ExtractFileInBackground(context, dialog, appInfo).execute();
            }
        });

        if(UtilsRoot.isRooted()) {
            cache.setVisibility(View.VISIBLE);
            cache.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialDialog dialog = UtilsDialog.showTitleContentWithProgress(context
                            , getResources().getString(R.string.dialog_cache_deleting)
                            , getResources().getString(R.string.dialog_cache_deleting_description));
                    new DeleteDataInBackground(context, dialog, appInfo.getData() + "/cache/**"
                            , getResources().getString(R.string.dialog_cache_success_description, appInfo.getName())).execute();
                }
            });
            clearData.setVisibility(View.VISIBLE);
            clearData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialDialog dialog = UtilsDialog.showTitleContentWithProgress(context
                            , getResources().getString(R.string.dialog_clear_data_deleting)
                            , getResources().getString(R.string.dialog_clear_data_deleting_description));
                    new DeleteDataInBackground(context, dialog, appInfo.getData() + "/**"
                            , getResources().getString(R.string.dialog_clear_data_success_description, appInfo.getName())).execute();
                }
            });
        }

        // FAB
        fab.setIcon(R.drawable.ic_send_white);
        fab.setColorNormal(appPreferences.getFABColorPref());
        fab.setColorPressed(UtilsUI.darker(appPreferences.getFABColorPref(), 0.8));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsApp.copyFile(appInfo);
                Intent shareIntent = UtilsApp.getShareIntent(UtilsApp.getOutputFilename(appInfo));
                startActivity(Intent.createChooser(shareIntent, String.format(getResources().getString(R.string.send_to), appInfo.getName())));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.i("App", "OK");
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("App", "CANCEL");
            }
        }
    }

    private void getInitialConfiguration() {
        String appName = getIntent().getStringExtra("app_name");
        String appApk = getIntent().getStringExtra("app_apk");
        String appVersion = getIntent().getStringExtra("app_version");
        String appSource = getIntent().getStringExtra("app_source");
        String appData = getIntent().getStringExtra("app_data");
        Bitmap bitmap = getIntent().getParcelableExtra("app_icon");
        Drawable appIcon = new BitmapDrawable(getResources(), bitmap);
        Boolean appIsSystem = getIntent().getExtras().getBoolean("app_isSystem");

        appInfo = new AppInfo(appName, appApk, appVersion, appSource, appData, appIcon, appIsSystem);
        appFavorites = appPreferences.getFavoriteApps();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        item_favorite = menu.findItem(R.id.action_favorite);
        UtilsApp.setAppFavorite(context, item_favorite, UtilsApp.isAppFavorite(appInfo.getAPK(), appFavorites));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
            case R.id.action_favorite:
                if (UtilsApp.isAppFavorite(appInfo.getAPK(), appFavorites)) {
                    appFavorites.remove(appInfo.getAPK());
                    appPreferences.setFavoriteApps(appFavorites);
                } else {
                    appFavorites.add(appInfo.getAPK());
                    appPreferences.setFavoriteApps(appFavorites);
                }
                UtilsApp.setAppFavorite(context, item_favorite, UtilsApp.isAppFavorite(appInfo.getAPK(), appFavorites));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
