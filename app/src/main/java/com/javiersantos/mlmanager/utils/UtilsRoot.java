package com.javiersantos.mlmanager.utils;

import android.os.Build;

import com.javiersantos.mlmanager.MLManagerApplication;

import java.io.File;

public class UtilsRoot {

    private static final int ROOT_STATUS_NOT_CHECKED = 0;
    private static final int ROOT_STATUS_ROOTED = 1;
    private static final int ROOT_STATUS_NOT_ROOTED = 2;

    private UtilsRoot() {
    }

    public static boolean isRooted() {
        int rootStatus = MLManagerApplication.getAppPreferences().getRootStatus();
        boolean isRooted = false;
        if (rootStatus == ROOT_STATUS_NOT_CHECKED) {
            isRooted = isRootByBuildTag() || isRootedByFileSU() || isRootedByExecutingCommand();
            MLManagerApplication.getAppPreferences().setRootStatus(isRooted ? ROOT_STATUS_ROOTED : ROOT_STATUS_NOT_ROOTED);
        } else if (rootStatus == ROOT_STATUS_ROOTED) {
            isRooted = true;
        }
        return isRooted;
    }

    public static boolean isRootByBuildTag() {
        String buildTags = Build.TAGS;
        return ((buildTags != null && buildTags.contains("test-keys")));
    }

    public static boolean isRootedByFileSU() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
        }
        return false;
    }

    public static boolean isRootedByExecutingCommand() {
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su")
                || canExecuteCommand("which su");
    }

    public static boolean removeWithRootPermission(String directory) {
        boolean status = false;
        try {
            String[] command = new String[]{"su", "-c", "rm -rf " + directory};
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            int i = process.exitValue();
            if (i == 0) {
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static boolean hideWithRootPermission(String apk, Boolean hidden) {
        boolean status = false;
        try {
            String [] command;
            if (hidden) {
                command = new String[]{"su", "-c", "pm unhide " + apk + "\n"};
            } else {
                command = new String[]{"su", "-c", "pm hide " + apk + "\n"};
            }

            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            int i = process.exitValue();
            if (i == 0) {
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static boolean uninstallWithRootPermission(String source) {
        boolean status = false;
        try {
            String[] command_write = new String[]{"su", "-c", "mount -o rw,remount /system\n"};
            String[] command_delete = new String[]{"su", "-c", "rm -r " + "/" + source + "\n"};
            String[] command_read = new String[]{"su", "-c", "mount -o ro,remount /system\n"};

            Process process = Runtime.getRuntime().exec(command_write);
            process.waitFor();
            int i = process.exitValue();
            if (i == 0) {
                process = Runtime.getRuntime().exec(command_delete);
                process.waitFor();
                i = process.exitValue();
                if (i == 0) {
                    process = Runtime.getRuntime().exec(command_read);
                    process.waitFor();
                    i = process.exitValue();
                    if (i == 0) {
                        status = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static boolean rebootSystem() {
        boolean status = false;
        try {
            String [] command = new String[]{"su", "-c", "reboot\n"};

            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            int i = process.exitValue();
            if (i == 0) {
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static long getFolderSizeInMB(String directory) {
        File f = new File(directory);
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSizeInMB(file.getAbsolutePath());
            }
        } else {
            size = f.length()/1024/2024;
        }

        return size;
    }

    private static boolean canExecuteCommand(String command) {
        boolean isExecuted;
        try {
            Runtime.getRuntime().exec(command);
            isExecuted = true;
        } catch (Exception e) {
            isExecuted = false;
        }

        return isExecuted;
    }
}
