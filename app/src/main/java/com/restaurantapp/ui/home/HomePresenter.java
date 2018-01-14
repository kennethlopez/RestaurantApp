package com.restaurantapp.ui.home;


import com.restaurantapp.R;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter,
        HomeContract.FragmentTags, HomeContract.MenuItemIndexes {

    private String[] mToolbarTitles;
    private String mFragmentTag = "";

    @Inject
    public HomePresenter() {}

    @Override
    public void attachView(HomeContract.View view) {
        super.attachView(view);
        mToolbarTitles = view.getToolbarTitles(R.array.bottom_nav_toolbar_titles);

        view.setToolbar();
        view.setBottomNav();

        loadFragment(FRAGMENT_FIND_PLACE);
    }

    @Override
    public boolean onNavigationItemSelected(int menuItemId) {
        String fragmentTag;

        switch (menuItemId) {
            default:
            case R.id.menu_bottom_nav_find_place:
                fragmentTag = FRAGMENT_FIND_PLACE;
                break;
            case R.id.menu_bottom_nav_tastes:
                fragmentTag = FRAGMENT_TASTES;
                break;
            case R.id.menu_bottom_nav_favorites:
                fragmentTag = FRAGMENT_FAVORITES;
                break;
            case R.id.menu_bottom_nav_map:
                fragmentTag = FRAGMENT_MAP;
                break;
        }

        if (!mFragmentTag.contentEquals(fragmentTag)) {
            mFragmentTag = fragmentTag;
            loadFragment(fragmentTag);
        }

        return true;
    }

    private void loadFragment(String fragmentTag) {
        int index;
        int fragmentId = R.id.activity_main_frame;

        switch (fragmentTag) {
            default:
            case FRAGMENT_FIND_PLACE:
                index = MENU_ITEM_FIND_PLACE_INDEX;
                getView().loadFindPlaceFragment(fragmentId, fragmentTag);
                break;

            case FRAGMENT_TASTES:
                index = MENU_ITEM_TASTES_INDEX;
                getView().loadTastesFragment(fragmentId, fragmentTag);
                break;
            case FRAGMENT_FAVORITES:
                index = MENU_ITEM_FAVORITES_INDEX;
                getView().loadFavoritesFragment(fragmentId, fragmentTag);
                break;
            case FRAGMENT_MAP:
                index = MENU_ITEM_MAP_INDEX;
                getView().loadMapFragment(fragmentId, fragmentTag);
                break;
        }

        getView().setToolbarTitle(mToolbarTitles[index]);
    }
}
