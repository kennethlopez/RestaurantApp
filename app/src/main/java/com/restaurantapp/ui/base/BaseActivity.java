package com.restaurantapp.ui.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.restaurantapp.App;
import com.restaurantapp.data.event.EventConnectionError;
import com.restaurantapp.injection.component.ActivityComponent;
import com.restaurantapp.injection.component.ConfigPersistentComponent;
import com.restaurantapp.injection.component.DaggerConfigPersistentComponent;
import com.restaurantapp.injection.module.ActivityModule;
import com.restaurantapp.util.DialogFactory;
import com.restaurantapp.util.RxBus;

import java.util.concurrent.atomic.AtomicLong;


public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent>
            sComponentsMap = new LongSparseArray<>();

    private ActivityComponent mComponent;
    private BasePresenter mPresenter;
    private RxBus mBus;
    private long mActivityId;

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
        onConnectionErrorEvent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    public abstract void attachView();

    protected void attachPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        attachView();

        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (mPresenter != null) {
            mPresenter.onPause();
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }

        if (!isChangingConfigurations()) {
            Log.d(TAG, String.format("Clearing ConfigPersistentComponent id=%d", mActivityId));
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    private void onConnectionErrorEvent() {
        mBus.filteredFlowable(EventConnectionError.class)
                .subscribe(connectionErrorEvent ->
                        DialogFactory.createGenericErrorDialog(this,
                                connectionErrorEvent.getMessageResId()));
    }

    public ActivityComponent getComponent() {
        return mComponent;
    }

    protected App getApp() {
        return (App) getApplicationContext();
    }
}