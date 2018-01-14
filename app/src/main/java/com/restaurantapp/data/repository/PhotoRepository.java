package com.restaurantapp.data.repository;


import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.data.Repository;
import com.restaurantapp.data.model.Photo;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PhotoRepository implements Repository<Photo> {
    private AppDatabase.PhotoDao mPhotoDao;

    @Inject
    public PhotoRepository(AppDatabase.PhotoDao photoDao) {
        mPhotoDao = photoDao;
    }

    @Override
    public long insert(Photo item) {
        return mPhotoDao.insert(item);
    }

    @Override
    public long[] insert(List<Photo> items) {
        return mPhotoDao.insert(items);
    }

    @Override
    public void update(Photo item) {
        mPhotoDao.update(item);
    }

    @Override
    public void update(List<Photo> items) {
        mPhotoDao.update(items);
    }
}