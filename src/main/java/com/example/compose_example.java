package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlConnection;

public class compose_example extends AbstractVerticle {

    EventService eventService;

    public EventService getEventService() {
        return eventService;
    }

    /**
     * @param eventService
     *                     constructor with eventService interface object
     */

    public compose_example(EventService eventService) {
        this.eventService = eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.vertx.core.AbstractVerticle#start()
     */
    @Override
    public void start() throws Exception {
        System.out.println("Database connection");
        PgConnectOptions connectOptions = new PgConnectOptions() // Parameters to connect to the database

                .setPort(5432)
                .setHost("localhost")
                .setDatabase("postgres")
                .setUser("postgres")
                .setPassword("admin");
        System.out.println("getting connection1");
        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        vertx.eventBus().consumer("http_req", handler -> {
            PgPool pool = PgPool.pool(vertx, connectOptions, poolOptions);
            JsonObject acc = (JsonObject) handler.body();

            System.out.println("getting connection");

            Future<JsonObject> f1 = asyncFetch1(pool, acc);

            f1.onSuccess((ar) -> {
                System.out.println(ar);
                if (!ar.isEmpty()) {
                    handler.reply(ar); // sending requested data from database
                } else {
                    handler.reply(" Name Not Found");
                }
            });
            f1.onFailure((err) -> {

                // handler.reply(err.getMessage().toString());
                handler.fail(404, err.toString());
            });
        });

    }

    /**
     * @param poolpool
     * @param acc
     * @return
     */
    Future<JsonObject> asyncFetch1(PgPool poolpool, JsonObject acc) {

        Promise<JsonObject> promise1 = Promise.promise();
        // System.out.println("calling p1");
        Future<SqlConnection> p = poolpool.getConnection();

        p.onSuccess(
                connection -> {

                    connection.begin()

                            .compose(tx -> {
                                System.out.println("calling select account function before update -------------->");

                                return eventService.selectAccount(connection,
                                        acc.getString("acc_name")) // calling interface(eventService)

                                        .compose(acc_obj -> {
                                            System.out.println(acc_obj);
                                            System.out.println("calling update account function----------->");

                                            return eventService.updateAccount(connection,
                                                    acc_obj.getDouble("balance") + 600,
                                                    acc_obj.getString("acc_name"));
                                        }) // calling interface(eventService)

                                        .compose(result -> {
                                            System.out.println(
                                                    "calling select account function after update-------------->");
                                            return eventService.selectAccount(connection, acc.getString("acc_name")); // calling
                                                                                                                      // interface(eventService)
                                        })

                                        .onSuccess(res -> {
                                            promise1.complete(res);
                                            tx.commit();
                                        })
                                        .onFailure(err -> {
                                            System.out.println("Transaction failed " + err.getMessage());
                                            promise1.fail("Name not Found, Please Enter Registered Name");
                                        })
                                        .eventually(v -> connection.close());

                            });
                });
        p.onFailure(c -> {
            System.out.println(c.getCause());
        });
        // System.out.println("returning p1");
        return promise1.future();
    }
}