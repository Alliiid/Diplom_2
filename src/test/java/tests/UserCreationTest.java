package tests;

import endpoints.UserEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.TestDataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class UserCreationTest {

    private User testUser;
    private String accessToken;

    @Before
    @Step("Подготовка тестовых данных: генерация уникального пользователя")
    public void setUp() {
        testUser = TestDataGenerator.generateUniqueUser();
    }

    @After
    @Step("Очистка: сброс accessToken")
    public void tearDown() {
        accessToken = null;
    }

    @Test
    @DisplayName("Создание уникального пользователя: статус 200")
    @Description("Проверяем, что создание уникального пользователя возвращает статус 200")
    public void testCreateUniqueUserReturnsOk() {
        Response response = UserEndpoints.createUser(testUser);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Создание уникального пользователя: success=true")
    @Description("Проверяем, что ответ содержит success=true")
    public void testCreateUniqueUserReturnsSuccessTrue() {
        Response response = UserEndpoints.createUser(testUser);
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Создание уникального пользователя: корректный email")
    @Description("Проверяем, что email созданного пользователя совпадает с отправленным")
    public void testCreateUniqueUserReturnsCorrectEmail() {
        Response response = UserEndpoints.createUser(testUser);
        assertEquals(testUser.getEmail(), response.jsonPath().getString("user.email"));
    }

    @Test
    @DisplayName("Создание уникального пользователя: корректное имя")
    @Description("Проверяем, что имя созданного пользователя совпадает с отправленным")
    public void testCreateUniqueUserReturnsCorrectName() {
        Response response = UserEndpoints.createUser(testUser);
        assertEquals(testUser.getName(), response.jsonPath().getString("user.name"));
    }

    @Test
    @DisplayName("Создание уникального пользователя: есть accessToken")
    @Description("Проверяем, что ответ содержит accessToken")
    public void testCreateUniqueUserReturnsAccessToken() {
        Response response = UserEndpoints.createUser(testUser);
        assertNotNull(response.jsonPath().getString("accessToken"));
    }

    @Test
    @DisplayName("Создание уникального пользователя: есть refreshToken")
    @Description("Проверяем, что ответ содержит refreshToken")
    public void testCreateUniqueUserReturnsRefreshToken() {
        Response response = UserEndpoints.createUser(testUser);
        assertNotNull(response.jsonPath().getString("refreshToken"));
    }

    @Test
    @DisplayName("Создание существующего пользователя: статус 403")
    @Description("Проверяем, что создание существующего пользователя возвращает 403")
    public void testCreateExistingUserReturnsForbidden() {
        UserEndpoints.createUser(testUser);
        Response response = UserEndpoints.createUser(testUser);
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    @DisplayName("Создание существующего пользователя: success=false")
    @Description("Проверяем, что ответ содержит success=false")
    public void testCreateExistingUserReturnsSuccessFalse() {
        UserEndpoints.createUser(testUser);
        Response response = UserEndpoints.createUser(testUser);
        assertFalse(response.jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Создание существующего пользователя: сообщение об ошибке")
    @Description("Проверяем, что сообщение об ошибке корректно")
    public void testCreateExistingUserReturnsCorrectMessage() {
        UserEndpoints.createUser(testUser);
        Response response = UserEndpoints.createUser(testUser);
        assertEquals("User already exists", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создание пользователя без email: статус 403")
    @Description("Проверяем, что создание без email возвращает 403")
    public void testCreateUserWithoutEmailReturnsForbidden() {
        Response response = UserEndpoints.createUser(TestDataGenerator.generateUserWithoutEmail());
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    @DisplayName("Создание пользователя без email: сообщение об ошибке")
    @Description("Проверяем, что сообщение об ошибке корректно")
    public void testCreateUserWithoutEmailReturnsCorrectMessage() {
        Response response = UserEndpoints.createUser(TestDataGenerator.generateUserWithoutEmail());
        assertEquals("Email, password and name are required fields", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля: статус 403")
    @Description("Проверяем, что создание без пароля возвращает 403")
    public void testCreateUserWithoutPasswordReturnsForbidden() {
        Response response = UserEndpoints.createUser(TestDataGenerator.generateUserWithoutPassword());
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    @DisplayName("Создание пользователя без пароля: сообщение об ошибке")
    @Description("Проверяем, что сообщение об ошибке корректно")
    public void testCreateUserWithoutPasswordReturnsCorrectMessage() {
        Response response = UserEndpoints.createUser(TestDataGenerator.generateUserWithoutPassword());
        assertEquals("Email, password and name are required fields", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создание пользователя без имени: статус 403")
    @Description("Проверяем, что создание без имени возвращает 403")
    public void testCreateUserWithoutNameReturnsForbidden() {
        Response response = UserEndpoints.createUser(TestDataGenerator.generateUserWithoutName());
        assertEquals(SC_FORBIDDEN, response.statusCode());
    }

    @Test
    @DisplayName("Создание пользователя без имени: сообщение об ошибке")
    @Description("Проверяем, что сообщение об ошибке корректно")
    public void testCreateUserWithoutNameReturnsCorrectMessage() {
        Response response = UserEndpoints.createUser(TestDataGenerator.generateUserWithoutName());
        assertEquals("Email, password and name are required fields", response.jsonPath().getString("message"));
    }
}