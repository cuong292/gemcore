package com.gemvietnam.base.viper.interfaces;


import android.app.Activity;
import android.os.Bundle;

import com.gemvietnam.base.BaseActivity;

/**
 * Base View
 * Created by neo on 2/5/2016.
 */
public interface IView<P extends IPresenter> {
  void initLayout();

  BaseActivity getBaseActivity();

  Activity getViewContext();

  Bundle getData();

  P getPresenter();

  void setPresenter(P presenter);
}
