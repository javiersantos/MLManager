package com.javiersantos.mlmanager.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.javiersantos.mlmanager.R;

import java.io.File;

public class UtilsDialog {

    public static MaterialDialog showTitleContent(Context context, String title, String content) {
        MaterialDialog.Builder materialBuilder = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(context.getResources().getString(R.string.button_ok))
                .cancelable(true)
                .callback(new MaterialDialog.ButtonCallback() {
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

    public static MaterialDialog.Builder showUninstall(Context context) {
        MaterialDialog.Builder materialBuilder = new MaterialDialog.Builder(context)
                .title(context.getResources().getString(R.string.dialog_uninstall_root))
                .content(context.getResources().getString(R.string.dialog_uninstall_root_description))
                .positiveText(context.getResources().getString(R.string.button_uninstall))
                .negativeText(context.getResources().getString(R.string.button_cancel))
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });
        return materialBuilder;
    }

    // 1: APK Extracted
    // 2: Snackbar without Button
    // 3: App unhide

    /**
     * Show Snackbar
     * @param activity Activity
     * @param text Text of the Snackbar
     * @param buttonText Button text of the Snackbar
     * @param file File to remove if style == 1
     * @param style 1 for extracted APKs, 2 display without button and 3 for hidden apps
     * @return Snackbar to show
     */
    public static SnackBar showSnackbar(Activity activity, String text, @Nullable String buttonText, @Nullable final File file, Integer style) {
        SnackBar snackBar;

        switch (style) {
            case 1:
                snackBar = new SnackBar(activity, text, buttonText, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        file.delete();
                    }
                });
                break;
            case 2:
                snackBar = new SnackBar(activity, text, null, null);
                break;
            case 3:
                snackBar = new SnackBar(activity, text, buttonText, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UtilsRoot.rebootSystem();
                    }
                });
                break;
            default:
                snackBar = new SnackBar(activity, text, null, null);
                break;
        }

        return snackBar;
    }

}
