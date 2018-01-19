package com.restaurantapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.restaurantapp.data.api.response.Fields;


@Entity(tableName = "restaurants",
        indices = @Index(value = {RestaurantModel.PLACE_ID},
        unique = true))
public class RestaurantModel implements Fields.RestaurantFields, Fields.LocationFields {
    public static final String ID = "_id";
    public static final String RESTAURANT_ID = "_restaurant_id";
    public static final String PLACE_ID = "place_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @SerializedName(LAT)
    @Expose
    @ColumnInfo(name = "lat")
    private double lat;

    @SerializedName(LNG)
    @Expose
    @ColumnInfo(name = "lng")
    private double lng;

    @SerializedName(PLACE_ID)
    @Expose
    @ColumnInfo(name = "place_id")
    private String placeId;

    @SerializedName(NAME)
    @Expose
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName(FORMATTED_ADDRESS)
    @Expose
    @ColumnInfo(name = "formatted_address")
    private String formattedAddress;

    @SerializedName(ICON)
    @Expose
    @ColumnInfo(name = "icon")
    private String icon;

    @SerializedName(RATING)
    @Expose
    @ColumnInfo(name = "rating")
    private double rating;

    @SerializedName(URL)
    @Expose
    @ColumnInfo(name = "url")
    private String url;

    @SerializedName(VICINITY)
    @Expose
    @ColumnInfo(name = "vicinity")
    private String vicinity;

    @SerializedName(WEBSITE)
    @Expose
    @ColumnInfo(name = "website")
    private String website;

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