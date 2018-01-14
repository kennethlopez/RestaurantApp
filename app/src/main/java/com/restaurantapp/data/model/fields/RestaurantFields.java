package com.restaurantapp.data.model.fields;


public interface RestaurantFields {
    String TABLE_NAME =  "restaurants";
    String ID =  "_id";
    String FORMATTED_ADDRESS =  "formatted_address";
    String LAT =  "lat";
    String LNG =  "lng";
    String ICON = "icon";
    String NAME = "name";
    String PLACE_ID =  "place_id";
    String RATING = "rating";
    String URL = "url";
    String VICINITY = "vicinity";
    String WEBSITE = "website";

    // foreign key
    String RESTAURANT_ID = "_restaurant_id";
}