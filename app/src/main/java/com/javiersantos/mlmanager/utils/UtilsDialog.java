package com.javiersantos.mlmanager.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.javiersantos.mlmanager.R;

import java.io.File;

public class UtilsDialog {

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

    public static SnackBar showSnackbar(Activity activity, String text, String buttonText, final File file) {
        final SnackBar snackBar;

        if (file == null) {
            snackBar = new SnackBar(activity, text, null, null);
        } else {
            snackBar = new SnackBar(activity, text, buttonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    file.delete();
                }
            });
        }

        return snackBar;
    }

}
