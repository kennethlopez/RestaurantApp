package com.restaurantapp.ui.home.fragment.map;


import com.google.android.gms.common.api.ResolvableApiException;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseView;
import com.restaurantapp.util.LocationService;

import java.util.List;

public interface MapContract {
    interface View extends BaseView {
        int PLACE_PICKER_REQUEST = 101;

        void setActionBarTitle(String title);

        void setActionBarTitle(int resId);

        void startLocationPicker();

        void setupMap();

        void setMapCurrentLocation(LocationService.Location location, float zoom);

        void animateCameraPosition();

        void addMarkers(List<Restaurant> restaurants);

        void addMarker(Restaurant restaurant);

        void clearMap();

        void setSpinner(int textArrayResId, int position);

        void onResolvableApiException(ResolvableApiException e);

        void gotoRestaurantDetails(Restaurant restaurant);

        void showSnackBar(String message);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter {
        void onOptionsItemSelected(int itemId);

        void onCurrentLocationPicked(LocationService.Location location);

        void fetchCurrentLocation();

        void onMapReady();

        void onMarkerClicked(Restaurant restaurant);

        void onSpinnerItemSelected(int index);

        void setCurrentZoom(float zoom);
    }
}