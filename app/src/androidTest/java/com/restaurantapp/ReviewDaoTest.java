package com.restaurantapp;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.model.RestaurantModel;
import com.restaurantapp.data.model.ReviewModel;
import com.restaurantapp.test.common.TestComponentRule;
import com.restaurantapp.test.common.TestDataFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ReviewDaoTest {
    private final TestComponentRule mComponent =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());

    private AppDatabase mAppDatabase;
    private AppDatabase.ReviewDao mReviewDao;
    private AppDatabase.RestaurantDao mRestaurantDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setDb() {
        mAppDatabase = mComponent.getAppDatabase();
        mReviewDao = mAppDatabase.reviewDao();
        mRestaurantDao = mAppDatabase.restaurantDao();
    }

    @Test
    public void getReviews() {
        RestaurantModel restaurant = TestDataFactory.makeRestaurantModel(1);
        long restaurantId = mRestaurantDao.insert(restaurant);

        List<ReviewModel> reviewModels = new ArrayList<>();
        ReviewModel reviewModel1 = TestDataFactory.makeReviewModel(restaurantId);
        ReviewModel reviewModel2 = TestDataFactory.makeReviewModel(restaurantId);
        reviewModels.add(reviewModel1);
        reviewModels.add(reviewModel2);
        mReviewDao.insert(reviewModels);

        List<ReviewModel> reviews = mReviewDao.getReviews(restaurantId);
        assertThat(reviews.size(), is(2));
        assertEquals(reviews.get(0).getRestaurantId(), reviewModel1.getRestaurantId());
        assertEquals(reviews.get(1).getRestaurantId(), reviewModel2.getRestaurantId());
    }

    @After
    public void closeDb() {
        mAppDatabase.close();
    }
}