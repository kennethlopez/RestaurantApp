package com.restaurantapp.ui.base;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.restaurantapp.App;
import com.restaurantapp.data.event.EventError;
import com.restaurantapp.injection.component.ActivityComponent;
import com.restaurantapp.injection.component.ConfigPersistentComponent;
import com.restaurantapp.injection.component.DaggerConfigPersistentComponent;
import com.restaurantapp.injection.module.ActivityModule;
import com.restaurantapp.ui.home.HomePresenter;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.DialogFactory;
import com.restaurantapp.util.RxBus;
import com.restaurantapp.util.RxUtil;
import com.restaurantapp.util.SharedPrefUtil;

import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public abstract class BaseActivity extends AppCompatActivity implements Constants.ErrorTypes {
    private static final String TAG = "BaseActivity";
    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent>
            sComponentsMap = new LongSparseArray<>();

    private ActivityComponent mComponent;
    private long mActivityId;

    private Disposable mDisposable;
    private BasePresenter mPresenter;
    private RxBus mBus;
    private Dialog mDialog;
    private SharedPrefUtil mSharedPrefUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent = sComponentsMap.get(mActivityId, null);

        if (configPersistentComponent == null) {
            Log.d(TAG, String.format("Creating new ConfigPersistentComponent id=%d", mActivityId));
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .appComponent(App.get(this).getAppComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        }
        mComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
        mBus = mComponent.rxBus();
        mSharedPrefUtil = mComponent.sharedPrefUtil();
        onErrorEvent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    public void attachView() {}

    protected void attachPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        attachView();

        if (mPresenter != null) mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) mPresenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mPresenter != null) mPresenter.onCreateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        if (mPresenter != null) mPresenter.onPause();

        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) mPresenter.onStop();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();

        RxUtil.dispose(mDisposable);

        if (!isChangingConfigurations()) {
            Log.d(TAG, String.format("Clearing ConfigPersistentComponent id=%d", mActivityId));
            sComponentsMap.remove(mActivityId);
        }

        // connection over query limit dialog can be shown when app is reopened
        resetOverQueryLimitDialogFlag();

        super.onDestroy();
    }

    private void onErrorEvent() {
        RxUtil.dispose(mDisposable);

        mDisposable = mBus.filteredFlowable(EventError.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventError -> {
                    if (mDialog == null || !mDialog.isShowing()) {
                        switch (eventError.getErrorType()) {
                            case ERROR_OVER_QUERY_LIMIT:
                                if (!mSharedPrefUtil.displayedOverQueryLimitDialog()) {
                                    showDialog(eventError.getMessageResId(), (dialogInterface, i) ->
                                            // flag to display dialog for this error only once
                                            mSharedPrefUtil.setDisplayedOverQueryLimitDialog(true));
                                }
                                break;
                            case ERROR_CONNECTION:
                                if (mSharedPrefUtil.displayedConnectionErrorDialog()) break;
                                else {
                                    mSharedPrefUtil.setDisplayedConnectionErrorDialog(true);
                                    resetConnectionErrorDialogFlag();
                                }
                            default:
                                showDialog(eventError.getMessageResId(),
                                        (DialogInterface.OnClickListener) null);
                                break;
                        }
                    }
                });
    }

    private void showDialog(int res, DialogInterface.OnClickListener clickListener) {
        mDialog = DialogFactory.createGenericErrorDialog(this, res, clickListener);
        mDialog.show();
    }

    private void resetConnectionErrorDialogFlag() {
        // connection error dialog can be shown after 12 seconds
        new Handler().postDelayed(() ->
                mSharedPrefUtil.setDisplayedConnectionErrorDialog(false),
                AppUtil.secondToMil(12));
    }

    private void resetOverQueryLimitDialogFlag() {
        if (mPresenter instanceof HomePresenter) {
            mSharedPrefUtil.setDisplayedOverQueryLimitDialog(false);
        }
    }

    public ActivityComponent getComponent() {
        return mComponent;
    }

    protected App getApp() {
        return (App) getApplicationContext();
    }
}