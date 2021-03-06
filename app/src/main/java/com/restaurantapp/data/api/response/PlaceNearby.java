package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceNearby implements ResponseFields, ResponseFields.PlaceFields {
    @SerializedName(NEXT_PAGE_TOKEN)
    @Expose
    private String nextPageToken;

    @SerializedName(RESULTS)
    @Expose
    private List<Restaurant> results;

    @SerializedName(STATUS)
    @Expose
    private String status;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<Restaurant> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }
}