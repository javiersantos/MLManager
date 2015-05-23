package com.javiersantos.mlmanager;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.javiersantos.mlmanager.utils.AppPreferences;
import com.javiersantos.mlmanager.utils.UtilsApp;
import com.javiersantos.mlmanager.utils.UtilsDialog;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;


public class AppActivity extends AppCompatActivity {
    // Load Settings
    AppPreferences appPreferences;

    // General variables
    private String appName, appApk, appSource, appData;
    private Drawable appIcon;

    // Configuration variables
    private int UNINSTALL_REQUEST_CODE = 1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        this.appPreferences = new AppPreferences(getApplicationContext());
        this.context = this;

        getInitialConfiguration();
        setInitialConfiguration();
        setScreenElements();

    }

    private void setInitialConfiguration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(appPreferences.getKeyPrimaryColorPref());
            getWindow().setStatusBarColor(appPreferences.getKeyPrimaryColorPref());
            toolbar.setBackgroundColor(appPreferences.getKeyPrimaryColorPref());
        }
    }

    private void setScreenElements() {
        TextView header = (TextView) findViewById(R.id.header);
        ImageView icon = (ImageView) findViewById(R.id.app_icon);
        TextView name = (TextView) findViewById(R.id.app_name);
        TextView apk = (TextView) findViewById(R.id.app_apk);
        CardView start = (CardView) findViewById(R.id.start_card);
        CardView extract = (CardView) findViewById(R.id.extract_card);
        CardView uninstall = (CardView) findViewById(R.id.uninstall_card);
        CardView cache = (CardView) findViewById(R.id.cache_card);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        icon.setImageDrawable(appIcon);
        name.setText(appName);
        apk.setText(appApk);

        // Header
        header.setBackgroundColor(appPreferences.getKeyPrimaryColorPref());

        // CardView
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage(appApk);
                startActivity(intent);
            }
        });
        extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File file = UtilsApp.copyFile(appApk, appSource);
                    UtilsDialog.showSavedDialog(context, appName, appApk).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                intent.setData(Uri.parse("package:" + appApk));
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
            }
        });

        /*if (UtilsApp.existCacheFolder(appData)) {
            cache.setVisibility(View.VISIBLE);
            TextView cache_description = (TextView) findViewById(R.id.cache_description);
            cache_description.setText(String.format(getString(R.string.dialog_cache_description), UtilsApp.getCacheFolderSize(appData)));
        } else {
            cache.setVisibility(View.GONE);
        }*/
        cache.setVisibility(View.GONE);

        // FAB
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_share));
        fab.setBackgroundColor(appPreferences.getKeyFABColorPref());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = UtilsApp.copyFile(appApk, appSource);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                shareIntent.setType("application/vnd.android.package-archive");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(shareIntent, String.format(getResources().getString(R.string.send_to), appName)));
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
        appName = getIntent().getStringExtra("app_name");
        appApk = getIntent().getStringExtra("app_apk");
        appSource = getIntent().getStringExtra("app_source");
        appData = getIntent().getStringExtra("app_data");
        Bitmap bitmap = (Bitmap) this.getIntent().getParcelableExtra("app_icon");
        appIcon = (Drawable) new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
