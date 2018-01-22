package com.restaurantapp.data.repository;


import android.arch.persistence.room.Transaction;

import com.google.android.gms.maps.model.LatLng;
import com.restaurantapp.R;
import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.Repository;
import com.restaurantapp.data.api.MapsApiService;
import com.restaurantapp.data.api.response.Directions;
import com.restaurantapp.data.api.response.PlaceDetails;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.api.response.PlaceNearby;
import com.restaurantapp.data.event.EventError;
import com.restaurantapp.data.event.exception.OverQueryLimitException;
import com.restaurantapp.data.model.PhotoModel;
import com.restaurantapp.data.model.RestaurantModel;
import com.restaurantapp.data.model.ReviewModel;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.ConverterUtil;
import com.restaurantapp.util.PolyUtil;
import com.restaurantapp.util.RxBus;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class RestaurantRepository implements Repository<RestaurantModel>, Constants.ErrorTypes {
    private AppDatabase.RestaurantDao mRestaurantDao;
    private AppDatabase.PhotoDao mPhotoDao;
    private AppDatabase.ReviewDao mReviewDao;
    private MapsApiService mApiService;
    private RxBus mBus;

    private Restaurant mRestaurant;
    private List<Restaurant> mRestaurants = new ArrayList<>();

    @Inject
    public RestaurantRepository(MapsApiService apiService, AppDatabase.RestaurantDao restaurantDao,
                                AppDatabase.PhotoDao photoDao, AppDatabase.ReviewDao reviewDao, RxBus bus) {
        mRestaurantDao = restaurantDao;
        mPhotoDao = photoDao;
        mReviewDao = reviewDao;
        mApiService = apiService;
        mBus = bus;
    }

    @Override
    public long insert(RestaurantModel item) {
        return mRestaurantDao.insert(item);
    }

    @Override
    public long[] insert(List<RestaurantModel> items) {
        return mRestaurantDao.insert(items);
    }

    @Override
    public void update(RestaurantModel item) {
        mRestaurantDao.update(item);
    }

    @Override
    public void update(List<RestaurantModel> items) {
        mRestaurantDao.update(items);
    }

    public List<Restaurant> getRestaurants() {
        return mRestaurants;
    }

    public Restaurant getRestaurant() {
        return mRestaurant;
    }

    public Single<PlaceNearby> nearbySearch(String location, int radius, String type,
                                            String key) {
        return mApiService.nearbySearch(location, radius, type, key)
                .map(placeNearby -> {
                    mRestaurants = saveResult(placeNearby);
                    return placeNearby;
                });
    }

    public Single<PlaceNearby> nearBySearchWithToken(String location, int radius, String type,
                                                     String key, String pageToken) {
        return mApiService.nearbySearchWithToken(location, radius, type, key, pageToken)
                .map(placeNearby -> {
                    // append page result to restaurants
                    mRestaurants.addAll(saveResult(placeNearby));
                    return placeNearby;
                });
    }

    public Single<PlaceDetails> detailsSearch(String placeId, String key) {
        return mApiService.details(placeId, key)
                .map(placeDetails -> {
                    mRestaurant = saveResult(placeDetails);
                    return placeDetails;
                });
    }

    @Transaction
    public Flowable<PlaceDetails> fetchRestaurant(String placeId) {
        return mRestaurantDao.getRestaurant(placeId)
                .map(restaurant -> {
                    List<PhotoModel> photos = mPhotoDao.getPhotos(restaurant.getId());
                    List<ReviewModel> reviews = mReviewDao.getReviews(restaurant.getId());

                    PlaceDetails placeDetails = new PlaceDetails();
                    placeDetails.setResult(ConverterUtil.fromRestaurantModel(restaurant, photos, reviews));
                    placeDetails.setStatus(Constants.AppConstants.STATUS_OK);

                    return placeDetails;
                });
    }

    public Flowable<List<Restaurant>> fetchRestaurants(String[] placeIds) {
        return mRestaurantDao.getRestaurants(placeIds)
                .map(this::queryRestaurants);
    }

    @Transaction
    public Flowable<Restaurant> fetchRestaurants() {
        return mRestaurantDao.getRestaurants()
                .switchMap(Flowable::fromIterable)
                .map(restaurantModel -> {
                    List<PhotoModel> photoModels = mPhotoDao.getPhotos(restaurantModel.getId());
                    List<ReviewModel> reviewModels = mReviewDao.getReviews(restaurantModel.getId());
                    return ConverterUtil.fromRestaurantModel(restaurantModel, photoModels,
                            reviewModels);
                });
    }

    public Single<List<LatLng>> getPolyLines(String originLatLng, String destinationPlaceId,
                                             String key) {
        String placeId = "place_id:" + destinationPlaceId;
        return mApiService.directions(originLatLng, placeId, key)
                .map(directions -> {
                    throwOnOverQueryLimit(directions.getStatus());

                    return PolyUtil.decode(directions.getRoutes()
                            .get(0)
                            .getOverViewPolyLine()
                            .getPoints());
                });
    }

    @Transaction
    private List<Restaurant> queryRestaurants(List<RestaurantModel> restaurantModels) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (RestaurantModel restaurantModel : restaurantModels) {
            List<PhotoModel> photoModels =
                    mPhotoDao.getPhotos(restaurantModel.getId());
            List<ReviewModel> reviewModels =
                    mReviewDao.getReviews(restaurantModel.getId());
            restaurants.add(ConverterUtil.fromRestaurantModel(restaurantModel,
                    photoModels, reviewModels));
        }
        return restaurants;
    }

    private Restaurant saveResult(PlaceDetails placeDetails) {
        throwOnOverQueryLimit(placeDetails.getStatus());

        return saveRestaurant(placeDetails.getResult());
    }

    private List<Restaurant> saveResult(PlaceNearby placeNearby) {
        throwOnOverQueryLimit(placeNearby.getStatus());

        List<Restaurant> newRestaurants = new ArrayList<>();
        List<Restaurant> restaurants = placeNearby.getResults();
        List<RestaurantModel> restaurantModels = ConverterUtil.toRestaurantModels(restaurants);

        // save restaurants to db
        long[] restaurantIds = insert(restaurantModels);
        for (int c = 0; c < restaurantModels.size(); c++) {
            Restaurant restaurant = savePhotosAndReviews(restaurants.get(c), restaurantIds[c]);
            newRestaurants.add(restaurant);
        }

        return newRestaurants;
    }

    private Restaurant savePhotosAndReviews(Restaurant restaurant, long restaurantId) {
        List<PhotoModel> photoModels = ConverterUtil.toPhotoModels(restaurant, restaurantId);
        List<ReviewModel> reviewModels = ConverterUtil.toReviewModels(restaurant, restaurantId);

        mPhotoDao.insert(photoModels);
        mReviewDao.insert(reviewModels);

        restaurant.setPhotos(ConverterUtil.fromPhotoModels(photoModels));
        restaurant.setReviews(ConverterUtil.fromReviewModels(reviewModels));

        return restaurant;
    }

    private Restaurant saveRestaurant(Restaurant restaurant) {
        long restaurantId = mRestaurantDao.insert(ConverterUtil.toRestaurantModel(restaurant));
        return savePhotosAndReviews(restaurant, restaurantId);
    }

    private void throwOnOverQueryLimit(String status) {
        if (AppUtil.overQueryLimit(status)) {
            mBus.post(new EventError(ERROR_OVER_QUERY_LIMIT, R.string.over_query_limit_message));
            throw new OverQueryLimitException();
        }
    }
}