package com.restaurantapp.data.api;


import com.restaurantapp.data.api.response.PlacesDetailsResponse;
import com.restaurantapp.data.api.response.PlacesNearbyResponse;


import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {
    @GET("nearbysearch/json")
    Flowable<PlacesNearbyResponse> nearbySearch(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key
    );

    @GET("nearbysearch/json")
    Flowable<PlacesNearbyResponse> nearbySearch(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key,
            @Query("keyword") String keyword
    );

    @GET("nearbysearch/json")
    Flowable<PlacesNearbyResponse> nearbySearchWithToken(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key,
            @Query("pagetoken") String token
    );

    @GET("nearbysearch/json")
    Flowable<PlacesNearbyResponse> nearbySearchWithToken(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key,
            @Query("keyword") String keyword,
            @Query("pagetoken") String token
    );

    @GET("details/json")
    Flowable<PlacesDetailsResponse> details(
            @Query("placeid") String placeId,
            @Query("key") String key
    );
}