package com.gemvietnam.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.EditText;

import com.gemvietnam.utils.DialogUtils;
import com.gemvietnam.utils.ViewUtils;

import java.util.List;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Base activity
 * Created by neo on 2/5/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

  private OnBackStackChangedListener mOnBackStackChangedListener = new OnBackStackChangedListener() {
    @Override
    public void onBackStackChanged() {
      onFragmentDisplay();
    }
  };

  public static Fragment getTopFragment(FragmentManager manager) {
    if (manager != null) {
      if (manager.getBackStackEntryCount() > 0) {
        FragmentManager.BackStackEntry backStackEntry = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1);
        return manager.findFragmentByTag(backStackEntry.getName());
      } else {
        List<Fragment> fragments = manager.getFragments();
        if (!fragments.isEmpty()) {
          return fragments.get(0);
        }
      }
    }
    return null;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    // Inject views
    ButterKnife.bind(this);

    parseArgs(getIntent());

    // Prepare layout
    initLayout();
    getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
  }

  public void parseArgs(Intent intent) {

  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    getSupportFragmentManager().removeOnBackStackChangedListener(mOnBackStackChangedListener);
  }

  private void onFragmentDisplay() {
    new Handler().post(new Runnable() {
      @Override
      public void run() {
        Fragment fragment = getTopFragment(getSupportFragmentManager());
        if (fragment instanceof BaseFragment) {
          ((BaseFragment) fragment).onDisplay();
        }
      }
    });
  }

  public void showAlertDialog(String message) {
    DialogUtils.showErrorAlert(this, message);
  }

  public void showProgress() {
    DialogUtils.showProgressDialog(this);
  }

  public void hideProgress() {
    DialogUtils.dismissProgressDialog();
  }

  public void onRequestError(String errorCode, String errorMessage) {
    DialogUtils.showErrorAlert(this, errorMessage);
    hideProgress();
  }

  public void onRequestSuccess() {
    hideProgress();
  }

  protected abstract int getLayoutId();

  public void hideKeyboard() {
    ViewUtils.hideKeyboard(this);
  }

  public void showKeyboard(EditText editText) {
    ViewUtils.showKeyboard(editText);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    FragmentManager manager = getSupportFragmentManager();
    onHandleFragmentResult(manager, requestCode, resultCode, data);
  }

  private void onHandleFragmentResult(FragmentManager manager, int requestCode, int resultCode, Intent data) {
    if (manager != null && !manager.getFragments().isEmpty()) {
      for (Fragment fragment : manager.getFragments()) {
        if (fragment != null) {
          fragment.onActivityResult(requestCode, resultCode, data);
          FragmentManager childManager = fragment.getChildFragmentManager();
          onHandleFragmentResult(childManager, requestCode, resultCode, data);
        }
      }
    }
  }

  public abstract void initLayout();
}
