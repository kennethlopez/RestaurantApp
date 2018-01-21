package com.restaurantapp.ui.home.fragment.favorites;


import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseView;

import java.util.List;

public interface FavoritesContract {
    interface View extends BaseView {
        void initRecyclerView(List<Restaurant> restaurants);

        void initSwipeRefresh();

        void setSearchActionListener();

        void onSearchTextChanged(CharSequence charSequence, int start, int before, int count);

        void emptySearchText();

        void gotoRestaurantsDetails(Restaurant restaurantResponse);

        void updateRecyclerView(List<Restaurant> restaurants);

        void displayEmptySnackBar(int messageResId);

        void setRefreshing(boolean refreshing);

        void hideKeyboard();

        void showSearchClose();

        void hideSearchClose();
    }

    interface Presenter {
        void onRecyclerViewItemClick(int position);

        void onSearchTextChanged(CharSequence charSequence, int start, int before, int count);

        void actionSearch();

        void onClickSearchClose();
    }
}