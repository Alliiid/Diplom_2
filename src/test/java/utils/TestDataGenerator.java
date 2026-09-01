package utils;

import models.User;

import java.util.UUID;

public class TestDataGenerator {

    public static User generateUniqueUser() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return User.builder()
                .email("test_" + uniqueId + "@example.com")
                .password("Password123")
                .name("TestUser_" + uniqueId)
                .build();
    }

    public static User generateInvalidUser() {
        return User.builder()
                .email("invalid@example.com")
                .password("WrongPassword123")
                .name("InvalidUser")
                .build();
    }

    public static User generateUserWithoutEmail() {
        return User.builder()
                .email(null)
                .password("Password123")
                .name("TestUser")
                .build();
    }

    public static User generateUserWithoutPassword() {
        return User.builder()
                .email("test@example.com")
                .password(null)
                .name("TestUser")
                .build();
    }

    public static User generateUserWithoutName() {
        return User.builder()
                .email("test@example.com")
                .password("Password123")
                .name(null)
                .build();
    }
}
