package com.bawp.virtualtryonforecommerce.model;

public class User {

    private String firstName;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password; // Note: Storing passwords in plaintext is not secure.
    private String userType;

    // Default constructor is required for Firebase
    public User() {
    }

    // Constructor with parameters
    public User(String firstName, String surname, String email, String phoneNumber, String password, String userType) {
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password; // Note: You should not store the password in plaintext.
        this.userType = userType;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // Remember, don't store or transmit passwords in plaintext.
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
