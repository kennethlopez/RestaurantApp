package com.restaurantapp.test.common;


import com.restaurantapp.data.api.response.ResponseFields;
import com.restaurantapp.data.model.PhotoModel;
import com.restaurantapp.data.model.RestaurantModel;
import com.restaurantapp.data.model.ReviewModel;

public class TestDataFactory {
    private static final int DEFAULT_RATING = 0;
    private static final double DEFAULT_LAT = 1.1;
    private static final double DEFAULT_LNG = 1.2;
    private static final long DEFAULT_TIME = 1;

    public static RestaurantModel makeRestaurantModel(int placeId) {
        return makeRestaurantModel(String.valueOf(placeId));
    }

    public static RestaurantModel makeRestaurantModel(String  placeId) {
        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setPlaceId(String.valueOf(placeId));
        restaurantModel.setFormattedAddress(ResponseFields.RestaurantFields.FORMATTED_ADDRESS);
        restaurantModel.setIcon(ResponseFields.RestaurantFields.ICON);
        restaurantModel.setLat(DEFAULT_LAT);
        restaurantModel.setLng(DEFAULT_LNG);
        restaurantModel.setName(ResponseFields.RestaurantFields.NAME);
        restaurantModel.setRating(DEFAULT_RATING);
        restaurantModel.setUrl(ResponseFields.RestaurantFields.URL);
        restaurantModel.setVicinity(ResponseFields.RestaurantFields.VICINITY);
        restaurantModel.setWebsite(ResponseFields.RestaurantFields.WEBSITE);
        return restaurantModel;
    }

    public static PhotoModel makePhotoModel(long restaurantId) {
        PhotoModel photoModel = new PhotoModel();
        photoModel.setHeight(1);
        photoModel.setPhotoReference(ResponseFields.PhotoFields.PHOTO_REFERENCE);
        photoModel.setRestaurantId(restaurantId);
        return photoModel;
    }

    public static ReviewModel makeReviewModel(long restaurantId) {
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setRestaurantId(restaurantId);
        reviewModel.setAuthorName(ResponseFields.ReviewFields.AUTHOR_NAME);
        reviewModel.setAuthorUrl(ResponseFields.ReviewFields.AUTHOR_URL);
        reviewModel.setProfilePhotoUrl(ResponseFields.ReviewFields.PROFILE_PHOTO_URL);
        reviewModel.setRating(DEFAULT_RATING);
        reviewModel.setRelativeTimeDescription(ResponseFields.ReviewFields.RELATIVE_TIME_DESCRIPTION);
        reviewModel.setText(ResponseFields.ReviewFields.TEXT);
        reviewModel.setTime(DEFAULT_TIME);
        return reviewModel;
    }
}