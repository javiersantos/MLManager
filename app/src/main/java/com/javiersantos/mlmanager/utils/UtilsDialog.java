package com.javiersantos.mlmanager.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.javiersantos.mlmanager.R;

public class UtilsDialog {
    private static MaterialDialog materialDialog;

    public static MaterialDialog.Builder showSavedDialog(Context context, String name, String apk) {
        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                .title(String.format(context.getResources().getString(R.string.dialog_saved), name))
                .content(String.format(context.getResources().getString(R.string.dialog_saved_description), name, apk))
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
                .icon(ContextCompat.getDrawable(context, R.drawable.ic_launcher))
                .positiveText(context.getResources().getString(R.string.button_ok))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });

        return materialBuilder;
    }

    public static MaterialDialog.Builder showIndeterminateProgressDialog(Context context, String name) {
        MaterialDialog.Builder materialBuilder = new MaterialDialog.Builder(context)
                .title(String.format(context.getResources().getString(R.string.dialog_saving), name))
                .content(context.getResources().getString(R.string.dialog_saving_description))
                .cancelable(false)
                .progress(true, 0);

        return materialBuilder;
    }

}
