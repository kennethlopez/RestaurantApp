package com.restaurantapp.data;


import java.util.List;

public interface Repository<T> {
    long insert(T item);
    long[] insert(List<T> items);
    void update(T item);
    void update(List<T> items);
}