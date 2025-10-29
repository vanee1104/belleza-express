package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserDashboardActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView
    private lateinit var btnServices: Button
    private lateinit var btnProfile: Button
    private lateinit var btnBookings: Button
    private lateinit var btnLogout: Button

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        initViews()
        setupListeners()
        loadUserData()
    }

    private fun initViews() {
        welcomeText = findViewById(R.id.welcomeText)
        btnServices = findViewById(R.id.btnServices)
        btnProfile = findViewById(R.id.btnProfile)
        btnBookings = findViewById(R.id.btnBookings)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun setupListeners() {
        btnServices.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnProfile.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        btnBookings.setOnClickListener {
            startActivity(Intent(this, BookingsActivity::class.java))
        }

        btnLogout.setOnClickListener {
            // Clear any session data and go to login
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserData() {
        // Load user data from shared preferences or intent
        val userName = intent.getStringExtra("user_name") ?: "Usuario"
        welcomeText.text = "Bienvenido, $userName"

        // You can also load additional user data from Supabase here
        scope.launch {
            try {
                // Example: Load user bookings count
                // val bookingsCount = loadBookingsCount()
                // runOnUiThread { updateBookingsBadge(bookingsCount) }
            } catch (e: Exception) {
                // Handle error silently or show toast
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}