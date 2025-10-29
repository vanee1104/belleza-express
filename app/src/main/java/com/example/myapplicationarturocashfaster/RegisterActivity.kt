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

class RegisterActivity : AppCompatActivity() {

    private lateinit var etIdentification: TextInputEditText
    private lateinit var etUsername: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView
    private lateinit var tvError: TextView

    // CORRECCIÓN: Separar Job del scope
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etIdentification = findViewById(R.id.etIdentification)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)
        tvError = findViewById(R.id.tvError)
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            registerUser()
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val identification = etIdentification.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (identification.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Por favor, completa todos los campos")
            return
        }

        if (password != confirmPassword) {
            showError("Las contraseñas no coinciden")
            return
        }

        if (password.length < 6) {
            showError("La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (!isValidEmail(email)) {
            showError("Por favor, ingresa un email válido")
            return
        }

        Log.d("RegisterActivity", "🔵 Iniciando proceso de registro...")

        btnRegister.isEnabled = false
        btnRegister.text = "Registrando..."

        scope.launch {
            try {
                Log.d("RegisterActivity", "🔵 Llamando a SupabaseManager.registerUser()")

                val result = SupabaseManager.registerUser(
                    identification = identification,
                    username = username,
                    email = email,
                    password = password
                )
                val success = result.first
                val message = result.second

                Log.d("RegisterActivity", "🔵 Resultado: success=$success, message=$message")

                btnRegister.isEnabled = true
                btnRegister.text = "Registrarse"

                if (success) {
                    showSuccess("✅ $message")
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showError("❌ $message")
                }
            } catch (e: Exception) {
                Log.e("RegisterActivity", "🔴 Error en corrutina: ${e.message}", e)
                btnRegister.isEnabled = true
                btnRegister.text = "Registrarse"
                showError("❌ Error: ${e.message}")
            }
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