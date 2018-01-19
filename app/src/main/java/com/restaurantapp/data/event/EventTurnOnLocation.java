package com.restaurantapp.data.event;


public final class EventTurnOnLocation {
    private final boolean locationEventTurnedOn;

    public EventTurnOnLocation(boolean locationEventTurnedOn) {
        this.locationEventTurnedOn = locationEventTurnedOn;
    }

    public boolean isLocationEventTurnedOn() {
        return locationEventTurnedOn;
    }
}
