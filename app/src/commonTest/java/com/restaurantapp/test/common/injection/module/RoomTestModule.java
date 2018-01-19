package com.restaurantapp.test.common.injection.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.restaurantapp.data.AppDatabase;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomTestModule {
    private AppDatabase mAppDatabase;

    public RoomTestModule(Context context) {
        mAppDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
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