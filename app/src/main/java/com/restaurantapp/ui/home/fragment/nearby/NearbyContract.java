package com.restaurantapp.ui.home.fragment.nearby;


import com.google.android.gms.common.api.ResolvableApiException;
import com.restaurantapp.ui.base.BaseView;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.util.LocationService;

import java.util.List;

public interface NearbyContract {
    interface View extends BaseView {
        int PLACE_PICKER_REQUEST = 101;

        void startLocationPicker();

        void gotoRestaurantsDetails(Restaurant restaurant);

        void setSpinner(int textArrayResId, int selectedPosition);

        void initRecyclerView(List<Restaurant> places);

        void setSwipeRefresh();

        void setRefreshing(boolean refreshing);

        void updateRecyclerView(List<Restaurant> restaurantBundles);

        void scrollToPosition(int position);

        void onResolvableApiException(ResolvableApiException resolvable);

        void setActionBarTitle(String title);

        void setActionBarTitle(int resId);
    }

    interface Presenter {
        int POSITION_TOP = 0;

        void onRecyclerViewItemClick(int position);

        void onSpinnerItemSelected(int index);

        void onOptionsItemSelected(int itemId);

        void onRefresh();

        void onCurrentLocationPicked(LocationService.Location location);

        void fetchCurrentLocation();
    }
}