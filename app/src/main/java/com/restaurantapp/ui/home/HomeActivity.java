package com.restaurantapp.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.restaurantapp.R;
import com.restaurantapp.ui.base.BaseActivity;
import com.restaurantapp.data.event.EventTurnOnLocation;
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
    @Inject RxBus mBus;
    @Inject HomePresenter mPresenter;

    private NearbyFragment mNearbyFragment;
    private TastesFragment mTastesFragment;
    private FavoritesFragment mFavoritesFragment;
    private MapFragment mMapFragment;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void attachView() {
        getComponent().inject(this);
        ButterKnife.bind(this);

        mPresenter.attachView(this);
        super.attachPresenter(mPresenter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // intent is called in FindPlaceFragment
            // EventTurnOnLocation is handled in FindPlacePresenter
            case NearbyFragment.REQUEST_TURN_ON_LOCATION:
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
    public void setToolbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public String[] getToolbarTitles(int resId) {
        return getResources().getStringArray(resId);
    }

    @Override
    public void loadFindPlaceFragment(int fragmentId, String tag) {
        if (mNearbyFragment == null) {
            mNearbyFragment = NearbyFragment.newInstance();
        }

        loadFragment(fragmentId, mNearbyFragment, tag);
    }

    @Override
    public void loadTastesFragment(int fragmentId, String tag) {
        if (mTastesFragment == null) {
            mTastesFragment = TastesFragment.newInstance();
        }

        loadFragment(fragmentId, mTastesFragment, tag);
    }

    @Override
    public void loadFavoritesFragment(int fragmentId, String tag) {
        if (mFavoritesFragment == null) {
            mFavoritesFragment = FavoritesFragment.newInstance();
        }

        loadFragment(fragmentId, mFavoritesFragment, tag);
    }

    @Override
    public void loadMapFragment(int fragmentId, String tag) {
        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance();
        }

        loadFragment(fragmentId, mMapFragment, tag);
    }

    private void loadFragment(final int fragmentId, final Fragment fragment, final String tag) {

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable runnable = () -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(fragmentId, fragment, tag);
            transaction.commitAllowingStateLoss();
        };

        mHandler.post(runnable);
    }

    @BindView(R.id.app_bar_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_main_bottom_nav) BottomNavigationView mBottomNav;
}
