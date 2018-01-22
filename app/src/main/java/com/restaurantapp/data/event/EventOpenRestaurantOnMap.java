package com.restaurantapp.data.event;


import com.restaurantapp.data.api.response.Restaurant;

public class EventOpenRestaurantOnMap {
    private Restaurant restaurant;

    public EventOpenRestaurantOnMap(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
