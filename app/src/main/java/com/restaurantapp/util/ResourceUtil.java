package com.restaurantapp.util;


import android.content.Context;

import com.restaurantapp.R;
import com.restaurantapp.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ResourceUtil {

    private Context mContext;

    @Inject
    public ResourceUtil(@ApplicationContext Context context) {
        mContext = context;
    }

    public String getGoogleApiKey() {
        return mContext.getResources().getString(R.string.google_api_key);
    }

    public String[] getToolbarTitles() {
        return mContext.getResources().getStringArray(R.array.bottom_nav_toolbar_titles);
    }

    public String getString(int resId) {
        return mContext.getResources().getString(resId);
    }
}