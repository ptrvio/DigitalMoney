package com.homeBankingTest.integrationtests.sprint1;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Sprint1 {

    private static final String url = "http://localhost:8084/users";
    private final String url2 = "http://localhost:8081/users";
    private final String registerPath = "/register";
    private static final String loginPath = "/login";
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
                        .post(url + loginPath)
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("access_token");
    }
    // REGISTRAR EXITOSO DE USUARIO
    @Test
    public void register() {

        JsonObject request = new JsonObject();
        request.addProperty("firstName", "Test5");
        request.addProperty("lastName", "User5");
        request.addProperty("dni", "12345467");
        request.addProperty("email", "test5@mail.com");
        request.addProperty("phone", "2664786454");
        request.addProperty("password", "Test1234");

        given()
                .contentType("application/json")
                .body(request)
                .post(url + registerPath)
                .then()
                .statusCode(201)
                .log().body();
    }

    //REGISTRO CON CAMPO FALTANTE (LASTNAME)
    @Test
    public void badRegister_1() {

        JsonObject request = new JsonObject();
        request.addProperty("firstName", "Test");
        request.addProperty("UserName", "test@mail.com");
        request.addProperty("email", "test@mail.com");
        request.addProperty("dni", "12345367");
        request.addProperty("phone", "2664786959");
        request.addProperty("password", "Test1234");
        given()
                .contentType("application/json")
                .body(request)
                .post(url + registerPath)
                .then()
                .statusCode(400)
                .log().body();
    }

    //REGISTRAR USUARIO PREVIAMENTE REGISTRADO
    @Test
    public void badRegister_2() {

        JsonObject request = new JsonObject();
        request.addProperty("firstName", "Test");
        request.addProperty("lastName", "User");
        request.addProperty("dni", "12345367");
        request.addProperty("email", "test@mail.com");
        request.addProperty("phone", "2664786959");
        request.addProperty("password", "Test1234");

        given()
                .contentType("application/json")
                .body(request)
                .post(url + registerPath)
                .then()
                .statusCode(400)
                .log().body();
    }

    //REGISTRA USUARIO CON USERNAME USADO
    @Test
    public void badRegister_3() {

        JsonObject request = new JsonObject();
        request.addProperty("firstName", "Test2");
        request.addProperty("lastName", "User2");
        request.addProperty("username", "test@mail.com");
        request.addProperty("email", "test@mail.com");
        request.addProperty("dni", "22258489");
        request.addProperty("phone", "2664565708");
        request.addProperty("password", "Test2345");

        given()
                .contentType("application/json")
                .body(request)
                .post(url + registerPath)
                .then()
                .statusCode(400)
                .log().body();
    }
    //LOGIN FALLIDO - CAMPO EMAIL
    @Test
    public void badLoginUser() {

        JsonObject request = new JsonObject();
        request.addProperty("email", "tests@yahoo.com");
        request.addProperty("password", "Test1234");

        given()
                .contentType("application/json")
                .body(request)
                .post(url + loginPath)
                .then()
                .statusCode(404)
                .log().body();
    }
    //LOGIN FALLIDO - CAMPO INCORRECTO
    @Test
    public void badLoginPassword() {

        JsonObject request = new JsonObject();
        request.addProperty("email", "test@mail.com");
        request.addProperty("password", "Test8907");

        given()
                .contentType("application/json")
                .body(request)
                .post(url + loginPath)
                .then()
                .statusCode(400)
                .log().body();
    }

    //RECUPERAR CONTRASEÑA
    @Test
    public void forgotPassword() {

        String forgotPasswordPath = "/ptrvio@gmail.com/forgot-password";

        given()
                .put(url2 + forgotPasswordPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //LOGOUT EXITOSO
    @Test
    public void logout() {

        String logoutPath = "/logout";


        given()
                .header("Authorization", "Bearer " + accessToken)
                .post(url + logoutPath)
                .then()
                .statusCode(200)
                .log().body();
    }
}
