package com.restaurantapp.ui.home.fragment.map;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.google.android.gms.common.api.ResolvableApiException;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.PlaceNearby;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.event.EventError;
import com.restaurantapp.data.event.EventTurnOnLocation;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.NetworkUtil;
import com.restaurantapp.util.ResourceUtil;
import com.restaurantapp.util.RxBus;
import com.restaurantapp.util.RxUtil;
import com.restaurantapp.util.SharedPrefUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class MapPresenter extends BasePresenter<MapContract.View> implements MapContract.Presenter,
        Constants.ErrorTypes {

    private final int[] mNearbyRadiusOptions = Constants.AppConstants.NEARBY_RADIUS_OPTIONS;

    private RestaurantRepository mRestaurantRepository;
    private SharedPrefUtil mSharedPref;
    private ResourceUtil mResourceUtil;
    private LocationService mLocationService;
    private RxBus mBus;

    private List<Restaurant> mRestaurants;
    private LocationService.Location mCurrentLocation;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable mDisposable;
    private String mApiKey;
    private boolean mMapReady;
    private boolean mMarkersAdded;
    private boolean mFirstSelection;
    private int mNearbyRadius;
    private float mZoom;
    private boolean mClearMap;
    private double mReconnectDelay;
    private String mNextPageToken;

    @Inject
    MapPresenter(RestaurantRepository restaurantRepository, SharedPrefUtil sharedPref,
                        ResourceUtil resourceUtil, LocationService locationService, RxBus bus) {
        mRestaurantRepository = restaurantRepository;
        mSharedPref = sharedPref;
        mLocationService = locationService;
        mBus = bus;
        mResourceUtil = resourceUtil;

        mApiKey = resourceUtil.getGoogleApiKey();
        mRestaurants = mRestaurantRepository.getRestaurants();
        mNearbyRadius = mSharedPref.getNearbyRadius();
        mCurrentLocation = mSharedPref.getCurrentLocation();
        mZoom = mSharedPref.getMapZoom();
    }

    @Override
    public void attachView(MapContract.View view) {
        super.attachView(view);
        mMapReady = false;
        mMarkersAdded = false;
        mFirstSelection = true;

        String locationAddress = mCurrentLocation.getAddress();
        int index = AppUtil.indexOf(mNearbyRadius, mNearbyRadiusOptions);

        view.setupMap();
        view.setSpinner(R.array.array_nearby_radius, index);

        if (!TextUtils.isEmpty(locationAddress)) view.setActionBarTitle(locationAddress);
        else view.setActionBarTitle(R.string.nearby_text);

        if (mRestaurants.size() == 0) {
            if (!mSharedPref.hasCurrentLocation()) fetchCurrentLocation();
            else nearbySearch(false);
        }

        RxUtil.dispose(mCompositeDisposable);
        eventTurnOnLocation();
    }

    @Override
    public void onOptionsItemSelected(int itemId) {
        switch (itemId) {
            case R.id.menu_nearby_fragment_search:
                getView().startLocationPicker();
                break;
            case R.id.menu_nearby_fragment_info:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        RxUtil.dispose(mDisposable);
        RxUtil.dispose(mCompositeDisposable);

        super.onDestroy();
    }

    @Override
    public void onCurrentLocationPicked(LocationService.Location location) {
        mCurrentLocation = location;
        mSharedPref.setCurrentLocation(location);

        if (isViewAttached()) {
            getView().showProgressBar();
            if (!TextUtils.isEmpty(location.getAddress())) {
                getView().setActionBarTitle(location.getAddress());
                getView().setMapCurrentLocation(mCurrentLocation, mZoom);
                getView().animateCameraPosition();
            } else getView().setActionBarTitle(R.string.nearby_text);
        }
        nearbySearch(true);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void fetchCurrentLocation() {
        RxUtil.dispose(mDisposable);
        // TODO handle required permission

        getView().showProgressBar();
        mDisposable = mLocationService.fetchCurrentLocation()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    if (location != null) onCurrentLocationPicked(location);
                }, throwable -> {
                    if (throwable instanceof ResolvableApiException) {
                        getView().onResolvableApiException((ResolvableApiException) throwable);
                    }
                    else onError(throwable);
                });
    }

    @Override
    public void onMapReady() {
        mMapReady = true;
        getView().setMapCurrentLocation(mCurrentLocation, mZoom);
        getView().animateCameraPosition();

        if (!mMarkersAdded) getView().addMarkers(mRestaurants);
        if (mRestaurants.size() > 0) {
            setRestaurants(false);
            nearbySearch(true);
        }
    }

    @Override
    public void onMarkerClicked(Restaurant restaurant) {
        getView().gotoRestaurantDetails(restaurant);
    }

    @Override
    public void onSpinnerItemSelected(int index) {
        if (!mFirstSelection) {
            int radius = mNearbyRadiusOptions[index];
            if (mNearbyRadius != radius) {
                mSharedPref.setNearbyRadius(radius);
                mNearbyRadius = radius;
                nearbySearch(true);
            }
        }
        mFirstSelection = false;
    }

    @Override
    public void setCurrentZoom(float zoom) {
        mZoom = zoom;
        mSharedPref.setMapZoom(zoom);
    }

    private void nearbySearch(boolean clearMap) {
        RxUtil.dispose(mDisposable);

        mClearMap = clearMap;
        int radius = AppUtil.kmToMeters(mNearbyRadius);
        String type = Constants.AppConstants.PLACE_TYPE_RESTAURANT;
        String location = mCurrentLocation.latLngString();

        getView().showProgressBar();
        mDisposable = mRestaurantRepository.nearbySearch(location, radius, type, mApiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placeNearby -> setPlaceNearby(placeNearby, clearMap),
                        this::onError);
    }

    private void nearbySearchWithToken(String nextPageToken) {
        if (!TextUtils.isEmpty(nextPageToken)) {
            int radius = AppUtil.kmToMeters(mNearbyRadius);
            String type = Constants.AppConstants.PLACE_TYPE_RESTAURANT;
            String location = mCurrentLocation.latLngString();

            mDisposable = mRestaurantRepository.nearBySearchWithToken(location, radius, type,
                    mApiKey, nextPageToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(placeNearby -> setPlaceNearby(placeNearby, false),
                            this::onError);
        } else getView().hideProgressBar();
    }

    private void setPlaceNearby(PlaceNearby nearby, boolean clearMap) {
        mRestaurants = mRestaurantRepository.getRestaurants();
        mNextPageToken = nearby.getNextPageToken();
        setRestaurants(clearMap);
        nearbySearchWithToken(nearby.getNextPageToken());
    }

    private void setRestaurants(boolean clearMap) {
        mRestaurants = mRestaurantRepository.getRestaurants();
        if (mMapReady) {

            mMarkersAdded = true;
            String displayed = mResourceUtil.getString(R.string.displayed_text);
            String restaurant = mResourceUtil.getString(R.string.restaurants_text);
            String message = displayed.concat(" ")
                    .concat(String.valueOf(mRestaurants.size()))
                    .concat(" ")
                    .concat(restaurant);

            if (clearMap) getView().clearMap();

            getView().addMarkers(mRestaurants);
            getView().showSnackBar(message);
        }
    }

    private void eventTurnOnLocation() {
        mCompositeDisposable.add(mBus.filteredFlowable(EventTurnOnLocation.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(turnOnLocationEvent -> {
                    if (turnOnLocationEvent.isLocationEventTurnedOn()) fetchCurrentLocation();
                    // TODO do something if user did not turn on location
                }));
    }

    private void onError(Throwable throwable) {
        getView().hideProgressBar();

        if (NetworkUtil.isConnectionError(throwable)) {
            RxUtil.dispose(mDisposable);

            mBus.post(new EventError(ERROR_CONNECTION, R.string.connection_error_message));
            mDisposable = RxUtil.reconnectDelay(mReconnectDelay)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aDouble -> {
                        mReconnectDelay = aDouble;
                        if (TextUtils.isEmpty(mNextPageToken)) nearbySearch(mClearMap);
                        else nearbySearchWithToken(mNextPageToken);
                    });
        }

        throwable.printStackTrace();
    }
}