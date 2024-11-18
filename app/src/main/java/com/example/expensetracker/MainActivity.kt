package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_main) // Make sure this matches your XML filename
            // Initialize views
            val imageView = findViewById<ImageView>(R.id.imageView2)
            val textView = findViewById<TextView>(R.id.textView)
            val getStartedButton = findViewById<Button>(R.id.getstartedButton)
            val loginTextView = findViewById<TextView>(R.id.loginTextView)
            // Set up click listeners
            getStartedButton.setOnClickListener {
                startActivity(Intent(this, SignUp::class.java)) // Replace NextActivity with your desired activity
            }
        loginTextView.setOnClickListener {
            startActivity(Intent(this, Login::class.java)) // Ensure Login::class.java points to your actual login activity
        }


        // Optional: Customize ImageView or TextViews if needed
            imageView.setImageResource(R.drawable.spalshscreen) // Make sure the drawable resource exists
            textView.text = getString(R.string.spend_smarter_nsave_more)
            getStartedButton.text = getString(R.string.get_started)
            loginTextView.text = getString(R.string.already_have_account_log_in)
        }
    }