package com.homeBankingTest.integrationtests.sprint2;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Sprint2 {

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

    // OBTENER INFORMACION DE LA CUENTA
    @Test
    public void getAccountInformation() {
        String accountUrl = "/accounts";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .get(userUrl + userId + accountUrl)
                .then()
                .statusCode(200)
                .log().body();
    }

    // ULTIMOS MOVIMIENTOS DE LA CUENTA
    @Test
    public void getLastFiveTrasnsactions() {

        String transactionPath = "/activities?_limit=5";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .get(userUrl + kcId + transactionPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //ACTUALIZAR USUARIO
    @Test
    public void updateUser() {

        String updateUserPath = "/update-user";

        JsonObject request = new JsonObject();
        request.addProperty("firstName", "Test2");
        request.addProperty("lastName", "User2");
        request.addProperty("userName", "test2@mail.com");
        request.addProperty("email", "test2@mail.com");
        request.addProperty("phone", "2664556674");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .patch(userUrl + updateUserPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //ACTUALIZAR ALIAS
    @Test
    public void updateAlias() {

        String updateAliasPath = "/accounts/5";

        JsonObject request = new JsonObject();
        request.addProperty("alias", "JERGA.BUENO.BURRO");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .patch(userUrl + kcId + updateAliasPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //ACTUALIZAR PASSWORD
    @Test
    public void updatePassword() {

        String updatePasswordPath = "/update-password";

        JsonObject request = new JsonObject();
        request.addProperty("password", "Test1234");
        request.addProperty("passwordRepeated", "Test1234");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .patch(userUrl2 + updatePasswordPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //REGISTRAR TARJETA
    @Test
    public void registerCard() {

        String registerCardPath = "/cards";

        JsonObject request = new JsonObject();
        request.addProperty("name", "TESTDOS USER");
        request.addProperty("number", "1234123412341289");
        request.addProperty("expiration", "1231");
        request.addProperty("cvc", "648");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .post(userUrl + userId + registerCardPath)
                .then()
                .statusCode(201)
                .log().body();
    }

    //REGISTRAR TARJETA QUE YA EXISTE
    @Test
    public void registerCardFail_1() {

        String registerCardPath = "/cards";

        JsonObject request = new JsonObject();
        request.addProperty("name", "TESTDOS USER");
        request.addProperty("number", "1234123412341234");
        request.addProperty("expiration", "1228");
        request.addProperty("cvc", "648");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .post(userUrl + userId + registerCardPath)
                .then()
                .statusCode(409)
                .log().body();
    }

    //REGISTRAR TARJETA CON FORMULARIO INCOMPLETO
    @Test
    public void registerCardFail_2() {

        String registerCardPath = "/cards";

        JsonObject request = new JsonObject();
        request.addProperty("name", "TESTD USER");
        request.addProperty("number", "1234123412341238");
        request.addProperty("expiration", "1229");


        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .post(userUrl + userId + registerCardPath)
                .then()
                .statusCode(400)
                .log().body();
    }

    //OBTENER TARJETA POR ID
    @Test
    public void getCard() {

        String cardIdPath="/6";
        String cardPath = "/cards";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .get(userUrl + userId + cardPath + cardIdPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //OBTENER TARJETA CON ID INEXISTENTE
    @Test
    public void getCardFail() {

        String cardIdPath="/2";
        String cardPath = "/cards";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .get(userUrl + userId + cardPath + cardIdPath)
                .then()
                .statusCode(404)
                .log().body();
    }

    //OBTENER TODAS LAS TARJETAS DE UNA MISMA CUENTA
    @Test
    public void getAllCardsById_1() {

        String cardsPath = "/cards";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .get(userUrl+ userId + cardsPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //BORRAR TARJETA POR ID
    @Test
    public void deleteCard() {

        String deleteCardPath = "/cards";
        String cardId = "/6";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .delete(userUrl + userId + deleteCardPath + cardId)
                .then()
                .statusCode(200)
                .log().body();
    }

    //BORRAR TARJETA POR ID QUE NO EXISTE
    @Test
    public void deleteCardFail() {

        String deleteCardPath = "/cards";
        String cardId = "/9";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .delete(userUrl + userId + deleteCardPath + cardId)
                .then()
                .statusCode(404)
                .log().body();
    }
}

