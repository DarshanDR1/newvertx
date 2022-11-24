package com.example;

import io.vertx.core.*;

/**
 * Main Class
 */
public class Main2Verticle {

  
  /**
   * @param args
   * 
   * Main function, here starting all the verticles
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new compose_example(new ev()));
    vertx.deployVerticle(new post_db());
  }
}
