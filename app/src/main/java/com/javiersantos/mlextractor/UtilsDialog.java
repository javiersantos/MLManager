package com.javiersantos.mlextractor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

public class UtilsDialog {
    private static AlertDialog alertDialog;

    public static AlertDialog showSavedDialog(Context context, String name, String apk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(String.format(context.getResources().getString(R.string.dialog_saved), name));
        builder.setMessage(String.format(context.getResources().getString(R.string.dialog_saved_description), name, apk));
        builder.setPositiveButton(context.getResources().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        return alertDialog;
    }

    public static AlertDialog showAboutDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(String.format(context.getResources().getString(R.string.dialog_about), context.getResources().getString(R.string.app_name)));
        builder.setMessage(context.getResources().getString(R.string.dialog_about_description));
        builder.setCancelable(false);
        builder.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_launcher));
        builder.setPositiveButton(context.getResources().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        return alertDialog;
    }

}
