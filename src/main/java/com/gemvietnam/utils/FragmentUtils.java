package com.gemvietnam.utils;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.gemvietnam.base.CoreDefault;
import com.gemvietnam.base.viper.ViewFragment;

import static com.gemvietnam.base.CoreDefault.ANIM_ENTER_LEFT_TO_RIGHT;
import static com.gemvietnam.base.CoreDefault.ANIM_ENTER_RIGHT_TO_LEFT;
import static com.gemvietnam.base.CoreDefault.ANIM_EXIT_LEFT_TO_RIGHT;
import static com.gemvietnam.base.CoreDefault.ANIM_EXIT_RIGHT_TO_LEFT;
import static com.google.common.base.Preconditions.checkNotNull;

public class FragmentUtils {

  public static boolean isBackStackContains(FragmentManager fragmentManager, String tag) {
    int backStackEntryCount = fragmentManager.getBackStackEntryCount();
    if (backStackEntryCount <= 0)
      return false;
    for (int i = 0; i < backStackEntryCount; i++) {
      FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
      if (backStackEntry.getName().equalsIgnoreCase(tag))
        return true;
    }
    return false;
  }

  public static void addFirstFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
    addFragment(fragmentManager, fragment, CoreDefault.FRAGMENT_CONTAINER_ID, false, false, fragment.getClass().getName(), false, true);
  }

  public static void pushView(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
    addFragment(fragmentManager, fragment, CoreDefault.FRAGMENT_CONTAINER_ID, true, true, fragment.getClass().getName(), false, false);
  }

  public static void pushView(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, AnimValue animValue) {
    addFragment(fragmentManager, fragment, CoreDefault.FRAGMENT_CONTAINER_ID, true, animValue.getValue(), fragment.getClass().getName(), false, false);
  }

  public static void pushView(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, BackStackValue backStackValue) {
    addFragment(fragmentManager, fragment, CoreDefault.FRAGMENT_CONTAINER_ID, backStackValue.getValue(), true, fragment.getClass().getName(), false, false);
  }

  public static void pushView(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, BackStackValue backStackValue, AnimValue animValue) {
    addFragment(fragmentManager, fragment, CoreDefault.FRAGMENT_CONTAINER_ID, backStackValue.getValue(), animValue.getValue(), fragment.getClass().getName(), false, false);
  }

  public static void pushChildView(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
    addFragment(fragmentManager, fragment, frameId, true, true, fragment.getClass().getName(), false, false);
  }

  public static void pushChildView(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, AnimValue animValue) {
    addFragment(fragmentManager, fragment, frameId, true, animValue.getValue(), fragment.getClass().getName(), false, false);
  }

  public static void pushChildView(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, BackStackValue backStackValue) {
    addFragment(fragmentManager, fragment, frameId, backStackValue.getValue(), true, fragment.getClass().getName(), false, false);
  }

  public static void pushChildView(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, BackStackValue backStackValue, AnimValue animValue) {
    addFragment(fragmentManager, fragment, frameId, backStackValue.getValue(), animValue.getValue(), fragment.getClass().getName(), false, false);
  }

  public static void addFragment(@NonNull FragmentManager fragmentManager,
                                 @NonNull Fragment fragment, int frameId, boolean
                                     addToBackStack, boolean withAnim, String tag, boolean loadExisted, boolean replace) {
    checkNotNull(fragmentManager);
    checkNotNull(fragment);
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    if (withAnim) {
      transaction.setCustomAnimations(ANIM_ENTER_RIGHT_TO_LEFT, ANIM_EXIT_RIGHT_TO_LEFT, ANIM_ENTER_LEFT_TO_RIGHT, ANIM_EXIT_LEFT_TO_RIGHT);
    }
    if (addToBackStack) {
      transaction.addToBackStack(tag);
    }
    if (loadExisted) {
      final Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
      if (existingFragment != null) {
        for (Fragment f : fragmentManager.getFragments()) {
          transaction.hide(f);
        }
        transaction.show(existingFragment);
        if (existingFragment instanceof ViewFragment) {
          new Handler().post(new Runnable() {
            @Override
            public void run() {
              ((ViewFragment) existingFragment).onDisplay();
            }
          });
        }
      } else {
        if (replace) {
          transaction.replace(frameId, fragment, tag);
        } else {
          transaction.add(frameId, fragment, tag);
        }
      }
    } else {
      if (replace) {
        transaction.replace(frameId, fragment, tag);
      } else {
        transaction.add(frameId, fragment, tag);
      }
    }
    transaction.commitAllowingStateLoss();
  }

  public enum BackStackValue {
    TRUE(true), FALSE(false);
    boolean value;

    BackStackValue(boolean value) {
      this.value = value;
    }

    public boolean getValue() {
      return value;
    }
  }

  public enum AnimValue {
    TRUE(true), FALSE(false);
    boolean value;

    AnimValue(boolean value) {
      this.value = value;
    }

    public boolean getValue() {
      return value;
    }
  }
}