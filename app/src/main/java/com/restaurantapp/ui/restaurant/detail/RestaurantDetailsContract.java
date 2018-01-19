package com.restaurantapp.ui.restaurant.detail;


import com.restaurantapp.data.api.response.Photo;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.api.response.Review;
import com.restaurantapp.ui.base.BaseView;

import java.util.List;

public interface RestaurantDetailsContract {
    interface View extends BaseView {
        void setToolbar();

        void setTitle(String title);

        void setName(String name);

        void setAddress(String address);

        void setPhoto(String url);

        void setWebsite(String website);

        void setPhotos(List<Photo> photos);

        void setReviews(List<Review> reviews);

        void setFavoritesMenuItemTint(int colorResId);

        void showSnackBar(int messageResId);

        void showProgressBars();

        void hideProgressBars();

        void hidePhotosContainer();

        void hideReviewsContainer();
    }

    interface Presenter {
        void setPlaceResponse(Restaurant restaurant, String apiKey);

        void optionsItemSelected(int itemId);
    }
}