package com.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class post_db extends AbstractVerticle {
  Injector injector = Guice.createInjector(new AppModule());
  compose_example cmp = injector.getInstance(compose_example.class);
  /*
   * (non-Javadoc)
   * 
   * @see io.vertx.core.AbstractVerticle#start(io.vertx.core.Promise)
   */
  public void start(Promise<Void> startPromise) throws Exception {

    System.out.println("Http methods");

    HttpServer server = vertx.createHttpServer(); // creating http server
    System.out.println("server start");
    Router router = Router.router(vertx);
    System.out.println("router getting cal");

    router.get("/api/account/:acc_name")
        .handler(BodyHandler.create()).handler(context -> {
          // JsonObject reqBody = context.body().asJsonObject();

          // System.out.println("Account details");

          // String acc_name = reqBody.getString("acc_name");
          String acc_name = context.request().getParam("acc_name");

          System.out.println("Account details");

          
          // String acc_name = reqBody.getString("acc_name");
          // System.out.println(acc_name);

          JsonObject resp = new JsonObject();

          resp.put("acc_name", acc_name);
          System.out.println(acc_name);
          vertx.eventBus().request("http_req", resp, message -> {

            System.out.println("Received response");
            if (message.result() != null) {
              context.response().putHeader("Name", "Darshan").putHeader("Lang", "en-us").setStatusCode(200)
                  .putHeader("Content-type", "text/html")
                  .end(
                      message.result().body().toString());
            } else {
              context.response().putHeader("Name", "Darshan").putHeader("Lang", "en-us").setStatusCode(404)
                  .putHeader("Content-type", "text/html")
                  .end(
                      "Name Not Found, Please Enter Registered Name!!!...");
            }
          });

        });

    server.requestHandler(router).listen(8080); // server listening on port 8080
    startPromise.complete();

  }

}