package com.gemvietnam.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by BaVV on 17-Nov-17.
 * ViewUtils
 */

public class ViewUtils {
  public static void setEditTextMaxLength(EditText editText, int length) {
    InputFilter[] filterArray = new InputFilter[1];
    filterArray[0] = new InputFilter.LengthFilter(length);
    editText.setFilters(filterArray);
  }

  public static void updateTabUI(TabLayout tablayout, Context context, int index) {
    int tabSize = tablayout.getTabCount();
    if (tabSize < 1) {
      return;
    }
    ViewGroup childTab = (ViewGroup) tablayout.getChildAt(0);
    for (int i = 0; i < tabSize; i++) {
      ViewGroup containerTabContent = (ViewGroup) childTab.getChildAt(i);
      TextView textView = (TextView) containerTabContent.getChildAt(1);
      Typeface typefaceNormal = TypefaceUtils.load(context.getAssets(), "fonts/Avenir-Medium.ttf");
      textView.setTypeface(typefaceNormal);
    }
    ViewGroup containerTabContent = (ViewGroup) childTab.getChildAt(index);
    TextView textView = (TextView) containerTabContent.getChildAt(1);
    Typeface typefaceBold = TypefaceUtils.load(context.getAssets(), "fonts/Avenir-Medium.ttf");
    textView.setTypeface(typefaceBold);
  }

  public static void delayViewPress(final View view) {
    if (view == null) {
      return;
    }
    view.setClickable(false);
    view.setFocusable(false);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (view == null)
          return;
        view.setClickable(true);
        view.setFocusable(true);

      }
    }, 100);
  }

  public static void focusView(View view) {
    view.setFocusable(true);
    view.setFocusableInTouchMode(true);
    view.requestFocus();
    view.requestFocusFromTouch();
    view.setFocusable(false);
    view.setFocusableInTouchMode(false);

    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void expand(View v) {
    expand(v, null);
  }

  public static void expand(final View v, Animation.AnimationListener animationListener) {
    v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    final int targetHeight = v.getMeasuredHeight();

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    v.getLayoutParams().height = 1;
    v.setVisibility(View.VISIBLE);
    Animation a = new Animation() {
      @Override
      protected void applyTransformation(float interpolatedTime, Transformation t) {
        v.getLayoutParams().height = interpolatedTime == 1
            ? ViewGroup.LayoutParams.WRAP_CONTENT
            : (int) (targetHeight * interpolatedTime);
        v.requestLayout();
      }

      @Override
      public boolean willChangeBounds() {
        return true;
      }
    };
    a.setAnimationListener(animationListener);
    // 1dp/ms
    a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
    v.startAnimation(a);
  }

  public static void collapse(final View v) {
    final int initialHeight = v.getMeasuredHeight();

    Animation a = new Animation() {
      @Override
      protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (interpolatedTime == 1) {
          v.setVisibility(View.GONE);
        } else {
          v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
          v.requestLayout();
        }
      }

      @Override
      public boolean willChangeBounds() {
        return true;
      }
    };

    // 1dp/ms
    a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
    v.startAnimation(a);
  }

  public static Spanned toSpanned(String text) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
    } else {
      return Html.fromHtml(text);
    }
  }

  public static void setupUI(View view, final Activity activity, List<View> viewExcludeList) {
    // Set up touch listener for non-text box views to hide keyboard.
    if (!(view instanceof EditText) && !viewExcludeList.contains(view)) {
      view.setOnTouchListener(new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
          InputMethodManager imm = (InputMethodManager)
              activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
          if (activity.getCurrentFocus() != null)
            activity.getCurrentFocus().clearFocus();
          return false;
        }
      });
    }

    //If a layout container, iterate over children and seed recursion.
    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        View innerView = ((ViewGroup) view).getChildAt(i);
        setupUI(innerView, activity, viewExcludeList);
      }
    }
  }

  public static void setPasswordFilter(EditText... editTexts) {
    InputFilter filter = new InputFilter() {
      public CharSequence filter(CharSequence source, int start, int end,
                                 Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
          if (Character.isWhitespace(source.charAt(i))) {
            return "";
          }
        }
        return null;
      }
    };
    for (EditText editText : editTexts) {
      editText.setFilters(new InputFilter[]{filter});
    }
  }

  /**
   * Hide keyboard of current focus view
   */
  public static void hideKeyboard(@NonNull Activity activity) {
    View view = activity.getCurrentFocus();
    if (view == null) {
      view = activity.findViewById(android.R.id.content);
    }
    hideKeyboard(activity, view);
    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
  }

  /**
   * Hide keyboard of current focus view
   */
  public static void hideKeyboard(@NonNull Context context, View view) {
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  /**
   * Show keyboard for {@link EditText}
   */
  public static void showKeyboard(@NonNull final EditText editText) {
    editText.post(new Runnable() {
      @Override
      public void run() {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
      }
    });
  }
}
