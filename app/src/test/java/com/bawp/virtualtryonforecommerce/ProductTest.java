package com.bawp.virtualtryonforecommerce;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.os.Parcel;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import com.bawp.virtualtryonforecommerce.model.Product;

@RunWith(RobolectricTestRunner.class)
public class ProductTest {

    private Product product;
    private final List<String> sampleImages = Arrays.asList("image1.jpg", "image2.jpg");

    @Before
    public void setUp() {
        product = new Product("Chair", "Comfortable chair", 59.99, "Furniture", sampleImages, "modelPath.glb", 10);
    }

    @Test
    public void testParcelable() {
        // Write the product to a parcel
        Parcel parcel = Parcel.obtain();
        product.writeToParcel(parcel, product.describeContents());

        // After you're done with writing, you need to reset the parcel for reading
        parcel.setDataPosition(0);

        // Create an instance of the product using CREATOR
        Product createdFromParcel = Product.CREATOR.createFromParcel(parcel);

        // Verify that the information is the same
        assertEquals(product.getProductName(), createdFromParcel.getProductName());
        assertEquals(product.getProductDescription(), createdFromParcel.getProductDescription());
        assertEquals(product.getProductPrice(), createdFromParcel.getProductPrice(), 0.0);
        assertEquals(product.getProductCategory(), createdFromParcel.getProductCategory());
        assertEquals(product.getProductImages(), createdFromParcel.getProductImages());
        assertEquals(product.getProductModel(), createdFromParcel.getProductModel());
        assertEquals(product.getStockQuantity(), createdFromParcel.getStockQuantity());

        parcel.recycle();
    }

    @Test
    public void testSettersAndGetters() {
        // Test setters
        product.setProductName("Table");
        assertEquals("Table", product.getProductName());

        product.setProductDescription("Wooden table");
        assertEquals("Wooden table", product.getProductDescription());

        product.setProductPrice(120.0);
        assertEquals(120.0, product.getProductPrice(), 0.0);

        product.setProductCategory("Home Furniture");
        assertEquals("Home Furniture", product.getProductCategory());

        List<String> newImages = Arrays.asList("table1.jpg", "table2.jpg");
        product.setProductImages(newImages);
        assertEquals(newImages, product.getProductImages());

        product.setProductModel("tableModel.glb");
        assertEquals("tableModel.glb", product.getProductModel());

        product.setStockQuantity(5);
        assertEquals(5, product.getStockQuantity());
    }
}

