package com.restaurantapp.ui.restaurant.detail;


import android.text.TextUtils;

import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Photo;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.event.EventError;
import com.restaurantapp.data.event.EventOpenRestaurantOnMap;
import com.restaurantapp.data.event.exception.OverQueryLimitException;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.NetworkUtil;
import com.restaurantapp.util.RxBus;
import com.restaurantapp.util.RxUtil;
import com.restaurantapp.util.SharedPrefUtil;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class RestaurantDetailsPresenter extends BasePresenter<RestaurantDetailsContract.View>
        implements RestaurantDetailsContract.Presenter, Constants.ErrorTypes {

    private RestaurantRepository mRestaurantRepository;
    private RxBus mBus;
    private RxBus mApplicationWideBus;

    private Disposable mDisposable;
    private Restaurant mRestaurant;
    private String mApiKey;
    private SharedPrefUtil mSharedPrefUtil;
    private boolean mIsFavorite;
    private boolean mIsOptionsMenuCreated;
    private double mReconnectDelay;

    @Inject
    RestaurantDetailsPresenter(RestaurantRepository repository, SharedPrefUtil sharedPrefUtil,
                                      RxBus bus) {
        mRestaurantRepository = repository;
        mBus = bus;
        mSharedPrefUtil = sharedPrefUtil;
        mRestaurant = mRestaurantRepository.getRestaurant();
    }

    @Override
    public void setPlaceResponse(Restaurant restaurant, String apiKey) {
        mApiKey = apiKey;
        mIsOptionsMenuCreated = false;

        getView().setToolbar();
        getView().setTitle(restaurant.getName());
        getView().setName(restaurant.getName());
        getView().setAddress(restaurant.getVicinity());
        getView().setWebsite(restaurant.getWebsite());
        getView().showProgressBars();

        if (mRestaurant != null && mRestaurant.equals(restaurant)) {
            setRestaurant(mRestaurant, true);
            details(restaurant);
        } else {
            mRestaurant = restaurant;
            details(restaurant);
        }
    }

    @Override
    protected void onCreateOptionsMenu() {
        super.onCreateOptionsMenu();
        mIsOptionsMenuCreated = true;
        setFavorite();
    }

    @Override
    public void optionsItemSelected(int itemId) {
        switch (itemId) {
            case R.id.menu_restaurant_details_favorite:
                if (!mIsFavorite) {
                    mSharedPrefUtil.addFavorite(mRestaurant.getPlaceId());
                    getView().showSnackBar(R.string.added_to_favorites_text);
                } else {
                    mSharedPrefUtil.removeFavorite(mRestaurant.getPlaceId());
                    getView().showSnackBar(R.string.removed_to_favorites_text);
                }
                setFavorite();
                break;
            case R.id.menu_restaurant_details_map:
                mApplicationWideBus.post(new EventOpenRestaurantOnMap(mRestaurant));
                getView().closeActivity();
                break;
        }
    }

    @Override
    public void setApplicationWideBus(RxBus bus) {
        mApplicationWideBus = bus;
    }

    @Override
    protected void onDestroy() {
        RxUtil.dispose(mDisposable);

        super.onDestroy();
    }

    private void details(Restaurant restaurant) {
        RxUtil.dispose(mDisposable);

        if (restaurant != null) {
            boolean hasPhotos = false;
            boolean hasReviews = false;
            List<Photo> photos = restaurant.getPhotos();

            if (photos != null && photos.size() > 0) {
                setPhotoFrom(restaurant.getPhotos());

                if (photos.size() > 1) hasPhotos = true;

            } else getView().hidePhotosContainer();

            if (restaurant.getRating() > 0) hasReviews = true;
            else getView().hideReviewsContainer();

            if (hasPhotos || hasReviews) {
                mDisposable = mRestaurantRepository.detailsSearch(restaurant.getPlaceId(), mApiKey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(placeDetails -> setRestaurant(placeDetails.getResult(), false),
                                throwable -> handleThrowable(restaurant, throwable));
            } else getView().hidePhotosContainer();
        } else hidePhotoAndReviewContainer();
    }

    private void setRestaurant(Restaurant restaurant, boolean setPhoto) {
        mRestaurant = restaurant;

        List<Photo> photos;
        if (setPhoto) photos = setPhotoFrom(restaurant.getPhotos());
        else photos = removeFirstAvailablePhoto(restaurant.getPhotos());

        if (photos != null && photos.size() > 0) getView().setPhotos(photos);
        else getView().hidePhotosContainer();

        setFavorite();
        getView().setReviews(restaurant.getReviews());
        getView().setPhotos(photos);
        getView().hideProgressBars();
    }

    private void setFavorite() {
        mIsFavorite = mSharedPrefUtil.isFavorite(mRestaurant.getPlaceId());

        if (mIsOptionsMenuCreated) {
            if (mIsFavorite) getView().setFavoritesMenuItemTint(R.color.colorAccent);
            else getView().setFavoritesMenuItemTint(android.R.color.white);
        }
    }

    private List<Photo> setPhotoFrom(List<Photo> photos) {
        if (photos != null) {
            Photo photo = getFirstPhoto(photos);
            if (photo != null) {
                setPhoto(photo);
                photos.remove(photo);
            }
        }
        return photos;
    }

    private void setPhoto(Photo photo) {
        int maxWidth = photo.getWidth();
        int height = AppUtil.dpToPx(120);
        String url = AppUtil.getGooglePhotosLink(photo.getPhotoReference(), maxWidth, height, mApiKey);

        getView().setPhoto(url);
    }

    private List<Photo> removeFirstAvailablePhoto(List<Photo> photos) {
        if (photos != null) photos.remove(getFirstPhoto(photos));
        return photos;
    }

    private Photo getFirstPhoto(List<Photo> photos) {
        if (photos != null) {
            for (Photo photo : photos) {
                if (photo != null && !TextUtils.isEmpty(photo.getPhotoReference())) return photo;
            }
        }
        return null;
    }

    private void handleThrowable(Restaurant restaurant, Throwable throwable) {
        RxUtil.dispose(mDisposable);

        if (NetworkUtil.isConnectionError(throwable)) {
            mBus.post(new EventError(ERROR_CONNECTION, R.string.connection_error_message));
            mDisposable = RxUtil.reconnectDelay(mReconnectDelay)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aDouble -> {
                        mReconnectDelay = aDouble;
                        details(mRestaurant);
                    });

        } else if (throwable instanceof OverQueryLimitException) {
            // get restaurant from local db
            mDisposable = mRestaurantRepository.fetchRestaurant(restaurant.getPlaceId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(placeDetails -> setRestaurant(restaurant, true));
        }
        throwable.printStackTrace();
    }

    private void hidePhotoAndReviewContainer() {
        getView().hidePhotosContainer();
        getView().hideReviewsContainer();
    }
}