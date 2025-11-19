package com.superstore;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new EndUser("test", "pass", "Test User");
        assertEquals("test", user.getLoginId());
        assertEquals("pass", user.getPassword());
        assertEquals("Test User", user.getName());
        assertEquals(User.UserType.END_USER, user.getUserType());
    }

    @Test
    public void testSuperUserCreation() {
        SuperUser superUser = new SuperUser("super", "pass", "Super");
        assertEquals(User.UserType.SUPER_USER, superUser.getUserType());
    }
}
