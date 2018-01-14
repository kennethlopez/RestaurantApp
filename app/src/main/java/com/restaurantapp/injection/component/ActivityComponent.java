package com.restaurantapp.injection.component;

import com.restaurantapp.injection.ActivityScope;
import com.restaurantapp.injection.module.ActivityModule;
import com.restaurantapp.ui.home.HomeActivity;
import com.restaurantapp.ui.home.fragment.nearby.NearbyFragment;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent extends AppComponent {
    void inject(HomeActivity homeActivity);

    void inject(NearbyFragment nearbyFragment);
}