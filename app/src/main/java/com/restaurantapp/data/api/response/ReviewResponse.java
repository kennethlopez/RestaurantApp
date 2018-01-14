package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewResponse {
    @SerializedName("author_name")
    @Expose
    private String authorName;

    @SerializedName("author_url")
    @Expose
    private String authorUrl;

    @SerializedName("profile_photo_url")
    @Expose
    private String profilePhotoUrl;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("relative_time_description")
    @Expose
    private String relativeTimeDescription;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("time")
    @Expose
    private long time;

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public int getRating() {
        return rating;
    }

    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }
}
