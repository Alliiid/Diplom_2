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

public class UserLoginTest {

    private User testUser;
    private String accessToken;

    @Before
    @Step("Подготовка тестовых данных: создание пользователя")
    public void setUp() {
        testUser = TestDataGenerator.generateUniqueUser();
        Response createResponse = UserEndpoints.createUser(testUser);
        accessToken = createResponse.jsonPath().getString("accessToken");
    }

    @After
    @Step("Очистка: сброс accessToken")
    public void tearDown() {
        accessToken = null;
    }

    @Test
    @DisplayName("Логин существующего пользователя: статус 200")
    @Description("Проверяем, что логин существующего пользователя возвращает 200")
    public void testLoginExistingUserReturnsOk() {
        Response response = UserEndpoints.loginUser(testUser);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Логин существующего пользователя: success=true")
    @Description("Проверяем, что ответ содержит success=true")
    public void testLoginExistingUserReturnsSuccessTrue() {
        Response response = UserEndpoints.loginUser(testUser);
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Логин существующего пользователя: есть accessToken")
    @Description("Проверяем, что ответ содержит accessToken")
    public void testLoginExistingUserReturnsAccessToken() {
        Response response = UserEndpoints.loginUser(testUser);
        assertNotNull(response.jsonPath().getString("accessToken"));
    }

    @Test
    @DisplayName("Логин существующего пользователя: есть refreshToken")
    @Description("Проверяем, что ответ содержит refreshToken")
    public void testLoginExistingUserReturnsRefreshToken() {
        Response response = UserEndpoints.loginUser(testUser);
        assertNotNull(response.jsonPath().getString("refreshToken"));
    }

    @Test
    @DisplayName("Логин существующего пользователя: корректный email")
    @Description("Проверяем, что email в ответе совпадает с отправленным")
    public void testLoginExistingUserReturnsCorrectEmail() {
        Response response = UserEndpoints.loginUser(testUser);
        assertEquals(testUser.getEmail(), response.jsonPath().getString("user.email"));
    }

    @Test
    @DisplayName("Логин существующего пользователя: корректное имя")
    @Description("Проверяем, что имя в ответе совпадает с отправленным")
    public void testLoginExistingUserReturnsCorrectName() {
        Response response = UserEndpoints.loginUser(testUser);
        assertEquals(testUser.getName(), response.jsonPath().getString("user.name"));
    }

    @Test
    @DisplayName("Логин с неверными данными: статус 401")
    @Description("Проверяем, что логин с неверными данными возвращает 401")
    public void testLoginWithInvalidCredentialsReturnsUnauthorized() {
        Response response = UserEndpoints.loginUser(TestDataGenerator.generateInvalidUser());
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    @DisplayName("Логин с неверными данными: сообщение об ошибке")
    @Description("Проверяем, что сообщение об ошибке корректно")
    public void testLoginWithInvalidCredentialsReturnsCorrectMessage() {
        Response response = UserEndpoints.loginUser(TestDataGenerator.generateInvalidUser());
        assertEquals("email or password are incorrect", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Логин с неверным паролем: статус 401")
    @Description("Проверяем, что логин с неверным паролем возвращает 401")
    public void testLoginWithWrongPasswordReturnsUnauthorized() {
        User userWithWrongPassword = User.builder()
                .email(testUser.getEmail())
                .password("WrongPassword123")
                .name(testUser.getName())
                .build();
        Response response = UserEndpoints.loginUser(userWithWrongPassword);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }

    @Test
    @DisplayName("Логин без пароля: статус 401")
    @Description("Проверяем, что логин без пароля возвращает 401")
    public void testLoginWithoutPasswordReturnsUnauthorized() {
        User userWithoutPassword = User.builder()
                .email(testUser.getEmail())
                .password(null)
                .name(testUser.getName())
                .build();
        Response response = UserEndpoints.loginUser(userWithoutPassword);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
    }
}