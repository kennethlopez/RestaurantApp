package com.restaurantapp;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.model.RestaurantModel;
import com.restaurantapp.test.common.TestComponentRule;
import com.restaurantapp.test.common.TestDataFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class RestaurantDaoTest {
    private final TestComponentRule mComponent =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());

    private AppDatabase mAppDatabase;
    private AppDatabase.RestaurantDao mRestaurantDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setDb() {
        mAppDatabase = mComponent.getAppDatabase();
        mRestaurantDao = mAppDatabase.restaurantDao();
    }

    @Test
    public void getRestaurants() {
        RestaurantModel restaurant1 = TestDataFactory.makeRestaurantModel(1);
        RestaurantModel restaurant2 = TestDataFactory.makeRestaurantModel(2);

        List<RestaurantModel> restaurants = new ArrayList<>();
        mRestaurantDao.insert(restaurant1);
        mRestaurantDao.insert(restaurant2);
        restaurants.add(restaurant1);
        restaurants.add(restaurant2);

        mRestaurantDao.getRestaurants()
                .test()
                .assertValue(restaurantModels -> samePlaceIds(restaurants, restaurantModels));
    }

    @Test
    public void getRestaurant() {
        String placeId = "1";
        mRestaurantDao.insert(TestDataFactory.makeRestaurantModel(placeId));
        mRestaurantDao.getRestaurant(placeId)
                .test()
                .assertValue(restaurant -> restaurant.getPlaceId().contentEquals(placeId));
    }

    @Test
    public void getRestaurantsWithPlaceIds() {
        String[] placeIds = {"1", "2"};
        List<RestaurantModel> restaurants = new ArrayList<>();
        for (String placeId : placeIds) {
            restaurants.add(TestDataFactory.makeRestaurantModel(placeId));
        }

        mRestaurantDao.insert(restaurants);
        mRestaurantDao.getRestaurants(placeIds)
                .test()
                .assertValue(restaurantModels -> samePlaceIds(restaurants, restaurantModels));
    }

    private boolean samePlaceIds(List<RestaurantModel> restaurants1,
                                 List<RestaurantModel> restaurants2) {
        if (restaurants1.size() != restaurants2.size()) return false;

        for (int c = 0; c < restaurants1.size(); c++) {
            String placeId1 = restaurants1.get(c).getPlaceId();
            String placeId2 = restaurants2.get(c).getPlaceId();
            if (!placeId1.contentEquals(placeId2)) return false;
        }
        return true;
    }

    @After
    public void closeDb() {
        mAppDatabase.close();
    }
}