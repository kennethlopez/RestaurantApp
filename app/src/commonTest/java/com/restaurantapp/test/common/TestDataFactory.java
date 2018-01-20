package com.restaurantapp.test.common;


import com.restaurantapp.data.api.response.Fields;
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
        restaurantModel.setFormattedAddress(Fields.RestaurantFields.FORMATTED_ADDRESS);
        restaurantModel.setIcon(Fields.RestaurantFields.ICON);
        restaurantModel.setLat(DEFAULT_LAT);
        restaurantModel.setLng(DEFAULT_LNG);
        restaurantModel.setName(Fields.RestaurantFields.NAME);
        restaurantModel.setRating(DEFAULT_RATING);
        restaurantModel.setUrl(Fields.RestaurantFields.URL);
        restaurantModel.setVicinity(Fields.RestaurantFields.VICINITY);
        restaurantModel.setWebsite(Fields.RestaurantFields.WEBSITE);
        return restaurantModel;
    }

    public static PhotoModel makePhotoModel(long restaurantId) {
        PhotoModel photoModel = new PhotoModel();
        photoModel.setHeight(1);
        photoModel.setPhotoReference(Fields.PhotoFields.PHOTO_REFERENCE);
        photoModel.setRestaurantId(restaurantId);
        return photoModel;
    }

    public static ReviewModel makeReviewModel(long restaurantId) {
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setRestaurantId(restaurantId);
        reviewModel.setAuthorName(Fields.ReviewFields.AUTHOR_NAME);
        reviewModel.setAuthorUrl(Fields.ReviewFields.AUTHOR_URL);
        reviewModel.setProfilePhotoUrl(Fields.ReviewFields.PROFILE_PHOTO_URL);
        reviewModel.setRating(DEFAULT_RATING);
        reviewModel.setRelativeTimeDescription(Fields.ReviewFields.RELATIVE_TIME_DESCRIPTION);
        reviewModel.setText(Fields.ReviewFields.TEXT);
        reviewModel.setTime(DEFAULT_TIME);
        return reviewModel;
    }
}