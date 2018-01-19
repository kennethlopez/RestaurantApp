package com.restaurantapp.ui.home.fragment.nearby;


import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Photo;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.event.EventBottomScrollRecyclerView;;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.ImageUtil;
import com.restaurantapp.util.ResourceUtil;
import com.restaurantapp.util.RxBus;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private List<Restaurant> mList;
    private RxBus mBus;
    private String mApiKey;

    @Inject
    public RestaurantListAdapter(ResourceUtil resourceUtil, RxBus bus) {
        mBus = bus;
        mList = new ArrayList<>();
        mApiKey = resourceUtil.getGoogleApiKey();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);

        return new ViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant restaurant = mList.get(position);

        if (restaurant.getPhotos() != null && restaurant.getPhotos().size() != 0) {
            int maxWidth = AppUtil.dpToPx(50);
            int height = AppUtil.dpToPx(50);

            for (Photo photo : restaurant.getPhotos()) {
                if (photo != null) {
                    String url = AppUtil.getGooglePhotosLink(photo.getPhotoReference(),
                            maxWidth, height, mApiKey);
                    holder.setImage(url);
                    break;
                }
            }
        } else holder.setImage(R.drawable.img_place_holder);

        holder.setAddress(restaurant.getVicinity());
        holder.setName(restaurant.getName());
        holder.setRating((float) restaurant.getRating());
        holder.setPhoneNumber(restaurant.getFormattedPhoneNumber());

        if (position == mList.size() - 1) mBus.post(new EventBottomScrollRecyclerView(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<Restaurant> list) {
        mList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public Context getContext() {
            return mContext;
        }

        public void setImage(String url) {
            ImageUtil.setImage(mContext, mImage, url);
        }

        public void setImage(int resId) {
            ImageUtil.setImage(mContext, resId, mImage);
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
}