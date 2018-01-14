package com.restaurantapp.data.event;


public class EventConnectionError {
    private int messageResId;

    public EventConnectionError(int messageResId) {
        this.messageResId = messageResId;
    }

    public int getMessageResId() {
        return messageResId;
    }
}
