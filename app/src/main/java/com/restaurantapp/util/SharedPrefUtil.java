package com.restaurantapp.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.restaurantapp.injection.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPrefUtil implements Constants.SharedPrefConstants {
    private SharedPreferences mSharedPreferences;

    @Inject
    public SharedPrefUtil(@ApplicationContext Context context) {
        mSharedPreferences = context.getSharedPreferences(Constants.AppConstants.SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
    }

    public void setCurrentLocation(LocationService.Location location) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(PREF_LAT, (float) location.getLatLng().latitude);
        editor.putFloat(PREF_LNG, (float) location.getLatLng().longitude);
        editor.putString(PREF_LOCATION_ADDRESS, location.getAddress());
        editor.apply();
    }

    public LocationService.Location getCurrentLocation() {
        LocationService.Location location =
                new LocationService.Location(mSharedPreferences.getFloat(PREF_LAT, 0),
                        mSharedPreferences.getFloat(PREF_LNG, 0));
        location.setAddress(getLocationAddress());
        return location;
    }

    public boolean hasCurrentLocation() {
        LocationService.Location location = getCurrentLocation();
        return location.getLat() != 0 && location.getLng() != 0;
    }

    public void setNearbyRadius(int radius) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(PREF_NEARBY_RADIUS, radius);
        editor.apply();
    }

    public int getNearbyRadius() {
        return mSharedPreferences.getInt(PREF_NEARBY_RADIUS,
                Constants.AppConstants.DEFAULT_NEARBY_RADIUS);
    }

    public void setDisplayedOverQueryLimitDialog(boolean displayed) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(PREF_DISPLAYED_OVER_QUERY_LIMIT_DIALOG, displayed);
        editor.apply();
    }

    public boolean displayedOverQueryLimitDialog() {
        return mSharedPreferences.getBoolean(PREF_DISPLAYED_OVER_QUERY_LIMIT_DIALOG, false);
    }

    public void setDisplayedConnectionErrorDialog(boolean displayed) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(PREF_DISPLAYED_CONNECTION_ERROR_DIALOG, displayed);
        editor.apply();
    }

    public boolean displayedConnectionErrorDialog() {
        return mSharedPreferences.getBoolean(PREF_DISPLAYED_CONNECTION_ERROR_DIALOG, false);
    }

    private String getLocationAddress() {
        return mSharedPreferences.getString(PREF_LOCATION_ADDRESS, null);
    }

    public String[] getFavoritesArray() {
        return getFavoriteString().split(Constants.AppConstants.FAVORITES_SEPARATOR);
    }

    public void addFavorite(String placeId) {
        if (!getFavoriteString().isEmpty()) {

            setFavorites(getFavoriteString()
                    .concat(Constants.AppConstants.FAVORITES_SEPARATOR)
                    .concat(placeId));

        } else setFavorites(placeId);
    }

    public void removeFavorite(String placeId) {
        List<String> favorites = getFavorites();
        for (int c = 0; c < favorites.size(); c++) {
            if (favorites.get(c).contentEquals(placeId)) {
                favorites.remove(c);
                break;
            }
        }
        setFavorites(AppUtil.favoritesToString(favorites));
    }

    public boolean isFavorite(String placeId) {
        for (String favorite : getFavorites()) {
            if (favorite.contentEquals(placeId)) return true;
        }
        return false;
    }

    private List<String> getFavorites() {
        String[] strings = getFavoritesArray();

        List<String> favorites = new ArrayList<>();
        favorites.addAll(Arrays.asList(strings));

        return favorites;
    }

    private void setFavorites(String favorites) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PREF_FAVORITES, favorites);
        editor.apply();
    }

    private String getFavoriteString() {
        return mSharedPreferences.getString(PREF_FAVORITES, "");
    }

    public void setMapZoom(float zoom) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(PREF_MAP_ZOOM, zoom);
        editor.apply();
    }

    public float getMapZoom() {
        return mSharedPreferences.getFloat(PREF_MAP_ZOOM, Constants.AppConstants.DEFAULT_ZOOM);
    }
}