package com.restaurantapp.test.common;


import android.content.Context;

import com.restaurantapp.App;
import com.restaurantapp.data.AppDatabase;
import com.restaurantapp.test.common.injection.component.DaggerTestComponent;
import com.restaurantapp.test.common.injection.component.TestComponent;
import com.restaurantapp.test.common.injection.module.AppTestModule;
import com.restaurantapp.test.common.injection.module.RoomTestModule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Test rule that creates and sets a Dagger TestComponent into the application overriding the
 * existing application component.
 * Use this rule in your test case in order for the app to use mock dependencies.
 * It also exposes some of the dependencies so they can be easily accessed from the tests, e.g. to
 * stub mocks etc.
 */
public class TestComponentRule implements TestRule {

    private final TestComponent mTestComponent;
    private final Context mContext;

    public TestComponentRule(Context context) {
        mContext = context;
        App application = App.get(context);
        // cannot resolve symbol is alright, because this class will be generated once the tests starts
        // note that DaggerTestComponent is generated based from TestComponent
        mTestComponent = DaggerTestComponent.builder()
                .appTestModule(new AppTestModule(application))
                .roomTestModule(new RoomTestModule(context))
                .build();
    }

    public Context getContext() {
        return mContext;
    }

    public AppDatabase getAppDatabase() {
        return mTestComponent.appDatabase();
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                App application = App.get(mContext);
                application.setComponent(mTestComponent);
                base.evaluate();
                application.setComponent(null);
            }
        };
    }
}