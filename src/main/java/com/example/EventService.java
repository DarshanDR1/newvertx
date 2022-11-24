package com.example;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.SqlConnection;

public interface EventService {

     /**
      * @param connection
      * @param a
      * @return
      */
     public Future<JsonObject> selectAccount(SqlConnection connection, String a);

     /**
      * @param connection
      * @param balance
      * @param string
      * @return
      */
     public Future<Boolean> updateAccount(SqlConnection connection, double balance, String string);

}
