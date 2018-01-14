package com.restaurantapp.injection.module;

import android.arch.persistence.room.Room;

import com.restaurantapp.App;
import com.restaurantapp.data.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private AppDatabase mAppDatabase;

    public RoomModule(App app) {
        mAppDatabase = Room.databaseBuilder(app, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    AppDatabase providesRoomDatabase() {
        return mAppDatabase;
    }

    @Provides
    @Singleton
    AppDatabase.RestaurantDao providesRestaurantDao() {
        return mAppDatabase.restaurantDao();
    }

    @Provides
    @Singleton
    AppDatabase.ReviewDao providesReviewDao() {
        return mAppDatabase.reviewDao();
    }

    @Provides
    @Singleton
    AppDatabase.PhotoDao providesPhotoDao() {
        return mAppDatabase.photoDao();
    }
}