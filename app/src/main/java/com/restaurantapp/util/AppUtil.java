package com.restaurantapp.util;


import android.content.Context;
import android.content.res.Resources;



public final class AppUtil {

    public static String[] getStringArray(Context context, int resId) {
        return context.getResources().getStringArray(resId);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static int kmToMeters(int km) {
        return km * 1000;
    }

    public static String getGooglePhotosLink(String reference, int maxWidth, int maxHeight) {
        return Constants.AppConstants.PLACES_API_SERVICE_HOST + "photo?maxwidth=" + maxWidth
                + "&maxheight=" + maxHeight + "&photoreference=" + reference +
                "&key=" + Constants.AppConstants.GOOGLE_API_KEY;
    }

    public static int indexOf(int value, int[] array) {
        for (int c = 0; c < array.length; c++) if (array[c] == value) return  c;

        return 0;
    }

    public static int secondToMil(double second) {
        return (int) (second * 1000);
    }
}