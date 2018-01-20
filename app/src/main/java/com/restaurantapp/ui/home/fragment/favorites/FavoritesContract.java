package com.restaurantapp.ui.home.fragment.favorites;


import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseView;

import java.util.List;

public interface FavoritesContract {
    interface View extends BaseView {
        void initRecyclerView(List<Restaurant> restaurants);

        void gotoRestaurantsDetails(Restaurant restaurantResponse);

        void updateRecyclerView(List<Restaurant> restaurants);

        void displayEmptySnackBar(int messageResId);
    }

    interface Presenter {
        void onRecyclerViewItemClick(int position);
    }
}
