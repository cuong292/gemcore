package com.gemvietnam.utils;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Dimension Utils
 * Created by neo on 8/15/2016.
 */
public class DimensionUtils {

  /**
   * Convert dp to px
   */
  public static float dpToPx(Context context, float valueInDp) {
    if (context == null || context.getResources() == null) {
      return 0;
    }
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
  }

  public static float pxToDp(Context context, float px) {
    if (context == null || context.getResources() == null) {
      return 0;
    }
    return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }

  public static int getInPx(Context context, @DimenRes int dimenId) {
    return context.getResources().getDimensionPixelSize(dimenId);
  }

  public static float getWidthScreenDp(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    return displayMetrics.widthPixels / displayMetrics.density;
  }

  public static float getHeightScreenDp(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    return displayMetrics.heightPixels / displayMetrics.density;
  }
}
