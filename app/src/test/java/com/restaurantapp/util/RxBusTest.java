package com.restaurantapp.util;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.subscribers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class RxBusTest {
    @InjectMocks RxBus mBus;

    @Rule
    // Must be added to every test class that targets app code that uses RxJava
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Test
    public void postedObjectsAreReceived() {
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        mBus.flowable().subscribe(testSubscriber);

        Object event1 = new Object();
        Object event2 = new Object();
        mBus.post(event1);
        mBus.post(event2);

        testSubscriber.assertValues(event1, event2);
    }

    @Test
    public void filteredObservableOnlyReceivesSomeObjects() {
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        mBus.filteredFlowable(String.class).subscribe(testSubscriber);

        String stringEvent = "string event";
        Integer intEvent = 20;
        mBus.post(stringEvent);
        mBus.post(intEvent);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(stringEvent);
    }
}
