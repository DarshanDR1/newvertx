package com.example;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventService.class).toInstance(new ev());
    }
}
