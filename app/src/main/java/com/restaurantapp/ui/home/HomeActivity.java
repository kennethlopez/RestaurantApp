package com.restaurantapp.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseActivity;
import com.restaurantapp.data.event.EventTurnOnLocation;
import com.restaurantapp.ui.home.fragment.mapsinglerestaurant.MapSingleRestaurantFragment;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.RxBus;
import com.restaurantapp.ui.home.fragment.favorites.FavoritesFragment;
import com.restaurantapp.ui.home.fragment.nearby.NearbyFragment;
import com.restaurantapp.ui.home.fragment.map.MapFragment;
import com.restaurantapp.ui.home.fragment.tastes.TastesFragment;
import com.restaurantapp.ui.helper.BottomNavigationViewBehavior;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeContract.View {
    public static final int REQUEST_TURN_ON_LOCATION = 102;

    @Inject RxBus mBus;
    @Inject HomePresenter mPresenter;

    private NearbyFragment mNearbyFragment;
    private TastesFragment mTastesFragment;
    private FavoritesFragment mFavoritesFragment;
    private MapFragment mMapFragment;
    private MapSingleRestaurantFragment mMapSingleRestaurantFragment;

    private Handler mHandler = new Handler();
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getComponent().inject(this);
        ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();

        mPresenter.attachView(this);
        mPresenter.setApplicationWideBus(mBus);
        super.attachPresenter(mPresenter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // intent is called in NearbyFragment or MapFragment
            // EventTurnOnLocation is handled in FindPlacePresenter
            case REQUEST_TURN_ON_LOCATION:
                mBus.post(new EventTurnOnLocation(resultCode == RESULT_OK));
                break;
        }
    }

    @Override
    public void setToolbar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    public void setBottomNav() {
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) mBottomNav.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        mBottomNav.setOnNavigationItemSelectedListener(
                item -> mPresenter.onNavigationItemSelected(item.getItemId())
        );
    }

    @Override
    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void loadNearbyFragment(int fragmentId, String tag) {
        if (mNearbyFragment == null)
            mNearbyFragment = NearbyFragment.newInstance();
        AppUtil.loadFragmentFadeIn(fragmentId, mHandler, mFragmentManager, mNearbyFragment, tag);
        mBottomNav.setSelectedItemId(R.id.menu_bottom_nav_nearby);
    }

    @Override
    public void loadTastesFragment(int fragmentId, String tag) {
        if (mTastesFragment == null) mTastesFragment = TastesFragment.newInstance();
        AppUtil.loadFragmentFadeIn(fragmentId, mHandler, mFragmentManager, mTastesFragment, tag);
        mBottomNav.setSelectedItemId(R.id.menu_bottom_nav_tastes);
    }

    @Override
    public void loadFavoritesFragment(int fragmentId, String tag) {
        if (mFavoritesFragment == null) mFavoritesFragment = FavoritesFragment.newInstance();
        AppUtil.loadFragmentFadeIn(fragmentId, mHandler, mFragmentManager, mFavoritesFragment, tag);
        mBottomNav.setSelectedItemId(R.id.menu_bottom_nav_favorites);
    }

    @Override
    public void loadMapFragment(int fragmentId, String tag) {
        if (mMapFragment == null) mMapFragment = MapFragment.newInstance();

        AppUtil.loadFragmentFadeIn(fragmentId, mHandler, mFragmentManager, mMapFragment, tag);
        mBottomNav.setSelectedItemId(R.id.menu_bottom_nav_map);
    }

    @Override
    public void loadMapFragment(int fragmentId, String tag, Restaurant restaurant) {
        if (mMapSingleRestaurantFragment == null) {
            mMapSingleRestaurantFragment = MapSingleRestaurantFragment.newInstance(restaurant);
        } else {
            Bundle args = MapSingleRestaurantFragment.createArguments(restaurant);
            mMapSingleRestaurantFragment.setArguments(args);
        }

        AppUtil.loadFragmentFadeIn(fragmentId, mHandler, mFragmentManager, mMapSingleRestaurantFragment, tag);
        mBottomNav.setSelectedItemId(R.id.menu_bottom_nav_map);
    }

    @BindView(R.id.app_bar_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_home_bottom_nav) BottomNavigationView mBottomNav;
}