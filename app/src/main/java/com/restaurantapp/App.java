package com.restaurantapp;


import android.app.Application;
import android.content.Context;

import com.restaurantapp.injection.component.AppComponent;
import com.restaurantapp.injection.component.DaggerAppComponent;
import com.restaurantapp.injection.module.AppModule;
import com.restaurantapp.injection.module.RoomModule;


public class App extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .build();
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(AppComponent applicationComponent) {
        mAppComponent = applicationComponent;
    }
}
