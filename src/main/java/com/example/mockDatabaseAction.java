package com.example;

import java.util.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.SqlConnection;

public class mockDatabaseAction implements EventService {
    int count = 0;
    HashMap<String, JsonObject> objectStore = new HashMap<String, JsonObject>();

    /**
     * constructor used to initialize values required by methods
     */
    mockDatabaseAction() {

        JsonObject j1 = new JsonObject();
        j1.put("acc_id", 1);
        j1.put("acc_num", "102");
        j1.put("org_name", "Darshan");
        j1.put("balance", 1000.0);
        j1.put("version", 1);

        JsonObject j2 = new JsonObject();
        j2.put("acc_id", 2);
        j2.put("acc_num", "101");
        j2.put("acc_name", "Ashish");
        j2.put("balance", 80.0);
        j2.put("version", 1);

        JsonObject j3 = new JsonObject();
        j3.put("acc_id", 4);
        j3.put("acc_num", "104");
        j3.put("acc_name", "Prajwal");
        j3.put("balance", 200.0);
        j3.put("version", 1);

        JsonObject j4 = new JsonObject();
        j4.put("acc_id", 3);
        j4.put("acc_num", "103");
        j4.put("acc_name", "Amshu");
        j4.put("balance", 99.0);
        j4.put("version", 1);

        objectStore.put("Darshan", j1);
        objectStore.put("Ashish", j2);
        objectStore.put("Prajwal", j3);
        objectStore.put("Amshu", j4);

    }

    /**
     * @param str
     * @return
     *         Mock implementation of the select account function
     */
    // Future<JsonObject> selectAccount(String str) {

    // if (!str.isEmpty()) {
    // if (objectStore.containsKey(str)) {
    // return Future.succeededFuture(objectStore.get(str));
    // } else {
    // return Future.failedFuture("Because, Name Not Found. Please Enter Registered
    // Name!!!...");
    // }
    // }
    // return Future.succeededFuture(null);

    // }

    /**
     * @param str
     * @param balance
     * @return
     *         Mock implementation of the update account function
     */
    // Future<Boolean> updateAccount(String str, double balance) {
    //     if (!str.isEmpty()) {
    //         JsonObject accountRec = objectStore.get(str);
    //         accountRec.put("balance", balance);
    //         objectStore.put(str, accountRec);
    //     }

    //     return Future.succeededFuture(false);
    // }

    @Override
    public Future<JsonObject> selectAccount(SqlConnection connection, String str) {
        if (!str.isEmpty()) {
            if (objectStore.containsKey(str)) {
                return Future.succeededFuture(objectStore.get(str));
            } else {
                return Future.failedFuture("Because, Name Not Found. Please Enter Registered Name!!!...");
            }
        }
        return Future.succeededFuture(null);
    }

    @Override
    public Future<Boolean> updateAccount(SqlConnection connection, double balance, String str) {
        if (!str.isEmpty()) {
            JsonObject accountRec = objectStore.get(str);
            accountRec.put("balance", balance);
            objectStore.put(str, accountRec);
        }

        return Future.succeededFuture(false);
    }
}
