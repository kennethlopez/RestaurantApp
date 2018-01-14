package com.restaurantapp.data.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.restaurantapp.data.api.response.PhotoResponse;
import com.restaurantapp.data.model.fields.PhotoFields;
import com.restaurantapp.data.model.fields.RestaurantFields;

@Entity(tableName = PhotoFields.TABLE_NAME,
        indices = @Index(value = {RestaurantFields.RESTAURANT_ID}),
        foreignKeys = @ForeignKey(
                entity = Restaurant.class,
                parentColumns = RestaurantFields.ID,
                childColumns = RestaurantFields.RESTAURANT_ID,
                onDelete = ForeignKey.CASCADE
        ))
public class Photo implements PhotoFields {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = HEIGHT)
    private int height;

    @ColumnInfo(name = WIDTH)
    private int width;

    @ColumnInfo(name = PHOTO_REFERENCE)
    private String photoReference;

    // foreign key
    @ColumnInfo(name = RestaurantFields.RESTAURANT_ID)
    private long restaurantId;

    public static Photo newInstance(PhotoResponse response, long restaurantId) {
        Photo photo = new Photo();
        photo.setWidth(response.getWidth());
        photo.setHeight(response.getHeight());
        photo.setRestaurantId(restaurantId);
        photo.setPhotoReference(response.getPhotoReference());

        return photo;
    }

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
