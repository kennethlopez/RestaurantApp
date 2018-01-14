package com.restaurantapp.test.common.injection.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.repository.RestaurantRepository;


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
    AppDatabase providesRoomDatabase() {
        return mAppDatabase;
    }

    @Provides
    AppDatabase.RestaurantDao providesRestaurantDao() {
        return mAppDatabase.restaurantDao();
    }

    @Provides
    RestaurantRepository providesRestaurantRepository(AppDatabase.RestaurantDao restaurantDao) {
        return new RestaurantRepository(restaurantDao);
    }
}