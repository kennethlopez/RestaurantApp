package com.restaurantapp.ui.home.fragment.nearby;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.google.android.gms.common.api.ResolvableApiException;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.event.EventBottomScrollRecyclerView;
import com.restaurantapp.data.event.EventError;
import com.restaurantapp.data.event.EventTurnOnLocation;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.AppUtil;
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
public class NearbyPresenter extends BasePresenter<NearbyContract.View> implements
        NearbyContract.Presenter, Constants.Methods, Constants.ErrorTypes {

    private final int[] mNearbyRadiusOptions = Constants.AppConstants.NEARBY_RADIUS_OPTIONS;

    private RestaurantRepository mRestaurantRepository;
    private SharedPrefUtil mSharedPref;
    private LocationService mLocationService;
    private RxBus mBus;

    private Disposable mDisposable;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private List<Restaurant> mRestaurants;
    private LocationService.Location mCurrentLocation;
    private String mPreviousNextPageToken;
    private String mCurrentNextPageToken;
    private int mNearbyRadius;
    private String mApiKey;
    private boolean mFirstSelection;
    private double mReconnectDelay;

    @Inject
    NearbyPresenter(RestaurantRepository repository, SharedPrefUtil sharedPref,
                           ResourceUtil resourceUtil, LocationService locationService,  RxBus bus) {
        mRestaurantRepository = repository;
        mSharedPref = sharedPref;
        mLocationService = locationService;
        mBus = bus;
        mApiKey = resourceUtil.getGoogleApiKey();
    }

    private void initData() {
        mFirstSelection = true;
        mRestaurants = mRestaurantRepository.getRestaurants();
        mNearbyRadius = mSharedPref.getNearbyRadius();
        mCurrentLocation = mSharedPref.getCurrentLocation();
    }

    @Override
    public void attachView(NearbyContract.View view) {
        super.attachView(view);
        initData();

        String locationAddress = mCurrentLocation.getAddress();
        int index = AppUtil.indexOf(mNearbyRadius, mNearbyRadiusOptions);

        view.setSpinner(R.array.array_nearby_radius, index);
        view.initRecyclerView(mRestaurants);
        view.setSwipeRefresh();

        if (!TextUtils.isEmpty(locationAddress)) view.setActionBarTitle(locationAddress);
        else view.setActionBarTitle(R.string.nearby_text);
        if (mRestaurants.size() == 0) {
            if (!mSharedPref.hasCurrentLocation()) fetchCurrentLocation();
            else nearbySearch();
        } else setRestaurant(POSITION_TOP);

        eventRecyclerViewBottomScrolled();
        eventTurnOnLocation();
    }

    @Override
    public void onRecyclerViewItemClick(int position) {
        getView().gotoRestaurantsDetails(mRestaurants.get(position));
    }

    @Override
    public void onSpinnerItemSelected(int index) {
        if (!mFirstSelection) {
            int radius = mNearbyRadiusOptions[index];
            if (mNearbyRadius != radius) {
                mSharedPref.setNearbyRadius(radius);
                mNearbyRadius = radius;
                nearbySearch();
            }
        }
        mFirstSelection = false;
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
    public void onRefresh() {
        nearbySearch();
    }

    @Override
    protected void onDestroy() {
        RxUtil.dispose(mCompositeDisposable);

        super.onDestroy();
    }

    @Override
    public void onCurrentLocationPicked(LocationService.Location location) {
        mCurrentLocation = location;
        mSharedPref.setCurrentLocation(location);
        if (isViewAttached()) {
            if (!TextUtils.isEmpty(location.getAddress())) {
                getView().setActionBarTitle(location.getAddress());
            } else getView().setActionBarTitle(R.string.nearby_text);
            nearbySearch();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void fetchCurrentLocation() {
        // TODO handle required permission
        refreshing(true);
        mLocationService.fetchCurrentLocation()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    if (location != null) onCurrentLocationPicked(location);
                    else refreshing(false);
                }, throwable -> {
                    refreshing(false);

                    if (throwable instanceof ResolvableApiException) {
                        getView().onResolvableApiException((ResolvableApiException) throwable);
                    }
                    else throwable.printStackTrace();
                });
    }

    private void nearbySearch() {
        RxUtil.dispose(mDisposable);
        mPreviousNextPageToken = mCurrentNextPageToken;

        int radius = AppUtil.kmToMeters(mNearbyRadius);
        String location = mCurrentLocation.latLngString();
        String type = Constants.AppConstants.PLACE_TYPE_RESTAURANT;

        refreshing(true);
        mDisposable = mRestaurantRepository.nearbySearch(location, radius, type, mApiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placeNearby -> {
                    mCurrentNextPageToken = placeNearby.getNextPageToken();

                    setRestaurant(POSITION_TOP);
                }, throwable -> handleThrowable(throwable, METHOD_SEARCH, POSITION_TOP));
    }

    private void nearbySearchWithToken(int position) {
        mPreviousNextPageToken = mCurrentNextPageToken;

        int radius = AppUtil.kmToMeters(mNearbyRadius);
        String location = mCurrentLocation.latLngString();
        String type = Constants.AppConstants.PLACE_TYPE_RESTAURANT;
        String key = mApiKey;

        refreshing(true);
        mDisposable = mRestaurantRepository.nearBySearchWithToken(location, radius, type, key,
                mCurrentNextPageToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placeNearby -> {
                    mCurrentNextPageToken = placeNearby.getNextPageToken();

                    // scroll to previous bottom position to avoid emitting consecutive bottom scrolled event
                    setRestaurant(position);
                }, throwable -> handleThrowable(throwable, METHOD_SEARCH_WITH_TOKEN, position));
    }

    private void setRestaurant(int position) {
        mRestaurants = mRestaurantRepository.getRestaurants();

        if (isViewAttached()) {
            refreshing(false);
            getView().updateRecyclerView(mRestaurants);
            getView().scrollToPosition(position);
        }
    }

    private void handleThrowable(Throwable throwable, String method, int position) {
        if (NetworkUtil.isConnectionError(throwable)) {
            RxUtil.dispose(mDisposable);

            mBus.post(new EventError(ERROR_CONNECTION, R.string.connection_error_message));
            mDisposable = RxUtil.reconnectDelay(mReconnectDelay)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aDouble -> {
                        mReconnectDelay = aDouble;
                        switch (method) {
                            case METHOD_SEARCH:
                                nearbySearch();
                                break;
                            case METHOD_SEARCH_WITH_TOKEN:
                                nearbySearchWithToken(position);
                                break;
                        }
                    });
        } else refreshing(false);

        throwable.printStackTrace();
    }

    private void eventRecyclerViewBottomScrolled() {
        mCompositeDisposable.add(mBus.filteredFlowable(EventBottomScrollRecyclerView.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recyclerViewBottomScrolledEvent -> {
                    if (mCurrentNextPageToken != null &&
                            !mCurrentNextPageToken.equals(mPreviousNextPageToken)) {
                        nearbySearchWithToken(recyclerViewBottomScrolledEvent.getPosition());
                    }
                },Throwable::printStackTrace));
    }

    private void eventTurnOnLocation() {
        mCompositeDisposable.add(mBus.filteredFlowable(EventTurnOnLocation.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(turnOnLocationEvent -> {
                    if (turnOnLocationEvent.isLocationEventTurnedOn()) fetchCurrentLocation();
                    // TODO do something if user did not turn on location
                }));
    }

    private void refreshing(boolean refreshing) {
        if (isViewAttached()) {
            getView().setRefreshing(refreshing);
        }
    }
}