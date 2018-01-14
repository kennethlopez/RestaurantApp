package com.restaurantapp.ui.home.fragment.nearby;


import com.google.android.gms.common.api.ResolvableApiException;
import com.restaurantapp.ui.base.BaseView;
import com.restaurantapp.data.api.response.PlaceResponse;
import com.restaurantapp.util.LocationService;

import java.util.List;

public interface NearbyContract {
    interface View extends BaseView {
        void startLocationPicker();

        void setSpinner(int textArrayResId, int selectedPosition);

        void initRecyclerView(List<PlaceResponse> places);

        void updateRecyclerView(List<PlaceResponse> restaurantBundles);

        LocationService getLocationService();

        String getApiKey(int resId);

        void scrollToPosition(int position);

        void onResolvableApiException(ResolvableApiException resolvable);

        void setActionBarTitle(String title);

        void showProgressBar();

        void hideProgressBar();

        void showSmallProgressBar();

        void hideSmallProgressBar();
    }

    interface Presenter {
        int POSITION_TOP = 0;

        void onRecyclerViewItemClick();

        void onSpinnerItemSelected(int index);

        void onOptionsItemSelected(int itemId);

        void onCurrentLocationPicked(LocationService.Location location);

        void fetchCurrentLocation();
    }
}