package tests;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.TestDataGenerator;

import static org.hamcrest.Matchers.*;

public class UserLoginTest {

    private User testUser;
    private String accessToken;

    @Before
    public void setUp() {
        testUser = TestDataGenerator.generateUniqueUser();
        Response createResponse = UserEndpoints.createUser(testUser);
        accessToken = createResponse.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        accessToken = null;
    }

    @Test
    public void loginExistingUserSuccess() {
        Response response = UserEndpoints.loginUser(testUser);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()));
    }

    @Test
    public void loginWithInvalidCredentialsFails() {
        User invalidUser = TestDataGenerator.generateInvalidUser();

        Response response = UserEndpoints.loginUser(invalidUser);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithWrongPasswordFails() {
        User userWithWrongPassword = User.builder()
                .email(testUser.getEmail())
                .password("WrongPassword123")
                .name(testUser.getName())
                .build();

        Response response = UserEndpoints.loginUser(userWithWrongPassword);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithoutPasswordFails() {
        User userWithoutPassword = User.builder()
                .email(testUser.getEmail())
                .password(null)
                .name(testUser.getName())
                .build();

        Response response = UserEndpoints.loginUser(userWithoutPassword);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
