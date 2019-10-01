package com.gemvietnam.base.viper.interfaces;

import android.support.v4.app.FragmentManager;

import com.gemvietnam.base.viper.ViewFragment;
import com.gemvietnam.utils.FragmentUtils;


/**
 * Container view that place fragments on it
 * Created by neo on 9/15/2016.
 */
public interface ContainerView extends IView {
  ViewFragment onCreateFirstFragment();

  void pushView(IView view);

  void pushChildView(IView view, int frameId, FragmentManager childFragmentManager, FragmentUtils.AnimValue animValue);

  void pushChildView(IView view, int frameId, FragmentManager childFragmentManager);

  void back();
}