package com.restaurantapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.restaurantapp.data.api.response.ReviewResponse;
import com.restaurantapp.data.model.fields.RestaurantFields;
import com.restaurantapp.data.model.fields.ReviewFields;

@Entity(tableName = ReviewFields.TABLE_NAME,
        indices = @Index(value = {RestaurantFields.RESTAURANT_ID}),
        foreignKeys = @ForeignKey(
                entity = Restaurant.class,
                parentColumns = RestaurantFields.ID,
                childColumns = RestaurantFields.RESTAURANT_ID,
                onDelete = ForeignKey.CASCADE
        ))
public class Review implements ReviewFields {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = AUTHOR_NAME)
    private String authorName;

    @ColumnInfo(name = AUTHOR_URL)
    private String authorUrl;

    @ColumnInfo(name = PROFILE_PHOTO_URL)
    private String profilePhotoUrl;

    @ColumnInfo(name = RATING)
    private int rating;

    @ColumnInfo(name = RELATIVE_TIME_DESCRIPTION)
    private String relativeTimeDescription;

    @ColumnInfo(name = TEXT)
    private String text;

    @ColumnInfo(name = TIME)
    private long time;

    // foreign key
    @ColumnInfo(name = RestaurantFields.RESTAURANT_ID)
    private long restaurantId;

    public static Review newInstance(ReviewResponse response, long restaurantId) {
        Review review = new Review();
        review.setAuthorName(response.getAuthorName());
        review.setAuthorUrl(response.getAuthorUrl());
        review.setProfilePhotoUrl(response.getProfilePhotoUrl());
        review.setRating(response.getRating());
        review.setRelativeTimeDescription(response.getRelativeTimeDescription());
        review.setRestaurantId(restaurantId);
        review.setText(response.getText());
        review.setTime(response.getTime());

        return review;
    }

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