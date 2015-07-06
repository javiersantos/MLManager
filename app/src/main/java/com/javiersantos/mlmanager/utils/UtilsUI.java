package com.javiersantos.mlmanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.javiersantos.mlmanager.MLManagerApplication;
import com.javiersantos.mlmanager.activities.AboutActivity;
import com.javiersantos.mlmanager.R;
import com.javiersantos.mlmanager.activities.SettingsActivity;
import com.javiersantos.mlmanager.adapters.AppAdapter;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Calendar;

public class UtilsUI {
    // Load Settings
    private static AppPreferences appPreferences;

    public static int darker (int color, double factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green( color );
        int b = Color.blue(color);

        return Color.argb(a, Math.max((int) (r * factor), 0), Math.max((int) (g * factor), 0), Math.max((int) (b * factor), 0));
    }

    public static Drawer setNavigationDrawer (Activity activity, final Context context, Toolbar toolbar, final AppAdapter appAdapter, final AppAdapter appSystemAdapter, final AppAdapter appFavoriteAdapter, final AppAdapter appHiddenAdapter, final RecyclerView recyclerView) {
        final String loadingLabel = context.getResources().getString(R.string.loading);
        int header;
        appPreferences = MLManagerApplication.getAppPreferences();
        String apps, systemApps, favoriteApps, hiddenApps;

        if (getDayOrNight() == 1) {
            header = R.drawable.header_day;
        } else {
            header = R.drawable.header_night;
        }

        if (appAdapter != null) {
            apps = Integer.toString(appAdapter.getItemCount());
        } else {
            apps = loadingLabel;
        }
        if (appSystemAdapter != null) {
            systemApps = Integer.toString(appSystemAdapter.getItemCount());
        } else {
            systemApps = loadingLabel;
        }
        if (appFavoriteAdapter != null) {
            favoriteApps = Integer.toString(appFavoriteAdapter.getItemCount());
        } else {
            favoriteApps = loadingLabel;
        }
        if (appHiddenAdapter != null) {
            hiddenApps = Integer.toString(appHiddenAdapter.getItemCount());
        } else {
            hiddenApps = loadingLabel;
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(header)
                .build();
        if (MLManagerApplication.isPro()) {
            return new DrawerBuilder()
                    .withActivity(activity)
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .withStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8))
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_apps)).withIcon(FontAwesome.Icon.faw_mobile).withBadge(apps),
                            new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_system_apps)).withIcon(FontAwesome.Icon.faw_android).withBadge(systemApps),
                            new DividerDrawerItem(),
                            new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_favorites)).withIcon(FontAwesome.Icon.faw_star).withBadge(favoriteApps),
                            new DividerDrawerItem(),
                            new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_hidden_apps)).withIcon(FontAwesome.Icon.faw_eye_slash).withBadge(hiddenApps),
                            new DividerDrawerItem(),
                            new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_settings)).withIcon(FontAwesome.Icon.faw_cog).withCheckable(false),
                            new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_about)).withIcon(FontAwesome.Icon.faw_info_circle).withCheckable(false)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                            switch (position) {
                                case 0:
                                    recyclerView.setAdapter(appAdapter);
                                    break;
                                case 1:
                                    recyclerView.setAdapter(appSystemAdapter);
                                    break;
                                case 3:
                                    recyclerView.setAdapter(appFavoriteAdapter);
                                    break;
                                case 5:
                                    recyclerView.setAdapter(appHiddenAdapter);
                                    break;
                                case 7:
                                    context.startActivity(new Intent(context, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    break;
                                case 8:
                                    context.startActivity(new Intent(context, AboutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    break;
                                default:
                                    break;
                            }

                            return false;
                        }
                    }).build();
        } else {
            return new DrawerBuilder()
                    .withActivity(activity)
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .withStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8))
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_apps)).withIcon(FontAwesome.Icon.faw_mobile).withBadge(apps),
                            new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_system_apps)).withIcon(FontAwesome.Icon.faw_android).withBadge(systemApps),
                            new DividerDrawerItem(),
                            new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_favorites)).withIcon(FontAwesome.Icon.faw_star).withBadge(favoriteApps),
                            new DividerDrawerItem(),
                            new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_buy)).withIcon(FontAwesome.Icon.faw_shopping_cart).withBadge(context.getResources().getString(R.string.action_buy_description)).withCheckable(false),
                            new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_settings)).withIcon(FontAwesome.Icon.faw_cog).withCheckable(false),
                            new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_about)).withIcon(FontAwesome.Icon.faw_info_circle).withCheckable(false)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                            switch (position) {
                                case 0:
                                    recyclerView.setAdapter(appAdapter);
                                    break;
                                case 1:
                                    recyclerView.setAdapter(appSystemAdapter);
                                    break;
                                case 3:
                                    recyclerView.setAdapter(appFavoriteAdapter);
                                    break;
                                case 5:
                                    context.startActivity(UtilsApp.goToGooglePlay(MLManagerApplication.getProPackage()));
                                    break;
                                case 6:
                                    context.startActivity(new Intent(context, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    break;
                                case 7:
                                    context.startActivity(new Intent(context, AboutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    break;
                                default:
                                    break;
                            }

                            return false;
                        }
                    }).build();
        }

    }

    public static int getDayOrNight() {
        int actualHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (actualHour >= 8 && actualHour < 19) {
            return 1;
        } else {
            return 0;
        }
    }

}
