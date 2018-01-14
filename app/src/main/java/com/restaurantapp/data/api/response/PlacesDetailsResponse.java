package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlacesDetailsResponse {
    @SerializedName("result")
    @Expose
    private PlaceResponse result;

    public PlaceResponse getResult() {
        return result;
    }
}