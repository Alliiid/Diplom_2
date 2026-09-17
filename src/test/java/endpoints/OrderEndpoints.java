package endpoints;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderEndpoints {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru";
    private static final String ORDERS_ENDPOINT = "/api/orders";
    private static final String INGREDIENTS_ENDPOINT = "/api/ingredients";

    @Step("Создать заказ с авторизацией")
    public static Response createOrder(Order order, String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .post(BASE_URL + ORDERS_ENDPOINT);
    }

    @Step("Создать заказ без авторизации")
    public static Response createOrderWithoutAuth(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .post(BASE_URL + ORDERS_ENDPOINT);
    }

    @Step("Получить список ингредиентов")
    public static Response getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .get(BASE_URL + INGREDIENTS_ENDPOINT);
    }

    @Step("Получить ID валидного ингредиента")
    public static String getValidIngredientId() {
        Response response = getIngredients();
        return response.jsonPath().getString("data[0]._id");
    }

    @Step("Получить ID ингредиента по индексу {index}")
    public static String getIngredientIdByIndex(int index) {
        Response response = getIngredients();
        return response.jsonPath().getString("data[" + index + "]._id");
    }
}