package com.restaurantapp.ui.home.fragment.favorites;



import android.text.TextUtils;

import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.RxUtil;
import com.restaurantapp.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.Collections;
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
    private String mFilter;
    private boolean mStart;
    private boolean mQueried;

    @Inject
    FavoritesPresenter (RestaurantRepository restaurantRepository, SharedPrefUtil sharedPrefUtil) {
        mRestaurantRepository = restaurantRepository;
        mSharedPrefUtil = sharedPrefUtil;
        mRestaurants = new ArrayList<>();
    }

    @Override
    public void attachView(FavoritesContract.View view) {
        super.attachView(view);
        getView().initRecyclerView(mRestaurants);
        getView().setSearchActionListener();
        mStart = true;
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
        if (mStart || favoritesUpdated(favorites)) {
            mFavorites = favorites;
            mQueried = true;

            mDisposable = mRestaurantRepository.fetchRestaurants(favorites)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(restaurants -> {
                        mRestaurants = restaurants;
                        if (!TextUtils.isEmpty(mFilter)) {
                            displayRestaurants(getFilteredRestaurants(mFilter));
                            mFilter = "";
                        } else displayRestaurants(restaurants);
                    }, Throwable::printStackTrace);
        }
    }

    @Override
    public void onRecyclerViewItemClick(int position) {
        getView().gotoRestaurantsDetails(mRestaurants.get(position));
    }

    @Override
    public void onSearchTextChanged(CharSequence charSequence, int start, int before, int count) {
        mQueried = false;

        if (count > 0) getView().showSearchClose();
        else getView().hideSearchClose();

        if (count > 2) {
            String filter = charSequence.toString().toLowerCase();

            // happens when user changes tab and leave texts on search then go back
            if (mFavorites.length > 0 && mRestaurants.size() == 0) {
                mFilter = filter;
                displayFavorites();
            } else displayRestaurants(getFilteredRestaurants(filter));

        } else displayRestaurants(mRestaurants);
    }

    private void displayRestaurants(List<Restaurant> restaurants) {
        if (mStart) mStart = false;

        getView().updateRecyclerView(restaurants);
        if (mQueried && restaurants.isEmpty()) {
            getView().displayEmptySnackBar(R.string.empty_favorites_text);
        }
    }

    private List<Restaurant> getFilteredRestaurants(String filter) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (Restaurant restaurant : mRestaurants) {
            String name = restaurant.getName();
            String vicinity = restaurant.getVicinity();
            if (name != null && name.toLowerCase().contains(filter) ||
                    vicinity != null && vicinity.toLowerCase().contains(filter)) {
                restaurants.add(restaurant);
            }
        }

        Collections.sort(restaurants, (restaurant, t1) -> {
            LocationService.Location currentLocation = mSharedPrefUtil.getCurrentLocation();
            double lat1 = restaurant.getGeometry()
                    .getLocation()
                    .getLat();

            double lng1 = restaurant.getGeometry()
                    .getLocation()
                    .getLng();

            double lat2 = t1.getGeometry()
                    .getLocation()
                    .getLat();

            double lng2 = t1.getGeometry()
                    .getLocation()
                    .getLng();

            double distance1 = currentLocation.distanceFrom(lat1, lng1);
            double distance2 = currentLocation.distanceFrom(lat2, lng2);
            if (distance1 > distance2) return 1;
            else if (distance1 < distance1) return -1;
            return 0;
        });

        return restaurants;
    }

    @Override
    public void actionSearch() {
        getView().hideKeyboard();
    }

    @Override
    public void onClickSearchClose() {
        getView().emptySearchText();
    }

    private boolean favoritesUpdated(String[] favorites) {
        if (favorites.length != mFavorites.length) return true;
        else {
            for (int c = 0; c < favorites.length; c++) {
                if (!favorites[c].contentEquals(mFavorites[c])) return true;
            }
        }
        return false;
    }
}