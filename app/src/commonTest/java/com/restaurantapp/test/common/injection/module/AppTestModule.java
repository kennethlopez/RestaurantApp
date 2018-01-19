package com.restaurantapp.test.common.injection.module;


import android.app.Application;
import android.content.Context;

import com.restaurantapp.data.api.PlacesApiService;
import com.restaurantapp.injection.ApplicationContext;
import com.restaurantapp.util.LocationService;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import static org.mockito.Mockito.mock;

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
public class AppTestModule {

    private final Application mApplication;

    public AppTestModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    /************* MOCKS *************/

    @Provides
    @Singleton
    OkHttpClient.Builder provideOkHttpClientBuilder() {
        return mock(OkHttpClient.Builder.class);
    }

    @Provides
    @Singleton
    PlacesApiService providePlacesApiService() {
        return mock(PlacesApiService.class);
    }

    @Provides
    @Singleton
    LocationService provideLocationService() {
        return mock(LocationService.class);
    }
}
