package com.restaurantapp.ui.home.fragment.favorites;


import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;
import com.restaurantapp.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class FavoritesPresenter extends BasePresenter<FavoritesContract.View> implements
        FavoritesContract.Presenter {

    private RestaurantRepository mRestaurantRepository;
    private SharedPrefUtil mSharedPrefUtil;
    private String[] mFavoritesArray;
    private List<Restaurant> mRestaurants = new ArrayList<>();

    @Inject
    FavoritesPresenter (RestaurantRepository restaurantRepository, SharedPrefUtil sharedPrefUtil) {
        mRestaurantRepository = restaurantRepository;
        mSharedPrefUtil = sharedPrefUtil;
    }

    @Override
    public void attachView(FavoritesContract.View view) {
        super.attachView(view);
        mFavoritesArray = mSharedPrefUtil.getFavoritesArray();
        getView().initRecyclerView(mRestaurants);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRestaurants.clear();
        displayFavorites();
    }

    private void displayFavorites() {
        mRestaurantRepository.fetchRestaurants(mFavoritesArray)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurant -> {
                    mRestaurants.add(restaurant);
                    getView().updateRecyclerView(mRestaurants);
                }, Throwable::printStackTrace);
    }

    @Override
    public void onRecyclerViewItemClick(int position) {
        getView().gotoRestaurantsDetails(mRestaurants.get(position));
    }
}