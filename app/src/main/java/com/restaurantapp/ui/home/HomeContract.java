package com.restaurantapp.ui.home;


import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseView;
import com.restaurantapp.util.RxBus;

public interface HomeContract {
    int FRAGMENT_CONTAINER_VIEW_ID = R.id.activity_home_frame;

    interface FragmentTags {
        String FRAGMENT_NEARBY = "HOME_CONTRACT_FRAGMENT_NEARBY";
        String FRAGMENT_TASTES = "HOME_CONTRACT_FRAGMENT_TASTES";
        String FRAGMENT_FAVORITES = "HOME_CONTRACT_FRAGMENT_FAVORITES";
        String FRAGMENT_MAP = "HOME_CONTRACT_FRAGMENT_MAP";
        String FRAGMENT_MAP_SINGLE_RESTAURANT = "HOME_FRAGMENT_MAP_SINGLE_RESTAURANT";
    }

    interface MenuItemIndexes {
        int MENU_ITEM_TASTES_INDEX = 1;
        int MENU_ITEM_FAVORITES_INDEX = 2;
    }

    interface View extends BaseView {
        void setToolbar();

        void setBottomNav();

        void setActionBarTitle(String title);

        void loadNearbyFragment(int fragmentId, String tag);

        void loadTastesFragment(int fragmentId, String tag);

        void loadFavoritesFragment(int fragmentId, String tag);

        void loadMapFragment(int fragmentId, String tag);

        void loadMapFragment(int fragmentId, String tag, Restaurant restaurant);
    }

    interface Presenter {
        boolean onNavigationItemSelected(int menuItemId);

        void setApplicationWideBus(RxBus bus);
    }
}