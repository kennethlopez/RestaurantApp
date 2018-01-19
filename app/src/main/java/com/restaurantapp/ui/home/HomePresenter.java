package com.restaurantapp.ui.home;


import com.restaurantapp.R;
import com.restaurantapp.injection.ConfigPersistent;
import com.restaurantapp.ui.base.BasePresenter;
import com.restaurantapp.util.ResourceUtil;

import javax.inject.Inject;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter,
        HomeContract.FragmentTags, HomeContract.MenuItemIndexes {

    private String[] mToolbarTitles;
    private String mCurrentFragmentTag;

    @Inject
    HomePresenter(ResourceUtil resourceUtil) {
        mToolbarTitles = resourceUtil.getToolbarTitles();
        mCurrentFragmentTag = HomeContract.FragmentTags.FRAGMENT_NEARBY;
    }

    @Override
    public void attachView(HomeContract.View view) {
        super.attachView(view);

        view.setToolbar();
        view.setBottomNav();

        loadFragment(mCurrentFragmentTag);
    }

    @Override
    public boolean onNavigationItemSelected(int menuItemId) {
        String fragmentTag;

        switch (menuItemId) {
            default:
            case R.id.menu_bottom_nav_nearby:
                fragmentTag = FRAGMENT_NEARBY;
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

        if (!mCurrentFragmentTag.contentEquals(fragmentTag)) {
            mCurrentFragmentTag = fragmentTag;
            loadFragment(fragmentTag);
        }

        return true;
    }

    private void loadFragment(String fragmentTag) {
        int index;
        int fragmentId = HomeContract.FRAGMENT_CONTAINER_VIEW_ID;

        // not set action bar title are provided by their fragment
        switch (fragmentTag) {
            default:
            case FRAGMENT_NEARBY:
                getView().loadNearbyFragment(fragmentId, fragmentTag);
                break;

            case FRAGMENT_TASTES:
                index = MENU_ITEM_TASTES_INDEX;
                getView().setActionBarTitle(mToolbarTitles[index]);
                getView().loadTastesFragment(fragmentId, fragmentTag);
                break;
            case FRAGMENT_FAVORITES:
                index = MENU_ITEM_FAVORITES_INDEX;
                getView().setActionBarTitle(mToolbarTitles[index]);
                getView().loadFavoritesFragment(fragmentId, fragmentTag);
                break;
            case FRAGMENT_MAP:
                getView().loadMapFragment(fragmentId, fragmentTag);
                break;
        }
    }
}