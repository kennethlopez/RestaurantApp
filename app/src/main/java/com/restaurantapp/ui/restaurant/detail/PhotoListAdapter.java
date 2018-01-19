package com.restaurantapp.ui.restaurant.detail;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Photo;
import com.restaurantapp.util.AppUtil;
import com.restaurantapp.util.ImageUtil;
import com.restaurantapp.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<Photo> mPhotos;
    private String mApiKey;

    @Inject
    public PhotoListAdapter(ResourceUtil resourceUtil) {
        mPhotos = new ArrayList<>();
        mApiKey = resourceUtil.getGoogleApiKey();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_preview, parent, false);
        return new ViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int maxWidth = AppUtil.dpToPx(120);
        int height = AppUtil.dpToPx(120);
        Photo photo = mPhotos.get(position);

        String url = AppUtil.getGooglePhotosLink(photo.getPhotoReference(),
                maxWidth, height, mApiKey);
        holder.setImage(url);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void setData(List<Photo> photos) {
        mPhotos = photos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            ButterKnife.bind(this, itemView);

        }

        public void setImage(String url) {
            ImageUtil.setImage(mContext, mImage, url);
        }

        public Context getContext() {
            return mContext;
        }

        @BindView(R.id.item_photo_preview_image) ImageView mImage;
    }
}