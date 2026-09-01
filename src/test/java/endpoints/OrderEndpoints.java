package endpoints;

import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderEndpoints {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru";
    private static final String ORDERS_ENDPOINT = "/api/orders";
    private static final String INGREDIENTS_ENDPOINT = "/api/ingredients";

    public static Response createOrder(Order order, String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .post(BASE_URL + ORDERS_ENDPOINT);
    }

    public static Response createOrderWithoutAuth(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .post(BASE_URL + ORDERS_ENDPOINT);
    }

    public static Response getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .get(BASE_URL + INGREDIENTS_ENDPOINT);
    }

    public static String getValidIngredientId() {
        Response response = getIngredients();
        return response.jsonPath().getString("data[0]._id");
    }
}