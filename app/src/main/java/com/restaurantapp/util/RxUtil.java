package com.restaurantapp.util;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RxUtil {
    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    public static Observable<Double> reconnectDelay(double seconds) {
        if (seconds == 0) seconds = 0.5;
        else if (seconds > 12) seconds = 12;
        return Observable.just(seconds)
                .delay(AppUtil.toMilliseconds(seconds), TimeUnit.MILLISECONDS)
                .map(aDouble -> aDouble + 0.5);
    }
}