package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceDetails implements ResponseFields, ResponseFields.PlaceFields {
    @SerializedName(RESULT)
    @Expose
    private Restaurant result;

    @SerializedName(STATUS)
    @Expose
    private String status;

    public void setResult(Restaurant result) {
        this.result = result;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Restaurant getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }
}