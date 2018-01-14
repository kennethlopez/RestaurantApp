package com.restaurantapp.util;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class RxBus {
    private final BackpressureStrategy mBackPressureStrategy = BackpressureStrategy.BUFFER;
    private final PublishSubject<Object> mBusSubject;

    @Inject
    public RxBus() {
        mBusSubject = PublishSubject.create();
    }

    /**
     * Posts an object (usually an Event) to the bus
     */
    public void post(Object event) {
        mBusSubject.onNext(event);
    }

    /**
     * Observable that will emmit everything posted to the event bus.
     */
    public Flowable<Object> flowable(){
        return mBusSubject.toFlowable(mBackPressureStrategy);
    }

    /**
     * Observable that only emits events of a specific class.
     * Use this if you only want to subscribe to one type of events.
     */
    public <T> Flowable <T> filteredFlowable(Class<T> clazz) {
        return mBusSubject.ofType(clazz).toFlowable(mBackPressureStrategy);
    }
}