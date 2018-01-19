package com.restaurantapp.util;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.restaurantapp.R;
import com.restaurantapp.injection.module.GlideApp;
import com.restaurantapp.injection.module.GlideRequest;


public class ImageUtil {
    private static final int NO_PLACE_HOLDER = -1;
    private static final int DEFAULT_CORNER_RADIUS = 2;

    public static void setImage(Context context, SimpleTarget<Drawable> target, String url) {
        setImage(context, target, url, DEFAULT_CORNER_RADIUS);
    }

    public static void setImage(Context context, ImageView view, String url) {
        setImage(context, view, NO_PLACE_HOLDER, url, DEFAULT_CORNER_RADIUS);
    }

    public static void setImage(Context context, ImageView view, int placeHolderId, String url) {
        setImage(context, view, placeHolderId, url, DEFAULT_CORNER_RADIUS);
    }

    public static void setImage(Context context, int resId, ImageView imageView) {
        setImage(context, resId, 70, imageView);
    }

    public static void setImage(Context context, SimpleTarget<Drawable> target, String url,
                                int cornerDp) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new RoundedCorners(AppUtil.dpToPx(cornerDp)));

        GlideApp.with(context)
                .load(url)
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.img_place_holder)
                .apply(options)
                .into(target);
    }

    public static void setImage(Context context, ImageView view, int placeHolderId,
                                String url, int cornerDp) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new RoundedCorners(AppUtil.dpToPx(cornerDp)));

        GlideRequest request = GlideApp.with(context)
                .load(url)
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options);
        if (placeHolderId != NO_PLACE_HOLDER) request.placeholder(placeHolderId).into(view);
        else request.into(view);
    }

    public static void setImage(Context context, int resId, int alpha, ImageView imageView) {
        GlideApp.with(context)
                .load(resId)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        resource.setAlpha(alpha);
                        imageView.setImageDrawable(resource);
                    }
                });
    }

    public static void setTint(Context context, MenuItem item, int colorResId) {
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(context.getResources().getColor(colorResId),
                    PorterDuff.Mode.SRC_ATOP);
        }
    }
}