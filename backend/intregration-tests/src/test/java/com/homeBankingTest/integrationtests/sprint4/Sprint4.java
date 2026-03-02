package com.homeBankingTest.integrationtests.sprint4;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Sprint4 {

    private static final String userUrl = "http://localhost:8084/users";
    private static final String userUrl2 = "http://localhost:8081/users";
    private static final String loginPath = "/login";
    String userId = "/5";
    String kcId = "/1cc83885-1229-4334-8d52-578aedc440cd";
    private static String accessToken;

    @BeforeAll
    static void login() {

        JsonObject request = new JsonObject();
        request.addProperty("email", "test2@mail.com");
        request.addProperty("password", "Test1234");

        accessToken =
                given()
                        .contentType("application/json")
                        .body(request.toString())
                        .when()
                        .post(userUrl + loginPath)
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("access_token");
    }

    //ENVIAR DINERO CON CVU
    @Test
    public void transferMoney() {

        String sendMoneyPath = "/activities";

        JsonObject request = new JsonObject();
        request.addProperty("accountId", 1);
        request.addProperty("type", "Transfer");
        request.addProperty("amount", 25000);
        request.addProperty("origin", "7031990505926037847195");
        request.addProperty("destination", "7031990390208919166700");
        request.addProperty("name", "Pablo Trivino");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .post(userUrl + kcId + sendMoneyPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //ENVIAR DINERO CON ALIAS
    @Test
    public void transferMoney2() {

        String sendMoneyPath = "/activities";
        String kcIdDestination = "/f6aea453-3751-4343-8e0f-188ad92eae3f";

        String cvu =
                given()
                        .contentType("application/json")
                        .when()
                        .get(userUrl + kcIdDestination)
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("cvu");

        JsonObject request = new JsonObject();
        request.addProperty("accountId", 1);
        request.addProperty("type", "Transfer");
        request.addProperty("amount", 18000);
        request.addProperty("origin", "7031990505926037847195");
        request.addProperty("destination", cvu);
        request.addProperty("name", "Pablo Trivino");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .post(userUrl + kcId + sendMoneyPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //ENVIAR DINERO FALLIDO - FALTA DE DINERO
    @Test
    public void transferMoneyFail() {

        String sendMoneyPath = "/activities";

        JsonObject request = new JsonObject();
        request.addProperty("accountId", 1);
        request.addProperty("type", "Transfer");
        request.addProperty("amount", 555000);
        request.addProperty("origin", "7031990505926037847195");
        request.addProperty("destination", "7031990390208919166700");
        request.addProperty("name", "Pablo Trivino");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .post(userUrl + kcId + sendMoneyPath)
                .then()
                .statusCode(400)
                .log().body();
    }
}
