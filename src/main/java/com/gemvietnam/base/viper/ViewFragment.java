package com.gemvietnam.base.viper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gemvietnam.base.BaseActivity;
import com.gemvietnam.base.BaseFragment;
import com.gemvietnam.base.ContainerActivity;
import com.gemvietnam.base.viper.interfaces.IPresenter;
import com.gemvietnam.base.viper.interfaces.PresentView;
import com.gemvietnam.common.R;
import com.gemvietnam.utils.ContextUtils;
import com.gemvietnam.utils.DialogUtils;
import com.gemvietnam.utils.NetworkUtils;
import com.gemvietnam.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragments that stand for View
 * Created by neo on 9/15/2016.
 */
public abstract class ViewFragment<P extends IPresenter>
    extends BaseFragment implements PresentView<P> {
  protected P mPresenter;
  protected boolean mIsInitialized = false;
  protected boolean mIsStarted = false;
  private boolean mViewHidden;
  private Toast mToast;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      mPresenter.registerEventBus();
    } catch (NullPointerException e) {
      getBaseActivity().finishAffinity();
      Intent i = getBaseActivity().getBaseContext().getPackageManager()
          .getLaunchIntentForPackage(getBaseActivity().getBaseContext().getPackageName());
      assert i != null;
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(i);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    try {
      mPresenter.unregisterEventBus();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    if (!mIsInitialized) {
      mRootView = super.onCreateView(inflater, container, savedInstanceState);

      // Prepare layout
      if (getArguments() != null) {
        parseArgs(getArguments());
      }

      initLayout();

      if (isTouchHideKeyboard()) {
        ViewUtils.setupUI(mRootView, getActivity(), getViewNotHideKeyboard());
      }
      mIsInitialized = true;
    }

    return mRootView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (!mIsStarted) {
      startPresent();
    }
  }

  @Override
  protected void startPresent() {
    try {
      mPresenter.start();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    mIsStarted = true;
  }

  @Override
  public void showProgress() {
    if (ContextUtils.isValidContext(getBaseActivity())) {
      getBaseActivity().showProgress();
    }
  }

  @Override
  public void hideProgress() {
    if (ContextUtils.isValidContext(getBaseActivity())) {
      getBaseActivity().hideProgress();
    }
  }

  @Override
  public void initLayout() {

  }

  @Override
  public void showAlertDialog(String message) {
    if (ContextUtils.isValidContext(getBaseActivity())) {
      getBaseActivity().showAlertDialog(message);
    }
  }

  @Override
  public BaseActivity getBaseActivity() {
    if (getActivity() instanceof BaseActivity) {
      return (BaseActivity) getActivity();
    } else {
      return null;
    }
  }

  @Override
  public void onRequestError(String errorCode, String errorMessage) {
    if (ContextUtils.isValidContext(getBaseActivity())) {
      getBaseActivity().onRequestError(errorCode, errorMessage);
    }
  }

  @Override
  public void onNetworkError(boolean shouldShowPopup) {
    if (!NetworkUtils.isNoNetworkAvailable(getActivity(), shouldShowPopup)) {
      DialogUtils.showErrorAlert(getActivity(), getString(R.string.msg_network_lost));
    }
  }

  @Override
  public void onRequestSuccess() {
    if (ContextUtils.isValidContext(getBaseActivity())) {
      getBaseActivity().onRequestSuccess();
    }
  }

  @Override
  public Activity getViewContext() {
    return getActivity();
  }

  @Override
  public Bundle getData() {
    return getArguments();
  }

  @Override
  public P getPresenter() {
    return mPresenter;
  }

  @Override
  public void setPresenter(P presenter) {
    mPresenter = presenter;
  }

  protected void parseArgs(Bundle args) {

  }

  @Override
  public void onDisplay() {
    try {
      mPresenter.onFragmentDisplay();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    mViewHidden = hidden;
  }

  @Override
  public boolean isViewHidden() {
    return mViewHidden;
  }

  @Override
  public boolean isShowing() {
    return isResumed() && this == BaseActivity.getTopFragment(getFragmentManager());
  }

  public ContainerActivity getContainerActivity() {
    if (getActivity() instanceof ContainerActivity)
      return (ContainerActivity) getActivity();
    return null;
  }

  @Override
  public void showToast(String msg) {
    if (mToast != null) {
      mToast.cancel();
    }
    mToast = Toast.makeText(getViewContext(), msg, Toast.LENGTH_LONG);
    mToast.show();
  }

  @Override
  public void showToast(int id_msg) {
    if (mToast != null) {
      mToast.cancel();
    }
    mToast = Toast.makeText(getViewContext(), id_msg, Toast.LENGTH_LONG);
    mToast.show();
  }

  public boolean isTouchHideKeyboard() {
    return true;
  }

  public List<View> getViewNotHideKeyboard() {
    return new ArrayList<>();
  }
}