package com.restaurantapp.data.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.restaurantapp.data.api.response.ResponseFields;

@Entity(tableName = "photos",
        indices = @Index(value = {RestaurantModel.RESTAURANT_ID}),
        foreignKeys = @ForeignKey(
                entity = RestaurantModel.class,
                parentColumns = RestaurantModel.ID,
                childColumns = RestaurantModel.RESTAURANT_ID,
                onDelete = ForeignKey.CASCADE
        ))
public class PhotoModel implements ResponseFields.PhotoFields {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private long id;

    @SerializedName(HEIGHT)
    @Expose
    @ColumnInfo(name = "height")
    private int height;

    @SerializedName(WIDTH)
    @Expose
    @ColumnInfo(name = "width")
    private int width;

    @SerializedName(PHOTO_REFERENCE)
    @Expose
    @ColumnInfo(name = "photo_reference")
    private String photoReference;

    // foreign key
    @ColumnInfo(name = RestaurantModel.RESTAURANT_ID)
    private long restaurantId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
