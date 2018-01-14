package com.restaurantapp.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public final class LocationService {
    public static final int DEFAULT_INTERVAL = 10000;
    public static final int DEFAULT_FAST_INTERVAL = 5000;

    private Context mContext;
    private LocationSettingsRequest mLocationSettingsRequest;
    private PlaceDetectionClient mPlaceDetectionClient;

    private LocationService(Context context) {
        mContext = context;
        mPlaceDetectionClient = Places.getPlaceDetectionClient(context, null);
    }

    private void setLocationSettingsRequest(LocationSettingsRequest locationSettingsRequest) {
        mLocationSettingsRequest = locationSettingsRequest;
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public Single<Location> fetchCurrentLocation() {
        return Single.create(e -> LocationServices.getSettingsClient(mContext)
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnCompleteListener(task -> {
                    try {
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        if (response.getLocationSettingsStates().isLocationPresent()) {
                            getCurrentLocation(e);
                        }
                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                e.onError(resolvable);
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                e.onError(exception);
                                break;
                        }
                    }
                }));
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(SingleEmitter<Location> e) {
        mPlaceDetectionClient.getCurrentPlace(null)
                .addOnCompleteListener(task -> {
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    Place place = null;
                    float likelyHood = 0;
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        if (placeLikelihood.getLikelihood() > likelyHood) {
                            likelyHood = placeLikelihood.getLikelihood();
                            place = placeLikelihood.getPlace();
                        }
                    }
                    if (place != null) e.onSuccess(new Location(place));
                    else e.onSuccess(null);

                    likelyPlaces.release();
                })
                .addOnFailureListener(e::onError);
    }

    public static final class Builder {
        private final Context mContext;
        private LocationSettingsRequest.Builder mBuilder;

        public Builder(Context context) {
            mContext = context;
            mBuilder = new LocationSettingsRequest.Builder();
        }

        public final LocationService.Builder addLocationRequest(@NonNull LocationRequest request) {
            mBuilder.addLocationRequest(request);
            return this;
        }

        @SuppressWarnings("unused")
        public final LocationService.Builder addDefaultLocationSettingsRequest() {
            mBuilder.addLocationRequest(newLocationRequest(DEFAULT_INTERVAL, DEFAULT_FAST_INTERVAL,
                    LocationRequest.PRIORITY_HIGH_ACCURACY))
                    .addLocationRequest(newLocationRequest(DEFAULT_INTERVAL, DEFAULT_FAST_INTERVAL,
                            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY));
            return this;
        }

        public final LocationService build() {
            LocationService locationService = new LocationService(mContext);
            locationService.setLocationSettingsRequest(mBuilder.build());

            return locationService;
        }
    }

    public static LocationRequest newLocationRequest(long interval, long fastestInterval,
                                                     int priority) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(fastestInterval);
        locationRequest.setPriority(priority);
        return locationRequest;
    }

    public static class Location {
        private Place mPlace;

        public Location(Place place) {
            mPlace = place;
        }

        public LatLng getLatLng() {
            return new LatLng(mPlace.getLatLng().latitude, mPlace.getLatLng().longitude);
        }

        public String getAddress() {
            return String.valueOf(mPlace.getAddress());
        }
    }
}