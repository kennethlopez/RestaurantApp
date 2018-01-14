package com.restaurantapp.data.event;


public class EventTurnOnLocation {
    private boolean locationEventTurnedOn;

    public EventTurnOnLocation(boolean locationEventTurnedOn) {
        this.locationEventTurnedOn = locationEventTurnedOn;
    }

    public boolean isLocationEventTurnedOn() {
        return locationEventTurnedOn;
    }
}
