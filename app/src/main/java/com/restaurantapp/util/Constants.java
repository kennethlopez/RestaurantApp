package com.restaurantapp.util;


public interface Constants {
    interface AppConstants {
        String SHARED_PREFERENCES = "com.restaurantapp.shared_preferences";
        String PLACES_API_SERVICE_HOST = "https://maps.googleapis.com/maps/api/place/";

        // default nearby radius(km) used for google places nearbySearch api
        int DEFAULT_NEARBY_RADIUS = 1;
        int[] NEARBY_RADIUS_OPTIONS = {1, 3, 5, 10, 15, 25};

        // google place types
        String PLACE_TYPE_RESTAURANT = "restaurant";
    }

    interface NetworkConstants {
        int NOT_FOUND_404 = 404;
        int NOT_HTTPEXCEPTION = -1;
    }
}
