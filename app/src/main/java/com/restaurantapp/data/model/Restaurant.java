package com.restaurantapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.restaurantapp.data.api.response.PlaceResponse;
import com.restaurantapp.data.model.fields.RestaurantFields;

@Entity(tableName = RestaurantFields.TABLE_NAME,
        indices = @Index(value = {RestaurantFields.PLACE_ID},
        unique = true))
public class Restaurant implements RestaurantFields {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = LAT)
    private double lat;

    @ColumnInfo(name = LNG)
    private double lng;

    @ColumnInfo(name = PLACE_ID)
    private String placeId;

    @ColumnInfo(name = NAME)
    private String name;

    @ColumnInfo(name = FORMATTED_ADDRESS)
    private String formattedAddress;

    @ColumnInfo(name = ICON)
    private String icon;

    @ColumnInfo(name = RATING)
    private double rating;

    @ColumnInfo(name = URL)
    private String url;

    @ColumnInfo(name = VICINITY)
    private String vicinity;

    @ColumnInfo(name = WEBSITE)
    private String website;

    public static Restaurant newInstance(PlaceResponse result) {
        Restaurant restaurant = new Restaurant();
        restaurant.setFormattedAddress(result.getFormattedAddress());
        restaurant.setIcon(result.getIcon());
        restaurant.setLat(result.getGeometry().getLocation().getLat());
        restaurant.setLng(result.getGeometry().getLocation().getLng());
        restaurant.setName(result.getName());
        restaurant.setPlaceId(result.getPlaceId());
        restaurant.setRating(result.getRating());
        restaurant.setUrl(result.getUrl());
        restaurant.setVicinity(result.getVicinity());
        restaurant.setWebsite(result.getWebsite());

        return restaurant;
    }

    public long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}