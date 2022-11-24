package com.example;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;

public class ev implements EventService {

        /*
         * (non-Javadoc)
         * 
         * @see com.example.EventService#selectAccount(io.vertx.sqlclient.SqlConnection,
         * java.lang.String)
         * 
         * Actual implementation of select account function
         */

        @Override
        public Future<JsonObject> selectAccount(SqlConnection connection, String a) {
                Promise<JsonObject> p4 = Promise.promise();
                // System.out
                // .println("Calling p4 of ev");

                connection
                                .preparedQuery(
                                                "SELECT * from demo.account where acc_name=$1")
                                .execute(Tuple.of(a), ar1 -> {
                                        JsonObject data = new JsonObject();
                                        if (ar1.succeeded() && ar1.result().size() > 0) {
                                                RowSet<Row> rs = ar1.result();
                                                data = rs.iterator().next().toJson();
                                                p4.complete(data);
                                        } else if (ar1.succeeded() && ar1.result().size() == 0) {
                                                p4.fail("because name not Found in database");
                                        } else {
                                                System.out.println("Failure: " + ar1.cause().getMessage());
                                                p4.fail("Failed due to unknow reason");
                                        }

                                });
                // System.out
                // .println("P4 returning future");
                return p4.future();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.example.EventService#updateAccount(io.vertx.sqlclient.SqlConnection,
         * double, java.lang.String)
         * 
         * Actual implementation of update account function
         */
        @Override
        public Future<Boolean> updateAccount(SqlConnection connection, double balance, String acc_name) {
                Promise<Boolean> p3 = Promise.promise();
                // System.out
                // .println("Calling p3 of EV");

                connection
                                .preparedQuery(
                                                "UPDATE demo.account SET balance=$1 where acc_name=$2")
                                .execute(Tuple.of(balance, acc_name), (res5) -> {
                                        if (res5.succeeded()) {
                                                System.out
                                                                .println("updated account Balance ");

                                                p3.complete(true);
                                        } else {
                                                p3.complete(false);
                                        }
                                });
                // System.out
                // .println("P3 returning future of EV");
                return p3.future();
        }
}
