package com.gemvietnam.base.viper.interfaces;

/**
 * Views can present on a {@link ContainerView}
 * Created by neo on 9/15/2016.
 */
public interface PresentView<P extends IPresenter> extends IView<P> {
  void showProgress();

  void hideProgress();

  void showToast(String message);

  void showToast(int id_message);

  void showAlertDialog(String message);

  void onRequestError(String errorCode, String errorMessage);

  void onNetworkError(boolean shouldShowPopup);

  void onRequestSuccess();

  //Check is top fragment of current activity or not
  boolean isShowing();

  // Check fragment is visible or hidden
  boolean isViewHidden();
}