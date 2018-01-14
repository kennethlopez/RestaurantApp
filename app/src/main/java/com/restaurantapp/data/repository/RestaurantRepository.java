package com.restaurantapp.data.repository;


import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.Repository;
import com.restaurantapp.data.api.PlacesApiService;
import com.restaurantapp.data.api.response.PhotoResponse;
import com.restaurantapp.data.api.response.ReviewResponse;
import com.restaurantapp.data.api.response.PlacesNearbyResponse;
import com.restaurantapp.data.model.Photo;
import com.restaurantapp.data.model.Restaurant;
import com.restaurantapp.data.api.response.PlaceResponse;
import com.restaurantapp.data.model.Review;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class RestaurantRepository implements Repository<Restaurant> {
    private AppDatabase.RestaurantDao mRestaurantDao;
    private AppDatabase.PhotoDao mPhotoDao;
    private AppDatabase.ReviewDao mReviewDao;
    private PlacesApiService mApiService;

    @Inject
    public RestaurantRepository(PlacesApiService apiService, AppDatabase.RestaurantDao restaurantDao,
                                AppDatabase.PhotoDao photoDao, AppDatabase.ReviewDao reviewDao) {
        mRestaurantDao = restaurantDao;
        mPhotoDao = photoDao;
        mReviewDao = reviewDao;
        mApiService = apiService;
    }

    @Override
    public long insert(Restaurant item) {
        return mRestaurantDao.insert(item);
    }

    @Override
    public long[] insert(List<Restaurant> items) {
        return mRestaurantDao.insert(items);
    }

    @Override
    public void update(Restaurant item) {
        mRestaurantDao.update(item);
    }

    @Override
    public void update(List<Restaurant> items) {
        mRestaurantDao.update(items);
    }

    public Flowable<PlacesNearbyResponse> nearbySearch(String location, int radius, String type,
                                                       String key) {
        return mApiService.nearbySearch(location, radius, type, key)
                .map(placesNearbyResponse -> {
                    processResponse(placesNearbyResponse);
                    return placesNearbyResponse;
                });
    }

    public Flowable<PlacesNearbyResponse> nearBySearchWithToken(String location, int radius,
            String type, String key, String pageToken) {
        return mApiService.nearbySearchWithToken(location, radius, type, key, pageToken)
                .map(placesNearbyResponse -> {
                    processResponse(placesNearbyResponse);
                    return placesNearbyResponse;
                });
    }

    private void processResponse(PlacesNearbyResponse placesNearbyResponse) {
        List<PlaceResponse> places = placesNearbyResponse.getResults();

        // save restaurants to db
        List<Restaurant> restaurants = new ArrayList<>();
        for (PlaceResponse place : places) {
            restaurants.add(Restaurant.newInstance(place));
        }
        long[] restaurantIds = insert(restaurants);

        for (int c = 0; c < restaurants.size(); c++) {
            PlaceResponse place = places.get(c);
            List<PhotoResponse> photosResponse = place.getPhotos();
            List<ReviewResponse> reviewsResponse = place.getReviews();

            // save photos of each restaurants to db
            if (places.get(c).getPhotos() != null) {
                List<Photo> photos = new ArrayList<>();
                for (PhotoResponse photoResponse : photosResponse) {
                    photos.add(Photo.newInstance(photoResponse, restaurantIds[c]));
                }
                mPhotoDao.insert(photos);
            }

            // save reviews of each restaurants to db
            if (places.get(c).getReviews() != null) {
                List<Review> reviews = new ArrayList<>();
                for (ReviewResponse reviewResponse : reviewsResponse) {
                    reviews.add(Review.newInstance(reviewResponse, restaurantIds[c]));
                }
                mReviewDao.insert(reviews);
            }
        }
    }
}