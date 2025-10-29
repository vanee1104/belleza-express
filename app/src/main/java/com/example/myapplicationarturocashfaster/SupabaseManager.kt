package com.example.myapplicationarturocashfaster

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest

object SupabaseManager {

    private const val SUPABASE_URL = "https://xokekkxfphiaylypxbbi.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inhva2Vra3hmcGhpYXlseXB4YmJpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjE2NjE3ODUsImV4cCI6MjA3NzIzNzc4NX0.Tc0tAQ4B1KAkmXwKaHj4t3PxYBd_hwqz0NsoPu2sHWE"

    private val JSON_MEDIA_TYPE = "application/json".toMediaType()

    // ✅ CLIENTE SEGURO - usa verificación SSL estándar
    private val client = OkHttpClient.Builder()
        .build()

    suspend fun registerUser(
        identification: String,
        username: String,
        email: String,
        password: String,
        tipoUsuario: String = "normal"
    ): Pair<Boolean, String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SupabaseManager", "🟡 Iniciando registro para: $email")

                // Crear JSON para insertar
                val userData = JSONObject().apply {
                    put("identificacion", identification)
                    put("nombre_usuario", username)
                    put("email", email)
                    put("contrasena", hashPassword(password))
                    put("tipo_usuario", tipoUsuario)
                    put("activo", true)
                }

                val jsonArray = JSONArray().apply {
                    put(userData)
                }

                Log.d("SupabaseManager", "🟡 JSON a enviar: ${userData.toString()}")

                // Crear request
                val request = Request.Builder()
                    .url("$SUPABASE_URL/rest/v1/usuarios")
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Authorization", "Bearer $SUPABASE_KEY")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .post(jsonArray.toString().toRequestBody(JSON_MEDIA_TYPE))
                    .build()

                Log.d("SupabaseManager", "🟡 Enviando request a Supabase...")

                // Ejecutar request
                val response = client.newCall(request).execute()

                Log.d("SupabaseManager", "🟡 Respuesta HTTP: ${response.code} - ${response.message}")

                if (response.isSuccessful) {
                    Log.d("SupabaseManager", "✅ Registro exitoso para: $username")
                    Pair(true, "Usuario registrado exitosamente")
                } else {
                    val errorBody = response.body?.string() ?: "Sin detalles"
                    Log.e("SupabaseManager", "❌ Error HTTP ${response.code}: $errorBody")

                    when (response.code) {
                        409 -> Pair(false, "El usuario o email ya existen")
                        400 -> Pair(false, "Datos inválidos enviados")
                        401 -> Pair(false, "Error de autenticación con Supabase")
                        else -> Pair(false, "Error del servidor (${response.code}): $errorBody")
                    }
                }

            } catch (e: Exception) {
                Log.e("SupabaseManager", "❌ Error de conexión: ${e.message}")
                Pair(false, "Error de conexión: ${e.message}")
            }
        }
    }

    suspend fun loginUser(email: String, password: String): Pair<Boolean, String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SupabaseManager", "🟡 Iniciando login para: $email")

                // Buscar usuario por email y contraseña
                val request = Request.Builder()
                    .url("$SUPABASE_URL/rest/v1/usuarios?email=eq.$email&contrasena=eq.${hashPassword(password)}&select=*")
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Authorization", "Bearer $SUPABASE_KEY")
                    .build()

                val response = client.newCall(request).execute()

                Log.d("SupabaseManager", "🟡 Respuesta login HTTP: ${response.code} - ${response.message}")

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("SupabaseManager", "🟡 Respuesta body: $responseBody")

                    if (responseBody != null && responseBody != "[]" && responseBody.isNotEmpty()) {
                        Log.d("SupabaseManager", "✅ Login exitoso para: $email")
                        Pair(true, "Login exitoso")
                    } else {
                        Log.d("SupabaseManager", "❌ Credenciales incorrectas para: $email")
                        Pair(false, "Email o contraseña incorrectos")
                    }
                } else {
                    val errorBody = response.body?.string() ?: "Sin detalles"
                    Log.e("SupabaseManager", "❌ Error HTTP en login ${response.code}: $errorBody")
                    Pair(false, "Error del servidor: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e("SupabaseManager", "❌ Error en login: ${e.message}")
                Pair(false, "Error de conexión: ${e.message}")
            }
        }
    }

    private fun hashPassword(password: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val bytes = md.digest(password.toByteArray())
            bytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e("SupabaseManager", "Error hashing password: ${e.message}")
            password // Fallback simple
        }
    }
}