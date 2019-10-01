package com.gemvietnam.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.gemvietnam.base.viper.interfaces.ContainerView;
import com.gemvietnam.base.viper.interfaces.IPresenter;
import com.gemvietnam.base.viper.interfaces.IView;
import com.gemvietnam.common.R;
import com.gemvietnam.utils.FragmentUtils;

/**
 * Base Fragment
 * Created by neo on 3/22/2016.
 */
public abstract class ContainerActivity extends BaseActivity implements ContainerView {
  /**
   * Return layout resource id for activity
   * Override if you using other layout
   */
  @Override
  public int getLayoutId() {
    return R.layout.container;
  }

  @Override
  public void pushView(IView view) {
    if (view instanceof BaseFragment) {
      FragmentUtils.pushView(getSupportFragmentManager(), (Fragment) view);
    }
  }

  @Override
  public void pushChildView(IView view, int frameId, FragmentManager childFragmentManager, FragmentUtils.AnimValue animValue) {
    FragmentUtils.pushChildView(childFragmentManager, (Fragment) view, frameId, animValue);
  }

  @Override
  public void pushChildView(IView view, int frameId, FragmentManager childFragmentManager) {
    FragmentUtils.pushChildView(childFragmentManager, (Fragment) view, frameId);
  }

  @Override
  public void initLayout() {
    FragmentUtils.addFirstFragmentToActivity(getSupportFragmentManager(), onCreateFirstFragment());
  }

  @Override
  public IPresenter getPresenter() {
    return null;
  }

  @Override
  public void setPresenter(IPresenter presenter) {
    // Nothing to do
  }

  @Override
  public BaseActivity getBaseActivity() {
    return this;
  }

  @Override
  public Bundle getData() {
    return getIntent().getExtras();
  }

  @Override
  public Activity getViewContext() {
    return this;
  }

  @Override
  public void back() {
    FragmentManager manager = getSupportFragmentManager();
    if (manager.getBackStackEntryCount() > 0) {
      manager.popBackStack();
    } else {
      finish();
    }
  }

  @Override
  public void onBackPressed() {
    back();
  }
}
