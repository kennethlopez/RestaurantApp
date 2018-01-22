package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo implements ResponseFields.PhotoFields {
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

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Photo) {
            Photo photo = (Photo) obj;
            return photo.getPhotoReference().contentEquals(getPhotoReference());
        }
        return false;
    }
}