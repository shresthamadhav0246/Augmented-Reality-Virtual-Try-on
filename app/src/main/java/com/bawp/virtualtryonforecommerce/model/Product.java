package com.bawp.virtualtryonforecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Product implements Parcelable {
    private String productName;
    private String productDescription;
    private double productPrice;
    private String productCategory;
    private List<String> productImages;
    private String productModel;
    private int stockQuantity;

    public Product(String productName, String productDescription, double productPrice, String productCategory, List<String> productImages, String productModel, int stockQuantity) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productImages = productImages;
        this.productModel = productModel;
        this.stockQuantity = stockQuantity;
    }
    public Product() {
        // Default constructor required for Firebase
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public List<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<String> productImages) {
        this.productImages = productImages;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Parcelable implementation
    protected Product(Parcel in) {
        productName = in.readString();
        productDescription = in.readString();
        productPrice = in.readDouble();
        productCategory = in.readString();
        productImages = in.createStringArrayList();
        productModel = in.readString();
        stockQuantity = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(productDescription);
        dest.writeDouble(productPrice);
        dest.writeString(productCategory);
        dest.writeStringList(productImages);
        dest.writeString(productModel);
        dest.writeInt(stockQuantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
