package com.restaurantapp.data.event;


public final class EventError {
    private final int messageResId;
    private final String errorType;

    public EventError(String errorType, int messageResId) {
        this.messageResId = messageResId;
        this.errorType = errorType;
    }

    public int getMessageResId() {
        return messageResId;
    }

    public String getErrorType() {
        return errorType;
    }
}
