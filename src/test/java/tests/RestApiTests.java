package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;


public class RestApiTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    public void successfulCreateUser() {
        given().contentType(ContentType.JSON)
                .body("{ \"name\": \"morpheus\", \"job\": \"leader\" }")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"));
    }

    @Test
    void successDalayedResponse() {
        given()
                .when()
                .get("/api/users?delay=3")
                .then()
                .statusCode(200)
                .body("data.email[3]", is("eve.holt@reqres.in"))
                .body("total", is(12))
                .body("total_pages", not(18))
                .body("support.text", is("To keep ReqRes free, " +
                        "contributions towards server costs are appreciated!"));
    }

    @Test
    void successUnknown() {
        given()
                .when()
                .get("/api/unknown/2")
                .then()
                .statusCode(200)
                .header("vary", "Accept-Encoding");
    }

    @Test
    void successRegister() {
        given().contentType(ContentType.JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }")
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));

    }

    String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

    @Test
    void successUpdate() {
        given().contentType(ContentType.JSON)
                .body("{ \"name\": \"morpheus\", \"job\": \"zion resident\" }")
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("updatedAt", containsString(String.valueOf(date)));

    }

}