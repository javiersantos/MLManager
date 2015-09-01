package com.javiersantos.mlmanager.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonFlat;
import com.javiersantos.mlmanager.MLManagerApplication;
import com.javiersantos.mlmanager.activities.AppActivity;
import com.javiersantos.mlmanager.AppInfo;
import com.javiersantos.mlmanager.R;
import com.javiersantos.mlmanager.activities.MainActivity;
import com.javiersantos.mlmanager.async.ExtractFileInBackground;
import com.javiersantos.mlmanager.utils.AppPreferences;
import com.javiersantos.mlmanager.utils.UtilsApp;
import com.javiersantos.mlmanager.utils.UtilsDialog;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> implements Filterable {
    // Load Settings
    private AppPreferences appPreferences;

    // AppAdapter variables
    private List<AppInfo> appList;
    private List<AppInfo> appListSearch;
    private Context context;

    public AppAdapter(List<AppInfo> appList, Context context) {
        this.appList = appList;
        this.context = context;
        this.appPreferences = MLManagerApplication.getAppPreferences();
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public void clear() {
        appList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        AppInfo appInfo = appList.get(i);
        appViewHolder.vName.setText(appInfo.getName());
        appViewHolder.vApk.setText(appInfo.getAPK());
        appViewHolder.vIcon.setImageDrawable(appInfo.getIcon());

        setButtonEvents(appViewHolder, appInfo);

    }

    private void setButtonEvents(AppViewHolder appViewHolder, final AppInfo appInfo) {
        ButtonFlat appExtract = appViewHolder.vExtract;
        ButtonFlat appShare = appViewHolder.vShare;
        final ImageView appIcon = appViewHolder.vIcon;
        final CardView cardView = appViewHolder.vCard;

        appExtract.setBackgroundColor(appPreferences.getPrimaryColorPref());
        appShare.setBackgroundColor(appPreferences.getPrimaryColorPref());

        appExtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog dialog = UtilsDialog.showTitleContentWithProgress(context
                        , String.format(context.getResources().getString(R.string.dialog_saving), appInfo.getName())
                        , context.getResources().getString(R.string.dialog_saving_description));
                new ExtractFileInBackground(context, dialog, appInfo).execute();
            }
        });
        appShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsApp.copyFile(appInfo);
                Intent shareIntent = UtilsApp.getShareIntent(UtilsApp.getOutputFilename(appInfo));
                context.startActivity(Intent.createChooser(shareIntent, String.format(context.getResources().getString(R.string.send_to), appInfo.getName())));
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;

                Intent intent = new Intent(context, AppActivity.class);
                intent.putExtra("app_name", appInfo.getName());
                intent.putExtra("app_apk", appInfo.getAPK());
                intent.putExtra("app_version", appInfo.getVersion());
                intent.putExtra("app_source", appInfo.getSource());
                intent.putExtra("app_data", appInfo.getData());
                Bitmap bitmap = ((BitmapDrawable) appInfo.getIcon()).getBitmap();
                intent.putExtra("app_icon", bitmap);
                intent.putExtra("app_isSystem", appInfo.isSystem());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String transitionName = context.getResources().getString(R.string.transition_app_icon);

                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, appIcon, transitionName);
                    context.startActivity(intent, transitionActivityOptions.toBundle());
                } else {
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
                }
            }
        });

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<AppInfo> results = new ArrayList<>();
                if (appListSearch == null) {
                    appListSearch = appList;
                }
                if (charSequence != null) {
                    if (appListSearch != null && appListSearch.size() > 0) {
                        for (final AppInfo appInfo : appListSearch) {
                            if (appInfo.getName().toLowerCase().contains(charSequence.toString())) {
                                results.add(appInfo);
                            }
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count > 0) {
                    MainActivity.setResultsMessage(false);
                } else {
                    MainActivity.setResultsMessage(true);
                }
                appList = (ArrayList<AppInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View appAdapterView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_layout, viewGroup, false);
        return new AppViewHolder(appAdapterView);
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
