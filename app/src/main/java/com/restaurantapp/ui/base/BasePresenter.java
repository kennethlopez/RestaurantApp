package com.restaurantapp.ui.base;



public abstract class BasePresenter <T extends BaseView> implements Presenter<T> {
    private T mView;

    @Override
    public void attachView(T view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public boolean isViewAttached() {
        return mView != null;
    }

    protected final T getView() {
        return mView;
    }

    protected void checkViewAttached() {
        if (!isViewAttached()) throw new ViewNotAttachedException("Please call Presenter.attachView(BaseView) before" +
                " requesting data to the Presenter");
    }

    // Activity or Fragment lifecycle. called in BaseActivity or BaseFragment
    protected  void onCreateOptionsMenu() {}

    protected void onStart() {}

    protected void onResume() {}

    protected void onPause() {}

    protected void onStop() {}

    protected void onDestroy() {
        detachView();
    }

    public static class ViewNotAttachedException extends RuntimeException {
        public ViewNotAttachedException(String message) {
            super(message);
        }
    }
}