package com.restaurantapp.ui.base;


import com.restaurantapp.injection.component.AppComponent;

public abstract class BasePresenter <T extends BaseView> {
    private T mView;
    private AppComponent mAppComponent;

    public void attachView(T view) {
        mView = view;
    }

    public void detachView() {
        mView = null;
        mAppComponent = null;
    }

    public boolean isViewAttached() {
        return mView != null;
    }

    protected final T getView() {
        return mView;
    }

    protected final AppComponent getComponent() {
        return mAppComponent;
    }

    // Activity or Fragment lifecycle. called in BaseActivity or BaseFragment

    protected void onStart() {}

    protected void onResume() {}

    protected void onPause() {}

    protected void onStop() {}

    protected void onDestroy() {
        detachView();
    }
}