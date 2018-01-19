package com.restaurantapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.restaurantapp.data.api.response.Fields;


@Entity(tableName = "reviews",
        indices = @Index(value = {RestaurantModel.RESTAURANT_ID}),
        foreignKeys = @ForeignKey(
                entity = RestaurantModel.class,
                parentColumns = RestaurantModel.ID,
                childColumns = RestaurantModel.RESTAURANT_ID,
                onDelete = ForeignKey.CASCADE
        ))
public class ReviewModel implements Fields.ReviewFields {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private long id;

    @SerializedName(AUTHOR_NAME)
    @Expose
    @ColumnInfo(name = "author_name")
    private String authorName;

    @SerializedName(AUTHOR_URL)
    @Expose
    @ColumnInfo(name = "author_url")
    private String authorUrl;

    @SerializedName(PROFILE_PHOTO_URL)
    @Expose
    @ColumnInfo(name = "profile_photo_url")
    private String profilePhotoUrl;

    @SerializedName(RATING)
    @Expose
    @ColumnInfo(name = "rating")
    private int rating;

    @SerializedName(RELATIVE_TIME_DESCRIPTION)
    @Expose
    @ColumnInfo(name = "relative_time_description")
    private String relativeTimeDescription;

    @SerializedName(TEXT)
    @Expose
    @ColumnInfo(name = "_text")
    private String text;

    @SerializedName(TIME)
    @Expose
    @ColumnInfo(name = "_time")
    private long time;

    // foreign key
    @ColumnInfo(name = RestaurantModel.RESTAURANT_ID)
    private long restaurantId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    public void setRelativeTimeDescription(String relativeTimeDescription) {
        this.relativeTimeDescription = relativeTimeDescription;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }
}