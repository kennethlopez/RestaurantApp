package com.restaurantapp.ui.restaurant.detail;


import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Review;
import com.restaurantapp.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    private List<Review> mReviews;

    @Inject
    public ReviewListAdapter() {
        mReviews = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.setName(review.getAuthorName());
        holder.setImage(review.getProfilePhotoUrl());
        holder.setRating(review.getRating());
        holder.setText(review.getText());
        holder.setTime(review.getRelativeTimeDescription());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    void setData(List<Review> reviews) {
        mReviews = reviews;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            ButterKnife.bind(this, itemView);
        }

        public void setName(String name) {
            mName.setText(name);
        }

        public void setImage(String url) {
            ImageUtil.setImage(mContext, mImage, R.drawable.img_place_holder, url);
        }

        public void setRating(int rating) {
            mRatingBar.setRating(rating);
        }

        public void setText(String text) {
            mText.setText(text);
        }

        public void setTime(String time) {
            mTime.setText(time);
        }

        @BindView(R.id.item_review_image) ImageView mImage;
        @BindView(R.id.item_review_name) AppCompatTextView mName;
        @BindView(R.id.item_review_text) AppCompatTextView mText;
        @BindView(R.id.item_review_time) AppCompatTextView mTime;
        @BindView(R.id.item_review_ratingbar) RatingBar mRatingBar;
    }
}