package com.restaurantapp.ui.view.holder;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.bumptech.glide.request.target.SimpleTarget;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Photo;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.ImageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private SimpleTarget<Drawable> mTarget;

    public RestaurantViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mContext = context;
    }

    public void setRestaurant(Restaurant restaurant, String apiKey, SimpleTarget<Drawable> target) {
        mTarget = target;
        setRestaurant(restaurant, apiKey);
    }

    public void setRestaurant(Restaurant restaurant, String apiKey) {
        if (restaurant.getPhotos() != null && restaurant.getPhotos().size() != 0) {
            int maxWidth = AppUtil.dpToPx(50);
            int height = AppUtil.dpToPx(50);

            for (Photo photo : restaurant.getPhotos()) {
                if (photo != null) {
                    String url = AppUtil.getGooglePhotosLink(photo.getPhotoReference(),
                            maxWidth, height, apiKey);
                    setImage(url);
                    break;
                }
            }
        }

        setAddress(restaurant.getVicinity());
        setName(restaurant.getName());
        setRating((float) restaurant.getRating());
        setPhoneNumber(restaurant.getFormattedPhoneNumber());
    }

    public void setTarget(SimpleTarget<Drawable> target) {
        mTarget = target;
    }

    public Context getContext() {
        return mContext;
    }

    public void setImage(String url) {
        if (mTarget != null) ImageUtil.setImage(mContext, mTarget, url);
        else ImageUtil.setImage(mContext, mImage, url);
    }

    public void setImage(Drawable drawable) {
        mImage.setImageDrawable(drawable);
    }

    public void setName(String name) {
        mName.setText(name);
    }

    public void setRating(float rating) {
        if (rating > 0) {
            mRatingBar.setVisibility(View.VISIBLE);
            mRatingText.setVisibility(View.VISIBLE);

            AppUtil.setRating(mRatingBar, mRatingText, rating);
        }
        else {
            mRatingBar.setVisibility(View.GONE);
            mRatingText.setVisibility(View.GONE);
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumber.setVisibility(View.VISIBLE);
            mPhoneNumber.setText(phoneNumber);
        }
        else mPhoneNumber.setVisibility(View.GONE);
    }

    public void setTastes(List<String> tastes) {
        boolean first = true;
        boolean marginIsSet = false;
        int left = AppUtil.dpToPx(5);
        int right = 0;
        int top = 0;
        int bottom = 0;

        AppCompatTextView textView;
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        mTastesContainer.removeAllViews();
        for(String taste : tastes) {
            if (!first && !marginIsSet) {
                layoutParams.setMargins(left, top, right, bottom);
                marginIsSet = true;
            }
            else first = false;

            textView = new AppCompatTextView(mContext);
            textView.setLayoutParams(layoutParams);
            textView.setTextAppearance(mContext, R.style.TasteLabel);
            textView.setText(taste);

            mTastesContainer.addView(textView);
        }
    }

    public void setAddress(String address) {
        mAddress.setText(address);
    }

    @BindView(R.id.item_restaurant_image) ImageView mImage;
    @BindView(R.id.item_restaurant_name) AppCompatTextView mName;
    @BindView(R.id.item_restaurant_rating) RatingBar mRatingBar;
    @BindView(R.id.item_restaurant_rating_text) AppCompatTextView mRatingText;
    @BindView(R.id.item_restaurant_tastes_container) LinearLayout mTastesContainer;
    @BindView(R.id.item_restaurant_address) AppCompatTextView mAddress;
    @BindView(R.id.item_restaurant_number) AppCompatTextView mPhoneNumber;
}
