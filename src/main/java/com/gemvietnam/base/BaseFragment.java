package com.gemvietnam.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;

/**
 * Base Fragment
 * Created by neo on 3/22/2016.
 */
public abstract class BaseFragment extends Fragment {
  protected View mRootView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mRootView = inflater.inflate(getLayoutId(), container, false);

    ButterKnife.bind(this, mRootView);
    Log.e("Test", this.getClass().getName());

    mRootView.setClickable(true);
    return mRootView;
  }

  protected final void hideKeyboard() {
    if ((getActivity()) != null && getActivity() instanceof BaseActivity)
      ((BaseActivity) getActivity()).hideKeyboard();
  }

  protected final void showUpKeyboard() {
    if (getActivity() == null)
      return;
    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  protected abstract int getLayoutId();

  protected abstract void startPresent();

  public abstract void onDisplay();
}