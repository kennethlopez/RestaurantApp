package com.restaurantapp;


import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.model.PhotoModel;
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

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class PhotoDaoTest {
    private final TestComponentRule mComponent =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());

    private AppDatabase mAppDatabase;
    private AppDatabase.PhotoDao mPhotoDao;
    private AppDatabase.RestaurantDao mRestaurantDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setDb() {
        mAppDatabase = mComponent.getAppDatabase();
        mPhotoDao = mAppDatabase.photoDao();
        mRestaurantDao = mAppDatabase.restaurantDao();
    }

    @Test
    public void getPhotos() {
        RestaurantModel restaurant = TestDataFactory.makeRestaurantModel(1);
        long restaurantId = mRestaurantDao.insert(restaurant);

        List<PhotoModel> photoModels = new ArrayList<>();
        PhotoModel photoModel1 = TestDataFactory.makePhotoModel(restaurantId);
        PhotoModel photoModel2 = TestDataFactory.makePhotoModel(restaurantId);
        photoModels.add(photoModel1);
        photoModels.add(photoModel2);
        mPhotoDao.insert(photoModels);

        List<PhotoModel> photos = mPhotoDao.getPhotos(restaurantId);
        assertThat(photos.size(), is(2));
        assertEquals(photos.get(0).getRestaurantId(), photoModel1.getRestaurantId());
        assertEquals(photos.get(1).getRestaurantId(), photoModel2.getRestaurantId());
    }

    @After
    public void closeDb() {
        mAppDatabase.close();
    }
}