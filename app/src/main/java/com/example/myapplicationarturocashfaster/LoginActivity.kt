package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvError: TextView

    // CORRECCIÓN: Separar Job del scope
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        setupListeners()

        // Verificar si ya hay una sesión activa
        checkExistingSession()
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvError = findViewById(R.id.tvError)
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            loginUser()
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showError("Por favor, completa todos los campos")
            return
        }

        if (!isValidEmail(email)) {
            showError("Por favor, ingresa un email válido")
            return
        }

        Log.d("LoginActivity", "🔵 Iniciando proceso de login...")

        btnLogin.isEnabled = false
        btnLogin.text = "Iniciando sesión..."

        scope.launch {
            try {
                Log.d("LoginActivity", "🔵 Llamando a SupabaseManager.loginUser()")

                val result = SupabaseManager.loginUser(email, password)
                val success = result.first
                val message = result.second

                Log.d("LoginActivity", "🔵 Resultado: success=$success, message=$message")

                btnLogin.isEnabled = true
                btnLogin.text = "Iniciar Sesión"

                if (success) {
                    showSuccess("✅ $message")
                    // Extraer username del email (o modificar según tu lógica)
                    val username = email.substringBefore("@")
                    saveUserSession(username, email)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showError("❌ $message")
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "🔴 Error en corrutina: ${e.message}", e)
                btnLogin.isEnabled = true
                btnLogin.text = "Iniciar Sesión"
                showError("❌ Error: ${e.message}")
            }
        }
    }

    private fun saveUserSession(username: String, email: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", true)
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putLong("login_time", System.currentTimeMillis())
        editor.apply()
        Log.d("LoginActivity", "✅ Sesión guardada para: $username")
    }

    private fun checkExistingSession() {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            val username = sharedPreferences.getString("username", "")
            val email = sharedPreferences.getString("email", "")
            Log.d("LoginActivity", "🔵 Sesión existente encontrada para: $username")

            // Opcional: Redirigir automáticamente si ya está logueado
            // val intent = Intent(this@LoginActivity, MainActivity::class.java)
            // startActivity(intent)
            // finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = TextView.VISIBLE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccess(message: String) {
        tvError.visibility = TextView.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // CORRECCIÓN: Cancelar el job, no el scope
        job.cancel()
    }
}