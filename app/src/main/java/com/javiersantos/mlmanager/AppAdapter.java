package com.javiersantos.mlmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import java.io.File;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    private List<AppInfo> appList;
    private Context context;

    public AppAdapter(List<AppInfo> appList, Context context) {
        this.appList = appList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        AppInfo appInfo = appList.get(i);
        appViewHolder.vName.setText(appInfo.name);
        appViewHolder.vApk.setText(appInfo.apk);
        appViewHolder.vIcon.setImageDrawable(appInfo.icon);

        setButtonEvents(appViewHolder, appInfo);

    }

    private void setButtonEvents(AppViewHolder appViewHolder, final AppInfo appInfo) {
        final String appName = appInfo.name;
        final String appApk = appInfo.apk;
        final String appSource = appInfo.source;
        final String appData = appInfo.data;
        final Drawable appIcon = appInfo.icon;

        ButtonFlat appExtract = appViewHolder.vExtract;
        ButtonFlat appShare = appViewHolder.vShare;
        CardView cardView = appViewHolder.vCard;

        appExtract.setOnClickListener(new View.OnClickListener() {
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
        appShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = UtilsApp.copyFile(appApk, appSource);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                shareIntent.setType("application/vnd.android.package-archive");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.send_to)));
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;

                Intent intent = new Intent(context, AppActivity.class);
                intent.putExtra("app_name", appName);
                intent.putExtra("app_apk", appApk);
                intent.putExtra("app_source", appSource);
                intent.putExtra("app_data", appData);
                Bitmap bitmap = ((BitmapDrawable)appIcon).getBitmap();
                intent.putExtra("app_icon", bitmap);

                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
            }
        });

    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_layout, viewGroup, false);
        return new AppViewHolder(itemView);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vApk;
        protected ImageView vIcon;
        protected ButtonFlat vExtract;
        protected ButtonFlat vShare;
        protected CardView vCard;

        public AppViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.txtName);
            vApk = (TextView) v.findViewById(R.id.txtApk);
            vIcon = (ImageView) v.findViewById(R.id.imgIcon);
            vExtract = (ButtonFlat) v.findViewById(R.id.btnExtract);
            vShare = (ButtonFlat) v.findViewById(R.id.btnShare);
            vCard = (CardView) v.findViewById(R.id.app_card);

        }
    }

}
