package com.restaurantapp.ui.home.fragment.favorites;


import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;
import com.restaurantapp.util.RxUtil;
import com.restaurantapp.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class FavoritesPresenter extends BasePresenter<FavoritesContract.View> implements
        FavoritesContract.Presenter {

    private RestaurantRepository mRestaurantRepository;
    private SharedPrefUtil mSharedPrefUtil;
    private String[] mFavorites;
    private List<Restaurant> mRestaurants = new ArrayList<>();
    private Disposable mDisposable;

    @Inject
    FavoritesPresenter (RestaurantRepository restaurantRepository, SharedPrefUtil sharedPrefUtil) {
        mRestaurantRepository = restaurantRepository;
        mSharedPrefUtil = sharedPrefUtil;
    }

    @Override
    public void attachView(FavoritesContract.View view) {
        super.attachView(view);
        getView().initRecyclerView(mRestaurants);
        mFavorites = mSharedPrefUtil.getFavoritesArray();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayFavorites();
    }

    @Override
    protected void onDestroy() {
        RxUtil.dispose(mDisposable);

        super.onDestroy();
    }

    private void displayFavorites() {
        RxUtil.dispose(mDisposable);

        String[] favorites = mSharedPrefUtil.getFavoritesArray();
        if (favoritesUpdated(favorites)) {
            mDisposable = mRestaurantRepository.fetchRestaurants(favorites)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(restaurants -> {
                        mRestaurants = restaurants;
                        getView().updateRecyclerView(restaurants);
                        if (restaurants.isEmpty()) {
                            getView().displayEmptySnackBar(R.string.empty_favorites_text);
                        }
                    }, Throwable::printStackTrace);
        }
    }

    @Override
    public void onRecyclerViewItemClick(int position) {
        getView().gotoRestaurantsDetails(mRestaurants.get(position));
    }

    private boolean favoritesUpdated(String[] favorites) {
        if (favorites.length != mFavorites.length) return true;
        else {
            for (int c = 0; c < favorites.length; c++) {
                if (favorites[c].contentEquals(mFavorites[c])) return true;
            }
        }
        return true;
    }
}