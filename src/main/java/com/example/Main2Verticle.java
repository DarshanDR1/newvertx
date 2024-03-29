package com.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import io.vertx.core.*;

/**
 * Main Class
 */
public class Main2Verticle {

  /**
   * @param args
   * 
   *             Main function, here starting all the verticles
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    
   
      Injector injector = Guice.createInjector(new AppModule());
      compose_example cmp = injector.getInstance(compose_example.class);
      vertx.deployVerticle(cmp);

    

    vertx.deployVerticle(new post_db());
  }
}
