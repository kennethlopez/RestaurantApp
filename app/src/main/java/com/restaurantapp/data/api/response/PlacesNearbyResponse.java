package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesNearbyResponse {
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;

    @SerializedName("results")
    @Expose
    private List<PlaceResponse> results;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<PlaceResponse> getResults() {
        return results;
    }
}