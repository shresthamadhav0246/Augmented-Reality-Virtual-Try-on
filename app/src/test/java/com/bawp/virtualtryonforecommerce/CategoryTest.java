package com.bawp.virtualtryonforecommerce;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.bawp.virtualtryonforecommerce.model.Category;

public class CategoryTest {

    private Category category;

    @Before
    public void setUp() {
        // Initialize the Category object before each test
        category = new Category("Furniture", "furnitureImage.jpg");
    }

    @Test
    public void testConstructor() {
        // Test that the constructor correctly assigns the name and image
        assertEquals("Furniture", category.getName());
        assertEquals("furnitureImage.jpg", category.getImage());
    }

    @Test
    public void testSetName() {
        // Test setName method
        category.setName("Electronics");
        assertEquals("Electronics", category.getName());
    }

    @Test
    public void testSetImage() {
        // Test setImage method
        category.setImage("electronicsImage.jpg");
        assertEquals("electronicsImage.jpg", category.getImage());
    }
}
