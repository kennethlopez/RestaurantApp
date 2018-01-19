package com.restaurantapp.util;


public interface Constants {
    interface AppConstants {
        String SHARED_PREFERENCES = "com.restaurantapp.shared_preferences";
        String PLACES_API_SERVICE_HOST = "https://maps.googleapis.com/maps/api/place/";

        // default nearby radius(km) used for google places nearbySearch api
        int DEFAULT_NEARBY_RADIUS = 1;
        int[] NEARBY_RADIUS_OPTIONS = {1, 3, 5, 10, 15, 25, 50};

        // google place types
        String PLACE_TYPE_RESTAURANT = "restaurant";

        // default google map zoom used by the app
        int DEFAULT_ZOOM = 17;

        // google places api query status
        String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
        String STATUS_OK = "OK";

        String FAVORITES_SEPARATOR = ";";
    }

    interface SharedPrefConstants {
        String PREF_LAT = "pref_lat";
        String PREF_LNG = "pref_lng";
        String PREF_LOCATION_ADDRESS = "pref_location_address";
        String PREF_NEARBY_RADIUS = "pref_nearby_radius";
        String PREF_DISPLAYED_OVER_QUERY_LIMIT_DIALOG = "pref_displayed_over_query_limit_dialog";
        String PREF_DISPLAYED_CONNECTION_ERROR_DIALOG = "pref_displayed_connection_error_dialog";
        String PREF_FAVORITES = "pref_favorites";
        String PREF_MAP_ZOOM = "pref_map_zoom";
    }

    interface Methods {
        String METHOD_SEARCH = "METHOD_SEARCH";
        String METHOD_SEARCH_WITH_TOKEN = "METHOD_SEARCH_WITH_TOKEN";
    }

    interface ErrorTypes {
        String ERROR_OVER_QUERY_LIMIT = "ERROR_OVER_QUERY_LIMIT";
        String ERROR_CONNECTION = "ERROR_CONNECTION";
    }
}