package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Geometry implements Fields.GeometryFields {
    @SerializedName(LOCATION)
    @Expose
    private Location location;

    public Geometry(Location location) {
        this.location = location;
    }

    public static Geometry newInstance(double lat, double lng) {
        return new Geometry(new Location(lat, lng));
    }

    public Location getLocation() {
        return location;
    }

    public static class Location implements Fields.LocationFields {
        @SerializedName(LAT)
        @Expose
        private double lat;

        @SerializedName(LNG)
        @Expose
        private double lng;

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}