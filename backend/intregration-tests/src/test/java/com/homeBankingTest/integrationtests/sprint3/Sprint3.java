package com.homeBankingTest.integrationtests.sprint3;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Sprint3 {


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

    //OBTENER TODAS LAS TRANSFERENCIAS
    @Test
    public void getAllActivity() {

        String activityPath = "/activities";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .get(userUrl + kcId + activityPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //OBTENER TODAS LAS TRANSFERENCIAS SIN TENER ALGUNA HECHA
    @Test
    public void getAllActivityFail_1() {

        String activityPath = "/activities";
        String accessTokenWithoutActivities = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJPdDhhc2FjbXRmdHlVN0hRUWRiOEsxZGp5SzJaRWhxeHlBNzBTOXFWekZZIn0.eyJleHAiOjE3Njg0MDk5OTIsImlhdCI6MTc2ODQwODc5MiwianRpIjoiMWYwODM4YWYtMTZkYi00ZTk1LTliMTQtYmM5YzIyZWVkMzE0IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9kaC1tb25leS11c2VycyIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5ZTQxZmI3Mi01YWU2LTQ0YjYtYTA1OC1hZjA4ODAyN2EzMzkiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhcGktZGgtbW9uZXkiLCJzaWQiOiI3ZTk0ZThkYS0wYjVhLTQzYjAtODJiMS00MzMxNzVmNjBmNWYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4NCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtZGgtbW9uZXktdXNlcnMiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IkxhdXJhIEZlcm5hbmRleiIsInByZWZlcnJlZF91c2VybmFtZSI6ImxhdXJhQG1haWwuY29tIiwiZ2l2ZW5fbmFtZSI6IkxhdXJhIiwiZmFtaWx5X25hbWUiOiJGZXJuYW5kZXoiLCJlbWFpbCI6ImxhdXJhQG1haWwuY29tIn0.h6MDn-XtHmCONyoYFerUuK8RoUtQNkouOyToCFppL7H7gmrpMQApo8aCvDKVShpUAZ6A1EveSHDQQnneyg-qtgNEgnKRQmkozLgN6jDu4Tm7-v-6_zMYC6gJi6rAIrjeupYmSNV_Z82orU7ktnAayB1z0FCY3rNLQ1yGHkSWGl7BiIZUWjNrg1FBa1JSMH2JVUvXaMmZwEn7RGJNEN8S385ood3CrjV_on1ZsEJVSq4Mtcm6vcxj0esuEKMYPUn_7enYIg0NNb9VMdi4jEYoyW3hmAgzULIXPcjVUtfUqj4GRWNdyx9NbLtbKH0ISWKjOI1Geiy9pyjEo9IO0de4PQ";

        given()
                .header("Authorization", "Bearer " + accessTokenWithoutActivities)
                .get(userUrl + kcId + activityPath)
                .then()
                .statusCode(404)
                .log().body();
    }

    //OBTENER TRANSFERENCIA POR ID
    @Test
    public void getSingleActivity() {

        String activityPath = "/activities";
        String transactionId = "/17";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .get(userUrl + kcId + activityPath + transactionId)
                .then()
                .statusCode(200)
                .log().body();
    }

    //OBTENER TRANSFERENCIA CON ID INEXISTENTE
    @Test
    public void getSingleActivityFail_1() {

        String activityPath = "/activities";
        String transactionId = "/50";

        given()
                .header("Authorization", "Bearer " + accessToken)
                .get(userUrl + kcId + activityPath + transactionId)
                .then()
                .statusCode(404)
                .log().body();
    }

    //OBTENER TRANSFERENCIA POR ID SIN ESTAR LOGUEADO
    @Test
    public void getSingleActivityFail_2() {

        String activityPath = "/activities";
        String transactionId = "/17";

        given()
                .header("Authorization", "Bearer ")
                .get(userUrl + kcId + activityPath + transactionId)
                .then()
                .statusCode(401)
                .log().body();
    }

    //AGREGAR DINERO DESDE UNA TARJETA A LA CUENTA
    @Test
    public void addMoney() {

        String activityPath = "/activities";

        JsonObject request = new JsonObject();
        request.addProperty("amount", 80000);
        request.addProperty("type", "Deposit");
        request.addProperty("description", "Depósito con tarjeta");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .post(userUrl + kcId + activityPath)
                .then()
                .statusCode(200)
                .log().body();
    }

}
