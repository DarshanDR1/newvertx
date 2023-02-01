package com.example;

import com.google.inject.AbstractModule;

public class AppModule1 extends AbstractModule {
    private mockDatabaseAction dao;

    AppModule1() {
        // Nitrite nitrite = Nitrite.builder().openOrCreate();
        dao = new mockDatabaseAction();
    }

    @Override
    protected void configure() {
        bind(EventService.class).toInstance(this.dao);
    }
}

