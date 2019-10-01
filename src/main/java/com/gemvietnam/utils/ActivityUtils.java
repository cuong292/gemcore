/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gemvietnam.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import static com.gemvietnam.base.CoreDefault.ANIM_ENTER_RIGHT_TO_LEFT;
import static com.gemvietnam.base.CoreDefault.ANIM_EXIT_RIGHT_TO_LEFT;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

  public static <T extends Activity> void startActivity(Context context, Class<T> clazz) {
    startActivity(context, clazz, null, true);
  }

  public static <T extends Activity> void startActivity(Context context, Class<T> clazz, Bundle extras) {
    startActivity(context, clazz, extras, true);
  }

  public static <T extends Activity> void startActivity(Context context, Class<T> clazz, boolean withAnim) {
    startActivity(context, clazz, null, withAnim);
  }

  public static <T extends Activity> void startActivity(Context context, Class<T> clazz, Bundle extras, boolean withAnim) {
    Intent intent = new Intent(context, clazz);
    if (extras != null) {
      intent.putExtras(extras);
    }
    context.startActivity(intent);
    overrideTransition(context, withAnim);
  }

  public static <T extends Activity> void startActivityClearTop(Context context, Class<T> clazz) {
    Intent intent = new Intent(context, clazz);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(intent);
    overrideTransition(context, true);
  }

  public static <T extends Activity> void startActivityClearTop(Context context, Class<T> clazz, Bundle data) {
    Intent intent = new Intent(context, clazz);
    if (data != null) {
      intent.putExtras(data);
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(intent);
    overrideTransition(context, true);
  }

  public static <T extends Activity> void startActivityClearTask(Context context, Class<T> clazz) {
    Intent intent = new Intent(context, clazz);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
    overrideTransition(context, true);
  }

  public static <T extends Activity> void startActivityClearTask(Context context, Class<T> clazz, Bundle data) {
    Intent intent = new Intent(context, clazz);
    if (data != null) {
      intent.putExtras(data);
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
    overrideTransition(context, true);
  }

  public static <T extends Activity> void startActivityForResult(Activity activity, Class<T> clazz, int requestCode) {
    startActivityForResult(activity, clazz, null, true, requestCode);
  }

  public static <T extends Activity> void startActivityForResult(Activity activity, Class<T> clazz, Bundle extras, int requestCode) {
    startActivityForResult(activity, clazz, extras, true, requestCode);
  }

  public static <T extends Activity> void startActivityForResult(Activity activity, Class<T> clazz, boolean withAnim, int requestCode) {
    startActivityForResult(activity, clazz, null, withAnim, requestCode);
  }

  public static <T extends Activity> void startActivityForResult(Activity activity, Class<T> clazz, Bundle extras, boolean withAnim, int requestCode) {
    Intent intent = new Intent(activity, clazz);
    if (extras != null) {
      intent.putExtras(extras);
    }
    activity.startActivityForResult(intent, requestCode);
    overrideTransition(activity, withAnim);
  }

  public static void startPlayVideo(Context context, String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    intent.setDataAndType(Uri.parse(url), "video/*");
    intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, false);
    context.startActivity(intent);
  }

  private static void overrideTransition(Context context, boolean withAnim) {
    if (context instanceof Activity && withAnim) {
      ((Activity) context).overridePendingTransition(ANIM_ENTER_RIGHT_TO_LEFT, ANIM_EXIT_RIGHT_TO_LEFT);
    }
  }
}
