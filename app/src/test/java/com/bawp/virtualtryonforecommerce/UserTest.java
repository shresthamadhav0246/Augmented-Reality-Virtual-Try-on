package com.bawp.virtualtryonforecommerce;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.bawp.virtualtryonforecommerce.model.User;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        // Initialize the User object before each test
        user = new User("John", "Doe", "john.doe@example.com", "1234567890", "password123", "admin");
    }

    @Test
    public void testConstructor() {
        // Test that the constructor correctly assigns all fields
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getSurname());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals("password123", user.getPassword());  // Note: Do not store passwords in plaintext in real applications
        assertEquals("admin", user.getUserType());
    }

    @Test
    public void testSetFirstName() {
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    public void testSetSurname() {
        user.setSurname("Smith");
        assertEquals("Smith", user.getSurname());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("jane.smith@example.com");
        assertEquals("jane.smith@example.com", user.getEmail());
    }

    @Test
    public void testSetPhoneNumber() {
        user.setPhoneNumber("0987654321");
        assertEquals("0987654321", user.getPhoneNumber());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newPassword123");  // Again, be cautious about how passwords are handled in actual code.
        assertEquals("newPassword123", user.getPassword());
    }

    @Test
    public void testSetUserType() {
        user.setUserType("user");
        assertEquals("user", user.getUserType());
    }
}

