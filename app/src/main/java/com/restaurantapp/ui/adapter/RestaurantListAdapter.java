package com.restaurantapp.ui.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.PhotoResponse;
import com.restaurantapp.data.api.response.PlaceResponse;
import com.restaurantapp.data.event.EventBottomScrollRecyclerView;;
import com.restaurantapp.injection.module.GlideApp;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.RxBus;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private List<PlaceResponse> mList;
    private RxBus mBus;

    public RestaurantListAdapter(List<PlaceResponse> list, RxBus bus) {
        mBus = bus;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);

        return new ViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaceResponse place = mList.get(position);

        if (place.getPhotos() != null && place.getPhotos().size() != 0) {
            int maxWidth = AppUtil.dpToPx(50);
            int height = AppUtil.dpToPx(50);

            PhotoResponse photo = place.getPhotos().get(0);
            String url = AppUtil.getGooglePhotosLink(photo.getPhotoReference(), maxWidth, height);
            holder.setImage(url);
        } else holder.setImage(R.drawable.ic_restaurant_menu_black_24dp);

        holder.setAddress(place.getVicinity());
        holder.setName(place.getName());
        holder.setRating((float) place.getRating());

        if (position == mList.size() - 1) mBus.post(new EventBottomScrollRecyclerView(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<PlaceResponse> list) {
        mList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            ButterKnife.bind(this, itemView);

            init();
        }

        public void init() {
            mName.setText("");
            mRatingBar.setRating(0.0f);
            mRatingText.setText("");
            mTastesContainer.removeAllViews();
            mAddress.setText("");
        }

        public void setImage(String string) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .transform(new RoundedCorners(AppUtil.dpToPx(2)));

            GlideApp.with(mContext)
                    .load(string)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(options)
                    .into(mImage);
        }

        public void setImage(int resId) {
            GlideApp.with(mContext)
                    .load(resId)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            resource.setAlpha(70);
                            mImage.setImageDrawable(resource);
                        }
                    });
        }

        public void setName(String name) {
            mName.setText(name);
        }

        public void setRating(float rating) {
            if (rating > 0) {
                mRatingBar.setVisibility(View.VISIBLE);
                mRatingText.setVisibility(View.VISIBLE);
                mRatingBar.setRating(rating);

                String ratingString;
                // check if rating is whole number
                if (rating - (int) rating == 0) ratingString = String.valueOf((int) rating);
                else ratingString = String.valueOf(rating);
                mRatingText.setText(ratingString);
            }
            else {
                mRatingBar.setVisibility(View.GONE);
                mRatingText.setVisibility(View.GONE);
            }
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
    }
}