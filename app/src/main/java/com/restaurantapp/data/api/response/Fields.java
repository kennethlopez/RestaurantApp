package com.restaurantapp.data.api.response;


public interface Fields {
    String RESULT = "result";
    String RESULTS = "results";
    String NEXT_PAGE_TOKEN = "next_page_token";
    String STATUS = "status";

    interface GeometryFields {
        String LOCATION = "location";
    }

    interface LocationFields {
        String LAT = "lat";
        String LNG = "lng";
    }

    interface PhotoFields {
        String HEIGHT = "height";
        String WIDTH = "width";
        String PHOTO_REFERENCE = "photo_reference";
    }

    interface RestaurantFields {
        String FORMATTED_ADDRESS =  "formatted_address";
        String FORMATTED_PHONE_NUMBER = "formatted_phone_number";
        String GEOMETRY = "geometry";
        String PHOTOS = "photos";
        String ICON = "icon";
        String NAME = "name";
        String PLACE_ID =  "place_id";
        String RATING = "rating";
        String REVIEWS = "reviews";
        String URL = "url";
        String VICINITY = "vicinity";
        String WEBSITE = "website";
    }

    interface ReviewFields {
        String AUTHOR_NAME = "author_name";
        String AUTHOR_URL = "author_url";
        String PROFILE_PHOTO_URL = "profile_photo_url";
        String RATING = "rating";
        String RELATIVE_TIME_DESCRIPTION = "relative_time_description";
        String TEXT = "text";
        String TIME = "time";
    }
}