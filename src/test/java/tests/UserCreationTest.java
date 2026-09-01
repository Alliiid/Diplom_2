package tests;

import endpoints.UserEndpoints;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.TestDataGenerator;

import static org.hamcrest.Matchers.*;

public class UserCreationTest {

    private User testUser;
    private String accessToken;

    @Before
    public void setUp() {
        testUser = TestDataGenerator.generateUniqueUser();
    }

    @After
    public void tearDown() {
        accessToken = null;
    }

    @Test
    public void createUniqueUserSuccess() {
        Response response = UserEndpoints.createUser(testUser);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    public void createExistingUserFails() {
        UserEndpoints.createUser(testUser);

        Response response = UserEndpoints.createUser(testUser);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void createUserWithoutEmailFails() {
        User userWithoutEmail = TestDataGenerator.generateUserWithoutEmail();

        Response response = UserEndpoints.createUser(userWithoutEmail);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutPasswordFails() {
        User userWithoutPassword = TestDataGenerator.generateUserWithoutPassword();

        Response response = UserEndpoints.createUser(userWithoutPassword);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutNameFails() {
        User userWithoutName = TestDataGenerator.generateUserWithoutName();

        Response response = UserEndpoints.createUser(userWithoutName);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}