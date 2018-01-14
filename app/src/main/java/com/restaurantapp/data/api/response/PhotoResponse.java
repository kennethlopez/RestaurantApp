package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoResponse {
    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("width")
    @Expose
    private int width;

    @SerializedName("photo_reference")
    @Expose
    private String photoReference;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getPhotoReference() {
        return photoReference;
    }
}
