package com.restaurantapp.util;


public interface SharedPrefUtil {
    String PREF_LAT = "pref_lat";
    String PREF_LNG = "pref_lng";
    String PREF_LOCATION_ADDRESS = "pref_location_address";
    String PREF_NEARBY_RADIUS = "pref_nearby_radius";

    void setCurrentLocation(double lat, double lng);

    LatLng getCurrentLocation();

    void setCurrentLocationAddress(String address);

    String getLocationAddress();

    boolean hasCurrentLocation();

    void setNearbyRadius(int radius);

    int getNearbyRadius();
}
