package com.bawp.virtualtryonforecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameEditText, surnameEditText, emailEditText, phoneNumberEditText, passwordEditText, repeatPasswordEditText;
    private Button signUpButton;
    private TextView alreadyHaveAccountPrompt;
    private ImageView logoImageView, showHidePasswordImageView, repeatShowHidePasswordImageView;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        firstNameEditText = findViewById(R.id.firstName);
        surnameEditText = findViewById(R.id.surname);
        emailEditText = findViewById(R.id.email);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        passwordEditText = findViewById(R.id.password);
        repeatPasswordEditText = findViewById(R.id.repeatPassword);
        signUpButton = findViewById(R.id.signupButton);
        alreadyHaveAccountPrompt = findViewById(R.id.alreadyHaveAccountPrompt);

        showHidePasswordImageView = findViewById(R.id.showHidePassword);
        repeatShowHidePasswordImageView = findViewById(R.id.repeatShowHidePassword);

        // Toggle for the first password field
        showHidePasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    // Show Password
//                    showHidePasswordImageView.setImageResource(R.drawable.hide); // Change this to the 'hide' icon
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // Hide Password
                    showHidePasswordImageView.setImageResource(R.drawable.show); // Change this to the 'show' icon
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        // Toggle for the repeat password field
        repeatShowHidePasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatPasswordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    // Show Password
//                    repeatShowHidePasswordImageView.setImageResource(R.drawable.hide); // Change this to the 'hide' icon
                    repeatPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // Hide Password
                    repeatShowHidePasswordImageView.setImageResource(R.drawable.show); // Change this to the 'show' icon
                    repeatPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String firstName = firstNameEditText.getText().toString().trim();
                String surname = surnameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String repeatPassword = repeatPasswordEditText.getText().toString();

                // Validate the inputs
                if (TextUtils.isEmpty(firstName)) {
                    firstNameEditText.setError("First name is required.");
                    return;
                }

                if (TextUtils.isEmpty(surname)) {
                    surnameEditText.setError("Surname is required.");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is required.");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Please enter a valid email address.");
                    return;
                }

                if (TextUtils.isEmpty(phoneNumber)) {
                    phoneNumberEditText.setError("Phone number is required.");
                    return;
                }

                if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
                    phoneNumberEditText.setError("Please enter a valid phone number.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is required.");
                    return;
                }

                if (password.length() < 6) {
                    passwordEditText.setError("Password must be at least 6 characters.");
                    return;
                }

                if (!password.equals(repeatPassword)) {
                    repeatPasswordEditText.setError("Passwords do not match.");
                    return;
                }

                // Check if passwords match
                if (!password.equals(repeatPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user object
                User user = new User(firstName, surname, email, phoneNumber, password,"user");

                // Add user to Firestore
                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(SignupActivity.this, "User signed up successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(SignupActivity.this, "Error signing up", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Set up the prompt to switch to the Login activity if the user already has an account
        alreadyHaveAccountPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
