package com.restaurantapp.injection.component;


import android.app.Application;
import android.content.Context;

import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.api.MapsApiService;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.injection.ApplicationContext;
import com.restaurantapp.injection.module.AppModule;
import com.restaurantapp.injection.module.RoomModule;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.ResourceUtil;
import com.restaurantapp.util.RxBus;
import com.restaurantapp.util.SharedPrefUtil;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {
    Application application();

    @ApplicationContext Context context();

    SharedPrefUtil sharedPrefUtil();

    ResourceUtil resourceUtil();

    OkHttpClient.Builder okHttpClientBuilder();

    MapsApiService apiService();

    LocationService locationService();

    RxBus rxBus();

    AppDatabase appDatabase();

    AppDatabase.RestaurantDao restaurantDao();

    AppDatabase.PhotoDao photoDao();

    AppDatabase.ReviewDao reviewDao();

    RestaurantRepository restaurantRepository();
}