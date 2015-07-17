package com.javiersantos.mlmanager.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.javiersantos.mlmanager.AppInfo;
import com.javiersantos.mlmanager.R;
import com.javiersantos.mlmanager.activities.MainActivity;
import com.javiersantos.mlmanager.utils.UtilsDialog;
import com.javiersantos.mlmanager.utils.UtilsRoot;

public class UninstallInBackground extends AsyncTask<Void, String, Boolean> {
    private Context context;
    private Activity activity;
    private MaterialDialog dialog;
    private AppInfo appInfo;

    public UninstallInBackground(Context context, MaterialDialog dialog, AppInfo appInfo) {
        this.context = context;
        this.activity = (Activity) context;
        this.dialog = dialog;
        this.appInfo = appInfo;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return UtilsRoot.uninstallWithRootPermission(appInfo.getSource());
    }

    @Override
    protected void onPostExecute(Boolean status) {
        super.onPostExecute(status);
        dialog.dismiss();
        if (status) {
            activity.finish();
            context.startActivity(new Intent(context, MainActivity.class));
        } else {
            UtilsDialog.showTitleContent(context, context.getResources().getString(R.string.dialog_root_required), context.getResources().getString(R.string.dialog_root_required_description));
        }
    }
}