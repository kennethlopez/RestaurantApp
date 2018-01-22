package com.restaurantapp.injection.module;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.restaurantapp.BuildConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.restaurantapp.App;
import com.restaurantapp.data.api.MapsApiService;
import com.restaurantapp.injection.ApplicationContext;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.NetworkUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

@Module
public class AppModule {
    private static final String TAG = "AppModule";
    private final App mApp;

    public AppModule(App mApp) {
        this.mApp = mApp;
    }

    @Provides
    Application provideApplication() {
        return mApp;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApp;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient.Builder provideOkHttpClient(@ApplicationContext Context context) {
        HttpLoggingInterceptor interceptor =
                new HttpLoggingInterceptor(message -> Log.d(TAG, message));
        interceptor.setLevel(BuildConfig.DEBUG ? HEADERS : NONE);

        Cache cache = null;
        try {
            cache = new Cache(new File(mApp.getCacheDir(), "http-cache"), 1024 * 1024 * 10); // 10mb
        } catch (Exception e) {
            Log.e(TAG, "Could not create cache!");
        }
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {  // cache
                    Response response = chain.proceed(chain.request());
                    // re-write response header to force use of cache
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxAge(2, TimeUnit.MINUTES)
                            .build();
                    return response.newBuilder()
                            .header("Cache-Control", cacheControl.toString())
                            .build();
                })
                .addInterceptor(chain -> {  // offline cache
                    Request request = chain.request();
                    if (!NetworkUtil.isNetworkConnected(context)) {
                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxStale(7, TimeUnit.DAYS)
                                .build();
                        request = request.newBuilder()
                                .cacheControl(cacheControl)
                                .build();
                    }
                    return chain.proceed(request);
                })
                .addInterceptor(chain -> {  // User-Agent changes
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .addHeader("User-Agent", System.getProperty("http.agent"))
                            .build();
                    return chain.proceed(request);
                })
                .addNetworkInterceptor(interceptor)
                .cache(cache)
                .connectTimeout(15L, TimeUnit.SECONDS)
                .writeTimeout(15L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
    }

    @Provides
    @Singleton
    MapsApiService provideApiService(OkHttpClient.Builder okHttpClientBuilder, Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClientBuilder.build())
                .baseUrl(Constants.AppConstants.MAPS_API_SERVICE_HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MapsApiService.class);
    }

    @Provides
    @Singleton
    LocationService provideLocationService(@ApplicationContext Context context) {
        long interval1 = AppUtil.toMilliseconds(1);
        long fastInterval1 = AppUtil.toMilliseconds(0.5);
        long interval2 = AppUtil.toMilliseconds(1);
        long fastInterval2 = AppUtil.toMilliseconds(0.5);

        return new LocationService.Builder(context)
                .addLocationRequest(LocationService.newLocationRequest(interval1, fastInterval1,
                        LocationRequest.PRIORITY_HIGH_ACCURACY))
                .addLocationRequest(LocationService.newLocationRequest(interval2, fastInterval2,
                        LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY))
                .build();
    }
}