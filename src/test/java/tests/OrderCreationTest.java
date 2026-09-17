package tests;

import endpoints.OrderEndpoints;
import endpoints.UserEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.TestDataGenerator;

import java.util.Arrays;
import java.util.Collections;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderCreationTest {

    private User testUser;
    private String accessToken;
    private String validIngredientId;

    @Before
    @Step("Подготовка тестовых данных: создание пользователя и получение ID ингредиента")
    public void setUp() {
        testUser = TestDataGenerator.generateUniqueUser();
        Response createResponse = UserEndpoints.createUser(testUser);
        accessToken = createResponse.jsonPath().getString("accessToken");
        validIngredientId = OrderEndpoints.getValidIngredientId();
    }

    @After
    @Step("Очистка: сброс accessToken")
    public void tearDown() {
        accessToken = null;
    }

    @Test
    @DisplayName("Создание заказа с авторизацией: статус 200")
    @Description("Проверяем, что создание заказа с авторизацией возвращает 200")
    public void testCreateOrderWithAuthReturnsOk() {
        Order order = Order.builder().ingredients(Arrays.asList(validIngredientId)).build();
        Response response = OrderEndpoints.createOrder(order, accessToken);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Создание заказа с авторизацией: success=true")
    @Description("Проверяем, что ответ содержит success=true")
    public void testCreateOrderWithAuthReturnsSuccessTrue() {
        Order order = Order.builder().ingredients(Arrays.asList(validIngredientId)).build();
        Response response = OrderEndpoints.createOrder(order, accessToken);
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией: order.number > 0")
    @Description("Проверяем, что номер заказа больше 0")
    public void testCreateOrderWithAuthReturnsOrderNumber() {
        Order order = Order.builder().ingredients(Arrays.asList(validIngredientId)).build();
        Response response = OrderEndpoints.createOrder(order, accessToken);
        assertTrue(response.jsonPath().getInt("order.number") > 0);
    }

    @Test
    @DisplayName("Создание заказа без авторизации: статус 200")
    @Description("Проверяем, что создание заказа без авторизации возвращает 200")
    public void testCreateOrderWithoutAuthReturnsOk() {
        Order order = Order.builder().ingredients(Arrays.asList(validIngredientId)).build();
        Response response = OrderEndpoints.createOrderWithoutAuth(order);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Создание заказа без авторизации: success=true")
    @Description("Проверяем, что ответ содержит success=true при создании без авторизации")
    public void testCreateOrderWithoutAuthReturnsSuccessTrue() {
        Order order = Order.builder().ingredients(Arrays.asList(validIngredientId)).build();
        Response response = OrderEndpoints.createOrderWithoutAuth(order);
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Создание заказа с валидными ингредиентами: статус 200")
    @Description("Проверяем, что создание заказа с валидными ингредиентами возвращает 200")
    public void testCreateOrderWithValidIngredientsReturnsOk() {
        Order order = Order.builder()
                .ingredients(Arrays.asList(validIngredientId, validIngredientId))
                .build();
        Response response = OrderEndpoints.createOrder(order, accessToken);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов: статус 400")
    @Description("Проверяем, что создание заказа без ингредиентов возвращает 400")
    public void testCreateOrderWithoutIngredientsReturnsBadRequest() {
        Order emptyOrder = Order.builder().ingredients(Collections.emptyList()).build();
        Response response = OrderEndpoints.createOrder(emptyOrder, accessToken);
        assertEquals(SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов: сообщение об ошибке")
    @Description("Проверяем, что сообщение об ошибке корректно")
    public void testCreateOrderWithoutIngredientsReturnsCorrectMessage() {
        Order emptyOrder = Order.builder().ingredients(Collections.emptyList()).build();
        Response response = OrderEndpoints.createOrder(emptyOrder, accessToken);
        assertEquals("Ingredient ids must be provided", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем: статус 500")
    @Description("Проверяем, что создание заказа с неверным хешем возвращает 500")
    public void testCreateOrderWithInvalidHashReturnsInternalServerError() {
        Order order = Order.builder().ingredients(Arrays.asList("invalid_hash_123")).build();
        Response response = OrderEndpoints.createOrder(order, accessToken);
        assertEquals(SC_INTERNAL_SERVER_ERROR, response.statusCode());
    }

    @Test
    @DisplayName("Создание заказа с несколькими ингредиентами: статус 200")
    @Description("Проверяем, что создание заказа с несколькими ингредиентами возвращает 200")
    public void testCreateOrderWithMultipleIngredientsReturnsOk() {
        String secondIngredient = OrderEndpoints.getIngredientIdByIndex(1);
        Order order = Order.builder()
                .ingredients(Arrays.asList(validIngredientId, secondIngredient))
                .build();
        Response response = OrderEndpoints.createOrder(order, accessToken);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Создание заказа с тремя ингредиентами: статус 200")
    @Description("Проверяем, что создание заказа с тремя ингредиентами возвращает 200")
    public void testCreateOrderWithThreeIngredientsReturnsOk() {
        String secondIngredient = OrderEndpoints.getIngredientIdByIndex(2);
        String thirdIngredient = OrderEndpoints.getIngredientIdByIndex(3);
        Order order = Order.builder()
                .ingredients(Arrays.asList(validIngredientId, secondIngredient, thirdIngredient))
                .build();
        Response response = OrderEndpoints.createOrder(order, accessToken);
        assertEquals(SC_OK, response.statusCode());
    }
}