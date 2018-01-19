package com.restaurantapp.ui.home;


import com.restaurantapp.R;
import com.restaurantapp.ui.base.BaseView;

public interface HomeContract {
    int FRAGMENT_CONTAINER_VIEW_ID = R.id.activity_home_frame;

    interface FragmentTags {
        String FRAGMENT_NEARBY = "HOME_CONTRACT_FRAGMENT_NEARBY";
        String FRAGMENT_TASTES = "HOME_CONTRACT_FRAGMENT_TASTES";
        String FRAGMENT_FAVORITES = "HOME_CONTRACT_FRAGMENT_FAVORITES";
        String FRAGMENT_MAP = "HOME_CONTRACT_FRAGMENT_MAP";
    }

    interface MenuItemIndexes {
        int MENU_ITEM_FIND_PLACE_INDEX = 0;
        int MENU_ITEM_TASTES_INDEX = 1;
        int MENU_ITEM_FAVORITES_INDEX = 2;
        int MENU_ITEM_MAP_INDEX = 3;
    }

    interface View extends BaseView {
        void setToolbar();

        void setBottomNav();

        void setActionBarTitle(String title);

        void loadNearbyFragment(int fragmentId, String tag);

        void loadTastesFragment(int fragmentId, String tag);

        void loadFavoritesFragment(int fragmentId, String tag);

        void loadMapFragment(int fragmentId, String tag);
    }

    interface Presenter {
        boolean onNavigationItemSelected(int menuItemId);
    }
}