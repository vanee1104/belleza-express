package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*

class UserProfileActivity : AppCompatActivity() {

    private lateinit var tvDashboardWelcome: TextView
    private lateinit var tvDashboardUserName: TextView
    private lateinit var tvDashboardUserEmail: TextView
    private lateinit var tvBookingsCount: TextView
    private lateinit var tvServicesCount: TextView
    private lateinit var btnBookService: Button
    private lateinit var btnMyBookings: Button
    private lateinit var btnLogout: ImageButton
    private lateinit var recentActivityRecyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var activityAdapter: ActivityAdapter

    // Variables para navegación
    private lateinit var cardServices: LinearLayout
    private lateinit var cardBookings: LinearLayout
    private lateinit var cardProfile: LinearLayout
    private lateinit var cardSettings: LinearLayout
    private lateinit var ivUserProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        try {
            initViews()
            setupSharedPreferences()
            setupUserInfo()
            setupListeners()
            setupNavigationCards()
            setupRecentActivity()
            loadUserProfileImage()

            Log.d("DEBUG", "✅ UserProfile cargado exitosamente")

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
        btnLogout = findViewById(R.id.btnLogout)
        recentActivityRecyclerView = findViewById(R.id.recentActivityRecyclerView)

        // Inicializar vistas de navegación
        ivUserProfile = findViewById(R.id.ivUserProfile)
        cardServices = findViewById(R.id.cardServices)
        cardBookings = findViewById(R.id.cardBookings)
        cardProfile = findViewById(R.id.cardProfile)
        cardSettings = findViewById(R.id.cardSettings)
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
            tvDashboardWelcome.text = getString(R.string.welcome_back, formattedUsername)

            // Mostrar estadísticas (datos de ejemplo)
            tvBookingsCount.text = "2"
            tvServicesCount.text = "5"

        } else {
            // Usuario no logueado - redirigir a login
            Toast.makeText(this, getString(R.string.please_login), Toast.LENGTH_SHORT).show()
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
            .into(ivUserProfile)
    }

    private fun setupListeners() {
        btnBookService.setOnClickListener {
            val intent = Intent(this, ServiceDetailActivity::class.java)
            startActivity(intent)
        }

        // ✅ CORREGIDO: btnMyBookings ahora va a ContactActivity
        btnMyBookings.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        // ✅ ELIMINADO: setupBottomNavigation() ya no existe
    }

    // ✅ CORREGIDO: Configurar tarjetas de navegación
    private fun setupNavigationCards() {
        cardServices.setOnClickListener {
            val intent = Intent(this, ServiceDetailActivity::class.java)
            startActivity(intent)
        }

        // ✅ CORREGIDO: Ahora va a ContactActivity
        cardBookings.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }

        // ✅ CORREGIDO: Ahora va a MainActivity (Landing)
        cardProfile.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra esta actividad
        }

        cardSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        ivUserProfile.setOnClickListener {
            // Ya estamos en el perfil, no hacer nada o mostrar mensaje
            Toast.makeText(this, "Ya estás en tu perfil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogoutConfirmation() {
        android.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.logout_confirmation_title))
            .setMessage(getString(R.string.logout_confirmation_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                logoutUser()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun logoutUser() {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Eliminar los datos de sesión del usuario
        editor.remove("is_logged_in")
        editor.remove("username")
        editor.remove("email")
        editor.remove("login_time")
        editor.apply()

        Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()

        // Redirigir al login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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
                title = getString(R.string.activity_service_booked),
                description = getString(R.string.activity_service_booked_desc),
                time = "2 hours ago",
                type = ActivityType.BOOKING
            ),
            ActivityItem(
                icon = android.R.drawable.ic_menu_save,
                title = getString(R.string.activity_profile_updated),
                description = getString(R.string.activity_profile_updated_desc),
                time = "1 day ago",
                type = ActivityType.PROFILE
            ),
            ActivityItem(
                icon = android.R.drawable.ic_dialog_info,
                title = getString(R.string.activity_welcome),
                description = getString(R.string.activity_welcome_desc),
                time = "2 days ago",
                type = ActivityType.SYSTEM
            )
        )
    }

    override fun onResume() {
        super.onResume()
        setupUserInfo() // Actualizar info al volver
    }
}