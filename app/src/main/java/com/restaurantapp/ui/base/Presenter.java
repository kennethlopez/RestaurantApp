package com.restaurantapp.ui.base;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the {@link BaseView} type that wants to be attached with.
 */
public interface Presenter<V extends BaseView>  {
    void attachView(V view);

    void detachView();
}
