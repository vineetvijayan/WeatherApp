package com.vv.syncpoc.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public final class PermissionUtils {

    private PermissionUtils() {
    }

    public static boolean checkPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isDeviceInfoGranted(Context context) {
        return checkPermission(context, Manifest.permission.READ_PHONE_STATE);
    }

    public static void requestPermissions(Object o, int permissionId, String... permissions) {
        if (o instanceof Fragment) {
//            requestPermissions(permissions, permissionId);
        } else if (o instanceof Activity) {
            ActivityCompat.requestPermissions((AppCompatActivity) o, permissions, permissionId);
        }
    }
}
