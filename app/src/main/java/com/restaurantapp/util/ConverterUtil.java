package com.restaurantapp.util;


import com.google.gson.Gson;
import com.restaurantapp.data.api.response.Geometry;
import com.restaurantapp.data.api.response.Photo;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.api.response.Review;
import com.restaurantapp.data.model.PhotoModel;
import com.restaurantapp.data.model.RestaurantModel;
import com.restaurantapp.data.model.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class ConverterUtil {

    /********************** Restaurant and RestaurantModel Converters *****************************/

    public static Restaurant fromRestaurantModel(RestaurantModel restaurantModel,
            List<PhotoModel> photoModels, List<ReviewModel> reviewModels) {
        Gson gson = new Gson();
        Restaurant restaurant = gson.fromJson(gson.toJson(restaurantModel), Restaurant.class);
        Geometry geometry = Geometry.newInstance(restaurantModel.getLat(), restaurantModel.getLng());

        List<Photo> photos = new ArrayList<>();
        List<Review> reviews = new ArrayList<>();
        for (PhotoModel photoModel : photoModels) photos.add(fromPhotoModel(photoModel));
        for (ReviewModel reviewModel : reviewModels) reviews.add(fromReviewModel(reviewModel));

        restaurant.setGeometry(geometry);
        restaurant.setPhotos(photos);
        restaurant.setReviews(reviews);
        return restaurant;
    }

    public static RestaurantModel toRestaurantModel(Restaurant restaurant) {
        Gson gson = new Gson();
        RestaurantModel model = gson.fromJson(gson.toJson(restaurant), RestaurantModel.class);
        model.setLat(restaurant.getGeometry().getLocation().getLat());
        model.setLng(restaurant.getGeometry().getLocation().getLng());
        return model;
    }

    public static List<RestaurantModel> toRestaurantModels(List<Restaurant> restaurants) {
        List<RestaurantModel> models = new ArrayList<>();
        for (Restaurant restaurant : restaurants) models.add(toRestaurantModel(restaurant));
        return models;
    }

    /********************** Review and ReviewModel Converters *****************************/

    public static Review fromReviewModel(ReviewModel review) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(review), Review.class);
    }

    public static ReviewModel fromReviewModel(Review review, long restaurantId) {
        Gson gson = new Gson();
        ReviewModel model = gson.fromJson(gson.toJson(review), ReviewModel.class);
        model.setRestaurantId(restaurantId);
        return model;
    }

    public static List<Review> fromReviewModels(List<ReviewModel> reviewModels) {
        List<Review> reviews = new ArrayList<>();
        for (ReviewModel reviewModel : reviewModels) reviews.add(fromReviewModel(reviewModel));
        return reviews;
    }

    public static List<ReviewModel> toReviewModels(Restaurant restaurant, long restaurantId) {
        List<ReviewModel> models = new ArrayList<>();
        List<Review> reviews = restaurant.getReviews();
        if (reviews != null) {
            for (Review review : reviews) {
                models.add(fromReviewModel(review, restaurantId));
            }
        }
        return models;
    }

    /********************** Photo and PhotoModel Converters *****************************/

    public static Photo fromPhotoModel(PhotoModel photoModel) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(photoModel), Photo.class);
    }

    public static List<Photo> fromPhotoModels(List<PhotoModel> photoModels) {
        List<Photo> photos = new ArrayList<>();
        for (PhotoModel photoModel : photoModels) photos.add(fromPhotoModel(photoModel));
        return photos;
    }

    public static PhotoModel toPhotoModel(Photo photo, long restaurantId) {
        Gson gson = new Gson();
        PhotoModel model = gson.fromJson(gson.toJson(photo), PhotoModel.class);
        model.setRestaurantId(restaurantId);
        return model;
    }

    public static List<PhotoModel> toPhotoModels(Restaurant restaurant, long restaurantId) {
        List<PhotoModel> models = new ArrayList<>();

        List<Photo> photos = restaurant.getPhotos();
        if (photos != null) {
            for (Photo photo : photos) {
                models.add(toPhotoModel(photo, restaurantId));
            }
        }
        return models;
    }
}