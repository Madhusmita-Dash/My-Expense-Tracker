package com.example.expensetracker


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    // Declare FirebaseAuth instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Get references to views
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerTextView = findViewById<TextView>(R.id.registerTextView)
        val forgotPasswordTextView = findViewById<TextView>(R.id.forgotPasswordTextView)

        // Set click listener for the login button
        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate inputs
            if (TextUtils.isEmpty(email)) {
                usernameEditText.error = "Email is required."
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.error = "Password is required."
                return@setOnClickListener
            }

            // Login with Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login successful, navigate to HomePageActivity
                        val homeIntent = Intent(this, Homepage::class.java)
                        startActivity(homeIntent)
                        finish() // Close the login activity
                    } else {
                        // Login failed, show error message
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // Set click listener for the "Register" text, navigate to SignUpActivity
        registerTextView.setOnClickListener {
            val signUpIntent = Intent(this, SignUp::class.java)
            startActivity(signUpIntent)
        }

        // Set click listener for the "Forgot password?" text
        forgotPasswordTextView.setOnClickListener {
            // Handle forgot password (e.g., navigate to a ForgotPasswordActivity or reset password flow)
            Toast.makeText(this, "Forgot password clicked.", Toast.LENGTH_SHORT).show()
        }
    }
}
