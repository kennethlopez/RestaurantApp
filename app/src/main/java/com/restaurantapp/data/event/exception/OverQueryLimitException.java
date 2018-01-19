package com.restaurantapp.data.event.exception;


public final class OverQueryLimitException extends RuntimeException {
    public OverQueryLimitException() {
        super("Exceeded query limit for today! Please try again later");
    }
}