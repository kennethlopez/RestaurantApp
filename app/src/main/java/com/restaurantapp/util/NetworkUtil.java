package com.restaurantapp.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public final class NetworkUtil {
    /**
     * Returns true if the Throwable is an instance of RetrofitError with an
     * http status code equals to the given one.
     */
    public static boolean isHttpStatusCode(Throwable throwable, int statusCode) {
        return throwable instanceof HttpException
                && ((HttpException) throwable).code() == statusCode;
    }

    /**
     * returns true if exception was caused by device not able to connect to internet
     * */
    public static boolean isConnectionError(Throwable throwable) {
        return throwable instanceof UnknownHostException ||
                throwable instanceof SocketTimeoutException ||
                throwable instanceof ConnectException;
    }

    public static boolean isNetworkConnected(final Context context) {
        // check if connected to WiFi/data
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }
}