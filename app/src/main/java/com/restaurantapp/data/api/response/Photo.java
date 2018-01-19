package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo implements Fields.PhotoFields {
    @SerializedName(HEIGHT)
    @Expose
    private int height;

    @SerializedName(WIDTH)
    @Expose
    private int width;

    @SerializedName(PHOTO_REFERENCE)
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