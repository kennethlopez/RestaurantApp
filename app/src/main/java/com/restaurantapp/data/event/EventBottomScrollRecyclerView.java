package com.restaurantapp.data.event;


public class EventBottomScrollRecyclerView {
    private int position;

    public EventBottomScrollRecyclerView(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}