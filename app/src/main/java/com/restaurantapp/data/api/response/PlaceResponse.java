package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceResponse {
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;

    @SerializedName("geometry")
    @Expose
    private GeometryResponse geometry;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("photos")
    @Expose
    private List<PhotoResponse> photos;

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("rating")
    @Expose
    private double rating;

    @SerializedName("reviews")
    @Expose
    private List<ReviewResponse> reviews;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    @SerializedName("website")
    @Expose
    private String website;

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public GeometryResponse getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public List<PhotoResponse> getPhotos() {
        return photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public double getRating() {
        return rating;
    }

    public List<ReviewResponse> getReviews() {
        return reviews;
    }

    public String getUrl() {
        return url;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getWebsite() {
        return website;
    }
}