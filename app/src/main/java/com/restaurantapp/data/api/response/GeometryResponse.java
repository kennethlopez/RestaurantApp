package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GeometryResponse {
    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public Location getLocation() {
        return location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public class Location {
        @SerializedName("lat")
        @Expose
        private double lat;

        @SerializedName("lng")
        @Expose
        private double lng;

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
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
    }

    public class Viewport {
        @SerializedName("northeast")
        @Expose
        private Location northeast;

        @SerializedName("southwest")
        @Expose
        private Location southwest;

        public Viewport(Location northeast, Location southwest) {
            this.northeast = northeast;
            this.southwest = southwest;
        }

        public Location getNortheast() {
            return northeast;
        }

        public void setNortheast(Location northeast) {
            this.northeast = northeast;
        }

        public Location getSouthwest() {
            return southwest;
        }

        public void setSouthwest(Location southwest) {
            this.southwest = southwest;
        }
    }
}