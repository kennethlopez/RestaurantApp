package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review implements Fields.ReviewFields {
    @SerializedName(AUTHOR_NAME)
    @Expose
    private String authorName;

    @SerializedName(AUTHOR_URL)
    @Expose
    private String authorUrl;

    @SerializedName(PROFILE_PHOTO_URL)
    @Expose
    private String profilePhotoUrl;

    @SerializedName(RATING)
    @Expose
    private int rating;

    @SerializedName(RELATIVE_TIME_DESCRIPTION)
    @Expose
    private String relativeTimeDescription;

    @SerializedName(TEXT)
    @Expose
    private String text;

    @SerializedName(TIME)
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