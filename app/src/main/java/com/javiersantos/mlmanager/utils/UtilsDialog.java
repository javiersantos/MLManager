package com.javiersantos.mlmanager.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.javiersantos.mlmanager.AppInfo;
import com.javiersantos.mlmanager.MLManagerApplication;
import com.javiersantos.mlmanager.R;

public class UtilsDialog {
    private static AppPreferences appPreferences;

    public static MaterialDialog.Builder showSavedDialog(Context context, AppInfo appInfo) {
        appPreferences = MLManagerApplication.getAppPreferences();
        String filename;

        switch (appPreferences.getCustomFilename()) {
            case "1":
                filename = appInfo.getAPK() + "_" + appInfo.getVersion();
                break;
            case "2":
                filename = appInfo.getName() + "_" + appInfo.getVersion();
                break;
            case "4":
                filename = appInfo.getName();
                break;
            default:
                filename = appInfo.getAPK();
                break;
        }

        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                .title(String.format(context.getResources().getString(R.string.dialog_saved), appInfo.getName()))
                .content(String.format(context.getResources().getString(R.string.dialog_saved_description), appInfo.getName(), filename))
                .positiveText(context.getResources().getString(R.string.button_ok))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });

        return materialDialog;
    }

    public static MaterialDialog.Builder showAboutDialog(Context context) {
        MaterialDialog.Builder materialBuilder = new MaterialDialog.Builder(context)
                .title(String.format(context.getResources().getString(R.string.dialog_about), context.getResources().getString(R.string.app_name)))
                .content(context.getResources().getString(R.string.dialog_about_description))
                .cancelable(false)
                .icon(ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
                .positiveText(context.getResources().getString(R.string.button_ok))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });

        return materialBuilder;
    }

    public static MaterialDialog.Builder showIndeterminateProgressDialog(Context context, AppInfo appInfo) {
        MaterialDialog.Builder materialBuilder = new MaterialDialog.Builder(context)
                .title(String.format(context.getResources().getString(R.string.dialog_saving), appInfo.getName()))
                .content(context.getResources().getString(R.string.dialog_saving_description))
                .cancelable(false)
                .progress(true, 0);

        return materialBuilder;
    }

    public static MaterialDialog showTitleContent(Context context, String title, String content) {
        MaterialDialog.Builder materialBuilder = new MaterialDialog.Builder(context)
                .title(title)
                .content(content).positiveText(context.getResources().getString(R.string.button_ok)).cancelable(true).callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });
        return materialBuilder.show();
    }

    public static MaterialDialog showTitleContentWithProgress(Context context, String title, String content) {
        MaterialDialog.Builder materialBuilder = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .cancelable(false)
                .progress(true, 0);
        return materialBuilder.show();
    }

}
