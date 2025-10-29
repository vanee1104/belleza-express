package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvDashboardWelcome: TextView
    private lateinit var tvDashboardUserName: TextView
    private lateinit var tvDashboardUserEmail: TextView
    private lateinit var tvBookingsCount: TextView
    private lateinit var tvServicesCount: TextView
    private lateinit var btnBookService: Button
    private lateinit var btnMyBookings: Button
    private lateinit var btnProfile: ImageButton
    private lateinit var recentActivityRecyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var activityAdapter: ActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            initViews()
            setupSharedPreferences()
            setupUserInfo()
            setupListeners()
            setupRecentActivity()
            loadUserProfileImage()

            Log.d("DEBUG", "✅ Dashboard cargado exitosamente")

        } catch (e: Exception) {
            Log.e("DEBUG", "❌ ERROR: ${e.message}", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initViews() {
        tvDashboardWelcome = findViewById(R.id.tvDashboardWelcome)
        tvDashboardUserName = findViewById(R.id.tvDashboardUserName)
        tvDashboardUserEmail = findViewById(R.id.tvDashboardUserEmail)
        tvBookingsCount = findViewById(R.id.tvBookingsCount)
        tvServicesCount = findViewById(R.id.tvServicesCount)
        btnBookService = findViewById(R.id.btnBookService)
        btnMyBookings = findViewById(R.id.btnMyBookings)
        btnProfile = findViewById(R.id.btnProfile)
        recentActivityRecyclerView = findViewById(R.id.recentActivityRecyclerView)
    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
    }

    private fun setupUserInfo() {
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        val username = sharedPreferences.getString("username", "")
        val email = sharedPreferences.getString("email", "")

        if (isLoggedIn && !username.isNullOrEmpty()) {
            // Usuario logueado - mostrar información personalizada
            val formattedUsername = username.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }

            tvDashboardUserName.text = formattedUsername
            tvDashboardUserEmail.text = email
            tvDashboardWelcome.text = "Welcome Back, $formattedUsername! 👋"

            // Mostrar estadísticas (datos de ejemplo)
            tvBookingsCount.text = "2"
            tvServicesCount.text = "5" // Número fijo en lugar de Service.getSampleServices().size

        } else {
            // Usuario no logueado - redirigir a login
            Toast.makeText(this, "Please login to access dashboard", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadUserProfileImage() {
        // Imagen de perfil genérica
        val profileImageUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80"

        Glide.with(this)
            .load(profileImageUrl)
            .placeholder(android.R.drawable.ic_menu_myplaces)
            .error(android.R.drawable.ic_menu_myplaces)
            .circleCrop()
            .into(btnProfile)
    }

    private fun setupListeners() {
        btnBookService.setOnClickListener {
            val intent = Intent(this, ServiceDetailActivity::class.java)
            startActivity(intent)
        }

        btnMyBookings.setOnClickListener {
            val intent = Intent(this, BookingsActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        // Bottom Navigation
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        val btnServices = findViewById<ImageButton>(R.id.btnServices)
        val btnBookings = findViewById<ImageButton>(R.id.btnBookings)
        val btnProfileNav = findViewById<ImageButton>(R.id.btnProfileNav)

        // Home ya está activo
        btnHome.setColorFilter(resources.getColor(android.R.color.holo_green_dark, theme))

        btnServices.setOnClickListener {
            val intent = Intent(this, ServiceDetailActivity::class.java)
            startActivity(intent)
        }

        btnBookings.setOnClickListener {
            val intent = Intent(this, BookingsActivity::class.java)
            startActivity(intent)
        }

        btnProfileNav.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecentActivity() {
        val recentActivities = createSampleActivities()
        activityAdapter = ActivityAdapter(recentActivities)

        recentActivityRecyclerView.layoutManager = LinearLayoutManager(this)
        recentActivityRecyclerView.adapter = activityAdapter

        Log.d("DEBUG", "✅ Actividad reciente configurada con ${recentActivities.size} items")
    }

    private fun createSampleActivities(): List<ActivityItem> {
        return listOf(
            ActivityItem(
                icon = android.R.drawable.ic_menu_edit,
                title = "Service Consultation Booked",
                description = "Architectural Design service",
                time = "2 hours ago",
                type = ActivityType.BOOKING
            ),
            ActivityItem(
                icon = android.R.drawable.ic_menu_save,
                title = "Profile Updated",
                description = "Your profile information was updated",
                time = "1 day ago",
                type = ActivityType.PROFILE
            ),
            ActivityItem(
                icon = android.R.drawable.ic_dialog_info,
                title = "Welcome to ArquiTech!",
                description = "Your account was successfully created",
                time = "2 days ago",
                type = ActivityType.SYSTEM
            )
        )
    }

    override fun onResume() {
        super.onResume()
        setupUserInfo() // Actualizar info al volver
        updateBottomNavigation()
    }

    private fun updateBottomNavigation() {
        // Resetear todos los colores
        val buttons = listOf(
            findViewById<ImageButton>(R.id.btnHome),
            findViewById<ImageButton>(R.id.btnServices),
            findViewById<ImageButton>(R.id.btnBookings),
            findViewById<ImageButton>(R.id.btnProfileNav)
        )

        buttons.forEach { button ->
            button.setColorFilter(resources.getColor(android.R.color.darker_gray, theme))
        }

        // Marcar Home como activo
        findViewById<ImageButton>(R.id.btnHome).setColorFilter(
            resources.getColor(android.R.color.holo_green_dark, theme)
        )
    }
}