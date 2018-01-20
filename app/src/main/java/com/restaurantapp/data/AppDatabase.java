package com.restaurantapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Update;

import com.restaurantapp.data.model.PhotoModel;
import com.restaurantapp.data.model.RestaurantModel;
import com.restaurantapp.data.model.ReviewModel;

import java.util.List;

import io.reactivex.Flowable;


@Database(entities = {RestaurantModel.class, PhotoModel.class, ReviewModel.class}, version = 6, exportSchema = false)
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
    public interface RestaurantDao extends BaseDao<RestaurantModel> {
        @Query("select * from restaurants where place_id = :placeId")
        Flowable<RestaurantModel> getRestaurant(String placeId);

        @Query("select * from restaurants")
        Flowable<List<RestaurantModel>> getRestaurants();

        @Query("select * from restaurants where place_id in(:placeIds)")
        Flowable<List<RestaurantModel>> getRestaurants(String[] placeIds);
    }

    @Dao
    public interface PhotoDao extends BaseDao<PhotoModel> {
        @Query("select * from photos where _restaurant_id = :restaurantId")
        List<PhotoModel> getPhotos(long restaurantId);
    }

    @Dao
    public interface ReviewDao extends BaseDao<ReviewModel> {
        @Query("select * from reviews where _restaurant_id = :restaurantId")
        List<ReviewModel> getReviews(long restaurantId);
    }
}