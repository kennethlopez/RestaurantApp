package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurant implements ResponseFields.RestaurantFields {
    @SerializedName(FORMATTED_ADDRESS)
    @Expose
    private String formattedAddress;

    @SerializedName(FORMATTED_PHONE_NUMBER)
    @Expose
    private String formattedPhoneNumber;

    @SerializedName(GEOMETRY)
    @Expose
    private Geometry geometry;

    @SerializedName(ICON)
    @Expose
    private String icon;

    @SerializedName(NAME)
    @Expose
    private String name;

    @SerializedName(PHOTOS)
    @Expose
    private List<Photo> photos;

    @SerializedName(PLACE_ID)
    @Expose
    private String placeId;

    @SerializedName(RATING)
    @Expose
    private double rating;

    @SerializedName(REVIEWS)
    @Expose
    private List<Review> reviews;

    @SerializedName(URL)
    @Expose
    private String url;

    @SerializedName(VICINITY)
    @Expose
    private String vicinity;

    @SerializedName(WEBSITE)
    @Expose
    private String website;

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public double getRating() {
        return rating;
    }

    public List<Review> getReviews() {
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

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Restaurant) {
            Restaurant restaurant = (Restaurant) obj;
            return restaurant.getPlaceId().contentEquals(getPlaceId());
        }
        return false;
    }
}