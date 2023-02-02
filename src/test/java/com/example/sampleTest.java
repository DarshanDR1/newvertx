package com.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.inject.Guice;
import com.google.inject.Injector;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxTestContext;
import io.vertx.junit5.VertxExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(VertxExtension.class)
public class sampleTest {

    mockDatabaseAction h1 = new mockDatabaseAction(); // Object to access mockDatabaseAction class

    static ev asPy = Mockito.mock(ev.class); // mocking ev class

    /**
     * @param vertx
     * @throws Exception
     */
    @BeforeAll
    public static void setUp(Vertx vertx, VertxTestContext testContext) throws Exception {
        Injector injector = Guice.createInjector(new AppModule1());
        compose_example cmp = injector.getInstance(compose_example.class);
        // vertx.deployVerticle(new post_db()); // Deploying server
        // deploying database verticle
        vertx.deployVerticle(cmp,
                ar -> {
                    if (ar.succeeded()) {
                        vertx.deployVerticle(new post_db(), ardb -> {
                            if(ardb.succeeded()) testContext.completeNow();
                            else testContext.failNow("HTTP VERTICLE FAILED");
                        });
                    } else {
                        System.out.println("Failed");
                        testContext.causeOfFailure();
                    }
                });
    }

    /**
     * @param vertx
     * @param testContext
     */
    @Test
    @DisplayName(" Deploy a HTTP service verticle and make requests 1")
    public void useSampleVerticle1(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        /**
         * mocking select account function
         * 
         */
        // doAnswer(invocation -> {
        // String name = invocation.getArgument(1);
        // return h1.selectAccount(name); // calling mock implementation class
        // }).when(asPy).selectAccount(any(), any());

        /**
         * mocking update account function
         * 
         */
        // doAnswer(invocation -> {
        // System.out.println("Updated");
        // double c = invocation.getArgument(1);
        // System.out.println("New balance = " + c);

        // String name = invocation.getArgument(2);
        // System.out.println(name);
        // return h1.updateAccount(name, c); // calling mock implementation class
        // }).when(asPy).updateAccount(any(), anyDouble(), any());

        /**
         * sending get request
         * 
         */

        Future<HttpResponse<Buffer>> c = client.get(8080, "localhost", "/api/account/Ashish").send();
System.out.println("in api");
        c.onSuccess(resp -> {

            testContext.verify(() -> {
                if (resp.statusCode() == 200) {
                    assertThat(resp.statusCode()).isEqualTo(200); // checking response by status code

                    assertEquals(680.0, (resp.bodyAsJsonObject()).getMap().get("balance")); // comparing expected and
                                                                                            // actual values
                } else {
                    assertThat(resp.statusCode()).isEqualTo(404);
                }
                testContext.completeNow();

            });

        });
        c.onFailure(f->{
            testContext.failNow(f.getMessage());
        });

    }

    // /**
    // * @param vertx
    // * @param testContext
    // */
    // @Test
    // @DisplayName(" Deploy a HTTP service verticle and make requests 2")
    // public void useSampleVerticle2(Vertx vertx, VertxTestContext testContext) {
    // WebClient client = WebClient.create(vertx);

    // doAnswer(invocation -> { // lambda form of below commented (do answer)
    // String name = invocation.getArgument(1);
    // return h1.selectAccount(name); // calling mock implementation class
    // }).when(asPy).selectAccount(any(), any());

    // // doAnswer(new Answer() {
    // // public Future<JsonObject> answer(InvocationOnMock invocation) {
    // // String name = invocation.getArgument(1);
    // // return h1.selectAccount(name);
    // // }
    // // }).when(asPy).selectAccount(any(), any());

    // doAnswer(invocation -> {
    // System.out.println("Updated");
    // double c = invocation.getArgument(1);
    // System.out.println("New balance = " + c);

    // String name = invocation.getArgument(2);
    // System.out.println(name);
    // return h1.updateAccount(name, c); // calling mock implementation class
    // }).when(asPy).updateAccount(any(), anyDouble(), any());

    // Future<HttpResponse<Buffer>> c = client.get(80, "localhost",
    // "/api/account/Ashish").send();

    // c.onSuccess(resp -> {

    // testContext.verify(() -> {
    // if(resp.statusCode()==200){
    // assertThat(resp.statusCode()).isEqualTo(200 ); // checking response by status
    // code

    // assertEquals(7699.0, (resp.bodyAsJsonObject()).getMap().get("balance")); //
    // comparing expected and
    // // actual values
    // }
    // else{
    // assertThat(resp.statusCode()).isEqualTo(404);
    // }
    // testContext.completeNow();

    // });

    // });
    // c.onFailure(resp->{
    // System.out.println("Failed");
    // });

    // }

    /**
     * @throws Exception
     */
    @AfterAll
    public static void tearDown(Vertx vertx) throws Exception {

      
        vertx.close();

        System.out.println("vertx closed..... ");
    }
}
