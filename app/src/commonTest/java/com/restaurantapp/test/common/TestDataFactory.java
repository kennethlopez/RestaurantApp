package com.restaurantapp.test.common;


import com.restaurantapp.data.api.response.Fields;
import com.restaurantapp.data.model.RestaurantModel;

@SuppressWarnings("WeakerAccess")
public class TestDataFactory implements Fields.RestaurantFields {
    public static final int DEFAULT_RATING = 0;
    public static final double DEFAULT_LAT = 1.1;
    public static final double DEFAULT_LNG = 1.2;

    public static RestaurantModel makeRestaurantModel(int placeId) {
        return makeRestaurantModel(String.valueOf(placeId));
    }

    public static RestaurantModel makeRestaurantModel(String  placeId) {
        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setPlaceId(String.valueOf(placeId));
        restaurantModel.setFormattedAddress(FORMATTED_ADDRESS);
        restaurantModel.setIcon(ICON);
        restaurantModel.setLat(DEFAULT_LAT);
        restaurantModel.setLng(DEFAULT_LNG);
        restaurantModel.setName(NAME);
        restaurantModel.setRating(DEFAULT_RATING);
        restaurantModel.setUrl(URL);
        restaurantModel.setVicinity(VICINITY);
        restaurantModel.setWebsite(WEBSITE);
        return restaurantModel;
    }
}