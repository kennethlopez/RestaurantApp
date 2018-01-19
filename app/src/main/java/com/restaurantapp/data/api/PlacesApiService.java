package com.restaurantapp.data.api;


import com.restaurantapp.data.api.response.PlaceDetails;
import com.restaurantapp.data.api.response.PlaceNearby;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {
    @GET("nearbysearch/json")
    Single<PlaceNearby> nearbySearch(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key
    );

    @GET("nearbysearch/json")
    Single<PlaceNearby> nearbySearch(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key,
            @Query("keyword") String keyword
    );

    @GET("nearbysearch/json")
    Single<PlaceNearby> nearbySearchWithToken(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key,
            @Query("pagetoken") String pageToken
    );

    @GET("nearbysearch/json")
    Single<PlaceNearby> nearbySearchWithToken(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key,
            @Query("keyword") String keyword,
            @Query("pagetoken") String pageToken
    );

    @GET("details/json")
    Single<PlaceDetails> details(
            @Query("placeid") String placeId,
            @Query("key") String key
    );
}