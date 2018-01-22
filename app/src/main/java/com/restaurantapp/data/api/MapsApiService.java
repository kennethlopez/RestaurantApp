package com.restaurantapp.data.api;


import com.restaurantapp.data.api.response.Directions;
import com.restaurantapp.data.api.response.PlaceDetails;
import com.restaurantapp.data.api.response.PlaceNearby;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapsApiService {
    @GET("place/nearbysearch/json")
    Single<PlaceNearby> nearbySearch(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key
    );

    @GET("place/nearbysearch/json")
    Single<PlaceNearby> nearbySearch(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key,
            @Query("keyword") String keyword
    );

    @GET("place/nearbysearch/json")
    Single<PlaceNearby> nearbySearchWithToken(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key,
            @Query("pagetoken") String pageToken
    );

    @GET("place/nearbysearch/json")
    Single<PlaceNearby> nearbySearchWithToken(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key,
            @Query("keyword") String keyword,
            @Query("pagetoken") String pageToken
    );

    @GET("place/details/json")
    Single<PlaceDetails> details(
            @Query("placeid") String placeId,
            @Query("key") String key
    );

    @GET("directions/json")
    Single<Directions> directions(
        @Query("origin") String originLatLng,
        @Query("destination") String destinationPlaceId,
        @Query("key") String key
    );
}