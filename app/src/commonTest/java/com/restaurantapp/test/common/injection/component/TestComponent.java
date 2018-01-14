package com.restaurantapp.test.common.injection.component;


import com.restaurantapp.injection.component.AppComponent;
import com.restaurantapp.test.common.injection.module.AppTestModule;
import com.restaurantapp.test.common.injection.module.RoomTestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppTestModule.class, RoomTestModule.class})
public interface TestComponent extends AppComponent {
}