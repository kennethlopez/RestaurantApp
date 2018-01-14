package com.restaurantapp.ui.home.fragment.nearby;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.common.api.ResolvableApiException;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.PlaceResponse;
import com.restaurantapp.data.api.response.PlacesNearbyResponse;
import com.restaurantapp.data.event.EventConnectionError;
import com.restaurantapp.data.event.EventBottomScrollRecyclerView;
import com.restaurantapp.data.event.EventTurnOnLocation;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.NetworkUtil;
import com.restaurantapp.util.RxBus;
import com.restaurantapp.util.RxUtil;
import com.restaurantapp.util.LatLng;
import com.restaurantapp.util.SharedPrefDataSource;
import com.restaurantapp.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class NearbyPresenter extends BasePresenter<NearbyContract.View> implements
        NearbyContract.Presenter {
    private static final String TAG = "FindPlaceFragment";

    private RestaurantRepository mRestaurantRepository;
    private SharedPrefUtil mSharedPref;
    private RxBus mBus;
    private LocationService mLocationService;
    private Disposable mDisposable;
    private List<PlaceResponse> mPlaces = new ArrayList<>();
    private LatLng mCurrentLocation;
    private String mPreviousNextPageToken;
    private String mCurrentNextPageToken;
    private int mNearbyRadius;
    private int[] mNearbyRadiusOptions = Constants.AppConstants.NEARBY_RADIUS_OPTIONS;

    @Inject
    public NearbyPresenter(RestaurantRepository repository, SharedPrefDataSource sharedPref, RxBus bus) {
        mRestaurantRepository = repository;
        mSharedPref = sharedPref;
        mBus = bus;
    }

    @Override
    public void attachView(NearbyContract.View view) {
        super.attachView(view);
        String locationAddress = mSharedPref.getLocationAddress();

        mLocationService = view.getLocationService();
        mCurrentLocation = mSharedPref.getCurrentLocation();
        mNearbyRadius = mSharedPref.getNearbyRadius();

        view.setSpinner(R.array.array_nearby_radius,
                AppUtil.indexOf(mNearbyRadius, mNearbyRadiusOptions));
        view.initRecyclerView(mPlaces);

        if (locationAddress != null) view.setActionBarTitle(locationAddress);
        if (mPlaces.size() == 0) {
            if (!mSharedPref.hasCurrentLocation()) fetchCurrentLocation();
            else nearbySearch();
        }

        eventRecyclerViewBottomScrolled();
        eventTurnOnLocation();
    }

    @Override
    public void onRecyclerViewItemClick() {

    }

    @Override
    public void onSpinnerItemSelected(int index) {
        int radius = mNearbyRadiusOptions[index];
        if (mNearbyRadius != radius) {
            mSharedPref.setNearbyRadius(radius);
            mNearbyRadius = radius;
            nearbySearch();
        }
    }

    @Override
    public void onOptionsItemSelected(int itemId) {
        switch (itemId) {
            case R.id.menu_find_place_fragment_search:
                getView().startLocationPicker();
                break;
            case R.id.menu_find_place_fragment_info:
                Log.d(TAG, "onOptionsItemSelected: info clicked");
                break;
        }
    }

    @Override
    public void onCurrentLocationPicked(LocationService.Location location) {
        mCurrentLocation = location.getLatLng();
        mSharedPref.setCurrentLocationAddress(location.getAddress());
        mSharedPref.setCurrentLocation(mCurrentLocation.getLat(), mCurrentLocation.getLng());
        getView().setActionBarTitle(location.getAddress());
        nearbySearch();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void fetchCurrentLocation() {
        // TODO handle required permission
        showProgressBar();
        mLocationService.fetchCurrentLocation()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    if (location != null) onCurrentLocationPicked(location);
                    else hideProgressBars();
                }, throwable -> {
                    hideProgressBars();

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
        String location = mCurrentLocation.getLat() + "," + mCurrentLocation.getLng();
        String type = Constants.AppConstants.PLACE_TYPE_RESTAURANT;
        String key = Constants.AppConstants.GOOGLE_API_KEY;

        showProgressBar();
        mDisposable = mRestaurantRepository.nearbySearch(location, radius, type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placesNearbyResponse -> {
                            mPlaces = placesNearbyResponse.getResults();
                            processNearbyResponse(placesNearbyResponse, POSITION_TOP);
                        }, this::handleThrowable);
    }

    private void nearbySearchWithToken(int position) {
        mPreviousNextPageToken = mCurrentNextPageToken;

        int radius = AppUtil.kmToMeters(mNearbyRadius);
        String location = mCurrentLocation.getLat() + "," + mCurrentLocation.getLng();
        String type = Constants.AppConstants.PLACE_TYPE_RESTAURANT;
        String key = Constants.AppConstants.GOOGLE_API_KEY;

        showProgressBar();
        mDisposable = mRestaurantRepository.nearBySearchWithToken(location, radius, type, key,
                mCurrentNextPageToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placesNearbyResponse -> {
                    // append result so that we can still view previous queried data
                    mPlaces.addAll(placesNearbyResponse.getResults());

                    // scroll to previous bottom position to avoid emitting consecutive bottom scrolled event
                    processNearbyResponse(placesNearbyResponse, position);
                }, this::handleThrowable);
    }

    private void processNearbyResponse(PlacesNearbyResponse placesNearbyResponse, int position) {
        mCurrentNextPageToken = placesNearbyResponse.getNextPageToken();
        if (getView() != null) {
            getView().updateRecyclerView(mPlaces);
            getView().scrollToPosition(position);
            hideProgressBars();
        }
    }

    private void eventRecyclerViewBottomScrolled() {
        mBus.filteredFlowable(EventBottomScrollRecyclerView.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recyclerViewBottomScrolledEvent -> {
                    if (mCurrentNextPageToken != null &&
                            !mCurrentNextPageToken.equals(mPreviousNextPageToken)) {
                        nearbySearchWithToken(recyclerViewBottomScrolledEvent.getPosition());
                    }
                },Throwable::printStackTrace);
    }

    private void eventTurnOnLocation() {
        mBus.filteredFlowable(EventTurnOnLocation.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(turnOnLocationEvent -> {
                    if (turnOnLocationEvent.isLocationEventTurnedOn()) fetchCurrentLocation();
                    // TODO do something if user did not turn on location
                });
    }

    private void handleThrowable(Throwable throwable) {
        hideProgressBars();
        if (NetworkUtil.isConnectionError(throwable)) mBus.post(
                new EventConnectionError(R.string.connection_error_message));
    }

    private void showProgressBar() {
        // show small progress bar if already displayed data
        if (getView() != null) {
            if (mPlaces.size() > 0) getView().showSmallProgressBar();
            else getView().showProgressBar();
        }
    }

    private void hideProgressBars() {
        if (getView() != null) {
            getView().hideProgressBar();
            getView().hideSmallProgressBar();
        }
    }
}