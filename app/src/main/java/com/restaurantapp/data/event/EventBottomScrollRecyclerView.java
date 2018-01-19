package com.restaurantapp.data.event;


public final class EventBottomScrollRecyclerView {
    private final int position;

    public EventBottomScrollRecyclerView(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}