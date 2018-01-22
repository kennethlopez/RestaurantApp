package com.restaurantapp.data.api.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Directions implements ResponseFields, ResponseFields.DirectionsFields {
    @SerializedName(ROUTES)
    @Expose
    private List<Route> routes;

    @SerializedName(STATUS)
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route implements ResponseFields.RouteFields {
        @SerializedName(OVERVIEW_POLYLINE)
        @Expose
        private OverViewPolyLine overViewPolyLine;

        public OverViewPolyLine getOverViewPolyLine() {
            return overViewPolyLine;
        }
    }

    public static class OverViewPolyLine implements ResponseFields.OverViewPolyLineFields {
        @SerializedName(POINTS)
        @Expose
        private String points;

        public String getPoints() {
            return points;
        }
    }
}