package com.restaurantapp.injection.component;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.api.PlacesApiService;
import com.restaurantapp.data.repository.PhotoRepository;
import com.restaurantapp.data.repository.RestaurantRepository;
import com.restaurantapp.data.repository.ReviewRepository;
import com.restaurantapp.injection.ApplicationContext;
import com.restaurantapp.injection.module.AppModule;
import com.restaurantapp.injection.module.RoomModule;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.RxBus;
import com.restaurantapp.util.SharedPrefDataSource;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {
    @ApplicationContext Context context();

    Application application();

    SharedPreferences sharedPreferences();

    SharedPrefDataSource sharePrefDataSource();

    Gson gson();

    HttpLoggingInterceptor httpLoggingInterceptor();

    Cache cache();

    OkHttpClient okHttpClient();

    PlacesApiService apiService();

    RxBus rxBus();

    LocationService locationService();

    AppDatabase appDatabase();

    AppDatabase.RestaurantDao restaurantDao();

    RestaurantRepository restaurantRepository();

    AppDatabase.PhotoDao photoDao();

    PhotoRepository photoRepository();

    AppDatabase.ReviewDao reviewDao();

    ReviewRepository reviewRepository();
}