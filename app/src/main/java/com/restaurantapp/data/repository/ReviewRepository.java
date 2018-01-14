package com.restaurantapp.data.repository;


import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.Repository;
import com.restaurantapp.data.model.Review;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ReviewRepository implements Repository<Review> {
    private AppDatabase.ReviewDao mReviewDao;

    @Inject
    public ReviewRepository(AppDatabase.ReviewDao reviewDao) {
        mReviewDao = reviewDao;
    }

    @Override
    public long insert(Review item) {
        return mReviewDao.insert(item);
    }

    @Override
    public long[] insert(List<Review> items) {
        return mReviewDao.insert(items);
    }

    @Override
    public void update(Review item) {
        mReviewDao.update(item);
    }

    @Override
    public void update(List<Review> items) {
        mReviewDao.update(items);
    }
}
