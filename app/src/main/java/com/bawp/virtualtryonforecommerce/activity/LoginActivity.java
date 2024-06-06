package com.bawp.virtualtryonforecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.database.FirebaseInitializer;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseFirestore db;
    private TextView forgotPasswordTextView, signupPromptTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseInitializer.initialize(this);
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordTextView = findViewById(R.id.forgotPassword);
        signupPromptTextView = findViewById(R.id.signupPrompt);

        loginButton.setOnClickListener(view -> loginUser());

        signupPromptTextView.setOnClickListener(view -> {
            // Navigate to the SignUpActivity
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        forgotPasswordTextView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(intent);
            // Handle forgot password
            // You can navigate to a new ForgotPasswordActivity or use Firebase to send a reset email
        });

        loginButton.setOnClickListener(v -> loginUser());
    }

    public void loginUser() {
        showLoader();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password) // Consider using a more secure authentication method
                .get()
                .addOnCompleteListener(task -> {

                    hideLoader();

                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Extract user type from the user's document
                        String userType = task.getResult().getDocuments().get(0).getString("userType");
                        switch (userType) {
                            case "admin":
                                Toast.makeText(LoginActivity.this, "Admin login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                break;
                            case "user":
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, "Login failed. Invalid user type.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed. Check your credentials or network connection.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoader() {
        // Show loader (ProgressBar or any other loading indicator)
        ProgressBar loader = findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        // Hide loader (ProgressBar or any other loading indicator)
        ProgressBar loader = findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
    }

}
