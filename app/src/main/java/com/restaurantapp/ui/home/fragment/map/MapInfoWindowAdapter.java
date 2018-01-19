package com.restaurantapp.ui.home.fragment.map;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.view.holder.RestaurantViewHolder;

import java.util.Hashtable;

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final Hashtable<String, Boolean> mMarkerSet;
    private final Hashtable<String, Restaurant> mRestaurantSet;

    private Hashtable<String, Drawable> mDrawableSet = new Hashtable<>();
    private Context mContext;
    private RestaurantViewHolder mHolder;
    private String mApiKey;
    private String mDisplayedMarkerId = "";

    MapInfoWindowAdapter(Context context, Hashtable<String, Boolean> markerSet,
                                Hashtable<String, Restaurant> restaurantSet, String apiKey) {
        mContext = context;
        mMarkerSet = markerSet;
        mRestaurantSet = restaurantSet;
        mApiKey = apiKey;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            View view = inflater.inflate(R.layout.item_restaurant, null);
            boolean isImageLoaded = mMarkerSet.get(marker.getId());
            Restaurant restaurant = mRestaurantSet.get(marker.getId());
            mDisplayedMarkerId = marker.getId();
            if (isImageLoaded) {
                mHolder = new RestaurantViewHolder(mContext, view);
                mHolder.setRestaurant(restaurant, mApiKey);
                mHolder.setImage(mDrawableSet.get(marker.getId()));
            } else {
                mMarkerSet.put(marker.getId(), true);
                mHolder = new RestaurantViewHolder(mContext, view);
                mHolder.setRestaurant(restaurant, mApiKey, new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        mDrawableSet.put(marker.getId(), resource);
                        mHolder.setImage(resource);
                        if (marker.getId().contentEquals(mDisplayedMarkerId)) marker.showInfoWindow();
                    }
                });
            }

            return view;
        }
        return null;
    }
}