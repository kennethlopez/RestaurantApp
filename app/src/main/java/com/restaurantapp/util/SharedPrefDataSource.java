package com.restaurantapp.util;


import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPrefDataSource implements SharedPrefUtil {
    private SharedPreferences mSharedPreferences;

    @Inject
    public SharedPrefDataSource(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public void setCurrentLocation(double lat, double lng) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(PREF_LAT, (float) lat);
        editor.putFloat(PREF_LNG, (float) lng);
        editor.apply();
    }

    @Override
    public LatLng getCurrentLocation() {
        return new LatLng(mSharedPreferences.getFloat(PREF_LAT, 0),
                mSharedPreferences.getFloat(PREF_LNG, 0));
    }

    @Override
    public void setCurrentLocationAddress(String address) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PREF_LOCATION_ADDRESS, address);
        editor.apply();
    }

    @Override
    public String getLocationAddress() {
        return mSharedPreferences.getString(PREF_LOCATION_ADDRESS, null);
    }

    @Override
    public boolean hasCurrentLocation() {
        LatLng latLng = getCurrentLocation();
        return latLng.getLat() != 0 && latLng.getLng() != 0;
    }

    @Override
    public void setNearbyRadius(int radius) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(PREF_NEARBY_RADIUS, radius);
        editor.apply();
    }

    @Override
    public int getNearbyRadius() {
        return mSharedPreferences.getInt(PREF_NEARBY_RADIUS,
                Constants.AppConstants.DEFAULT_NEARBY_RADIUS);
    }
}