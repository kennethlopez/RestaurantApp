package com.restaurantapp.util;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RatingBar;

import java.util.List;


public final class AppUtil {

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static int kmToMeters(int km) {
        return km * 1000;
    }

    public static long toMilliseconds(double second) {
        return (long) (second * 1000);
    }

    public static String getGooglePhotosLink(String reference, int maxWidth, int maxHeight,
                                             String apiKey) {
        return Constants.AppConstants.MAPS_API_SERVICE_HOST + "place/photo?maxwidth=" + maxWidth
                + "&maxheight=" + maxHeight + "&photoreference=" + reference +
                "&key=" + apiKey;
    }

    public static int indexOf(int value, int[] array) {
        for (int c = 0; c < array.length; c++) if (array[c] == value) return  c;
        return 0;
    }

    static String favoritesToString(List<String> favorites) {
        boolean first = true;
        String favoritesString = "";
        for (String favorite : favorites) {
            if (first) {
                first = false;
                favoritesString = favorite;
            } else {
                favoritesString = favoritesString.concat(Constants.AppConstants.FAVORITES_SEPARATOR)
                        .concat(favorite);
            }
        }
        return favoritesString;
    }

    public static boolean overQueryLimit(String status) {
        return status.contentEquals(Constants.AppConstants.OVER_QUERY_LIMIT);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setRating(RatingBar ratingBar, AppCompatTextView ratingText, float rating) {
        ratingBar.setRating(rating);
        String ratingString;

        // check if rating is whole number
        if (rating - (int) rating == 0) ratingString = String.valueOf((int) rating);
        else ratingString = String.valueOf(rating);
        ratingText.setText(ratingString);
    }

    public static void loadFragmentFadeIn(int containerViewId, Handler handler,
        FragmentManager manager,  Fragment fragment, String tag) {

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable runnable = () -> {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(containerViewId, fragment, tag);
            transaction.commitAllowingStateLoss();
        };

        handler.post(runnable);
    }
}