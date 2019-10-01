package com.gemvietnam.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.gemvietnam.common.R;

/**
 * Check, request permission utils
 * Created by neo on 7/26/2016.
 */
public class PermissionUtils {

  public static boolean checkToRequest(Activity activity, String permission, int requestCode) {
    int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        Log.i("PermissionUtils", "Should show request permission rationale");
      } else {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
      }
      return false;
    } else {
      return true;
    }
  }

  public static boolean needRequestPermissions(Context context, Fragment fragment, String[] permissions, int requestCode) {
    if (hasPermissions(context, permissions)) {
      return false;
    }
    fragment.requestPermissions(permissions, requestCode);
    return true;
  }

  public static boolean hasPermissions(Context context, String[] permissions) {
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
      for (String permission : permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }

  public static boolean needRequestPermissions(Context context, String[] permissions, int requestCode) {
    if (hasPermissions(context, permissions)) {
      return false;
    }

    ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    return true;
  }


  public static boolean isPermissionGranted(Activity activity, String permission) {
    return ContextCompat.checkSelfPermission(activity, permission) ==
        PackageManager.PERMISSION_GRANTED;
  }

  public static boolean isPermissionsGranted(Context context, @NonNull int[] grantResults) {
    for (int grantResult : grantResults) {
      if (grantResult != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  public static boolean isHavePermissionReadWrite(Context context) {
    return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  public static boolean shouldShowRequestPermissionRationaleReadWrite(Activity activity) {
    return activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        && activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
  }

  public static void requestRequestPermissionReadWrite(Context context, int requestCode) {
    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
  }

  public static void requestRequestPermissionReadWrite(Fragment fragment, int requestCode) {
    fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
  }

  public static void openSettingPermissonApp(final Context context) {
    //open setting screen for user enable permission
    DialogUtils.showDialogMessage(context, "Request Permission", "You have to turn on camera/storage permission to use this method", "Setting", "No,thanks", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (which == 12) {
          Intent i = new Intent();
          i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
          i.addCategory(Intent.CATEGORY_DEFAULT);
          i.setData(Uri.parse("package:" + context.getApplicationContext().getPackageName()));
          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
          i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
          context.startActivity(i);
        }
        dialog.dismiss();
      }
    });
  }
}
