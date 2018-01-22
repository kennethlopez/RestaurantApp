package com.restaurantapp.ui.home.fragment.mapsinglerestaurant;


import com.google.android.gms.maps.model.LatLng;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseView;
import com.restaurantapp.util.LocationService;

import java.util.List;

class MapSingleRestaurantContract {
    interface View extends BaseView {
        void setActionBarTitle(String title);

        void setUpMap();

        void setMapCurrentLocation(LocationService.Location location, float zoom);

        void addMarker(Restaurant restaurant);

        void addPolyLines(List<LatLng> points);
    }

    interface Presenter {
        void onMapReady(Restaurant restaurant);
    }
}
