package com.restaurantapp.injection.component;

import com.restaurantapp.injection.ActivityScope;
import com.restaurantapp.injection.module.ActivityModule;
import com.restaurantapp.ui.base.BaseActivity;
import com.restaurantapp.ui.home.HomeActivity;
import com.restaurantapp.ui.home.fragment.favorites.FavoritesFragment;
import com.restaurantapp.ui.home.fragment.map.MapFragment;
import com.restaurantapp.ui.home.fragment.nearby.NearbyFragment;
import com.restaurantapp.ui.restaurant.detail.RestaurantDetailsActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent extends AppComponent {
    void inject(HomeActivity homeActivity);

    void inject(NearbyFragment nearbyFragment);

    void inject(RestaurantDetailsActivity restaurantDetailsActivityView);

    void inject(MapFragment mapFragment);

    void inject(FavoritesFragment favoritesFragment);
}