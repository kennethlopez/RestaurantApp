package com.restaurantapp.ui.home.fragment.mapsinglerestaurant;


import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.event.EventError;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.NetworkUtil;
import com.restaurantapp.util.ResourceUtil;
import com.restaurantapp.util.RxBus;
import com.restaurantapp.util.RxUtil;
import com.restaurantapp.util.SharedPrefUtil;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.restaurantapp.util.Constants.ErrorTypes.ERROR_CONNECTION;

@ConfigPersistent
public class MapSingleRestaurantPresenter extends BasePresenter<MapSingleRestaurantContract.View>
        implements MapSingleRestaurantContract.Presenter {

    private RestaurantRepository mRestaurantRepository;
    private RxBus mBus;

    private Disposable mDisposable;
    private Restaurant mRestaurant;
    private String mApiKey;
    private String mCurrentLatLng;
    private double mReconnectDelay;

    @Inject
    MapSingleRestaurantPresenter(RestaurantRepository restaurantRepository,
                                 SharedPrefUtil sharedPrefUtil, ResourceUtil resourceUtil,
                                 RxBus bus) {

        mRestaurantRepository = restaurantRepository;
        mBus = bus;

        mApiKey = resourceUtil.getGoogleApiKey();
        mCurrentLatLng = sharedPrefUtil.getCurrentLocation().latLngString();
    }

    @Override
    public void attachView(MapSingleRestaurantContract.View view) {
        super.attachView(view);
        getView().setUpMap();
    }

    @Override
    protected void onDestroy() {
        RxUtil.dispose(mDisposable);

        super.onDestroy();
    }

    @Override
    public void onMapReady(Restaurant restaurant) {
        mRestaurant = restaurant;

        double lat = restaurant.getGeometry()
                .getLocation()
                .getLat();

        double lng = restaurant.getGeometry()
                .getLocation()
                .getLng();

        LocationService.Location location = new LocationService.Location(lat, lng);
        getView().setActionBarTitle(restaurant.getName());
        getView().setMapCurrentLocation(location, Constants.AppConstants.DEFAULT_ZOOM);
        getView().addMarker(restaurant);

        getPolyLines();
    }

    private void getPolyLines() {
        RxUtil.dispose(mDisposable);

        mDisposable = mRestaurantRepository.getPolyLines(mCurrentLatLng, mRestaurant.getPlaceId(),
                mApiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(points -> getView().addPolyLines(points), this::onError);
    }

    private void onError(Throwable throwable) {

        if (NetworkUtil.isConnectionError(throwable)) {
            RxUtil.dispose(mDisposable);

            mBus.post(new EventError(ERROR_CONNECTION, R.string.connection_error_message));
            mDisposable = RxUtil.reconnectDelay(mReconnectDelay)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aDouble -> {
                        mReconnectDelay = aDouble;
                        getPolyLines();
                    });
        }

        throwable.printStackTrace();
    }
}