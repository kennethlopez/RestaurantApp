package com.restaurantapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Update;

import com.restaurantapp.data.model.Photo;
import com.restaurantapp.data.model.Restaurant;
import com.restaurantapp.data.model.Review;

import java.util.List;

import io.reactivex.Flowable;


@Database(entities = {Restaurant.class, Photo.class, Review.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "restaurantapp-db";

    public abstract RestaurantDao restaurantDao();

    public abstract PhotoDao photoDao();

    public abstract ReviewDao reviewDao();

    public interface BaseDao<T> {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        long insert(T item);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        long[] insert(List<T> items);

        @Update
        void update(T item);

        @Update
        void update(List<T> items);
    }

    @Dao
    public interface ReviewDao extends BaseDao<Review> {
        @Query("select * from reviews where _restaurant_id = :restaurantId")
        Flowable<List<Review>> getReviews(int restaurantId);
    }

    @Dao
    public interface PhotoDao extends BaseDao<Photo> {
        @Query("select * from photos where _restaurant_id = :restaurantId")
        Flowable<List<Photo>> getPhotos(int restaurantId);
    }

    @Dao
    public interface RestaurantDao extends BaseDao<Restaurant> {
        @Query("select * from restaurants")
        Flowable<List<Restaurant>> getRestaurants();

        @Query("select * from restaurants where _id = :id")
        Flowable<Restaurant> getRestaurant(int id);
    }
}