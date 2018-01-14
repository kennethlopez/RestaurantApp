package com.restaurantapp.ui.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import android.view.View;

import com.restaurantapp.App;
import com.restaurantapp.injection.component.ActivityComponent;
import com.restaurantapp.injection.component.ConfigPersistentComponent;
import com.restaurantapp.injection.component.DaggerConfigPersistentComponent;
import com.restaurantapp.injection.module.ActivityModule;

import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent>
            sComponentsMap = new LongSparseArray<>();

    private ActivityComponent mComponent;
    private BasePresenter mPresenter;
    private long mActivityId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent = sComponentsMap.get(mActivityId, null);

        if (configPersistentComponent == null) {
            Log.d(TAG, String.format("Creating new ConfigPersistentComponent id=%d", mActivityId));
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .appComponent(App.get(getActivity()).getAppComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        }
        mComponent = configPersistentComponent.activityComponent(new ActivityModule(getActivity()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachView(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }

        if (!getActivity().isChangingConfigurations()) {
            Log.d(TAG, String.format("Clearing ConfigPersistentComponent id=%d", mActivityId));
            sComponentsMap.remove(mActivityId);
        }
    }

    protected abstract void attachView(View view);

    protected void attachPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }

    public ActivityComponent getComponent() {
        return mComponent;
    }

    protected App getApp() {
        return (App) getActivity().getApplicationContext();
    }
}
