package tests;

import endpoints.OrderEndpoints;
import endpoints.UserEndpoints;
import io.restassured.response.Response;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.TestDataGenerator;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;

public class OrderCreationTest {

    private User testUser;
    private String accessToken;
    private String validIngredientId;

    @Before
    public void setUp() {
        testUser = TestDataGenerator.generateUniqueUser();
        Response createResponse = UserEndpoints.createUser(testUser);
        accessToken = createResponse.jsonPath().getString("accessToken");
        validIngredientId = OrderEndpoints.getValidIngredientId();
    }

    @After
    public void tearDown() {
        accessToken = null;
    }

    @Test
    public void createOrderWithAuthSuccess() {
        Order order = Order.builder()
                .ingredients(Arrays.asList(validIngredientId))
                .build();

        Response response = OrderEndpoints.createOrder(order, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", greaterThan(0));
    }

    @Test
    public void createOrderWithoutAuthSuccess() {
        Order order = Order.builder()
                .ingredients(Arrays.asList(validIngredientId))
                .build();

        Response response = OrderEndpoints.createOrderWithoutAuth(order);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", greaterThan(0));
    }

    @Test
    public void createOrderWithValidIngredientsSuccess() {
        Order order = Order.builder()
                .ingredients(Arrays.asList(validIngredientId, validIngredientId))
                .build();

        Response response = OrderEndpoints.createOrder(order, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", greaterThan(0));
    }

    @Test
    public void createOrderWithoutIngredientsFails() {
        Order emptyOrder = Order.builder()
                .ingredients(Collections.emptyList())
                .build();

        Response response = OrderEndpoints.createOrder(emptyOrder, accessToken);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithInvalidIngredientHashFails() {
        Order order = Order.builder()
                .ingredients(Arrays.asList("invalid_hash_123"))
                .build();

        Response response = OrderEndpoints.createOrder(order, accessToken);

        response.then()
                .statusCode(500);
    }

    @Test
    public void createOrderWithMultipleIngredientsSuccess() {
        String secondIngredient = OrderEndpoints.getIngredients()
                .jsonPath().getString("data[1]._id");

        Order order = Order.builder()
                .ingredients(Arrays.asList(validIngredientId, secondIngredient))
                .build();

        Response response = OrderEndpoints.createOrder(order, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", greaterThan(0));
    }

    @Test
    public void createOrderWithMultipleIngredientsWithAuthSuccess() {
        String secondIngredient = OrderEndpoints.getIngredients()
                .jsonPath().getString("data[2]._id");
        String thirdIngredient = OrderEndpoints.getIngredients()
                .jsonPath().getString("data[3]._id");

        Order order = Order.builder()
                .ingredients(Arrays.asList(validIngredientId, secondIngredient, thirdIngredient))
                .build();

        Response response = OrderEndpoints.createOrder(order, accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", greaterThan(0));
    }
}