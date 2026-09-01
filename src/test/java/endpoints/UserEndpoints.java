package endpoints;

import io.restassured.response.Response;
import models.User;

import static io.restassured.RestAssured.given;

public class UserEndpoints {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru";
    private static final String REGISTER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";

    public static Response createUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .post(BASE_URL + REGISTER_ENDPOINT);
    }

    public static Response loginUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .post(BASE_URL + LOGIN_ENDPOINT);
    }
}